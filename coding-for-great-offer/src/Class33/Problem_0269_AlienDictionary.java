package Class33;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

//出版社印发了一个名单，名单上的每一行都是一个字符串，字符串都是小写字母，但是字符串出现的先后顺序和日常的字典序不一样
//现在怀疑出版社内部对于字符有自己的字典序规则，请根据字符串之间出现的顺序，返回可能存在的、出版社内部的字符顺序
//如果从名单来看不存在这样的字符顺序，返回空字符串
//Leetcode题目 : https://leetcode.com/problems/alien-dictionary/
//时间复杂度为O(C),空间复杂度为O(C)
public class Problem_0269_AlienDictionary {
    //接收字符串数组，最后返回字符串的顺序
    public static String alienOrder(String[] words) {
        if (words == null || words.length == 0) {
            return "";
        }
        int N = words.length;
        //有多少个字符在当前字符的前面
        //Character用于封装基本数据类型char
        HashMap<Character, Integer> indegree = new HashMap<>();
        //将所有字符的入度初始化为0
        for (int i = 0; i < N; i++) {
            for (char c : words[i].toCharArray()) {
                indegree.put(c, 0);
            }
        }
        //记录字符之间的先后关系
        HashMap<Character, HashSet<Character>> graph = new HashMap<>();
        //遍历相邻的字符串来建立字符串之间的先后关系
        for (int i = 0; i < N - 1; i++) {
            char[] cur = words[i].toCharArray();//当前字符串
            char[] nex = words[i + 1].toCharArray();//下一个字符串
            int len = Math.min(cur.length, nex.length);//获取短字符的长度
            int j = 0;
            for (; j < len; j++) {
                if (cur[j] != nex[j]) {
                    if (!graph.containsKey(cur[j])) {
                        graph.put(cur[j], new HashSet<>());
                    }
                    //记录两个字符的相邻关系
                    if (!graph.get(cur[j]).contains(nex[j])) {
                        graph.get(cur[j]).add(nex[j]);
                        indegree.put(nex[j], indegree.get(nex[j]) + 1);
                    }
                    break;//找到一组关系后直接退出
                }
            }
            //特殊情况：如果前一个字符串是后一个的前缀且更长，则是无效的
            //例如 ["apple", "app"] 是无效的
            if (j < cur.length && j == nex.length) {
                return "";
            }
        }
        StringBuilder ans = new StringBuilder();
        Queue<Character> q = new LinkedList<>();//用于拓扑排序
        //先将所有入度为0的字符加上队列
        for (Character key : indegree.keySet()) {
            if (indegree.get(key) == 0) {
                q.offer(key);
            }
        }
        //执行拓扑排序
        while (!q.isEmpty()) {
            char cur = q.poll();
            ans.append(cur);
            if (graph.containsKey(cur)) {
                for (char next : graph.get(cur)) {
                    indegree.put(next, indegree.get(next) - 1);
                    if (indegree.get(next) == 0) {
                        q.offer(next);
                    }
                }
            }
        }
        return ans.length() == indegree.size() ? ans.toString() : "";
    }
}
