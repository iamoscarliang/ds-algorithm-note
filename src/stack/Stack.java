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
            throw new RuntimeException("Empty stack");
        }
        return mList.removeLast();
    }

    public T peek() {
        if (isEmpty()) {
            throw new RuntimeException("Empty stack");
        }
        return mList.peekLast();
    }

    @Override
    public Iterator<T> iterator() {
        return mList.iterator();
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            int count = size() - 1;
            for (int i = 0; i < count; i++) {
                sb.append(mList.get(i) + ", ");
            }
            sb.append(mList.get(count) + "]");
            return sb.toString();
        }
    }

}