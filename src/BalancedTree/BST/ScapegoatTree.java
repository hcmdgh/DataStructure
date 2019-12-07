package BalancedTree.BST;

import java.util.ArrayList;
import java.util.List;

public class ScapegoatTree implements Tree {
    static final double ALPHA = 0.7;

    static class Node {
        int val;
        Node leftChild, rightChild;
        int validCount;  // 该结点所在子树中有效结点（即没有被删除的结点）的数量
        int totalCount;  // 该结点所在子树中所有结点的数量
        boolean deleted;  // 该结点是否已被删除

        Node(int val) {
            this.val = val;
            leftChild = rightChild = null;
            validCount = totalCount = 1;
            deleted = false;
        }

        // 该子树是否需要压扁重建
        boolean needToRebuild() {
            int leftCnt = leftChild != null ? leftChild.totalCount : 0;
            int rightCnt = rightChild != null ? rightChild.totalCount : 0;
            return leftCnt > ALPHA * totalCount + 5
                    || rightCnt > ALPHA * totalCount + 5;
        }

        // 更新结点的信息，当该结点的子树发生变化后使用
        void update() {
            totalCount = 1;
            validCount = deleted ? 0 : 1;
            if (leftChild != null) {
                totalCount += leftChild.totalCount;
                validCount += leftChild.validCount;
            }
            if (rightChild != null) {
                totalCount += rightChild.totalCount;
                validCount += rightChild.validCount;
            }
        }
    }

    private Node root = null;

    // 中序遍历该结点所在的子树，将所有结点加入到List中
    private static void inorderTraverse(Node node, List<Integer> list) {
        if (node != null) {
            inorderTraverse(node.leftChild, list);
            if (!node.deleted) {
                list.add(node.val);
            }
            inorderTraverse(node.rightChild, list);
        }
    }

    // 递归地按照有序数组建树，返回树根
    private static Node recursiveBuild(List<Integer> list, int begin, int end) {
        if (begin == end) {
            return new Node(list.get(begin));
        } else if (begin > end) {
            return null;
        } else {
            int mid = (begin + end) / 2;
            Node node = new Node(list.get(mid));
            node.leftChild = recursiveBuild(list, begin, mid - 1);
            node.rightChild = recursiveBuild(list, mid + 1, end);
            node.update();
            return node;
        }
    }

    // 将该结点所在的子树压扁重建，返回新的树根
    private static Node rebuild(Node node) {
        List<Integer> list = new ArrayList<>();
        inorderTraverse(node, list);
        return recursiveBuild(list, 0, list.size() - 1);
    }

    private void recursiveInsert(int val, Node node) {
        int _val = node.val;
        if (val < _val) {
            if (node.leftChild == null) {
                node.leftChild = new Node(val);
            } else {
                recursiveInsert(val, node.leftChild);
                if (node.leftChild.needToRebuild()) {
                    node.leftChild = rebuild(node.leftChild);
                }
            }
        } else {
            if (node.rightChild == null) {
                node.rightChild = new Node(val);
            } else {
                recursiveInsert(val, node.rightChild);
                if (node.rightChild.needToRebuild()) {
                    node.rightChild = rebuild(node.rightChild);
                }
            }
        }
        node.update();
    }

    // 插入结点
    public void insert(int val) {
        if (root == null) {
            root = new Node(val);
        } else {
            recursiveInsert(val, root);
        }
    }

    private boolean recursiveRemove(int val, Node node) {
        if (node == null) {
            return false;
        } else {
            boolean res;
            if (val < node.val) {
                res = recursiveRemove(val, node.leftChild);
            } else if (val > node.val) {
                res = recursiveRemove(val, node.rightChild);
            } else {
                if (!node.deleted) {
                    node.deleted = true;
                    res = true;
                } else {
                    if (recursiveRemove(val, node.leftChild)) {
                        res = true;
                    } else {
                        res = recursiveRemove(val, node.rightChild);
                    }
                }
            }
            if (res) {
                node.update();
            }
            return res;
        }
    }

    // 删除值为val的结点
    // 如果存在多个相同的数，只删除一个，返回true
    // 如果不存在，返回false
    public boolean remove(int val) {
        return recursiveRemove(val, root);
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

    // 清空容器
    public void clear() {
        root = null;
    }

    // 获取结点总数
    public int size() {
        if (root == null) {
            return 0;
        } else {
            return root.validCount;
        }
    }

    // 计算结点node在以node为树根的子树中的排名
    private int nodeRank(Node node) {
        return (node.deleted ? 0 : 1) + (node.leftChild != null ? node.leftChild.validCount : 0);
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
            } else if (cur.deleted) {
                cur = cur.leftChild;
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
            } else if (cur.deleted) {
                cur = cur.rightChild;
            } else {
                return true;
            }
        }
        return false;
    }
}
