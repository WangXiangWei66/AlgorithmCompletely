package class_2022_08_1;

import java.util.ArrayList;

//设计一个数据结构，有效地找到给定子数组的 多数元素 。
//子数组的 多数元素 是在子数组中出现threshold次数或次数以上的元素。
//实现 MajorityChecker 类:
//MajorityChecker(int[] arr)
//会用给定的数组 arr对MajorityChecker 初始化。
//int query(int left, int right, int threshold)
//返回子数组中的元素 arr[left...right]至少出现threshold次数，
//如果不存在这样的元素则返回 -1。
//测试链接 : https://leetcode.cn/problems/online-majority-element-in-subarray/
public class Code03_OnlineMajorityElementInSubarray {

    class MajorityChecker {
        SegmentTree st;

        CountQuicker cq;

        public MajorityChecker(int[] arr) {
            st = new SegmentTree(arr);
            cq = new CountQuicker(arr);
        }

        public int query(int left, int right, int threshold) {
            //用线段树来筛选出候选的元素
            int candidate = st.query(left, right);
            return cq.realTimes(left, right, candidate) >= threshold ? candidate : -1;
        }

        //用于筛选候选元素
        class SegmentTree {
            int n;//原数组长度
            int[] candidate;//每个线段树节点存储的候选元素
            int[] hp;//每个候选元素的生命值

            public SegmentTree(int[] arr) {
                n = arr.length;
                candidate = new int[(n + 1) << 2];
                hp = new int[(n + 1) >> 2];
                build(arr, 1, n, 1);
            }

            private void build(int[] arr, int l, int r, int rt) {
                if (l == r) {
                    //候选元素是自身
                    candidate[rt] = arr[l - 1];
                    hp[rt] = 1;
                } else {
                    int m = (l + r) >> 1;
                    build(arr, l, m, rt << 1);
                    build(arr, m + 1, r, rt << 1 | 1);
                    int lc = candidate[rt << 1];
                    int rc = candidate[rt << 1 | 1];
                    int lh = hp[rt << 1];
                    int rh = hp[rt << 1 | 1];
                    if (lc == rc) {
                        candidate[rt] = lc;
                        hp[rt] = lh + rh;
                    } else {
                        //保留生命值大的
                        candidate[rt] = lh >= rh ? lc : rc;
                        hp[rt] = Math.abs(lh - rh);
                    }
                }
            }

            public int query(int left, int right) {
                return query(left + 1, right + 1, 1, n, 1)[0];
            }

            private int[] query(int L, int R, int l, int r, int rt) {
                if (L <= l && r <= R) {
                    return new int[]{candidate[rt], hp[rt]};
                }
                int m = (l + r) >> 1;
                //查询区间仅在左子树
                if (R <= m) {
                    return query(L, R, l, m, rt << 1);
                    //查询区间仅在右子树
                } else if (L > m) {
                    return query(L, R, m + 1, r, rt << 1 | 1);
                } else {
                    int[] ans1 = query(L, R, l, m, rt << 1);
                    int[] ansr = query(L, R, m + 1, r, rt << 1 | 1);
                    if (ans1[0] == ansr[0]) {
                        ans1[1] += ansr[1];
                        return ans1;
                    } else {
                        if (ans1[1] >= ansr[1]) {
                            ans1[1] -= ansr[1];
                            return ans1;
                        } else {
                            ansr[1] -= ans1[1];
                            return ansr;
                        }
                    }
                }
            }
        }

        //快速计数:元素索引列表+二分查找
        class CountQuicker {
            ArrayList<ArrayList<Integer>> cnt;//存储每个元素的所有出现索引

            public CountQuicker(int[] arr) {
                cnt = new ArrayList<>();
                //找到数组的最大元素来确定列表的大小
                int max = 0;
                for (int num : arr) {
                    max = Math.max(max, num);
                }
                //为每个元素初始化一个索引列表
                for (int i = 0; i <= max; i++) {
                    cnt.add(new ArrayList<>());
                }
                for (int i = 0; i < arr.length; i++) {
                    cnt.get(arr[i]).add(i);
                }
            }

            public int realTimes(int left, int right, int num) {
                ArrayList<Integer> indies = cnt.get(num);//获取num的所有索引
                // 子数组中出现次数 = 右边界内的索引数 - 左边界左侧的索引数
                return size(indies, right) - size(indies, left - 1);
            }
            // 计算索引列表中≤index的元素个数（即元素在[0, index]范围内出现的次数）
            private int size(ArrayList<Integer> indies, int index) {
                int l = 0;
                int r = indies.size() - 1;
                int m = 0;
                int ans = -1;
                while (l <= r) {
                    m = (l + r) / 2;
                    if (indies.get(m) <= index) {
                        ans = m;
                        l = m + 1;
                    } else {
                        r = m - 1;
                    }
                }
                return ans + 1;
            }
        }
    }
}
