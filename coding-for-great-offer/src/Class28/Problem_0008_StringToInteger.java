package Class28;

//请你来实现一个myAtoi(string s)函数，使其能将字符串转换成一个 32 位有符号整数（类似 C/C++ 中的 atoi 函数）
//函数myAtoi(string s) 的算法如下：
//读入字符串并丢弃无用的前导空格
//检查下一个字符（假设还未到字符末尾）为正还是负号，读取该字符（如果有）。确定最终结果是负数还是正数。 如果两者都不存在，则假定结果为正。
//读入下一个字符，直到到达下一个非数字字符或到达输入的结尾。字符串的其余部分将被忽略。
//将前面步骤读入的这些数字转换为整数（即，"123" -> 123， "0032" -> 32）。如果没有读入数字，则整数为 0 。必要时更改符号（从步骤 2 开始）。
//如果整数数超过 32 位有符号整数范围 [−231, 231− 1] ，需要截断这个整数，使其保持在这个范围内。具体来说，小于 −231 的整数应该被固定为 −231 ，大于 231− 1 的整数应该被固定为231 − 1。
//返回整数作为最终结果。
//注意：本题中的空白字符只包括空格字符 ' ' 。除前导空格或数字后的其余字符串外，请勿忽略 任何其他字符。
//Leetcode题目：https://leetcode.com/problems/string-to-integer-atoi/
public class Problem_0008_StringToInteger {

    public static int myAtoi(String s) {
        if (s == null || s.equals("")) {
            return 0;
        }
        s = removeHeadZero(s.trim());//先去除字符串前后的空格，再去除前导0，并处理非数字字符
        if (s == null || s.equals("")) {
            return 0;
        }
        char[] str = s.toCharArray();
        //判断字符串是否有效
        if (!isValid(str)) {
            return 0;
        }
        boolean posi = str[0] == '-' ? false : true;
        int minq = Integer.MIN_VALUE / 10;
        int minr = Integer.MIN_VALUE % 10;
        int res = 0;
        int cur = 0;//存储当前处理的数字字符
        for (int i = (str[0] == '-' || str[0] == '+') ? 1 : 0; i < str.length; i++) {
            cur = '0' - str[i];//将字符转化为对用的负数形式
            if ((res < minq) || (res == minq && cur < minr)) {//满足这个条件说明会溢出
                return posi ? Integer.MAX_VALUE : Integer.MIN_VALUE;
            }
            res = res * 10 + cur;
        }

        if (posi && res == Integer.MIN_VALUE) {
            return Integer.MAX_VALUE;
        }
        return posi ? -res : res;
    }
    //该方法用于处理前导0和处理非数字字符
    public static String removeHeadZero(String str) {
        boolean r = (str.startsWith("+") || str.startsWith("-"));
        int s = r ? 1 : 0;
        for (; s < str.length(); s++) {
            if (str.charAt(s) != '0') {//去找第一个非0字符的位置
                break;
            }
        }
        int e = -1;//初始化结果索引e为-1
        for (int i = str.length() - 1; i >= (r ? 1 : 0); i--) {
            //寻找有效数字的结束位置
            if (str.charAt(i) < '0' || str.charAt(i) > '9') {
                e = i;
            }
        }
        return (r ? String.valueOf(str.charAt(0)) : "") + str.substring(s, e == -1 ? str.length() : e);//拼接过程
    }
    //验证字符数组是否符合要求
    public static boolean isValid(char[] chas) {
        if (chas[0] != '-' && chas[0] != '+' && chas[0] < '0' || chas[0] > '9') {
            return false;
        }
        if ((chas[0] == '-' || chas[0] == '+') && chas.length == 1) {
            return false;
        }
        for (int i = 1; i < chas.length; i++) {
            if (chas[i] < '0' || chas[i] > '9') {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println(Integer.MAX_VALUE);
    }
}
