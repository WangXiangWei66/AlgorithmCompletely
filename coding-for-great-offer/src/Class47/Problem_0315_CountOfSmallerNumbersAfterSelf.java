package Class47;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//给你一个整数数组 nums ，按要求返回一个新数组counts 。数组 counts 有该性质： counts[i] 的值是 nums[i] 右侧小于nums[i] 的元素的数量。
//输入：nums = [5,2,6,1]
//输出：[2,1,1,0]
//解释：
//5 的右侧有 2 个更小的元素 (2 和 1)
//2 的右侧仅有 1 个更小的元素 (1)
//6 的右侧有 1 个更小的元素 (1)
//1 的右侧有 0 个更小的元素
//leetcode题目：https://leetcode.com/problems/count-of-smaller-numbers-after-self/
public class Problem_0315_CountOfSmallerNumbersAfterSelf {

    public static class Node {
        public int sum;
        public Node left;
        public Node right;
    }

    public static class DynamicSegmentTree {
        public Node root;
        public int size;

        public DynamicSegmentTree(int max) {
            root = new Node();
            size = max;
        }

        public void add(int i, int v) {
            add(root, 1, size, i, v);
        }

        private void add(Node c, int l, int r, int i, int v) {
            if (l == r) {
                c.sum += v;
            } else {
                int mid = (l + r) / 2;
                if (i <= mid) {
                    if (c.left == null) {
                        c.left = new Node();
                    }
                    add(c.left, l, mid, i, v);
                } else {
                    if (c.right == null) {
                        c.right = new Node();
                    }
                    add(c.right, mid + 1, r, i, v);
                }
                c.sum = (c.left != null ? c.left.sum : 0) + (c.right != null ? c.right.sum : 0);
            }
        }

        public int query(int s, int e) {
            return query(root, 1, size, s, e);
        }

        private int query(Node c, int l, int r, int s, int e) {
            if (c == null) {
                return 0;
            }
            if (s <= l && r <= e) {
                return c.sum;
            }
            int mid = (l + r) / 2;
            if (e <= mid) {
                return query(c.left, l, mid, s, e);
            } else if (s > mid) {
                return query(c.right, mid + 1, r, s, e);
            } else {
                return query(c.left, l, mid, s, e) + query(c.right, mid + 1, r, s, e);
            }
        }

        public static List<Integer> countSmaller(int[] nums) {
            List<Integer> ans = new ArrayList<>();
            if (nums == null || nums.length == 0) {
                return ans;
            }
            int n = nums.length;
            //结果列表初始化
            for (int i = 0; i < n; i++) {
                ans.add(0);
            }
            //存储元素值和元素索引
            int[][] arr = new int[n][];
            for (int i = 0; i < n; i++) {
                arr[i] = new int[]{nums[i], i};
            }
            Arrays.sort(arr, (a, b) -> (a[0] - b[0]));
            DynamicSegmentTree dst = new DynamicSegmentTree(n);
            for (int[] num : arr) {
                ans.set(num[1], dst.query(num[1] + 1, n));
                dst.add(num[1] + 1, 1);
            }
            return ans;
        }
    }
}
