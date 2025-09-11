package Class_2021_12_2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

//给定一棵多叉树的头节点head
//每个节点的颜色只会是0、1、2、3中的一种
//任何两个节点之间的都有路径
//如果节点a和节点b的路径上，包含全部的颜色，这条路径算达标路径
//(a -> ... -> b)和(b -> ... -> a)算两条路径
//求多叉树上达标的路径一共有多少？
//点的数量 <= 10^5
public class Code05_Colors {

    public static class Node {
        public int color;//节点的颜色
        public List<Node> nexts;//子节点列表

        public Node(int c) {
            color = c;
            nexts = new ArrayList<>();
        }
    }

    public static int colors1(Node head) {
        if (head == null) {
            return 0;
        }
        //创建节点到父节点的映射
        HashMap<Node, Node> map = new HashMap<>();
        parentMap(head, null, map);
        //收集所有节点
        List<Node> allNodes = new ArrayList<>();
        for (Node cur : map.keySet()) {
            allNodes.add(cur);
        }
        int ans = 0;
        //检查所有节点对
        for (int i = 0; i < allNodes.size(); i++) {
            for (int j = i + 1; j < allNodes.size(); j++) {
                if (ok(allNodes.get(i), allNodes.get(j), map)) {
                    ans++;//路径达标则计数+1
                }
            }
        }
        return ans << 1;
    }

    public static void parentMap(Node cur, Node pre, HashMap<Node, Node> map) {
        if (cur != null) {
            map.put(cur, pre);//记录当前节点的父
            for (Node next : cur.nexts) {
                parentMap(next, cur, map);
            }
        }
    }

    public static boolean ok(Node a, Node b, HashMap<Node, Node> map) {
        //收集从a到根的所有节点
        HashSet<Node> apath = new HashSet<>();
        Node cur = a;
        while (cur != null) {
            apath.add(cur);
            cur = map.get(cur);
        }
        //找a和b的最低公共祖先
        Node lowest = b;
        while (!apath.contains(lowest)) {
            lowest = map.get(lowest);
        }
        //用位运算记录路径的颜色
        int colors = 1 << lowest.color;
        cur = a;
        while (cur != lowest) {
            colors |= (1 << cur.color);
            cur = map.get(cur);
        }
        cur = b;
        while (cur != lowest) {
            colors |= (1 << cur.color);
            cur = map.get(cur);
        }
        return colors == 15;
    }

    public static long colors2(Node head) {
        if (head == null) {
            return 0;
        }
        return process2(head).all;
    }

    public static class Info {
        public long all;//所有达标路径的数量
        public long[] colors;//从当前节点出发，记录从当前节点出发到子树中各节点的颜色状态计数

        public Info() {
            all = 0;
            colors = new long[16];
        }
    }

    public static Info process2(Node h) {
        Info ans = new Info();
        int hs = 1 << h.color;//当前节点的颜色状态
        ans.colors[hs] = 1;//当前节点到自身的映射，路径此时只有一条
        if (!h.nexts.isEmpty()) {
            int n = h.nexts.size();
            //存储所有子节点的Info信息
            Info[] infos = new Info[n + 1];
            for (int i = 1; i <= n; i++) {
                infos[i] = process2(h.nexts.get(i - 1));//i-1是因为索引要从0开始
                ans.all += infos[i].all;//添加子树中已有的达标路径
            }
            //lefts[i][s]:表示前i个子树中状态为s的路径总数
            long[][] lefts = new long[n + 2][16];
            for (int i = 1; i <= n; i++) {
                for (int status = 1; status < 16; status++) {
                    lefts[i][status] = lefts[i - 1][status] + infos[i].colors[status];
                }
            }
            // 构建右后缀数组：rights[i][s]表示从i到n个子树中状态为s的路径总数
            long[][] rights = new long[n + 2][16];
            for (int i = n; i >= 1; i--) {
                for (int status = 1; status < 16; status++) {
                    rights[i][status] = rights[i + 1][status] + infos[i].colors[status];
                }
            }
            //处理当前节点到子树节点的直连路径
            for (int status = 1; status < 16; status++) {
                //将子树中的路径状态与当前节点颜色合并
                //rights[1][status] 涵盖第 1 到第 n 个子树的所有路径。
                ans.colors[status | hs] += rights[1][status];
            }
            ans.all += ans.colors[15] << 1;//双向
            //处理不同子树间通过当前节点连接的路径
            for (int from = 1; from <= n; from++) {
                //from子树的所有颜色状态
                for (int fromStatus = 1; fromStatus < 16; fromStatus++) {
                    for (int toStatus = 1; toStatus < 16; toStatus++) {
                        if ((fromStatus | toStatus | hs) == 15) {
                            // 计算"from"子树外的其他子树中状态为toStatus的路径总数，并累积达标路径数量（from子树中的路径数 × 其他子树中的路径数）
                            ans.all += infos[from].colors[fromStatus] * (lefts[from - 1][toStatus] + rights[from + 1][toStatus]);
                        }
                    }
                }
            }
        }
        return ans;
    }

    public static long colors3(Node head) {
        if (head == null) {
            return 0;
        }
        return process3(head).all;
    }

