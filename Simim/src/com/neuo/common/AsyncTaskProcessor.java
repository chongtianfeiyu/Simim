package com.neuo.common;

import java.util.ArrayList;
import java.util.Iterator;

public class AsyncTaskProcessor{
	public static abstract class AsyncTask{
		public static final int NotStart 	= 1;
		public static final int Running 	= 2;
		public static final int End 		= 3;
		protected String taskName = "";
		protected int taskState = NotStart;
		protected ActionTriggle taskTriggle;
		public ActionTriggle getTaskTriggle() {
			return taskTriggle;
		}
		
		public AsyncTask() {
		}
		
		public void setName(String name) {
			this.taskName = name;
		}
		
		public AsyncTask(int triggleCount) {
			taskTriggle = new ActionTriggle(triggleCount);
		}
		
		protected void triggleTask() {
			if (null != taskTriggle) {
				taskTriggle.triggle();
			}
		}
		
		protected void triggleTask(int triggleCount) {
			if (null != taskTriggle) {
				taskTriggle.triggle(triggleCount, true);
			}
		}
		
		public int getTaskState() {
			return taskState;
		}
		
		public void goTask() {
			taskState = Running;
			runnable();
			taskState = End;
		}
		
		protected abstract void runnable();
	}
	
	protected WorkState state = WorkState.STOP;
	protected ArrayList<AsyncTask> taskArrayList;
	protected CommonThread asyncThread;
	protected boolean isGo = false;
	private static AsyncTaskProcessor asyncTaskProcessor;

	public static void release() {
		if (asyncTaskProcessor != null) {
			asyncTaskProcessor.stop();
			asyncTaskProcessor = null;
		}
	}

	public static synchronized AsyncTaskProcessor getAsyncTaskProcessor() {
		if (null == asyncTaskProcessor){
			asyncTaskProcessor = new AsyncTaskProcessor();
		}
		return asyncTaskProcessor;
	}
	
	public AsyncTaskProcessor() {
		state = WorkState.STOP;
	}

	protected AsyncTask waitAsyncTask() {
		AsyncTask resAsyncTask = null;
		synchronized (taskArrayList) {
			while (0 == taskArrayList.size() && !isGo){
				try {
					taskArrayList.wait();
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					return null;
				}
			}
			if (isGo)return null;
			resAsyncTask = taskArrayList.remove(0);
		}
		return resAsyncTask;
	}
	
	public synchronized void putAsyncTask(AsyncTask task, boolean isDup) {
		if (WorkState.STOP == state)return;
		synchronized (taskArrayList) {
			if (isDup) {
				for (Iterator<AsyncTask> iterator = taskArrayList.iterator(); iterator.hasNext();) {
					if (iterator.next().taskName.equals(task.taskName)) {
						return;
					}
				}
			}
			taskArrayList.add(task);
			if (taskArrayList.size() == 1){
				taskArrayList.notify();
			}
		}
	}
	
	public synchronized void stop() {
		if (WorkState.STOP != state) {
			state = WorkState.STOP;
			asyncThread.onStop();
			asyncThread = null;
			taskArrayList.clear();
			taskArrayList = null;
		}
	}
	
	public void pause() {
		if (WorkState.RUN == state) {
			state = WorkState.PAUSE;
			asyncThread.onPause();
		}
	}

	public synchronized void start() {
		if (WorkState.STOP == state) {
			state = WorkState.PAUSE;
			taskArrayList = new ArrayList<AsyncTaskProcessor.AsyncTask>();
			asyncThread = new CommonThread() {
				{
					setName("async process thread");
				}
				public void goNotify() {
					synchronized (taskArrayList) {
						isGo = true;
						taskArrayList.notifyAll();
					}
				}
				public void go() {
					AsyncTask task = waitAsyncTask();
					if (null == task) {
						isGo = false;
						return;
					}
					if (isGo) {
						putAsyncTask(task, false);
						isGo = false;
						return;
					}
					task.goTask();
				}
			};
			asyncThread.onStart(true);
		} else if (WorkState.PAUSE == state) {
			state = WorkState.RUN;
			asyncThread.onResume();
		}
	}
}
