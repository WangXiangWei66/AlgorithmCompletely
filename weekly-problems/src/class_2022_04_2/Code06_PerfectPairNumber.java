package class_2022_04_2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.HashMap;

//来自阿里
//x = { a, b, c, d }
//y = { e, f, g, h }
//x、y两个小数组长度都是4
//如果有: a + e = b + f = c + g = d + h
//那么说x和y是一个完美对
//题目给定N个小数组，每个小数组长度都是K
//返回这N个小数组中，有多少完美对
public class Code06_PerfectPairNumber {

    public static long perfectPairs(int[][] matrix) {
        long ans = 0;
        //记录每个特征字符串出现的次数
        HashMap<String, Integer> counts = new HashMap<>();
        for (int[] arr : matrix) {
            StringBuilder self = new StringBuilder();//构建当前数组的特征字符串
            StringBuilder minus = new StringBuilder();//构建当前数组的互补特征字符串
            for (int i = 1; i < arr.length; i++) {
                self.append("_" + (arr[i] - arr[i - 1]));
                minus.append("_" + (arr[i - 1] - arr[i]));
            }
            ans += counts.getOrDefault(minus.toString(), 0);
            counts.put(self.toString(), counts.getOrDefault(self.toString(), 0) + 1);
        }
        return ans;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        while (in.nextToken() != StreamTokenizer.TT_EOF) {
            int n = (int) in.nval;
            in.nextToken();
            int m = (int) in.nval;
            int[][] matrix = new int[n][m];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    in.nextToken();
                    matrix[i][j] = (int) in.nval;
                }
            }
            long ans = perfectPairs(matrix);
            out.println(ans);
            out.flush();
        }
    }
}
