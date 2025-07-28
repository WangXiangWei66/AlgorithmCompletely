package Class26;

import java.util.LinkedList;
import java.util.List;

//给定一个仅包含数字0-9的字符串和一个目标值，在数字之间添加 二元 运算符（不是一元）+、-或*，返回所有能够得到目标值的表达式。
//输入: num = "123", target = 6
//输出: ["1+2+3", "1*2*3"]
//示例2:
//输入: num = "232", target = 8
//输出: ["2*3+2", "2+3*2"]
//示例 3:
//输入: num = "105", target = 5
//输出: ["1*0+5","10-5"]
//示例4:
//输入: num = "00", target = 0
//输出: ["0+0", "0-0", "0*0"]
//Leetcode题目：https://leetcode.com/problems/expression-add-operators/
public class Code03_ExpressionAndOperators {

    public static List<String> addOperators(String num, int target) {
        List<String> ret = new LinkedList<>();//存储结果的列表
        if (num.length() == 0) {
            return ret;//空字符串直接返回空结果
        }
        // path数组用于存储当前表达式，长度最大为num长度*2-1（每个数字间最多一个运算符）
        char[] path = new char[num.length() * 2 - 1];
        char[] digits = num.toCharArray();//将输入的字符串转化为字符数组
        long n = 0;//构建初始数字
        for (int i = 0; i < digits.length; i++) {
            n = n * 10 + digits[i] - '0';  // 构建数字，实现字符串的一个拼接
            path[i] = digits[i];
            dfs(ret, path, i + 1, 0, n, digits, i + 1, target);
            // 处理前导零：如果当前数字是0，不能再向后拼接（如"01"是无效数字）
            if (n == 0) {
                break;
            }
        }
        return ret;
    }
    //res：计算结果=aim的表达式字符串
    //path:存储当前构建的表达式
    //len:当前表达式的长度
    //left：表达式中已经确定的部分（不包含当前正在计算的乘法项）
    //cur：当前正在计算的乘法项（因为乘法优先级高于加减）
    //num : 原始数字字符串转换的字符数组
    //index：当前处理到的数字在num中的位置
    //aim：目标结果值
    public static void dfs(List<String> res, char[] path, int len, long left, long cur, char[] num, int index, int aim) {
        if (index == num.length) {
            if (left + cur == aim) {
                res.add(new String(path, 0, len));
            }
            return;
        }
        long n = 0;//构建当前要操作的数字
        int j = len + 1;//记录当前path中存储下一个数字的起始位置
        for (int i = index; i < num.length; i++) {
            n = n * 10 + num[i] - '0';
            path[j++] = num[i];//将当前正在处理的数字字符存入path
            path[len] = '+';
            dfs(res, path, j, left + cur, n, num, i + 1, aim);
            path[len] = '-';
            dfs(res, path, j, left + cur, -n, num, i + 1, aim);
            path[len] = '*';
            dfs(res, path, j, left, cur * n, num, i + 1, aim);
            if (num[index] == '0') {
                break;
            }
        }
    }
}
