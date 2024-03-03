package ds.linkedlist;

import java.util.Iterator;

public class LinkedList<T> implements Iterable<T> {

    private Node<T> mHead = null;
    private Node<T> mTail = null;
    private int mSize = 0;

    public int size() {
        return mSize;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    // O(n)
    public void clear() {
        Node<T> node = mHead;
        while (node != null) {
            Node<T> next = node.mNext;
            node.mPrev = node.mNext = null;
            node.mData = null;
            node = next;
        }
        mHead = mTail = null;
        mSize = 0;
    }

    // O(1)
    public void add(T element) {
        addLast(element);
    }

    // O(1)
    public void addLast(T element) {
        if (isEmpty()) {
            mHead = mTail = new Node<T>(element, null, null);
        } else {
            mTail.mNext = new Node<T>(element, mTail, null);
            mTail = mTail.mNext;
        }
        mSize++;
    }

    // O(1)
    public void addFirst(T elem) {
        if (isEmpty()) {
            mHead = mTail = new Node<T>(elem, null, null);
        } else {
            mHead.mPrev = new Node<T>(elem, null, mHead);
            mHead = mHead.mPrev;
        }
        mSize++;
    }

    // O(1)
    public T removeFirst() {
        if (isEmpty()) {
            throw new RuntimeException("Empty list");
        }

        T data = mHead.mData;
        mHead = mHead.mNext;
        mSize--;

        if (isEmpty()) {
            mTail = null;
        } else {
            mHead.mPrev = null;
        }

        return data;
    }

    // O(1)
    public T removeLast() {
        if (isEmpty()) {
            throw new RuntimeException("Empty list");
        }

        T data = mTail.mData;
        mTail = mTail.mPrev;
        mSize--;

        if (isEmpty()) {
            mHead = null;
        } else {
            mTail.mNext = null;
        }

        return data;
    }

    // O(1)
    private T remove(Node<T> node) {
        if (node.mPrev == null) {
            return removeFirst();
        }
        if (node.mNext == null) {
            return removeLast();
        }

        node.mNext.mPrev = node.mPrev;
        node.mPrev.mNext = node.mNext;

        T data = node.mData;
        node.mData = null;
        node.mPrev = node.mNext = null;
        mSize--;

        return data;
    }

    // O(n)
    public T removeAt(int index) {
        if (index < 0 || index >= mSize) {
            throw new IllegalArgumentException();
        }

        Node<T> node;

        if (index < mSize / 2) {
            // Search from the front of the list
            int i = 0;
            node = mHead;
            while (i != index) {
                node = node.mNext;
                i++;
            }
        } else {
            // Search from the back of the list
            int i = mSize - 1;
            node = mTail;
            while (i != index) {
                node = node.mPrev;
                i--;
            }
        }

        return remove(node);
    }

    // O(n)
    public boolean remove(T element) {
        Node<T> node = mHead;

        if (element == null) {
            while (node != null) {
                if (node.mData == null) {
                    remove(node);
                    return true;
                }
                node = node.mNext;
            }
        } else {
            while (node != null) {
                if (element.equals(node.mData)) {
                    remove(node);
                    return true;
                }
                node = node.mNext;
            }
        }
        return false;
    }

    // O(n)
    public int indexOf(T element) {
        int index = 0;
        Node<T> node = mHead;

        // Check is element null
        if (element == null) {
            while (node != null) {
                if (node.mData == null) {
                    return index;
                }
                node = node.mNext;
                index++;
            }
        } else {
            while (node != null) {
                if (element.equals(node.mData)) {
                    return index;
                }
                node = node.mNext;
                index++;
            }
        }

        return -1;
    }

    // O(1)
    public T peekFirst() {
        if (isEmpty()) {
            throw new RuntimeException("Empty list");
        }
        return mHead.mData;
    }

    // O(1)
    public T peekLast() {
        if (isEmpty()) {
            throw new RuntimeException("Empty list");
        }
        return mTail.mData;
    }

    // O(n)
    public boolean contains(T element) {
        return indexOf(element) != -1;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {

            private Node<T> node = mHead;

            @Override
            public boolean hasNext() {
                return node != null;
            }

            @Override
            public T next() {
                T data = node.mData;
                node = node.mNext;
                return data;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

        };
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        Node<T> node = mHead;
        while (node != null) {
            sb.append(node.mData + ", ");
            node = node.mNext;
        }
        sb.append("]");
        return sb.toString();
    }

}