package arraylist;

import java.util.Iterator;

public class ArrayList<T> implements Iterable<T> {

    private T[] mArray;
    private int mSize = 0;
    private int mCapacity;

    public ArrayList() {
        this(10);
    }

    @SuppressWarnings("unchecked")
    public ArrayList(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("Negative capacity");
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

    @SuppressWarnings("unchecked")
    public void add(T element) {
        // Check is resize needed
        if (mSize + 1 >= mCapacity) {
            if (mCapacity == 0) {
                mCapacity = 1;
            } else {
                // Double the size
                mCapacity *= 2;
            }
            // Copy all element from old array to new array
            T[] newArray = (T[]) new Object[mCapacity];
            for (int i = 0; i < mSize; i++) {
                newArray[i] = mArray[i];
            }
            mArray = newArray;
        }

        mArray[mSize++] = element;
    }

    @SuppressWarnings("unchecked")
    public T removeAt(int index) {
        if (index >= mSize || index < 0) {
            throw new IndexOutOfBoundsException();
        }
        T removeElement = mArray[index];
        T[] newArray = (T[]) new Object[mSize - 1];
        for (int i = 0, j = 0; i < mSize; i++, j++) {
            if (i == index) {
                j--;
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
        if (isEmpty()) {
            return "[]";
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            for (int i = 0; i < mSize - 1; i++) {
                sb.append(mArray[i] + ", ");
            }
            sb.append(mArray[mSize - 1] + "]");
            return sb.toString();
        }
    }

}