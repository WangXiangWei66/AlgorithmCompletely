package class_2022_09_3;

//来自美团
//某天，小美在玩一款游戏，游戏开始时，有n台机器，
//每台机器都有一个能量水平，分别为a1、a2、…、an，
//小美每次操作可以选其中的一台机器，假设选的是第i台，
//那小美可以将其变成 ai+10^k（k为正整数且0<=k<=9），
//由于能量过高会有安全隐患，所以机器会在小美每次操作后会自动释放过高的能量
//即变成 (ai+10^k)%m
//其中%m表示对m取模，由于小美还有工作没有完成，所以她想请你帮她计算一下，
//对于每台机器，将其调节至能量水平为0至少需要多少次操作
//机器自动释放能量不计入小美的操作次数）。
//第一行两个正整数n和m，表示数字个数和取模数值。
//第二行为n个正整数a1, a2,...... an，其中ai表示第i台机器初始的能量水平。
//1 <= n <= 30000，2 <= m <= 30000, 0 <= ai <= 10^12。
public class Code03_AllNumbersModToZeroMinTimes {

    // arr为long[]，删除错误判断，统一基于模值计算
    public static int[] times(int n, int m, long[] arr) {
        int[] map = new int[m];
        bfs(m, map);  // 依赖修正后的bfs（add上限1e9，map[0]=0）
        int[] ans = new int[n];

        for (int i = 0; i < n; i++) {
            long num = arr[i];  // 用long存储大数值，避免溢出
            int x = (int) (num % m);  // 关键：计算num的模m值x（x∈[0,m-1]，不会数组越界）

            int minTimes = map[x];  // 情况1：不加任何10^k，直接用x到0的步数

            // 情况2：加1次10^k，总步数=1 + map[新模值]，取最小值
            for (long add = 1; add <= 1000000000; add *= 10) {
                int newMod = (int) (((long) x + add) % m);  // x是模值，等价于(num+add)%m
                minTimes = Math.min(minTimes, map[newMod] + 1);
            }

            ans[i] = minTimes;
        }
        return ans;
    }

    public static void bfs(int m, int[] map) {
        boolean[] visited = new boolean[m];
        visited[0] = true;
        map[0]=0;
        //存储待处理的模值
        int[] queue = new int[m];
        int l = 0;//头指针,用于处理当前模值
        int r = 1;//尾指针,处理新发现模值
        while (l < r) {
            int cur = queue[l++];
            //正向逻辑：from + add → 取模m → cur → 等价于 (from + add) % m = cur
            //反向推导：from = (cur - (add % m)) % m（避免add太大，先取模m简化计算）
            for (long add = 1; add <= 1000000000; add *= 10) {
                int from = cur - (int) (add % m);
                if (from < 0) {
                    from += m;
                }
                if (!visited[from]) {
                    visited[from] = true;
                    map[from] = map[cur] + 1;
                    queue[r++] = from;
                }
            }
        }
    }

    public static void main(String[] args) {
        int m = 100;
        int[] map = new int[m];
        bfs(m, map);
        for (int i = 0; i < m; i++) {
            System.out.println(i + " , " + map[i]);
        }
    }
}
