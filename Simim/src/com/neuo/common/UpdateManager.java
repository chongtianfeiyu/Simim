package com.neuo.common;

import com.neuo.struct.BinarySortTree;

public class UpdateManager {
	public static interface CommonUpdate {
		public void update();
	}
	
	public static interface CommonCalcu {
		public void calcu(long interval);
	}
	
	private static class UpdateWrapper 
	implements Comparable<Object> {
		public CommonUpdate update;
		public CommonCalcu calcu;
		public boolean isCalcu;
		public boolean isOnce;
		public boolean isNeedDel;
		
		public UpdateWrapper(CommonCalcu calcu, CommonUpdate update, boolean isCalcu, boolean isOnce) {
			if (isCalcu) {
				setCalcu(calcu, isOnce);
			} else {
				setUpdate(update, isOnce);
			}
			isNeedDel = false;
		}
		
		
		public void setUpdate(CommonUpdate arg, boolean isOnce) {
			this.update = arg;
			this.calcu = null;
			this.isCalcu = false;
			this.isOnce = isOnce;
		}
		
		public void setCalcu(CommonCalcu arg, boolean isOnce) {
			this.calcu = arg;
			this.update = null;
			this.isCalcu = true;
			this.isOnce = isOnce;
		}

		@Override
		public boolean equals(Object other) {
			UpdateWrapper tmp = (UpdateWrapper)other;
			if (this.isCalcu == tmp.isCalcu) {
				if (this.isCalcu) {
					return this.calcu == tmp.calcu;
				} else {
					return this.update == tmp.update;
				}
			}
			return false;
		}
		
		@Override
		public int compareTo(Object another) {
			if (this.equals(another)) {
				throw new RuntimeException("can't be equals");
			} else {
				return 1;
			}
		}
	}
	
	private BinarySortTree<UpdateWrapper> updateNodes;
	private BinarySortTree<UpdateWrapper>.TreeIterator updateIterator;
	private BinarySortTree<UpdateWrapper>.TreeIterator dealIterator;

	private BinarySortTree<UpdateWrapper> addUpdateNodes;
	private BinarySortTree<UpdateWrapper>.TreeIterator addUpdateIterator;

	public void clear() {
		updateNodes.clear();
		addUpdateNodes.clear();
	}
	
	private void updateSelf() {
		for (dealIterator.clear(); dealIterator.hasNext();) {
			UpdateWrapper tmp = dealIterator.next();
			if (tmp.isNeedDel) {
				dealIterator.remove();
			}
		}
		if (addUpdateNodes.getNodeCount() > 0) {
			for (addUpdateIterator.clear(); addUpdateIterator.hasNext();) {
				UpdateWrapper tmp = addUpdateIterator.next();
				updateNodes.insert(tmp);
			}
			addUpdateNodes.clear();
		}
	}

	private void searchAndModifyUpdate(UpdateWrapper updateWrapper, boolean isAdd, boolean atOnce) {
		if (isAdd) {
			UpdateWrapper removeNode = null;
			for (addUpdateIterator.clear(); addUpdateIterator.hasNext();) {
				UpdateWrapper tmp = addUpdateIterator.next();
				if (updateWrapper.equals(tmp)) {
					if (atOnce) {
						removeNode = tmp;
						addUpdateIterator.remove();
						break;
					} else {
						return;
					}
				}
			}
			if (removeNode != null) {
				updateNodes.insert(removeNode);
				return;
			} else {
				for (dealIterator.clear(); dealIterator.hasNext();) {
					UpdateWrapper tmp = dealIterator.next();
					if (updateWrapper.equals(tmp)) {
						tmp.isNeedDel = false;
						return;
					}
				}
				if (atOnce) {
					updateNodes.insert(updateWrapper);
				} else {
					addUpdateNodes.insert(updateWrapper);
				}
			}
		} else {
			for (addUpdateIterator.clear(); addUpdateIterator.hasNext();) {
				UpdateWrapper tmp = addUpdateIterator.next();
				if (updateWrapper.equals(tmp)) {
					addUpdateIterator.remove();
					return; 
				}
			}
			for (dealIterator.clear(); dealIterator.hasNext();) {
				UpdateWrapper tmp = dealIterator.next();
				if (updateWrapper.equals(tmp)) {
					if (atOnce) {
						dealIterator.remove();
					} else {
						tmp.isNeedDel = true;
					}
					return;
				}
			}
		}
		return;
	}
	
	public void addCalcu(CommonCalcu calcu, boolean isOnce, boolean atOnce) {
		searchAndModifyUpdate(new UpdateWrapper(calcu, null, true, isOnce), true, atOnce);
	}
	 
	public void addUpdate(CommonUpdate update, boolean isOnce, boolean atOnce) {
		searchAndModifyUpdate(new UpdateWrapper(null, update, false, isOnce), true, atOnce);
	}
	
	public void removeCalcu(CommonCalcu calcu, boolean atOnce) {
		searchAndModifyUpdate(new UpdateWrapper(calcu, null, true, false), false, atOnce);
	}
	
	public void removeUpdate(CommonUpdate update, boolean atOnce) {
		searchAndModifyUpdate(new UpdateWrapper(null, update, false, false), false, atOnce);
	}
	
	public void deal(long interval) {
		updateSelf();
		for (updateIterator.clear(); updateIterator.hasNext();) {
			UpdateWrapper tmp = updateIterator.next();
			if (!tmp.isNeedDel) {
				if (tmp.isCalcu) {
					tmp.calcu.calcu(interval);
				} else {
					tmp.update.update();
				}
			}
			if (tmp.isOnce || tmp.isNeedDel) {
				updateIterator.remove();
			}
		}
	}
	
	public UpdateManager() {
		init();
	}
	
	private void init() {
		this.updateNodes = new BinarySortTree<UpdateWrapper>();
		this.updateIterator = (BinarySortTree<UpdateWrapper>.TreeIterator)(this.updateNodes.iterator(BinarySortTree.midFirst));
		this.dealIterator = (BinarySortTree<UpdateWrapper>.TreeIterator)(this.updateNodes.iterator(BinarySortTree.midFirst));

		this.addUpdateNodes = new BinarySortTree<UpdateWrapper>();
		this.addUpdateIterator = (BinarySortTree<UpdateWrapper>.TreeIterator)(this.addUpdateNodes.iterator(BinarySortTree.midFirst));
	}
}
