package class_2021_12_5;

import java.util.Arrays;
import java.util.PriorityQueue;

//来自hulu
//有n个人，m个任务，任务之间有依赖记录在int[][] depends里
//比如: depends[i] = [a, b]，表示a任务依赖b任务的完成
//其中 0 <= a < m，0 <= b < m
//1个人1天可以完成1个任务，每个人都会选当前能做任务里，标号最小的任务
//一个任务所依赖的任务都完成了，该任务才能开始做
//返回n个人做完m个任务，需要几天
public class Code02_DoAllJobs {

    public static int days(int n, int m, int[][] depends) {
        //人数小于1，无法完成任务
        if (n < 1) {
            return -1;
        }
        if (m <= 0) {
            return 0;
        }
        //任务依赖的邻接表
        int[][] nexts = nexts(depends, m);
        //每个任务依赖的任务的数量
        int[] indegree = indegree(nexts, m);
        //按照工人可用的最早时间从小到大排序
        PriorityQueue<Integer> workers = new PriorityQueue<>();
        for (int i = 0; i < n; i++) {
            workers.add(0);
        }
        //按照当前可执行的任务，按照编号从小到大排序
        PriorityQueue<Integer> zeroIn = new PriorityQueue<>();
        for (int i = 0; i < m; i++) {
            if (indegree[i] == 0) {
                zeroIn.add(i);
            }
        }
        //每个任务最早开始的时间
        int[] start = new int[m];
        int finishAll = 0;//记录总天数
        int done = 0;//已完成的任务数量
        //处理所有可执行的任务
        while (!zeroIn.isEmpty()) {
            int job = zeroIn.poll();
            int wake = workers.poll();
            int finish = Math.max(start[job], wake) + 1;//任务的开始时间
            finishAll = Math.max(finishAll, finish);
            done++;
            //处理依赖当前任务的后续任务
            for (int next : nexts[job]) {
                start[next] = Math.max(start[next], finish);
                if (--indegree[next] == 0) {
                    zeroIn.add(next);
                }
            }
            workers.add(finish);
        }
        return done == m ? finishAll : -1;
    }

    public static int[][] nexts(int[][] depends, int m) {
        //按照依赖任务从小到大排序
        Arrays.sort(depends, (a, b) -> a[1] - b[1]);
        int n = depends.length;
        int[][] nexts = new int[m][0];
        if (n == 0) {
            return nexts;
        }
        //将依赖的同一前置任务的任务放在一起
        int size = 1;
        for (int i = 1; i < n; i++) {
            if (depends[i - 1][1] != depends[i][1]) {
                int from = depends[i - 1][1];
                nexts[from] = new int[size];
                //填充依赖该前置任务的所有任务
                for (int k = 0, j = i - size; k < size; k++, j++) {
                    nexts[from][k] = depends[j][0];
                }
                size = 1;
            } else {
                size++;
            }
        }
        int from = depends[n - 1][1];
        nexts[from] = new int[size];
        for (int k = 0, j = n - size; k < size; k++, j++) {
            nexts[from][k] = depends[j][0];
        }
        return nexts;
    }

    public static int[] indegree(int[][] nexts, int m) {
        int[] indegree = new int[m];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < nexts[i].length; j++) {
                indegree[nexts[i][j]]++;
            }
        }
        return indegree;
    }

    public static void main(String[] args) {
        int[][] d = {
                {3,0},
                {4,1},
                {5,2},
                {4,3},
                {6,5},
                {7,4},
                {7,6}
        };
        System.out.println(days(3,8,d));
        System.out.println(days(2,8,d));
    }
}
