package Class28;

//编写一个函数来查找字符串数组中的最长公共前缀，如果不存在公共前缀，返回空字符串""。
//Leetcode题目：https://leetcode.com/problems/longest-common-prefix/
public class Problem_0014_LongestCommonPrefix {
    //本代码的时间复杂度为O(S),其中S是所有字符串中字符的总数
    public static String longestCommonPrefix(String[] strs) {
        if (strs == null || strs.length == 0) {
            return "";
        }
        char[] chs = strs[0].toCharArray(); //将数组中第一个字符串转换为字符数组，作为基准进行比较
        int min = Integer.MAX_VALUE;//用来记录最长公共前缀的长度
        for (String str : strs) {
            char[] tmp = str.toCharArray();
            int index = 0;//记录当前正在比较的位置
            //循环比较：index在当前字符串长度和基准字符串长度范围内
            while (index < tmp.length && index < chs.length) {
                if (chs[index] != tmp[index]) {
                    break;
                }
                index++;//如果对应的字符相等，则去比较下一个字符
            }
            min = Math.min(index, min);
            if (min == 0) {
                return "";
            }
        }
        return strs[0].substring(0, min);
    }
}
