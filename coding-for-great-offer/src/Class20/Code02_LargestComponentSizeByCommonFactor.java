package Class20;

import java.util.HashMap;

//给定一个由不同正整数的组成的非空数组 A，考虑下面的图：有A.length个节点，按从A[0]到A[A.length - 1]标记；
//只有当 A[i] 和 A[j] 共用一个大于 1 的公因数时，A[i]和 A[j] 之间才有一条边。返回图中最大连通集合的大小
//Leetcode题目：https://leetcode.com/problems/largest-component-size-by-common-factor/
public class Code02_LargestComponentSizeByCommonFactor {
    //寻找最大联通分量大小
    public static int largestComponentSize1(int[] arr) {
        int N = arr.length;
        // 创建一个大小为N的并查集，每个元素初始属于自己的集合
        UnionFind set = new UnionFind(N);
        //遍历所有的元素对
        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                // 如果两数的最大公约数不为1（即存在公共因子）
                if (gcd(arr[i], arr[j]) != 1) {
                    //将两个元素所在的集合进行合并
                    set.union(i, j);
                }
            }
        }
        //最后返回并查集中最大联通分量的大小
        return set.maxSize();
    }

    // 计算两个数的最大公约数（欧几里得算法）——辗转相除
    //定理：对于任意整数 a 和 b（b ≠ 0），有 gcd(a, b) = gcd(b, a % b)
    // //终止条件：当 b 为 0 时，gcd(a, 0) = a。。
    public static int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    //并查集用于解决连通性问题，如社交网络中的好友分组，图的联通分量计算等
    public static class UnionFind {
        private int[] parents;//存储每个元素的父节点
        private int[] sizes;//存储每个集合的大小（仅根节点有效）
        private int[] help;//辅助数组，用于路径压缩

        public UnionFind(int N) {
            parents = new int[N];
            sizes = new int[N];
            help = new int[N];

            for (int i = 0; i < N; i++) {
                parents[i] = i;// 初始时每个元素的父节点是自己
                sizes[i] = 1; // 每个集合的初始大小为1
            }
        }

        //返回最大集合的大小
        public int maxSize() {
            int ans = 0;
            for (int size : sizes) {
                ans = Math.max(ans, size);
            }
            return ans;
        }

        //查询x所属的集合
        private int find(int i) {
            int hi = 0;
            while (i != parents[i]) {
                help[hi++] = i;
                i = parents[i];//向上来查找父节点
            }
            //将路径上的所有节点的父节点，都设为父节点
            for (hi--; hi >= 0; hi--) {
                parents[help[hi]] = i;
            }
            return i;//将根节点返回
        }

        //将两个集合合并
        public void union(int i, int j) {
            int f1 = find(i);
            int f2 = find(j);
            if (f1 != f2) {
                int big = sizes[f1] >= sizes[f2] ? f1 : f2;
                int small = big == f1 ? f2 : f1;
                parents[small] = big;
                sizes[big] = sizes[f1] + sizes[f2];
            }
        }
    }
    //相比暴力方法，该方法通过分解质因数的方式提高效率
    public static int largestComponentSize2(int[] arr) {
        int N = arr.length;
        // arr中，N个位置，在并查集初始时，每个位置自己是一个集合
        UnionFind unionFind = new UnionFind(N);
        //使用哈希表来记录每个质因数首次出现的索引，后续遇到相同质因数的元素时，将其与首次出现的元素合并
        HashMap<Integer, Integer> fatorsMap = new HashMap<>();
        //遍历数组中的每个元素
        for (int i = 0; i < N; i++) {
            int num = arr[i];
            // 计算平方根，用于分解因数
            int limit = (int) Math.sqrt(num);
            //下面开始分解因数
            for (int j = 1; j <= limit; j++) {
                if (num % j == 0) {//这里表明j是num的一个因数
                    // 处理因数j（排除j=1，因为1不是质因数）
                    if (j != 1) {
                        if (!fatorsMap.containsKey(j)) {
                            //这一步记录该因数首次出现的位置
                            fatorsMap.put(j, i);
                        } else {
                            // 若已有元素包含该因数，合并当前元素与首个包含该因数的元素
                            unionFind.union(fatorsMap.get(j), i);
                        }
                    }
                    //下面开始处理另外的因数
                    int other = num / j;
                    if (other != 1) {
                        if (!fatorsMap.containsKey(other)) {
                            fatorsMap.put(other, i);
                        } else {
                            unionFind.union(fatorsMap.get(other), i);
                        }
                    }
                }
            }
        }
        //最后返回最大连通分量的大小
        return unionFind.maxSize();
    }
}
