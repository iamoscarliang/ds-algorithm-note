# ds-algorithm-note

Data Structure & Algorithm in Java

---

## :pushpin: Content
- :books: [Stack](#books-stack)
- :people_holding_hands: [Queue](#peopleholdinghands-queue)

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
System.out.println(stack.peek());

stack.pop();
System.out.println(stack.peek());
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
