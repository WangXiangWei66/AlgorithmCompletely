package Class38;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//给定两个字符串s和 p，找到s中所有p的异位词的子串，返回这些子串的起始索引。不考虑答案输出的顺序。
//异位词 指字母相同，但排列不同的字符串。
//示例1:
//输入: s = "cbaebabacd", p = "abc"
//输出: [0,6]
//解释:
//起始索引等于 0 的子串是 "cba", 它是 "abc" 的异位词。
//起始索引等于 6 的子串是 "bac", 它是 "abc" 的异位词。
//示例 2:
//输入: s = "abab", p = "ab"
//输出: [0,1,2]
//解释:
//起始索引等于 0 的子串是 "ab", 它是 "ab" 的异位词。
//起始索引等于 1 的子串是 "ba", 它是 "ab" 的异位词。
//起始索引等于 2 的子串是 "ab", 它是 "ab" 的异位词。
//提示:
//1 <= s.length, p.length <= 3 * 10^4
//s和p仅包含小写字母
//Leetcode题目 : https://leetcode.com/problems/find-all-anagrams-in-a-string/
public class Problem_0438_FindAllAnagramsInAString {
    //时间复杂度为O(N),额外空间复杂度为O(1)
    public static List<Integer> findAnagrams(String s, String p) {
        List<Integer> ans = new ArrayList<>();//存储结果的起始索引列表
        if (s == null || p == null || s.length() < p.length()) {
            return ans;
        }
        char[] str = s.toCharArray();
        int N = str.length;
        char[] pst = p.toCharArray();
        int M = pst.length;
        //计算p中每个字符出现的频次
        HashMap<Character, Integer> map = new HashMap<>();
        for (char cha : pst) {
            if (!map.containsKey(cha)) {
                map.put(cha, 1);
            } else {
                map.put(cha, map.get(cha) + 1);
            }
        }
        int all = M;//需要匹配的有效字符的总数
        for (int end = 0; end < M - 1; end++) {
            if (map.containsKey(str[end])) {
                int count = map.get(str[end]);
                if (count > 0) {
                    all--;
                }
                map.put(str[end], count - 1);
            }
        }
        for (int end = M - 1, start = 0; end < N; end++, start--) {
            if (map.containsKey(str[end])) {
                int count = map.get(str[end]);
                if (count > 0) {
                    all--;
                }
                map.put(str[end], count - 1);
            }
            if (all == 0) {
                ans.add(start);
            }
            //恢复
            if (map.containsKey(str[start])) {
                int count = map.get(str[start]);
                if (count >= 0) {
                    all++;
                }
                map.put(str[start], count + 1);
            }
        }
        return ans;
    }
}
