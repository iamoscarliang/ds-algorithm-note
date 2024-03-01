package tree.binarytree;

import tree.TreePrinter;

import java.util.*;

public class BinaryTree<T extends Comparable<T>> {

    private Node<T> mRoot;
    private int mSize = 0;

    public int size() {
        return mSize;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    // O(log(n))
    public boolean add(T element) {
        if (contains(element)) {
            return false;
        }

        mRoot = add(mRoot, element);
        mSize++;
        return true;
    }

    // O(log(n))
    private Node<T> add(Node<T> node, T element) {
        if (node == null) {
            return new Node<>(element);
        }
        if (element.compareTo(node.mData) < 0) {
            // Insert node in left subtree
            node.mLeft = add(node.mLeft, element);
        } else {
            // Insert node in right subtree
            node.mRight = add(node.mRight, element);
        }

        return node;
    }

    // O(log(n))
    public boolean remove(T element) {
        if (!contains(element)) {
            return false;
        }

        mRoot = remove(mRoot, element);
        mSize--;
        return true;
    }

    // O(log(n))
    private Node<T> remove(Node<T> node, T element) {
        if (node == null) {
            return null;
        }

        int compare = element.compareTo(node.mData);
        if (compare < 0) {
            // Search left subtree
            node.mLeft = remove(node.mLeft, element);
        } else if (compare > 0) {
            // Search right subtree
            node.mRight = remove(node.mRight, element);
        } else {
            if (node.mLeft == null) {
                // This is the case with only a right subtree or no subtree at all
                // We just swap the node we wish to remove with its right child
                return node.mRight;
            } else if (node.mRight == null) {
                // This is the case with only a left subtree or no subtree at all
                // We just swap the node we wish to remove with its left child
                return node.mLeft;
            } else {
                // When removing a node with two children the successor can be the largest
                // value in the left subtree or the smallest value in the right subtree

                // Find the leftmost node in the right subtree
                Node<T> minNode = findMin(node.mRight);

                // Swap the data
                node.mData = minNode.mData;

                // Go into the right subtree and remove the leftmost node to
                // prevent having two nodes with the same value in the tree
                node.mRight = remove(node.mRight, minNode.mData);
            }
        }

        return node;
    }

    // O(log(n))
    public boolean contains(T element) {
        return contains(mRoot, element);
    }

    // O(log(n))
    private boolean contains(Node<T> node, T element) {
        if (node == null) {
            return false;
        }

        int compare = element.compareTo(node.mData);
        if (compare < 0) {
            // Search left subtree
            return contains(node.mLeft, element);
        } else if (compare > 0) {
            // Search right subtree
            return contains(node.mRight, element);
        } else {
            // We find the contain element
            return true;
        }
    }

    // O(log(n))
    private Node<T> findMin(Node<T> node) {
        // Find the leftmost node
        while (node.mLeft != null) {
            node = node.mLeft;
        }
        return node;
    }

    // O(log(n))
    private Node<T> findMax(Node<T> node) {
        // Find the rightmost node
        while (node.mRight != null) {
            node = node.mRight;
        }
        return node;
    }

    // O(n)
    public int height() {
        return height(mRoot);
    }

    // O(n)
    private int height(Node<T> node) {
        if (node == null) {
            return 0;
        }
        return Math.max(height(node.mLeft), height(node.mRight)) + 1;
    }

    @Override
    public String toString() {
        return TreePrinter.getTreeDisplay(mRoot);
    }

    public Iterator<T> traverse(TreeTraversalOrder order) {
        switch (order) {
            case PRE_ORDER:
                return preOrderTraversal();
            case POST_ORDER:
                return postOrderTraversal();
            case IN_ORDER:
                return inOrderTraversal();
            case LEVEL_ORDER:
                return levelOrderTraversal();
            default:
                return null;
        }
    }

    private Iterator<T> preOrderTraversal() {
        final int expectedNodeCount = mSize;
        final Stack<Node<T>> stack = new Stack<>();
        stack.push(mRoot);

        return new Iterator<T>() {

            @Override
            public boolean hasNext() {
                if (expectedNodeCount != mSize) {
                    throw new ConcurrentModificationException();
                }
                return mRoot != null && !stack.isEmpty();
            }

            @Override
            public T next() {
                if (expectedNodeCount != mSize) {
                    throw new ConcurrentModificationException();
                }
                Node<T> node = stack.pop();
                if (node.mRight != null) {
                    stack.push(node.mRight);
                }
                if (node.mLeft != null) {
                    stack.push(node.mLeft);
                }
                return node.mData;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

        };

    }

    private Iterator<T> postOrderTraversal() {
        final int expectedNodeCount = mSize;
        final Stack<Node<T>> stack = new Stack<>();
        stack.push(mRoot);

        return new Iterator<T>() {

            Node<T> current = mRoot;

            @Override
            public boolean hasNext() {
                if (expectedNodeCount != mSize) {
                    throw new ConcurrentModificationException();
                }
                return mRoot != null && !stack.isEmpty();
            }

            @Override
            public T next() {
                if (expectedNodeCount != mSize){
                    throw new ConcurrentModificationException();
                }

                // Go into leftmost node
                while (current != null && current.mLeft != null) {
                    stack.push(current.mLeft);
                    current = current.mLeft;
                }

                Node<T> node = stack.pop();

                // Moving down right once
                if (node.mRight != null) {
                    stack.push(node.mRight);
                    current = node.mRight;
                }

                return node.mData;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

        };

    }

    private Iterator<T> inOrderTraversal() {
        final int expectedNodeCount = mSize;
        final Stack<Node<T>> stack1 = new java.util.Stack<>();
        final Stack<Node<T>> stack2 = new java.util.Stack<>();
        stack1.push(mRoot);
        while (!stack1.isEmpty()) {
            Node<T> node = stack1.pop();
            if (node != null) {
                stack2.push(node);
                if (node.mLeft != null) {
                    stack1.push(node.mLeft);
                }
                if (node.mRight != null) {
                    stack1.push(node.mRight);
                }
            }
        }
        return new Iterator<T>() {
            @Override
            public boolean hasNext() {
                if (expectedNodeCount != mSize) {
                    throw new ConcurrentModificationException();
                }
                return mRoot != null && !stack2.isEmpty();
            }

            @Override
            public T next() {
                if (expectedNodeCount != mSize) {
                    throw new ConcurrentModificationException();
                }
                return stack2.pop().mData;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    private Iterator<T> levelOrderTraversal() {

        final int expectedNodeCount = mSize;
        final Queue<Node<T>> queue = new LinkedList<>();
        queue.offer(mRoot);

        return new java.util.Iterator<T>() {

            @Override
            public boolean hasNext() {
                if (expectedNodeCount != mSize) throw new ConcurrentModificationException();
                return mRoot != null && !queue.isEmpty();
            }

            @Override
            public T next() {
                if (expectedNodeCount != mSize) throw new ConcurrentModificationException();
                Node<T> node = queue.poll();
                if (node.mLeft != null) {
                    queue.offer(node.mLeft);
                }
                if (node.mRight != null) {
                    queue.offer(node.mRight);
                }
                return node.mData;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

        };

    }

    public enum TreeTraversalOrder {
        PRE_ORDER,
        POST_ORDER,
        IN_ORDER,
        LEVEL_ORDER
    }

}