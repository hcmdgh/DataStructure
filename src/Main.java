import BalancedTree.BST.BST;

import java.util.LinkedList;
import java.util.ListIterator;

public class Main {
    public static void main(String[] args) {
        BST bst = new BST();
        bst.insert(50);
        bst.insert(30);
        bst.insert(70);
        bst.insert(10);
        bst.insert(40);
        bst.insert(0);
        bst.insert(35);
        bst.insert(32);
        bst.insert(36);
        bst.insert(33);
        System.out.println(bst.size());
        bst.remove(30);
        System.out.println(bst.size());
    }
}
