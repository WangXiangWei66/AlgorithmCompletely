package Class22;

import java.util.HashSet;
import java.util.Stack;
import java.util.function.IntPredicate;

//一个不含有负数的数组可以代表一圈环形山，每个位置的值代表山的高度
//比如， {3,1,2,4,5}、{4,5,3,1,2}或{1,2,4,5,3}都代表同样结构的环形山
//山峰A和山峰B能够相互看见的条件为:
//1.如果A和B是同一座山，认为不能相互看见
//2.如果A和B是不同的山，并且在环中相邻，认为可以相互看见
//3.如果A和B是不同的山，并且在环中不相邻，假设两座山高度的最小值为min
//1)如果A通过顺时针方向到B的途中没有高度比min大的山峰，认为A和B可以相互看见
//2)如果A通过逆时针方向到B的途中没有高度比min大的山峰，认为A和B可以相互看见
//两个方向只要有一个能看见，就算A和B可以相互看见
//给定一个不含有负数且没有重复值的数组 arr，请返回有多少对山峰能够相互看见
//进阶，给定一个不含有负数但可能含有重复值的数组arr，返回有多少对山峰能够相互看见
public class Code04_VisibleMountains {
    //本方法使用单调栈，将时间复杂度设为了O(N)
    //栈中放的记录
    //value就是值，times是收集的个数
    public static class Record {
        public int value;//存储数组元素的值
        public int times;//记录该值出现的次数

        public Record(int value) {
            this.value = value;
            this.times = 1;//初始化次数为1
        }
    }

    public static int getVisibleNum(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;
        }
        int N = arr.length;
        int maxIndex = 0;//记录最大值的索引
        // 找到数组中最大值的索引（如果有多个最大值，取第一个
        for (int i = 0; i < N; i++) {
            maxIndex = arr[maxIndex] < arr[i] ? i : maxIndex;
        }
        // 创建栈并将最大值入栈，作为遍历的起点
        Stack<Record> stack = new Stack<Record>();
        stack.push(new Record(arr[maxIndex]));
        //计算下一个数组（环形数组）
        int index = nextIndex(maxIndex, N);
        int res = 0;//存储结果
        //遍历数组，直到回到maxIndex
        while (index != maxIndex) {
            // 当栈顶元素小于当前元素时，弹出栈顶并计算可见对数
            while (stack.peek().value < arr[index]) {
                int k = stack.pop().times;
                // 内部可见对数 + 与当前元素形成的可见对数
                res += getInternalSum(k) + 2 * k;
            }
            // 如果栈顶元素等于当前元素，次数加1
            if (stack.peek().value == arr[index]) {
                stack.peek().times++;
            } else {
                //否则直接将元素加入栈
                stack.push(new Record(arr[index]));
            }
            //移动到下一个索引
            index = nextIndex(index, N);
        }
        //开始处理栈中剩余的元素
        while (stack.size() > 2) {
            int times = stack.pop().times;
            res += getInternalSum(times) + 2 * times;//当前元素内部的可见对数+当前弹出元素与前后元素形成的可见对数
        }

        if (stack.size() == 2) {
            int times = stack.pop().times;
            // 如果栈顶只剩一个元素，加times，否则加2*times
            res += getInternalSum(times) + (stack.peek().times == 1 ? times : 2 * times);
        }
        //处理最后一个元素的内部可见对数
        res += getInternalSum(stack.pop().times);
        return res;
    }

    //计算k个相同元素内部的可见对数
    public static int getInternalSum(int k) {
        return k == 1 ? 0 : (k * (k - 1) / 2);
    }

    //计算环形数组的下一个索引（核心让数组首尾相连）
    public static int nextIndex(int i, int size) {
        return i < (size - 1) ? (i + 1) : 0;
    }

    //计算环形数组的上一个索引
    public static int lastIndex(int i, int size) {
        return i > 0 ? (i - 1) : (size - 1);
    }

    //时间复杂度为O(N ^ 3)
    public static int rightWay(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;
        }
        int res = 0;
        // 用于记录相同元素间已计算过的可见对，避免重复计数
        HashSet<String> equalCounted = new HashSet<>();
        // 遍历每个元素，计算从该元素出发能看到的山峰数量
        for (int i = 0; i < arr.length; i++) {
            res += getVisibleNumFromIndex(arr, i, equalCounted);
        }
        return res;
    }

    public static int getVisibleNumFromIndex(int[] arr, int index, HashSet<String> equalCounted) {
        int res = 0;
        // 遍历数组中其他所有元素，检查是否可见
        for (int i = 0; i < arr.length; i++) {
            if (i != index) {//首先排除自身
                //如果与当前元素值相等
                if (arr[i] == arr[index]) {
                    // 生成唯一标识（小索引_大索引），确保相同对只被计算一次
                    String key = Math.min(index, i) + "_" + Math.max(index, i);
                    // 如果是首次记录且两个元素可见，则计数+1
                    if (equalCounted.add(key) && isVisible(arr, index, i)) {
                        res++;
                    }
                    //处理值不相等的情况，直接判断是否可见
                } else if (isVisible(arr, index, i)) {
                    res++;
                }
            }
        }
        return res;
    }


    public static boolean isVisible(int[] arr, int lowIndex, int highIndex) {
        // 如果起点元素值大于目标元素值，不可见（题目定义：只有低的能看见高的，高的不能看见低的）
        if (arr[lowIndex] > arr[highIndex]) {
            return false;
        }
        int size = arr.length;
        //检查顺时针方向是否可见
        boolean walkNext = true;
        int mid = nextIndex(lowIndex, size);//从下一个索引开始
        while (mid != highIndex) {
            // 如果中间有比起点更高的元素，遮挡视线，顺时针方向不可见
            if (arr[mid] > arr[lowIndex]) {
                walkNext = false;
                break;
            }
            mid = nextIndex(mid, size);//继续顺时针移动
        }
        //检查逆时针方向是否可见
        boolean walkLast = true;
        mid = lastIndex(lowIndex, size);//从上一个索引开始
        while (mid != highIndex) {
            // 如果中间有比起点更高的元素，遮挡视线，逆时针方向不可见
            if (arr[mid] > arr[lowIndex]) {
                walkLast = false;
                break;
            }
            mid = lastIndex(mid, size);//继续逆时针移动
        }
        // 两个方向中至少一个方向可见，即为可见
        return walkNext || walkLast;
    }

    public static int[] getRandomArray(int size, int max) {
        int[] arr = new int[(int) (Math.random() * size)];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) (Math.random() * max);
        }
        return arr;
    }

    public static void printArray(int[] arr) {
        if (arr == null || arr.length == 0) {
            return;
        }
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + "");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        int size = 10;
        int max = 10;
        int testTime = 200000;
        System.out.println("test begin!");
        for (int i = 0; i < testTime; i++) {
            int[] arr = getRandomArray(size, max);
            if (rightWay(arr) != getVisibleNum(arr)) {
                printArray(arr);
                System.out.println(rightWay(arr));
                System.out.println(getVisibleNum(arr));
                break;
            }
        }
        System.out.println("test end");
    }
}
