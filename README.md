# ds-algorithm-note

Data Structure & Algorithm in Java

---

## :pushpin: Content
- :books: [Stack](#books-stack)
- :people_holding_hands: [Queue](#peopleholdinghands-queue)
- :1234: [Arraylist](#1234-arraylist)
- :link: [Linkedlist](#link-linkedlist)

## :books: Stack
Stack is a linear data structure that follows an order in LIFO (Last In First Out).

```java
public class Stack<T> implements Iterable<T> {

    private final LinkedList<T> mList = new LinkedList<T>();

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
    //...

}
```

To use the stack...

```java
Stack<String> stack = new Stack<>();

stack.push("Java");
stack.push("Kotlin");
System.out.println(stack);

stack.pop();
System.out.println(stack);
```

Which will print...

```java
[Java, Kotlin]
[Java]
```

## :people_holding_hands: Queue
Queue is a linear data structure that follows an order in FIFO (First In First Out).

```java
public class Queue<T> implements Iterable<T> {

    private final LinkedList<T> mList = new LinkedList<T>();

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
    //...

}
```

To use the queue...

```java
Queue<String> queue = new Queue<>();

queue.offer("Java");
queue.offer("Kotlin");
System.out.println(queue.peek());

queue.poll();
System.out.println(queue.peek());
```

Which will print...

```java
[Java, Kotlin]
[Kotlin]
```

## :1234: Arraylist
Arraylist (or Dynamic Array) is a random access, variable-size linear data structure that allows elements to be added or removed.

```java
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

    public T get(int index) {
        return mArray[index];
    }

    public void set(int index, T element) {
        mArray[index] = element;
    }

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

}
```

To use the arraylist...

```java
ArrayList<String> arrayList = new ArrayList<>(2);

arrayList.add("Java");
arrayList.add("Kotlin");
System.out.println(arrayList);

arrayList.set(1, "Python");
arrayList.removeAt(0);
System.out.println(arrayList);
```

Which will print...

```java
[Java, Kotlin]
[Python]
```

## :link: Linkedlist
Linkedlist is a linear data structure that contain a set of "nodes" connected together via links, and each node contains data and links to the next and the previous node.

```java
public class Node<T> {

    public T mData;
    public Node<T> mPrev;
    public Node<T> mNext;

    public Node(T data, Node<T> prev, Node<T> next) {
        mData = data;
        mPrev = prev;
        mNext = next;
    }

}

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

    // O(n)
    public boolean contains(T element) {
        return indexOf(element) != -1;
    }
    
}
```

To use the linkedlist...

```java
LinkedList<String> linkedList = new LinkedList<>();

linkedList.addLast("Java");
linkedList.addLast("Kotlin");
linkedList.addFirst("Python");
System.out.println(linkedList);

linkedList.removeLast();
linkedList.remove("Java");
System.out.println(linkedList);
```

Which will print...

```java
[Python, Java, Kotlin]
[Python]
```
