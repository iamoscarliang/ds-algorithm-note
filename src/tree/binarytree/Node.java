package tree.binarytree;

import tree.TreePrinter;

public class Node<T> implements TreePrinter.PrintableNode {

    public T mData;
    public Node<T> mLeft;
    public Node<T> mRight;

    public Node(T data) {
        mData = data;
    }

    @Override
    public TreePrinter.PrintableNode getLeft() {
        return mLeft;
    }

    @Override
    public TreePrinter.PrintableNode getRight() {
        return mRight;
    }

    @Override
    public String getText() {
        return mData.toString();
    }

}