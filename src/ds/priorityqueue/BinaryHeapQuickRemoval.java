package ds.priorityqueue;

import java.util.*;

public class BinaryHeapQuickRemoval<T extends Comparable<T>> {

    private final List<T> mHeap;
    private int mSize = 0;   // The number of elements currently inside the heap
    private int mCapacity = 0;   // The internal capacity of the heap

    // This map keeps track of the possible indices a particular
    // node value is found in the heap. Having this mapping lets
    // us have O(log(n)) removals and O(1) element containment check
    // at the cost of some additional space and minor overhead
    private final Map<T, TreeSet<Integer>> mMap = new HashMap<>();

    // Construct and initially empty priority ds.queue
    public BinaryHeapQuickRemoval() {
        this(1);
    }

    // Construct a priority ds.queue with an initial capacity
    public BinaryHeapQuickRemoval(int capacity) {
        mHeap = new ArrayList<>(capacity);
    }

    // Construct a priority ds.queue using heapify in O(n) time, a great explanation can be found at:
    public BinaryHeapQuickRemoval(T[] elements) {
        mSize = mCapacity = elements.length;
        mHeap = new ArrayList<T>(mCapacity);

        // Place all element in heap
        for (int i = 0; i < mSize; i++) {
            mapAdd(elements[i], i);
            mHeap.add(elements[i]);
        }

        // Heapify process, O(n)
        for (int i = Math.max(0, (mSize / 2) - 1); i >= 0; i--) {
            bubbleDown(i);
        }
    }

    // Priority ds.queue construction, O(nlog(n))
    public BinaryHeapQuickRemoval(Collection<T> elements) {
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
        mMap.clear();
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

    // Check is element contained in the heap, O(1)
    public boolean contains(T element) {
        // Map lookup to check containment, O(1)
        if (element == null) {
            return false;
        }
        return mMap.containsKey(element);

        // Linear check to check containment, O(n)
        // for (int i = 0; i < mSize; i++) {
        //     if (mHeap.get(i).equals(element)) {
        //         return true;
        //     }
        // }
        // return false;
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

        mapAdd(element, mSize);

        bubbleUp(mSize);
        mSize++;
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

    // Removes a particular element in the heap, O(log(n))
    public boolean remove(T element) {
        if (element == null) {
            return false;
        }

        // Logarithmic removal with map, O(log(n))
        Integer index = mapGet(element);
        if (index != null) {
            removeAt(index);
        }
        return index != null;

        // Linear removal via search, O(n)
        // for (int i = 0; i < mSize; i++) {
        //     if (element.equals(mHeap.get(i))) {
        //         removeAt(i);
        //         return true;
        //     }
        // }
        // return false;
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

        mapRemove(removedElement, mSize);

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

    // Add a node value and its index to the map
    private void mapAdd(T value, int index) {

        // Get the index set from the value in map
        TreeSet<Integer> set = mMap.get(value);

        // Check is value exists in map
        if (set == null) {
            // New value being inserted in map
            set = new TreeSet<>();
            set.add(index);
            mMap.put(value, set);
        } else {
            // Value already exists in map
            set.add(index);
        }
    }

    // Removes the index at a given value, O(log(n))
    private void mapRemove(T value, int index) {
        TreeSet<Integer> set = mMap.get(value);
        set.remove(index);   // TreeSets take O(log(n)) removal time
        if (set.isEmpty()) {
            mMap.remove(value);
        }
    }

    // Extract an index position for the given value
    // NOTE: If a value exists multiple times in the heap the highest
    // index is returned (this has arbitrarily been chosen)
    private Integer mapGet(T value) {
        TreeSet<Integer> set = mMap.get(value);
        if (set != null) {
            return set.last();
        }
        return null;
    }

    @Override
    public String toString() {
        return mHeap.toString();
    }

}
