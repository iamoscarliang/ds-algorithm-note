package tree.avltree;

import tree.TreePrinter;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Stack;

public class AVLTree<T extends Comparable<T>> implements Iterable<T> {

    private Node mRoot;
    private int mNodeCount = 0;

    private class Node implements TreePrinter.PrintableNode {

        private T mData;
        private Node mLeft;
        private Node mRight;
        private int mHeight;
        private int mBalanceFactor;

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

    // The height of a rooted tree is the number of edges between the tree's
    // root and its furthest leaf. A tree with a single node has a height of 0
    public int height() {
        if (mRoot == null) {
            return 0;
        }
        return mRoot.mHeight;
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

        // Update balance factor and height
        update(node);

        // Re-balance tree.
        return balance(node);
    }

    // Remove the element from the tree, O(log(n))
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
                // Check is left or right subtree has larger height to remove
                if (node.mLeft.mHeight > node.mRight.mHeight) {
                    // Swap the value with the successor
                    T successor = findMax(node.mLeft);
                    node.mData = successor;

                    // Go into the left subtree and remove the rightmost node to
                    // prevent having two nodes with the same value in the tree
                    node.mLeft = remove(node.mLeft, successor);
                } else {
                    // Swap the value with the successor
                    T successor = findMin(node.mRight);
                    node.mData = successor;

                    // Go into the right subtree and remove the leftmost node to
                    // prevent having two nodes with the same value in the tree
                    node.mRight = remove(node.mRight, successor);
                }
            }
        }

        // Update balance factor and height
        update(node);

        // Re-balance tree.
        return balance(node);
    }

    // Returns is the element exists in the tree
    public boolean contains(T element) {
        return contains(mRoot, element);
    }

    // Recursively find an element in the tree
    private boolean contains(Node node, T element) {
        if (node == null)  {
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

    // Update a node's height and balance factor.
    private void update(Node node) {
        int leftNodeHeight = (node.mLeft == null) ? -1 : node.mLeft.mHeight;
        int rightNodeHeight = (node.mRight == null) ? -1 : node.mRight.mHeight;

        // Update this node's height.
        node.mHeight = 1 + Math.max(leftNodeHeight, rightNodeHeight);

        // Update balance factor.
        node.mBalanceFactor = rightNodeHeight - leftNodeHeight;
    }

    // Re-balance a node if its balance factor is +2 or -2.
    private Node balance(Node node) {
        // Check left or right is heavier
        if (node.mBalanceFactor == -2) {
            // Left heavy subtree
            if (node.mLeft.mBalanceFactor <= 0) {
                // Left-Left case
                return leftLeftCase(node);
            } else {
                // Left-Right case.
                return leftRightCase(node);
            }
        } else if (node.mBalanceFactor == 2) {
            // Right heavy subtree
            if (node.mRight.mBalanceFactor >= 0) {
                // Right-Right case
                return rightRightCase(node);
            } else {
                // Right-Left case
                return rightLeftCase(node);
            }
        }

        // Node either has a balance factor of 0, +1 or -1 which is fine.
        return node;
    }

    private Node leftLeftCase(Node node) {
        return rightRotation(node);
    }

    private Node leftRightCase(Node node) {
        node.mLeft = leftRotation(node.mLeft);
        return leftLeftCase(node);
    }

    private Node rightRightCase(Node node) {
        return leftRotation(node);
    }

    private Node rightLeftCase(Node node) {
        node.mRight = rightRotation(node.mRight);
        return rightRightCase(node);
    }

    private Node leftRotation(Node node) {
        Node newParent = node.mRight;
        node.mRight = newParent.mLeft;
        newParent.mLeft = node;
        update(node);
        update(newParent);
        return newParent;
    }

    private Node rightRotation(Node node) {
        Node newParent = node.mLeft;
        node.mLeft = newParent.mRight;
        newParent.mRight = node;
        update(node);
        update(newParent);
        return newParent;
    }

    // Find the leftmost node (which has the smallest value)
    private T findMin(Node node) {
        while (node.mLeft != null) {
            node = node.mLeft;
        }
        return node.mData;
    }

    // Find the rightmost node (which has the largest value)
    private T findMax(Node node) {
        while (node.mRight != null) {
            node = node.mRight;
        }
        return node.mData;
    }

    // Returns as iterator to traverse the tree in order.
    @Override
    public Iterator<T> iterator() {

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

                if (expectedNodeCount != mNodeCount) {
                    throw new ConcurrentModificationException();
                }

                while (trav != null && trav.mLeft != null) {
                    stack.push(trav.mLeft);
                    trav = trav.mLeft;
                }

                Node node = stack.pop();

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

    @Override
    public String toString() {
        return TreePrinter.getTreeDisplay(mRoot);
    }

    // Make sure all left child nodes are smaller in value than their parent and
    // make sure all right child nodes are greater in value than their parent.
    // (Used only for testing)
    public boolean validateBSTInvariant(Node node) {
        if (node == null) {
            return true;
        }
        T val = node.mData;
        boolean isValid = true;
        if (node.mLeft != null) {
            isValid = isValid && node.mLeft.mData.compareTo(val) < 0;
        }
        if (node.mRight != null) {
            isValid = isValid && node.mRight.mData.compareTo(val) > 0;
        }
        return isValid && validateBSTInvariant(node.mLeft) && validateBSTInvariant(node.mRight);
    }

}
