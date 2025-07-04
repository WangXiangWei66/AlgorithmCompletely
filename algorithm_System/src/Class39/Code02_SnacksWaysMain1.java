package Class39;

import java.io.*;
import java.util.Map.Entry;
import java.util.TreeMap;

public class Code02_SnacksWaysMain1 {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        while (in.nextToken() != StreamTokenizer.TT_EOF) {
            int n = (int) in.nval;
            in.nextToken();
            int bag = (int) in.nval;
            int[] arr = new int[n];
            for (int i = 0; i < n; i++) {
                in.nextToken();
                arr[i] = (int) in.nval;
            }
            long ways = ways(arr, bag);
            out.println(ways);
            out.flush();
        }
    }

    public static long ways(int[] arr, int bag) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        if (arr.length == 1) {
            return arr[0] <= bag ? 2 : 1;
        }
        int mid = (arr.length - 1) >> 1;
        TreeMap<Long, Long> lMap = new TreeMap<>();
        long ways = process(arr, 0, 0, mid, bag, lMap);
        TreeMap<Long, Long> rMap = new TreeMap<>();
        ways += process(arr, mid + 1, 0, arr.length - 1, bag, rMap);
        TreeMap<Long, Long> rPre = new TreeMap<>();
        long pre = 0;
        for (Entry<Long, Long> entry : rMap.entrySet()) {
            pre += entry.getValue();
            rPre.put(entry.getKey(), pre);
        }
        for (Entry<Long, Long> entry : lMap.entrySet()) {
            long lWeight = entry.getKey();
            long lWays = entry.getValue();
            Long floor = rPre.floorKey(bag - lWeight);
            if (floor != null) {
                long rWays = rPre.get(floor);
                ways += lWays * rWays;
            }
        }
        return ways + 1;
    }

    public static long func(int[] arr, int index, int end, long sum, long bag, TreeMap<Long, Long> map) {
        if (sum > bag) {
            return 0;
        }
        if (index > end) {
            if (sum != 0) {
                if (!map.containsKey(sum)) {
                    map.put(sum, 1L);
                } else {
                    map.put(sum, map.get(sum) + 1);
                }
                return 1;
            } else {
                return 0;
            }
        }
        long ways = func(arr, index + 1, end, sum, bag, map);
        ways += func(arr, index + 1, end, sum + arr[index], bag, map);
        return ways;
    }

    public static long process(int[] arr, int index, long w, int end, int bag, TreeMap<Long, Long> map) {
        if (w > bag) {
            return 0;
        }
        if (index > end) {
            if (w != 0) {
                if (!map.containsKey(w)) {
                    map.put(w, 1L);
                } else {
                    map.put(w, map.get(w) + 1);
                }
                return 1;
            } else {
                return 0;
            }
        } else {
            long ways = process(arr, index + 1, w, end, bag, map);
            ways += process(arr, index + 1, w + arr[index], end, bag, map);
            return ways;
        }
    }
}
