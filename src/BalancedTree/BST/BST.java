package BalancedTree.BST;

import BalancedTree.Tree;

import java.util.Stack;

public class BST implements Tree {
    static class Node {
        int val;  // 结点中存储的值
        Node leftChild, rightChild;  // 左孩子和右孩子
        int cnt;  // 该结点所在子树中所有结点的数量

        Node(int val) {
            this.val = val;
            leftChild = rightChild = null;
            cnt = 1;
        }

        // 更新结点的信息，当该结点的子树发生变化后使用
        void update() {
            cnt = 1;
            cnt += leftChild != null ? leftChild.cnt : 0;
            cnt += rightChild != null ? rightChild.cnt : 0;
        }
    }

    private Node root = null;

    // 计算结点node在以node为树根的子树中的排名
    private int nodeRank(Node node) {
        if (node.leftChild != null) {
            return node.leftChild.cnt + 1;
        } else {
            return 1;
        }
    }

    // 插入结点
    public void insert(int val) {
        if (root == null) {
            root = new Node(val);
        } else {
            Node cur = root;
            while (true) {
                ++cur.cnt;
                if (val < cur.val) {
                    if (cur.leftChild == null) {
                        cur.leftChild = new Node(val);
                        break;
                    } else {
                        cur = cur.leftChild;
                    }
                } else {
                    if (cur.rightChild == null) {
                        cur.rightChild = new Node(val);
                        break;
                    } else {
                        cur = cur.rightChild;
                    }
                }
            }
        }
    }

    // 删除值为val的结点
    // 如果存在多个相同的数，只删除一个，返回true
    // 如果不存在，返回false
    public boolean remove(int val) {
        if (root == null)
            return false;
        Node now = root;
        Node father = null;
        boolean isFatherLeft = true;  // 记录now结点是father的左孩子还是右孩子
        Stack<Node> toUpdate = new Stack<>();  // 记录在删除操作后需要更新的结点
        while (now != null) {
            toUpdate.push(now);
            if (val < now.val) {
                father = now;
                isFatherLeft = true;
                now = now.leftChild;
            } else if (val > now.val) {
                father = now;
                isFatherLeft = false;
                now = now.rightChild;
            } else {
                toUpdate.pop();
                if (now.leftChild == null && now.rightChild == null) {
                    // 情况一：待删除结点没有左右孩子，直接删除即可
                    if (father == null) {
                        root = null;
                    } else if (isFatherLeft) {
                        father.leftChild = null;
                    } else {
                        father.rightChild = null;
                    }
                } else if (now.leftChild != null && now.rightChild == null) {
                    // 情况二：待删除结点只有左孩子，则将该左孩子连接到父结点上即可
                    if (father == null) {
                        root = now.leftChild;
                    } else if (isFatherLeft) {
                        father.leftChild = now.leftChild;
                    } else {
                        father.rightChild = now.leftChild;
                    }
                } else if (now.leftChild == null && now.rightChild != null) {
                    // 情况三：待删除结点只有右孩子，则将该右孩子连接到父结点上即可
                    if (father == null) {
                        root = now.rightChild;
                    } else if (isFatherLeft) {
                        father.leftChild = now.rightChild;
                    } else {
                        father.rightChild = now.rightChild;
                    }
                } else {
                    // 情况四：待删除结点有左右孩子，则：
                    // 1. 找到该结点的右结点x
                    // 2. 一直向左，找到x最左边的左结点y
                    // 3. 将待删除结点的值改为y的值
                    // 4. 按情况三的思路删除y
                    toUpdate.push(now);
                    Node cur = now.rightChild;
                    Node _father = null;
                    while (cur.leftChild != null) {
                        toUpdate.push(cur);
                        _father = cur;
                        cur = cur.leftChild;
                    }
                    now.val = cur.val;
                    if (_father != null) {
                        _father.leftChild = cur.rightChild;
                    } else {
                        now.rightChild = cur.rightChild;
                    }
                }
                // 删除操作完毕后，更新祖先结点
                while (!toUpdate.empty()) {
                    toUpdate.pop().update();
                }
                return true;
            }
        }
        return false;
    }

    // 清空容器
    public void clear() {
        root = null;
    }

    // 获取结点总数
    public int size() {
        if (root == null) {
            return 0;
        } else {
            return root.cnt;
        }
    }

    // 获取val的排名
    // val的排名定义为比val小的数的个数+1
    public int rank(int val) {
        Node cur = root;
        int _rank = 1;
        while (cur != null) {
            if (val <= cur.val) {
                cur = cur.leftChild;
            } else {
                _rank += nodeRank(cur);
                cur = cur.rightChild;
            }
        }
        return _rank;
    }

    // 获取排名为k的元素
    // 返回值为-1表示元素不存在
    public int kth(int rk) {
        Node cur = root;
        while (cur != null) {
            int _rank = nodeRank(cur);
            if (rk < _rank) {
                cur = cur.leftChild;
            } else if (rk > _rank) {
                rk -= _rank;
                cur = cur.rightChild;
            } else {
                return cur.val;
            }
        }
        return -1;
    }

    // 检测容器是否包含某个值
    public boolean contains(int val) {
        Node cur = root;
        while (cur != null) {
            if (val < cur.val) {
                cur = cur.leftChild;
            } else if (val > cur.val) {
                cur = cur.rightChild;
            } else {
                return true;
            }
        }
        return false;
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
