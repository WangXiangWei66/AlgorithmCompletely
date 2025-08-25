package Class42;

//给定一个含有n个正数的数组x。从点(0,0)开始，先向北移动x[0]米，然后向西移动x[1]米，向南移动x[2]米，向东移动x[3]米，持续移动
//也就是说，每次移动后你的方位会发生逆时针变化
//编写一个O(1)空间复杂度的一趟扫描算法，判断你所经过的路径是否相交。
//leetcode题目 : https://leetcode.com/problems/self-crossing/
public class Problem_0335_SelfCrossing {
    //x[0]：向北（y 轴正方向）移动 x[0] 米；
    //x[1]：向西（x 轴负方向）移动 x[1] 米；
    //x[2]：向南（y 轴负方向）移动 x[2] 米；
    //x[3]：向东（x 轴正方向）移动 x[3] 米；
    //x[4]：再次向北移动 x[4] 米，以此类推。
    public static boolean isSelfCrossing(int[] x) {
        if (x == null || x.length < 4) {
            return false;
        }
        if ((x.length > 3 && x[2] <= x[0] && x[3] >= x[1])
                || (x.length > 4
                && ((x[3] <= x[1] && x[4] >= x[2]) || (x[3] == x[1] && x[0] + x[4] >= x[2])))) {
            return true;
        }
        //第 i-1 步距离 ≤ 第 i-3 步距离，且满足以下任一：
        //1. 第 i 步距离 ≥ 第 i-2 步距离；
        //2. 第 i-2 步距离 ≥ 第 i-4 步距离，且第 i-5 步 + 第 i-1 步 ≥ 第 i-3 步，且第 i-4 步 + 第 i 步 ≥ 第 i-2 步
        for (int i = 5; i < x.length; i++) {
            if (x[i - 1] <= x[i - 3] && ((x[i] >= x[i - 2])
                    || (x[i - 2] >= x[i - 4] && x[i - 5] + x[i - 1] >= x[i - 3] && x[i - 4] + x[i] >= x[i - 2]))) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        int[] arr = {2, 2, 3, 2, 2};
        System.out.println(isSelfCrossing(arr));
    }
}
