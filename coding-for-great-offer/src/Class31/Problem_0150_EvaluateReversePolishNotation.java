package Class31;

import java.util.Stack;

//根据 逆波兰表示法，求表达式的值。
//有效的算符包括+、-、*、/。每个运算对象可以是整数，也可以是另一个逆波兰表达式。
//说明：整数除法只保留整数部分。给定逆波兰表达式总是有效的。换句话说，表达式总会得出有效数值且不存在除数为 0 的情况。
//示例1：
//输入：tokens = ["2","1","+","3","*"]
//输出：9
//解释：该算式转化为常见的中缀算术表达式为：((2 + 1) * 3) = 9
//示例2：
//输入：tokens = ["4","13","5","/","+"]
//输出：6
//解释：该算式转化为常见的中缀算术表达式为：(4 + (13 / 5)) = 6
//示例3：
//输入：tokens = ["10","6","9","3","+","-11","*","/","*","17","+","5","+"]
//输出：22
//解释：
//该算式转化为常见的中缀算术表达式为：
//((10 * (6 / ((9 + 3) * -11))) + 17) + 5
//= ((10 * (6 / (12 * -11))) + 17) + 5
//= ((10 * (6 / -132)) + 17) + 5
//= ((10 * 0) + 17) + 5
//= (0 + 17) + 5
//= 17 + 5
//= 22
//Leetcode题目 : https://leetcode.com/problems/evaluate-reverse-polish-notation/
public class Problem_0150_EvaluateReversePolishNotation {
    //时间复杂度和空间复杂度都为O(n)
    public static int evalRPN(String[] tokens) {
        //用栈来存储操作数
        Stack<Integer> stack = new Stack<>();
        for (String str : tokens) {
            if (str.equals("+") || str.equals("-") || str.equals("*") || str.equals("/")) {
                //如果是运算符，就进行计算
                compute(stack, str);
            } else {
                //数字，转化为整数压入栈中
                stack.push(Integer.valueOf(str));
            }
        }
        return stack.peek();
    }

    public static void compute(Stack<Integer> stack, String op) {
        //弹出栈顶的两个元素（注意顺序：后入栈的是第二个操作数）
        int num2 = stack.pop();
        int num1 = stack.pop();
        int ans = 0;
        switch (op) {
            case "+":
                ans = num1 + num2;
                break;
            case "-":
                ans = num1 - num2;
                break;
            case "*":
                ans = num1 * num2;
                break;
            case "/":
                ans = num1 / num2;
                break;
        }
        stack.push(ans);
    }
}
