package Class26;

import java.util.*;

//按字典wordList 完成从单词 beginWord 到单词 endWord 转化，一个表示此过程的 转换序列 是形式上像 beginWord -> s1 -> s2 -> ... -> sk 这样的单词序列，并满足：
//每对相邻的单词之间仅有单个字母不同。
//转换过程中的每个单词 si（1 <= i <= k）必须是字典wordList 中的单词。注意，beginWord 不必是字典 wordList 中的单词
//给你两个单词 beginWord 和 endWord ，以及一个字典 wordList
//请你找出并返回所有从 beginWord 到 endWord 的 最短转换序列 ，如果不存在这样的转换序列，返回一个空列表
//每个序列都应该以单词列表 [beginWord, s1, s2, ..., sk] 的形式返回
//Leetcode题目：https://leetcode.com/problems/word-ladder-ii/
public class Code04_WorldLadderII {
    //转换规则：每次只能改变一个字符，且中间单词必须在给定的单词列表中

    //构建邻居表 - > 计算双向距离 - > 搜索所有最短路径
    public static List<List<String>> findLadders(String start, String end, List<String> list) {
        list.add(start);//先将起始的单词加入到单词的列表
        // 1. 构建每个单词的"邻居表"（所有可通过改变一个字符得到的单词）
        HashMap<String, List<String>> nexts = getNexts(list);
        // 2. 计算从start到所有单词的最短距离（BFS）
        HashMap<String, Integer> fromDistances = getDistances(start, nexts);
        List<List<String>> res = new ArrayList<>();
        if (!fromDistances.containsKey(end)) {
            return res;
        }
        // 3. 计算从end到所有单词的最短距离（反向BFS）
        HashMap<String, Integer> toDistances = getDistances(end, nexts);
        // 4. 深度优先搜索所有最短路径
        LinkedList<String> pathList = new LinkedList<>();
        getShortestPaths(start, end, nexts, fromDistances, toDistances, pathList, res);
        return res;
    }

    public static HashMap<String, List<String>> getNexts(List<String> words) {
        HashSet<String> dict = new HashSet<>(words);// 转为Set，方便快速判断是否存在
        HashMap<String, List<String>> nexts = new HashMap<>();
        for (int i = 0; i < words.size(); i++) {
            nexts.put(words.get(i), getNext(words.get(i), dict));
        }
        return nexts;
    }

    //生成每个单词的邻居
    public static List<String> getNext(String word, HashSet<String> dict) {
        ArrayList<String> res = new ArrayList<String>();//存储所有的邻居单词
        char[] chs = word.toCharArray();
        //最外层尝试所有可能的替换字符
        for (char cur = 'a'; cur <= 'z'; cur++) {
            //尝试替换单词中的每一个位置
            for (int i = 0; i < chs.length; i++) {
                if (chs[i] != cur) {//自己替换自己没有意义
                    char tmp = chs[i];
                    chs[i] = cur;
                    if (dict.contains(String.valueOf(chs))) {
                        res.add(String.valueOf(chs));
                    }
                    chs[i] = tmp;// 恢复原字符（回溯操作，不影响下一次替换）
                }
            }
        }
        return res;
    }

    public static HashMap<String, Integer> getDistances(String start, HashMap<String, List<String>> nexts) {
        HashMap<String, Integer> distances = new HashMap<>();//存储单词到start的最短距离
        distances.put(start, 0);//起始的距离为0
        Queue<String> queue = new LinkedList<>();//用于BFS
        queue.add(start);
        HashSet<String> set = new HashSet<>();// 标记已访问的单词（避免重复计算）
        set.add(start);
        while (!queue.isEmpty()) {
            String cur = queue.poll();
            for (String next : nexts.get(cur)) {
                if (!set.contains(next)) {
                    distances.put(next, distances.get(cur) + 1);
                    queue.add(next);
                    set.add(next);
                }
            }
        }
        return distances;
    }

    //cur:当前正在处理的单词
    //to:目标单词
    //nexts：每个单词的“邻接表”
    //fromDistances；正向距离表
    //toDistances：反向距离表
    //path：当前路径容器
    //res：结果集合
    public static void getShortestPaths(String cur, String to, HashMap<String, List<String>> nexts, HashMap<String, Integer> fromDistances, HashMap<String, Integer> toDistances, LinkedList<String> path, List<List<String>> res) {
        path.add(cur);
        if (to.equals(cur)) {
            res.add(new LinkedList<String>(path));//复制当前的路径到结果中
        } else {
            //遍历当前单词的所有邻居，筛选符合最短路径条件的邻居
            for (String next : nexts.get(cur)) {
                if (fromDistances.get(next) == fromDistances.get(cur) + 1 && toDistances.get(next) == toDistances.get(cur) - 1) {
                    getShortestPaths(next, to, nexts, fromDistances, toDistances, path, res);
                }
            }
        }
        path.pollLast();  // 回溯：移除当前单词（移除的是路径中的最后一个元素），尝试其他路径
    }
}
