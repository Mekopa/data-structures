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
       if (index < 0 || index > size - 1) {
             throw new UnsupportedOperationException("invalid index");
       } else {
            Node currentNode = head;
             for (int i = 0; i < index; i++) {
                currentNode = currentNode.next;
             }
             return currentNode.data;
       }
    }

    @Override
    public boolean remove(T item) {
        if (size == 0) {
            return false;
        } else {
            if (item.equals(head.data)){
                head = head.next;
                if (head == null){
                    tail = null;
                }
                size = size - 1;
                return true;
            }
            Node previusNode = head;
            Node currentNode = head.next;
            while (currentNode != null){
                if(item.equals(currentNode.data)){
                previusNode.next = currentNode.next;
                if(currentNode.equals(tail)){
                    tail = previusNode;
                }
                 size = size - 1;
                 return true;
                } else {
                previusNode = currentNode;
                currentNode = currentNode.next;
                }
            }
            return false;
        }
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
