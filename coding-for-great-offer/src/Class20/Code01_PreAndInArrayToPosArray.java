package Class20;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

//如果只给定一个二叉树前序遍历数组pre和中序遍历数组in
//能否不重建树，而直接生成这个二叉树的后序数组并返回，已知二叉树中没有重复值
public class Code01_PreAndInArrayToPosArray {

    public static int[] zuo(int[] pre, int[] in) {
        if (pre == null || in == null || pre.length != in.length) {
            return null;
        }
        int N = pre.length;
        //中序遍历中”值——>索引"的映射表，用于快速定位根节点在中序遍历的位置
        HashMap<Integer, Integer> inMap = new HashMap<>();
        for (int i = 0; i < N; i++) {
            inMap.put(in[i], i);
        }
        int[] pos = new int[N];
        func(pre, 0, N - 1, in, 0, N - 1, pos, 0, N - 1, inMap);
        return pos;
    }

    //使用了递归分治的思想，通过先序遍历确定根节点，再通过中序遍历划分左右子树，最终完成后序遍历的结果
    //后序遍历的最后一个元素是根节点，左子树的后序在前，右子树的后序在中间
    public static void func(int[] pre, int L1, int R1, int[] in, int L2, int R2, int[] pos, int L3, int R3, HashMap<Integer, Integer> inMap) {
        if (L1 > R1) {
            return;
        }
        if (L1 == R1) {
            pos[L3] = pre[L1];
        } else {
            //先将先序遍历的第一个节点放在后序遍历的最后一个位置
            pos[R3] = pre[L1];
            int index = inMap.get(pre[L1]);//在中序遍历中找到根节点来划分左右子树
            //递归处理左子树。左子树大小 = 根节点在中序中的索引 - 中序左边界（index - L2）
            func(pre, L1 + 1, L1 + index - L2, in, L2, index - 1, pos, L3, L3 + index - L2 - 1, inMap);
            //递归处理右子树
            func(pre, L1 + index - L2 + 1, R1, in, index + 1, R2, pos, L3 + index - L2, R3 - 1, inMap);
        }
    }

    public static class Node {
        public int value;
        public Node left;
        public Node right;

        public Node(int v) {
            value = v;
        }
    }

    public static int[] preInToPos1(int[] pre, int[] in) {
        if (pre == null || in == null || pre.length != in.length) {
            return null;
        }
        int N = pre.length;
        int[] pos = new int[N];
        process1(pre, 0, N - 1, in, 0, N - 1, pos, 0, N - 1);
        return pos;
    }

    //不再是使用哈希表，而是直接通过遍历查找根节点在中序遍历的位置
    public static void process1(int[] pre, int L1, int R1, int[] in, int L2, int R2, int[] pos, int L3, int R3) {
        if (L1 > R1) {
            return;
        }
        if (L1 == R1) {
            pos[L3] = pre[L1];
            return;
        }
        pos[R3] = pre[L1];
        int mid = L2;//在中序遍历中找到根节点的位置
        for (; mid <= R2; mid++) {
            if (in[mid] == pre[L1]) {
                break;
            }
        }
        int leftSize = mid - L2;//中序遍历中左子树节点数量
        //递归处理左子树
        process1(pre, L1 + 1, L1 + leftSize, in, L2, mid - 1, pos, L3, L3 + leftSize - 1);
        //递归处理右子树
        process1(pre, L1 + leftSize + 1, R1, in, mid + 1, R2, pos, L3 + leftSize, R3 - 1);
    }

    public static int[] preInToPos2(int[] pre, int[] in) {
        if (pre == null || in == null || pre.length != in.length) {
            return null;
        }
        int N = pre.length;
        HashMap<Integer, Integer> inMap = new HashMap<>();
        for (int i = 0; i < N; i++) {
            inMap.put(in[i], i);
        }
        int[] pos = new int[N];
        process2(pre, 0, N - 1, in, 0, N - 1, pos, 0, N - 1, inMap);
        return pos;
    }

