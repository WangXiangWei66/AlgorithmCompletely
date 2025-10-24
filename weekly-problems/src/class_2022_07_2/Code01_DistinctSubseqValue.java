package class_2022_07_2;

import java.util.HashMap;

//给定一个字符串 s，计算 s 的 不同非空子序列 的个数
//因为结果可能很大，所以返回答案需要对 10^9 + 7 取余 。
//字符串的 子序列 是经由原字符串删除一些（也可能不删除）字符
//但不改变剩余字符相对位置的一个新字符串。
//本题测试链接 : https://leetcode.com/problems/distinct-subsequences-ii/
public class Code01_DistinctSubseqValue {

    public static int distinctSubSeqII(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        int m = 1000000007;
        char[] str = s.toCharArray();
        int[] count = new int[26];//以第 i 个字母（'a' + i）结尾的不同子序列的个数。
        int all = 1;//包含空序列在内的所有不同子序列的个数
        for (char x : str) {
            int add = (all - count[x - 'a'] + m) % m;
            all = (all + add) % m;
            count[x - 'a'] = (count[x - 'a'] + add) % m;
        }
        return (all - 1 + m) % m;
    }

    public static int wang(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        int m = 1000000007;
        char[] str = s.toCharArray();
        HashMap<Character, Integer> map = new HashMap<>();
        int all = 1;
        for (char x : str) {
            int newAdd = all;
            int curAll = all;
            curAll = (curAll + newAdd) % m;
            curAll = (curAll - (map.containsKey(x) ? map.get(x) : 0) + m) % m;
            all = curAll;
            map.put(x, newAdd);
        }
        return all;
    }

    public static void main(String[] args) {
        String s = "bccaccbaabbc";
        System.out.println(distinctSubSeqII(s) + 1);
        System.out.println(wang(s));
    }
}
