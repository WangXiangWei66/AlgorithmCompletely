package Class34;

//给你一个整数数组nums，将它重新排列成nums[0] < nums[1] > nums[2] < nums[3]...的顺序。
//你可以假设所有输入数组都可以得到满足题目要求的结果。
//示例 1：
//输入：nums = [1,5,1,1,6,4]
//输出：[1,6,1,5,1,4]
//解释：[1,4,1,5,1,6] 同样是符合题目要求的结果，可以被判题程序接受。
//示例 2：
//输入：nums = [1,3,2,2,3,1]
//输出：[2,3,1,3,1,2]
//进阶：你能用O(n) 时间复杂度和原地 O(1) 额外空间来实现吗？
//Leetcode题目 : https://leetcode.com/problems/wiggle-sort-ii/
public class Problem_0324_WiggleSortII {
    //核心思路：寻找中位数+分割数组+特殊洗牌操作
    public static void wiggleSort(int[] nums) {
        if (nums == null || nums.length < 2) {
            return;
        }
        int N = nums.length;
        //找到数组的中位数并将数组分割为两部分
        findIndexNum(nums, 0, nums.length - 1, N / 2);
        //根据奇偶性，执行不同的洗牌操作
        if ((N & 1) == 0) {
            shuffle(nums, 0, nums.length - 1);
            reverse(nums, 0, nums.length - 1);
        } else {
            shuffle(nums, 1, nums.length - 1);
        }
    }

    public static int findIndexNum(int[] arr, int L, int R, int index) {
        int pivot = 0;
        int[] range = null;
        while (L < R) {
            pivot = arr[L + (int) (Math.random() * (R - L + 1))];
            range = partition(arr, L, R, pivot);
            if (index >= range[0] && index <= range[1]) {
                return arr[index];
            } else if (index < range[0]) {
                R = range[0] - 1;
            } else {
                L = range[1] + 1;
            }
        }
        return arr[L];
    }

    public static int[] partition(int[] arr, int L, int R, int pivot) {
        int less = L - 1;
        int more = R + 1;
        int cur = L;
        while (cur < more) {
            if (arr[cur] < pivot) {
                swap(arr, ++less, cur++);
            } else if (arr[cur] > pivot) {
                swap(arr, cur, --more);
            } else {
                cur++;
            }
        }
        return new int[]{less + 1, more - 1};
    }

    public static void shuffle(int[] nums, int l, int r) {
        while (r - l + 1 > 0) {
            int lenAndOne = r - l + 2;//子数组长度+1是为了寻找合适的“三进制分段长度”
            int bloom = 3;
            int k = 1;//用来计算3的倍数
            //寻找最大的3的k次方
            while (bloom <= lenAndOne / 3) {
                bloom *= 3;
                k++;
            }
            int m = (bloom - 1) / 2;
            int mid = (l + r) / 2;
            rotate(nums, l + m, mid, mid + m);//实现左右两部分的交换
            cycles(nums, l - 1, bloom, k);
            l = l + bloom - 1;//移动左边界，处理未剩余的子数组
        }
    }

    //循环移位来打乱元素
    public static void cycles(int[] nums, int base, int bloom, int k) {
        //i控制循环轮次，trigger是三进制的起始触发点
        for (int i = 0, trigger = 1; i < k; i++, trigger *= 3) {
            //计算下一个位置
            int next = (2 * trigger) % bloom;
            int cur = next;
            int record = nums[next + base];
            int tmp = 0;
            //将trigger位置的元素放到next位置
            nums[next + base] = nums[trigger + base];
            //循环移位，直到回到trigger位置，完成一个闭环
            while (cur != trigger) {
                next = (2 * cur) % bloom;//计算下一个位置
                tmp = nums[next + base];//暂存下一个位置的元素
                nums[next + base] = record;//将当前记录的元素放在next位置
                cur = next;//移动到下一个元素
                record = tmp;//更新记录的元素
            }
        }
    }

    public static void rotate(int[] arr, int l, int m, int r) {
        reverse(arr, l, m);
        reverse(arr, m + 1, r);
        reverse(arr, l, r);
    }

    //左右指针向中间移动，交换元素
    public static void reverse(int[] arr, int l, int r) {
        while (l < r) {
            swap(arr, l++, r--);
        }
    }

    public static void swap(int[] nums, int i, int j) {
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }


    public static boolean test(int[] arr) {
        return process(arr, 0);
    }

    //使用回溯的方法生成数组的所有可能排列
    public static boolean process(int[] arr, int index) {
        if (index == arr.length) {
            return valid(arr);
        }
        //尝试将index位置的元素与后续所有的位置进行交换
        for (int i = index; i < arr.length; i++) {
            swap(arr, index, i);
            if (process(arr, index + 1)) {
                return true;
            }
            swap(arr, index, i);//回溯
        }
        return false;
    }

    public static boolean valid(int[] arr) {
        boolean more = true;//标记当前需要满足的关系：true表示“前 < 后”，false表示“前 > 后”
        //检查当前相邻元素是否违反摆动规则：
        //若需要“前 < 后”（more=true），但实际“前 >= 后” → 无效
        //若需要“前 > 后”（more=false），但实际“前 <= 后” → 无效
        for (int i = 1; i < arr.length; i++) {
            if ((more && arr[i - 1] >= arr[i]) || (!more && arr[i - 1] <= arr[i])) {
                return false;
            }
            more = !more;//切换关系
        }
        return true;
    }


    public static int[] randomArray(int n, int v) {
        int[] ans = new int[n];
        for (int i = 0; i < n; i++) {
            ans[i] = (int) (Math.random() * v);
        }
        return ans;
    }

    public static int[] copyArray(int[] arr) {
        int[] ans = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            ans[i] = arr[i];
        }
        return ans;
    }

    public static void main(String[] args) {
        int N = 10;
        int V = 10;
        int testTime = 1000;
        System.out.println("test begin");
        for (int i = 0; i < testTime; i++) {
            int n = (int) (Math.random() * N) + 1;
            int[] arr1 = randomArray(n, V);
            int[] arr2 = copyArray(arr1);
            wiggleSort(arr1);
            if (valid(arr1) != test(arr2)) {
                System.out.println("Oops");
            }
        }
        System.out.println("test end");
    }
}
