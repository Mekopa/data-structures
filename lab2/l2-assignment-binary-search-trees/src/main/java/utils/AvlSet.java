package utils;

import java.util.Comparator;

/**
 * The AVL-tree implementation of a ordered set.
 *
 * @param <E> Type of the set element. Must implement the Comparable<E> interface, or
 * an object that implements Comparator<E> interface must be passed through the class constructor
 * @author darius.matulis@ktu.lt
 * @task Review and understand the provided methods.
 */
public class AvlSet<E extends Comparable<E>> extends BstSet<E> implements SortedSet<E> {

    public AvlSet() {
    }

    public AvlSet(Comparator<? super E> c) {
        super(c);
    }

    /**
     * Adds a new element to the set.
     *
     * @param element
     */
    @Override
    public void add(E element) {
        if (element == null) {
            throw new IllegalArgumentException("Element is null in add(E element)");
        }
        root = addRecursive(element, (AVLNode<E>) root);

    }

    /**
     * A recursive method used as helper method for adding a new element to the set
     *
     * @param element
     * @param node
     * @return
     */
    private AVLNode<E> addRecursive(E element, AVLNode<E> node) {
        if (node == null) {
            size++;
            return new AVLNode<>(element);
        }
        int cmp = c.compare(element, node.element);

        if (cmp < 0) {
            node.setLeft(addRecursive(element, node.getLeft()));
            if ((height(node.getLeft()) - height(node.getRight())) == 2) {
                int cmp2 = c.compare(element, node.getLeft().element);
                node = cmp2 < 0 ? rightRotation(node) : doubleRightRotation(node);
            }
        } else if (cmp > 0) {
            node.setRight(addRecursive(element, node.getRight()));
            if ((height(node.getRight()) - height(node.getLeft())) == 2) {
                int cmp2 = c.compare(node.getRight().element, element);
                node = cmp2 < 0 ? leftRotation(node) : doubleLeftRotation(node);
            }
        }
        node.height = Math.max(height(node.getLeft()), height(node.getRight())) + 1;
        return node;
    }

    /**
     * Removes an element from the set.
     *
     * @param element
     */
    @Override
    public void remove(E element) {
        if (element == null) {
            throw new IllegalArgumentException("Element is null in remove(E element)");
        }
        root = removeRecursive(element, (AVLNode<E>) root);
    }

    /**
     * Recursive helper method for removing an element from AVL tree.
     * Performs standard BST deletion followed by rebalancing.
     *
     * @param element the element to remove
     * @param node current node being processed
     * @return the updated subtree root after removal and rebalancing
     */
    private AVLNode<E> removeRecursive(E element, AVLNode<E> node) {
        // Base case: element not found
        if (node == null) {
            return null;
        }

        int cmp = c.compare(element, node.element);

        if (cmp < 0) {
            // Element is in left subtree
            node.setLeft(removeRecursive(element, node.getLeft()));
        } else if (cmp > 0) {
            // Element is in right subtree
            node.setRight(removeRecursive(element, node.getRight()));
        } else {
            // Found the element to delete - handle 3 BST deletion cases

            // CASE 1: Leaf node (no children)
            if (node.getLeft() == null && node.getRight() == null) {
                size--;
                return null;
            }

            // CASE 2: One child (right only)
            if (node.getLeft() == null) {
                size--;
                return node.getRight();
            }

            // CASE 2: One child (left only)
            if (node.getRight() == null) {
                size--;
                return node.getLeft();
            }

            // CASE 3: Two children - use predecessor (max in left subtree)
            AVLNode<E> predecessor = getMaxNode(node.getLeft());
            node.element = predecessor.element;
            node.setLeft(removeMaxNode(node.getLeft()));
            size--;
        }

        // Update height of current node
        node.height = Math.max(height(node.getLeft()), height(node.getRight())) + 1;

        // Check balance factor and rebalance if needed
        int balance = height(node.getLeft()) - height(node.getRight());

        // Left-heavy (balance > 1)
        if (balance > 1) {
            // Left-Left case: left child is left-heavy or balanced
            if (height(node.getLeft().getLeft()) >= height(node.getLeft().getRight())) {
                return rightRotation(node);
            }
            // Left-Right case: left child is right-heavy
            else {
                return doubleRightRotation(node);
            }
        }

        // Right-heavy (balance < -1)
        if (balance < -1) {
            // Right-Right case: right child is right-heavy or balanced
            if (height(node.getRight().getRight()) >= height(node.getRight().getLeft())) {
                return leftRotation(node);
            }
            // Right-Left case: right child is left-heavy
            else {
                return doubleLeftRotation(node);
            }
        }

        return node;
    }

