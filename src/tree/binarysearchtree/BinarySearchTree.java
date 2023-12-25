package tree.binarysearchtree;

import tree.TreePrinter;

import java.util.*;

public class BinarySearchTree<T extends Comparable<T>> {

    private Node mRoot;
    private int mNodeCount = 0;

    private class Node implements TreePrinter.PrintableNode {

        private T mData;
        private Node mLeft;
        private Node mRight;

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

    // Return the number of nodes in the tree
    public int size() {
        return mNodeCount;
    }

    // Returns if the tree contains no node
    public boolean isEmpty() {
        return size() == 0;
    }

    // Add an element to the tree, O(log(n))
    public boolean add(T element) {
        // Check if the value already exists in the tree
        if (contains(element)) {
            return false;
        }

        // Otherwise, Add the element to the tree
        mRoot = add(mRoot, element);
        mNodeCount++;
        return true;
    }

    // Recursively add an element in the tree
    private Node add(Node node, T element) {
        if (node == null) {
            // Found a leaf node
            return new Node(element);
        }
        // Pick a subtree to insert element
        if (element.compareTo(node.mData) < 0) {
            // Insert node in left subtree
            node.mLeft = add(node.mLeft, element);
        } else {
            // Insert node in right subtree
            node.mRight = add(node.mRight, element);
        }

        return node;
    }

    // Remove an element from the binary tree, O(log(n))
    public boolean remove(T element) {
        // Check is node exists before we remove it
        if (!contains(element)) {
            return false;
        }

        mRoot = remove(mRoot, element);
        mNodeCount--;
        return true;
    }

    // Recursively remove an element in the tree
    private Node remove(Node node, T element) {
        if (node == null) {
            return null;
        }

        int compare = element.compareTo(node.mData);
        if (compare < 0) {
            // Recursively search left subtree
            node.mLeft = remove(node.mLeft, element);
        } else if (compare > 0) {
            // Recursively search right subtree
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
                Node minNode = findMin(node.mRight);

                // Swap the data
                node.mData = minNode.mData;

                // Go into the right subtree and remove the leftmost node to
                // prevent having two nodes with the same value in the tree
                node.mRight = remove(node.mRight, minNode.mData);
            }
        }

        return node;
    }

    // Returns is the element exists in the tree, O(log(n))
    public boolean contains(T element) {
        return contains(mRoot, element);
    }

    // Recursively find an element in the tree
    private boolean contains(Node node, T element) {
        if (node == null) {
            return false;
        }

        int compare = element.compareTo(node.mData);
        if (compare < 0) {
            // Recursively search left subtree
            return contains(node.mLeft, element);
        } else if (compare > 0) {
            // Recursively search right subtree
            return contains(node.mRight, element);
        } else {
            // We find the contain element
            return true;
        }
    }

    // Computes the height of the tree, O(n)
    public int height() {
        return height(mRoot);
    }

    // Recursively find the height of the tree
    private int height(Node node) {
        if (node == null) {
            return 0;
        }
        return Math.max(height(node.mLeft), height(node.mRight)) + 1;
    }

    // Find the leftmost node (which has the smallest value)
    private Node findMin(Node node) {
        while (node.mLeft != null) {
            node = node.mLeft;
        }
        return node;
    }

    // Find the rightmost node (which has the largest value)
    private Node findMax(Node node) {
        while (node.mRight != null) {
            node = node.mRight;
        }
        return node;
    }

    // Returns an iterator for a given TreeTraversalOrder.
    public Iterator<T> traverse(TreeTraversalOrder order) {
        switch (order) {
            case PRE_ORDER:
                return preOrderTraversal();
            case IN_ORDER:
                return inOrderTraversal();
            case POST_ORDER:
                return postOrderTraversal();
            case LEVEL_ORDER:
                return levelOrderTraversal();
            default:
                return null;
        }
    }

    // Returns an iterator to traverse the tree in pre-order
    private Iterator<T> preOrderTraversal() {

        final int expectedNodeCount = mNodeCount;
        final Stack<Node> stack = new Stack<>();
        stack.push(mRoot);

        return new Iterator<T>() {

            @Override
            public boolean hasNext() {
                if (expectedNodeCount != mNodeCount) {
                    throw new ConcurrentModificationException();
                }
                return mRoot != null && !stack.isEmpty();
            }

            @Override
            public T next() {
                if (expectedNodeCount != mNodeCount) {
                    throw new ConcurrentModificationException();
                }
                Node node = stack.pop();
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

    // Returns an iterator to traverse the tree in in-order
    private Iterator<T> inOrderTraversal() {

        final int expectedNodeCount = mNodeCount;
        final Stack<Node> stack = new Stack<>();
        stack.push(mRoot);

        return new Iterator<T>() {

            Node trav = mRoot;

            @Override
            public boolean hasNext() {
                if (expectedNodeCount != mNodeCount) {
                    throw new ConcurrentModificationException();
                }
                return mRoot != null && !stack.isEmpty();
            }

            @Override
            public T next() {

                if (expectedNodeCount != mNodeCount){
                    throw new ConcurrentModificationException();
                }

                // Dig left
                while (trav != null && trav.mLeft != null) {
                    stack.push(trav.mLeft);
                    trav = trav.mLeft;
                }

                Node node = stack.pop();

                // Try moving down right once
                if (node.mRight != null) {
                    stack.push(node.mRight);
                    trav = node.mRight;
                }

                return node.mData;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

        };

    }

    // Returns an iterator to traverse the tree in post-order
    private Iterator<T> postOrderTraversal() {
        final int expectedNodeCount = mNodeCount;
        final Stack<Node> stack1 = new java.util.Stack<>();
        final Stack<Node> stack2 = new java.util.Stack<>();
        stack1.push(mRoot);
        while (!stack1.isEmpty()) {
            Node node = stack1.pop();
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
                if (expectedNodeCount != mNodeCount) {
                    throw new ConcurrentModificationException();
                }
                return mRoot != null && !stack2.isEmpty();
            }

            @Override
            public T next() {
                if (expectedNodeCount != mNodeCount) {
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

    // Returns an iterator to traverse the tree in level-order
    private Iterator<T> levelOrderTraversal() {

        final int expectedNodeCount = mNodeCount;
        final Queue<Node> queue = new LinkedList<>();
        queue.offer(mRoot);

        return new java.util.Iterator<T>() {

            @Override
            public boolean hasNext() {
                if (expectedNodeCount != mNodeCount) throw new ConcurrentModificationException();
                return mRoot != null && !queue.isEmpty();
            }

            @Override
            public T next() {
                if (expectedNodeCount != mNodeCount) throw new ConcurrentModificationException();
                Node node = queue.poll();
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

    @Override
    public String toString() {
        return TreePrinter.getTreeDisplay(mRoot);
    }

    public enum TreeTraversalOrder {
        PRE_ORDER,
        IN_ORDER,
        POST_ORDER,
        LEVEL_ORDER
    }

}
