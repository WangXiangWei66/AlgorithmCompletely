package Class18;

//汉诺塔问题讲解：https://blog.csdn.net/qq_41112170/article/details/80725336
//给定一个数组arr，长度为N，arr中的值只有1，2，3三种
//arr[i] == 1，代表汉诺塔问题中，从上往下第i个圆盘目前在左
//arr[i] == 2，代表汉诺塔问题中，从上往下第i个圆盘目前在中
//arr[i] == 3，代表汉诺塔问题中，从上往下第i个圆盘目前在右
//那么arr整体就代表汉诺塔游戏过程中的一个状况，如果这个状况不是汉诺塔最优解运动过程中的状况，返回-1
//如果这个状况是汉诺塔最优解运动过程中的状态，返回它是第几个状态
public class Code01_HanoiProblem {
    public static int step1(int[] arr) {
        if (arr == null || arr.length == 0) {
            return -1;
        }
        return process(arr, arr.length - 1, 1, 2, 3);
    }

    // 目标是: 把0~i的圆盘，从from全部挪到to上
    // 返回，根据arr中的状态arr[0..i]，它是最优解的第几步？
    // f(i, 3 , 2, 1) f(i, 1, 3, 2) f(i, 3, 1, 2)
    public static int process(int[] arr, int i, int from, int other, int to) {
        // 基本情况：所有圆盘都已处理完毕，返回0步
        if (i == -1) {
            return 0;
        }
        // 错误情况：当前圆盘不在from杆或to杆上（无效状态）
        if (arr[i] != from && arr[i] != to) {
            return -1;
        }
        // 情况1：当前最大圆盘（i）仍在from杆上，需递归处理剩余圆盘
        if (arr[i] == from) {
            return process(arr, i - 1, from, to, other);
        } else { // 情况2：当前最大圆盘已在to杆上
            int rest = process(arr, i - 1, other, from, to);// 递归计算将剩余i-1个圆盘从other杆移动到to杆的步数
            if (rest == -1) {
                return -1;
            }
            return (1 << i) + rest; // 总步数 = 移动前i-1个圆盘到other杆的步数（2^i -1） + 剩余步数
        }
    }

    public static int step2(int[] arr) {
        if (arr == null || arr.length == 0) {
            return -1;
        }
        int from = 1;
        int mid = 2;
        int to = 3;
        int i = arr.length - 1;
        int res = 0;//记录总步数
        int tmp = 0;//临时变量，用于交换柱子
        // 从最大圆盘到最小圆盘依次处理
        while (i >= 0) {
            //先检查当前的圆盘是否在合法的柱子上
            if (arr[i] != from && arr[i] != to) {
                return -1;
            }
            if (arr[i] == to) {
                res += 1 << i;
                // 最大圆盘已在目标柱，接下来需要处理剩余圆盘从新的起始柱到新的目标柱
                tmp = from;
                from = mid;
            } else {
                tmp = to;
                to = mid;
            }
            //更新中间柱为临时保存的值
            mid = tmp;
            //处理下一个较小的圆盘
            i--;
        }
        return res;
    }

    public static int kth(int[] arr) {
        int N = arr.length;
        return step(arr, N - 1, 1, 3, 2);
    }

    // 0...index这些圆盘，arr[0..index] index+1层塔
    // 在哪？from 去哪？to 另一个是啥？other
    // arr[0..index]这些状态，是index+1层汉诺塔问题的，最优解第几步
    public static int step(int[] arr, int index, int from, int to, int other) {
        if (index == -1) {
            return 0;
        }
        if (arr[index] == other) {
            return -1;
        }
        // arr[index] == from arr[index] == to;
        if (arr[index] == from) {
            return step(arr, index - 1, from, other, to);
        } else {
            // p1: 将前index-1个圆盘从from移动到other的步数（2^index -1）
            int p1 = (1 << index) - 1;
            // p2: 将第index个圆盘从from移动到to的步数（1步）
            int p2 = 1;
            // p3: 递归验证剩余圆盘能否从other移动到to（借助from）
            int p3 = step(arr, index - 1, other, to, from);
            if (p3 == -1) {
                return -1;
            }
            return p1 + p2 + p3;
        }
    }

    public static void main(String[] args) {
        int[] arr = {3, 3, 2, 1};
        System.out.println(step1(arr));
        System.out.println(step2(arr));
        System.out.println(kth(arr));
    }
}
