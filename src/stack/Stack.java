package stack;

import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.LinkedList;

public class Stack<T> implements Iterable<T> {

    private final LinkedList<T> mList = new LinkedList<T>();

    public Stack() {
    }

    public Stack(T firstElement) {
        push(firstElement);
    }

    public int size() {
        return mList.size();
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public void push(T element) {
        mList.addLast(element);
    }

    public T pop() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        return mList.removeLast();
    }

    public T peek() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        return mList.peekLast();
    }

    @Override
    public Iterator<T> iterator() {
        return mList.iterator();
    }

}