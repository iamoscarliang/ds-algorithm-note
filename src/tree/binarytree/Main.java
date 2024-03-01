package tree.binarytree;

import java.util.Iterator;

public class Main {

    public static void main(String[] args) {
        BinaryTree<Integer> binaryTree = new BinaryTree<>();
        binaryTree.add(5);
        binaryTree.add(2);
        binaryTree.add(1);
        binaryTree.add(10);
        binaryTree.add(8);
        binaryTree.add(7);
        binaryTree.add(4);
        binaryTree.add(9);
        System.out.println(binaryTree);

        Iterator<Integer> iterator = binaryTree.traverse(BinaryTree.TreeTraversalOrder.IN_ORDER);
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }

}