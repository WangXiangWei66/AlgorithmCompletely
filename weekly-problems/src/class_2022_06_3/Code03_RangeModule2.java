package class_2022_06_3;

//Range模块是跟踪数字范围的模块。
//设计一个数据结构来跟踪表示为 半开区间 的范围并查询它们。
//半开区间[left, right)表示所有left <= x < right的实数 x 。
//实现 RangeModule 类:
//RangeModule()初始化数据结构的对象
//void addRange(int left, int right) :
//添加 半开区间[left, right)，跟踪该区间中的每个实数。
//添加与当前跟踪的数字部分重叠的区间时，
//应当添加在区间[left, right)中尚未跟踪的任何数字到该区间中。
//boolean queryRange(int left, int right) :
//只有在当前正在跟踪区间[left, right)中的每一个实数时，才返回 true
//否则返回 false 。
//void removeRange(int left, int right) :
//停止跟踪 半开区间[left, right)中当前正在跟踪的每个实数。
//测试连接 : https://leetcode.com/problems/range-module/
public class Code03_RangeModule2 {

    class RangeModule {
        class Node {
            public int sum;//区间内部被跟踪的数字总数（1/0）
            public int change;
            public boolean update;
            public Node left;
            public Node right;
        }

        class DynamicSegmentTree {
            public Node root;
            public int size;//动态线段树覆盖的最大范围

            public DynamicSegmentTree(int max) {
                root = new Node();
                size = max;
            }

            private void pushUp(Node c) {
                c.sum = c.left.sum + c.right.sum;
            }

            private void pushDown(Node p, int ln, int rn) {
                if (p.left == null) {
                    p.left = new Node();
                }
                if (p.right == null) {
                    p.right = new Node();
                }
                if (p.update) {
                    p.left.update = true;
                    p.right.update = true;
                    p.left.change = p.change;
                    p.right.change = p.change;
                    p.left.sum = p.change * ln;
                    p.right.sum = p.change * rn;
                    p.update = false;
                }
            }

            public void update(int s, int e, int v) {
                update(root, 1, size, s, e, v);
            }

            private void update(Node c, int l, int r, int s, int e, int v) {
                //当前节点区间完全包含在被更新区间内部
                if (s <= l && r <= e) {
                    c.update = true;
                    c.change = v;
                    c.sum = v * (r - l + 1);
                } else {
                    int mid = (l + r) >> 1;
                    pushDown(c, mid - l + 1, r - mid);
                    if (s <= mid) {
                        update(c.left, l, mid, s, e, v);
                    }
                    if (e > mid) {
                        update(c.right, mid + 1, r, s, e, v);
                    }
                    pushUp(c);
                }
            }

            public int query(int s, int e) {
                return query(root, 1, size, s, e);
            }

            private int query(Node c, int l, int r, int s, int e) {
                if (s <= l && r <= e) {
                    return c.sum;
                }
                int mid = (l + r) >> 1;
                pushDown(c, mid - l + 1, r - mid);
                int ans = 0;
                if (s <= mid) {
                    ans += query(c.left, l, mid, s, e);
                }
                if (e > mid) {
                    ans += query(c.right, mid + 1, r, s, e);
                }
                return ans;
            }
        }

        DynamicSegmentTree dst;

        public RangeModule() {
            dst = new DynamicSegmentTree(1000000001);
        }

        public void addRange(int left, int right) {
            dst.update(left, right - 1, 1);
        }

        public boolean queryRange(int left, int right) {
            return dst.query(left, right - 1) == right - left;
        }

        public void removeRange(int left, int right) {
            dst.update(left, right - 1, 0);
        }
    }
}