    public static void process2(int[] pre, int L1, int R1, int[] in, int L2, int R2, int[] pos, int L3, int R3, HashMap<Integer, Integer> inMap) {
        if (L1 > R1) {
            return;
        }
        if (L1 == R1) {
            pos[L3] = pre[L1];
            return;
        }
        pos[R3] = pre[L1];
        int mid = inMap.get(pre[L1]);
        int leftSize = mid - L2;
        process2(pre, L1 + 1, L1 + leftSize, in, L2, mid - 1, pos, L3, L3 + leftSize - 1, inMap);
        process2(pre, L1 + leftSize + 1, R1, in, mid + 1, R2, pos, L3 + leftSize, R3 - 1, inMap);
    }

    public static int[] getPreArray(Node head) {
        ArrayList<Integer> arr = new ArrayList<>();
        fillPreArray(head, arr);
        int[] ans = new int[arr.size()];
        for (int i = 0; i < ans.length; i++) {
            ans[i] = arr.get(i);
        }
        return ans;
    }

    public static void fillPreArray(Node head, ArrayList<Integer> arr) {
        if (head == null) {
            return;
        }
        arr.add(head.value);
        fillPreArray(head.left, arr);
        fillPreArray(head.right, arr);
    }

    public static int[] getInArray(Node head) {
        ArrayList<Integer> arr = new ArrayList<>();
        fillInArray(head, arr);
        int[] ans = new int[arr.size()];
        for (int i = 0; i < ans.length; i++) {
            ans[i] = arr.get(i);
        }
        return ans;
    }

    public static void fillInArray(Node head, ArrayList<Integer> arr) {
        if (head == null) {
            return;
        }
        fillInArray(head.left, arr);
        arr.add(head.value);
        fillInArray(head.right, arr);
    }

    public static int[] getPosArray(Node head) {
        ArrayList<Integer> arr = new ArrayList<>();
        fillPosArray(head, arr);
        int[] ans = new int[arr.size()];
        for (int i = 0; i < ans.length; i++) {
            ans[i] = arr.get(i);
        }
        return ans;
    }

    public static void fillPosArray(Node head, ArrayList<Integer> arr) {
        if (head == null) {
            return;
        }
        fillPosArray(head.left, arr);
        fillPosArray(head.right, arr);
        arr.add(head.value);
    }

    public static Node generateRandomTree(int value, int maxLevel) {
        HashSet<Integer> hasValue = new HashSet<Integer>();
        return createTree(value, 1, maxLevel, hasValue);
    }

    public static Node createTree(int value, int level, int maxLevel, HashSet<Integer> hasValue) {
        if (level > maxLevel) {
            return null;
        }
        int cur = 0;
        do {
            cur = (int) (Math.random() * value) + 1;
        } while (hasValue.contains(cur));
        hasValue.add(cur);
        Node head = new Node(cur);
        head.left = createTree(value, level + 1, maxLevel, hasValue);
        head.right = createTree(value, level + 1, maxLevel, hasValue);
        return head;
    }

    public static boolean isEqual(int[] arr1, int[] arr2) {
        if ((arr1 == null && arr2 != null) || (arr1 != null && arr2 == null)) {
            return false;
        }
        if (arr1 == null && arr2 == null) {
            return true;
        }
        if (arr1.length != arr2.length) {
            return false;
        }
        for (int i = 0; i < arr1.length; i++) {
            if (arr1[i] != arr2[i]) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println("test begin");
        int maxLevel = 5;
        int value = 1000;
        int testTime = 100000;
        for (int i = 0; i < testTime; i++) {
            Node head = generateRandomTree(value, maxLevel);
            int[] pre = getPreArray(head);
            int[] in = getInArray(head);
            int[] pos = getPosArray(head);
            int[] ans1 = preInToPos1(pre, in);
            int[] ans2 = preInToPos2(pre, in);
            int[] classAns = zuo(pre, in);
            if (!isEqual(pos, ans1) || !isEqual(ans1, ans2) || !isEqual(pos, classAns)) {
                System.out.println("Oops");
                break;
            }
        }
        System.out.println("test end");
    }
}
