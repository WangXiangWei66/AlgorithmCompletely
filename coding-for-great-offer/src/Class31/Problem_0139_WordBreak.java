package Class31;

import java.util.List;

//给定一个非空字符串 s 和一个包含非空单词的列表 wordDict，判定s 是否可以被空格拆分为一个或多个在字典中出现的单词。
//说明：拆分时可以重复使用字典中的单词。你可以假设字典中没有重复的单词。
//Leetcode题目 : https://leetcode.com/problems/word-break/
//Lintcode题目 : https://www.lintcode.com/problem/107/
public class Problem_0139_WordBreak {
    //定义了字典树的节点类
    public static class Node {
        public boolean end;//当前节点是否是一个单词的结尾
        public Node[] nexts;//对应26个小写英文字母

        public Node() {
            end = false;
            nexts = new Node[26];
        }
    }

    public static boolean wordBreak1(String s, List<String> wordDict) {
        Node root = new Node();//字典树的根节点
        for (String str : wordDict) {
            char[] chs = str.toCharArray();
            Node node = root;
            int index = 0;
            for (int i = 0; i < chs.length; i++) {
                index = chs[i] - 'a';//字符对应的索引
                if (node.nexts[index] == null) {
                    node.nexts[index] = new Node();
                }
                node = node.nexts[index];
            }
            node.end = true;
        }
        char[] str = s.toCharArray();
        int N = str.length;
        //dp[i]:字符串从位置i到结尾是否可以被拆分
        boolean[] dp = new boolean[N + 1];
        dp[N] = true;//空字符串可以被拆分
        for (int i = N - 1; i >= 0; i--) {
            Node cur = root;//当前在字典树中的节点位置
            for (int end = i; end < N; end++) {
                cur = cur.nexts[str[end] - 'a'];//移动到当前字符对应的字典树节点
                if (cur == null) {
                    break;
                }
                if (cur.end) {
                    dp[i] |= dp[end + 1];//只需要有一种拆分方式即可
                }
                if (dp[i]) {
                    break;
                }
            }
        }
        return dp[0];
    }

    public static int wordBreak2(String s, List<String> wordDict) {
        Node root = new Node();
        for (String str : wordDict) {
            char[] chs = str.toCharArray();
            Node node = root;
            int index = 0;
            for (int i = 0; i < chs.length; i++) {
                index = chs[i] - 'a';
                if (node.nexts[index] == null) {
                    node.nexts[index] = new Node();
                }
                node = node.nexts[index];
            }
            node.end = true;
        }
        char[] str = s.toCharArray();
        int N = str.length;
        //dp[i]:表示字符串从位置i到结尾的所有可能的拆分的数量
        int[] dp = new int[N + 1];
        dp[N] = 1;
        for (int i = N - 1; i >= 0; i--) {
            Node cur = root;
            for (int end = i; end < N; end++) {
                cur = cur.nexts[str[end] - 'a'];
                if (cur == null) {
                    break;
                }
                if (cur.end) {
                    dp[i] += dp[end + 1];
                }
            }
        }
        return dp[0];
    }
}
