package Class24;

import java.util.HashMap;
import java.util.HashSet;

//给定一个正数数组arr，长度一定大于6（>=7），一定要选3个数字做分割点，从而分出4个部分，并且每部分都有数
//分割点的数字直接删除，不属于任何4个部分中的任何一个。返回有没有可能分出的4个部分累加和一样大
//如：{3,2,3,7,4,4,3,1,1,6,7,1,5,2}。可以分成{3,2,3}、{4,4}、{1,1,6}、{1,5,2}。分割点是不算的！
public class Code01_Split4Parts {
    //时间复杂度为O(N),空间复杂度为O(n)
    public static boolean canSplits1(int[] arr) {
        if (arr == null || arr.length < 7) {
            return false;
        }
        //用哈希集合来存储中间计算结果
        HashSet<String> set = new HashSet<String>();
        int sum = 0;//计算所有数组的元素总和
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
        }
        //计算左侧累加和，并将可能的分割点信息存入集合
        int leftSum = arr[0];
        for (int i = 1; i < arr.length - 1; i++) {
            //存储"左侧和_右侧和"的字符串表示
            set.add(String.valueOf(leftSum) + "_" + String.valueOf(sum - leftSum - arr[i]));
            leftSum += arr[i];
        }
        //初始化左右指针和对应的累加和
        int l = 1;//左指针
        int lSum = arr[0];//左侧累加和
        int r = arr.length - 2;//右指针
        int rSum = arr[arr.length - 1];//右侧累加和
        //移动指针来寻找可能的分割
        while (l < r - 3) {// 确保中间有足够空间容纳剩余部分
            if (lSum == rSum) {
                //当左右累加和相等时，检查是否存在符合条件的中间分割
                String lKey = String.valueOf(lSum * 2 + arr[l]);
                String rKey = String.valueOf(rSum * 2 + arr[r]);
                if (set.contains(lKey + "_" + rKey)) {
                    return true;
                }
                // 移动左指针并更新左侧累加和
                lSum += arr[l++];
            } else if (lSum < rSum) {
                lSum += arr[l++];
            } else {
                rSum += arr[r--];
            }
        }
        return false;
    }

    public static boolean canSplits2(int[] arr) {
        if (arr == null || arr.length < 7) {
            return false;
        }
        //存储累加和与对应索引
        HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
        int sum = arr[0];
        for (int i = 1; i < arr.length; i++) {
            map.put(sum, i);
            sum += arr[i];
        }
        // 左侧第一个部分的和，初始为arr[0]
        int lSum = arr[0];
        //遍历第一个点的可能位置s1
        for (int s1 = 1; s1 < arr.length - 5; s1++) {
            //计算检查和：第一个部分和*2 + 第一个分割点的值
            int checkSum = lSum * 2 + arr[s1];
            if (map.containsKey(checkSum)) {//检查这个分割点是否存在
                int s2 = map.get(checkSum);
                checkSum += lSum + arr[s2];// 计算下一个检查和：加上第二个部分的和（lSum）和第二个分割点的值
                if (map.containsKey(checkSum)) {//继续检查分割点
                    int s3 = map.get(checkSum);
                    //判断第四个累加和也是Sum
                    if (checkSum + arr[s3] + lSum == sum) {
                        return true;
                    }
                }
            }
            //更新左侧第一个部分的和（包含当前元素s1，因为s1向右移动了）
            lSum += arr[s1];
        }
        return false;
    }

    public static int[] generateRandomArray() {
        int[] res = new int[(int) (Math.random() * 10) + 7];
        for (int i = 0; i < res.length; i++) {
            res[i] = (int) (Math.random() * 10) + 1;
        }
        return res;
    }

    public static void main(String[] args) {
        int testTime = 300000;
        for (int i = 0; i < testTime; i++) {
            int[] arr = generateRandomArray();
            if (canSplits1(arr) ^ canSplits2(arr)) {
                System.out.println("Error");
            }
        }
    }
}
