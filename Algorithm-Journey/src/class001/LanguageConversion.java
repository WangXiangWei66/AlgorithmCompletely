package class001;

public class LanguageConversion {

    class Solution {
        public static int[] sortArray(int[] nums) {
            if (nums.length > 1) {
                mergeSort(nums);
            }
            return nums;
        }

        public static int MAXN = 50001;

        public static int[] help = new int[MAXN];

        public static void mergeSort(int[] arr) {
            int n = arr.length;
            //step:当前归并的子数组的长度
            for (int l, m, r, step = 1; step < n; step <<= 1) {
                l = 0;
                while (l < n) {
                    m = l + step - 1;
                    if (m + 1 >= n) {
                        break;
                    }
                    r = Math.min(l + (step << 1) - 1, n - 1);
                    merge(arr, l, m, r);
                    l = r + 1;
                }
            }
        }

        public static void merge(int[] nums, int l, int m, int r) {
            int p1 = l;
            int p2 = m + 1;
            int i = l;
            while (p1 <= m && p2 <= r) {
                help[i++] = nums[p1] <= nums[p2] ? nums[p1++] : nums[p2++];
            }
            while (p1 <= m) {
                help[i++] = nums[p1++];
            }
            while (p2 <= r) {
                help[i++] = nums[p2++];
            }
            for (i = l; i <= r; i++) {
                nums[i] = help[i];
            }
        }

        public static void quickSort(int[] arr) {
            sort(arr, 0, arr.length - 1);
        }

        public static void sort(int[] arr, int l, int r) {
            if (l >= r) {
                return;
            }
            int x = arr[l + (int) (Math.random() * (r - l + 1))];
            partition(arr, l, r, x);
            int left = first;
            int right = last;
            sort(arr, l, left - 1);
            sort(arr, right + 1, r);
        }

        public static int first, last;

        public static void partition(int[] nums, int l, int r, int x) {
            first = l;
            last = r;
            int i = l;
            while (i <= last) {
                if (nums[i] == x) {
                    i++;
                } else if (nums[i] < x) {
                    swap(nums, first++, i++);
                } else {
                    swap(nums, i, last--);
                }
            }
        }

        public static void swap(int[] arr, int i, int j) {
            int tmp = arr[i];
            arr[i] = arr[j];
            arr[j] = tmp;
        }

        public static void heapSort(int[] nums) {
            int n = nums.length;
            //将数组构建成大根堆
            for (int i = n - 1; i >= 0; i--) {
                heapify(nums, i, n);
            }
            while (n > 1) {
                swap(nums, 0, --n);
                heapify(nums, 0, n);
            }
        }

        public static void heapInsert(int[] nums, int i) {
            while (nums[i] > nums[(i - 1) / 2]) {
                swap(nums, i, (i - 1) / 2);
                i = (i - 1) / 2;
            }
        }

        public static void heapify(int[] nums, int i, int s) {
            int l = i * 2 + 1;
            while (l < s) {
                //找到左右子节点中较大的那个
                int best = l + 1 < s && nums[l + 1] > nums[l] ? l + 1 : l;
                best = nums[best] > nums[i] ? best : i;
                if (best == i) {
                    break;
                }
                swap(nums, best, i);
                i = best;//移动到子节点位置
                l = i * 2 + 1;
            }
        }
    }
}
