# ds-algorithm-note

Data Structure & Algorithm in Java

---

## :pushpin: Content
- :books: [Stack](#stack)

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
