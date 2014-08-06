package com.neuo.common;

import com.neuo.common.CommonEvent.Event;
import com.neuo.struct.BinarySortTree;

public class EventManager implements EventHander {

	public static interface SortEventHander
	extends EventHander {
		public void setSortValue(float value);
		public float getSortValue();
	}
	
	private static class EventHanderWrapper 
	implements Comparable<Object> {
		public float sortValue;
		public boolean isNeedDel;
		public SortEventHander eventHander;
		
		public EventHanderWrapper(SortEventHander eventHander) {
			clear();
			this.eventHander = eventHander;
			setChange();
		}
		
		public boolean isChange() {
			return this.sortValue != eventHander.getSortValue();
		}
		
		public void setChange() {
			this.sortValue = eventHander.getSortValue();
		}
		
		@Override
		public boolean equals(Object arg) {
			EventHanderWrapper other = (EventHanderWrapper)arg;
			return this.eventHander == other.eventHander;
		}
		
		private void clear() {
			sortValue = 0f;
			isNeedDel = false;
			eventHander = null;
		}

		@Override
		public int compareTo(Object another) {
			float interval = this.sortValue - ((EventHanderWrapper)another).sortValue;
			if (interval > 0) {
				return 1;
			} else if (interval < 0) {
				return -1;
			} else {
				return 0;
			}
		}
	}
	
	private BinarySortTree<EventHanderWrapper> sortEvents;
	private BinarySortTree<EventHanderWrapper>.TreeIterator sortIterator;
	private BinarySortTree<EventHanderWrapper>.TreeIterator dealIterator;

	private BinarySortTree<EventHanderWrapper> addSortEvents;
	private BinarySortTree<EventHanderWrapper>.TreeIterator addIterator;
	
	private BinarySortTree<EventHanderWrapper> tmpSortEvents;
	private BinarySortTree<EventHanderWrapper>.TreeIterator tmpIterator;

	private EventHander currHander;
	private int acceptedState;
	
	public EventManager() {
		init();
	}
	
	private void init() {
		sortEvents = new BinarySortTree<EventHanderWrapper>();
		sortIterator = (BinarySortTree<EventHanderWrapper>.TreeIterator)sortEvents.iterator(BinarySortTree.rightFirst);
		dealIterator = (BinarySortTree<EventHanderWrapper>.TreeIterator)sortEvents.iterator(BinarySortTree.midFirst);

		addSortEvents = new BinarySortTree<EventHanderWrapper>();
		addIterator = (BinarySortTree<EventHanderWrapper>.TreeIterator)addSortEvents.iterator(BinarySortTree.midFirst);

		tmpSortEvents = new BinarySortTree<EventHanderWrapper>();
		tmpIterator = (BinarySortTree<EventHanderWrapper>.TreeIterator)tmpSortEvents.iterator(BinarySortTree.midFirst);
	}

	private void updateSelf() {
		tmpSortEvents.clear();
		for (dealIterator.clear(); dealIterator.hasNext();) {
			EventHanderWrapper tmp = dealIterator.next();
			if (tmp.isNeedDel) {
				dealIterator.remove();
			} else if (tmp.isChange()) {
				dealIterator.remove();
				tmpSortEvents.insert(tmp);
			}
		}
		if (tmpSortEvents.getNodeCount() > 0) {
			for (tmpIterator.clear(); tmpIterator.hasNext();) {
				EventHanderWrapper tmp = tmpIterator.next();
				tmp.setChange();
				sortEvents.insert(tmp);
			}
			tmpSortEvents.clear();
		}
		if (addSortEvents.getNodeCount() > 0) {
			for (addIterator.clear(); addIterator.hasNext();) {
				EventHanderWrapper tmp = addIterator.next();
				tmp.setChange();
				sortEvents.insert(tmp);
			}
			addSortEvents.clear();
		}
	}
	
