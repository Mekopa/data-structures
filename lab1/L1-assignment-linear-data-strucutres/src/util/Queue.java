/**
 * Queue interface implementing FIFO (First-In-First-Out) behavior
 *
 * A Queue is a linear data structure where:
 * - Elements are added at the REAR (enqueue)
 * - Elements are removed from the FRONT (dequeue)
 * - The first element added is the first one removed
 *
 * Common applications:
 * - Task scheduling (print queue, CPU scheduling)
 * - Breadth-first search in graphs/trees
 * - Buffering (keyboard buffer, network packets)
 *
 * @param <E> the type of elements held in this queue
 *
 * @author Mekopa
 */
package util;

public interface Queue<E> {

    /**
     * Adds a new element to the end (rear) of the queue
     *
     * Time complexity: O(1) amortized
     *
     * @param item the element to add
     * @throws IllegalArgumentException if item is null
     */
    void enqueue(E item);

    /**
     * Removes and returns the element at the front of the queue
     *
     * Time complexity: O(1) amortized
     *
     * @return the front element, or null if queue is empty
     */
    E dequeue();

    /**
     * Returns (but does not remove) the element at the front of the queue
     *
     * Time complexity: O(1)
     *
     * @return the front element, or null if queue is empty
     */
    E peek();

    /**
     * Checks if the queue is empty
     *
     * Time complexity: O(1)
     *
     * @return true if the queue contains no elements, false otherwise
     */
    boolean isEmpty();
}
