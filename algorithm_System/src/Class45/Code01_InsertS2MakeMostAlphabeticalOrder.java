package Class45;

public class Code01_InsertS2MakeMostAlphabeticalOrder {

    public static String right(String s1, String s2) {
        if (s1 == null || s1.length() == 0) {
            return s2;
        }
        if (s2 == null || s2.length() == 0) {
            return s1;
        }
        String p1 = s1 + s2;
        String p2 = s2 + s1;
        String ans = p1.compareTo(p2) > 0 ? p1 : p2;
        for (int end = 1; end < s1.length(); end++) {
            String cur = s1.substring(0, end) + s2 + s1.substring(end);
            if (cur.compareTo(ans) > 0) {
                ans = cur;
            }
        }
        return ans;
    }

    public static String maxCombine(String s1, String s2) {
        if (s1 == null || s1.length() == 0) {
            return s2;
        }
        if (s2 == null || s2.length() == 0) {
            return s1;
        }
        char[] str1 = s1.toCharArray();
        char[] str2 = s2.toCharArray();
        int N = str1.length;
        int M = str2.length;
        int min = str1[0];
        int max = str1[0];
        for (int i = 1; i < N; i++) {
            min = Math.min(min, str1[i]);
            max = Math.max(max, str1[i]);
        }
        for (int i = 0; i < M; i++) {
            min = Math.min(min, str2[i]);
            max = Math.max(max, str2[i]);
        }
        int[] all = new int[N + M + 1];
        int index = 0;
        for (int i = 0; i < N; i++) {
            all[index++] = str1[i] - min + 2;
        }
        all[index++] = 1;
        for (int i = 0; i < M; i++) {
            all[index++] = str2[i] - min + 2;
        }
        DC3 dc3 = new DC3(all, max - min + 2);
        int[] rank = dc3.rank;
        int comp = N + 1;
        for (int i = 0; i < N; i++) {
            if (rank[i] < rank[comp]) {
                int best = bestSplit(s1, s2, i);
                return s1.substring(0, best) + s2 + s1.substring(best);
            }
        }
        return s1 + s2;
    }


    public static int bestSplit(String s1, String s2, int first) {
        int N = s1.length();
        int M = s2.length();
        int end = N;
        for (int i = first, j = 0; i < N && j < M; i++, j++) {
            if (s1.charAt(i) < s2.charAt(j)) {
                end = i;
                break;
            }
        }
        String bestPrefix = s2;
        int bestSplit = first;
        for (int i = first + 1, j = M - 1; i <= end; i++, j--) {
            String curPrefix = s1.substring(first, i) + s2.substring(0, j);
            if (curPrefix.compareTo(bestPrefix) >= 0) {
                bestPrefix = curPrefix;
                bestSplit = i;
            }
        }
        return bestSplit;
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

    public static String randomNumberString(int len, int range) {
        char[] str = new char[len];
        for (int i = 0; i < len; i++) {
            str[i] = (char) ((int) (Math.random() * range) + '0');
        }
        return String.valueOf(str);
    }

    public static void main(String[] args) {
        int range = 10;
        int len = 50;
        int testTime = 100000;
        System.out.println("功能测试开始");
        for (int i = 0; i < testTime; i++) {
            int s1Len = (int) (Math.random() * len);
            int s2Len = (int) (Math.random() * len);
            String s1 = randomNumberString(s1Len, range);
            String s2 = randomNumberString(s2Len, range);
            String ans1 = right(s1, s2);
            String ans2 = maxCombine(s1, s2);
            if (!ans1.equals(ans2)) {
                System.out.println("Oops");
                System.out.println(s1);
                System.out.println(s2);
                System.out.println(ans1);
                System.out.println(ans2);
                break;
            }
        }
        System.out.println("功能测试结束");
        System.out.println("==========");

        System.out.println("性能测试开始");
        int s1Len = 100000;
        int s2Len = 500;
        String s1 = randomNumberString(s1Len, range);
        String s2 = randomNumberString(s2Len, range);
        long start = System.currentTimeMillis();
        maxCombine(s1, s2);
        long end = System.currentTimeMillis();
        System.out.println("运行时间：" + (end - start) + "ms");
        System.out.println("性能测试结束");
    }
}
