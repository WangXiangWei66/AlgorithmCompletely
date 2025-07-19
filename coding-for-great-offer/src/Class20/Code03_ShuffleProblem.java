package Class20;

import java.util.Arrays;

//完美洗牌问题
//给定一个长度为偶数的数组arr，假设长度为N*2
//左部分：arr[L1...Ln] 右部分： arr[R1...Rn]
//请把arr调整成arr[L1,R1,L2,R2,L3,R3,...,Ln,Rn]
//要求时间复杂度O(N)，额外空间复杂度O(1)
public class Code03_ShuffleProblem {

    // 数组的长度为len，调整前的位置是i，返回调整之后的位置
    // 下标不从0开始，从1开始
    public static int modifyIndex1(int i, int len) {
        if (i <= len / 2) {
            //将前半部分的索引映射到偶数的位置
            return 2 * i;
        } else {
            //将后半部分映射到奇数的位置
            return 2 * (i - (len / 2)) - 1;
        }
    }

    // 数组的长度为len，调整前的位置是i，返回调整之后的位置
    // 下标不从0开始，从1开始
    public static int modifyIndex2(int i, int len) {
        return (2 * i) % (len + 1);// len 是 2^k - 1 形式
    }

    // 主函数
    // 数组必须不为空，且长度为偶数
    public static void shuffle(int[] arr) {
        if (arr != null && arr.length != 0 && (arr.length & 1) == 0) {
            shuffle(arr, 0, arr.length - 1);
        }
    }

    // 在arr[L..R]上做完美洗牌的调整（arr[L..R]范围上一定要是偶数个数字）
    public static void shuffle(int[] arr, int L, int R) {
        while (R - L + 1 > 0) {// 切成一块一块的解决，每一块的长度满足(3^k)-1
            int len = R - L + 1;//当前快的长度
            int base = 3;
            int k = 1;
            // 计算最大的k，使得3^k <= len+1
            while (base <= (len + 1) / 3) {
                base *= 3;
                k++;
            }
            int half = (base - 1) / 2;//当前快的一半长度
            int mid = (L + R) / 2;//原数组的中点
            rotate(arr, L + half, mid, mid + half);//将 [L+half..mid] 和 [mid+1..mid+half] 两部分交换。
            cycles(arr, L, base - 1, k);
            L = L + base - 1; // 处理剩余元素
        }
    }

    // 从start位置开始，往右len的长度这一段，做下标连续推
    // 出发位置依次为1,3,9...
    public static void cycles(int[] arr, int start, int len, int k) {
        // 1. 获取触发点的初始值（注意下标转换：trigger是从1开始的位置，需转换为数组下标）
        for (int i = 0, trigger = 1; i < k; i++, trigger *= 3) {
            int preValue = arr[trigger + start - 1];//arr中的操作是使他转化为数组下标
            int cur = modifyIndex2(trigger, len);//计算下一个位置的触发点
            //沿着循环连交换元素
            while (cur != trigger) {
                //循环交换元素
                int tmp = arr[cur + start - 1];
                arr[cur + start - 1] = preValue;
                preValue = tmp;
                cur = modifyIndex2(cur, len);
            }
            // 3. 闭合循环，完成最后一次交换
            arr[cur + start - 1] = preValue;
        }
    }

    // 把数组中两个相邻区间的位置交换
    public static void rotate(int[] arr, int L, int M, int R) {
        reverse(arr, L, M);//将左区间反转
        reverse(arr, M + 1, R);//将右区间反转
        reverse(arr, L, R);//将数组整体反转
    }

    public static void reverse(int[] arr, int L, int R) {
        while (L < R) {
            int tmp = arr[L];
            arr[L++] = arr[R];
            arr[R--] = tmp;
        }
    }

    public static void wiggleSort(int[] arr) {
        if (arr == null || arr.length == 0) {
            return;
        }
        // 假设这个排序是额外空间复杂度O(1)的，当然系统提供的排序并不是，你可以自己实现一个堆排序
        Arrays.sort(arr);
        if ((arr.length & 1) == 1) {
            shuffle(arr, 1, arr.length - 1);
        } else {
            shuffle(arr, 0, arr.length - 1);
            for (int i = 0; i < arr.length; i += 2) {
                int tmp = arr[i];
                arr[i] = arr[i + 1];
                arr[i + 1] = tmp;
            }
        }
    }

    public static boolean isValidWiggle(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            if ((i & 1) == 1 && arr[i] < arr[i - 1]) {//奇数的时候，前面的数要大
                return false;
            }
            if ((i & 1) == 0 && arr[i] > arr[i - 1]) {//偶数的时候，前面的数要小
                return false;
            }
        }
        return true;
    }

    public static void printArray(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }

    public static int[] generateArray() {
        int len = (int) (Math.random() * 10) * 2;
        int[] arr = new int[len];
        for (int i = 0; i < len; i++) {
            arr[i] = (int) (Math.random() * 100);
        }
        return arr;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 500000; i++) {
            int[] arr = generateArray();
            wiggleSort(arr);
            if (!isValidWiggle(arr)) {
                System.out.println("Oops");
                printArray(arr);
                break;
            }
        }
    }
}
