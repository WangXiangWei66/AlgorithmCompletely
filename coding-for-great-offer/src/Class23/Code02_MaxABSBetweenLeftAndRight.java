package Class23;

//给定一个数组arr，长度为N > 1，从中间切一刀，保证左部分和右部分都有数字，一共有N-1种切法
//如此多的切法中，每一种都有:绝对值(左部分最大值 – 右部分最大值)，返回最大的绝对值是多少
public class Code02_MaxABSBetweenLeftAndRight {
    //时间复杂度为O(N^2)
    public static int maxABS1(int[] arr) {
        int res = Integer.MIN_VALUE;//用来记录最大的绝对值
        //声明左右最大值的变量
        int maxLeft = 0;
        int maxRight = 0;
        // 3. 遍历数组中的每个位置i，将数组分为[0..i]和[i+1..n-1]两部分
        for (int i = 0; i != arr.length - 1; i++) {
            //先将左侧最大值重置
            maxLeft = Integer.MIN_VALUE;
            // 5. 计算[0..i]范围内的最大值
            for (int j = 0; j != i + 1; j++) {
                maxLeft = Math.max(arr[j], maxLeft);
            }
            //记录右侧的最大值
            maxRight = Integer.MIN_VALUE;
            // 5. 计算[0..i]范围内的最大值
            for (int j = i + 1; j != arr.length; j++) {
                maxRight = Math.max(arr[j], maxRight);
            }
            //计算当前分割方式下的绝对值，并更新全局最大值
            res = Math.max(Math.abs(maxLeft - maxRight), res);
        }
        return res;//将最终结果返回
    }

    //时间复杂度为O(N)
    //通过预处理减少了重复计算
    public static int maxABS2(int[] arr) {
        // 1. 创建两个辅助数组，分别存储左侧最大值和右侧最大值
        int[] lArr = new int[arr.length];// lArr[i]表示[0..i]范围内的最大值
        int[] rArr = new int[arr.length]; // rArr[i]表示[i..n-1]范围内的最大值
        //初始化左侧最大值为第一个元素
        lArr[0] = arr[0];
        //初始化右侧最大值为最后一个元素
        rArr[arr.length - 1] = arr[arr.length - 1];
        //填充左侧最大值数组
        for (int i = 1; i < arr.length; i++) {
            // 当前位置的最大值 = 前一位置的最大值 与 当前元素 的较大者
            lArr[i] = Math.max(lArr[i - 1], arr[i]);
        }
        for (int i = arr.length - 2; i > -1; i--) {
            rArr[i] = Math.max(rArr[i + 1], arr[i]);
        }
        //将结果变量初始化
        int max = 0;
        //计算所有分割方式的最大值
        for (int i = 0; i < arr.length - 1; i++) {
            max = Math.max(max, Math.abs(lArr[i] - rArr[i + 1]));
        }
        return max;
    }

    //时间复杂度和空间复杂度都是O(1)
    public static int maxABS3(int[] arr) {
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < arr.length; i++) {
            max = Math.max(arr[i], max);
        }
        return max - Math.min(arr[0], arr[arr.length - 1]);
    }

    public static int[] generateRandomArr(int length) {
        int[] arr = new int[length];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) (Math.random() * 1000) - 499;
        }
        return arr;
    }

    public static void main(String[] args) {
        int[] arr = generateRandomArr(200);
        System.out.println(maxABS1(arr));
        System.out.println(maxABS2(arr));
        System.out.println(maxABS3(arr));
    }
}
