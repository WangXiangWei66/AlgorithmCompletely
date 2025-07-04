package Class45;


public class Code02_CreatMaximumNumber {

    public static int[] maxNumber1(int[] nums1, int[] nums2, int k) {
        int len1 = nums1.length;
        int len2 = nums2.length;
        if (k < 0 || k > len1 + len2) {
            return null;
        }
        int[] res = new int[k];
        int[][] dp1 = getDp(nums1);
        int[][] dp2 = getDp(nums2);
        for (int get1 = Math.max(0, k - len2); get1 <= Math.min(k, len1); get1++) {
            int[] pick1 = maxPick(nums1, dp1, get1);
            int[] pick2 = maxPick(nums2, dp2, k - get1);
            int[] merge = merge(pick1, pick2);
            res = preMoreThanLast(res, 0, merge, 0) ? res : merge;
        }
        return res;
    }

    public static int[] merge(int[] nums1, int[] nums2) {
        int k = nums1.length + nums2.length;
        int[] ans = new int[k];
        for (int i = 0, j = 0, r = 0; r < k; ++r) {
            ans[r] = preMoreThanLast(nums1, i, nums2, j) ? nums1[i++] : nums2[j++];
        }
        return ans;
    }

    public static boolean preMoreThanLast(int[] nums1, int i, int[] nums2, int j) {
        while (i < nums1.length && j < nums2.length && nums1[i] == nums2[j]) {
            i++;
            j++;
        }
        return j == nums2.length || (i < nums1.length && nums1[i] > nums2[j]);
    }

    public static int[] maxNumber2(int[] nums1, int[] nums2, int k) {
        int len1 = nums1.length;
        int len2 = nums2.length;
        if (k < 0 || k > len1 + len2) {
            return null;
        }
        int[] res = new int[k];
        int[][] dp1 = getDp(nums1);
        int[][] dp2 = getDp(nums2);
        for (int get1 = Math.max(0, k - len2); get1 <= Math.min(k, len1); get1++) {
            int[] pick1 = maxPick(nums1, dp1, get1);
            int[] pick2 = maxPick(nums2, dp2, k - get1);
            int[] merge = mergeBySuffixArray(pick1, pick2);
            res = moreThan(res, merge) ? res : merge;
        }
        return res;
    }

    public static boolean moreThan(int[] pre, int[] last) {
        int i = 0;
        int j = 0;
        while (i < pre.length && j < last.length && pre[i] == last[j]) {
            i++;
            j++;
        }
        return j == last.length || (i < pre.length && pre[i] > last[j]);
    }

    public static int[] mergeBySuffixArray(int[] nums1, int[] nums2) {
        int size1 = nums1.length;
        int size2 = nums2.length;
        int[] nums = new int[size1 + 1 + size2];
        for (int i = 0; i < size1; i++) {
            nums[i] = nums1[i] + 2;
        }
        nums[size1] = 1;
        for (int j = 0; j < size2; j++) {
            nums[j + size1 + 1] = nums2[j] + 2;
        }
        DC3 dc3 = new DC3(nums, 11);
        int[] rank = dc3.rank;
        int[] ans = new int[size1 + size2];
        int i = 0;
        int j = 0;
        int r = 0;
        while (i < size1 && j < size2) {
            ans[r++] = rank[i] > rank[j + size1 + 1] ? nums1[i++] : nums2[j++];
        }
        while (i < size1) {
            ans[r++] = nums1[i++];
        }
        while (j < size2) {
            ans[r++] = nums2[j++];
        }
        return ans;
    }

    public static class DC3 {
        public int[] sa;//名次对应的前缀
        public int[] rank;//前缀对应的名次

        public DC3(int[] nums, int max) {
            sa = sa(nums, max);
            rank = rank();
        }

        private int[] sa(int[] nums, int max) {
            int n = nums.length;
            int[] arr = new int[n + 3];
            for (int i = 0; i < n; i++) {
                arr[i] = nums[i];
            }
            return skew(arr, n, max);
        }

