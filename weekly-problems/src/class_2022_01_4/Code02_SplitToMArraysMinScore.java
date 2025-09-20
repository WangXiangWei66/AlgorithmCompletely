package class_2022_01_4;

import java.util.ArrayList;
import java.util.LinkedList;

//来自美团
//小团去参加军训，军训快要结束了，长官想要把大家一排n个人分成m组，然后让每组分别去参加阅兵仪式
//只能选择相邻的人一组，不能随意改变队伍中人的位置
//阅兵仪式上会进行打分，其中有一个奇怪的扣分点是每组的最大差值，即每组最大值减去最小值
//长官想要让这分成的m组总扣分量最小，即这m组分别的极差之和最小
//长官正在思索如何安排中，就让小团来帮帮他吧
public class Code02_SplitToMArraysMinScore {

    public static int minScore1(int[] arr, int m) {
        if (m == 0) {
            return 0;
        }
        //从1开始是因为索引1已经作为了第一组的起始
        return process(arr, 1, m, arr[0], arr[0]);
    }

    //rest：剩余的组数
    public static int process(int[] arr, int index, int rest, int preMin, int preMax) {
        if (index == arr.length) {
            return rest == 1 ? (preMax - preMin) : -1;
        }
        int p1 = process(arr, index + 1, rest, Math.min(preMin, arr[index]), Math.max(preMax, arr[index]));
        //当前元素作为新一组的开始
        int p2Next = process(arr, index + 1, rest - 1, arr[index], arr[index]);
        int p2 = p2Next == -1 ? -1 : (preMax - preMin + p2Next);
        if (p1 == -1) {
            return p2;
        }
        if (p2 == -1) {
            return p1;
        }
        return Math.min(p1, p2);
    }

    //外层对应独立的分组
    //内存当个分组的元素的集合
    public static int score(ArrayList<LinkedList<Integer>> sets) {
        int ans = 0;
        for (LinkedList<Integer> set : sets) {
            if (set.isEmpty()) {
                return Integer.MAX_VALUE;
            }
            int max = Integer.MAX_VALUE;
            int min = Integer.MIN_VALUE;
            for (int num : set) {
                max = Math.max(max, num);
                min = Math.min(min, num);
            }
            ans += max - min;
        }
        return ans;
    }

    //时间复杂度为O(N ^ 2 * m)
    public static int minScore2(int[] arr, int m) {
        if (m == 0) {
            return 0;
        }
        int n = arr.length;
        //score[i][j]:子数组arr[i...j]的极差
        int[][] score = new int[n][n];
        //下面这个嵌套for循环计算所有可能的极差
        //固定左边界
        for (int i = 0; i < n; i++) {
            int max = arr[i];
            int min = arr[i];
            score[i][i] = max - min;
            //向右扩展右边界
            for (int j = i + 1; j < n; j++) {
                max = Math.max(max, arr[j]);
                min = Math.min(min, arr[j]);
                score[i][j] = max - min;
            }
        }
        //dp[split][i]:表示将子数组arr[0...i]分成spilt组的最小总极差
        int[][] dp = new int[m + 1][n];
        for (int i = 0; i < n; i++) {
            dp[1][i] = score[0][i];
        }
        //最外层循环枚举分组数
        for (int split = 2; split <= m; split++) {
            //枚举右边界
            for (int i = split; i < n; i++) {
                dp[split][i] = dp[split - 1][i];
                //枚举最后一组的右边界
                for (int j = 1; j <= i; j++) {
                    // 状态转移方程：
                    // 前split-1组处理arr[0..j-1]，最后1组处理arr[j..i]
                    // 总极差 = 前split-1组的最小极差 + arr[j..i]的极差
                    dp[split][i] = Math.min(dp[split][i], dp[split - 1][j - 1] + score[j][i]);
                }
            }
        }
        return dp[m][n - 1];
    }

    public static int[] randomArray(int n, int v) {
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = (int) (Math.random() * v);
        }
        return arr;
    }

    public static void main(String[] args) {
        int len = 15;
        int value = 50;
        int testTime = 20000;
        System.out.println("测试开始");
        for (int i = 0; i < testTime; i++) {
            int n = (int) (Math.random() * len) + 1;
            int[] arr = randomArray(n, value);
            int m = (int) (Math.random() * n) + 1;
            int ans1 = minScore1(arr, m);
            int ans2 = minScore2(arr, m);
            if (ans1 != ans2) {
                System.out.println("出错了!");
                for (int num : arr) {
                    System.out.print(num + " ");
                }
                System.out.println();
                System.out.println("m : " + m);
                System.out.println(ans1);
                System.out.println(ans2);
                break;
            }
        }
        System.out.println("测试结束");
    }
}
