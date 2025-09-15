package Class_2021_12_5;

//来自hulu
//你只有1*1、1*2、1*3、1*4，四种规格的砖块
//你想铺满n行m列的区域，规则如下：
//1）不管那种规格的砖，都只能横着摆
//比如1*3这种规格的砖，3长度是水平方向，1长度是竖直方向
//2）会有很多方法铺满整个区域，整块区域哪怕有一点点不一样，就算不同的方法
//3）区域内部(不算区域整体的4条边界)，不能有任何砖块的边界线（从上一直贯穿到下）
//返回符合三条规则下，铺满n行m列的区域，有多少种不同的摆放方法
public class Code03_WaysToBuildWall {
    //r[i]:用指定的砖块铺满1行i列的方法数
    public static long[] r = {0, 1, 2, 4, 8};

    public static long ways(int n, int m) {
        if (n <= 0 || m <= 1) {
            return 1;
        }
        //len[i]:一行i列的摆放方法数
        long[] len = new long[m + 1];
        for (int i = 1; i <= Math.max(m, 4); i++) {
            len[i] = r[i];
        }
        for (int i = 5; i <= m; i++) {
            // 递推公式：len[i] = 前4种情况之和（最后一块用1×1、1×2、1×3、1×4）
            len[i] = len[i - 1] + len[i - 2] + len[i - 3] + len[i - 4];
        }
        //any[i]:n行i列区域的所有摆放方法数
        long[] any = new long[m + 1];
        for (int i = 1; i <= m; i++) {
            any[i] = power(len[i], n);
        }
        long[] solid = new long[m + 1];
        solid[1] = 1;
        for (int i = 2; i <= m; i++) {
            long invalid = 0;
            for (int j = 1; j < i; j++) {
                invalid += solid[j] * any[i - j];
            }
            solid[i] = any[i] - invalid;
        }
        return solid[m];
    }

    public static long power(long base, int power) {
        long ans = 1;
        while (power != 0) {
            if ((power & 1) != 0) {
                ans *= base;
            }
            base *= base;
            power >>= 1;
        }
        return ans;
    }
}
