package util;

import java.util.ArrayList;

/**
 * Array-based implementation of Queue using ArrayList
 *
 * Internal structure:
 *   Front (index 0) ← [10, 20, 30, 40] → Rear (end)
 *
 * Operations:
 *   - enqueue(50): add to rear → [10, 20, 30, 40, 50]
 *   - dequeue(): remove from front → returns 10, queue becomes [20, 30, 40, 50]
 *   - peek(): view front → returns 20 (doesn't remove)
 *
 * @param <E> the type of elements held in this queue
 * @author Mekopa
 */
public class ArrayQueue<E> implements Queue<E> {

    private ArrayList<E> data;

    /**
     * Constructs an empty queue
     */
    public ArrayQueue() {
        data = new ArrayList<>();
    }

    /**
     * Adds a new element to the rear (end) of the queue
     *
     * Think: Which ArrayList method adds to the end?
     * Hint: Same as Stack.push()
     *
     * @param item the element to add
     */
    @Override
    public void enqueue(E e) {
        data.add(e);
    }

    /**
     * Removes and returns the element at the front of the queue
     *
     * Think: How do you remove from index 0 in ArrayList?
     * Remember: Return null if queue is empty (check isEmpty() first)
     *
     * @return the front element, or null if queue is empty
     */
    @Override
    public E dequeue() {
        if (data.isEmpty()) {
            return null;
        }
        return data.remove(0);
    }

    /**
     * Returns (but does not remove) the element at the front of the queue
     *
     * Think: How do you access the first element without removing it?
     * Hint: ArrayList has a get(int index) method
     *
     * @return the front element, or null if queue is empty
     */
    @Override
    public E peek() {
        if (isEmpty()) {
            return null;
        }
        return data.get(0);
    }

    /**
     * Checks if the queue is empty
     *
     * @return true if the queue contains no elements, false otherwise
     */
    @Override
    public boolean isEmpty() {
        return data.isEmpty();
    }
}
