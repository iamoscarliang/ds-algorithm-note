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

    // Return the size of the queue
    public int size() {
        return mList.size();
    }

    // Returns whether the queue is empty
    public boolean isEmpty() {
        return size() == 0;
    }

    // Peek the element at the front of the queue
    public T peek() {
        if (isEmpty()) {
            throw new RuntimeException("Queue Empty");
        }
        return mList.peekFirst();
    }

    // Poll an element from the front of the queue
    public T poll() {
        if (isEmpty()) {
            throw new RuntimeException("Queue Empty");
        }
        return mList.removeFirst();
    }

    // Add an element to the back of the queue
    public void offer(T element) {
        mList.addLast(element);
    }

    @Override
    public Iterator<T> iterator() {
        return mList.iterator();
    }

}
