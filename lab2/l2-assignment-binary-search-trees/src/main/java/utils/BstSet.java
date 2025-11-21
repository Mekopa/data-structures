package utils;

import java.lang.reflect.Array;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Stack;

/**
 * The binary search tree implementation of a ordered set.
 *
 * @param <E> Aibės elemento tipas. Turi tenkinti interfeisą Comparable<E>, or
 *  * an object that implements Comparator<E> interface must be passed through the class constructor
 * @author darius.matulis@ktu.lt
 * @task Review and understand the provided methods.
 */
public class BstSet<E extends Comparable<E>> implements SortedSet<E>, Cloneable {

    // The roor node of tree
    protected BstNode<E> root = null;
    // Tree size
    protected int size = 0;
    // Pointer to comparator
    protected Comparator<? super E> c;

    /**
     * Creates a set object, whereby comparator is defined by Comparable<E>
     */
    public BstSet() {
        this.c = Comparator.naturalOrder();
    }

    /**
     * Creates a set object with custom Comparator<E> comparator
     *
     * @param c Comparator
     */
    public BstSet(Comparator<? super E> c) {
        this.c = c;
    }

    /**
     * Checks if the set is empty.
     *
     * @return Returns true if the set is empty.
     */
    @Override
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * @return Returns the number of elements in the array.
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Clears the set.
     */
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    /**
     * Checks whether an element exists in the array.
     *
     * @param element - element of the array.
     * @return true if an element exists in the set, else false.
     */
    @Override
    public boolean contains(E element) {
        if (element == null) {
            throw new IllegalArgumentException("Element is null in contains(E element)");
        }

        return get(element) != null;
    }

    /**
     * Checks if all elements of the input set exist in the set
     *
     * @param set input set
     * @return
     */
    @Override
    public boolean containsAll(Set<E> set) {
        throw new UnsupportedOperationException("Students need to implement containsAll(Set<E> set)");
    }

    /**
     Adds a new element to the set.
     *
     * @param element - element.
     */
    @Override
    public void add(E element) {
        if (element == null) {
            throw new IllegalArgumentException("Element is null in add(E element)");
        }

        root = addRecursive(element, root);
    }

    /**
     * Adds all elements of the input set to the set
     *
     * @param set input set
     */
    @Override
    public void addAll(Set<E> set) {
        throw new UnsupportedOperationException("Students need to implement addAll(Set<E> set)");
    }

    private BstNode<E> addRecursive(E element, BstNode<E> node) {
        if (node == null) {
            size++;
            return new BstNode<>(element);
        }

        int cmp = c.compare(element, node.element);
        if (cmp < 0) {
            node.left = addRecursive(element, node.left);
        } else if (cmp > 0) {
            node.right = addRecursive(element, node.right);
        }

        return node;
    }

    /**
     * Removes an element from the set.
     *
     * @param element - element.
     */
    @Override
    public void remove(E element) {
        throw new UnsupportedOperationException("Students need to implement remove(E element)");
    }

    /**
     * Only elements within the input set remain in the set.
     *
     * @param set input set
     */
    @Override
    public void retainAll(Set<E> set) {
        throw new UnsupportedOperationException("Students need to implement retainAll(Set<E> set)");
    }

    private BstNode<E> removeRecursive(E element, BstNode<E> node) {
        throw new UnsupportedOperationException("Students need to implement removeRecursive(E element, BstNode<E>n)");
    }

    /**
     * Removes node with the max value from the set
     *
     * @param node starting root node
     * @return
     */
    BstNode<E> removeMax(BstNode<E> node) {
        if (node == null) {
            return null;
        } else if (node.right != null) {
            node.right = removeMax(node.right);
            return node;
        } else {
            return node.left;
        }
    }

    /**
     * Returns the node with the maximum value
     *
     * @param node starting root node
     * @return
     */
    BstNode<E> getMax(BstNode<E> node) {
        return get(node, true);
    }

    /**
     * Returns the node with the minimum value
     *
     * @param node starting root node
     * @return
     */
    BstNode<E> getMin(BstNode<E> node) {
        return get(node, false);
    }

    private BstNode<E> get(BstNode<E> node, boolean findMax) {
        BstNode<E> parent = null;
        while (node != null) {
            parent = node;
            node = (findMax) ? node.right : node.left;
        }
        return parent;
    }

    private E get(E element) {
        if (element == null) {
            throw new IllegalArgumentException("Element is null in get(E element)");
        }

        BstNode<E> node = root;
        while (node != null) {
            int cmp = c.compare(element, node.element);

            if (cmp < 0) {
                node = node.left;
            } else if (cmp > 0) {
                node = node.right;
            } else {
                return node.element;
            }
        }

        return null;
    }