        private int[] skew(int[] nums, int n, int K) {
            int n0 = (n + 2) / 3, n1 = (n + 1) / 3, n2 = n / 3, n02 = n0 + n2; // 计算模 3 余 0、1、2 的后缀数量以及模 3 不为 0 的后缀总数
            int[] s12 = new int[n02 + 3]; // 存储位置后缀模三部位 0 的后缀索引
            int[] sa12 = new int[n02 + 3]; // 存储排序后的索引
            for (int i = 0, j = 0; i < n + (n0 - n1); ++i) { // 遍历数组，找出模 3 不为 0 的后缀的起始位置
                if (0 != i % 3) { // 判断当前位置是否模 3 不为 0
                    s12[j++] = i; // 将模 3 不为 0 的后缀的起始位置存入 s12 数组
                }
            }
            radixPass(nums, s12, sa12, 2, n02, K); // 对模 3 不为 0 的后缀进行第一次基数排序，偏移量为 2
            radixPass(nums, sa12, s12, 1, n02, K); // 对模 3 不为 0 的后缀进行第二次基数排序，偏移量为 1
            radixPass(nums, s12, sa12, 0, n02, K); // 对模 3 不为 0 的后缀进行第三次基数排序，偏移量为 0

            int name = 0, c0 = -1, c1 = -1, c2 = -1; // 初始化重命名所需的变量
            for (int i = 0; i < n02; ++i) { // 遍历排序后的模 3 不为 0 的后缀
                if (c0 != nums[sa12[i]] || c1 != nums[sa12[i] + 1] || c2 != nums[sa12[i] + 2]) { // 判断当前后缀与前一个后缀是否不同
                    name++; // 若不同，则重命名，名字加 1
                    c0 = nums[sa12[i]]; // 更新当前后缀的第一个字符
                    c1 = nums[sa12[i] + 1]; // 更新当前后缀的第二个字符
                    c2 = nums[sa12[i] + 2]; // 更新当前后缀的第三个字符
                }
                if (1 == sa12[i] % 3) { // 判断当前后缀的起始位置是否模 3 余 1
                    s12[sa12[i] / 3] = name; // 将重命名后的名字存入 s12 数组
                } else {
                    s12[sa12[i] / 3 + n0] = name; // 将重命名后的名字存入 s12 数组
                }
            }
            if (name < n02) { // 判断重命名后的名字数量是否小于模 3 不为 0 的后缀总数
                sa12 = skew(s12, n02, name); // 若小于，则递归调用 skew 方法
                for (int i = 0; i < n02; i++) { // 遍历递归排序后的结果
                    s12[sa12[i]] = i + 1; // 更新 s12 数组
                }
            } else {
                for (int i = 0; i < n02; i++) { // 若不小于，则直接构建 sa12 数组
                    sa12[s12[i] - 1] = i; // 更新 sa12 数组
                }
            }
            int[] s0 = new int[n0], sa0 = new int[n0]; // 创建存储模 3 余 0 的后缀的数组

            for (int i = 0, j = 0; i < n02; i++) { // 遍历排序后的模 3 不为 0 的后缀
                if (sa12[i] < n0) { // 判断当前后缀的起始位置是否小于 n0
                    s0[j++] = 3 * sa12[i]; // 将模 3 余 0 的后缀的起始位置存入 s0 数组
                }
            }
            radixPass(nums, s0, sa0, 0, n0, K); // 对模 3 余 0 的后缀进行基数排序，偏移量为 0
            int[] sa = new int[n]; // 创建最终的后缀数组
            for (int p = 0, t = n0 - n1, k = 0; k < n; k++) { // 合并模 3 余 0 和模 3 不为 0 的后缀
                int i = sa12[t] < n0 ? sa12[t] * 3 + 1 : (sa12[t] - n0) * 3 + 2; // 计算模 3 不为 0 的后缀的起始位置
                int j = sa0[p]; // 获取模 3 余 0 的后缀的起始位置
                if (sa12[t] < n0 ? leg(nums[i], s12[sa12[t] + n0], nums[j], s12[j / 3]) : leg(nums[i], nums[i + 1], s12[sa12[t] - n0 + 1], nums[j], nums[j + 1], s12[j / 3 + n0])) { // 比较两个后缀的大小
                    sa[k] = i; // 若模 3 不为 0 的后缀较小，则将其起始位置存入最终的后缀数组
                    t++; // 移动模 3 不为 0 的后缀的指针
                    if (t == n02) { // 判断模 3 不为 0 的后缀是否全部处理完
                        for (k++; p < n0; p++, k++) { // 若处理完，则将剩余的模 3 余 0 的后缀存入最终的后缀数组
                            sa[k] = sa0[p];
                        }
                    }
                } else {
                    sa[k] = j; // 若模 3 余 0 的后缀较小，则将其起始位置存入最终的后缀数组
                    p++; // 移动模 3 余 0 的后缀的指针
                    if (p == n0) { // 判断模 3 余 0 的后缀是否全部处理完
                        for (k++; t < n02; t++, k++) { // 若处理完，则将剩余的模 3 不为 0 的后缀存入最终的后缀数组
                            sa[k] = sa12[t] < n0 ? sa12[t] * 3 + 1 : (sa12[t] - n0) * 3 + 2;
                        }
                    }
                }
            }
            return sa; // 返回最终的后缀数组
        }

        private void radixPass(int[] nums, int[] input, int[] output, int offset, int n, int k) {
            int[] cnt = new int[k + 1]; // 创建一个长度为 k + 1 的计数数组，用于统计每个字符的出现次数
            for (int i = 0; i < n; ++i) { // 遍历输入数组
                cnt[nums[input[i] + offset]]++; // 统计每个字符的出现次数
            }
            for (int i = 0, sum = 0; i < cnt.length; ++i) { // 对计数数组进行前缀和计算
                int t = cnt[i]; // 保存当前字符的出现次数
                cnt[i] = sum; // 更新当前字符的起始位置
                sum += t; // 更新前缀和
            }
            for (int i = 0; i < n; ++i) { // 遍历输入数组
                output[cnt[nums[input[i] + offset]]++] = input[i]; // 根据计数数组将输入数组中的元素放入输出数组中
            }
        }

        private boolean leg(int a1, int a2, int b1, int b2) {
            return a1 < b1 || (a1 == b1 && a2 <= b2);
        }

        private boolean leg(int a1, int a2, int a3, int b1, int b2, int b3) {
            return a1 < b1 || (a1 == b1 && leg(a2, a3, b2, b3));
        }

        private int[] rank() {
            int n = sa.length;
            int[] ans = new int[n];
            for (int i = 0; i < n; i++) {
                ans[sa[i]] = i;
            }
            return ans;
        }
    }

    public static int[][] getDp(int[] arr) {
        int size = arr.length;
        int pick = arr.length + 1;
        int[][] dp = new int[size][pick];
        for (int get = 1; get < pick; get++) {
            int maxIndex = size - get;
            for (int i = size - get; i >= 0; i--) {
                if (arr[i] >= arr[maxIndex]) {
                    maxIndex = i;
                }
                dp[i][get] = maxIndex;
            }
        }
        return dp;
    }

    public static int[] maxPick(int[] arr, int[][] dp, int pick) {
        int[] res = new int[pick];
        for (int resIndex = 0, dpRow = 0; pick > 0; pick--, resIndex++) {
            res[resIndex] = arr[dp[dpRow][pick]];
            dpRow = dp[dpRow][pick] + 1;
        }
        return res;
    }
}
