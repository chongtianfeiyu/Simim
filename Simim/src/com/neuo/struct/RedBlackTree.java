package com.neuo.struct;

import java.util.Iterator;

import com.neuo.common.CommonClone;
import com.neuo.common.CommonCloneable;

public class RedBlackTree<Type extends Object>
extends BinarySortTree<Type> {
	
	public RedBlackTree(RedBlackTree<Type> other, CommonClone cloneObj) {
		super(other, cloneObj);
	}
	
	public RedBlackTree() {
		super();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected void copyFrom(Node parent, Node other, CommonClone cloneObj) {
		if (other != null) {
			boolean isLeft = getNodeDir(other);
			Node tmp = newNode(null, parent, other.isRed);
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
	
	@Override
	protected void newRoot() {
		this.root = newNode(null, null, false);
	}
	
	@Override
	public RedBlackTree<Type> getClone() {
		return new RedBlackTree<Type>(this, null);
	}
	
	@Override
	public RedBlackTree<Type> getClone(CommonClone cloneObj) {
		return new RedBlackTree<Type>(this, cloneObj);
	}
	
	@Override
	public void insert(Type value) {
		if (this.root.left == null) {
			this.root.left = newNode(value, this.root, false);
		} else {
			insertFixUp(insert(this.root.left, value));
		}
		this.nodeCount++;
 	}
	
	protected void rotateLeft(Node node) {
		if (node == null || node.right == null) {
			return;
		}
		boolean isLeft = getNodeDir(node);
		Node right = node.right;
		node.right = right.left;
		if (node.right != null) {
			node.right.parent = node;
		}
		right.parent = node.parent;
		if (isLeft) {
			right.parent.left = right;
		} else {
			right.parent.right = right;
		}
		right.left = node;
		node.parent = right;
	}
	
	protected void rotateRight(Node node) {
		if (node == null || node.left == null) {
			return;
		}
		boolean isLeft = getNodeDir(node);
		Node left = node.left;
		node.left = left.right;
		if (node.left != null) {
			node.left.parent = node;
		}
		left.parent = node.parent;
		if (isLeft) {
			left.parent.left = left;
		} else {
			left.parent.right = left;
		}
		left.right = node;
		node.parent = left;
	}
	
	protected void insertFixUp(Node node) {
		while (node.parent.isRed) {
			if (node.parent.parent.left == node.parent) {
				Node uncle = node.parent.parent.right;
				if (uncle != null && uncle.isRed) {
					node.parent.isRed = false;
					uncle.isRed = false;
					node.parent.parent.isRed = true;
					node = node.parent.parent;
				} else {
					if (!getNodeDir(node)) {
						node = node.parent;
						rotateLeft(node);
					}
					node.parent.isRed = false;
					node.parent.parent.isRed = true;
					rotateRight(node.parent.parent);
				}
			} else {
				Node uncle = node.parent.parent.left;
				if (uncle != null && uncle.isRed) {
					node.parent.isRed = false;
					uncle.isRed = false;
					node.parent.parent.isRed = true;
					node = node.parent.parent;
				} else {
					if (getNodeDir(node)) {
						node = node.parent;
						rotateRight(node);
					}
					node.parent.isRed = false;
					node.parent.parent.isRed = true;
					rotateLeft(node.parent.parent);
				}
			}
		}
		this.root.left.isRed = false;
	}
	
	protected Node newNode(Type value, Node parent, boolean isRed) {
		Node tmp = newNode(value, parent);
		tmp.isRed = isRed;
		return tmp;
	}

	@Override
	protected Node newNode(Type value, Node parent) {
		Node tmp = new Node();
		tmp.parent = parent;
		tmp.value = value;
		tmp.isRed = true;
		tmp.left = null;
		tmp.right = null;
		return tmp;
	}
	
	@Override
	protected Node removeNode(Node node) {
		Node delNode = super.removeNode(node);
		if (!delNode.isRed) {
			removeFixUp(delNode.parent, getRemoveNodeChild(delNode));
		}
		return delNode;
	}
		
	protected boolean getNodeDir(Node parent, Node node) {
		if (parent.left == node) {
			return true;
		} else {
			return false;
		}
	}

	protected void removeFixUp(Node parent, Node node) {
		while (node != this.root.left && (node == null || !node.isRed)) {
			if (getNodeDir(parent, node)) {
				Node brother = parent.right;
				//不会同时为nil
				if (brother.isRed) {
					brother.isRed = false;
					parent.isRed = true;
					rotateLeft(parent);
					brother = parent.right;
				}
				//brother 必然不为空，即使经过了case 1
				if ((brother.left == null || !brother.left.isRed) && 
							(brother.right == null || !brother.right.isRed)) {
					brother.isRed = true;
					node = parent;
					parent = parent.parent;
				} else {
					if (brother.right == null || !brother.right.isRed) {
						//if (brother.left != null) {
						brother.left.isRed = false;
						//}
						brother.isRed = true;
						rotateRight(brother);
						brother = parent.right;
					}
					//case4
					brother.isRed = parent.isRed;
					parent.isRed = false;
					brother.right.isRed = false;
					rotateLeft(parent);
					break;
				}
			} else {
				Node brother = parent.left;
				//不会同时为nil
				if (brother.isRed) {
					brother.isRed = false;
					parent.isRed = true;
					rotateRight(parent);
					brother = parent.left;
				}
				//brother 必然不为空，即使经过了case 1
				if ((brother.right == null || !brother.right.isRed) && 
							(brother.left == null || !brother.left.isRed)) {
					brother.isRed = true;
					node = parent;
					parent = parent.parent;
				} else {
					if (brother.left == null || !brother.left.isRed) {
						//if (brother.left != null) {
						brother.right.isRed = false;
						//}
						brother.isRed = true;
						rotateLeft(brother);
						brother = parent.left;
					}
					//case4
					brother.isRed = parent.isRed;
					parent.isRed = false;
					brother.left.isRed = false;
					rotateRight(parent);
					break;
				}
			}
		}
		if (node != null) {
			node.isRed = false;
		}
	}

	@Override
	public Iterator<Type> iterator() {
		return new RBTreeIterator(leftFirst);
	}
	
	@Override
	public Iterator<Type> iterator(int model) {
		return new RBTreeIterator(model);
	}

	public class RBTreeIterator extends BinarySortTree<Type>.TreeIterator {
		
		public RBTreeIterator(int model) {
			super(model);
		}

		@Override
		public void remove() {
			if (model == midFirst) {
				throw new RuntimeException("can't remove when use mid mode");
			}
			super.remove();
		}
		
	}
}
