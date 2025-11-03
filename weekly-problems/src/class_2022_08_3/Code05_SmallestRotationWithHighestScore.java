package class_2022_08_3;

//给你一个数组nums，我们可以将它按一个非负整数 k 进行轮调，
//例如，数组为nums = [2,4,1,3,0]，
//我们按k = 2进行轮调后，它将变成[1,3,0,2,4]。
//这将记为 3 分，
//因为 1 > 0 [不计分]、3 > 1 [不计分]、0 <= 2 [计 1 分]、
//2 <= 3 [计 1 分]，4 <= 4 [计 1 分]。
//在所有可能的轮调中，返回我们所能得到的最高分数对应的轮调下标 k 。
//如果有多个答案，返回满足条件的最小的下标 k 。
//测试链接 :
//https://leetcode.cn/problems/smallest-rotation-with-highest-score/
public class Code05_SmallestRotationWithHighestScore {
    //将数组向右移动k位后，原下标i的元素会移动到新下标(i - k + n) % n（保证非负）
    public static int bestRotation(int[] nums) {
        int n = nums.length;
        // cnt : 差分数组
        // cnt最后进行前缀和的加工！
        // 加工完了的cnt[0] :  整体向右移动0的距离, 一共能得多少分
        // 加工完了的cnt[i] :  整体向右移动i的距离, 一共能得多少分
        int[] cnt = new int[n + 1];
        for (int i = 0; i < n; i++) {
            // 遍历每个数！
            // 看看每个数，对差分数组哪些范围，会产生影响!
            if (nums[i] < n) {
                if (i <= nums[i]) {
                    add(cnt, nums[i] - i, n - i - 1);
                } else {
                    add(cnt, 0, n - i - 1);
                    add(cnt, n - i + nums[i], n - 1);
                }
            }
        }
        for (int i = 1; i <= n; i++) {
            cnt[i] += cnt[i - 1];
        }
        int max = cnt[0];
        int ans = 0;
        //反向遍历,保证有多个k得分相同时,取最小的
        for (int i = n - 1; i >= 1; i--) {
            if (cnt[i] > max) {
                max = cnt[i];
                ans = i;
            }
        }
        return ans == 0 ? 0 : (n - ans);
    }

    public static void add(int[] cnt, int l, int r) {
        cnt[l]++;
        cnt[r + 1]--;
    }
}

/*
前提：轮调后新下标的计算
数组nums长度为n，当轮调k（向右移动k位）时：
原下标为i的元素，新下标j的计算公式为：
j = (i - k + n) % n
（+n是为了避免i - k为负数，%n保证结果在[0, n-1]范围内）
例如：n=5，i=1，k=2 → j=(1-2+5)%5=4%5=4。
得分条件：nums[i] <= j
即：nums[i] <= (i - k + n) % n我们需要找到所有满足该条件的k值（k的范围是0 <= k < n，因为轮调n位后数组会回到原状态）。
推导步骤
为了简化计算，先去掉取模符号。注意到(i - k + n) % n的结果等价于 “i - k + n减去n的整数倍，最终落在[0, n-1]内”，因此：(i - k + n) % n = i - k + n - m*n（其中m是使得结果在[0, n-1]的整数）
但更简单的方式是分两种情况讨论i - k的正负：
情况 1：i >= k（即i - k >= 0）
此时j = i - k（因为i - k + n已经大于等于n？不，这里直接计算：i >=k → i -k >=0，且i <n、k <n → i -k <n，因此(i -k +n)%n = i -k）。得分条件变为：nums[i] <= i - k移项得：k <= i - nums[i]
情况 2：i < k（即i - k < 0）
此时j = i - k + n（因为i -k为负，+n后落在[0, n-1]内）。得分条件变为：nums[i] <= i - k + n移项得：k >= i - nums[i] + n
合并k的有效范围
结合k的取值范围0 <= k < n，以及nums[i]的大小，分两种场景讨论：
场景 A：nums[i] >= n
此时无论k取何值，j最大为n-1，显然nums[i] <= j不成立（因为nums[i] >=n > n-1）。因此，nums[i]对任何k都无贡献。
场景 B：nums[i] < n
需要结合i与nums[i]的大小进一步分析：
子场景 B1：i <= nums[i]
情况 1（i >=k）的条件k <= i - nums[i]：
由于i <= nums[i]，i - nums[i] <=0，而k >=0，因此只有k=0可能满足，但k=0时k <= i - nums[i] → 0 <= 负数，不成立。因此情况 1 无有效k。
情况 2（i <k）的条件k >= i - nums[i] + n：
因为i <= nums[i]，i - nums[i] <=0 → i - nums[i] +n <=n，而k <n，因此有效范围是k >= (i - nums[i] +n)且k <n。
简化：k ∈ [n + i - nums[i], n-1]？不，再整理：
i - nums[i] +n可能小于 0（例如i=2，nums[i]=3，n=5 → 2-3+5=4），但k >=0，因此实际有效范围是：
k ∈ [nums[i] - i, n - i - 1]
（推导过程略，核心是通过i <k和k <n进一步收缩范围）。
子场景 B2：i > nums[i]
情况 1（i >=k）的条件k <= i - nums[i]：
结合k >=0，有效范围是k ∈ [0, i - nums[i]]。
情况 2（i <k）的条件k >= i - nums[i] +n：
结合k <n，有效范围是k ∈ [i - nums[i] +n, n-1]。
简化后合并为：
k ∈ [0, n - i - 1] ∪ [n - i + nums[i], n-1]
最终结论
每个元素nums[i]对k的贡献区间：
若nums[i] >=n：无贡献。
若nums[i] <n：
当i <= nums[i]时，贡献区间为[nums[i] - i, n - i - 1]。
当i > nums[i]时，贡献区间为[0, n - i - 1] ∪ [n - i + nums[i], n-1]。
 */