package com.neuo.glcommon;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.neuo.common.CommonEvent;
import com.neuo.common.CommonEvent.Event;
import com.neuo.common.ThreadUtil;
import com.neuo.common.WorkState;
import com.neuo.struct.BinarySortTree;

import android.annotation.SuppressLint;
import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class GlViewForDraw extends GLSurfaceView {
	private static final int pauseFrame = 50;
	private static final long pauseSleepTime = 1000 / pauseFrame;
	private static final long logInterval = 1000;
	private static final long maxFrame = 60;
	private long frame = maxFrame;
	private long minFrameTime = 1000 / maxFrame;
	private boolean isLog = false;
	private long lastTime = 0;
	private long frameCount = 0;
	private BinarySortTree<GlView> glViews;
	private SceneRenderer myRenderer;
	private long resizeId = 0;
	private long reinitId = 0;
	private int vWidth;
	private int vHeight;
	private WorkState state = WorkState.STOP;
	private boolean isMockPause;
	private boolean isMock;
	private Object mockLock;
	private Context context;
	private BinarySortTree<GlView>.TreeIterator drawIterator;
	private BinarySortTree<GlView>.TreeIterator runIterator;
	private BinarySortTree<GlView>.TreeIterator touchIterator;
	
	private EventDelegate eventDelegate;
	private class TouchControl implements 
	GestureDetector.OnGestureListener,
	GestureDetector.OnDoubleTapListener {
		private boolean isOver;
		private boolean isDouble;
		private boolean isUp;
		private boolean isDoubleMove;
		private GestureDetectorCompat detector;
		
		@SuppressLint("Recycle")
		private final MotionEvent cancleEvent = MotionEvent.obtain(0, 0, MotionEvent.ACTION_CANCEL, 0, 0, 0);
		
		public TouchControl() {
			detector = new GestureDetectorCompat(context, this);
			detector.setOnDoubleTapListener(this);
			resetTouch();
		}
		
		public void resetTouch() {
			isOver = true;
			detector.onTouchEvent(cancleEvent);
		}
		
		private boolean deal(MotionEvent e) {
			int action = e.getActionMasked();
			if (action == MotionEvent.ACTION_DOWN) {
				//Log.d("test", "action down");
				if (!isOver) {
					//Log.d("test", "not over, cancle");
					eventDelegate.deal(new Event(CommonEvent.Cancle));
					resetTouch();
				}
				isDouble = false;
				isOver = false;

				detector.onTouchEvent(e);
				if (isDouble) {
					eventDelegate.deal(new Event(CommonEvent.Double, e, true));
				} else {
					eventDelegate.deal(new Event(CommonEvent.Down, e, false));
				}
				return true;
			} else if (!isOver && MotionEvent.ACTION_UP == action) {
				isUp = true;
				detector.onTouchEvent(e);
				isOver = true;
				if (isUp) {
					eventDelegate.deal(new Event(CommonEvent.Up, e, isDouble));
				}
				return false;
			} else if (!isOver && MotionEvent.ACTION_MOVE == action) {
				isDoubleMove = false;
				detector.onTouchEvent(e);
				if (isDoubleMove) {
					eventDelegate.deal(new Event(CommonEvent.Move, e, true));
				}
				return false;
			} else if (!isOver && MotionEvent.ACTION_CANCEL == action) {
				//Log.d("test", "cancle");
				eventDelegate.deal(new Event(CommonEvent.Cancle));
				resetTouch();
				return false;
			} else if (!isOver) {
				detector.onTouchEvent(e);
				return false;
			} else {
				return false;
			}
		}
		
		@Override
		public boolean onDoubleTap(MotionEvent e) {
			isDouble = true;
			return true;
		}

		@Override
		public boolean onDoubleTapEvent(MotionEvent e) {
			if (e.getAction() == MotionEvent.ACTION_MOVE) {
				isDoubleMove = true;
			}
			return true;
		}

		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
			isUp = false;
			eventDelegate.deal(new Event(CommonEvent.SingleConfirm, e));
			return true;
		}

		@Override
		public boolean onDown(MotionEvent e) {
			//do nothing
			return true;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			isUp = false;
			eventDelegate.deal(new Event(CommonEvent.Fling, e2, isDouble,
														velocityX, -velocityY));
			return true;
		}

		@Override
		public void onLongPress(MotionEvent e) {
			eventDelegate.deal(new Event(CommonEvent.LongPress, e, isDouble));
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			eventDelegate.deal(new Event(CommonEvent.Scroll, e2, isDouble,
														-distanceX, distanceY));
			return true;
		}

		@Override
		public void onShowPress(MotionEvent e) {
			//do nothing
		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			//do nothing
			return true;
		}
	}

	private class EventDelegate extends CommonEvent {
		private GlView currTouchView;

		public EventDelegate() {
			super();
		}
		
		@Override
		public void clear() {
			super.clear();
			resetEvent();
		//	Log.d("test", "left event clear");
		}
		
		public void resetEvent() {
			if (currTouchView != null) {
				//Log.d("test", "curr view reset event");
				currTouchView.resetTouchState();
				currTouchView = null;
			}
		}

		public void removeView(GlView view) {
			if (currTouchView == view) {
				resetEvent();
			}
		}
		
		@Override
		protected void deal(Event e) {
			if (e.action == CommonEvent.Double || e.action == CommonEvent.Down) {
				if (e.action == CommonEvent.Double && currTouchView != null) {
				} else {
					currTouchView = null;
					for (touchIterator.clear(); touchIterator.hasNext();) {
						GlView view = touchIterator.next();
						if (view.getIsVisible() &&
								view.isHanderEvent(e) != null) {
							currTouchView = view;
							break;
						}
					}
				}
				if (currTouchView != null) {
					currTouchView.onHanderEvent(e);
				} else {
					//Log.d("test", "don't find view to deal touch");
				}
			} else if ((e.action & CommonEvent.KeyEvent) != 0) {
				GlView tmp = null;
				for (touchIterator.clear(); touchIterator.hasNext();) {
					GlView view = touchIterator.next();
					if (view.getIsVisible() &&
							view.isHanderEvent(e) != null) {
						tmp = view;
						break;
					}
				}
				if (tmp != null) {
					tmp.onHanderEvent(e);
				}
			} else if (currTouchView != null) {
				currTouchView.onHanderEvent(e);
			} else {
				//Log.d("test", "have no view to deal touch " + e.action);
			}
			e.clear();
		}	
	}
	
	private TouchControl touchControl;
	
	public GlViewForDraw(Context context) {
		super(context);
		this.context = context;
		touchControl = new TouchControl();
		myRenderer = new SceneRenderer();
		glViews = new BinarySortTree<GlView>();
		drawIterator = (BinarySortTree<GlView>.TreeIterator) glViews.iterator();
		runIterator = (BinarySortTree<GlView>.TreeIterator) glViews.iterator();
		touchIterator = (BinarySortTree<GlView>.TreeIterator) glViews.iterator(BinarySortTree.rightFirst);
		isMockPause = true;
		isMock = false;
		mockLock = new Object();
		mockPause();
		
		eventDelegate = new EventDelegate();
	}
	
	protected void setMaxFrame(long frame) {
		this.frame = frame;
		this.minFrameTime = 1000 / this.frame;
	}
	
	public void initGl() {
		this.setEGLContextClientVersion(2);
		setEGLConfigChooser(8, 8, 8, 8, 16, 8);
		setRenderer(myRenderer);
		setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
	}
	
	private void mockPause() {
		synchronized (mockLock) {
			isMockPause = true;
		}
	}
	
	private void mockResume() {
		synchronized (mockLock) {
			isMockPause = false;
		}
	}
	
	public void removeGlView(GlView other) {
		if (WorkState.RUN == state) {
			mockPause();
		}
		
		GlView tmp = glViews.remove(other);
		if (tmp != null) {
			eventDelegate.removeView(tmp);
			tmp.onPause();
			tmp.uninit();
		}
		
		if (WorkState.RUN == state) {
			mockResume();
		}
	}
	
	public void addGlView(GlView other, int viewId, boolean isRestart) {
		if (WorkState.RUN == state) {
			mockPause();
		}
		GlView tmp = glViews.remove(other);
		if (tmp != null && isRestart) {
			eventDelegate.removeView(tmp);
			tmp.onPause();
			tmp.uninit();
		}
		other.setViewId(viewId);
		if (tmp == null || isRestart) {
			other.init();
			other.onResume();
		}
		glViews.insert(other);
		
		if (WorkState.RUN == state) {
			mockResume();
		}
	}
	
	public void setGlView(GlView other, int viewId) {
		if (WorkState.RUN == state){
			mockPause();
		}
		eventDelegate.resetEvent();
		if (0 < glViews.getNodeCount()  && WorkState.STOP != state) {
			for (runIterator.clear(); runIterator.hasNext();) {
				GlView view = runIterator.next();
				view.onPause();
				view.uninit();
				runIterator.remove();
			}
		}
		if (null != other){
			other.setViewId(viewId);
			glViews.insert(other);
			other.init();
		}
		if (WorkState.RUN == state) {
			mockResume();
		}
	}
	
	public boolean onTouchEvent(MotionEvent e) {
		return touchControl.deal(e);
	}
	
	public boolean onKeyEvent(Event e) {
		this.eventDelegate.deal(e);
		return false;
	}
	
	public void go(long interval) {
		if (0 < glViews.getNodeCount()) {
			for (runIterator.clear(); runIterator.hasNext();) {
				runIterator.next().run(interval);
			}
		}
	}
	
	public void start() {
		if (WorkState.STOP == state){
			mockResume();
			state = WorkState.RUN;
		} else if (WorkState.PAUSE == state) {
			if (!this.isMock) {
				if (0 < glViews.getNodeCount()) {
					for (runIterator.clear(); runIterator.hasNext();) {
						runIterator.next().onResume();
					}
				}
				mockResume();
				super.onResume();
			} else {
				mockResume();
			}
			state = WorkState.RUN; 
		}
	}
	
	public void pause(boolean isMock) {
		if (WorkState.RUN == state){
			state = WorkState.PAUSE;
			this.isMock = isMock;
			if (!isMock) {
				super.onPause();
				mockPause();
				touchControl.resetTouch();
				eventDelegate.clear();
				
				if (0 < glViews.getNodeCount()) {
					for (runIterator.clear(); runIterator.hasNext();) {
						runIterator.next().onPause();
					}
				}
			} else {
				mockPause();
			}
		} else if (WorkState.PAUSE == state && !isMock && this.isMock) {
			this.isMock = false;
			super.onPause();
			touchControl.resetTouch();
			eventDelegate.clear();
			
			if (0 < glViews.getNodeCount()) {
				for (runIterator.clear(); runIterator.hasNext();) {
					runIterator.next().onPause();
				}
			}
		}
	}

	public void release() {
		if (WorkState.STOP != state) {
			state = WorkState.STOP;
			mockPause();
			
			touchControl.resetTouch();
			eventDelegate.clear();

			if (0 < glViews.getNodeCount()) {
				for (runIterator.clear(); runIterator.hasNext();) {
					GlView view = runIterator.next();
					view.onPause();
					view.uninit();
					runIterator.remove();
				}
			}
		}
	}
	
	private void setReinit() {
		reinitId ++;
	}
	
	private void setReSize(int width, int height) {
		resizeId ++;
		vHeight = height;
		vWidth = width;
	}
	
	private class SceneRenderer implements GLSurfaceView.Renderer {
		@Override
		public void onDrawFrame(GL10 gl) {
			boolean isPause = false;
			long currTime = System.currentTimeMillis();
			synchronized (mockLock) {
				if (isMockPause) {
					isPause = true;
				} else {
					GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
					if (0 < glViews.getNodeCount()) {
						for (drawIterator.clear(); drawIterator.hasNext();) {
							GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_STENCIL_BUFFER_BIT);
							GlView drawGlView = drawIterator.next();
							if (!drawGlView.getIsVisible()) {
								continue;
							}
							if (drawGlView.getReinitId() < reinitId) {
								drawGlView.reGlInit(reinitId);
							}
							if (drawGlView.getResizeId() < resizeId) {
								drawGlView.reScreenSize(resizeId, vWidth, vHeight);
							}
							drawGlView.onDraw();
						}
					}
				}
			}
			if (isPause) {
				ThreadUtil.sleepInterrupted(pauseSleepTime, true);
			} else {
				long thisTime = System.currentTimeMillis();
				if (thisTime - currTime < minFrameTime) {
					ThreadUtil.sleepInterrupted(minFrameTime - (thisTime - currTime), true);
				}
				frameCount++;
			}
			if (isLog) {
				if (currTime - lastTime > logInterval) {
					Log.d("gl frame", "frame " + (float)frameCount * 1000 / logInterval);
					lastTime = currTime;
					frameCount = 0;
				}
			}
		}

		@Override
		public void onSurfaceChanged(GL10 gl, int width, int height) {
			GLES20.glViewport(0, 0, width, height);
			setReSize(width, height);
		}

		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			setReinit();
		}
	}
}
