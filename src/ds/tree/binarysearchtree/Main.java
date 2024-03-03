package ds.tree.binarysearchtree;

import java.util.Iterator;

public class Main {

    public static void main(String[] args) {
        BinarySearchTree<Integer> binarySearchTree = new BinarySearchTree<>();
        binarySearchTree.add(5);
        binarySearchTree.add(2);
        binarySearchTree.add(1);
        binarySearchTree.add(10);
        binarySearchTree.add(8);
        binarySearchTree.add(7);
        binarySearchTree.add(4);
        binarySearchTree.add(9);
        System.out.println(binarySearchTree);

        Iterator<Integer> iterator = binarySearchTree.traverse(BinarySearchTree.TreeTraversalOrder.IN_ORDER);
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }

}