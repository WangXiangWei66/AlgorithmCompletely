package Class41;

import java.util.Arrays;
import java.util.HashMap;

//来自小红书
//有四种诗的韵律分别为: AABB、ABAB、ABBA、AAAA
//比如 : 1 1 3 3就属于AABB型的韵律、6 6 6 6就属于AAAA型的韵律等等
//一个数组arr，当然可以生成很多的子序列，如果某个子序列一直以韵律的方式连接起来，我们称这样的子序列是有效的
//比如, arr = { 1, 1, 15, 1, 34, 1, 2, 67, 3, 3, 2, 4, 15, 3, 17, 4, 3, 7, 52, 7, 81, 9, 9 }
//arr的一个子序列为{1, 1, 1, 1, 2, 3, 3, 2, 4, 3, 4, 3, 7, 7, 9, 9}
//其中1, 1, 1, 1是AAAA、2, 3, 3, 2是ABBA、4, 3, 4, 3是ABAB、7, 7, 9, 9是AABB
//可以看到，整个子序列一直以韵律的方式连接起来，所以这个子序列是有效的
//给定一个数组arr, 返回最长的有效子序列长度
//题目限制 : arr长度 <= 4000, arr中的值<= 10^9
public class Code02_PoemProblem {

    public static int maxLen1(int[] arr) {
        if (arr == null || arr.length < 4) {
            return 0;
        }
        int[] path = new int[arr.length];//存储当前选择的子序列
        return process1(arr, 0, path, 0);
    }

    //index:当前处理的索引位置
    //size:当前子序列的长度
    public static int process1(int[] arr, int index, int[] path, int size) {
        if (index == arr.length) {
            if (size % 4 != 0) {
                return 0;
            } else {
                for (int i = 0; i < size; i += 4) {
                    if (!valid(path, i)) {
                        return 0;
                    }
                }
                return size;
            }
        } else {
            int p1 = process1(arr, index + 1, path, size);//不选择当前的元素，直接进行下一个
            path[size] = arr[index];
            int p2 = process1(arr, index + 1, path, size + 1);
            return Math.max(p1, p2);
        }
    }

    public static boolean valid(int[] p, int i) {
        return (p[i] == p[i + 1] && p[i + 2] == p[i + 3]) ||
                (p[i] == p[i + 2] && p[i + 1] == p[i + 3] && p[i] != p[i + 1]) ||
                (p[i] == p[i + 3] && p[i + 1] == p[i + 2] && p[i] != p[i + 1]);
    }

    public static int maxLen2(int[] arr) {
        if (arr == null || arr.length < 4) {
            return 0;
        }
        int n = arr.length;
        int[] sorted = Arrays.copyOf(arr, n);//复制一份原始数组
        Arrays.sort(sorted);
        //为每个唯一值分配一个索引
        HashMap<Integer, Integer> vMap = new HashMap<>();
        int index = 0;
        vMap.put(sorted[0], index++);
        for (int i = 1; i < n; i++) {
            if (sorted[i] != sorted[i - 1]) {
                vMap.put(sorted[i], index++);
            }
        }
        int[] sizeArr = new int[index];//每个离散化后的值出现的总次数
        for (int i = 0; i < n; i++) {
            arr[i] = vMap.get(arr[i]);
            sizeArr[arr[i]]++;
        }
        //初始化iMap的二维结构（每行长度 = 对应离散值的出现次数）
        int[][] iMap = new int[index][];
        for (int i = 0; i < index; i++) {
            iMap[i] = new int[sizeArr[i]];
        }
        for (int i = n - 1; i >= 0; i--) {
            iMap[arr[i]][--sizeArr[arr[i]]] = i;
        }
        return process2(arr, iMap, 0);
    }

