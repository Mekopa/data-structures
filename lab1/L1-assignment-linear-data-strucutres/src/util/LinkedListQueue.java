package util;

/**
 * Linked list based implementation of Queue
 *
 * Uses LinkedList internally:
 *   Front (index 0) ← [first] → [second] → [third] → [last] → Rear (end)
 *
 * Operations:
 *   - enqueue(e): add to END of linked list → O(1)
 *   - dequeue(): remove from BEGINNING (index 0) → O(1)
 *   - peek(): view BEGINNING (index 0) → O(1)
 *
 * Advantage over ArrayQueue: dequeue() is true O(1), not O(n)
 *
 * @param <E> the type of elements held in this queue
 * @author Mekopa
 */
public class LinkedListQueue<E extends Comparable<E>> implements Queue<E> {

    private LinkedList<E> data;

    /**
     * Constructs an empty queue
     */
    public LinkedListQueue() {
        data = new LinkedList<>();
    }

    /**
     * Adds a new element to the rear (end) of the queue
     *
     * Think: How do you add to the END of a LinkedList?
     * Hint: Look at how you added to the end in LinkedList.add(e)
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
     * Think: How do you remove from index 0 in LinkedList?
     * Hint: This is EXACTLY the same as LinkedListStack.pop()!
     *
     * @return the front element, or null if queue is empty
     */
    @Override
    public E dequeue() {
        if (isEmpty()) {
            return null;
        }
        return data.remove(0);
    }

    /**
     * Returns (but does not remove) the element at the front of the queue
     *
     * Think: How do you view the element at index 0 without removing?
     * Hint: This is EXACTLY the same as LinkedListStack.peek()!
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
