package linkedlist;

public class Node<T> {

    public T mData;
    public Node<T> mPrev;
    public Node<T> mNext;

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