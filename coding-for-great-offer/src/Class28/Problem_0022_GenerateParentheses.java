package Class28;

import java.util.ArrayList;
import java.util.List;

//数字 n代表生成括号的对数，请你设计一个函数，用于能够生成所有可能的并且有效的括号组合。
//示例 1：
//输入：n = 3
//输出：["((()))","(()())","(())()","()(())","()()()"]
//示例 2：
//输入：n = 1
//输出：["()"]
//Leetcode题目：https://leetcode.com/problems/generate-parentheses/
public class Problem_0022_GenerateParentheses {
    //生成n对有效括号的所有组合
    public static List<String> generateParenthesis(int n) {
        //创建字符数组来存当前括号组合，长度为2n
        char[] path = new char[n << 1];
        List<String> ans = new ArrayList<>();//创建集合来存所有有效组合的结果
        process(path, 0, 0, n, ans);//调递归来生成所有的有效组合
        return ans;
    }

    //path:当前正在构建的括号组合
    //index:当前需要填充的位置索引
    //leftMinusRight:左括号数量减去右括号数量的差值（确保有效性）
    //leftRest:还可以使用的左括号数量
    //ans:收集结果的集合
    public static void process(char[] path, int index, int leftMinusRight, int leftRest, List<String> ans) {
        if (index == path.length) {
            ans.add(String.valueOf(path));
        } else {
            if (leftRest > 0) {
                path[index] = '(';
                process(path, index + 1, leftMinusRight + 1, leftRest - 1, ans);
            }
            if (leftMinusRight > 0) {
                path[index] = ')';
                process(path, index + 1, leftMinusRight - 1, leftRest, ans);
            }
        }
    }

    public static List<String> generateParenthesis2(int n) {
        char[] path = new char[n << 1];
        List<String> ans = new ArrayList<>();
        process2(path, 0, ans);
        return ans;
    }

    public static void process2(char[] path, int index, List<String> ans) {
        if (index == path.length) {
            if (isValid(path)) {
                ans.add(String.valueOf(path));
            }
        } else {
            path[index] = '(';//尝试在当前位置放置左括号
            process2(path, index + 1, ans);
            path[index] = ')';
            process2(path, index + 1, ans);
        }
    }

    public static boolean isValid(char[] path) {
        int count = 0;
        for (char cha : path) {
            if (cha == '(') {
                count++;
            } else {
                count--;
            }
            if (count < 0) {
                return false;
            }
        }
        return count == 0;
    }
}
