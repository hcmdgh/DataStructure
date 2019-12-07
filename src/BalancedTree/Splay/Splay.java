package BalancedTree.Splay;

import BalancedTree.Tree;

public class Splay implements Tree {
    static class Node {
        int val;
        Node father, leftChild, rightChild;
        int cnt;  // 该结点所在子树中所有结点的数量
        int repeat;  // 该结点的值重复了几次

        Node(int val, Node father) {
            this.val = val;
            this.father = father;
            leftChild = rightChild = null;
            cnt = repeat = 1;
        }

        // 更新结点的信息，当该结点的子树发生变化后使用
        void update() {
            cnt = repeat;
            cnt += leftChild != null ? leftChild.cnt : 0;
            cnt += rightChild != null ? rightChild.cnt : 0;
        }

        // 确定当前结点是父结点的左孩子还是右孩子
        // 返回true表示左孩子，返回false表示右孩子
        boolean isLeftChild() {
            if (father == null) {
                return false;
            } else {
                return this == father.leftChild;
            }
        }
    }

    private Node root;  // root的右孩子才是真正的树根！

    public Splay() {
        clear();
    }

    // 建立父子关系
//    private static void connect(Node son, Node father, boolean isLeftChild) {
//        son.father = father;
//        if (isLeftChild) {
//            father.leftChild = son;
//        } else {
//            father.rightChild = son;
//        }
//    }

    // 旋转结点，将结点左旋或右旋
    private static void rotate(Node node) {
        Node father = node.father;
        Node fatherFather = father.father;
        boolean fatherPos = father.isLeftChild();
        if (node.isLeftChild()) {
            father.leftChild = node.rightChild;
            if (father.leftChild != null)
                father.leftChild.father = father;
            node.rightChild = father;
            node.rightChild.father = node;
            node.father = fatherFather;
        } else {
            father.rightChild = node.leftChild;
            if (father.rightChild != null)
                father.rightChild.father = father;
            node.leftChild = father;
            node.leftChild.father = node;
            node.father = fatherFather;
        }
        if (fatherPos) {
            fatherFather.leftChild = node;
        } else {
            fatherFather.rightChild = node;
        }
        father.update();
        node.update();
    }

    // 上旋结点，将结点src上旋到dest
    private static void splay(Node src, Node dest) {
        Node destFather = dest.father;
        while (src.father != destFather) {
            Node father = src.father;
            if (father.father == destFather) {
                rotate(src);
            } else if (src.isLeftChild() == father.isLeftChild()) {
                rotate(father);
                rotate(src);
            } else {
                rotate(src);
                rotate(src);
            }
        }
    }

    // 插入结点
    public void insert(int val) {
        if (root.rightChild == null) {
            root.rightChild = new Node(val, root);
        } else {
            Node cur = root.rightChild;
            while (true) {
                ++cur.cnt;
                if (val < cur.val) {
                    if (cur.leftChild == null) {
                        cur.leftChild = new Node(val, cur);
                        splay(cur.leftChild, root.rightChild);
                        break;
                    } else {
                        cur = cur.leftChild;
                    }
                } else if (val > cur.val) {
                    if (cur.rightChild == null) {
                        cur.rightChild = new Node(val, cur);
                        splay(cur.rightChild, root.rightChild);
                        break;
                    } else {
                        cur = cur.rightChild;
                    }
                } else {
                    ++cur.repeat;
                    break;
                }
            }
        }
        root.update();
    }

    public boolean remove(int val) {
        return false;
    }

    public void clear() {
        root = new Node(0, null);
        root.cnt = root.repeat = 0;
    }

    // 获取结点总数
    public int size() {
        return root.cnt;
    }

    public int rank(int val) {
        return 0;
    }

    public int kth(int rk) {
        return 0;
    }

    public boolean contains(int val) {
        return false;
    }

    public int lower(int val) {
        return 0;
    }

    public int upper(int val) {
        return 0;
    }
}
