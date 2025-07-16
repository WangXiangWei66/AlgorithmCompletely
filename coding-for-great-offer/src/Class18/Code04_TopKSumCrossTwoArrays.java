package Class18;

import java.io.*;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;

//给定两个有序数组arr1和arr2，再给定一个整数k，返回来自arr1和arr2的两个数相加和最大的前k个
// 两个数必须分别来自两个数组，按照降序输出
//时间复杂度为O(k*logk)
//输入描述：
//第一行三个整数N, K分别表示数组arr1, arr2的大小，以及需要询问的数
//接下来一行N个整数，表示arr1内的元素
//再接下来一行N个整数，表示arr2内的元素
//输出描述：
//输出K个整数表示答案
//牛客网题目：https://www.nowcoder.com/practice/7201cacf73e7495aa5f88b223bbbf6d1
public class Code04_TopKSumCrossTwoArrays {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        while (in.nextToken() != StreamTokenizer.TT_EOF) {
            int N = (int) in.nval;
            in.nextToken();
            int K = (int) in.nval;
            int[] arr1 = new int[N];
            int[] arr2 = new int[N];
            for (int i = 0; i < N; i++) {
                in.nextToken();
                arr1[i] = (int) in.nval;
            }
            for (int i = 0; i < N; i++) {
                in.nextToken();
                arr2[i] = (int) in.nval;
            }
            int[] topK = topKSum(arr1, arr2, K);
            for (int i = 0; i < K; i++) {
                out.print(topK[i] + " ");
            }
            out.println();
            out.flush();
        }
    }

    //放入大根堆中的结构
    public static class Node {
        public int index1;//数组arr1的索引
        public int index2;//数组arr2的索引
        public int sum;//对应元素的和

        public Node(int i1, int i2, int s) {
            index1 = i1;
            index2 = i2;
            sum = s;
        }
    }

    //生成大根堆的比较器
    public static class MaxHeapComp implements Comparator<Node> {
        @Override
        public int compare(Node o1, Node o2) {
            return o2.sum - o1.sum;
        }
    }

    public static int[] topKSum(int[] arr1, int[] arr2, int topK) {
        if (arr1 == null || arr2 == null || topK < 1) {
            return null;
        }
        int N = arr1.length;
        int M = arr2.length;
        topK = Math.min(topK, N * M);//确保K不超过最大可能组合数
        int[] res = new int[topK];
        int resIndex = 0;//用于跟踪结果数组填充位置
        PriorityQueue<Node> maxHeap = new PriorityQueue<>(new MaxHeapComp());//存储当前最大和与索引对
        HashSet<String> set = new HashSet<>();//记录已访问的索引对，避免重复计算
        //从数组末尾开始，初始化堆和集合
        int i1 = N - 1;
        int i2 = M - 1;
        maxHeap.add(new Node(i1, i2, arr1[i1] + arr2[i2]));
        set.add(i1 + "_" + i2);
        while (resIndex != topK) {
            //将当前的最大和与索引对从堆中取出
            Node curNode = maxHeap.poll();
            res[resIndex++] = curNode.sum;//将该和加入结果数组，并将索引对标为已访问
            i1 = curNode.index1;
            i2 = curNode.index2;
            //生成两个可能的后续索引对
            set.remove(i1 + "_" + i2);
            if (i1 - 1 >= 0 && !set.contains((i1 - 1) + "_" + i2)) {
                set.add((i1 - 1) + "_" + i2);
                maxHeap.add(new Node(i1 - 1, i2, arr1[i1 - 1] + arr2[i2]));
            }
            if (i2 - 1 >= 0 && !set.contains(i1 + "_" + (i2 - 1))) {
                set.add(i1 + "_" + (i2 - 1));
                maxHeap.add(new Node(i1, i2 - 1, arr1[i1] + arr2[i2 - 1]));
            }
        }
        return res;
    }
}
