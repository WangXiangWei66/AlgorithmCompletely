package Class23;

import java.util.Arrays;
import java.util.HashSet;

//定义什么是可整合数组：一个数组排完序之后，除了最左侧的数外，有arr[i] = arr[i-1]+1，则称这个数组为可整合数组
//比如{5,1,2,4,3}、{6,2,3,1,5,4}都是可整合数组，返回arr中最长可整合子数组的长度
public class Code03_LongestIntegratedLength {

    public static int maxLen(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        int N = arr.length;
        //3. 创建HashSet用于检测子数组中是否有重复元素
        HashSet<Integer> set = new HashSet<>();
        int ans = 1;//将结果变量初始化，因为最小长度便为1
        //枚举子数组的起始位置
        for (int L = 0; L < N; L++) {
            //将数组清空，来准备记录新子数组的元素
            set.clear();
            //将子数组的最大最小值初始化
            int min = arr[L];
            int max = arr[L];
            set.add(arr[L]);//将起始元素加入集合
            //下面开始枚举子数组的结束位置
            for (int R = L + 1; R < N; R++) {
                //如果元素已经在集合中了，则直接结束当前L的后续检查
                if (set.contains(arr[R])) {
                    break;
                }
                //否则将当前的元素加技进去
                set.add(arr[R]);
                //将最大最小值进行一下更新
                min = Math.min(min, arr[R]);
                max = Math.max(max, arr[R]);
                //判断索引差与下标是否相等
                if (max - min == R - L) {
                    ans = Math.max(ans, R - L + 1);
                }
            }
        }
        return ans;
    }

    //本方法的时间复杂度为O(N^3 * logN)
    public static int getLIL1(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        int len = 0;//记录最长连续子数组的长度
        //下面的方法时间复杂度为O(N^3 * logN)
        for (int start = 0; start < arr.length; start++) {
            for (int end = start; end < arr.length; end++) {
                if (isIntegrated(arr, start, end)) {
                    len = Math.max(len, end - start + 1);
                }
            }
        }
        return len;
    }

    //判断子数组是否为连续的子数组
    public static boolean isIntegrated(int[] arr, int left, int right) {
        // 1. 复制子数组到新数组（范围是[left, right]）
        int[] newArr = Arrays.copyOfRange(arr, left, right + 1);
        Arrays.sort(newArr);
        for (int i = 1; i < newArr.length; i++) {
            if (newArr[i - 1] != newArr[i] - 1) {
                return false;
            }
        }
        return true;
    }

    //本代码的时间复杂度为O(N^2)
    public static int getLIL2(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        int len = 0;//最长连续子数组长度
        int max = 0;//当前子数组的最大值
        int min = 0;//当前子数组的最小值
        //检测子数组是否有重复元素
        HashSet<Integer> set = new HashSet<>();
        //枚举子数组的起始位置
        for (int L = 0; L < arr.length; L++) {
            set.clear();
            max = Integer.MIN_VALUE;
            min = Integer.MAX_VALUE;
            for (int R = L; R < arr.length; R++) {
                if (set.contains(arr[R])) {
                    break;
                }
                set.add(arr[R]);//将当前元素加入集合
                max = Math.max(max, arr[R]);
                min = Math.min(min, arr[R]);
                if (max - min == R - L) {
                    len = Math.max(len, R - L + 1);
                }
            }
        }
        return len;
    }

    public static void main(String[] args) {
        int[] arr = {5, 5, 3, 2, 6, 4, 3};
        System.out.println(getLIL1(arr));
        System.out.println(getLIL2(arr));
    }
}
