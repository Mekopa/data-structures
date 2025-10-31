package util;

import java.util.ArrayList;

/**
 * Array based implementation of stack
 * uses arrayList internally with the end of the list as the top of the stack
 * @param <E> elements in the stack
 */
public class ArrayStack<E> implements Stack<E> {

    private ArrayList<E> data;

    /**
     * constructs an empty stack
     */
    public ArrayStack() {
        data = new ArrayList<>();
    }

    /**
     * adds new element to top of the stack (end of the array)
     * @param e new element object
    */
    @Override
    public void push(E e) {
        data.add(e);
    }

    /**
     * removes the top element from stack (end of the array)
     * @return deleted data
    */
    @Override
    public E pop() {
        if (isEmpty()) {
            return null;
        } 
        return data.remove(data.size() - 1);
    }

    /**
     * returns the top element from stack
     * @return last element
    */
    @Override
    public E peek() {
        if(isEmpty()) {
            return null;
        }
        return data.get(data.size() - 1);
    }

    /**
     * checks if the stack is empty or not
     * @return result of is is empty or not as bool
    */
    @Override
    public boolean isEmpty() {
        return data.isEmpty();
    }
}
