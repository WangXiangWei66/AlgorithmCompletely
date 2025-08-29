package Class45;

import java.util.HashSet;

//给定两个字符串pattern和s，返回s是否符合pattern的规定
//这里的符合是指，pattern里的每个字母和字符串s中子串，存在着单一映射双向连接的对应
//例子1：
//输入：pattern = "abab", s = "redblueredblue"
//输出：true
//解释：
//映射如下
//'a' -> "red"
//'b' -> "blue"
//例子2
//输入：pattern = "aaaa", s = "asdasdasdasd"
//输出：true
//解释:
//映射如下
//'a' -> "asd"
//例子3
//输入: pattern = "abab", s = "asdasdasdasd"
//输出: true
//解释:
//映射如下
//'a' -> "a"
//'b' -> "sdasd"
//例子4
//输入：pattern = "aabb", s = "xyzabcxzyabc"
//输出：false
//Leetcode题目 : https://leetcode.com/problems/word-pattern-ii/
public class Problem_0291_WordPatternII {
    //使用映射数组+哈希集合来保证双向唯一性
    //new String[26]：字符到字串的映射数组
    public static boolean wordPatternMatch(String pattern, String str) {
        return match(str, pattern, 0, 0, new String[26], new HashSet<>());
    }

    public static boolean match(String s, String p, int si, int pi, String[] map, HashSet<String> set) {
        if (pi == p.length() && si == s.length()) {
            return true;
        }
        if (pi == p.length() || si == s.length()) {
            return false;
        }
        char ch = p.charAt(pi);//拿到当前需要匹配的模式字符
        String cur = map[ch - 'a'];
        if (cur != null) {
            return si + cur.length() <= s.length()
                    && cur.equals(s.substring(si, si + cur.length()))//长度是一致的
                    && match(s, p, si + cur.length(), pi + 1, map, set);
        }
        //当前模式字符无映射
        //剪枝优化：计算字串的最大可能长度
        int end = s.length();
        for (int i = p.length() - 1; i > pi; i--) {
            end -= map[p.charAt(i) - 'a'] == null ? 1 : map[p.charAt(i) - 'a'].length();
        }
        //遍历所有的字串，尝试映射并回溯
        for (int i = si; i < end; i++) {
            cur = s.substring(si, i + 1);
            if (!set.contains(cur)) {
                set.add(cur);
                map[ch - 'a'] = cur;
                if (match(s, p, i + 1, pi + 1, map, set)) {
                    return true;
                }
                //回溯操作，继续其他的尝试
                map[ch - 'a'] = null;
                set.remove(cur);
            }
        }
        return false;
    }
}
