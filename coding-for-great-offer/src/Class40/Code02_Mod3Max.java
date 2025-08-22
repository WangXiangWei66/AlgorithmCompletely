package Class40;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeSet;

//来自去哪儿网
//给定一个arr，里面的数字都是0~9
//你可以随意使用arr中的数字，哪怕打乱顺序也行
//请拼出一个能被3整除的，最大的数字，用str形式返回
public class Code02_Mod3Max {

    public static String max1(int[] arr) {
        Arrays.sort(arr);
        //反转数组，变成降序排列
        for (int l = 0, r = arr.length - 1; l < r; l++, r--) {
            int tmp = arr[l];
            arr[l] = arr[r];
            arr[r] = tmp;
        }
        StringBuilder builder = new StringBuilder();
        //用TreeSet来存储符合条件的字符串
        TreeSet<String> set = new TreeSet<>((a, b) -> Integer.valueOf(b).compareTo(Integer.valueOf(a)));
        process1(arr, 0, builder, set);
        return set.isEmpty() ? "" : set.first();
    }

    public static void process1(int[] arr, int index, StringBuilder builder, TreeSet<String> set) {
        if (index == arr.length) {
            if (builder.length() != 0 && Integer.valueOf(builder.toString()) % 3 == 0) {
                set.add(builder.toString());
            }
        } else {
            process1(arr, index + 1, builder, set);
            builder.append(arr[index]);
            process1(arr, index + 1, builder, set);
            //回溯，删除最后添加的数字
            builder.deleteCharAt(builder.length() - 1);
        }
    }

    public static String max2(int[] arr) {
        if (arr == null || arr.length == 0) {
            return "";
        }
        Arrays.sort(arr);
        for (int l = 0, r = arr.length - 1; l < r; l++, r--) {
            int tmp = arr[l];
            arr[l] = arr[r];
            arr[r] = tmp;
        }
        if (arr[0] == 0) {
            return "0";
        }
        String ans = process2(arr, 0, 0);
        //去除结果字符串中的开头所有0
        //^：表示匹配任意字符的开始位置
        //0+:表示匹配一个或多个连续的字符0
        String res = ans.replaceAll("^(0+)", "");
        if (!res.equals("")) {
            return res;
        }
        return ans.equals("") ? ans : "0";
    }

    public static String process2(int[] arr, int index, int mod) {
        if (index == arr.length) {
            return mod == 0 ? "" : "$";
        }
        String p1 = "$";//选择当前元素
        //计算选择当前元素后的新余数
        int nextMod = nextMod(mod, arr[index] % 3);
        String next = process2(arr, index + 1, nextMod);
        //后续处理有效，将当前元素加入结果
        if (!next.equals("$")) {
            p1 = String.valueOf(arr[index]) + next;
        }
        String p2 = process2(arr, index + 1, mod);
        if (p1.equals("$") && p2.equals("$")) {
            return "$";
        }
        if (!p1.equals("$") && !p2.equals("$")) {
            return smaller(p1, p2) ? p2 : p1;
        }
        return p1.equals("$") ? p2 : p1;
    }

    //根据当前余数和新加入数字的余数，计算新的余数
    public static int nextMod(int require, int current) {
        if (require == 0) {
            if (current == 0) {
                return 0;
            } else if (current == 1) {
                return 2;
            } else {
                return 1;
            }
        } else if (require == 1) {
            if (current == 0) {
                return 1;
            } else if (current == 1) {
                return 0;
            } else {
                return 2;
            }
        } else {
            if (current == 0) {
                return 2;
            } else if (current == 1) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    public static boolean smaller(String p1, String p2) {
        if (p1.length() != p2.length()) {
            return p1.length() < p2.length();
        }
        return p1.compareTo(p2) < 0;
    }

    public static String max3(int[] A) {
        if (A == null || A.length == 0) {
            return "";
        }
        int mod = 0;//计算所有元素总和对3的余数
        ArrayList<Integer> arr = new ArrayList<>();//将数组转化为列表，便于操作
        //将元素添加到列表，并计算总和对3的余数
        for (int num : A) {
            arr.add(num);
            mod += num;
            mod %= 3;
        }
        //总和如果不能被3整除，尝试移除元素使总和能被3整除
        if ((mod == 1 || mod == 2) && !remove(arr, mod, 3 - mod)) {
            return "";
        }
        if (arr.isEmpty()) {
            return "";
        }
        arr.sort((a, b) -> b - a);//降序排序
        if (arr.get(0) == 0) {
            return "0";
        }
        StringBuilder builder = new StringBuilder();
        for (int num : arr) {
            builder.append(num);
        }
        return builder.toString();
    }

    //移除元素使总和能被3整除
    public static boolean remove(ArrayList<Integer> arr, int first, int second) {
        //空列表无法移除元素
        if (arr.size() == 0) {
            return false;
        }
        arr.sort((a, b) -> compare(a, b, first, second));
        int size = arr.size();
        //尝试移除一个余数为first的元素
        if (arr.get(size - 1) % 3 == first) {
            arr.remove(size - 1);
            return true;
            //否则尝试移除两个余数为second的元素
        } else if (size > 1 && arr.get(size - 1) % 3 == second && arr.get(size - 2) % 3 == second) {
            arr.remove(size - 1);
            arr.remove(size - 2);
            return true;
        } else {
            return false;
        }
    }

    public static int compare(int a, int b, int f, int s) {
        int ma = a % 3;
        int mb = b % 3;
        //余数相同，则吧较大的数排在前面
        if (ma == mb) {
            return b - a;
        } else {
            if (ma == 0 || mb == 0) {
                return ma == 0 ? -1 : 1;
            } else {
                return ma == s ? -1 : 1;
            }
        }
    }

    public static int[] randomArray(int len) {
        int[] arr = new int[len];
        for (int i = 0; i < len; i++) {
            arr[i] = (int) (Math.random() * 10);
        }
        return arr;
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
        int testTime = 10000;
        System.out.println("test begin!");
        for (int i = 0; i < testTime; i++) {
            int len = (int) (Math.random() * N);
            int[] arr1 = randomArray(len);
            int[] arr2 = copyArray(arr1);
            int[] arr3 = copyArray(arr1);
            String ans1 = max1(arr1);
            String ans2 = max2(arr2);
            String ans3 = max3(arr3);
            if (!ans1.equals(ans2) || !ans1.equals(ans3)) {
                System.out.println("Oops");
                for (int num : arr3) {
                    System.out.print(num + " ");
                }
                System.out.println();
                System.out.println(ans1);
                System.out.println(ans2);
                System.out.println(ans3);
                break;
            }
        }
        System.out.println("test end!");
    }
}
