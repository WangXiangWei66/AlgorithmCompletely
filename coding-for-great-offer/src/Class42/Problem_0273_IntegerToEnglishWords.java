package Class42;

//给定一个整数num，返回一个字符串str，是num的英文表达
//leetcode题目 : https://leetcode.com/problems/integer-to-english-words/
public class Problem_0273_IntegerToEnglishWords {

    public static String num1To19(int num) {
        if (num < 1 || num > 19) {
            return "";
        }
        String[] names = {"One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten",
                "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen",
                "Nineteen"};
        return names[num - 1];
    }

    public static String num1To99(int num) {
        if (num < 1 || num > 99) {
            return "";
        }
        if (num < 20) {
            return num1To19(num);
        }
        int high = num / 10;//取出十位数字
        String[] tyNames = {"Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety"};
        return tyNames[high - 2] + num1To19(num % 10);
    }

    public static String num1To999(int num) {
        if (num < 1 || num > 999) {
            return "";
        }
        if (num < 100) {
            return num1To99(num);
        }
        int high = num / 100;//取出百位数字
        return num1To19(high) + "Hundred" + num1To99(num % 100);
    }
    //整数转英文
    public static String numberToWords(int num) {
        if (num == 0) {
            return "Zero";
        }
        String res = "";
        if (num < 0) {
            res = "Negative";
        }
        if (num == Integer.MIN_VALUE) {
            res += "Two Billion";
            num %= -2000000000;//取出系统最小值的剩余部分
        }
        num = Math.abs(num);
        //定义量级：从高到低依次为10^9（Billion）、10^6（Million）、10^3（Thousand）、1（无名称）
        int high = 1000000000;//初始最高量级
        int highIndex = 0;//量级名称数组的索引
        String[] names = {"Billion", "Million", "Thousand", ""};
        while (num != 0) {
            int cur = num / high;
            num %= high;
            if (cur != 0) {
                res += num1To999(cur);
                res += names[highIndex];
            }
            high /= 1000;
            highIndex++;
        }
        return res.trim();//取出结果前后的空格情况
    }

    public static void main(String[] args) {
        int test = Integer.MIN_VALUE;
        System.out.println(test);
        test = -test;
        System.out.println(test);
        int num = -10001;
        System.out.println(numberToWords(num));
    }
}
