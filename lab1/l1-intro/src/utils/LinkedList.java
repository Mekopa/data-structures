package utils;

import java.util.Iterator;

/*
Implement all the methods of the interface on the basis of a linked list.
Do not use the java class LinkedList<E>, try to write all the logic yourself.
Additional methods and variables can be created if needed.
*/
public class LinkedList<T> implements List<T> {
    

    @Override
    public void add(T item) {
        throw new UnsupportedOperationException("Method needs to be implemented");
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
