package Class39;

//来自腾讯
//给定一个长度为n的数组arr，求有多少个子数组满足:
//子数组两端的值，是这个子数组的最小值和次小值，最小值和次小值谁在最左和最右无所谓
//n<=100000
public class Code02_ValidSequence {

    public static int nums(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;
        }
        int n = arr.length;
        //单调栈相关变量（模拟栈结构，避免使用Stack类来提高效率）
        int[] values = new int[n];//存储栈中元素的值，严格单调递增
        int[] times = new int[n];//存储栈中相同值连续出现的次数
        int size = 0;//栈中的当前大小
        int ans = 0;//累积有效子数组的数量
        //从左往右遍历，统计最小值在左，次小值在右的情况
        for (int i = 0; i < arr.length; i++) {
            while (size != 0 && values[size - 1] > arr[i]) {
                size--;
                ans += times[size] + cn2(times[size]);
            }
            if (size != 0 && values[size - 1] == arr[i]) {
                times[size - 1]++;
            } else {
                values[size] = arr[i];
                times[size++] = 1;
            }
        }
        //处理栈中剩余元素（计算相同元素间的有效子数组）
        while (size != 0) {
            ans += cn2(times[--size]);
        }
        //从右往左遍历，与统计次小值在左，最小值在右
        for (int i = arr.length - 1; i >= 0; i--) {
            while (size != 0 && values[size - 1] > arr[i]) {
                ans += times[--size];
            }
            if (size != 0 && values[size - 1] == arr[i]) {
                times[size - 1]++;
            } else {
                values[size] = arr[i];
                times[size++] = 1;
            }
        }
        return ans;
    }

    public static int cn2(int n) {
        return (n * (n - 1)) >> 1;
    }

    public static int test(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;
        }
        int ans = 0;
        for (int s = 0; s < arr.length; s++) {
            for (int e = s + 1; e < arr.length; e++) {
                int max = Math.max(arr[s], arr[e]);//数组两端值的最大值
                boolean valid = true;
                for (int i = s + 1; i < e; i++) {
                    if (arr[i] < max) {
                        valid = false;
                        break;
                    }
                }
                ans += valid ? 1 : 0;
            }
        }
        return ans;
    }

    public static int[] randomArray(int n, int v) {
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = (int) (Math.random() * v);
        }
        return arr;
    }

    public static void printArray(int[] arr) {
        for (int num : arr) {
            System.out.print(num + "");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        int n = 30;
        int v = 30;
        int testTime = 100000;
        System.out.println("test begin!");
        for (int i = 0; i < testTime; i++) {
            int m = (int) (Math.random() * n);
            int[] arr = randomArray(m, v);
            int ans1 = nums(arr);
            int ans2 = test(arr);
            if (ans1 != ans2) {
                System.out.println("Oops");
                printArray(arr);
                System.out.println(ans1);
                System.out.println(ans2);
                break;
            }
        }
        System.out.println("test end!");
    }
}
