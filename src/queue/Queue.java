package queue;

import java.util.Iterator;
import java.util.LinkedList;

public class Queue<T> implements Iterable<T> {

    private final LinkedList<T> mList = new LinkedList<T>();

    public Queue() {
    }

    public Queue(T firstElem) {
        offer(firstElem);
    }

    public int size() {
        return mList.size();
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public void offer(T element) {
        mList.addLast(element);
    }

    public T poll() {
        if (isEmpty()) {
            throw new RuntimeException("Empty queue");
        }
        return mList.removeFirst();
    }

    public T peek() {
        if (isEmpty()) {
            throw new RuntimeException("Empty queue");
        }
        return mList.peekFirst();
    }

    @Override
    public Iterator<T> iterator() {
        return mList.iterator();
    }

}