    public static int[][] consider = {{},
            //不同索引对应的有效状态toStatus
            {14, 15},
            {13, 15},
            {12, 13, 14, 15},
            {11, 15},
            {10, 11, 14, 15},
            {9, 11, 13, 15},
            {8, 9, 10, 11, 12, 13, 14, 15},
            {7, 15},
            {6, 7, 14, 15},
            {5, 7, 13, 15},
            {4, 5, 6, 7, 12, 13, 14, 15},
            {3, 7, 11, 15},
            {2, 3, 6, 7, 10, 11, 14, 15},
            {1, 3, 5, 7, 9, 11, 13, 15},
            {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15}
    };

    public static Info process3(Node h) {
        Info ans = new Info();
        int hs = 1 << h.color;
        ans.colors[hs] = 1;
        if (!h.nexts.isEmpty()) {
            int n = h.nexts.size();
            Info[] infos = new Info[n + 1];
            for (int i = 1; i <= n; i++) {
                infos[i] = process3(h.nexts.get(i - 1));
                ans.all += infos[i].all;
            }
            long[][] lefts = new long[n + 2][16];
            for (int i = 1; i <= n; i++) {
                for (int status = 1; status < 16; status++) {
                    lefts[i][status] = lefts[i - 1][status] + infos[i].colors[status];
                }
            }
            long[][] rights = new long[n + 2][16];
            for (int i = n; i >= 1; i--) {
                for (int status = 1; status < 16; status++) {
                    rights[i][status] = rights[i + 1][status] + infos[i].colors[status];
                }
            }
            for (int status = 1; status < 16; status++) {
                ans.colors[status | hs] += rights[1][status];
            }
            ans.all += ans.colors[15] << 1;
            for (int from = 1; from <= n; from++) {
                for (int fromStatus = 1; fromStatus < 16; fromStatus++) {
                    //直接使用预定义的toStatus列表
                    for (int toStatus : consider[fromStatus | hs]) {
                        ans.all += infos[from].colors[fromStatus] * (lefts[from - 1][toStatus] + rights[from + 1][toStatus]);
                    }
                }
            }
        }
        return ans;
    }

    //len：树的总结点数量
    public static Node randomTree(int len, int childs) {
        Node head = new Node((int) (Math.random() * 4));
        generate(head, len - 1, childs);
        return head;
    }

    //pre：当前正在被处理的节点
    public static void generate(Node pre, int restLen, int childs) {
        if (restLen == 0) {
            return;
        }
        //随机生成当前节点的子节点数量
        int size = (int) (Math.random() * childs);
        for (int i = 0; i < size; i++) {
            Node next = new Node((int) (Math.random() * 4));
            generate(next, restLen - 1, childs);//递归生成该子节点的子树
            pre.nexts.add(next);
        }
    }

    public static void printTree(Node head) {
        System.out.print(head.color + " ");
        if (!head.nexts.isEmpty()) {
            System.out.print("(");
            for (Node next : head.nexts) {
                printTree(next);
                System.out.print(",");
            }
            System.out.print(")");
        }
    }

    //使用了层序遍历的方式构建树
    public static Node randomTree() {
        Queue<Node> curQ = new LinkedList<>();//当前层的所有节点
        Queue<Node> nexQ = new LinkedList<>();//下一层要生成的节点
        Node head = new Node((int) (Math.random() * 4));
        curQ.add(head);
        //生成八层子节点
        for (int len = 1; len < 9; len++) {
            while (!curQ.isEmpty()) {
                Node cur = curQ.poll();
                //为当前节点生成5个子节点
                for (int i = 0; i < 5; i++) {
                    Node next = new Node((int) (Math.random() * 4));
                    cur.nexts.add(next);
                    nexQ.add(next);
                }
            }
            Queue<Node> tmp = nexQ;
            nexQ = curQ;
            curQ = tmp;
        }
        return head;
    }

    public static void main(String[] args) {
        int len = 6;
        int childs = 6;
        int testTimes = 3000;
        System.out.println("功能测试开始");
        for (int i = 0; i < testTimes; i++) {
            Node head = randomTree(len, childs);
            int ans1 = colors1(head);
            long ans2 = colors2(head);
            long ans3 = colors3(head);
            if (ans1 != ans2 || ans2 != ans3) {
                System.out.println("Oops");
                printTree(head);
                System.out.println();
                System.out.println(ans1);
                System.out.println(ans2);
                System.out.println(ans3);
                break;
            }
        }
        System.out.println("功能测试结束！");
        System.out.println("性能测试开始！");
        Node h = randomTree();
        System.out.println("节点数量达到 5*(10^5) 规模");
        long start;
        long end;

        start = System.currentTimeMillis();
        long ans2 = colors2(h);
        end = System.currentTimeMillis();
        System.out.println("方法二答案 : " + ans2 + ", 方法二运行时间 : " + (end - start) + " 毫秒");

        start = System.currentTimeMillis();
        long ans3 = colors3(h);
        end = System.currentTimeMillis();
        System.out.println("方法三答案 : " + ans3 + ", 方法三运行时间 : " + (end - start) + " 毫秒");

        System.out.println("性能测试结束");
    }
}