    /**
     * Converts set to an array.
     *
     * @return Returns an array of set elements.
     */
    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        int i = 0;
        for (E o : this) {
            array[i++] = o;
        }
        return array;
    }

    public E[] toArray(Class<E> clasz) {
        //Generic array
        E[] array = (E[]) Array.newInstance(clasz, size);
        int i = 0;
        for (E o : this) {
            array[i++] = o;
        }
        return array;
    }

    /**
     * Forms an Inorder (ascending order) string of set elements.
     *
     * @return string of set elements
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (E element : this) {
            sb.append(element.toString()).append(System.lineSeparator());
        }
        return sb.toString();
    }

    /**
     * Box drawing symbols used for tree visualisation, see: unicode.org/charts/PDF/U2500.pdf
     * These are the 4 possible terminal symbols at the end of the tree branch
     */
    private static final String[] term = {"\u2500", "\u2534", "\u252C", "\u253C"};
    private static final String rightEdge = "\u250C";
    private static final String leftEdge = "\u2514";
    private static final String endEdge = "\u25CF";
    private static final String vertical = "\u2502 ";
    private String horizontal;

    /*
     * Method for tree visualisation.
     * @author E. Karčiauskas
     */
    @Override
    public String toVisualizedString(String dataCodeDelimiter) {
        horizontal = term[0] + term[0];
        return root == null ? ">" + horizontal
                : toTreeDraw(root, ">", "", dataCodeDelimiter);
    }

    private String toTreeDraw(BstNode<E> node, String edge, String indent, String dataCodeDelimiter) {
        if (node == null) {
            return "";
        }
        String step = (edge.equals(leftEdge)) ? vertical : " ";
        StringBuilder sb = new StringBuilder();
        sb.append(toTreeDraw(node.right, rightEdge, indent + step, dataCodeDelimiter));
        int t = (node.right != null) ? 1 : 0;
        t = (node.left != null) ? t + 2 : t;
        sb.append(indent).append(edge).append(horizontal).append(term[t]).append(endEdge).append(
                split(node.element.toString(), dataCodeDelimiter)).append(System.lineSeparator());
        step = (edge.equals(rightEdge)) ? vertical : " ";
        sb.append(toTreeDraw(node.left, leftEdge, indent + step, dataCodeDelimiter));
        return sb.toString();
    }

    private String split(String s, String dataCodeDelimiter) {
        int k = s.indexOf(dataCodeDelimiter);
        if (k <= 0) {
            return s;
        }
        return s.substring(0, k);
    }

    /**
     * Creates and returns a copy of the set.
     *
     * @return A copy of the set.
     * @throws java.lang.CloneNotSupportedException
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        BstSet<E> cl = (BstSet<E>) super.clone();
        if (root == null) {
            return cl;
        }
        cl.root = cloneRecursive(root);
        cl.size = this.size;
        return cl;
    }

    private BstNode<E> cloneRecursive(BstNode<E> node) {
        if (node == null) {
            return null;
        }

        BstNode<E> clone = new BstNode<>(node.element);
        clone.left = cloneRecursive(node.left);
        clone.right = cloneRecursive(node.right);
        return clone;
    }

    /**
     * Returns the subset comprising elements up to a limit defined in the parameter element in a sorted manner (natural order) excluding the element.
     *
     * @param element - limit element.
     * @return the subset comprising elements up to a limit defined in the parameter element in a sorted manner (natural order) excluding the element.
     */
    @Override
    public Set<E> headSet(E element) {
        throw new UnsupportedOperationException("Students need to implement headSet()");
    }

    /**
     * Returns the subset comprising elements from element1 (inclusive) to element2 (exclusive) in a sorted manner (natural order).
     *
     * @param element1 - start element.
     * @param element2 - end element.
     * @return the subset comprising elements from element1 (inclusive) to element2 (exclusive) in a sorted manner (natural order).
     */
    @Override
    public Set<E> subSet(E element1, E element2) {
        throw new UnsupportedOperationException("Students need to implement subSet()");
    }

    /**
     * Returns the subset comprising elements starting from parameter element (inclusive) up to the end of the set in a sorted manner (natural order).
     *
     * @param element - element of the set.
     * @return the subset comprising elements starting from parameter element (inclusive) up to the end of the set in a sorted manner (natural order).
     */
    @Override
    public Set<E> tailSet(E element) {
        throw new UnsupportedOperationException("Students need to implement tailSet()");
    }

    /**
     * Returns a natural iterator.
     *
     * @return natural interator.
     */
    @Override
    public Iterator<E> iterator() {
        return new IteratorBst(true);
    }

    /**
     * Returns the inverse iterator.
     *
     * @return inverse iterator.
     */
    @Override
    public Iterator<E> descendingIterator() {
        return new IteratorBst(false);
    }

    /**
     * Internal iterator class. Iterators: ascending and
     * decreasing. The set is iterated by visiting each item once
     * inorder. All visited items are stored on the stack.
     * The stack is taken from the java.util package, but it is possible to create your own.
     */
    private class IteratorBst implements Iterator<E> {

        private final Stack<BstNode<E>> stack = new Stack<>();
        // Specifies the direction of the iterator, true for ascending, false for descending
        private final boolean ascending;
        // Required for the remove() method.
        private BstNode<E> lastInStack;
        private BstNode<E> last;

        IteratorBst(boolean ascendingOrder) {
            this.ascending = ascendingOrder;
            this.toStack(root);
        }

        @Override
        public boolean hasNext() {
            return !stack.empty();
        }

        @Override
        public E next() {// If stack is empty
            if (stack.empty()) {
                lastInStack = root;
                last = null;
                return null;
            } else {
                // Returns the top element of the stack
                BstNode<E> n = stack.pop();
                // The last returned element is saved, as well as the last element in the stack.
                // Needed for remove() method
                lastInStack = stack.isEmpty() ? root : stack.peek();
                last = n;
                BstNode<E> node = ascending ? n.right : n.left;
                // If ascending is true, the minimum element of the right sub-tree is searched,
                // and all elements in the search path are placed on the stack
                toStack(node);
                return n.element;
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Students need to implement remove()");
        }

        private void toStack(BstNode<E> node) {
            while (node != null) {
                stack.push(node);
                node = ascending ? node.left : node.right;
            }
        }
    }

    /**
     * Inner class of the collection node
     *
     * @param <N> node element data type
     */
    protected static class BstNode<N> {

        // Element
        protected N element;
        // Pointer to the left subtree
        protected BstNode<N> left;
        // Pointer to the right subtree
        protected BstNode<N> right;

        protected BstNode() {
        }

        protected BstNode(N element) {
            this.element = element;
            this.left = null;
            this.right = null;
        }
    }
}
