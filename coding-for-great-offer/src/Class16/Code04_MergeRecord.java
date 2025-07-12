package Class16;

import javax.swing.plaf.TreeUI;

/*
 * 腾讯原题
 *
 * 给定整数power，给定一个数组arr，给定一个数组reverse。含义如下：
 * arr的长度一定是2的power次方，reverse中的每个值一定都在0~power范围。
 * 例如power = 2, arr = {3, 1, 4, 2}，reverse = {0, 1, 0, 2}
 * 任何一个在前的数字可以和任何一个在后的数组，构成一对数。可能是升序关系、相等关系或者降序关系。
 * 比如arr开始时有如下的降序对：(3,1)、(3,2)、(4,2)，一共3个。
 * 接下来根据reverse对arr进行调整：
 * reverse[0] = 0, 表示在arr中，划分每1(2的0次方)个数一组，然后每个小组内部逆序，那么arr变成
 * [3,1,4,2]，此时有3个逆序对。
 * reverse[1] = 1, 表示在arr中，划分每2(2的1次方)个数一组，然后每个小组内部逆序，那么arr变成
 * [1,3,2,4]，此时有1个逆序对
 * reverse[2] = 0, 表示在arr中，划分每1(2的0次方)个数一组，然后每个小组内部逆序，那么arr变成
 * [1,3,2,4]，此时有1个逆序对。
 * reverse[3] = 2, 表示在arr中，划分每4(2的2次方)个数一组，然后每个小组内部逆序，那么arr变成
 * [4,2,3,1]，此时有5个逆序对。
 * 所以返回[3,1,1,5]，表示每次调整之后的逆序对数量。
 *
 * 输入数据状况：
 * power的范围[0,20]
 * arr长度范围[1,10的7次方]
 * reverse长度范围[1,10的6次方]
 *
 * */
public class Code04_MergeRecord {
    //本代码实现了计算数组中逆序对的功能，并且通过反转特定长度的子数组来动态调整逆序对的数量
    //根据reverseArr中的每个元素，动他调整originArr并计算逆序对的数量
    public static int[] reversePair1(int[] originArr, int[] reverseArr, int power) {
        int[] ans = new int[reverseArr.length];
        for (int i = 0; i < reverseArr.length; i++) {
            reverseArray(originArr, 1 << (reverseArr[i])); //对特定长度的数组进行反转
            ans[i] = countReversePair(originArr);//计算当前数组的逆序对数量
        }
        return ans;
    }

    //将数组固定大小分组，并反转每个分组内的元素
    public static void reverseArray(int[] originArr, int teamSize) {
        if (teamSize < 2) {
            return;
        }
        for (int i = 0; i < originArr.length; i += teamSize) {
            reversePart(originArr, i, i + teamSize - 1); //对当前组内的元素进行反转
        }
    }

    //将数组从L到R的元素进行反转
    public static void reversePart(int[] arr, int L, int R) {
        while (L < R) {
            int tmp = arr[L];
            arr[L++] = arr[R];
            arr[R--] = tmp;
        }
    }

    //计算数组中逆序对的数量
    public static int countReversePair(int[] originArr) {
        int ans = 0;
        for (int i = 0; i < originArr.length; i++) {
            for (int j = i + 1; j < originArr.length; j++) {
                if (originArr[i] > originArr[j]) {
                    ans++;
                }
            }
        }
        return ans;
    }

    //下面使用了归并排序的思想来高效统计逆序对数量
    public static int[] reversePair2(int[] originArr, int[] reverseArr, int power) {
        int[] reverse = copyArray(originArr);
        reversePart(reverse, 0, reverse.length - 1);//创建原始数组的反转版本
        //分别记录原始数组和反转数组在不同规模下的逆序对数量
        int[] recordDown = new int[power + 1];
        int[] recordUp = new int[power + 1];
        //调用process方法处理原始数组和反转数组，填充记录数组
        process(originArr, 0, originArr.length - 1, power, recordDown);
        process(reverse, 0, reverse.length - 1, power, recordUp);
        int[] ans = new int[reverseArr.length];
        //下面进行交换的操作
        for (int i = 0; i < reverseArr.length; i++) {
            int curPower = reverseArr[i];
            for (int p = 1; p <= curPower; p++) {
                int tmp = recordDown[p];
                recordDown[p] = recordUp[p];
                recordUp[p] = tmp;
            }
            //将结果值进行累加
            for (int p = 1; p <= power; p++) {
                ans[i] += recordDown[p];
            }
        }
        return ans;
    }


