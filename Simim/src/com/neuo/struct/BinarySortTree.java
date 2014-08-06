package com.neuo.struct;

import java.util.Iterator;

import com.neuo.common.CommonClone;
import com.neuo.common.CommonCloneable;

public class BinarySortTree<Type extends Object> 
implements Iterable<Type>, CommonCloneable {
	
	protected class Node {
		public Type value;
		public Node parent;
		public Node left, right;
		public boolean isRed;
	}
	
	protected Node root;
	protected int nodeCount;
	
	public BinarySortTree(BinarySortTree<Type> other, CommonClone cloneObj) {
		newRoot();
		clear();
		copyFrom(this.root, other.root.left, cloneObj);
	}
	
	@SuppressWarnings("unchecked")
	protected void copyFrom(Node parent, Node other, CommonClone cloneObj) {
		if (other != null) {
			boolean isLeft = getNodeDir(other);
			Node tmp = newNode(null, parent);
			if (cloneObj == null) {
				tmp.value = (Type)((CommonCloneable)other.value).getClone();
			} else {
				tmp.value = (Type)cloneObj.getClone(other.value);
			}
			if (isLeft) {
				parent.left = tmp;
			} else {
				parent.right = tmp;
			}
			this.nodeCount ++;
			copyFrom(tmp, other.left, cloneObj);
			copyFrom(tmp, other.right, cloneObj);
		}
	}
	
	public BinarySortTree() {
		newRoot();
		clear();
	}
	
	protected void newRoot() {
		this.root = newNode(null, null);
	}
	
	public BinarySortTree<Type> getClone() {
		return new BinarySortTree<Type>(this, null);
	}
	
	public BinarySortTree<Type> getClone(CommonClone cloneObj) {
		return new BinarySortTree<Type>(this, cloneObj);
	}
	
	public void clear() {
		this.root.left = null;
		nodeCount = 0;
	}
	
	protected int compare(Type value1, Type value2) {
		@SuppressWarnings("unchecked")
		Comparable<Object> nComparable = (Comparable<Object>)value2;
		return nComparable.compareTo(value1);
	}
	
	public Type getRoot() {
		if (root.left == null) {
			return null;
		} else {
			return root.left.value;
		}
	}
	
	public Type findByValue(Type value) {
		return findByValue(this.root.left, value);
	}

	protected Type findByValue(Node root, Type value) {
		if (root != null) {
			int c = compare(root.value, value);
			if (c == 0) {
				return root.value;
			} else if (c < 0) {
				return findByValue(root.left, value);
			} else {
				return findByValue(root.right, value);
			}
		} else {
			return null;
		}
	}
	
	public Type find(Type value) {
		return find(this.root.left, value);
	}
	
	//如果不是比较值查找需要全部遍历
	protected Type find(Node root, Type value) {
		if (root != null) {
			if (root.value == value) {
				return root.value;
			} else {
				Type tmp = find(root.left, value);
				if (tmp != null) {
					return tmp;
				}
				return find(root.right, value);
			}
		} else {
			return null;
		}
	}
	
	
	protected Node insert(Node root, Type value) {
		int c = compare(root.value, value);
		if (c < 0) {
			if (root.left == null) {
				root.left = newNode(value, root);
				return root.left;
			} else {
				return insert(root.left, value);
			}
		} else {
			if (root.right == null) {
				root.right = newNode(value, root);
				return root.right;
			} else {
				return insert(root.right, value);
			}
		}
	}
	
	public void insert(Type value) {
		if (this.root.left == null) {
			this.root.left = newNode(value, this.root);
		} else {
			insert(this.root.left, value);
		}
		this.nodeCount++;
 	}
	
	protected Node newNode(Type value, Node parent) {
		Node tmp = new Node();
		tmp.parent = parent;
		tmp.value = value;
		tmp.right = null;
		tmp.left = null;
		return tmp;
	}
	
	protected static final int left = 0;
	protected static final int right = 1;
	protected Node getRemoveNodeChild(Node delNode) {
		if (delNode.left != null) {
			return delNode.left;
		} else {
			return delNode.right;
		}
		
	}

	protected Node removeNode(Node node) {
		Node child = null;
		Node delNode = null;
		if (node.left == null || node.right == null) {
			delNode = node;
		} else {
			delNode = getLeft(node.right);
		}
		child = getRemoveNodeChild(delNode);
		if (child != null) {
			child.parent = delNode.parent;
		}
		if (getNodeDir(delNode)) {
			delNode.parent.left = child;
		} else {
			delNode.parent.right = child;
		}
		if (delNode != node) {
			node.value = delNode.value;
		}
		nodeCount--;
		return delNode;
	}
	
	protected Type remove(Node root, Type value) {
		if (null != root) {
			if (root.value == value) {
				Type tmp = root.value;
				removeNode(root);
				return tmp;
			} else {
				Type tmp = remove(root.left, value);
				if (tmp != null) {
					return tmp;
				}
				return remove(root.right, value);
			}
		} else {
			return null;
		}
	}
	
	public Type remove(Type value) {
		return remove(this.root.left, value);
	}
	
	protected Type removeByValue(Node root, Type value) {
		if (null != root) {
			int c = compare(root.value, value);
			if (c == 0) {
				Type tmp = root.value;
				removeNode(root);
				return tmp;
			} else {
				if (c < 0) {
					return removeByValue(root.left, value);
				} else {
					return removeByValue(root.right, value);
				}
			}
		} else {
			return null;
		}
	}
	
	public Type removeByValue(Type value) {
		return removeByValue(this.root.left, value);
	}

	@Override
	public Iterator<Type> iterator() {
		return new TreeIterator(leftFirst);
	}
	
	public Iterator<Type> iterator(int model) {
		return new TreeIterator(model);
	}
	
	protected Node getLeft(Node pre) {
		if (pre == null) {
			return null;
		}
		if (pre.left == null) {
			return pre;
		} else {
			return getLeft(pre.left);
		}
	}
	
	public int getTreeHigh(boolean isMax) {
		return getTreeHeight(this.root.left, 0, isMax);
	}
	
	private int getTreeHeight(Node node, int high, boolean isMax) {
		if (node == null) {
			return high;
		} else {
			high++;
			int left = getTreeHeight(node.left, high, isMax);
			if (!isMax && left == high) {
				return high;
			}
			int right = getTreeHeight(node.right, high, isMax);
			if ((isMax && left < right) || (!isMax && left > right)) {
				return right;
			} else {
				return left;
			}
		}
	}
	
	protected Node getRight(Node pre) {
		if (pre == null) {
			return null;
		}
		if (pre.right == null) {
			return pre;
		} else {
			return getRight(pre.right);
		}
	}
	
	protected Node getPreRightChild(Node pre) {
		if (pre == null) {
			return null;
		}
		Node tmp = getPreLeft(pre);
		if (tmp != null && tmp.right != null) {
			return tmp.right;
		} else if (tmp != null) {
			return getPreRightChild(tmp);
		}
		return null;
	}
	
	protected Node getPreRight(Node pre) {
		if (pre == null) {
			return null;
		}
		if (pre.parent == root) {
			return null;
		} else if (pre.parent.left == pre) {
			return getPreRight(pre.parent);
		} else {
			return pre.parent;
		}
	}
	
	protected Node getPreLeft(Node pre) {
		if (pre == null) {
			return null;
		}
		if (pre.parent == root) {
			return null;
		} else if (pre.parent.right == pre) {
			return getPreLeft(pre.parent);
		} else {
			return pre.parent;
		}
	}
	
	protected Node getSuccessor(Node node) {
		if (node == null) {
			return null;
		} else if (node.right != null) {
			return getLeft(node.right);
		} else {
			return getPreLeft(node);
		}
	}
	
	protected boolean getNodeDir(Node node) {
		if (node.parent.left == node) {
			return true;
		} else {
			return false;
		}
	}
	
	public int getNodeCount() {
		return nodeCount;
	}
	
	public Type getVale(int index, int model) {
		if (index >= 0 && index < nodeCount) {
			Node tmpNode = getNextNode(this.root.left, true, model);
			while (index > 0) {
				index--;
				tmpNode = getNextNode(tmpNode, false, model);
			}
			return tmpNode.value;
		} else {
			return null;
		}
	}
	
	public Node getPreNode(Node next, int model) {
		if (next == null) {
			return null;
		}
		if (model == leftFirst) {
			if (next.left == null) {
				return getPreRight(next);
			} else {
				return getRight(next.left);
			}
		} else if (model == rightFirst) {
			if (next.right == null) {
				return getPreLeft(next);
			} else {
				return getLeft(next.right);
			}
		} else {
			if (next.parent == root) {
				return null;
			} else if (next.parent.left == next) {
				return next.parent;
			} else if (next.parent.left == null) {
				return next.parent;
			} else {
				return getRight(next.parent.left);
			}
		}
	}
	
	public Node getNextNode(Node pre, boolean isStart, int model) {
		if (isStart) {
			if (model == leftFirst) {
				return getLeft(this.root.left);
			} else if (model == rightFirst) {
				return getRight(this.root.left);
			} else {
				return this.root.left;
			}
		} else {
			if (pre == null) {
				return null;
			}
			if (model == leftFirst) {
				return getSuccessor(pre);
			} else if (model == rightFirst) {
				if (pre.left != null) {
					return getRight(pre.left);
				} else {
					return getPreRight(pre);
				}
			} else {
				if (pre.left != null) {
					return pre.left;
				} else if (pre.right != null) {
					return pre.right;
				} else {
					return getPreRightChild(pre);
				}
			}
		}
	}
	
	public static final int leftFirst = 0;
	public static final int rightFirst = 1;
	public static final int midFirst = 2;
	public class TreeIterator implements Iterator<Type> {
		protected int model;
		protected int curIndex;
		protected Node currNode;
		public TreeIterator(int model) {
			this.model = model;
			clear();
		}
		
		public void clear() {
			curIndex = 0;
			currNode = null;
		}
		
		public int getIndex() {
			return curIndex;
		}
		
		@Override
		public boolean hasNext() {
			return curIndex < nodeCount;
		}

		@Override
		public Type next() {
			if (hasNext()) {
				if (curIndex == 0) {
					currNode = getNextNode(currNode, true, model);
				} else {
					currNode = getNextNode(currNode, false, model);
				}
				curIndex++;
				return currNode.value;
			} else {
				return null;
			}
		}

		@Override
		public void remove() {
			Node tmpNode;
			if (curIndex >= 1) {
				tmpNode = getPreNode(currNode, model);
				removeNode(currNode);
			} else {
				throw new RuntimeException("can't enter this branch");
			}
			curIndex --;
			if (curIndex == 0) {
				currNode = null;
			} else {
				currNode = tmpNode;
			}
		}
		
	}
}
