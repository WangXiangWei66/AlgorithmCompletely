package Class48;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//给定一个 不含重复 单词的字符串数组 words ，编写一个程序，返回words 中的所有 连接词 。
//连接词 的定义为：一个字符串完全是由至少两个给定数组中的单词组成的。
//示例 1：
//输入：words = ["cat","cats","catsdogcats","dog","dogcatsdog","hippopotamuses","rat","ratcatdogcat"]
//输出：["catsdogcats","dogcatsdog","ratcatdogcat"]
//解释："catsdogcats"由"cats", "dog" 和 "cats"组成;
//"dogcatsdog"由"dog", "cats"和"dog"组成;
//"ratcatdogcat"由"rat", "cat", "dog"和"cat"组成。
//示例 2：
//输入：words = ["cat","dog","catdog"]
//输出：["catdog"]
//leetcode题目：https://leetcode.com/problems/concatenated-words/
public class Problem_0472_ConcatenatedWords {

    public static class TrieNode {
        public boolean end;//标记当前节点是否为一个单词的结尾
        public TrieNode[] nexts;//26个英文字母对应的子节点

        public TrieNode() {
            end = false;
            nexts = new TrieNode[26];
        }
    }

    public static void insert(TrieNode root, char[] s) {
        int path = 0;
        for (char c : s) {
            path = c - 'a';
            if (root.nexts[path] == null) {
                root.nexts[path] = new TrieNode();
            }
            root = root.nexts[path];
        }
        root.end = true;
    }

    public static List<String> findAllConcatenatedWordsInADict1(String[] words) {
        List<String> ans = new ArrayList<>();//存储最终结果
        if (words == null || words.length < 3) {
            return ans;
        }
        //按照长度进行排序，因为短单词不可能是连接词
        Arrays.sort(words, (str1, str2) -> (str1.length() - str2.length()));
        TrieNode root = new TrieNode();
        for (String str : words) {
            char[] s = str.toCharArray();
            if (s.length > 0 && split1(s, root, 0)) {
                ans.add(str);
            } else {
                insert(root, s);
            }
        }
        return ans;
    }

    //判断字符串是否可以分割成多个已有的单词
    public static boolean split1(char[] s, TrieNode r, int i) {
        boolean ans = false;
        if (i == s.length) {
            ans = true;
        } else {
            TrieNode c = r;//从根节点开始查
            for (int end = i; end < s.length; end++) {
                int path = s[end] - 'a';
                if (c.nexts[path] == null) {
                    break;
                }
                c = c.nexts[path];
                if (c.end && split1(s, r, end + 1)) {
                    ans = true;
                    break;
                }
            }
        }
        return ans;
    }
    //0：未计算
    //1：从该位置分割成功
    //-1：分割失败
    public static int[] dp = new int[1000];

    public static List<String> findAllConcatenateWordsInADict2(String[] words) {
        List<String> ans = new ArrayList<>();
        if (words == null || words.length < 3) {
            return ans;
        }
        Arrays.sort(words, (str1, str2) -> (str1.length() - str2.length()));
        TrieNode root = new TrieNode();
        for (String str : words) {
            char[] s = str.toCharArray();
            //重置数组，只重置当前长度+1的位置
            Arrays.fill(dp, 0, s.length + 1, 0);
            if (s.length > 0 && split2(s, root, 0, dp)) {
                ans.add(str);
            } else {
                insert(root, s);
            }
        }
        return ans;
    }

    public static boolean split2(char[] s, TrieNode r, int i, int[] dp) {
        if (dp[i] != 0) {
            return dp[i] == 1;
        }
        boolean ans = false;
        if (i == s.length) {
            ans = true;
        } else {
            TrieNode c = r;
            for (int end = i; end < s.length; end++) {
                int path = s[end] - 'a';
                if (c.nexts[path] == null) {
                    break;
                }
                c = c.nexts[path];
                if (c.end && split2(s, r, end + 1, dp)) {
                    ans = true;
                    break;
                }
            }
        }
        dp[i] = ans ? 1 : -1;
        return ans;
    }
}
