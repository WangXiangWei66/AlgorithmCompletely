package class_2022_09_3;

//来自360
//有n个黑白棋子，它们的一面是黑色，一面是白色
//它们被排成一行，位置0~n-1上。一开始所有的棋子都是黑色向上
//一共有q次操作，每次操作将位置标号在区间[L，R]内的所有棋子翻转
//那么这个范围上的每一颗棋子的颜色也就都改变了
//请在每次操作后，求这n个棋子中，黑色向上的棋子个数
//1 <= n <= 10^18
//1 <= q <= 300
//0 <= 每一条操作的L、R <= n - 1
//输出q行，每一行一个整数，表示操作后的所有黑色棋子的个数
//注意 : 其实q <= 10^5也可以通过，360考试时候降低了难度
public class Code01_BlackWhiteChess {

    public static class Right {
        boolean[] white;

        public Right(long n) {
            white = new boolean[(int) n];
        }

        public void reverse(long l, long r) {
            if (l <= r) {
                for (int i = (int) l; i <= (int) r; i++) {
                    white[i] = !white[i];
                }
            }
        }

        public long blacks() {
            long ans = 0;
            for (boolean s : white) {
                ans += !s ? 1 : 0;
            }
            return ans;
        }
    }

    // 正式结构的实现
    // 动态开点线段树
    // 1 ~ 10^18 -> node
    // l ~ r -> node
    // l ~ r -> sum(黑子的数量)
    // l ~ r -> 当前有没有翻转的动作需要往下传

    public static class Node {
        public long sum;//区间内1的总个数
        public boolean change;//标记该区间是否需要翻转
        public Node left;
        public Node right;

        public Node(long len) {
            //初始化1的个数为区间长度
            sum = len;
            change = false;//初始无翻转标记
        }
    }

    public static class DynamicSegmentTree {
        public Node root;//线段树根节点
        public long size;//线段树覆盖的总长度

        public DynamicSegmentTree(long n) {
            root = new Node(n);
            size = n;
        }

        //查询当前所有1的总个数
        public long blacks() {
            return root.sum;
        }

        public void reverse(long l, long r) {
            if (l <= r) {
                l++;
                r++;
                reverse(root, 1, size, l, r);
            }
        }

        //翻转区间[s,e]
        private void reverse(Node cur, long l, long r, long s, long e) {
            if (s <= l && r <= e) {
                //翻转懒标记
                cur.change = !cur.change;
                //1的个数为:区间长度-原来1的个数
                cur.sum = (r - l + 1) - cur.sum;
            } else {
                //当前节点区间与旋转区间部分重叠
                long m = (l + r) >> 1;
                //向下传递懒标记,并创建子节点
                pushDown(cur, m - l + 1, r - m);
                if (s <= m) {
                    reverse(cur.left, l, m, s, e);
                }
                if (e > m) {
                    reverse(cur.right, m + 1, r, s, e);
                }
                //完成递归后,更新当前节点sum
                pushUp(cur);
            }
        }

        //向下传递懒标记:处理延迟更新+同时创建子节点
        private void pushDown(Node cur, long ln, long rn) {
            if (cur.left == null) {
                cur.left = new Node(ln);
            }
            if (cur.right == null) {
                cur.right = new Node(rn);
            }
            //若当前节点有翻转标记,传递给子节点
            if (cur.change) {
                cur.left.change = !cur.left.change;
                cur.left.sum = ln - cur.left.sum;
                cur.right.change = !cur.right.change;
                cur.right.sum = rn - cur.right.sum;
                //清除当前节点的懒标记
                cur.change = false;
            }
        }

        //向上更新sum
        private void pushUp(Node cur) {
            cur.sum = cur.left.sum + cur.right.sum;
        }
    }

    public static void main(String[] args) {
        int N = 1000;
        int testTimes = 5000;
        int opTimes = 500;
        System.out.println("功能测试开始");
        for (int i = 0; i < testTimes; i++) {
            int n = (int) (Math.random() * N) + 1;
            Right right = new Right(n);
            DynamicSegmentTree dst = new DynamicSegmentTree(n);
            boolean pass = true;
            for (int j = 0; j < opTimes; j++) {
                int a = (int) (Math.random() * n);
                int b = (int) (Math.random() * n);
                int l = Math.min(a, b);
                int r = Math.max(a, b);
                right.reverse(l, r);
                dst.reverse(l, r);
                if (right.blacks() != dst.blacks()) {
                    pass = false;
                    return;
                }
            }
            if (!pass) {
                System.out.println("出错了!");
                break;
            }
        }
        System.out.println("功能测试结束");

        System.out.println("性能测试开始");
        long n = 1000000000000000000L;
        int ops = 100000;
        System.out.println("数组范围 : " + n);
        System.out.println("查询次数 : " + ops);
        DynamicSegmentTree dst = new DynamicSegmentTree(n);
        long start = System.currentTimeMillis();
        for (int j = 0; j < ops; j++) {
            long a = (long) (Math.random() * n);
            long b = (long) (Math.random() * n);
            long l = Math.min(a, b);
            long r = Math.max(a, b);
            dst.reverse(l, r);
        }
        long end = System.currentTimeMillis();
        System.out.println("运行时间 : " + (end - start) + " 毫秒");
        System.out.println("性能测试结束");
    }
}
