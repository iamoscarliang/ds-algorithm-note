package array;

import java.util.Iterator;

public class DynamicArray<T> implements Iterable<T> {

    private T[] mArray;
    private int mSize = 0;   // The number of elements currently inside the array
    private int mCapacity = 0;   // The internal capacity of the array

    public DynamicArray() {
        this(16);
    }

    public DynamicArray(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("Illegal Capacity: " + capacity);
        }
        mCapacity = capacity;
        mArray = (T[]) new Object[capacity];
    }

    public int size() {
        return mSize;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public T get(int index) {
        return mArray[index];
    }

    public void set(int index, T element) {
        mArray[index] = element;
    }

    public void clear() {
        for (int i = 0; i < mSize; i++) {
            mArray[i] = null;
        }
        mSize = 0;
    }

    public void add(T element) {
        // Check is resize needed
        if (mSize + 1 >= mCapacity) {
            if (mCapacity == 0) {
                mCapacity = 1;
            } else {
                mCapacity *= 2;   // Double the size
            }
            // Copy all element from old array to new  array
            T[] newArray = (T[]) new Object[mCapacity];
            for (int i = 0; i < mSize; i++) {
                newArray[i] = mArray[i];
            }
            mArray = newArray;
        }

        mArray[mSize++] = element;
    }

    public T removeAt(int index) {
        if (index >= mSize || index < 0) {
            throw new IndexOutOfBoundsException();
        }
        T removeElement = mArray[index];
        T[] newArray = (T[]) new Object[mSize - 1];
        for (int i = 0, j = 0; i < mSize; i++, j++) {
            if (i == index) {
                j--;   // Skip removed element
            } else {
                newArray[j] = mArray[i];
            }
        }
        mArray = newArray;
        mCapacity = --mSize;
        return removeElement;
    }

    public boolean remove(Object obj) {
        int index = indexOf(obj);
        if (index == -1) {
            return false;
        }
        removeAt(index);
        return true;
    }

    public int indexOf(Object obj) {
        for (int i = 0; i < mSize; i++) {
            if (obj == null) {
                if (mArray[i] == null) {
                    return i;
                }
            } else {
                if (obj.equals(mArray[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    public boolean contains(Object obj) {
        return indexOf(obj) != -1;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            int index = 0;

            @Override
            public boolean hasNext() {
                return index < mSize;
            }

            @Override
            public T next() {
                return mArray[index++];
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public String toString() {
        if (mSize == 0) {
            return "[]";
        } else {
            StringBuilder sb = new StringBuilder(mSize);
            sb.append("[");
            for (int i = 0; i < mSize - 1; i++) {
                sb.append(mArray[i] + ", ");
            }
            sb.append(mArray[mSize - 1] + "]");
            return sb.toString();
        }
    }

}
