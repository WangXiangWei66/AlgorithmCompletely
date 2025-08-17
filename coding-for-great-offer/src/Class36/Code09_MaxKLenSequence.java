package Class36;

import java.util.TreeSet;

//来自腾讯
//给定一个字符串str，和一个正数k
//返回长度为k的所有子序列中，字典序最大的子序列
//子序列：通过删除某些字符而不改变剩余字符相对顺序得到的序列
public class Code09_MaxKLenSequence {

    public static String maxString(String s, int k) {
        if (k <= 0 || s.length() < k) {
            return "";
        }
        char[] str = s.toCharArray();
        int n = str.length;
        char[] stack = new char[n];//数组模拟栈，存储候选字符
        int size = 0;//栈的当前大小，即栈顶指针
        for (int i = 0; i < n; i++) {
            // size + n - i > k 表示：当前栈中元素数 + 剩余未处理字符数 > 需要的k个
            while (size > 0 && stack[size - 1] < str[i] && size + n - i > k) {
                size--;
            }
            if (size + n - i == k) {
                return String.valueOf(stack, 0, size) + s.substring(i);
            }
            stack[size++] = str[i];
        }
        return String.valueOf(stack, 0, k);
    }

    public static String test(String str, int k) {
        if (k <= 0 || str.length() < k) {
            return "";
        }
        TreeSet<String> ans = new TreeSet<>();//自动按照字典序排序，且能去重
        process(0, 0, str.toCharArray(), new char[k], ans);
        return ans.last();
    }

    //si:当前处理到原字符串的第si个字符
    //pi:当前填充到路径数组的第pi个位置
    //str:原字符串中的字符数组
    //path:存储当前正在构建的子序列
    //ans:收集所有符合条件的子序列
    public static void process(int si, int pi, char[] str, char[] path, TreeSet<String> ans) {
        if (si == str.length) {
            if (pi == path.length) {
                ans.add(String.valueOf(path));
            }
        } else {
            process(si + 1, pi, str, path, ans);
            if (pi < path.length) {
                path[pi] = str[si];
                process(si + 1, pi + 1, str, path, ans);
            }
        }
    }

    public static String randomString(int len, int range) {
        char[] str = new char[len];
        for (int i = 0; i < len; i++) {
            str[i] = (char) ((int) (Math.random() * range) + 'a');
        }
        return String.valueOf(str);
    }

    public static void main(String[] args) {
        int n = 12;
        int r = 5;
        int testTime = 10000;
        System.out.println("test begin!");
        for (int i = 0; i < testTime; i++) {
            int len = (int) (Math.random() * (n + 1));
            String str = randomString(len, r);
            int k = (int) (Math.random() * (str.length() + 1));
            String ans1 = maxString(str, k);
            String ans2 = test(str, k);
            if (!ans1.equals(ans2)) {
                System.out.println("Oops!");
                System.out.println(str);
                System.out.println(k);
                System.out.println(ans1);
                System.out.println(ans2);
                break;
            }
        }
        System.out.println("test finish!");
    }
}
