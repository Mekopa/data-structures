package util;

/**
 * Linked list based implementation of stack
 * uses LinkedList internally with the beginning (index 0) as the top of the stack
 * @param <E> elements in the stack
 */
public class LinkedListStack<E extends Comparable<E>> implements Stack<E> {

    private LinkedList<E> data;

    /**
     * constructs an empty stack
     */
    public LinkedListStack() {
        data = new LinkedList<>();
    }

    @Override
    public void push(E e) {
        data.add(0, e);
    }

    @Override
    public E pop() {
        if (isEmpty()) {
            return null;
        }
        return data.remove(0);
    }

    @Override
    public E peek() {
        if (isEmpty()) {
            return null;
        }
        return data.get(0);
    }

    @Override
    public boolean isEmpty() {
        return data.isEmpty();
    }
}
