package class_2022_08_2;

import java.util.Stack;

//给定一个逆波兰式
//转化成正确的中序表达式
//要求只有必要加括号的地方才加括号
public class Code03_ReversePolishNotation {

    public static int getAns(String rpn) {
        if (rpn == null || rpn.equals("")) {
            return 0;
        }
        //将逆波兰式按空格分割成token数组
        String[] parts = rpn.split(" ");
        //使用栈存储操作数
        Stack<Integer> stack = new Stack<>();
        for (String part : parts) {
            if (part.equals("+") || part.equals("-") || part.equals("*") || part.equals("/")) {
                int right = stack.pop();
                int left = stack.pop();
                int ans = 0;
                if (part.equals("+")) {
                    ans = left + right;
                } else if (part.equals("-")) {
                    ans = left - right;
                } else if (part.equals("*")) {
                    ans = left * right;
                } else {
                    ans = left / right;
                }
                stack.push(ans);
            } else {
                stack.push(Integer.valueOf(part));
            }
        }
        return stack.pop();
    }

    //操作数优先级的枚举
    enum Operation {
        SingleNumber, AddOrMinus, MultiplyOrDivide;
    }

    public static String convert(String rpn) {
        if (rpn == null || rpn.equals("")) {
            return rpn;
        }
        String[] parts = rpn.split(" ");
        //表达式片段
        Stack<String> stack1 = new Stack<>();
        //对应片段的优先级
        Stack<Operation> stack2 = new Stack<>();
        for (String cur : parts) {
            if (cur.equals("+") || cur.equals("-")) {
                // 弹出右操作数的表达式片段和其优先级
                String b = stack1.pop();
                // 弹出左操作数的表达式片段和其优先级
                String a = stack1.pop();
                // 移除左右操作数对应的优先级记录（已用不到）
                stack2.pop();
                stack2.pop();
                stack1.push(a + cur + b);
                //记录当前拼接的表达式的优先级
                stack2.push(Operation.AddOrMinus);
            } else if (cur.equals("*") || cur.equals("/")) {
                String b = stack1.pop();
                String a = stack1.pop();
                Operation bOp = stack2.pop();
                Operation aOp = stack2.pop();
                String left = aOp == Operation.AddOrMinus ? ("(" + a + ")") : (a);
                String right = bOp == Operation.AddOrMinus ? ("(" + b + ")") : (b);
                stack1.push(left + cur + right);
                stack2.push(Operation.MultiplyOrDivide);
            } else {
                stack1.push(cur);
                stack2.push(Operation.SingleNumber);
            }
        }
        return stack1.pop();
    }

    public static void main(String[] args) {
        // 3*(-5+13)+6/(2-3+2)-4*5*3
        String rpn = "3 -5 13 + * 6 2 3 - 2 + / + 4 5 3 * * -";
        System.out.println(getAns(rpn));
        System.out.println(convert(rpn));
    }
}
