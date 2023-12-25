package stack;

import java.util.Iterator;
import java.util.LinkedList;

public class Stack<T> implements Iterable<T> {

    private final LinkedList<T> mList = new LinkedList<T>();

    public Stack() {
    }

    public Stack(T firstElement) {
        push(firstElement);
    }

    // Return the number of elements in the stack
    public int size() {
        return mList.size();
    }

    // Check if the stack is empty
    public boolean isEmpty() {
        return size() == 0;
    }

    // Push an element on the stack
    public void push(T element) {
        mList.addLast(element);
    }

    // Pop an element off the stack
    public T pop() {
        if (isEmpty()) {
            throw new java.util.EmptyStackException();
        }
        return mList.removeLast();
    }

    // Peek the top of the stack without removing an element
    public T peek() {
        if (isEmpty()) {
            throw new java.util.EmptyStackException();
        }
        return mList.peekLast();
    }

    @Override
    public Iterator<T> iterator() {
        return mList.iterator();
    }

}
