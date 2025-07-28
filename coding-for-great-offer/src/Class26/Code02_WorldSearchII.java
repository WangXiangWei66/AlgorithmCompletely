package Class26;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

//给定一个m x n 二维字符网格board和一个单词（字符串）列表 words，找出所有同时在二维网格和字典中出现的单词
//单词必须按照字母顺序，通过 相邻的单元格 内的字母构成，其中“相邻”单元格是那些水平相邻或垂直相邻的单元格
//同一个单元格内的字母在一个单词中不允许被重复使用。
//Leetcode题目：https://leetcode.com/problems/word-search-ii/
public class Code02_WorldSearchII {

    public static class TrieNode {
        public TrieNode[] nexts;//子节点数组，对应26个小写字母
        public int pass;//经过该节点的单词数量
        public boolean end;//标记是否为单词的结尾

        public TrieNode() {
            nexts = new TrieNode[26];
            pass = 0;
            end = false;
        }
    }

    public static void fillWord(TrieNode head, String word) {
        head.pass++;//根节点的pass+1 表示有新单词加入
        char[] chs = word.toCharArray();
        int index = 0;
        TrieNode node = head;
        for (int i = 0; i < chs.length; i++) {
            index = chs[i] - 'a';// 计算当前字符对应的索引（0-25）
            if (node.nexts[index] == null) {
                node.nexts[index] = new TrieNode();
            }
            node = node.nexts[index];
            node.pass++;
        }
        node.end = true;
    }

    public static String generatePath(LinkedList<Character> path) {
        char[] str = new char[path.size()];//创建与路径长度相同的字符数组
        int index = 0;
        for (Character cha : path) {//将路径上的字符复制到数组中
            str[index++] = cha;
        }
        return String.valueOf(str);//将他转化为字符串并返回
    }

    public static List<String> findWords(char[][] board, String[] words) {
        TrieNode head = new TrieNode(); // 创建前缀树的根节点
        HashSet<String> set = new HashSet<>();//哈希Set用来去重
        for (String word : words) {
            if (!set.contains(word)) {//只插入不重复的单词
                fillWord(head, word);
                set.add(word);
            }
        }

        List<String> ans = new ArrayList<>();//存储找到的单词
        LinkedList<Character> path = new LinkedList<>();//记录当前的路径
        //遍历棋盘的每个位置作为起点
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[0].length; col++) {
                process(board, row, col, path, head, ans);
            }
        }
        return ans;
    }
    //深度优先搜索
    public static int process(char[][] board, int row, int col, LinkedList<Character> path, TrieNode cur, List<String> res) {
        char cha = board[row][col];//先获取当前位置的字符
        if (cha == 0) {//如果当前的位置已经被访问过了，则返回0
            return 0;
        }
        int index = cha = 'a';
        // 如果前缀树中没有对应的节点，或该节点已无单词经过，返回0
        if (cur.nexts[index] == null || cur.nexts[index].pass == 0) {
            return 0;
        }
        cur = cur.nexts[index];//移动到对应的前缀树节点
        path.addLast(cha);//将当前的字符加入到路径中
        int fix = 0;//记录找到的单词的数量
        if (cur.end) {
            res.add(generatePath(path));
            cur.end = false;
            fix++;
        }
        board[row][col] = 0;//标记为已访问
        if (row > 0) {
            fix += process(board, row - 1, col, path, cur, res);
        }
        if (row < board.length - 1) {
            fix += process(board, row + 1, col, path, cur, res);
        }
        if (col > 0) {
            fix += process(board, row, col - 1, path, cur, res);
        }
        if (col < board[0].length - 1) {
            fix += process(board, row, col + 1, path, cur, res);
        }
        board[row][col] = cha;//恢复当前位置的字符（回溯）
        path.pollLast();//从路径中将当前位置的字符进行移除（回溯）
        cur.pass -= fix;//将已经找到的单词数进行减去
        return fix;//将找到的单词数进行返回
    }
}
