package Class49;

//给定整数n和k，找到1到n中字典序第k小的数字。
//注意：1 ≤ k ≤ n ≤ 10^9。
//示例 :
//输入:
//n: 13   k: 2
//输出:
//10
//解释:
//字典序的排列是 [1, 10, 11, 12, 13, 2, 3, 4, 5, 6, 7, 8, 9]，所以第二小的数字是 10。
//leetcode题目：https://leetcode.com/problems/k-th-smallest-in-lexicographical-order/
public class Problem_0440_KthSmallestInLexicographicalOrder {
    // offset[i] = 10^(i-1)，用于提取数字的高位或计算位数
    public static int[] offset = {0, 1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000, 1000000000};
    // number[i] = (10^i - 1)/9，即i个1组成的数（如number[3]=111），用于计算某一前缀下的数字总数
    public static int[] number = {0, 1, 11, 111, 1111, 11111, 111111, 1111111, 11111111, 111111111, 1111111111};
    //按照字典序的层级划分范围
    public static int findKthNumber(int n, int k) {
        int len = len(n);
        int first = n / offset[len];//提取n的最高位数字
        //最高位小于first的所有数字总数
        int left = (first - 1) * number[len];
        int pick = 0;//选中的前缀
        int already = 0;//已跳过的数字数量
        //第k小的数字是否在“在最高位小于first”的范围内
        if (k <= left) {
            pick = (k + number[len] - 1) / number[len];//第k小的前缀在哪
            already = (pick - 1) * number[len];
            //递归查找该前缀下的第(k-already)小的数字
            return kth((pick + 1) * offset[len] - 1, len, k - already);
        }
        //最高位等于dirst的数字总数
        int mid = number[len - 1] + (n % offset[len]) + 1;//最高位的数字总数
        if (k - left <= mid) {
            return kth(n, len, k - left);
        }
        k -= left + mid;//减去前两部分的数量（小于和等于first的总数）
        len--;
        pick = (k + number[len] - 1) / number[len] + first;
        already = (pick - first - 1) * number[len];
        return kth((pick + 1) * offset[len] - 1, len, k - already);
    }

    public static int len(int n) {
        int len = 0;
        while (n != 0) {
            n /= 10;
            len++;
        }
        return len;
    }
    //递归查找在指定范围内（最大值为max,位数为len)的第kth小的数字
    public static int kth(int max, int len, int kth) {
        boolean closeToMax = true;
        int ans = max / offset[len];//当前拼接的数字，初始为范围的最高位
        while (--kth > 0) {
            max %= offset[len--];//更新max为去除最高位的值
            int pick = 0;//记录当前要拼接的下一位数字
            if (!closeToMax) {
                pick = (kth - 1) / number[len];
                ans = ans * 10 + pick;
                kth -= pick * number[len];//减去该数字前缀下的所有数
            } else {
                int first = max / offset[len];//当前max的最高位
                int left = first * number[len];//下一位数字小于first的数字总数
                if (kth <= left) {
                    closeToMax = false;
                    pick = (kth - 1) / number[len];
                    ans = ans * 10 + pick;
                    kth -= pick * number[len];//减去pick前缀下的数字总数
                    continue;
                }
                kth -= left;
                int mid = number[len - 1] + (max % offset[len]) + 1;
                if (kth <= mid) {
                    ans = ans * 10 + first;
                    continue;
                }
                closeToMax = false;
                kth -= mid;
                len--;
                pick = (kth + number[len] - 1) / number[len] + first;
                ans = ans * 10 + pick;
                kth -= (pick - first - 1) * number[len];
            }
        }
        return ans;
    }
}