    //i：当前递归的起始位置
    //终止递归条件 -> 不选当前元素 -> 选择当前元素匹配四种韵律
    public static int process2(int[] vArr, int[][] iMap, int i) {
        if (i + 4 > vArr.length) {
            return 0;
        }
        int p0 = process2(vArr, iMap, i + 1);//不选当前元素
        int p1 = 0;//AABB
        //A1(i) -> A2(>A1) -> B1(>A2,B != A) -> B2(>B1)
        int rightClosedP1A2 = rightClosed(iMap, vArr[i], i);
        if (rightClosedP1A2 != -1) {
            //找B1
            for (int next = rightClosedP1A2 + 1; next < vArr.length; next++) {
                if (vArr[i] != vArr[next]) {
                    //找B2
                    int rightClosedP1B2 = rightClosed(iMap, vArr[next], next);
                    if (rightClosedP1B2 != -1) {
                        p1 = Math.max(p1, 4 + process2(vArr, iMap, rightClosedP1B2 + 1));
                    }
                }
            }
        }
        int p2 = 0;//ABAB
        for (int P2B1 = i + 1; P2B1 < vArr.length; P2B1++) {
            if (vArr[i] != vArr[P2B1]) {
                int rightClosedP2A2 = rightClosed(iMap, vArr[i], P2B1);
                if (rightClosedP2A2 != -1) {
                    int rightClosedP2B2 = rightClosed(iMap, vArr[P2B1], rightClosedP2A2);
                    if (rightClosedP2B2 != -1) {
                        p2 = Math.max(p2, 4 + process2(vArr, iMap, rightClosedP2B2 + 1));
                    }
                }
            }
        }

        int p3 = 0;//ABBA
        for (int p3B1 = i + 1; p3B1 < vArr.length; p3B1++) {
            if (vArr[i] != vArr[p3B1]) {
                int rightClosedP3B2 = rightClosed(iMap, vArr[p3B1], p3B1);
                if (rightClosedP3B2 != -1) {
                    int rightClosedP3A2 = rightClosed(iMap, vArr[i], rightClosedP3B2);
                    if (rightClosedP3A2 != -1) {
                        p3 = Math.max(p3, 4 + process2(vArr, iMap, rightClosedP3A2 + 1));
                    }
                }
            }
        }
        int p4 = 0;//AAAA
        int rightClosedP4A2 = rightClosed(iMap, vArr[i], i);
        int rightClosedP4A3 = rightClosedP4A2 == -1 ? -1 : rightClosed(iMap, vArr[i], rightClosedP4A2);
        int rightClosedP4A4 = rightClosedP4A3 == -1 ? -1 : rightClosed(iMap, vArr[i], rightClosedP4A3);
        if (rightClosedP4A4 != -1) {
            p4 = Math.max(p4, 4 + process2(vArr, iMap, rightClosedP4A4 + 1));
        }
        return Math.max(Math.max(p1, p2), Math.max(p3, p4));
    }

    //在离散值v的所有出现位置中，找到【大于当前位置i的最小位置】
    public static int rightClosed(int[][] iMap, int v, int i) {
        int left = 0;
        int right = iMap[v].length - 1;
        int ans = -1;
        while (left <= right) {
            int mid = (left + right) / 2;
            if (iMap[v][mid] <= i) {
                left = mid + 1;
            } else {
                ans = mid;
                right = mid - 1;
            }
        }
        return ans == -1 ? -1 : iMap[v][ans];
    }