    // originArr[L...R]完成排序！
    // L...M左  M...R右  merge
    // L...R  2的power次方
    //record用于存储不同规模的逆序对数量
    public static void process(int[] originArr, int L, int R, int power, int[] record) {
        if (L == R) {
            return;
        }
        int mid = L + ((R - L) >> 1);
        process(originArr, L, mid, power - 1, record);
        process(originArr, mid + 1, R, power - 1, record);
        //将统计的结果累加到record[power]中
        record[power] += merge(originArr, L, mid, R);
    }

    //合并两个有序子数组，并统计跨越中点的逆序对数量
    public static int merge(int[] arr, int L, int m, int r) {
        int[] help = new int[r - L + 1];
        int i = 0;
        int p1 = L;
        int p2 = m + 1;
        int ans = 0;
        while (p1 <= m && p2 <= r) {
            //如果左指针元素大于右指针元素，说明存在逆序对，数量为左半部分剩余元素的个数。
            ans += arr[p1] > arr[p2] ? (m - p1 + 1) : 0;
            help[i++] = arr[p1] <= arr[p2] ? arr[p1++] : arr[p2++];
        }

        while (p1 <= m) {
            help[i++] = arr[p1];
        }
        while (p2 <= r) {
            help[i++] = arr[p2++];
        }
        for (i = 0; i < help.length; i++) {
            arr[L + i] = help[i];
        }
        return ans;
    }

    public static int[] generateRandomOriginArr(int power, int value) {
        int[] ans = new int[1 << power];
        for (int i = 0; i < ans.length; i++) {
            ans[i] = (int) (Math.random() * value);
        }
        return ans;
    }

    public static int[] generateRandomReverseArray(int len, int power) {
        int[] ans = new int[len];
        for (int i = 0; i < ans.length; i++) {
            ans[i] = (int) (Math.random() * (power + 1));
        }
        return ans;
    }

    public static void printArray(int[] arr) {
        System.out.println("arr size :" + arr.length);
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }

    public static int[] copyArray(int[] arr) {
        if (arr == null) {
            return null;
        }
        int[] res = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            res[i] = arr[i];
        }
        return res;
    }

    public static boolean isEquals(int[] arr1, int[] arr2) {
        if ((arr1 == null && arr2 != null) || (arr1 != null && arr2 == null)) {
            return false;
        }
        if (arr1 == null && arr2 == null) {
            return true;
        }
        if (arr1.length != arr2.length) {
            return false;
        }
        for (int i = 0; i < arr1.length; i++) {
            if (arr1[i] != arr2[i]) {
                return false;
            }
        }
        return true;
    }


    public static void main(String[] args) {
        int powerMax = 8;
        int msizeMax = 10;
        int value = 30;
        int testTime = 50000;
        System.out.println("test begin!");
        for (int i = 0; i < testTime; i++) {
            int power = (int) (Math.random() * powerMax) + 1;
            int msize = (int) (Math.random() * msizeMax) + 1;
            int[] originArr = generateRandomOriginArr(power, value);
            int[] originArr1 = copyArray(originArr);
            int[] originArr2 = copyArray(originArr);
            int[] reverseArr = generateRandomReverseArray(msize, power);
            int[] reverseArr1 = copyArray(reverseArr);
            int[] reverseArr2 = copyArray(reverseArr);
            int[] ans1 = reversePair1(originArr1, reverseArr1, power);
            int[] ans2 = reversePair2(originArr2, reverseArr2, power);
            if (!isEquals(ans1, ans2)) {
                System.out.println("Oops!");
            }
        }
        System.out.println("test finish");


        powerMax = 20;
        msizeMax = 1000000;
        value = 1000;
        int[] originArr = generateRandomOriginArr(powerMax, value);
        int[] reverseArr = generateRandomReverseArray(msizeMax, powerMax);
        long start = System.currentTimeMillis();
        reversePair2(originArr, reverseArr, powerMax);
        long end = System.currentTimeMillis();
        System.out.println("run time :" + (end - start) + "ms");
    }
}
