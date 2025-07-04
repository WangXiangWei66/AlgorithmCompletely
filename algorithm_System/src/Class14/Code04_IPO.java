package Class14;

import java.util.Comparator;
import java.util.PriorityQueue;

public class Code04_IPO {

    //最多K个项目
    //W是初始资金
    //Profits[] Capital[]等长
    //返回最终最大资金
    public static int findMaximizedCapital(int K, int W, int[] profits, int[] Capital) {
        PriorityQueue<Program> minCostQ = new PriorityQueue<>(new MinCostComparator());
        PriorityQueue<Program> maxProfitQ = new PriorityQueue<>(new maxProfitComparator());
        for (int i = 0; i < profits.length; i++) {
            minCostQ.add(new Program(profits[i], Capital[i]));
        }
        for (int i = 0; i < K; i++) {
            while (!minCostQ.isEmpty() && minCostQ.peek().c <= W) {
                maxProfitQ.add(minCostQ.poll());
            }
            //现有的资金无法再解锁出新的项目了
            if (maxProfitQ.isEmpty()) {
                return W;
            }
            W += maxProfitQ.poll().p;
        }
        return W;
    }

    public static class Program {
        public int p;//项目的纯利润
        public int c;//项目的启动资金

        public Program(int p, int c) {
            this.p = p;
            this.c = c;
        }
    }

    public static class MinCostComparator implements Comparator<Program> {

        @Override
        public int compare(Program o1, Program o2) {
            return o1.c - o2.c;
        }
    }

    public static class maxProfitComparator implements Comparator<Program> {

        @Override
        public int compare(Program o1, Program o2) {
            return o2.p - o1.p;
        }
    }
}
