package Class37;

//来自字节
//扑克牌中的红桃J和梅花Q找不到了，为了利用剩下的牌做游戏，小明设计了新的游戏规则
//1) A,2,3,4....10,J,Q,K分别对应1到13这些数字，大小王对应0
//2) 游戏人数为2人，轮流从牌堆里摸牌，每次摸到的牌只有“保留”和“使用”两个选项，且当前轮必须做出选择
//3) 如果选择“保留”当前牌，那么当前牌的分数加到总分里，并且可以一直持续到游戏结束
//4) 如果选择“使用”当前牌，那么当前牌的分数*3，加到总分上去，但是只有当前轮，下一轮，下下轮生效，之后轮效果消失。
//5) 每一轮总分大的人获胜
//   假设小明知道每一轮对手做出选择之后的总分，返回小明在每一轮都赢的情况下，最终的最大分是多少
//   如果小明怎么都无法保证每一轮都赢，返回-1
public class Code02_GameForEveryStepWin {
    //cands：牌堆数组，存储每一轮摸到的牌的分数
    //scores：对手每一轮的总分
    //index：当前轮次索引
    //hold：保留的分数总和
    //cur：当前轮仍在生效的“使用”分数
    //next：下一轮将生效的“使用”分数
    public static int f(int[] cands, int[] scores, int index, int hold, int cur, int next) {
        if (index == 25) {
            //总分：保留分 + 当前生效分 + 本轮使用的分数×3
            int all = hold + cur + cands[index] * 3;
            if (all <= scores[index]) {
                return -1;
            }
            return all;
        }
        //保留当前牌
        int all1 = hold + cur + cands[index];
        int p1 = -1;
        if (all1 > scores[index]) {
            p1 = f(cands, scores, index + 1, hold + cands[index], next, 0);
        }
        //使用当前牌
        int all2 = hold + cur + cands[index] * 3;
        int p2 = -1;
        if (all2 > scores[index]) {
            p2 = f(cands, scores, index + 1, hold, next + cands[index] * 3, cands[index] * 3);
        }
        return Math.max(p1, p2);
    }


    public static int process(int[] cands, int[] scores, int index, int hold, int cur, int next) {
        if (index == 25) {
            int all = hold + cur + cands[index] * 3;
            if (all > scores[index]) {
                return all;
            } else {
                return -1;
            }
        } else {
            int d1 = hold + cur + cands[index];
            int p1 = -1;
            if (d1 > scores[index]) {
                p1 = process(cands, scores, index + 1, hold + cands[index], next, 0);
            }
            int d2 = hold + cur + cands[index] * 3;
            int p2 = -1;
            if (d2 > scores[index]) {
                p2 = process(cands, scores, index + 1, hold, next + cands[index] * 3, cands[index] * 3);
            }
            return Math.max(p1, p2);
        }
    }

    public static int p(int[] cands, int[] scores, int index, int hold, int cur, int next) {
        if (index == 25) {
            int all = hold + cur * 3 + cands[index] * 3;
            if (all <= scores[index]) {
                return -1;
            }
            return all;
        }

        int all1 = hold + cur * 3 + cands[index];
        int p1 = -1;
        if (all1 > scores[index]) {
            p1 = f(cands, scores, index + 1, hold + cands[index], next, 0);
        }
        int all2 = hold + cur * 3 + cands[index] * 3;
        int p2 = -1;
        if (all2 > scores[index]) {
            p2 = f(cands, scores, index + 1, hold, next + cands[index], cands[index]);
        }
        return Math.max(p1, p2);
    }
}
