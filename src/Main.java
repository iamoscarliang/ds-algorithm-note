import tree.balancedtree.AVLTree;
import tree.binarysearchtree.BinarySearchTree;

public class Main {

    public static void main(String[] args) {
        BinarySearchTree<Integer> binarySearchTree = new BinarySearchTree<>();
        binarySearchTree.add(5);
        binarySearchTree.add(1);
        binarySearchTree.add(10);
        binarySearchTree.add(7);
        binarySearchTree.add(9);
        System.out.println(binarySearchTree);

        AVLTree<Integer> avlTree2 = new AVLTree<>();
        avlTree2.add(5);
        avlTree2.add(1);
        avlTree2.add(10);
        avlTree2.add(7);
        avlTree2.add(9);
        System.out.println(avlTree2);
    }

}