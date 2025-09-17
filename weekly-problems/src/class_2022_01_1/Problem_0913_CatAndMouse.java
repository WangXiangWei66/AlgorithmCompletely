package class_2022_01_1;

import java.util.ArrayList;

//在一张 无向 图上，节点编号0~N-1。老鼠开始在1节点，猫在2节点，0号节点是洞，老鼠想进洞
//老鼠第先出发，猫后出发，轮流行动。
//在每个玩家的行动中，他们 必须 沿着图中与所在当前位置连通的一条边移动
//此外猫无法移动到洞中（节点 0）。
//然后，游戏在出现以下三种情形之一时结束：
//如果猫和老鼠出现在同一个节点，猫获胜。
//如果老鼠到达洞中，老鼠获胜。
//如果某一位置重复出现（即，玩家的位置和移动顺序都与上一次行动相同），游戏平局。
//给你一张图 graph ，并假设两位玩家都都以最佳状态参与游戏，返回谁获胜
//leetcode链接 : https://leetcode.com/problems/cat-and-mouse/
public class Problem_0913_CatAndMouse {

    //拓扑排序解法
    public static final int MOUSE_TURN = 0, CAT_TURN = 1;//标记当前是谁的回合
    public static final int DRAW = 0, MOUSE_WIN = 1, CAT_WIN = 2;

    public int catMouseGame(int[][] graph) {
        int n = graph.length;
        //记录每个状态的入度
        //[老鼠位置][猫位置][当前回合]
        int[][][] indegree = new int[n][n][2];
        //初始化当前回合老鼠和猫在当前位置的邻居数
        for (int i = 0; i < n; i++) {
            for (int j = 1; j < n; j++) {
                indegree[i][j][MOUSE_TURN] = graph[i].length;
                indegree[i][j][CAT_TURN] = graph[j].length;
            }
        }
        for (int i = 0; i < n; i++) {
            for (int node : graph[0]) {
                indegree[i][node][CAT_TURN]--;//猫不能进0号洞
            }
        }
        //存储每个状态的游戏结果
        int[][][] ans = new int[n][n][2];
        //拓扑排序处理的队列
        int[][] queue = new int[n * n * 2][];
        //队列的头尾指针
        int left = 0;
        int right = 0;
        //两个for循环初始化已知的结果
        for (int j = 1; j < n; j++) {
            ans[0][j][MOUSE_TURN] = MOUSE_WIN;
            ans[0][j][CAT_TURN] = MOUSE_WIN;
            queue[right++] = new int[]{0, j, MOUSE_TURN};
            queue[right++] = new int[]{0, j, CAT_TURN};
        }
        for (int i = 1; i < n; i++) {
            ans[i][i][MOUSE_TURN] = CAT_WIN;
            ans[i][i][CAT_TURN] = CAT_WIN;
            queue[right++] = new int[]{i, i, MOUSE_TURN};
            queue[right++] = new int[]{i, i, CAT_TURN};
        }
        while (left != right) {
            int[] cur = queue[left++];
            int mouse = cur[0], cat = cur[1], turn = cur[2];
            int curAns = ans[mouse][cat][turn];
            //处理当前可能到达状态的前置位置
            for (int[] preState : preState(graph, mouse, cat, turn)) {
                int preMouse = preState[0], preCat = preState[1], preTurn = preState[2];
                if (ans[preMouse][preCat][preTurn] == DRAW) {
                    boolean canWin = (curAns == MOUSE_WIN && preTurn == MOUSE_TURN) ||
                            (curAns == CAT_WIN && preTurn == CAT_TURN);
                    if (canWin) {
                        ans[preMouse][preCat][preTurn] = curAns;
                        queue[right++] = new int[]{preMouse, preCat, preTurn};
                    } else {
                        if (--indegree[preMouse][preCat][preTurn] == 0) {
                            int lose = preTurn == MOUSE_TURN ? CAT_WIN : MOUSE_WIN;
                            ans[preMouse][preCat][preTurn] = lose;
                            queue[right++] = new int[]{preMouse, preCat, preTurn};
                        }
                    }
                }
            }
        }
        return ans[1][2][MOUSE_TURN];
    }

    public ArrayList<int[]> preState(int[][] graph, int mouse, int cat, int turn) {
        ArrayList<int[]> prevStates = new ArrayList<int[]>();
        int prevTurn = turn == MOUSE_TURN ? CAT_TURN : MOUSE_TURN;//前一回合是对方的回合
        if (prevTurn == MOUSE_TURN) {
            for (int prev : graph[mouse]) {
                prevStates.add(new int[]{prev, cat, prevTurn});
            }
        } else {
            for (int prev : graph[cat]) {
                if (prev != 0) {
                    prevStates.add(new int[]{mouse, prev, prevTurn});
                }
            }
        }
        return prevStates;
    }
}
