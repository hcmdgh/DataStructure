package BalancedTree.BST;

public interface Tree {
    // 插入结点
    void insert(int val);

    // 删除值为val的结点
    // 如果存在多个相同的数，只删除一个，返回true
    // 如果不存在，返回false
    boolean remove(int val);

    // 清空容器
    void clear();

    // 返回结点总数
    int size();

    // 返回val的排名
    // val的排名定义为比val小的数的个数+1
    int rank(int val);

    // 返回排名为k的元素
    // 返回值为-1表示元素不存在
    int kth(int rk);

    // 检测容器是否包含某个值
    boolean contains(int val);

    // 返回val的前驱
    // val的前驱定义为小于val且最大的数
    int lower(int val);

    // 返回val的后继
    // val的后继定义为大于val且最小的数
    int upper(int val);
}
