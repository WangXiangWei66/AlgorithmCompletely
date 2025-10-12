package class_2022_05_2;

//来自网易
//小红拿到了一个长度为N的数组arr，她准备只进行一次修改
//可以将数组中任意一个数arr[i]，修改为不大于P的正数（修改后的数必须和原数不同)
//并使得所有数之和为X的倍数
//小红想知道，一共有多少种不同的修改方案
//1 <= N, X <= 10^5, 1 <= arr[i], P <= 10^9
public class Code02_ModifyOneNumberModXWays {

    public static int ways1(int[] arr, int p, int x) {
        long sum = 0;
        for (int num : arr) {
            sum += num;
        }
        int ans = 0;
        for (int num : arr) {
            sum -= num;
            for (int v = 1; v <= p; v++) {
                if (v != num) {
                    if ((sum + v) % x == 0) {
                        ans++;
                    }
                }
            }
            sum += num;
        }
        return ans;
    }

    public static int ways2(int[] arr, int p, int x) {
        long sum = 0;
        for (int num : arr) {
            sum += num;
        }
        int ans = 0;
        for (int num : arr) {
            //对每个元素计算可行的方案数并累加
            //(x - (int) ((sum - num) % x)) % x  ： 修改元素num后，新值需要满足%x结果
            //推导：设修改后的值为 new_val，需满足 (sum - num + new_val) % x == 0
            //等价于：new_val % x == (x - (sum - num) % x) % x
            ans += cnt(p, x, num, (x - (int) ((sum - num) % x)) % x);
        }
        return ans;
    }
    //p：修改的值不大于p
    //x：所有数之和的倍数
    public static int cnt(int p, int x, int num, int mod) {
        int ans = (p / x) + ((p % x) >= mod ? 1 : 0) - (mod == 0 ? 1 : 0);
        //减去原数字本身（如果原数字符合条件）
        return ans - ((num <= p && num % x == mod) ? 1 : 0);
    }

    public static int[] randomArray(int n, int v) {
        int[] ans = new int[n];
        for (int i = 0; i < n; i++) {
            ans[i] = (int) (Math.random() * v) + 1;
        }
        return ans;
    }

    public static void main(String[] args) {
        int len = 100;
        int value = 100;
        int testTime = 100000;
        System.out.println("测试开始");
        for (int i = 0; i < testTime; i++) {
            int n = (int) (Math.random() * len) + 1;
            int[] arr = randomArray(n, value);
            int p = (int) (Math.random() * value) + 1;
            int x = (int) (Math.random() * value) + 1;
            int ans1 = ways1(arr, p, x);
            int ans2 = ways2(arr, p, x);
            if (ans1 != ans2) {
                System.out.println("出错了！");
            }
        }
        System.out.println("测试结束");
    }

}
