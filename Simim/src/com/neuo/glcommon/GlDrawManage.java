package com.neuo.glcommon;

import com.neuo.struct.BinarySortTree;

public class GlDrawManage {
	public static interface CommonDraw {
		public float getSorValue();
		public void draw();
		public void onRemove(boolean isWait);
		public void onAdd();
		
		public void calcuMVPMatrix(GlCamera glCamera);
	}
	
	private static class DrawWrapper 
	implements Comparable<Object> {
		public float sortValue;
		public CommonDraw commonDraw;
		
		public DrawWrapper(CommonDraw commonDraw) {
			clear();
			this.commonDraw = commonDraw;
		}
		
		@SuppressWarnings("unused")
		public boolean isChange() {
			return this.sortValue != commonDraw.getSorValue();
		}
		
		public void setChange() {
			this.sortValue = commonDraw.getSorValue();
		}
		
		public void clear() {
			sortValue = 0f;
			commonDraw = null;
		}

		@Override
		public int compareTo(Object another) {
			DrawWrapper tmp = (DrawWrapper)another;
			float interval = this.sortValue - tmp.sortValue;
			if (interval > 0) {
				return 1;
			} else if (interval < 0) {
				return -1;
			} else {
				return 0;
			}
		}
	}
	
	private BinarySortTree<DrawWrapper> dealDrawTree;
	private BinarySortTree<DrawWrapper>.TreeIterator dealDrawIterator;
	
	private BinarySortTree<DrawWrapper> sortDrawTree;
	private BinarySortTree<DrawWrapper>.TreeIterator sortIterator;
	private BinarySortTree<DrawWrapper>.TreeIterator sortDealIterator;
	
	private BinarySortTree<DrawWrapper> addDrawTree;
	private BinarySortTree<DrawWrapper>.TreeIterator addIterator;
	
	private BinarySortTree<DrawWrapper> delDrawTree;
	private BinarySortTree<DrawWrapper>.TreeIterator delIterator;
	
	private GlCamera glCamera;
	private Object cameraLock = new Object();
	
	public void add(CommonDraw commonDraw, boolean atOnce) {
		synchronized (sortDrawTree) {
			for (delIterator.clear(); delIterator.hasNext();) {
				DrawWrapper tmp = delIterator.next();
				if (tmp.commonDraw == commonDraw) {
					delIterator.remove();
					return;
				}
			}
			DrawWrapper removeNode = null;
			for (addIterator.clear(); addIterator.hasNext();) {
				DrawWrapper tmp = addIterator.next();
				if (tmp.commonDraw == commonDraw) {
					if (atOnce) {
						removeNode = tmp;
						addIterator.remove();
						break;
					} else {
						return;
					}
				}
			}
			if (removeNode != null) {
				removeNode.commonDraw.onAdd();
				sortDrawTree.insert(removeNode);
			} else {
				for (sortDealIterator.clear(); sortDealIterator.hasNext();) {
					DrawWrapper tmp = sortDealIterator.next();
					if (tmp.commonDraw == commonDraw) {
						return;
					}
				}
				if (atOnce) {
					commonDraw.onAdd();
					sortDrawTree.insert(new DrawWrapper(commonDraw));
				} else {
					addDrawTree.insert(new DrawWrapper(commonDraw));
				}
			}
		}
	}
	
	public void clear() {
		addDrawTree.clear();
		delDrawTree.clear();
		sortDrawTree.clear();
	}
	
