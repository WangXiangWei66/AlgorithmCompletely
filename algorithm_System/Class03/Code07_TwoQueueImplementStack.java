package Class03;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class Code07_TwoQueueImplementStack {
    public static class TwoQueueStack<T> {
        public Queue<T> queue;
        public Queue<T> help;

        public TwoQueueStack() {
            queue = new LinkedList<>();
            help = new LinkedList<>();
        }

        //往queue里面加数
        public void push(T value) {
            queue.offer(value);
        }

        //先将数加入help中
        public T poll() {
            while (queue.size() > 1) {
                help.offer(queue.poll());
            }
            //要弹出的数为queue的栈顶元素，即还剩的最后一个元素
            T ans = queue.poll();
            //将queue与help队列交换一下
            Queue<T> tmp = queue;
            queue = help;
            help = tmp;
            return ans;
        }

        public T peek() {
            //先将数导入help
            while (queue.size() > 1) {
                help.offer(queue.poll());
            }
            T ans = queue.poll();
            help.offer(ans);
            Queue<T> tmp = queue;
            queue = help;
            help = tmp;
            return ans;
        }

        public boolean isEmpty() {
            return queue.isEmpty();
        }
    }

    public static void main(String[] args) {
        System.out.println("test begin!");
        TwoQueueStack<Integer> myStack = new TwoQueueStack<>();
        Stack<Integer> test = new Stack<>();
        int testTime = 10000000;
        int max = 1000000;
        for (int i = 0; i < testTime; i++) {
            if (myStack.isEmpty()) {
                if (!test.isEmpty()) {
                    System.out.println("Ops!");
                }
                int num = (int) (max * Math.random());
                myStack.push(num);
                test.push(num);
            } else {
                if (Math.random() < 0.25) {
                    int num = (int) (max * Math.random());
                    myStack.push(num);
                    test.push(num);
                } else if (Math.random() < 0.5) {
                    if (!myStack.peek().equals(test.peek())) {
                        System.out.println("Ops!");
                    }
                } else if (Math.random() < 0.75) {
                    if (!myStack.poll().equals(test.pop())) {
                        System.out.println("Ops!");
                    }
                } else {
                    if (myStack.isEmpty() != test.isEmpty()) {
                        System.out.println("Ops!");
                    }
                }
            }
        }
        System.out.println("test finish!");
    }
}