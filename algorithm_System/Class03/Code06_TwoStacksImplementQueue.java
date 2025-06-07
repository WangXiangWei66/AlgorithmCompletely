package Class03;

import java.util.Stack;

public class Code06_TwoStacksImplementQueue {
    public static class TwoStackQueue{
        public Stack<Integer> stackPush;
        public Stack<Integer> stackPop;

        public TwoStackQueue(){
            stackPush =new Stack<Integer>();
            stackPop =new Stack<Integer>();
        }

        //push栈向pop栈导入数据
        public void pushToPop(){
            //pop栈为空，才能去导数据
            if(stackPop.empty()){
                while(!stackPush.empty()){
                    stackPop.push(stackPush.pop());
                }
            }
        }

        public void add(int pushInt){
            stackPush.push(pushInt);
            pushToPop();
        }

        public int poll(){
            if(stackPop.empty()&& stackPush.empty()){
                throw new RuntimeException("Queue is empty!");
            }
            pushToPop();
            return stackPop.pop();
        }

        public int peek(){
            if(stackPop.empty()&& stackPush.empty()){
                throw new RuntimeException("Queue is empty");
            }
            pushToPop();
            return stackPop.peek();
        }
    }

    public static void main(String[]args){
        TwoStackQueue test = new TwoStackQueue();
        test.add(1);
        test.add(2);
        test.add(3);
        System.out.println(test.peek());
        System.out.println(test.poll());
        System.out.println(test.peek());
        System.out.println(test.poll());
        System.out.println(test.peek());
        System.out.println(test.poll());
    }
}
