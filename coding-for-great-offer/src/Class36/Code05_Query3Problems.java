package Class36;

//来自美团
//给定一个数组arr，长度为N，做出一个结构，可以高效的做如下的查询
//1) int querySum(L,R) : 查询arr[L...R]上的累加和
//2) int queryAim(L,R) : 查询arr[L...R]上的目标值，目标值定义如下：
//   假设arr[L...R]上的值为[a,b,c,d]，a+b+c+d = s
//   目标值为 : (s-a)^2 + (s-b)^2 + (s-c)^2 + (s-d)^2
//3) int queryMax(L,R) : 查询arr[L...R]上的最大值
//要求：
//4) 初始化该结构的时间复杂度不能超过O(N*logN)
//5) 三个查询的时间复杂度不能超过O(logN)
//6) 查询时，认为arr的下标从1开始，比如 :
//   arr = [ 1, 1, 2, 3 ];
//   querySum(1, 3) -> 4
//   queryAim(2, 4) -> 50
//   queryMax(1, 4) -> 3
public class Code05_Query3Problems {
    //前缀和数组处理区间和目标值
    //线段树处理区间最大值
    //负责区间最大值的维护和查询
    public static class SegmentTree {
        private int[] max;//存储每个线段树节点的区间最大值
        private int[] change;//记录需要批量复制的目标值
        private boolean[] update;//标记当前节点是否有未下发的批量更新

        public SegmentTree(int N) {
            max = new int[N << 2];
            change = new int[N << 2];
            update = new boolean[N << 2];
            for (int i = 0; i < max.length; i++) {
                max[i] = Integer.MIN_VALUE;
            }
        }

        private void pushUp(int rt) {
            max[rt] = Math.max(max[rt << 1], max[rt << 1 | 1]);
        }
        //rt:当前节点编号
        //ln:左子树区间长度
        //rn:右子树区间长度
        private void pushDown(int rt, int ln, int rn) {
            if (update[rt]) {
                update[rt << 1] = true;
                update[rt << 1 | 1] = true;
                //传递更新的值
                change[rt << 1] = change[rt];
                change[rt << 1 | 1] = change[rt];
                //更新左右子节点的最大值，批量赋值为change[rt]
                max[rt << 1] = change[rt];
                max[rt << 1 | 1] = change[rt];
                update[rt] = false;
            }
        }

        public void update(int L, int R, int C, int l, int r, int rt) {
            if (L <= l && r <= R) {
                update[rt] = true;
                change[rt] = C;//记录更新值
                max[rt] = C;
                return;
            }
            //当前节点区间与目标区间部分重叠
            int mid = (l + r) >> 1;
            pushDown(rt, mid - l + 1, r - mid);
            if (L <= mid) {
                update(L, R, C, l, mid, rt << 1);
            }
            if (R > mid) {
                update(L, R, C, mid + 1, r, rt << 1 | 1);
            }
            pushUp(rt);//子节点更新后，向上同步父节点的最大值
        }

        public int query(int L, int R, int l, int r, int rt) {
            if (L <= l && r <= R) {
                return max[rt];
            }
            int mid = (l + r) >> 1;
            pushDown(rt, mid - l + 1, r - mid);//先下发懒更新
            int left = 0;//记录更新结果
            int right = 0;
            if (L <= mid) {
                left = query(L, R, l, mid, rt << 1);
            }
            if (R > mid) {
                right = query(L, R, mid + 1, r, rt << 1 | 1);
            }
            return Math.max(left, right);
        }
    }

    //整合前缀和数组与线段树，对外提供三类查询接口
    public static class Query {
        public int[] sum1;//前缀和数组
        public int[] sum2;//前缀和平方数组
        public SegmentTree st;
        public int m;//前缀数组长度（= arr.length + 1，适配1-based下标）

        public Query(int[] arr) {
            int n = arr.length;
            m = arr.length + 1;
            sum1 = new int[m];
            sum2 = new int[m];
            st = new SegmentTree(m);
            for (int i = 0; i < n; i++) {
                sum1[i + 1] = sum1[i] + arr[i];
                sum2[i + 1] = sum2[i] + arr[i] * arr[i];
                //线段树中，将arr[i]初始化为叶子节点
                st.update(i + 1, i + 1, arr[i], 1, m, 1);
            }
        }

        public int querySum(int L, int R) {
            return sum1[R] - sum1[L - 1];
        }
        //(s-a)² + (s-b)² + ... + (s-k)²
        //= k*s² - 2s*(a+b+...+k) + (a² + b² + ... + k²)
        // 因a+b+...+k = s，代入后：
        //= k*s² - 2s*s + sum2(L,R)
        //= (k-2)*s² + sum2(L,R)
        //其中k = R-L+1（区间元素个数），sum2(L,R) = 区间平方和
        public int queryAim(int L, int R) {
            int sumPower2 = querySum(L, R);
            sumPower2 *= sumPower2;
            return sum2[R] - sum2[L - 1] + (R - L - 1) * sumPower2;
        }

        public int queryMax(int L, int R) {
            return st.query(L, R, 1, m, 1);
        }
    }

    public static void main(String[] args) {
        int[] arr = {1, 1, 2, 3};
        Query q = new Query(arr);
        System.out.println(q.querySum(1, 3));
        System.out.println(q.queryAim(2, 4));
        System.out.println(q.queryMax(1, 4));
    }
}
