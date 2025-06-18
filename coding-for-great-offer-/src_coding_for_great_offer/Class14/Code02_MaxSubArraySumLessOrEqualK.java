package Class14;

import java.util.TreeSet;

public class Code02_MaxSubArraySumLessOrEqualK {

    public static int getMaxLessOrEqualK(int[] arr, int K) {
        TreeSet<Integer> set = new TreeSet<Integer>();
        set.add(0);
        int max = Integer.MIN_VALUE;
        int sum = 0;//一个数也没有的时候，要把前缀和设为0
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
            if (set.ceiling(sum - K) != null) {
                max = Math.max(max, sum - set.ceiling(sum - K));
            }
            set.add(sum);
        }
        return max;
    }
}
