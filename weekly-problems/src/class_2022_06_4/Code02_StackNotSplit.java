package class_2022_06_4;

import java.util.ArrayList;
import java.util.Stack;

//来自微软
//请设计一种叫做“栈的管理器”的结构，实现如下6个功能
//1) void createNewStack() : 可以在该结构中生成一个栈结构，编号从0开始
//2) void push(int num, int stackIndex) : 将编号为stackIndex的栈里，压入num
//3) int pop(int stackIndex) : 从编号为stackIndex的栈里，弹出栈顶返回
//4) int peek(int stackIndex) ：从编号为stackIndex的栈里，返回栈顶但是不弹出
//5) boolean isEmpty(int statckIndex)：返回编号为stackIndex的栈是否为空
//6) int stackSize() : 返回一共生成了多少个栈
//   要求：不管用户调用多少次上面的方法，只使用有限几个动态数组(常数个)，完成代码实现
public class Code02_StackNotSplit {

    public static class Stack1 {
        //Arraylist中的每一个元素代表一个独立的栈
        public ArrayList<Stack<Integer>> stacks;

        public Stack1() {
            stacks = new ArrayList<>();
        }

        public int stackSize() {
            return stacks.size();
        }

        public void createNewStack() {
            stacks.add(new Stack<>());
        }

        public void push(int num, int stackIndex) {
            stacks.get(stackIndex).push(num);
        }

        public int pop(int stackIndex) {
            return stacks.get(stackIndex).pop();
        }

        public boolean isEmpty(int stackIndex) {
            return stacks.get(stackIndex).isEmpty();
        }

        public int peek(int stackIndex) {
            return stacks.get(stackIndex).peek();
        }
    }

    public static class Stack2 {
        public ArrayList<Integer> heads;//每个栈的栈顶索引
        public ArrayList<Integer> values;//存储所有栈的元素值
        public ArrayList<Integer> lasts;//每个元素的前一个元素的索引  - > 类似与链表
        public ArrayList<Integer> frees;//空闲位置索引
        public int occupySize;//values中已占用的有效位置数量
        public int freeSize;//frees中记录的空闲位置数量

        public Stack2() {
            heads = new ArrayList<>();
            values = new ArrayList<>();
            lasts = new ArrayList<>();
            frees = new ArrayList<>();
            occupySize = 0;
            freeSize = 0;
        }

        public int stackSize() {
            return heads.size();
        }

        public void createNewStack() {
            heads.add(-1);//初始化为空栈
        }

        public void push(int num, int stackIndex) {
            int headIndex = heads.get(stackIndex);//获取目标栈当前的栈顶索引
            if (freeSize == 0) {
                heads.set(stackIndex, occupySize++);
                values.add(num);
                lasts.add(headIndex);
            } else {
                int freeIndex = frees.get(--freeSize);
                heads.set(stackIndex, freeIndex);//将栈顶更新为空闲索引
                values.set(freeIndex, num);
                lasts.set(freeIndex, headIndex);
            }
        }

        public int pop(int stackIndex) {
            int headIndex = heads.get(stackIndex);
            int ans = values.get(headIndex);
            int newHeadIndex = lasts.get(headIndex);
            heads.set(stackIndex, newHeadIndex);
            //将弹出的位置加入空闲列表
            if (freeSize >= frees.size()) {
                frees.add(headIndex);
                freeSize++;
            } else {
                frees.set(freeSize++, headIndex);
            }
            return ans;
        }

        public boolean isEmpty(int stackIndex) {
            return heads.get(stackIndex) == -1;
        }

        public int peek(int stackIndex) {
            return values.get(heads.get(stackIndex));
        }
    }

    public static void main(String[] args) {
        int V = 10000;
        int testTime = 20000;
        System.out.println("测试开始");
        Stack1 stack1 = new Stack1();
        Stack2 stack2 = new Stack2();
        for (int i = 0; i < testTime; i++) {
            double decide = Math.random();
            if (decide < 0.25) {
                stack1.createNewStack();
                stack2.createNewStack();
            } else {
                int stackSize1 = stack1.stackSize();
                int stackSize2 = stack2.stackSize();
                if (stackSize1 != stackSize2) {
                    System.out.println("栈的数量不一致！");
                    break;
                }
                if (stackSize1 > 0) {
                    int stackIndex = (int) (Math.random() * stackSize1);
                    if (decide < 0.5) {
                        int num = (int) (Math.random() * V);
                        stack1.push(num, stackIndex);
                        stack2.push(num, stackIndex);
                    } else if (decide < 0.75) {
                        if (stack1.isEmpty(stackIndex) != stack2.isEmpty(stackIndex)) {
                            System.out.println(stackIndex + "号栈的是否为空不一致！");
                            break;
                        }
                        if (!stack1.isEmpty(stackIndex)) {
                            if (stack1.pop(stackIndex) != stack2.pop(stackIndex)) {
                                System.out.println(stackIndex + "号栈的弹出数据不一致！");
                                break;
                            }
                        }
                    } else {
                        if (stack1.isEmpty(stackIndex) != stack2.isEmpty(stackIndex)) {
                            System.out.println(stackIndex + "号栈的是否为空不一致！");
                            break;
                        }
                        if (!stack1.isEmpty(stackIndex)) {
                            if (stack1.peek(stackIndex) != stack2.peek(stackIndex)) {
                                System.out.println(stackIndex + "号栈的栈顶数据不一致！");
                                break;
                            }
                        }
                    }
                }
            }
        }
        System.out.println("测试结束");
    }
}