	public void remove(SortEventHander hander, boolean atOnce) {
		searchAndModifyUpdate(new EventHanderWrapper(hander), false, atOnce);
	}
	
	public void add(SortEventHander hander, float value, boolean atOnce) {
		hander.setSortValue(value);
		searchAndModifyUpdate(new EventHanderWrapper(hander), true, atOnce);
	}
	
	public void add(SortEventHander hander,  boolean atOnce) {
		searchAndModifyUpdate(new EventHanderWrapper(hander), true, atOnce);
	}
	
	private void searchAndModifyUpdate(EventHanderWrapper handerWrapper, boolean isAdd, boolean atOnce) {
		if (isAdd) {
			EventHanderWrapper removeNode = null;
			for (addIterator.clear(); addIterator.hasNext();) {
				EventHanderWrapper tmp = addIterator.next();
				if (tmp.eventHander == handerWrapper.eventHander) {
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
				removeNode.setChange();
				sortEvents.insert(removeNode);
				return;
			} else {
				for (dealIterator.clear(); dealIterator.hasNext();) {
					EventHanderWrapper tmp = dealIterator.next();
					if (tmp.eventHander == handerWrapper.eventHander) {
						tmp.isNeedDel = false;
						return;
					}
				}
				if (atOnce) {
					sortEvents.insert(handerWrapper);
				} else {
					addSortEvents.insert(handerWrapper);
				}
			}
		} else {
			for (addIterator.clear(); addIterator.hasNext();) {
				EventHanderWrapper tmp = addIterator.next();
				if (tmp.eventHander == handerWrapper.eventHander) {
					addIterator.remove();
					return; 
				}
			}
			for (dealIterator.clear(); dealIterator.hasNext();) {
				EventHanderWrapper tmp = dealIterator.next();
				if (tmp.eventHander == handerWrapper.eventHander) {
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
	
	public void clear() {
		sortEvents.clear();
		addSortEvents.clear();
		resetTouchState();
	}
	
	@Override
	public EventHander isHanderEvent(Event e) {
		updateSelf();
		for (sortIterator.clear(); sortIterator.hasNext();) {
			EventHanderWrapper tmp = sortIterator.next();
			if (!tmp.isNeedDel) {
				EventHander res = tmp.eventHander.isHanderEvent(e);
				if (res != null) {
					return res;
				}
			}
		}
		return null;
	}

	@Override
	public void resetTouchState() {
		if (currHander != null) {
			if ((acceptedState & CommonEvent.Cancle) != 0) {
				currHander.resetTouchState();
			}
			currHander = null;
			acceptedState = CommonEvent.None;
		}
	}
	
	@Override
	public int onHanderEvent(Event e) {
		int res = CommonEvent.None;
		if (e.action == CommonEvent.Down || e.action == CommonEvent.Double) {
			EventHander lastHander = currHander;
			currHander = null;
			if (lastHander != null &&
					((acceptedState & e.action) != 0)) {
				currHander = lastHander;
			} else {
				currHander = isHanderEvent(e);
			}
			if (currHander != null) {
				acceptedState = currHander.onHanderEvent(e);
				if (acceptedState == CommonEvent.None) {
					currHander = null;
				}
			} else {
				acceptedState = CommonEvent.None;
			}
			res = acceptedState;
		} else if ((e.action & CommonEvent.KeyEvent) != 0) {
			EventHander keyHander = isHanderEvent(e);
			if (keyHander != null) {
				keyHander.onHanderEvent(e);
			}
		} else {
			if (currHander != null &&
					((acceptedState & e.action) != 0)) {
				acceptedState = currHander.onHanderEvent(e);
				if (acceptedState == CommonEvent.None) {
					currHander = null;
				}
			} else if (e.action == CommonEvent.Cancle) {
				acceptedState = CommonEvent.None;
				currHander = null;
			}
			res = acceptedState;
		}
		return res;
	}
 }
