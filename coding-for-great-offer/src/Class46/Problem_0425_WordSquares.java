package Class46;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

//给定n个字符串，并且每个字符串长度一定是n，请组成单词方阵，比如：
//给定4个字符串，长度都是4，["ball","area","lead","lady"]
//可以组成如下的方阵：
//b a l l
//a r e a
//l e a d
//l a d y
//什么叫单词方阵？如上的方阵可以看到，第1行和第1列都是"ball"，第2行和第2列都是"area"，第3行和第3列都是"lead"，第4行和第4列都是"lady"
//所以如果有N个单词，单词方阵是指，一个N*N的二维矩阵，并且i行和i列都是某个单词，不要求全部N个单词都在这个方阵里。
//请返回所有可能的单词方阵。
//示例：
//输入: words = ["abat","baba","atan","atal"]
//输出: [["baba","abat","baba","atal"],["baba","abat","baba","atan"]]
//解释：
//可以看到输出里，有两个链表，代表两个单词方阵
//第一个如下：
//b a b a
//a b a t
//b a b a
//a t a l
//这个方阵里没有atan，因为不要求全部单词都在方阵里
//第二个如下：
//b a b a
//a b a t
//b a b a
//a t a n
//这个方阵里没有atal，因为不要求全部单词都在方阵里
//leetcode题目：https://leetcode.com/problems/word-squares/
public class Problem_0425_WordSquares {
    //构建前缀映射表
    public static List<List<String>> wordSquares(String[] words) {
        int n = words[0].length();
        //构建【前缀+单词列表】的映射表，key是前缀，value是所有以该前缀开头的单词
        HashMap<String, List<String>> map = new HashMap<>();
        for (String word : words) {
            for (int end = 0; end <= n; end++) {
                String preFix = word.substring(0, end);//截取前缀
                if (!map.containsKey(preFix)) {
                    map.put(preFix, new ArrayList<>());
                }
                map.get(preFix).add(word);
            }
        }
        //初始化结果集和回溯操作
        List<List<String>> ans = new ArrayList<>();
        process(0, n, map, new LinkedList<>(), ans);
        return ans;
    }

    //i：当前要构建的行号
    //path当前正在构建的方阵行
    public static void process(int i, int n, HashMap<String, List<String>> map, LinkedList<String> path, List<List<String>> ans) {
        if (i == n) {
            ans.add(new ArrayList<>(path));//新建一个列表
        } else {
            StringBuilder builder = new StringBuilder();
            for (String pre : path) {
                builder.append(pre.charAt(i));
            }
            String preFix = builder.toString();
            if (map.containsKey(preFix)) {
                for (String next : map.get(preFix)) {
                    path.addLast(next);
                    process(i + 1, n, map, path, ans);
                    path.pollLast();
                }
            }
        }
    }
}
