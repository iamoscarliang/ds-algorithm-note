package linkedlist;

import java.util.Iterator;

public class DoublyLinkedList<T> implements Iterable<T> {

    private Node<T> mHead = null;
    private Node<T> mTail = null;
    private int mSize = 0;

    // Return the size of the linked list
    public int size() {
        return mSize;
    }

    // Returns if the list contains no elements
    public boolean isEmpty() {
        return size() == 0;
    }

    // Clear everything in the linked list, O(n)
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

    // Add a node to the end of the linked list, O(1)
    public void add(T element) {
        addLast(element);
    }

    // Add a node to the tail of the linked list, O(1)
    public void addLast(T element) {
        if (isEmpty()) {
            mHead = mTail = new Node<T>(element, null, null);
        } else {
            mTail.mNext = new Node<T>(element, mTail, null);
            mTail = mTail.mNext;
        }
        mSize++;
    }

    // Add an element to the head of the linked list, O(1)
    public void addFirst(T elem) {
        if (isEmpty()) {
            mHead = mTail = new Node<T>(elem, null, null);
        } else {
            mHead.mPrev = new Node<T>(elem, null, mHead);
            mHead = mHead.mPrev;
        }
        mSize++;
    }

    // Check the value of the head node if it exists, O(1)
    public T peekFirst() {
        if (isEmpty()) {
            throw new RuntimeException("Empty list");
        }
        return mHead.mData;
    }

    // Check the value of the last node if it exists, O(1)
    public T peekLast() {
        if (isEmpty()) {
            throw new RuntimeException("Empty list");
        }
        return mTail.mData;
    }

    // Remove the first value at the head of the linked list, O(1)
    public T removeFirst() {
        if (isEmpty()) {
            throw new RuntimeException("Empty list");
        }

        // Extract the data we want to return
        T data = mHead.mData;
        // Move the head pointer forwards one node
        mHead = mHead.mNext;
        mSize--;

        // If the list is empty set the tail to null
        if (isEmpty()) {
            mTail = null;
        } else {
            // Do a memory cleanup of the previous node
            mHead.mPrev = null;
        }

        // Return the data we extracted
        return data;
    }

    // Remove the last value at the tail of the linked list, O(1)
    public T removeLast() {
        // Can't remove data from an empty list
        if (isEmpty()) {
            throw new RuntimeException("Empty list");
        }

        // Extract the data we want to return
        T data = mTail.mData;
        // Move the tail pointer backwards one node
        mTail = mTail.mPrev;
        mSize--;

        // If the list is empty set the head to null
        if (isEmpty()) {
            mHead = null;
        } else {
            // Do a memory clean of the next node
            mTail.mNext = null;
        }

        // Return the data we extracted
        return data;
    }

    // Remove an arbitrary node from the linked list, O(1)
    private T remove(Node<T> node) {
        // Check if the node to remove is at head or tail
        if (node.mPrev == null) {
            return removeFirst();
        }
        if (node.mNext == null) {
            return removeLast();
        }

        // Make the pointers of adjacent nodes skip over 'node'
        node.mNext.mPrev = node.mPrev;
        node.mPrev.mNext = node.mNext;

        // Extract the data we want to return
        T data = node.mData;

        // Memory cleanup
        node.mData = null;
        node = node.mPrev = node.mNext = null;

        mSize--;

        // Return the data in the node we just removed
        return data;
    }

    // Remove a node at a particular index, O(n)
    public T removeAt(int index) {
        // Make sure the index provided is valid
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

    // Remove a particular element in the linked list, O(n)
    public boolean remove(T element) {
        Node<T> node = mHead;

        // Check is element null
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

    // Find the index of a particular element in the linked list, O(n)
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

    // Check is element contained in the linked list, O(n)
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
        sb.append("[ ");
        Node<T> node = mHead;
        while (node != null) {
            sb.append(node.mData + ", ");
            node = node.mNext;
        }
        sb.append(" ]");
        return sb.toString();
    }

    private static class Node<T> {
        private T mData;
        private Node<T> mPrev;
        private Node<T> mNext;

        public Node(T data, Node<T> prev, Node<T> next) {
            mData = data;
            mPrev = prev;
            mNext = next;
        }

        @Override
        public String toString() {
            return mData.toString();
        }

    }

}
