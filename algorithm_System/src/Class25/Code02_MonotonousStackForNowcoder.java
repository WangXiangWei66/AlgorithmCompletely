package Class25;

import java.io.*;

public class Code02_MonotonousStackForNowcoder {

    public static int[] arr = new int[100000];
    public static int[][] ans = new int[100000][2];

    public static int[] stack1 = new int[100000];
    public static int[] stack2 = new int[100000];

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));

        while (in.nextToken() != StreamTokenizer.TT_EOF) {
            int n = (int) in.nval;
            for (int i = 0; i < n; i++) {
                in.nextToken();
                arr[i] = (int) in.nval;
            }
            getNearLess(n);
            for (int i = 0; i < n; i++) {
                out.println(ans[i][0] + " " + ans[i][1]);
            }
            out.flush();
        }
    }

    public static void getNearLess(int n) {
        int stackSize1 = 0;
        int stackSize2 = 0;
        for (int i = 0; i < n; i++) {
            while (stackSize1 > 0 && arr[stack1[stackSize1 - 1]] > arr[i]) {
                int curIndex = stack1[--stackSize1];
                int left = stackSize2 < 2 ? -1 : stack2[stackSize2 - 2];
                ans[curIndex][0] = left;
                ans[curIndex][1] = i;
                if (stackSize1 == 0 || arr[stack1[stackSize1 - 1]] != arr[curIndex]) {
                    stackSize2--;
                }
            }
            if (stackSize1 != 0 && arr[stack1[stackSize1 - 1]] == arr[i]) {
                stack2[stackSize2 - 1] = i;
            } else {
                stack2[stackSize2++] = i;
            }
            stack1[stackSize1++] = i;
        }
        while (stackSize1 != 0) {
            int curIndex = stack1[--stackSize1];
            int left = stackSize2 < 2 ? -1 : stack2[stackSize2 - 2];
            ans[curIndex][0] = left;
            ans[curIndex][1] = -1;
            if (stackSize1 == 0 || arr[stack1[stackSize1 - 1]] != arr[curIndex]) {
                stackSize2--;
            }
        }
    }
}
