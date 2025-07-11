package Class16;

import jdk.dynalink.beans.StaticClass;

import java.util.HashMap;
import java.util.HashSet;

//给定一个有正、有负、有0的数组arr，
//给定一个整数k，
//返回arr的子集是否能累加出k
//1）正常怎么做？
//2）如果arr中的数值很大，但是arr的长度不大，怎么做？
public class Code01_IsSum {

    //arr的值可能为正，可能为负，可能为0
    //自由选择arr中的数字，额能不能累加得到sum
    //暴力方法

    public static boolean isSum1(int[] arr, int sum) {
        if (sum == 0) {
            return true;
        }
        if (arr == null || arr.length == 0) {
            return false;
        }
        return process1(arr, arr.length - 1, sum);
    }

    //可以自由的使用arr[0...i]上的数字，能不能累加得到sum
    public static boolean process1(int[] arr, int i, int sum) {
        if (sum == 0) {
            return true;
        }
        if (i == -1) {
            return false;
        }
        return process1(arr, i - 1, sum) || process1(arr, i - 1, sum - arr[i]);
    }

    // arr中的值可能为正，可能为负，可能为0
    // 自由选择arr中的数字，能不能累加得到sum
    // 记忆化搜索方法
    // 从暴力递归方法来，加了记忆化缓存，就是动态规划了
    public static boolean isSum2(int[] arr, int sum) {
        if (sum == 0) {
            return true;
        }
        if (arr == null || arr.length == 0) {
            return false;
        }
        return process2(arr, arr.length - 1, sum, new HashMap<>());
    }

    public static boolean process2(int[] arr, int i, int sum, HashMap<Integer, HashMap<Integer, Boolean>> dp) {
        if (dp.containsKey(i) && dp.get(i).containsKey(sum)) {
            return dp.get(i).get(sum);
        }

        boolean ans = false;
        if (sum == 0) {
            ans = true;
        } else if (i != -1) {
            ans = process2(arr, i - 1, sum, dp) || process2(arr, i - 1, sum - arr[i], dp);
        }
        if (!dp.containsKey(i)) {
            dp.put(i, new HashMap<>());
        }
        dp.get(i).put(sum, ans);
        return ans;
    }


    // arr中的值可能为正，可能为负，可能为0
    // 自由选择arr中的数字，能不能累加得到sum
    // 经典动态规划
    public static boolean isSum3(int[] arr, int sum) {
        //前i个元素，能否组成和为j的状态
        if (sum == 0) {
            return true;
        }

        if (arr == null || arr.length == 0) {
            return false;
        }

        int min = 0;
        int max = 0;
        for (int num : arr) {
            min += num < 0 ? num : 0; //累加所有的负数——最小可能和
            max += num > 0 ? num : 0;//累加所有正数（最大可能和）
        }
        //将不可能的情况提前排除
        if (sum < min || sum > max) {
            return false;
        }

        int N = arr.length;
        //dp[i][j]：前i+1个元素，能否组成(min+j)的组合  —— 保证每个数都是正数，避免出现越界情况
        boolean[][] dp = new boolean[N][max - min + 1];//保证可以覆盖所有可能的元素
        dp[0][-min] = true; //不选第一个元素，和为0
        dp[0][arr[0] - min] = true;//选第一个元素，可以组成arr[0]
        for (int i = 1; i < N; i++) { //遍历从第二个元素，到最后一个元素
            for (int j = min; j <= max; j++) { //遍历所有的可能和
                // 状态1：不选当前元素arr[i]，则状态与前i-1个元素相同
                dp[i][j - min] = dp[i - 1][j - min];
                // 状态2：选当前元素arr[i]，则需检查“前i-1个元素能否组成和为(j - arr[i])
                int next = j - min - arr[i];// 计算“前i-1个元素需组成的和”对应的列索引
                // 若next在有效范围内，且前i-1个元素能组成该和，则当前状态为true
                dp[i][j - min] |= (next >= 0 && next <= max - min && dp[i - 1][next]);
            }
        }
        return dp[N - 1][sum - min];
    }

