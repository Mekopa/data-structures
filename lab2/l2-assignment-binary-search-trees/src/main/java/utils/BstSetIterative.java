package utils;

import java.util.Comparator;

/**
 * The class inherits the BstSet class and overwrites the add method with an iterative implementation
 *
 * @param <E>
 * @author darius
 */
public class BstSetIterative<E extends Comparable<E>> extends BstSet<E> implements SortedSet<E> {

    public BstSetIterative() {
        super();
    }

    public BstSetIterative(Comparator<? super E> c) {
        super(c);
    }

    /**
     * Adds new element to the set
     *
     * @param element
     */
    @Override
    public void add(E element) {
        if (element == null) {
            throw new IllegalArgumentException("Element is null in add(E element)");
        }

        if (root == null) {
            root = new BstNode<E>(element);
        } else {
            BstNode<E> current = root;
            BstNode<E> parent = null;
            while (current != null) {
                parent = current;
                int cmp = c.compare(element, current.element);
                if (cmp < 0) {
                    current = current.left;
                } else if (cmp > 0) {
                    current = current.right;
                } else {
                    return;
                }
            }

            int cmp = c.compare(element, parent.element);
            if (cmp < 0) {
                parent.left = new BstNode<E>(element);
            } else if (cmp > 0) {
                parent.right = new BstNode<E>(element);
            }
        }
        size++;
    }
}
