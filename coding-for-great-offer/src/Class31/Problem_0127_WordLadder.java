package Class31;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

//字典 wordList 中从单词 beginWord 和 endWord 的 转换序列 是一个按下述规格形成的序列：
//序列中第一个单词是 beginWord
//序列中最后一个单词是 endWord
//每次转换只能改变一个字母。
//转换过程中的中间单词必须是字典 wordList 中的单词
//给你两个单词 beginWord 和 endWord 和一个字典 wordList ，找到从 beginWord 到 endWord 的 最短转换序列 中的 单词数目 。如果不存在这样的转换序列，返回 0。
//Leetcode题目 : https://leetcode.com/problems/word-ladder/
public class Problem_0127_WordLadder {

    public static int ladderLength1(String start, String to, List<String> list) {
        //将起始单词加入单词列表
        list.add(start);
        //构建所有单词的邻接表，（记录每个单词可直接转换到的单词）
        HashMap<String, ArrayList<String>> nexts = getNexts(list);
        //记录每个单词到起始单词的最短距离
        HashMap<String, Integer> distanceMap = new HashMap<>();
        distanceMap.put(start, 1);
        //用于记录已经访问过的单词
        HashSet<String> set = new HashSet<>();
        set.add(start);
        //BFS：用于按层遍历单词
        Queue<String> queue = new LinkedList<>();
        queue.add(start);
        while (!queue.isEmpty()) {
            String cur = queue.poll();//从队列种取出一个单词
            Integer distance = distanceMap.get(cur);//当前单词到起始单词的距离
            for (String next : nexts.get(cur)) {
                if (next.equals(to)) {
                    return distance + 1;//邻接单词是目标单词
                }
                if (!set.contains(next)) {
                    set.add(next);//先将他标记为已访问
                    queue.add(next);
                    distanceMap.put(next, distance + 1);
                }
            }
        }
        return 0;
    }

    public static HashMap<String, ArrayList<String>> getNexts(List<String> words) {
        HashSet<String> dict = new HashSet<>(words);
        HashMap<String, ArrayList<String>> nexts = new HashMap<>();
        //为每个单词生成对应的邻接列表
        for (int i = 0; i < words.size(); i++) {
            nexts.put(words.get(i), getNext(words.get(i), dict));
        }
        return nexts;
    }

    public static ArrayList<String> getNext(String word, HashSet<String> dict) {
        ArrayList<String> res = new ArrayList<String>();
        char[] chs = word.toCharArray();
        for (int i = 0; i < chs.length; i++) {
            for (char cur = 'a'; cur <= 'z'; cur++) {
                if (chs[i] != cur) {
                    char tmp = chs[i];//暂存原字符，用于后续的恢复
                    chs[i] = cur;
                    if (dict.contains(String.valueOf(chs))) {
                        res.add(String.valueOf(chs));
                    }
                    chs[i] = tmp;
                }
            }
        }
        return res;
    }

    public static int ladderLength2(String beginword, String endWord, List<String> wodList) {
        //将单词列表转化为HashSet，便于快速查询
        HashSet<String> dict = new HashSet<>(wodList);
        if (!dict.contains(endWord)) {
            return 0;
        }
        HashSet<String> startSet = new HashSet<>(); //从起始单词出发的集合
        HashSet<String> endSet = new HashSet<>();//从目标单词出发的集合
        HashSet<String> visit = new HashSet<>();//记录已访问的单词（避免重复）
        startSet.add(beginword);
        endSet.add(endWord);
        //从两边开始往中间搜索，len为当前路径的长度
        for (int len = 2; !startSet.isEmpty(); len++) {
            HashSet<String> nextSet = new HashSet<>();//存储起始集合的下一个单词
            for (String w : startSet) {
                for (int j = 0; j < w.length(); j++) {
                    char[] ch = w.toCharArray();
                    for (char c = 'a'; c <= 'z'; c++) {
                        if (c != w.charAt(j)) {
                            ch[j] = c;
                            String next = String.valueOf(ch);
                            //原字符在目标集合中，说明两边相遇
                            if (endSet.contains(next)) {
                                return len;
                            }
                            if (dict.contains(next) && !visit.contains(next)) {
                                nextSet.add(next);
                                visit.add(next);
                            }
                        }
                    }
                }
            }
            //选择规模更小的集合作为下一轮的起始集合
            startSet = (nextSet.size() < endSet.size()) ? nextSet : endSet;
            endSet = (startSet == nextSet) ? endSet : nextSet;
        }
        return 0;
    }
}
