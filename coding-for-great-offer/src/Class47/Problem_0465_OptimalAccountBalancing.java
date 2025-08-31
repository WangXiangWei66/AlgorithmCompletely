package Class47;

import java.util.Arrays;
import java.util.HashMap;

//一开始所有人手上都是0元钱，[a,b,c]代表，a给b了c元钱。有很多这样的转账记录
//那么所有转账完成后，有的人剩余的钱是正数，有的人剩余的钱是负数
//现在想重新让所有人的钱都是0元，也就是回到初始状态，请问最少多少笔转账可以做到这一点
//例子1：
//输入：[[0,1,10],[2,0,5]]
//输出：2
//解释：0号转给1号10元，2号转给0号5元。上面所有转账完成后：0号-5，1号10，2号-5
//所以如果想所有人的钱都是0元，1号需要分别给0号和2号转账5元。所以最少2笔转账。
//例子2：
//输入：[[0,1,10],[1,0,1],[1,2,5],[2,0,5]]
//输出：1
//解释：如上的转账完成后，0号-4元，1号4元，2号0元，所以如果想所有人的钱都是0元，1号需要给0号转账4元。所以最少1比交易。
//leetcode题目：https://leetcode.com/problems/optimal-account-balancing/
public class Problem_0465_OptimalAccountBalancing {

    public static int minTransfers1(int[][] transactions) {
        int[] dept = depts(transactions);
        int N = dept.length;
        //最少转账记录=非0账户数-最大分组数
        //sum：当前分组的余额和
        //set：状态压缩标记
        //N：非0账户数
        return N - process1(dept, (1 << N) - 1, 0, N);
    }

    //通过状态压缩标记已分组的账户，递归找到能构成余额和为0的最大分组数
    public static int process1(int[] dept, int set, int sum, int N) {
        //如果只剩下一个账户未分组，返回0
        if ((set & (set - 1)) == 0) {
            return 0;
        }
        int value = 0;//当前账户余额
        int max = 0;//当前状态下的最大分组数
        for (int i = 0; i < N; i++) {
            value = dept[i];//获取账户i的余额
            if ((set & (1 << i)) != 0) {
                max = Math.max(max, process1(dept, set ^ (1 << i), sum - value, N));
            }
        }
        return sum == 0 ? max + 1 : max;
    }

    //计算每个账户的最终余额，筛选出非0余额的账户
    public static int[] depts(int[][] transaction) {
        //key：账户ID，value：余额
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int[] tran : transaction) {
            //0:转帐方
            //1:收款方
            //2:转账金额
            map.put(tran[0], map.getOrDefault(tran[0], 0) + tran[2]);
            map.put(tran[1], map.getOrDefault(tran[1], 0) - tran[2]);
        }
        //统计非0金额的账户数
        int N = 0;
        for (int value : map.values()) {
            if (value != 0) {
                N++;
            }
        }
        //创建非0账户数组
        int[] dept = new int[N];
        int index = 0;
        for (int value : map.values()) {
            if (value != 0) {
                dept[index++] = value;
            }
        }
        return dept;
    }

    public static int minTransfers2(int[][] transactions) {
        int[] dept = depts(transactions);
        int N = dept.length;
        int sum = 0;
        for (int num : dept) {
            sum += num;
        }
        //dp[set]:在状态set下，能构成的最大分组数
        int[] dp = new int[1 << N];
        Arrays.fill(dp, -1);
        return N - process2(dept, (1 << N) - 1, sum, N, dp);
    }

    public static int process2(int[] dept, int set, int sum, int N, int[] dp) {
        if (dp[set] != -1) {
            return dp[set];
        }
        int ans = 0;//当前状态的最大分组数
        if ((set & (set - 1)) != 0) {
            int value = 0;
            int max = 0;
            for (int i = 0; i < N; i++) {
                value = dept[i];
                if ((set & (1 << i)) != 0) {
                    max = Math.max(max, process2(dept, set ^ (1 << i), sum - value, N, dp));
                }
            }
            ans = sum == 0 ? max + 1 : max;
        }
        dp[set] = ans;
        return ans;
    }

    public static int[][] randomTrans(int s, int n, int m) {
        int[][] trans = new int[s][3];
        for (int i = 0; i < s; i++) {
            trans[i][0] = (int) (Math.random() * n);
            trans[i][1] = (int) (Math.random() * n);
            trans[i][2] = (int) (Math.random() * m) + 1;
        }
        return trans;
    }

    public static void main(String[] args) {
        int s = 8;
        int n = 8;
        int m = 10;
        int testTime = 10000;
        System.out.println("test begin!");
        for (int i = 0; i < testTime; i++) {
            int[][] trans = randomTrans(s, n, m);
            int ans1 = minTransfers1(trans);
            int ans2 = minTransfers2(trans);
            if (ans1 != ans2) {
                System.out.println("Oops");
                System.out.println(ans1);
                System.out.println(ans2);
                break;
            }
        }
        System.out.println("test end!");
    }
}
