package Class19;

public class Code04_LongestCommonSubsequence {

    public static int longestCommonSubsequence1(String s1, String s2) {
        if (s1 == null || s2 == null || s1.length() == 0 || s2.length() == 0) {
            return 0;
        }
        char[] str1 = s1.toCharArray();
        char[] str2 = s2.toCharArray();
        //尝试
        return process1(str1, str2, str1.length - 1, str2.length - 1);
    }

    //str[0.....i]和str[0.....j]这个范围上最长公共子序列的长度是多少
    //可能性分类
    //a)最长公共子序列，一定不以str1[i]字符结尾，也一定不以str2[j]字符结尾
    //b)最长公共子序列，可能以str1[i]字符结尾，但是一定不以str2[j]字符结尾
    //c)最长公共子序列，一定不以str1[i]字符结尾，但是可能以str2[j]字符结尾
    //d)最长公共子序列，必须以str1[i]字符结尾，也必须以str2[j]字符结尾
    //注意a) b) c) d)并不是完全互斥的，他们可能会有重叠的情况
    //但是可以肯定，答案不会超过这四种可能性的范围
    //那么来分别看一下，这几种可能性怎么调用后续的递归
    //a)最长公共子序列，一定不以str1[i]字符结尾，也一定不以str2[j]字符结尾
    //   如果是这种情况，那么有没有str[i]和str[j]就根本不重要了，因为这两个字符一定没用
    //   所以砍掉这两个字符，最长公共子序列=str1[0...i-1] 与str2[0...j-1]的最长公共子序列的长度（后续递归）
    //b)最长公共子序列，可能以str1[i]字符结尾，但是一定不以str2[j]字符结尾
    //   如果是这种情况，那么可以确定str2[j]一定没用，要砍掉，但是str1[i]可能有用，要保留
    //   最长公共子序列=str1[0...i] 与str2[0...j-1]的最长公共子序列的长度（后续递归）
    //c)最长公共子序列，一定不以str1[i]字符结尾，但是可能以str2[j]字符结尾
    //   如果是这种情况，那么可以确定str1[i]一定没用，要砍掉，但是str2[j]可能有用，要保留
    //    最长公共子序列=str1[0...i-1] 与str2[0...j]的最长公共子序列的长度（后续递归）
    //d)最长公共子序列，必须以str1[i]字符结尾，也必须以str2[j]字符结尾
    //    同时可以看到，可能性d)存在的条件，一定是在str1[i]==str2[j]的情况下才成立
    //     所以，最长公共子序列=str1[0...i-1] 与str2[0...j-1]的最长公共子序列的长度（后续递归）+1（共同的结尾）
    //综上，四种情况已经穷尽了所有的可能性，四种情况取最大即可
    //其中b) c)一定参与最大值的比较
    //当str1[i]==str2[j]时，a)一定比d)小，所以d)参与
    //当str1[i]！=str2[j]时，d)压根不存在，所以a)参与
    //但是再次注意了
    //a)是str1[0...i-1] 与str2[0...j-1]的最长公共子序列的长度
    //b)是str1[0...i] 与str2[0...j-1]的最长公共子序列的长度
    //c)是str1[0...i-1] 与str2[0...j]的最长公共子序列的长度
    //a)中str1的范围<b)中str1的范围  ，a)中str2的范围==b)中str2的范围
    //所以a)中不用求也知道，他是比不过b)的，因为有一个样本的范围比b)小
    //a)中str1的范围==c)中str1的范围  ，a)中str2的范围<c)中str2的范围
    //   所以a)中不用求也知道，他是比不过c)的，因为有一个样本的范围比c)小
    //至此，可以知道，a)就是个垃圾，有他没他，都不影响最大值的决策
    //所以当str1[i]==str2[j]时，b) c) d)中选出最大值
    //当str1[i]!=str2[j]时，b) c)选出最大值
    public static int process1(char[] str1, char[] str2, int i, int j) {
        if (i == 0 && j == 0) {
            //str1[0...0]和str2[0...0]都只剩一个字符了
            //那如果字符相等，公共子序列的长度就是1，不相等就是0
            //这显而易见
            return str1[i] == str2[j] ? 1 : 0;
        } else if (i == 0) {
            //这里的情况为:
            //str1[0...0]和str2[0...j],str1只剩1个字符了，但是str2不止一个字符
            //因为str1只剩一个字符，所以str1[0...0]和str2[0...j]公共子序列的长度最多为1
            //如果str1[0]==str2[j] 那么此时相等已经找到了，公共子序列长度就是1，也不可能更大了
            //如果str1[0]!=str2[j] ,只是此时不相等而已
            //那么str2[0...j-1]上有没有字符等于str1[0]呢?不知道，所以递归继续找
            if (str1[i] == str2[j]) {
                return 1;
            } else {
                return process1(str1, str2, i, j - 1);
            }
        } else if (j == 0) {
            //和上面的else if 同理
            //str1[0...i]和str2[0...0],str2只剩1个字符了，但是str1不止一个字符
            //因为str2只剩一个字符，所以str1[0...i]和str2[0...0]公共子序列的长度最多为1
            //如果str1[i]==str2[0] 那么此时相等已经找到了，公共子序列长度就是1，也不可能更大了
            //如果str1[i]!=str2[0] ,只是此时不相等而已
            //那么str1[0...i-1]上有没有字符等于str2[0]呢?不知道，所以递归继续找
            if (str1[i] == str2[j]) {
                return 1;
            } else {
                return process1(str1, str2, i - 1, j);
            }
        } else {
            //i!=0&&j!=0
            //这里的情况为
            //str1[0...i]和str2[0...j]都不止一个字符
            //p1为可能性c)
            int p1 = process1(str1, str2, i - 1, j);
            //p2就是可能性b)
            int p2 = process1(str1, str2, i, j - 1);
            //p3就是可能性d)，如果可能性d)存在，即str1[i]==str2[j]那么p3就求出来，参与pk
            //如果可能性d)不存在，即str1[i]！=str2[j] 那么让p3==0,然后去参与pk，反正不影响
            int p3 = str1[i] == str2[j] ? (1 + process1(str1, str2, i - 1, j - 1)) : 0;
            return Math.max(p1, Math.max(p2, p3));
        }
    }

    public static int longestCommonSubsequence2(String s1, String s2) {
        if (s1 == null || s2 == null || s1.length() == 0 || s2.length() == 0) {
            return 0;
        }
        char[] str1 = s1.toCharArray();
        char[] str2 = s2.toCharArray();
        int N = str1.length;
        int M = str2.length;
        int[][] dp = new int[N][M];
        dp[0][0] = str1[0] == str2[0] ? 1 : 0;
        for (int j = 1; j < M; j++) {
            dp[0][j] = str1[0] == str2[j] ? 1 : dp[0][j - 1];
        }
        for (int i = 1; i < N; i++) {
            dp[i][0] = str1[i] == str2[0] ? 1 : dp[i - 1][0];
        }
        for (int i = 1; i < N; i++) {
            for (int j = 1; j < M; j++) {
                int p1 = dp[i - 1][j];
                int p2 = dp[i][j - 1];
                int p3 = str1[i] == str2[j] ? (1 + dp[i - 1][j - 1]) : 0;
                dp[i][j] = Math.max(p1, Math.max(p2, p3));
            }
        }
        return dp[N - 1][M - 1];
    }
}
