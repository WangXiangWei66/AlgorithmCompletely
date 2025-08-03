package Class30;

//一条包含字母A-Z 的消息通过以下映射进行了 编码 ：
//'A' -> 1
//'B' -> 2
//...
//'Z' -> 26
//要 解码 已编码的消息，所有数字必须基于上述映射的方法，反向映射回字母（可能有多种方法）。例如，"11106" 可以映射为：
//"AAJF" ，将消息分组为 (1 1 10 6)
//"KJF" ，将消息分组为 (11 10 6)
//注意，消息不能分组为 (1 11 06) ，因为 "06" 不能映射为 "F" ，这是由于 "6" 和 "06" 在映射中并不等价。
//给你一个只含数字的 非空 字符串 s ，请计算并返回 解码 方法的 总数 。
//题目数据保证答案肯定是一个 32 位 的整数。
//Leetcode题目 : https://leetcode.com/problems/decode-ways/
public class Problem_0091_DecodeWays {
    //本代码时间复杂度为O(2^n),空间复杂度为O(n)
    public static int numDecodings1(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        char[] str = s.toCharArray();
        return process(str, 0);
    }

    //index:当前处理到的位置索引
    public static int process(char[] str, int index) {
        if (index == str.length) {
            return 1;
        }
        //如果当前位置为0，无法单独解码
        if (str[index] == '0') {
            return 0;
        }
        int ways = process(str, index + 1);
        if (index + 1 == str.length) {
            return ways;
        }
        //计算当前字符和下一个字符组成的两位数数值
        int num = (str[index] - '0') * 10 + str[index + 1] - '0';
        //如果组成的两位数在1~26以内，则去处理index+2位置
        if (num < 27) {
            ways += process(str, index + 2);
        }
        return ways;
    }

    //时间复杂度为O(N),额外空间复杂度为O（N）
    public static int numDecodings2(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        char[] str = s.toCharArray();
        int N = str.length;
        //dp[i]:从索引i开始到字符串末尾的解码方法数
        int[] dp = new int[N + 1];
        dp[N] = 1;//处理到字符串末尾时，也是一种解码方式
        for (int i = N - 1; i >= 0; i--) {
            if (str[i] != '0') {
                dp[i] = dp[i + 1];
                if (i + 1 == str.length) {
                    continue;
                }
                int num = (str[i] - '0') * 10 + str[i + 1] - '0';
                if (num <= 26) {
                    dp[i] = dp[i + 2];
                }
            }
        }
        return dp[0];
    }

    public static int numDecodings(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        char[] str = s.toCharArray();
        int N = str.length;
        int[] dp = new int[N + 1];
        dp[N] = 1;
        for (int i = N - 1; i >= 0; i--) {
            if (str[i] == '0') {
                dp[i] = 0;
            } else if (str[i] == '1') {
                dp[i] = dp[i + 1];
                if (i + 1 < N) {
                    dp[i] += dp[i + 2];
                }
            } else if (str[i] == '2') {
                dp[i] = dp[i + 1];
                if (i + 1 < str.length && (str[i + 1] >= '0' && str[i + 1] <= '6')) {
                    dp[i] += dp[i + 2];
                }
            } else {
                dp[i] = dp[i + 1];
            }
        }
        return dp[0];
    }
}
