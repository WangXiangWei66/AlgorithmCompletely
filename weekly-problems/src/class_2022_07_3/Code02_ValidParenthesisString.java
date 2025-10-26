package class_2022_07_3;

//来自蔚来汽车
//给定一个只包含三种字符的字符串：( 、)和 *，
//写一个函数来检验这个字符串是否为有效字符串。有效字符串具有如下规则：
//任何左括号 (必须有相应的右括号 )
//任何右括号 )必须有相应的左括号 (
//左括号 ( 必须在对应的右括号之前 )
//*可以被视为单个右括号 )，或单个左括号 (，或一个空字符。
//  一个空字符串也被视为有效字符串。
//  测试链接 : https://leetcode.cn/problems/valid-parenthesis-string/
public class Code02_ValidParenthesisString {

    public static boolean valid(String str) {
        char[] s = str.toCharArray();
        int n = s.length;
        //dp[i][j] 表示从索引i开始，当前左括号比右括号多j个时，后续能否形成有效字符串
        int[][] dp = new int[n][n + 1];
        // dp[i][j] == 0 没算过！
        // dp[i][j] == -1 算过了！结果是false！
        // dp[i][j] == 1 算过了！结果是true！
        return wang(s, 0, 0, dp);
    }

    //i:读取处理到的索引
    //c：读取左括号比右括号多的数量
    public static boolean wang(char[] s, int i, int c, int[][] dp) {
        if (i == s.length) {
            return c == 0;
        }
        if (c < 0) {
            return false;
        }
        if (dp[i][c] != 0) {
            return dp[i][c] == 1;
        }
        boolean ans = false;
        if (s[i] == '(') {
            ans = wang(s, i + 1, c + 1, dp);
        } else if (s[i] == ')') {
            ans = wang(s, i + 1, c - 1, dp);
        } else {
            boolean p1 = wang(s, i + 1, c + 1, dp);
            boolean p2 = wang(s, i + 1, c - 1, dp);
            boolean p3 = wang(s, i + 1, c, dp);
            ans = p1 || p2 || p3;
        }
        dp[i][c] = ans ? 1 : -1;
        return ans;
    }

    public static boolean checkValidString1(String s) {
        char[] str = s.toCharArray();
        int n = str.length;
        int[][] dp = new int[n][n];
        return f(str, 0, 0, dp);
    }

    public static boolean f(char[] s, int i, int c, int[][] dp) {
        if (i == s.length) {
            return c == 0;
        }
        if (c < 0) {
            return false;
        }
        if (c > s.length - i) {
            return false;
        }
        if (dp[i][c] != 0) {
            return dp[i][c] == 1;
        }
        boolean ans = false;
        if (s[i] == '(') {
            ans = f(s, i + 1, c + 1, dp);
        } else if (s[i] == ')') {
            ans = f(s, i + 1, c - 1, dp);
        } else {
            ans |= f(s, i + 1, c + 1, dp);
            ans |= f(s, i + 1, c - 1, dp);
            ans |= f(s, i + 1, c, dp);
        }
        dp[i][c] = ans ? 1 : -1;
        return ans;
    }

    public static boolean checkValidString2(String s) {
        char[] str = s.toCharArray();
        int max = 0;//最大净左括号数
        int min = 0;//最小净左括号数
        for (char x : str) {
            if (x == '(') {
                max++;
                min++;
            } else {
                if (x == ')' && max == 0) {
                    return false;
                }
                max += x == ')' ? -1 : 1;
                if (min > 0) {
                    min--;
                }
            }
        }
        return min == 0;
    }
}
