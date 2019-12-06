package BalancedTree.BST;

import java.util.*;

public class Test {
    static final int ROUND = 200;  // 测试轮数
    static final int UPPER_BOUND = 200;  // 往容器中添加的元素的最大值
    static final int MAX_SIZE = 500;  // 当容器尺寸超过阈值时，结束测试
    static final int QUERY_TIME = 10;  // 查询排名的次数

    List<Integer> list;
    BST bst;
    Random random = new Random();

    void list_insert(int val) {
        ListIterator<Integer> iter = list.listIterator();
        while (iter.hasNext()) {
            int _val = iter.next();
            if (val <= _val) {
                iter.previous();
                break;
            }
        }
        iter.add(val);
    }

    int list_rank(int val) {
        ListIterator<Integer> iter = list.listIterator();
        int rank = 1;
        while (iter.hasNext()) {
            int _val = iter.next();
            if (_val >= val) {
                break;
            }
            ++rank;
        }
        return rank;
    }

    void test_insert() {
        int val = random.nextInt(UPPER_BOUND);
        list_insert(val);
        bst.insert(val);
        test_size();
    }

    void test_size() {
        if (bst.size() != list.size()) {
            throw new Error("两个容器的size不一致！");
        }
    }

    void test_remove() {
        Integer val = random.nextInt(UPPER_BOUND);
        if (list.remove(val) != bst.remove(val)) {
            throw new Error("remove()的实现有问题！");
        }
        test_size();
    }

    void test_rank() {
        for (int i = 0; i < QUERY_TIME; ++i) {
            int val = random.nextInt(UPPER_BOUND);
            if (list_rank(val) != bst.rank(val)) {
                throw new Error("rank()的实现有问题！");
            }
        }
    }

    void test_kth() {
        for (int i = 0; i < QUERY_TIME; ++i) {
            int size = list.size();
            int rank = random.nextInt(size * 3 + 1) - size;
            int val;
            if (rank >= 1 && rank <= list.size()) {
                val = list.get(rank - 1);
            } else {
                val = -1;
            }
            if (bst.kth(rank) != val) {
                throw new Error("kth()的实现有问题！");
            }
        }
    }

    void test_contains() {
        for (int i = 0; i < QUERY_TIME; ++i) {
            int val = random.nextInt(UPPER_BOUND);
            if (bst.contains(val) != list.contains(val)) {
                throw new Error("contains()的实现有问题！");
            }
        }
    }

    void test_lower() {
        for (int i = 0; i < QUERY_TIME; ++i) {
            int val = random.nextInt(UPPER_BOUND);
            Iterator<Integer> iter = ((LinkedList<Integer>) list).descendingIterator();
            int lower = -1;
            while (iter.hasNext()) {
                int _val = iter.next();
                if (_val < val) {
                    lower = _val;
                    break;
                }
            }
            if (bst.lower(val) != lower) {
                throw new Error("lower()的实现有问题！");
            }
        }
    }

    void test_upper() {
        for (int i = 0; i < QUERY_TIME; ++i) {
            int val = random.nextInt(UPPER_BOUND);
            int upper = -1;
            for (int num : list) {
                if (num > val) {
                    upper = num;
                    break;
                }
            }
            if (bst.upper(val) != upper) {
                throw new Error("upper()的实现有问题！");
            }
        }
    }

    void test() {
        list = new LinkedList<>();
        bst = new BST();
        Random random = new Random();
        for (int round = 0; round < ROUND; ++round) {
            list.clear();
            bst.clear();
            if (list.size() != bst.size()) {
                throw new Error("clear()的实现有问题！");
            }
            while (list.size() <= MAX_SIZE) {
                int op = random.nextInt(8) + 1;
                if (op == 1) {
                    test_insert();
                } else if (op == 2) {
                    test_remove();
                } else if (op == 3) {
                    test_rank();
                } else if (op == 4) {
                    test_kth();
                } else if (op == 5) {
                    test_upper();
                } else if (op == 6) {
                    test_lower();
                } else if (op == 7) {
                    test_size();
                } else if (op == 8) {
                    test_contains();
                } else {
                    throw new Error("op设置有误！");
                }
            }
            test_size();
        }
        System.out.println("测试完毕，没有发现错误！");
    }

    public static void main(String[] args) {
        new Test().test();
    }
}
