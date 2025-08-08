package Class32;

import java.util.HashMap;//记录余数出现的位置，以检测循环小数

//给定两个整数，分别表示分数的分子numerator 和分母 denominator，以 字符串形式返回小数 。
//如果小数部分为循环小数，则将循环的部分括在括号内。
//如果存在多个答案，只需返回任意一个 。
//对于所有给定的输入，保证答案字符串的长度小于 104 。
//示例 1：
//输入：numerator = 1, denominator = 2
//输出："0.5"
//示例 2：
//输入：numerator = 2, denominator = 1
//输出："2"
//示例 3：
//输入：numerator = 2, denominator = 3
//输出："0.(6)"
//示例 4：
//输入：numerator = 4, denominator = 333
//输出："0.(012)"
//示例 5：
//输入：numerator = 1, denominator = 5
//输出："0.2"
//Leetcode题目 : https://leetcode.com/problems/fraction-to-recurring-decimal/
public class Problem_0166_FractionToRecurringDecimal {
    //时间复杂度和空间复杂度都是O(n)
    public static String fractionToDecimal(int numerator, int denominator) {
        if (numerator == 0) {
            return "0";
        }
        StringBuilder res = new StringBuilder();
        res.append(((numerator > 0) ^ (denominator > 0)) ? "-" : "");//判断分子与分母的符号是否相同
        long num = Math.abs((long) numerator);
        long den = Math.abs((long) denominator);
        res.append(num / den);//将计算的整数部分添加到结果中
        num %= den;//计算余数，用于小数部分的计算
        if (num == 0) {
            return res.toString();
        }
        res.append(".");
        HashMap<Long, Integer> map = new HashMap<Long, Integer>();//存储余数及其在结果字符串中出现的位置，通过这个来检测循环
        map.put(num, res.length());
        while (num != 0) {
            num *= 10;
            res.append(num / den);
            num %= den;
            if (map.containsKey(num)) {
                //在第一次出现的位置插入左括号
                int index = map.get(num);
                res.insert(index, "(");
                res.append(")");
                break;
            } else {
                map.put(num, res.length());
            }
        }
        return res.toString();
    }

    public static void main(String[] args) {
        System.out.println(fractionToDecimal(127, 999));
    }
}
