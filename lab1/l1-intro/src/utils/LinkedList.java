package utils;

import java.util.Iterator;

/*
Implement all the methods of the interface on the basis of a linked list.
Do not use the java class LinkedList<E>, try to write all the logic yourself.
Additional methods and variables can be created if needed.
*/
public class LinkedList<T> implements List<T> {
    
    private class Node {
        T data;
        Node next;

        Node(T data){
            this.data = data;
            this.next = null;
        }
    }

    private Node head;
    private Node tail;
    private int size;

    public LinkedList(){
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    @Override
    public void add(T item) { 
        Node newNode = new Node(item);
        if (size == 0) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
        size = size + 1;
    }

    @Override
    public T get(int index) {
        throw new UnsupportedOperationException("Method needs to be implemented");
    }

    @Override
    public boolean remove(T item) {
        throw new UnsupportedOperationException("Method needs to be implemented");
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            
            @Override
            public boolean hasNext() {
                throw new UnsupportedOperationException("Method needs to be implemented");
            }

            @Override
            public T next() {
                throw new UnsupportedOperationException("Method needs to be implemented");
            }
        };
    }
}