    /**
     * Finds the node with maximum value in the subtree.
     *
     * @param node root of subtree
     * @return node with maximum value
     */
    private AVLNode<E> getMaxNode(AVLNode<E> node) {
        while (node.getRight() != null) {
            node = node.getRight();
        }
        return node;
    }

    /**
     * Removes the node with maximum value from subtree and rebalances.
     *
     * @param node root of subtree
     * @return updated subtree root after removal and rebalancing
     */
    private AVLNode<E> removeMaxNode(AVLNode<E> node) {
        if (node.getRight() == null) {
            return node.getLeft();
        }

        node.setRight(removeMaxNode(node.getRight()));

        // Update height
        node.height = Math.max(height(node.getLeft()), height(node.getRight())) + 1;

        // Check balance and rebalance if needed
        int balance = height(node.getLeft()) - height(node.getRight());

        if (balance > 1) {
            if (height(node.getLeft().getLeft()) >= height(node.getLeft().getRight())) {
                return rightRotation(node);
            } else {
                return doubleRightRotation(node);
            }
        }

        return node;
    }

    // AVL tree rotation methods

    //           n2
    //          /                n1
    //         n1      ==>      /  \
    //        /                n3  n2
    //       n3

    private AVLNode<E> rightRotation(AVLNode<E> n2) {
        AVLNode<E> n1 = n2.getLeft();
        n2.setLeft(n1.getRight());
        n1.setRight(n2);
        n2.height = Math.max(height(n2.getLeft()), height(n2.getRight())) + 1;
        n1.height = Math.max(height(n1.getLeft()), height(n2)) + 1;
        return n1;
    }

    private AVLNode<E> leftRotation(AVLNode<E> n1) {
        AVLNode<E> n2 = n1.getRight();
        n1.setRight(n2.getLeft());
        n2.setLeft(n1);
        n1.height = Math.max(height(n1.getLeft()), height(n1.getRight())) + 1;
        n2.height = Math.max(height(n2.getRight()), height(n1)) + 1;
        return n2;
    }

    //            n3               n3
    //           /                /                n2
    //          n1      ==>      n2      ==>      /  \
    //           \              /                n1  n3
    //            n2           n1
    //
    private AVLNode<E> doubleRightRotation(AVLNode<E> n3) {
        n3.left = leftRotation(n3.getLeft());
        return rightRotation(n3);
    }

    private AVLNode<E> doubleLeftRotation(AVLNode<E> n1) {
        n1.right = rightRotation(n1.getRight());
        return leftRotation(n1);
    }

    private int height(AVLNode<E> n) {
        return (n == null) ? -1 : n.height;
    }

    /**
     * Inner class of tree node
     *
     * @param <N> node element data type
     */
    protected class AVLNode<N> extends BstNode<N> {

        protected int height;

        protected AVLNode(N element) {
            super(element);
            this.height = 0;
        }

        protected void setLeft(AVLNode<N> left) {
            this.left = left;
        }

        protected AVLNode<N> getLeft() {
            return (AVLNode<N>) left;
        }

        protected void setRight(AVLNode<N> right) {
            this.right = right;
        }

        protected AVLNode<N> getRight() {
            return (AVLNode<N>) right;
        }
    }
}
