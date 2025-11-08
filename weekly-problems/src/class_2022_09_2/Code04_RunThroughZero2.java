package class_2022_09_2;

//来自学员问题
//有一个数组包含0、1、2三种值，
//有m次修改机会，第一种将所有连通的1变为0，修改次数-1
//第二种将所有连通的2变为1或0，修改次数-2，
//返回m次修改机会的情况下，让最大的0连通区，最长能是多少？
//1 <= arr长度 <= 10^6
//0 <= 修改机会 <= 10^6
public class Code04_RunThroughZero2 {

    public static int maxZero1(int[] arr, int k) {
        int n = arr.length;
        int ans = 0;
        for (int i = 0; i < n; i++) {
            for (int j = n - 1; j >= i; j--) {
                //计算子数组i,j的修改成本
                if (cost1(arr, i, j) <= k) {
                    ans = Math.max(ans, j - i + 1);
                    //反向遍历,找到了最大的满足条件
                    break;
                }
            }
        }
        return ans;
    }

    public static int cost1(int[] arr, int l, int r) {
        int num0 = 0;
        int num2 = 0;
        //子数组的长度
        int n = r - l + 1;
        for (int i = l; i <= r; i++) {
            num0 += arr[i] == 0 ? 1 : 0;
            num2 += arr[i] == 2 ? 1 : 0;
        }
        if (num0 == n) {
            return 0;
        }
        if (num2 == n) {
            return 2;
        }
        //2连通块的数量
        int area2 = arr[l] == 2 ? 1 : 0; // 左边界是2，初始化1个2连通块
        //寻找2连通块的个数
        for (int i = l; i < r; i++) {
            if (arr[i] != 2 && arr[i + 1] == 2) {
                area2++;
            }
        }
        //统一1连通块的数量
        boolean has1 = false;
        int areaHas1No0 = 0;
        for (int i = l; i <= r; i++) {
            if (arr[i] == 0) {
                if (has1) {
                    areaHas1No0++;
                }
                has1 = false;
            }
            if (arr[i] == 1) {
                has1 = true;
            }
        }
        if (has1) {
            areaHas1No0++;
        }
        return 2 * area2 + areaHas1No0;
    }
    //预处理数组:提前计算每个位置的关键信息
    //left10[i]	位置 i 左侧（含i）最近的 0 的索引（仅用于 1 的连通块计算）
    public static int[] left10 = new int[1000001];
    //left2x[i]	位置 i 左侧（含i）最近的 “非 2 元素”（0 或 1）的索引；仅用于 2 的连通块计算
    public static int[] left2x = new int[1000001];
    //位置i右侧
    public static int[] right10 = new int[1000001];
    public static int[] right2x = new int[1000001];
    //从数组开头到位置i的2连通的数量
    public static int[] area2s = new int[1000001];
    //从数组开头到i的1连通的数量
    public static int[] area1s = new int[1000001];

