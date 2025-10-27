package class_2022_07_4;

//一个数组如果满足 :
//升降升降升降... 或者 降升降升...都是满足的
//给定一个数组，
//1，看有几种方法能够剔除一个元素，达成上述的要求
//2，数组天然符合要求返回0
//3，剔除1个元素达成不了要求，返回-1，
//比如：
//给定[3, 4, 5, 3, 7]，返回3
//移除0元素，4 5 3 7 符合
//移除1元素，3 5 3 7 符合
//移除2元素，3 4 3 7 符合
//再比如：给定[1, 2, 3, 4] 返回-1
//因为达成不了要求
public class Code01_WaysWiggle {

    public static int ways1(int[] arr) {
        //判断数组是否天然符合
        if (isWiggle(arr, -1)) {
            return 0;
        }
        int ans = 0;
        for (int i = 0; i < arr.length; i++) {
            if (isWiggle(arr, i)) {
                ans++;
            }
        }
        return ans == 0 ? -1 : ans;
    }

    public static boolean isWiggle(int[] arr, int removeIndex) {
        //先升后降模式
        boolean ans = true;
        boolean request = true;
        for (int i = 1; i < arr.length; i++) {
            if (i == removeIndex) {
                continue;
            }
            //跳过提出第0个 元素
            if (i - 1 == removeIndex && removeIndex == 0) {
                continue;
            }
            //上一个有效元素的索引
            int last = i - 1 == removeIndex ? (i - 2) : (i - 1);
            if (request) {
                if (arr[last] >= arr[i]) {
                    ans = false;
                    break;
                }
            } else {
                if (arr[last] <= arr[i]) {
                    ans = false;
                    break;
                }
            }
            //另一种模式
            request = !request;
        }
        if (ans) {
            return true;
        }
        ans = true;
        request = false;
        for (int i = 1; i < arr.length; i++) {
            if (i == removeIndex) {
                continue;
            }
            if (i - 1 == removeIndex && removeIndex == 0) {
                continue;
            }
            int last = i - 1 == removeIndex ? (i - 2) : (i - 1);
            if (request) {
                if (arr[last] >= arr[i]) {
                    ans = false;
                    break;
                }
            } else {
                if (arr[last] <= arr[i]) {
                    ans = false;
                    break;
                }
            }
            request = !request;
        }
        return ans;
    }

    public static int ways2(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;
        }
        int n = arr.length;
        // rightUp[i]：从i开始，右侧子数组满足“升→降→升...”
        // rightDown[i]：从i开始，右侧子数组满足“降→升→降...”
        boolean[] rightUp = new boolean[n];
        boolean[] rightDown = new boolean[n];
        //最后一个元素默认满足状态
        rightUp[n - 1] = true;
        rightDown[n - 1] = true;
        for (int i = n - 2; i >= 0; i--) {
            rightUp[i] = arr[i] < arr[i + 1] && rightDown[i + 1];
            rightDown[i] = arr[i] > arr[i + 1] && rightUp[i + 1];
        }
        //原始数组是否已经满足条件
        if (rightUp[0] || rightDown[0]) {
            return 0;
        }
        //初始化剔除第0个元素
        int ans = (rightUp[1] || rightDown[1]) ? 1 : 0;
        //维护左侧状态
        boolean leftUp = true;
        boolean leftDown = true;
        boolean tmp;
        for (int i = 1, l = 0, r = 2; i < n - 1; i++, l++, r++) {
            // 条件：剔除i后，左侧结尾与右侧开头匹配
            // 情况1：左侧以降结尾（leftDown），右侧以升开头（rightUp[r]），且左侧最后元素 > 右侧第一个元素（arr[l] > arr[r]）
            // 情况2：左侧以升结尾（leftUp），右侧以降开头（rightDown[r]），且左侧最后元素 < 右侧第一个元素（arr[l] < arr[r]）
            ans += (arr[l] > arr[r] && rightUp[r] && leftDown) || (arr[l] < arr[r] && rightDown[r] && leftUp) ? 1 : 0;
            tmp = leftUp;
            leftUp = arr[l] > arr[i] && leftDown;
            leftDown = arr[l] < arr[i] && tmp;
        }
        //最后一个元素只需要左侧子数组满足条件
        ans += leftUp || leftDown ? 1 : 0;
        return ans == 0 ? -1 : ans;
    }

    public static int[] randomArray(int len, int maxValue) {
        int[] ans = new int[len];
        for (int i = 0; i < len; i++) {
            ans[i] = (int) (Math.random() * maxValue) + 1;
        }
        return ans;
    }

    // 为了验证
    public static void main(String[] args) {
        int maxLen = 10;
        int maxValue = 100;
        int testTime = 30000;
        System.out.println("测试开始");
        for (int i = 0; i < testTime; i++) {
            int len = (int) (Math.random() * maxLen) + 1;
            int[] arr = randomArray(len, maxValue);
            int ans1 = ways1(arr);
            int ans2 = ways2(arr);
            if (ans1 != ans2) {
                System.out.println(ans1);
                System.out.println(ans2);
                System.out.println("出错了！");
                break;
            }
        }
        System.out.println("测试结束");
    }
}
