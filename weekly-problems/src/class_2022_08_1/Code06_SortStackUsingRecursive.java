package class_2022_08_1;

import java.util.Stack;

//栈只提供push、pop、isEmpty三个方法
//请完成无序栈的排序，要求排完序之后，从栈顶到栈底从小到大
//只能使用栈提供的push、pop、isEmpty三个方法、以及递归函数
//除此之外不能使用任何的容器，任何容器都不许，连数组也不行
//也不能自己定义任何结构体
//就是只用：
//1) 栈提供的push、pop、isEmpty三个方法
//2) 简单返回值的递归函数
public class Code06_SortStackUsingRecursive {

    public static void sortStack(Stack<Integer> stack) {
        int deep = deep(stack);//获取栈的深度
        while (deep > 0) {
            //当前深度范围内的最大值
            int max = max(stack, deep);
            //最大值在当前深度范围内出现的次数
            int k = times(stack, max, deep);
            //将最大值压到当前深度的底部
            down(stack, deep, max, k);
            deep -= k;
        }
    }
    //计算栈的深度
    public static int deep(Stack<Integer> stack) {
        if (stack.isEmpty()) {
            return 0;
        }
        int num = stack.pop();
        int deep = deep(stack) + 1;
        stack.push(num);
        return deep;
    }
    //查找当前范围内的最大值
    public static int max(Stack<Integer> stack, int deep) {
        if (deep == 0) {
            return Integer.MIN_VALUE;
        }
        int num = stack.pop();
        int restMax = max(stack, deep - 1);
        int max = Math.max(num, restMax);
        stack.push(num);
        return max;
    }
    //统计最大值在当前深度范围内出现的次数
    public static int times(Stack<Integer> stack, int max, int deep) {
        if (deep == 0) {
            return 0;
        }
        int num = stack.pop();
        int restTimes = times(stack, max, deep - 1);
        int times = restTimes + (num == max ? 1 : 0);
        stack.push(num);
        return times;
    }
    //将最大值压到当前深度的底部
    public static void down(Stack<Integer> stack, int deep, int max, int k) {
        if (deep == 0) {
            for (int i = 0; i < k; i++) {
                stack.push(max);
            }
        } else {
            int num = stack.pop();
            down(stack, deep - 1, max, k);
            if (num != max) {
                stack.push(num);
            }
        }
    }
}
