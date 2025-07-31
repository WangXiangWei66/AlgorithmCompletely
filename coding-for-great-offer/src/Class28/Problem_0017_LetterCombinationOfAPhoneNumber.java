package Class28;

import java.util.ArrayList;
import java.util.List;

//给定一个仅包含数字2-9的字符串，返回所有它能表示的字母组合。答案可以按任意顺序返回。
//给出数字到字母的映射如下（与电话按键相同）。注意 1 不对应任何字母。
//按键2对应：'a', 'b', 'c'
//按键3对应：'d', 'e', 'f'
//按键4对应：'g', 'h', 'i'
//按键5对应：'j', 'k', 'l'
//按键6对应：'m', 'n', 'o'
//按键7对应：'p', 'q', 'r', 's'
//按键8对应：'t', 'u', 'v'
//按键9对应：'w', 'x', 'y', 'z'
//示例 1：
//输入：digits = "23"
//输出：["ad","ae","af","bd","be","bf","cd","ce","cf"]
//示例 2：
//输入：digits = ""
//输出：[]
//示例 3：
//输入：digits = "2"
//输出：["a","b","c"]
//Leetcode题目：https://leetcode.com/problems/letter-combinations-of-a-phone-number/
public class Problem_0017_LetterCombinationOfAPhoneNumber {

    public static char[][] phone = {
            {'a', 'b', 'c'},
            {'d', 'e', 'f'},
            {'g', 'h', 'i'},
            {'j', 'k', 'l'},
            {'m', 'n', 'o'},
            {'p', 'q', 'r', 's'},
            {'t', 'u', 'v'},
            {'w', 'x', 'y', 'z'},
    };

    //返回值是所有可能的字母组合
    public static List<String> letterCombinations(String digits) {
        List<String> ans = new ArrayList<>();//首先创建结果列表
        if (digits == null || digits.length() == 0) {
            return ans;
        }
        char[] str = digits.toCharArray();//将输入的字符串转化为结果数组
        char[] path = new char[str.length]; //创建路径数组，用于存储当前组合
        process(str, 0, path, ans);
        return ans;
    }

    //str：输入的数字字符数组
    //index:当前处理的位置
    //path：存储当前组合的路径
    //ans:结果列表
    public static void process(char[] str, int index, char[] path, List<String> ans) {
        if (index == str.length) {
            ans.add(String.valueOf(path)); //将当前路径转为字符串，添加到结果列表
        } else {
            //根据当前当前数字对应的字母数组
            //str[index] - '2'：将字符数字转为数组索引（如'2'-'2'=0，'3'-'2'=1）
            char[] cands = phone[str[index] - '2'];
            for (char cur : cands) {
                path[index] = cur;//将当前的字母放入路径的对应位置
                process(str, index + 1, path, ans);//递归去处理下一个数字
            }
        }
    }
}