    public static int maxZero2(int[] arr, int k) {
        //六个循环预处理数组初始化
        int n = arr.length;
        int last = -1;//记录最近0的位置
        for (int i = 0; i < n; i++) {
            if (arr[i] == 0) {
                last = i;
            }
            if (arr[i] == 1) {
                left10[i] = last;
            }
        }
        //记录最近非2元素
        last = -1;
        for (int i = 0; i < n; i++) {
            if (arr[i] != 2) {
                last = i;
            }
            if (arr[i] == 2) {
                left2x[i] = last;
            }
        }
        //右侧最近0的索引
        last = n;
        for (int i = n - 1; i >= 0; i--) {
            if (arr[i] == 0) {
                last = i;
            }
            if (arr[i] == 1) {
                right10[i] = last;
            }
        }
        //2右侧最近非2元素的索引
        last = n;
        for (int i = n - 1; i >= 0; i--) {
            if (arr[i] != 2) {
                last = i;
            }
            if (arr[i] == 2) {
                right2x[i] = last;
            }
        }
        //2连通块数量前缀和
        int area2 = arr[0] == 2 ? 1 : 0;
        for (int i = 0; i < n - 1; i++) {
            if (arr[i] != 2) {
                //仅非2元素位置记录当前2连通总数
                area2s[i] = area2;
                if (arr[i + 1] == 2) {
                    area2++;
                }
            }
        }
        if (arr[n - 1] != 2) {
            area2s[n - 1] = area2;
        }
        //1连通块数量前缀和
        boolean has1 = false;
        //1连通块的总数
        int area1 = 0;
        for (int i = 0; i < n; i++) {
            if (arr[i] == 0) {
                if (has1) {
                    area1++;
                }
                has1 = false;
                // 仅0的位置记录当前1连通块总数,0是1连通块的分隔符
                area1s[i] = area1;
            }
            if (arr[i] == 1) {
                has1 = true;
            }
        }
        //滑动窗口,双指针求最大长度
        int ans = 0;
        int right = 0;
        for (int left = 0; left < n; left++) {
            //扩展右边界
            while (right < n && cost2(arr, left, right) <= k) {
                right++;
            }
            ans = Math.max(ans, right - left);
            //左边界右移,右边界至少在left+1位置
            right = Math.max(right, left + 1);
        }
        return ans;
    }
    //利用预处理数组,快速计算子数组的修改成本
    public static int cost2(int[] arr, int left, int right) {
        //左边界是2，且其右侧最近非2元素超出当前right → 整个子数组是2连通块，成本2
        if (arr[left] == 2 && right2x[left] > right) {
            return 2;
        }
        //计算2连通块数量
        int area2 = arr[left] == 2 ? 1 : 0;
        area2 += arr[right] == 2 ? 1 : 0;
        left = arr[left] == 2 ? right2x[left] : left;
        right = arr[right] == 2 ? left2x[right] : right;
        //中间的2连通块的数量
        area2 += area2s[right] - area2s[left];
        //计算1连通块数量
        int area1 = 0;
        if (arr[left] == 0 && arr[right] == 0) {
            area1 = area1s[right] - area1s[left];
        } else if (arr[left] == 0) {
            area1++;
            right = left10[right];//找到右侧最近的0
            area1 += area1s[right] - area1s[left];
        } else if (arr[right] == 0) {
            area1++;
            left = right10[left];
            area1 += area1s[right] - area1s[left];
        } else {
            if (right10[left] > right) {
                area1++;
            } else {
                //左右的1是两个连通块
                area1 += 2;
                left = right10[left];
                right = left10[right];
                area1 += area1s[right] - area1s[left];
            }
        }
        return 2 * area2 + area1;
    }

    public static int[] randomArray(int n) {
        int[] ans = new int[n];
        for (int i = 0; i < n; i++) {
            ans[i] = (int) (Math.random() * 3);
        }
        return ans;
    }

    // 为了测试
    public static void main(String[] args) {
        int N = 100;
        int K = 100;
        int testTimes = 5000;
        System.out.println("功能测试开始");
        for (int i = 0; i < testTimes; i++) {
            int n = (int) (Math.random() * N) + 1;
            int k = (int) (Math.random() * K);
            int[] arr = randomArray(n);
            int ans1 = maxZero1(arr, k);
            int ans2 = maxZero2(arr, k);
            if (ans1 != ans2) {
                for (int num : arr) {
                    System.out.print(num + " ");
                }
                System.out.println();
                System.out.println("k : " + k);
                System.out.println(ans1);
                System.out.println(ans2);
                break;
            }
        }
        System.out.println("功能测试结束");

        System.out.println("性能测试开始");
        int n = 1000000;
        int k = 1000000;
        System.out.println("数组长度 : " + n);
        System.out.println("修改次数 : " + k);
        int[] arr = randomArray(n);
        long start = System.currentTimeMillis();
        maxZero2(arr, k);
        long end = System.currentTimeMillis();
        System.out.println("运行时间 : " + (end - start) + "毫秒");
        System.out.println("性能测试结束");

    }
}
