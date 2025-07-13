package Class17;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
//Manacher算法用O（N）的实践计算所有位置的回文半径
//给定一个字符串数组arr，里面都是互不相同的单词
//找出所有不同的索引对(i, j)，使得列表中的两个单词，words[i] + words[j]，可拼接成回文串。
//Leetcode题目：https://leetcode.com/problems/palindrome-pairs/
public class Code03_PalindromePair1 {
    //找出所有不同的索引对，使得word[i]+word[j]是回文串
    public static List<List<Integer>> palindromePairs(String[] words) {
        //将每个单词及其索引，存入wordSet
        HashMap<String, Integer> wordSet = new HashMap<>();
        for (int i = 0; i < words.length; i++) {
            wordSet.put(words[i], i);
        }
        //用于存储所有符合条件的索引对
        List<List<Integer>> res = new ArrayList<>();
        for (int i = 0; i < words.length; i++) {
            //addAll：将findAll返回的当前单词对应的所有回文串，批量添加到全局结果res中
            res.addAll(findAll(words[i], i, wordSet));//调用 findAll方法寻找所有能与它组成回文对的索引 j，并将结果添加到 res 中。
        }
        return res;
    }
    //word：当前处理的单词（作为候选前缀或后缀）
    //index：word在words中的索引
    //最终返回与word组成回文对的索引对列表
    public static List<List<Integer>> findAll(String word, int index, HashMap<String, Integer> words) {
        //存储当前word对应的所有回文串
        List<List<Integer>> res = new ArrayList<>();
        //得到word的反转字符串
        String reverse = reverse(word);
        Integer rest = words.get("");//在word中差空字符串的索引，处理空字符串与回文单词组合可形成回文串的情况
        //if同时避免了自己与自己组合，同时自己本身也是一个回文串
        if (rest != null && rest != index && word.equals(reverse)) {
            //asList：在java中用于将一组参数转化为一个固定大小的列表
            res.add(Arrays.asList(rest, index));
            res.add(Arrays.asList(index, rest));
        }
        //rs 中每个元素 rs[i] 表示以预处理后字符串的第 i 位为中心的最长回文半径，用于快速判断 word 的前缀或后缀是否为回文。
        int[] rs = manacherss(word);//计算word经预处理后的【回文半径数组】res
        int mid = rs.length >> 1;//word预处理后字符串的中心位置
        //遍历预处理后字符串【中心左侧】位置
        //for循环的目的是为了寻找回文前缀
        for (int i = 1; i < mid; i++) {//跳过0是因为0位置是#
            if (i - rs[i] == -1) {//表示从word靠头到对应位置的字串是回文
                rest = words.get(reverse.substring(0, mid - i));
                if (rest != null && rest != index) {
                    res.add(Arrays.asList(rest, index));
                }
            }
        }
        //下面开始寻找回文后缀
        for (int i = mid + 1; i < rs.length; i++) {
            if (i + rs[i] == rs.length) {//从word对应位置到结尾的字串是回文
                rest = words.get(reverse.substring((mid << 1) - i));
                if (rest != null && rest != index) {
                    res.add(Arrays.asList(index, rest));
                }
            }
        }
        return res;
    }
    //计算回文半径：从中心到最右侧（包括自身）的距离
    public static int[] manacherss(String word) {
        char[] mchs = manachercs(word);//获取预处理后的字符数组
        int[] rs = new int[mchs.length];//存储每个位置的最长回文半径
        int center = -1;//当前已知的最右回文串的中心
        int pr = -1;//最右回文边界
        for (int i = 0; i != mchs.length; i++) {
            //如果当前位置在最后回文边界内
            rs[i] = pr > i ? Math.min(rs[(center << 1) - i], pr - i) : 1;//利用对称性初始化回文半径
            //扩展回文半径：从初始半径开始，尝试向左右两侧扩展，检查是否仍为回文。
            while (i + rs[i] < mchs.length && i - rs[i] > -1) {//如果左右边界都没有超出数组
                if (mchs[i + rs[i]] != mchs[i - rs[i]]) {//判断左右对称的字符是否相等
                    break;
                }
                rs[i]++;
            }
            //更新最右回文边界和中心
            if (i + rs[i] > pr) {
                pr = i + rs[i];
                center = i;
            }
        }
        return rs;//最后返回完整的回文半径数组rs
    }
    //字符串预处理：插入特殊字符
    public static char[] manachercs(String word) {
        char[] chs = word.toCharArray();
        char[] mchs = new char[chs.length * 2 + 1];
        int index = 0;//用于遍历原字符串数组
        for (int i = 0; i != mchs.length; i++) {
            mchs[i] = (i & 1) == 0 ? '#' : chs[index++];
        }
        return mchs;
    }
    //反转字符串
    public static String reverse(String str) {
        char[] chs = str.toCharArray();
        int l = 0;
        int r = chs.length - 1;
        while (l < r) {
            char tmp = chs[l];
            chs[l++] = chs[r];
            chs[r--] = tmp;
        }
        return String.valueOf(chs);
    }
}
