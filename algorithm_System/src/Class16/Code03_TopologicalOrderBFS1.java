package Class16;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class Code03_TopologicalOrderBFS1 {

    public static class DirectedGraphNode {
        public int label;
        public ArrayList<DirectedGraphNode> neighbors;

        public DirectedGraphNode(int x) {
            label = x;
            neighbors = new ArrayList<DirectedGraphNode>();
        }
    }

    public static ArrayList<DirectedGraphNode> topSort(ArrayList<DirectedGraphNode> graph) {
        HashMap<DirectedGraphNode, Integer> inDegreeMap = new HashMap<>();
        for (DirectedGraphNode cur : graph) {
            inDegreeMap.put(cur, 0);
        }
        for (DirectedGraphNode cur : graph) {
            for (DirectedGraphNode next : cur.neighbors) {
                inDegreeMap.put(next, inDegreeMap.get(next) + 1);
            }
        }
        Queue<DirectedGraphNode> zeroQueue = new LinkedList<>();
        for (DirectedGraphNode cur : inDegreeMap.keySet()) {
            if (inDegreeMap.get(cur) == 0) {
                zeroQueue.add(cur);
            }
        }
        ArrayList<DirectedGraphNode> ans = new ArrayList<>();
        while (!zeroQueue.isEmpty()) {
            DirectedGraphNode cur = zeroQueue.poll();
            ans.add(cur);
            for (DirectedGraphNode next : cur.neighbors) {
                inDegreeMap.put(next, inDegreeMap.get(next) - 1);
                if (inDegreeMap.get(next) == 0) {
                    zeroQueue.offer(next);
                }
            }
        }
        return ans;
    }
}
