package ds.priorityqueue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BinaryHeap<T extends Comparable<T>> {

    private final List<T> mHeap;
    private int mSize = 0;   // The number of elements currently inside the heap
    private int mCapacity = 0;   // The internal capacity of the heap

    // Construct and initially empty priority ds.queue
    public BinaryHeap() {
        this(1);
    }

    // Construct a priority ds.queue with an initial capacity
    public BinaryHeap(int capacity) {
        mHeap = new ArrayList<>(capacity);
    }

    // Construct a priority ds.queue using heapify in O(n) time, a great explanation can be found at:
    public BinaryHeap(T[] elements) {
        mSize = mCapacity = elements.length;
        mHeap = new ArrayList<T>(mCapacity);

        // Place all element in heap
        for (int i = 0; i < mSize; i++) {
            mHeap.add(elements[i]);
        }

        // Heapify process, O(n)
        for (int i = Math.max(0, (mSize / 2) - 1); i >= 0; i--) {
            bubbleDown(i);
        }
    }

    // Priority ds.queue construction, O(nlog(n))
    public BinaryHeap(Collection<T> elements) {
        this(elements.size());
        for (T elem : elements) {
            add(elem);
        }
    }

    // Return the size of the heap
    public int size() {
        return mSize;
    }

    // Returns if the heap contains no elements
    public boolean isEmpty() {
        return mSize == 0;
    }

    // Clear everything in the heap, O(n)
    public void clear() {
        for (int i = 0; i < mCapacity; i++) {
            mHeap.set(i, null);
        }
        mSize = 0;
    }

    // Returns the element with the lowest priority
    public T peek() {
        if (isEmpty()) {
            return null;
        }
        return mHeap.get(0);
    }

    // Removes the root of the heap, O(log(n))
    public T poll() {
        return removeAt(0);
    }

    // Check is element contained in the heap, O(n)
    public boolean contains(T element) {
        for (int i = 0; i < mSize; i++) {
            if (mHeap.get(i).equals(element)) {
                return true;
            }
        }
        return false;
    }

    // Adds an element to the heap, O(log(n))
    public void add(T element) {
        if (element == null) {
            throw new IllegalArgumentException();
        }

        if (mSize < mCapacity) {
            mHeap.set(mSize, element);
        } else {
            mHeap.add(element);
            mCapacity++;
        }

        bubbleUp(mSize);
        mSize++;
    }

    // Removes a particular element in the heap, O(n)
    public boolean remove(T element) {
        if (element == null) {
            return false;
        }
        // Linear removal via search, O(n)
        for (int i = 0; i < mSize; i++) {
            if (element.equals(mHeap.get(i))) {
                removeAt(i);
                return true;
            }
        }
        return false;
    }

    // Removes a node at particular index, O(log(n))
    private T removeAt(int index) {
        if (isEmpty()) {
            return null;
        }

        mSize--;
        T removedElement = mHeap.get(index);

        // Swap the last element with removed element and clear it
        swap(index, mSize);
        mHeap.set(mSize, null);

        // Check if the last element was removed
        if (index == mSize) {
            return removedElement;
        }
        T element = mHeap.get(index);

        // Try bubble down element
        bubbleDown(index);

        // If bubble down did not work try bubble up
        if (mHeap.get(index).equals(element)) {
            bubbleUp(index);
        }
        return removedElement;
    }

    // Perform bottom up node adjustment, O(log(n))
    private void bubbleUp(int index) {
        int child = index;
        int parent = (child - 1) / 2;   // Parent node

        // Check is not reached the root and less than the parent.
        while (child > 0 && less(child, parent)) {
            // Swap child and parent
            swap(parent, child);

            // Move to the next pair of child and parent
            child = parent;
            parent = (child - 1) / 2;
        }
    }

    // Perform top down node adjustment, O(log(n))
    private void bubbleDown(int index) {
        int parent = index;
        while (true) {
            int leftChild = 2 * parent + 1; // Left  child
            int rightChild = 2 * parent + 2; // Right child
            int smallestChild = leftChild;   // Assume left is the smallest one of the two children

            // Check which is smaller
            if (rightChild < mSize && less(rightChild, leftChild)) {
                smallestChild = rightChild;
            }

            // Stop if we're out of bound or the parent less than child
            if (leftChild >= mSize || less(parent, smallestChild)) {
                break;
            }

            // Swap child and parent
            swap(smallestChild, parent);
            parent = smallestChild;
        }
    }

    // Test if the value of node i <= node j
    private boolean less(int i, int j) {
        T nodeA = mHeap.get(i);
        T nodeB = mHeap.get(j);
        return nodeA.compareTo(nodeB) <= 0;
    }

    // Swap two nodes
    private void swap(int i, int j) {
        T nodeA = mHeap.get(i);
        T nodeB = mHeap.get(j);
        mHeap.set(i, nodeB);
        mHeap.set(j, nodeA);
    }

    // Checks if this heap is a min heap from the root
    public boolean isMinHeap() {
        return isMinHeap(0);
    }

    // Recursively checks if this heap is a min heap
    public boolean isMinHeap(int startIndex) {
        // If we are outside the bounds of the heap return true
        if (startIndex >= mSize) {
            return true;
        }

        int leftChild = 2 * startIndex + 1;
        int rightChild = 2 * startIndex + 2;

        // Check is current node is less than both of its children if they exist
        if (leftChild < mSize && !less(startIndex, leftChild)) {
            return false;
        }
        if (rightChild < mSize && !less(startIndex, rightChild)) {
            return false;
        }

        // Recurse check on both children
        return isMinHeap(leftChild) && isMinHeap(rightChild);
    }

    @Override
    public String toString() {
        return mHeap.toString();
    }

}