	public void remove(CommonDraw commonDraw, boolean atOnce) {
		synchronized (sortDrawTree) {
			DrawWrapper removeNode = null;
			for (delIterator.clear(); delIterator.hasNext();) {
				DrawWrapper tmp = delIterator.next();
				if (tmp.commonDraw == commonDraw) {
					if (atOnce) {
						removeNode = tmp;
						delIterator.remove();
						break;
					} else {
						return;
					}
				}
			}
			if (removeNode != null) {
				for (sortDealIterator.clear(); sortDealIterator.hasNext();) {
					DrawWrapper tmp = sortDealIterator.next();
					if (tmp.commonDraw == commonDraw) {
						tmp.commonDraw.onRemove(false);
						sortDealIterator.remove();
						return;
					}
				}
				throw new RuntimeException("can't go here");
			} else {
				for (addIterator.clear(); addIterator.hasNext();) {
					DrawWrapper tmp = addIterator.next();
					if (tmp.commonDraw == commonDraw) {
						tmp.commonDraw.onRemove(true);
						addIterator.remove();
						return;
					}
				}
				for (sortDealIterator.clear(); sortDealIterator.hasNext();) {
					DrawWrapper tmp = sortDealIterator.next();
					if (tmp.commonDraw == commonDraw) {
						if (atOnce) {
							tmp.commonDraw.onRemove(false);
							sortDealIterator.remove();
						} else {
							delDrawTree.insert(new DrawWrapper(commonDraw));
						}
						return;
					}
				}
			}
		}
	}
	
	public void draw() {
		updateSelf();
		updateSort();
		for (dealDrawIterator.clear(); dealDrawIterator.hasNext();) {
			DrawWrapper tmp = dealDrawIterator.next();
			tmp.commonDraw.draw();
		}
		dealDrawTree.clear();
	}
	
	public void setGlCamera(GlCamera glCamera) {
		synchronized (cameraLock) {
			this.glCamera = glCamera;
		}
	}
	
	private GlCamera updateCamera() {
		synchronized (cameraLock) {
			if (glCamera == null) return null;
			glCamera.confirmAll();
			glCamera.update();
			return glCamera;
		}
	}
	
	private void updateSort() {
		GlCamera tmpCamera = updateCamera();
		for (sortIterator.clear(); sortIterator.hasNext();) {
			DrawWrapper tmp = sortIterator.next();
			if (tmpCamera != null) {
				tmp.commonDraw.calcuMVPMatrix(tmpCamera);
			}
			tmp.setChange();
			dealDrawTree.insert(tmp);
		}
	}

	private void updateSelf() {
		synchronized (sortDrawTree) {
			if (delDrawTree.getNodeCount() > 0) {
				for (delIterator.clear(); delIterator.hasNext();) {
					DrawWrapper delNode = delIterator.next();
					for (sortDealIterator.clear(); sortDealIterator.hasNext();) {
						DrawWrapper tmp = sortDealIterator.next();
						if (tmp.commonDraw == delNode.commonDraw) {
							tmp.commonDraw.onRemove(false);
							sortDealIterator.remove();
							break;
						}
					}
				}
				delDrawTree.clear();
			}
			if (addDrawTree.getNodeCount() > 0) {
				for (addIterator.clear(); addIterator.hasNext();) {
					DrawWrapper addNode = addIterator.next();
					addNode.commonDraw.onAdd();
					sortDrawTree.insert(addNode);
				}
				addDrawTree.clear();
			}
		}
	}
	
	public GlDrawManage() {
		init();
	}
	
	private void init() {
		sortDrawTree = new BinarySortTree<DrawWrapper>();
		sortIterator = (BinarySortTree<DrawWrapper>.TreeIterator)sortDrawTree.iterator(BinarySortTree.midFirst);
		sortDealIterator = (BinarySortTree<DrawWrapper>.TreeIterator)sortDrawTree.iterator(BinarySortTree.midFirst);
		
		dealDrawTree = new BinarySortTree<DrawWrapper>();
		dealDrawIterator = (BinarySortTree<DrawWrapper>.TreeIterator)dealDrawTree.iterator();

		addDrawTree = new BinarySortTree<DrawWrapper>();
		addIterator = (BinarySortTree<DrawWrapper>.TreeIterator)addDrawTree.iterator(BinarySortTree.midFirst);
		
		delDrawTree = new BinarySortTree<DrawWrapper>();
		delIterator = (BinarySortTree<DrawWrapper>.TreeIterator)delDrawTree.iterator(BinarySortTree.midFirst);
	}
}
