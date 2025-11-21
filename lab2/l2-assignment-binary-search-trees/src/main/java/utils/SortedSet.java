package utils;

import java.util.Iterator;

public interface SortedSet<E> extends Set<E> {

    /**
     * Returns the subset comprising elements up to a limit defined in the parameter element in a sorted manner (natural order) excluding the element.
     *
     * @param element - limit element.
     * @return the subset comprising elements up to a limit defined in the parameter element in a sorted manner (natural order) excluding the element.
     */
    Set<E> headSet(E element);

    /**
     * Returns the subset comprising elements from element1 (inclusive) to element2 (exclusive) in a sorted manner (natural order).
     *
     * @param element1 - start element.
     * @param element2 - end element.
     * @return the subset comprising elements from element1 (inclusive) to element2 (exclusive) in a sorted manner (natural order).
     */
    Set<E> subSet(E element1, E element2);

    /**
     * Returns the subset comprising elements starting from parameter element (inclusive) up to the end of the set in a sorted manner (natural order).
     *
     * @param element - element of the set.
     * @return the subset comprising elements starting from parameter element (inclusive) up to the end of the set in a sorted manner (natural order).
     */
    Set<E> tailSet(E element);

    /**
     * Returns a descending iterator.
     *
     * @return descending iterator.
     */
    Iterator<E> descendingIterator();
}