    // arr中的值可能为正，可能为负，可能为0
    // 自由选择arr中的数字，能不能累加得到sum
    // 分治的方法
    // 如果arr中的数值特别大，动态规划方法依然会很慢
    // 此时如果arr的数字个数不算多(40以内)，哪怕其中的数值很大，分治的方法也将是最优解
    public static boolean isSum4(int[] arr, int sum) {
        if (sum == 0) {
            return true;
        }
        if (arr == null || arr.length == 0) {
            return false;
        }
        //处理数组，只有一个元素的情况
        if (arr.length == 1) {
            return arr[0] == sum;
        }

        int N = arr.length;
        int mid = N >> 1;
        //用hashSet存左右两部分的所有和，保证快速查询
        //组合验证：遍历左部分的所有可能和，检查右部分是否存在补数（即sum-当前和),从而验证是否存在满足条件的子集
        HashSet<Integer> leftSum = new HashSet<>();
        HashSet<Integer> rightSum = new HashSet<>();
        //递归计算，左右两部分的累加和
        process4(arr, 0, mid, 0, leftSum);
        process4(arr, mid, N, 0, rightSum);
        // 检查三种情况：
        // 1. 只使用左部分能否组成sum（即leftSum包含sum）
        // 2. 只使用右部分能否组成sum（即rightSum包含sum）
        // 3. 左右部分组合能否组成sum（即存在l∈leftSum，使得sum-l∈rightSum）
        for (int l : leftSum) {
            if (rightSum.contains(sum - l)) {
                return true;
            }
        }
        return false;
    }

    // arr[0...i-1]决定已经做完了！形成的累加和是pre
    // arr[i...end - 1] end(终止)  所有数字随意选择，
    // arr[0...end-1]所有可能的累加和存到ans里去
    // 递归计算从索引i到end-1的所有可能子集和，并存入ans
    //pre是递归路上，已经选择的元素的累加和
    public static void process4(int[] arr, int i, int end, int pre, HashSet<Integer> ans) {
        if (i == end) {
            ans.add(pre);//递归结束，将当前累加和加入集合
        } else {
            //选择不包含当前元素arr[i]
            process4(arr, i + 1, end, pre, ans);
            //选择包含当前元素arr[i]
            process4(arr, i + 1, end, pre + arr[i], ans);
        }
    }

    // 为了测试
    // 生成长度为len的随机数组
    // 值在[-max, max]上随机
    public static int[] randomArray(int len, int max) {
        int[] arr = new int[len];
        for (int i = 0; i < len; i++) {
            arr[i] = (int) (Math.random() * ((max << 1) + 1)) - max;
        }
        return arr;
    }

    public static void main(String[] args) {
        int N = 20;
        int M = 100;
        int testTime = 100000;
        System.out.println("test begin");
        for (int i = 0; i < testTime; i++) {
            int size = (int) (Math.random() * (N + 1));
            int[] arr = randomArray(size, M);
            int sum = (int) (Math.random() * ((M << 1) + 1)) - M;
            boolean ans1 = isSum1(arr, sum);
            boolean ans2 = isSum2(arr, sum);
            boolean ans3 = isSum3(arr, sum);
            boolean ans4 = isSum4(arr, sum);
            if (ans1 ^ ans2 || ans3 ^ ans4 || ans1 ^ ans3) {
                System.out.println("出错了");
                System.out.print("arr:");
                for (int num : arr) {
                    System.out.print(num + ""); //紧跟在当前输出后
                }
                System.out.println(); //从下一行开始输出，有换行
                System.out.println("sum : " + ans1);
                System.out.println("方法一的答案:" + ans1);
                System.out.println("方法二的答案:" + ans2);
                System.out.println("方法三的答案:" + ans3);
                System.out.println("方法四的答案:" + ans4);
                break;
            }
        }
        System.out.println("test finish");
    }
}
