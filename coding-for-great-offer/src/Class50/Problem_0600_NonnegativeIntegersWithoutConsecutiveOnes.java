package Class50;

//给定一个正整数 n，找出小于或等于 n 的非负整数中，其二进制表示不包含连续的1的个数。
//示例 1:
//输入: 5
//输出: 5
//解释:
//下面是带有相应二进制表示的非负整数<= 5：
//0 : 0
//1 : 1
//2 : 10
//3 : 11
//4 : 100
//5 : 101
//其中，只有整数3违反规则（有两个连续的1），其他5个满足规则。
//说明：1 <= n <= 10^9
//leetcode题目：https://leetcode-cn.com/problems/non-negative-integers-without-consecutive-ones/
public class Problem_0600_NonnegativeIntegersWithoutConsecutiveOnes {

    public static int findIntegers(int n) {
        //找到n的二进制表示中，最高位1的位置
        int i = 31;
        for (; i >= 0; i--) {
            if ((n & (1 << i)) != 0) {
                break;
            }
        }
        //pre：前一位是否为1(记录的是前一个二进制的值）
        //alreadyLess：当前构建的数字是否小于n，0表示未确定，1表示已确定
        //index：当前处理的二进制位索引
        int[][][] dp = new int[2][2][i + 1];
        return f(0, 0, i, n, dp);
    }
    //递归是从最高位往低位来处理的
    public static int f(int pre, int alreadyLess, int index, int num, int[][][] dp) {
        //所有位都已经处理完毕
        if (index == -1) {
            return 1;
        }
        if (dp[pre][alreadyLess][index] != 0) {
            return dp[pre][alreadyLess][index];
        }
        int ans = 0;
        //前一位是1当前位只能为0
        if (pre == 1) {
            ans = f(0, Math.max(alreadyLess, (num & (1 << index)) != 0 ? 1 : 0), index - 1, num, dp);
        } else {
            if ((num & (1 << index)) == 0 && alreadyLess == 0) {//num位的数据已经为0了
                ans = f(0, alreadyLess, index - 1, num, dp);
            } else {
                ans = f(1, alreadyLess, index - 1, num, dp) + f(0, Math.max(alreadyLess, (num & (1 << index)) != 0 ? 1 : 0), index - 1, num, dp);
            }
        }
        dp[pre][alreadyLess][index] = ans;
        return ans;
    }
}
