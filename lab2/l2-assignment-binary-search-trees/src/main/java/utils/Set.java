package utils;

/**
 * Interface for Set ADT.
 *
 * @param <E> type of the set element
 */
public interface Set<E> extends Iterable<E> {

    //Check if the array is empty.
    boolean isEmpty();

    // Returns the number of elements in the array.
    int size();

    // Clears the array.
    void clear();

    // Adds a new element to the array.
    void add(E element);

    // The elements of an input set are added to the existing set, if both arrays have the same element, it is not added.
    void addAll(Set<E> set);

    // Removes an element from the set.
    void remove(E element);

    // remain only those elements that exist in both sets (set intersection)
    void retainAll(Set<E> set);

    // Checks if the element exists in the set.
    boolean contains(E element);

    // Checks if all elements of the input set exist in the set.
    boolean containsAll(Set<E> set);

    // Converts set to array of Objects
    Object[] toArray();

    // Converts set to array
    E[] toArray(Class<E> elementClass);

    // Produces a string, whereby the set is visualised as a binary search tree
    String toVisualizedString(String dataCodeDelimiter);
}