    public static int maxLen3(int[] arr) {
        if (arr == null || arr.length < 4) {
            return 0;
        }
        int n = arr.length;
        int[] sorted = Arrays.copyOf(arr, n);
        Arrays.sort(sorted);
        HashMap<Integer, Integer> vmap = new HashMap<>();
        int index = 0;
        vmap.put(sorted[0], index++);
        for (int i = 1; i < n; i++) {
            if (sorted[i] != sorted[i - 1]) {
                vmap.put(sorted[i], index++);
            }
        }
        int[] sizeArr = new int[index];
        for (int i = 0; i < n; i++) {
            arr[i] = vmap.get(arr[i]);
            sizeArr[arr[i]]++;
        }
        int[][] imap = new int[index][];
        for (int i = 0; i < index; i++) {
            imap[i] = new int[sizeArr[i]];
        }
        for (int i = n - 1; i >= 0; i--) {
            imap[arr[i]][--sizeArr[arr[i]]] = i;
        }
        //dp[i]:从数组位置i开始的最长有效子序列长度
        int[] dp = new int[n + 1];
        for (int i = n - 4; i >= 0; i--) {
            int p0 = dp[i + 1];
            int p1 = 0;
            int rightClosedP1A2 = rightClosed(imap, arr[i], i);
            if (rightClosedP1A2 != -1) {
                for (int next = rightClosedP1A2 + 1; next < arr.length; next++) {
                    if (arr[i] != arr[next]) {
                        int rightClosedP1B2 = rightClosed(imap, arr[next], next);
                        if (rightClosedP1B2 != -1) {
                            p1 = Math.max(p1, 4 + dp[rightClosedP1B2 + 1]);
                        }
                    }
                }
            }

            int p2 = 0;
            for (int p2B1 = i + 1; p2B1 < arr.length; p2B1++) {
                if (arr[i] != arr[p2B1]) {
                    int rightClosedP2A2 = rightClosed(imap, arr[i], p2B1);
                    if (rightClosedP2A2 != -1) {
                        int rightClosedP2B2 = rightClosed(imap, arr[p2B1], rightClosedP2A2);
                        if (rightClosedP2A2 != -1) {
                            p2 = Math.max(p2, 4 + dp[rightClosedP2B2 + 1]);
                        }
                    }
                }
            }

            int p3 = 0;
            for (int p3B1 = i + 1; p3B1 < arr.length; p3B1++) {
                if (arr[i] != arr[p3B1]) {
                    int rightClosedP3B2 = rightClosed(imap, arr[p3B1], p3B1);
                    if (rightClosedP3B2 != -1) {
                        int rightClosedP3A2 = rightClosed(imap, arr[i], rightClosedP3B2);
                        if (rightClosedP3A2 != -1) {
                            p3 = Math.max(p3, 4 + dp[rightClosedP3A2 + 1]);
                        }
                    }
                }
            }

            int p4 = 0;
            int rightClosedP4A2 = rightClosed(imap, arr[i], i);
            int rightClosedP4A3 = rightClosedP4A2 == -1 ? -1 : rightClosed(imap, arr[i], rightClosedP4A2);
            int rightClosedP4A4 = rightClosedP4A3 == -1 ? -1 : rightClosed(imap, arr[i], rightClosedP4A3);
            if (rightClosedP4A3 != -1) {
                p4 = Math.max(p4, 4 + dp[rightClosedP4A4 + 1]);
            }
            dp[i] = Math.max(p0, Math.max(Math.max(p1, p2), Math.max(p3, p4)));
        }
        return dp[0];
    }

    public static int maxLen4(int[] arr) {
        HashMap<Integer, Integer> map = new HashMap<>();//当前窗口内每个元素出现的次数
        int two = 0;
        int ans = 0;
        int numTimes = 0;//临时存储当前元素出现的次数
        for (int num : arr) {
            map.put(num, map.getOrDefault(num, 0) + 1);
            numTimes = map.get(num);
            two += numTimes == 2 ? 1 : 0;
            if (two == 2 || numTimes == 4) {
                ans += 4;
                map.clear();
                two = 0;
            }
        }
        return ans;
    }

    public static int[] randomArray(int len, int value) {
        int[] arr = new int[len];
        for (int i = 0; i < len; i++) {
            arr[i] = (int) (Math.random() * value);
        }
        return arr;
    }

    public static void main(String[] args) {
        int[] test = {1, 1, 15, 1, 34, 1, 2, 67, 3, 3, 2, 4, 15, 3, 17, 4, 3, 7, 52, 7, 81, 9, 9};
        System.out.println(maxLen1(test));
        System.out.println(maxLen2(test));
        System.out.println(maxLen3(test));
        System.out.println(maxLen4(test));
        System.out.println("==============");

        int len = 16;
        int value = 10;
        int[] arr = randomArray(len, value);
        int[] arr1 = Arrays.copyOf(arr, arr.length);
        int[] arr2 = Arrays.copyOf(arr, arr.length);
        int[] arr3 = Arrays.copyOf(arr, arr.length);
        int[] arr4 = Arrays.copyOf(arr, arr.length);
        System.out.println(maxLen1(arr1));
        System.out.println(maxLen2(arr2));
        System.out.println(maxLen3(arr3));
        System.out.println(maxLen4(arr4));
        System.out.println("==============");
        long start;
        long end;
        int[] longArr = randomArray(4000, 20);
        start = System.currentTimeMillis();
        System.out.println(maxLen3(longArr));
        end = System.currentTimeMillis();
        System.out.println("运行时间（毫秒）：" + (end - start));
        System.out.println("==============");

        start = System.currentTimeMillis();
        System.out.println(maxLen4(longArr));
        end = System.currentTimeMillis();
        System.out.println("运行时间（毫秒）：" + (end - start));
        System.out.println("==============");
    }
}
