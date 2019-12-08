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

    private Node root;  // root是空结点，root的右孩子才是真正的树根！

    public Splay() {
        clear();
    }

    // 建立父子关系
    private static void connect(Node son, Node father, boolean isLeftChild) {
        if (son != null) {
            son.father = father;
        }
        if (isLeftChild) {
            father.leftChild = son;
        } else {
            father.rightChild = son;
        }
    }

    // 旋转结点，将结点左旋或右旋
    private static void rotate(Node node) {
        // 结合Excel中的图示
        // node：结点X
        Node father = node.father;  // 结点Y
        Node fatherFather = father.father;  // 结点Z
        boolean fatherPos = father.isLeftChild();
        if (node.isLeftChild()) {  // 如果X是Y的左孩子
            // Y的左孩子=B
            connect(node.rightChild, father, true);
            // X的右孩子=Y
            connect(father, node, false);
        } else {  // 如果X是Y的右孩子
            // Y的右孩子=A
            connect(node.leftChild, father, false);
            // X的左孩子=Y
            connect(father, node, true);
        }
        // 将Z的孩子由Y改为X
        connect(node, fatherFather, fatherPos);
        // 更新结点X和Y的信息
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
                        // 插入新结点后，将新结点旋转到树根
                        splay(cur.leftChild, root.rightChild);
                        break;
                    } else {
                        cur = cur.leftChild;
                    }
                } else if (val > cur.val) {
                    if (cur.rightChild == null) {
                        cur.rightChild = new Node(val, cur);
                        // 插入新结点后，将新结点旋转到树根
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

    // 查找val对应的结点，并将结点旋转到树根，找不到则返回null
    private Node find(int val) {
        Node cur = root.rightChild;
        while (cur != null) {
            if (val < cur.val) {
                cur = cur.leftChild;
            } else if (val > cur.val) {
                cur = cur.rightChild;
            } else {
                splay(cur, root.rightChild);
                return cur;
            }
        }
        return null;
    }

    // 删除值为val的结点
    // 如果存在多个相同的数，只删除一个，返回true
    // 如果不存在，返回false
    public boolean remove(int val) {
        Node node = find(val);
        if (node == null) {
            return false;
        } else {
            if (node.repeat > 1) {
                --node.repeat;
                --node.cnt;
            } else if (node.leftChild == null) {
                // 如果待删除结点没有左孩子，直接将右孩子作为树根
                connect(node.rightChild, root, false);
            } else {
                // 如果待删除结点有左孩子，执行以下步骤：
                // 1. 找到左孩子所在子树中最大的结点x
                // 2. 将x上旋到左孩子的位置（此时x的右孩子显然为空）
                // 3. 将待删除结点的右孩子作为x的右孩子
                // 4. 将x作为树根
                Node cur = node.leftChild;
                while (cur.rightChild != null) {
                    cur = cur.rightChild;
                }
                splay(cur, node.leftChild);
                connect(node.rightChild, cur, false);
                connect(cur, root, false);
                cur.update();
            }
            root.update();
            return true;
        }
    }

    // 清空容器
    public void clear() {
        root = new Node(0, null);
        root.cnt = root.repeat = 0;
    }

    // 获取结点总数
    public int size() {
        return root.cnt;
    }

    // 获取val的排名
    // val的排名定义为比val小的数的个数+1
    public int rank(int val) {
        int _rank = 1;
        Node cur = root.rightChild;
        while (cur != null) {
            if (val < cur.val) {
                cur = cur.leftChild;
            } else if (val > cur.val) {
                _rank += (cur.leftChild != null ? cur.leftChild.cnt : 0) + cur.repeat;
                cur = cur.rightChild;
            } else {
                _rank += cur.leftChild != null ? cur.leftChild.cnt : 0;
                // 找到结点后，将其上旋到树根
                splay(cur, root.rightChild);
                return _rank;
            }
        }
        return _rank;
    }

    // 获取排名为k的元素
    // 返回值为-1表示元素不存在
    public int kth(int rk) {
        Node cur = root.rightChild;
        while (cur != null) {
            int rankLowerBound = cur.leftChild != null ? cur.leftChild.cnt : 0;
            int rankUpperBound = rankLowerBound + cur.repeat;
            if (rk <= rankLowerBound) {
                cur = cur.leftChild;
            } else if (rk > rankUpperBound) {
                rk -= rankUpperBound;
                cur = cur.rightChild;
            } else {
                splay(cur, root.rightChild);
                return cur.val;
            }
        }
        return -1;
    }

    // 检测容器是否包含某个值
    public boolean contains(int val) {
        return find(val) != null;
    }

    // 返回val的前驱
    // val的前驱定义为小于val且最大的数
    public int lower(int val) {
        return kth(rank(val) - 1);
    }

    // 返回val的后继
    // val的后继定义为大于val且最小的数
    public int upper(int val) {
        return kth(rank(val + 1));
    }
}
