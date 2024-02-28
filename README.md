# ds-algorithm-note

Data Structure & Algorithm in Java

---

## :pushpin: Content
- :books: [Stack](#books-stack)
- :people_holding_hands: [Queue](#peopleholdinghands-queue)
- :1234: [Arraylist](#1234-arraylist)

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
