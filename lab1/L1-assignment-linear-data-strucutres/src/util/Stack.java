package util;

/**
 * Stack interface - LIFO (Last In, First Out) data structure
 *
 * A Stack allows insertion and removal of elements from one end only (the "top").
 * Think of it like a stack of plates - you can only add or remove from the top.
 *
 * @param <E> the type of elements in this stack
 */
public interface Stack<E> {

    /**
    * adds new element to the stack
    * @param e element that gonna be added
    */
    void push(E e);

    /**
    * removes top element from the stack
    * @return the revomed element from the stack
    */
    E pop();

    /**
    * gets the top element from the stack
    * @return the top element from the stack
    */
    E peek();

    /**
    * checkes if the stack is empty or not
    * @return the bool result of is stack empty or not
    */
    boolean isEmpty();


}
