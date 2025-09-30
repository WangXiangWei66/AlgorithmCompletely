package class_2022_03_3;

import java.util.Arrays;

// 来自美团
// void add(int L, int R, int C)代表在arr[L...R]上每个数加C
// int get(int L, int R)代表查询arr[L...R]上的累加和
// 假设你可以在所有操作开始之前，重新排列arr
// 请返回每一次get查询的结果都加在一起最大能是多少
// 输入参数:
// int[] arr : 原始数组
// int[][] ops，二维数组每一行解释如下:
// [a,b,c]，如果数组有3个数，表示调用add(a,b,c)
// [a,b]，如果数组有2个数，表示调用get(a,b)
// a和b表示arr范围，范围假设从1开始，不从0开始
// 输出：
// 假设你可以在开始时重新排列arr，返回所有get操作返回值累计和最大是多少？
public class Code04_ArrangeAddGetMax {

    public static class SegmentTree {
        private int n;//数组的区间长度
        private int[] arr;//存储原始数组数据
        private int[] sum;//线段树节点和
        private int[] lazy;

        public SegmentTree(int size) {
            n = size + 1;
            sum = new int[n << 2];//线段树数组的大小通常为4n
            lazy = new int[n << 2];
            n--;
        }

        public SegmentTree(int[] origin) {
            n = origin.length + 1;
            arr = new int[n];
            //转换数组的下标
            for (int i = 1; i < n; i++) {
                arr[i] = origin[i - 1];
            }
            sum = new int[n << 2];
            lazy = new int[n << 2];
            build(1, --n, 1);
        }

        private void build(int l, int r, int rt) {
            if (l == r) {
                sum[rt] = arr[l];
                return;
            }
            int mid = (l + r) >> 1;
            build(l, mid, rt << 1);
            build(mid + 1, r, rt << 1 | 1);
            pushUp(rt);
        }

        private void pushUp(int rt) {
            sum[rt] = sum[rt << 1] + sum[rt << 1 | 1];
        }

        private void pushDown(int rt, int ln, int rn) {
            if (lazy[rt] != 0) {
                lazy[rt << 1] += lazy[rt];
                sum[rt << 1] += lazy[rt] * ln;
                lazy[rt << 1 | 1] += lazy[rt];
                sum[rt << 1 | 1] += lazy[rt] * rn;
                lazy[rt] = 0;
            }
        }

        public void add(int L, int R, int C) {
            add(L, R, C, 1, n, 1);
        }

        private void add(int L, int R, int C, int l, int r, int rt) {
            if (L <= l && r <= R) {
                sum[rt] += C * (r - l + 1);
                lazy[rt] += C;
                return;
            }
            int mid = (l + r) >> 1;
            pushDown(rt, mid - l + 1, r - mid);//先下发未处理的区间
            if (L <= mid) {
                add(L, R, C, l, mid, rt << 1);
            }
            if (R > mid) {
                add(L, R, C, mid + 1, r, rt << 1 | 1);
            }
            pushUp(rt);
        }

        public int get(int L, int R) {
            return query(L, R, 1, n, 1);
        }

        private int query(int L, int R, int l, int r, int rt) {
            if (L <= l && r <= R) {
                return sum[rt];
            }
            int mid = (l + r) >> 1;
            pushDown(rt, mid - l + 1, r - mid);
            int ans = 0;
            if (L <= mid) {
                ans += query(L, R, l, mid, rt << 1);
            }
            if (R > mid) {
                ans += query(L, R, mid + 1, r, rt << 1 | 1);
            }
            return ans;
        }
    }
    //两个序列的乘积和，在两个序列同序（都升序或都降序）时最大，逆序时最小。
    public static int maxGets(int[] arr, int[][] ops) {
        int n = arr.length;
        SegmentTree getTree = new SegmentTree(n);//记录每个位置元素被get操作覆盖的次数
        for (int[] op : ops) {
            if (op.length == 2) {
                getTree.add(op[0], op[1], 1);
            }
        }
        //[原始索引,被查询次数]:
        int[][] getCnts = new int[n][2];
        for (int i = 1, j = 0; i <= n; i++, j++) {
            getCnts[j][0] = j;
            getCnts[j][1] = getTree.get(i, i);//获取i位置被查询的次数
        }
        //排序为了使小元素少查询，大元素多查询
        Arrays.sort(getCnts, (a, b) -> a[1] - b[1]);
        Arrays.sort(arr);
        int[] reArrange = new int[n];//重新排列后的数组
        for (int i = 0; i < n; i++) {
            reArrange[getCnts[i][0]] = arr[i];
        }
        SegmentTree st = new SegmentTree(reArrange);
        int ans = 0;
        for (int[] op : ops) {
            if (op.length == 3) {
                st.add(op[0], op[1], op[2]);
            } else {
                ans += st.get(op[0], op[1]);
            }
        }
        return ans;
    }
}
