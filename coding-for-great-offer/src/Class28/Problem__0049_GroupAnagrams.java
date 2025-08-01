package Class28;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

//给定一个字符串数组，将字母异位词组合在一起。可以按任意顺序返回结果列表。
//字母异位词指字母相同，但排列不同的字符串。
//示例 1:
//输入: strs = ["eat", "tea", "tan", "ate", "nat", "bat"]
//输出: [["bat"],["nat","tan"],["ate","eat","tea"]]
//示例 2:
//输入: strs = [""]
//输出: [[""]]
//示例 3:
//输入: strs = ["a"]
//输出: [["a"]]
//Leetcode题目：https://leetcode.com/problems/group-anagrams/
public class Problem__0049_GroupAnagrams {
    //接受字数数组strs,返回一个包含分组结果的二维列表（每个子列表包含一组字母异位词
    //本方法时间复杂度为O(n * k)
    public static List<List<String>> groupAnagrams1(String[] strs) {
        //键（Key）：用于标识一组字母异位词的特征字符串（同一组异位词会生成相同的 Key）
        //值（Value）：存储该组所有字母异位词的列表
        HashMap<String, List<String>> map = new HashMap<String, List<String>>();
        for (String str : strs) {//遍历输入的每一个字符串
            int[] record = new int[26];//记录字符串中每一个字母出现的次数
            for (char cha : str.toCharArray()) {
                record[cha - 'a']++;//先将字符串转化为字符数组，在计算出对应的索引，最后将对应索引的字母次数+1
            }
            //将字母出现次数数组转化为特征字符串key
            StringBuilder builder = new StringBuilder();
            for (int value : record) {
                builder.append(String.valueOf(value)).append("_");//添加一个下划线，防止出现歧义
            }
            String key = builder.toString();//将StringBuilder转化为字符串，作为当前字符串的特征key
            if (!map.containsKey(key)) {
                map.put(key, new ArrayList<>());//不存在则创建一个新的空列表
            }
            map.get(key).add(str);
        }
        List<List<String>> res = new ArrayList<List<String>>();
        for (List<String> list : map.values()) {
            res.add(list);
        }
        return res;
    }

    public static List<List<String>> groupAnagrams2(String[] strs) {
        HashMap<String, List<String>> map = new HashMap<String, List<String>>();
        for (String str : strs) {
            char[] chs = str.toCharArray();
            Arrays.sort(chs);
            String key = String.valueOf(chs);//将排序后的字符数组转化为字符串
            if (!map.containsKey(key)) {
                map.put(key, new ArrayList<String>());
            }
            map.get(key).add(str);
        }
        List<List<String>> res = new ArrayList<List<String>>();
        for (List<String> list : map.values()) {
            res.add(list);
        }
        return res;
    }
}
