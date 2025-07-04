package Class45;

public class Code04_LongestRepeatingSubString {

    public static int longestRepeatingSubString(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        char[] str = s.toCharArray();
        int n = str.length;
        int min = str[0];
        int max = str[0];
        for (int i = 0; i < n; i++) {
            min = Math.min(min, str[i]);
            max = Math.max(max, str[i]);
        }
        int[] all = new int[n];
        for (int i = 0; i < n; i++) {
            all[i] = str[i] - min + 1;
        }
        DC3 dc3 = new DC3(all, max - min + 1);
        int ans = 0;
        for (int i = 1; i < n; i++) {
            ans = Math.max(ans, dc3.height[i]);
        }
        return ans;
    }

    public static class DC3 {
        public int[] sa;//名次对应的前缀
        public int[] rank;//前缀对应的名次
        public int[] height;

        public DC3(int[] nums, int max) {
            sa = sa(nums, max);
            rank = rank();
            height = height(nums);
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

        private int[] height(int[] s) {
            int n = s.length;
            int[] ans = new int[n];
            for (int i = 0, k = 0; i < n; ++i) {
                if (rank[i] != 0) {
                    if (k > 0) {
                        --k;
                    }
                    int j = sa[rank[i] - 1];
                    while (i + k < n && j + k < n && s[i + k] == s[j + k]) {
                        ++k;
                    }
                    ans[rank[i]] = k;
                }
            }
            return ans;
        }
    }

    public static String randomNumberString(int len, int range) {
        char[] str = new char[len];
        for (int i = 0; i < len; i++) {
            str[i] = (char) ((int) (Math.random() * range) + 'a');
        }
        return String.valueOf(str);
    }

    public static void main(String[] args) {
        int len = 500000;
        int range = 3;
        long start = System.currentTimeMillis();
        longestRepeatingSubString(randomNumberString(len, range));
        long end = System.currentTimeMillis();
        System.out.println("字符长度为：" + len + "字符种类数为:" + range + "时");
        System.out.println("运行时间:" + (end - start) + "ms");
    }
}
