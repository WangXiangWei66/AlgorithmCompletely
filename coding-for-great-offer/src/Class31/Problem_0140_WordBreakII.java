package Class31;

import java.util.ArrayList;
import java.util.List;

//给定一个非空字符串 s 和一个包含非空单词列表的字典 wordDict，在字符串中增加空格来构建一个句子，使得句子中所有的单词都在词典中。返回所有这些可能的句子。
//说明：分隔时可以重复使用字典中的单词。你可以假设字典中没有重复的单词。
//示例 1：
//输入:
//s = "catsanddog"
//wordDict = ["cat", "cats", "and", "sand", "dog"]
//输出:
//[
// "cats and dog",
// "cat sand dog"
//]
//示例 2：
//输入:
//s = "pineapplepenapple"
//wordDict = ["apple", "pen", "applepen", "pine", "pineapple"]
//输出:
//[
// "pine apple pen apple",
// "pineapple pen apple",
// "pine applepen apple"
//]
//解释: 注意你可以重复使用字典中的单词。
//示例3：
//输入:
//s = "catsandog"
//wordDict = ["cats", "dog", "sand", "and", "cat"]
//输出:
//[]
//Leetcode题目 : https://leetcode.com/problems/word-break-ii/
public class Problem_0140_WordBreakII {

    public static class Node {
        public String path;//存储当前节点对应的完整单词（仅在end=true时有效）
        public boolean end;//标记单词节点是否为一个单词的结尾
        public Node[] nexts;//子节点数组

        public Node() {
            path = null;
            end = false;
            nexts = new Node[26];
        }
    }

    public static List<String> wordBreak(String s, List<String> wordDict) {
        char[] str = s.toCharArray();
        Node root = gettrie(wordDict);//构建字典树，将单词字典存入其中
        boolean[] dp = getdp(s, root);//生成动态规划数组，判断字串是否可以被拆分
        ArrayList<String> path = new ArrayList<>();//存储回溯路径中的单词
        List<String> ans = new ArrayList<>();//存储最终左右有效拆分的结果
        process(str, 0, root, dp, path, ans);//回溯递归生成所有可能的拆分
        return ans;
    }
    //str：目标字符串的字符数组
    //index：当前拆分的起始索引
    //root：字典树的根节点
    //dp：动态规划数组，用于剪枝
    //path：当前已经选择好的 单词列表
    public static void process(char[] str, int index, Node root, boolean[] dp, ArrayList<String> path, List<String> ans) {
        if (index == str.length) {
            StringBuilder builder = new StringBuilder();
            //将path中的单词拼接为句子，用空格分割
            for (int i = 0; i < path.size() - 1; i++) {
                builder.append(path.get(i) + " ");
            }
            builder.append(path.get(path.size() - 1));//最后一个单词后不加空格
            ans.add(builder.toString());
        } else {
            Node cur = root;
            for (int end = index; end < str.length; end++) {
                int road = str[end] - 'a';//计算当前字符对应的索引
                if (cur.nexts[road] == null) {
                    break;
                }
                cur = cur.nexts[road];
                if (cur.end && dp[end + 1]) {
                    path.add(cur.path);
                    process(str, end + 1, root, dp, path, ans);
                    path.remove(path.size() - 1);//回溯
                }
            }
        }
    }

    public static Node gettrie(List<String> wordDict) {
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
            node.path = str;//标记单词完整字符串
            node.end = true;//标记为单词结尾
        }
        return root;
    }

    public static boolean[] getdp(String s, Node root) {
        char[] str = s.toCharArray();
        int N = str.length;
        boolean[] dp = new boolean[N + 1];//i及其往后是否可以拆分
        dp[N] = true;
        for (int i = N - 1; i >= 0; i--) {
            Node cur = root;
            for (int end = i; end < N; end++) {
                int path = str[end] - 'a';
                if (cur.nexts[path] == null) {
                    break;
                }
                cur = cur.nexts[path];
                if (cur.end && dp[end + 1]) {
                    dp[i] = true;
                    break;//找到一个方式即可
                }
            }
        }
        return dp;
    }
}
