package Class28;

//给定一个只包括 '('，')'，'{'，'}'，'['，']'的字符串 s ，判断字符串是否有效。
//有效字符串需满足：
//左括号必须用相同类型的右括号闭合。
//左括号必须以正确的顺序闭合。
//Leetcode题目：https://leetcode.com/problems/valid-parentheses/
public class Problem_0020_ValidParentheses {

    public static boolean isValid(String s) {
        if (s == null || s.length() == 0) {
            return true;
        }
        char[] str = s.toCharArray();
        int N = str.length;
        //创建一个字符数组作为栈，用于存储对应的右括号
        //栈的最大容量为字符串的长度（最坏的情况是全部都是左括号）
        char[] stack = new char[N];
        int size = 0; //栈的大小指针（size=0表示栈为空，size-1为栈顶索引）
        for (int i = 0; i < N; i++) {
            char cha = str[i];
            //出现左括号时，往栈中压入右括号，目的是为了简化计算
            if (cha == '(' || cha == '[' || cha == '{') {
                stack[size++] = cha == '(' ? ')' : (cha == '[' ? ']' : '}');//用三目运算符来实现映射
            } else {
                if (size == 0) {
                    return false;
                }
                char last = stack[--size]; //弹出栈顶元素（即当前右括号应匹配的左括号对应的右括号）
                if (cha != last) {
                    return false;
                }
            }
        }
        return size == 0;
    }
}
