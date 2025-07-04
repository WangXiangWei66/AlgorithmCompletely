package Class03;

import java.util.Stack;

public class Code05_GetMinStack {
    public static class MyStack1{
        //准备两个栈，分别是数据栈和最小栈
        private Stack<Integer> stackData;
        private Stack<Integer> stackMin;

        public MyStack1(){
            stackData=new Stack<Integer>();
            stackMin=new Stack<Integer>();
        }
        //如果最小栈是空的，或者新加的值比最小栈栈顶的元素小，就把这个值加入最小栈
        public void push(int newNum){
            if(stackMin.isEmpty()||newNum<=this.getMin()){
                stackMin.push(newNum);
            }
            stackData.push(newNum);
        }

        public int pop(){
            if(stackData.isEmpty()){
                throw new RuntimeException("Your stack is empty");
            }
            int value=stackData.pop();
            if(value== getMin()){
                stackMin.pop();
            }
            return value;
        }

        private int getMin() {
            if(stackMin.isEmpty()){
                throw new RuntimeException("Your stack is empty!");
            }
            return stackMin.peek();
        }
    }
    public static class MyStack2{
        private Stack<Integer>stackData;
        private Stack<Integer>stackMin;

        public MyStack2(){
            stackData=new Stack<Integer>();
            stackMin=new Stack<Integer>();

        }
        public void push(int newNum){
            if(stackMin.isEmpty()||newNum< getMin()){
                stackMin.push(newNum);
            }else{
                stackMin.push(stackMin.peek());
            }
            stackData.push(newNum);
        }
        public int pop(){
            if(stackData.isEmpty()){
                throw new RuntimeException("Your stack is empty!");
            }
            stackMin.pop();
            return stackData.pop();
        }
        public int getMin() {
            if(stackMin.isEmpty()){
                throw new RuntimeException("Your stack is empty!");
            }
            return stackMin.peek();
        }
    }

    public static void main(String[]args){
        MyStack1 stack1 = new MyStack1();
        stack1.push(3);
        System.out.println(stack1.getMin());
        stack1.push(4);
        System.out.println(stack1.getMin());
        stack1.push(1);
        System.out.println(stack1.getMin());
        System.out.println(stack1.pop());
        System.out.println(stack1.getMin());

        System.out.println("================");

        MyStack2 stack2 = new MyStack2();
        stack2.push(3);
        System.out.println(stack2.getMin());
        stack2.push(4);
        System.out.println(stack2.getMin());
        stack2.push(1);
        System.out.println(stack2.getMin());
        System.out.println(stack2.pop());
        System.out.println(stack2.getMin());

    }
}
