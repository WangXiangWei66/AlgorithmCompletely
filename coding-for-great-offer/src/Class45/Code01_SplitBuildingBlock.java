package Class45;

import java.util.Arrays;

//来自京东笔试
//小明手中有n块积木，并且小明知道每块积木的重量。现在小明希望将这些积木堆起来
//要求是任意一块积木如果想堆在另一块积木上面，那么要求：
//1) 上面的积木重量不能小于下面的积木重量
//2) 上面积木的重量减去下面积木的重量不能超过x
//3) 每堆中最下面的积木没有重量要求
//   现在小明有一个机会，除了这n块积木，还可以获得k块任意重量的积木。
//   小明希望将积木堆在一起，同时希望积木堆的数量越少越好，你能帮他找到最好的方案么？
//   输入描述:
//   第一行三个整数n,k,x，1<=n<=200000，0<=x,k<=1000000000
//   第二行n个整数，表示积木的重量，任意整数范围都在[1,1000000000]
//   样例输出：
//   n = 13 k = 1 x = 38
//   arr : 20 20 80 70 70 70 420 5 1 5 1 60 90
//   输出：2
//   解释：
//   两堆分别是
//   1 1 5 5 20 20 x 60 70 70 70 80 90
//   420
//   其中x是一个任意重量的积木，夹在20和60之间可以让积木继续往上搭
public class Code01_SplitBuildingBlock {
    //i：当前处理的积木索引
    //r：剩余可使用的额外积木数量
    public static int zuo(int[] arr, int x, int i, int r) {
        if (i == arr.length - 1) {
            return 1;//这块积木单独成一堆
        }
        //当前积木和下一堆可以直接堆叠
        if (arr[i + 1] - arr[i] <= x) {
            return zuo(arr, x, i + 1, r);
        } else {
            int p1 = 1 + zuo(arr, x, i + 1, r);//当前积木为一堆，后续重新开始
            int p2 = Integer.MAX_VALUE;//使用额外积木
            int need = (arr[i + 1] - arr[i] - 1) / x;//通过除法模拟向上取整
            if (r >= need) {
                //t块积木，会分成t+1个小段
                p2 = zuo(arr, x, i + 1, r - need);
            }
            return Math.min(p1, p2);
        }
    }
    //排序 -> 统计必要拆分 -> 用额外积木减少堆数
    public static int minSplit(int[] arr, int k, int x) {
        Arrays.sort(arr);
        int n = arr.length;
        int[] needs = new int[n];//存储需要额外积木才能链接的间隙重量差
        int size = 0;//实际要处理的间隙重量
        int splits = 1;//初始的堆数
        for (int i = 1; i < n; i++) {
            if (arr[i] - arr[i - 1] > x) {
                needs[size++] = arr[i] - arr[i - 1];//记录这个间隙的重量差
                splits++;
            }
        }
        if (splits == 1 || x == 0 || k == 0) {
            return splits;
        }
        Arrays.sort(needs, 0, size);
        for (int i = 0; i < size; i++) {
            int need = (needs[i] - 1) / x;
            if (k >= need) {
                splits--;
                k -= need;
            } else {
                break;
            }
        }
        return splits;
    }
}
