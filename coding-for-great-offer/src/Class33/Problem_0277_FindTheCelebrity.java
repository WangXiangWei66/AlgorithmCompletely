package Class33;

//给定一个数n，所有人的编号从0到n-1
//给定一个函数 boolean know(int i, int j)，该函数表示i这个人认不认识j这个人，认识关系是单向的
//有了这个函数，你可以检查认识这件事情。
//规定何为明星？1）所有人都认识这个人。2）这个人不认识自己之外的所有人。那么这个人就是明星
//利用know函数，找到明星，返回明星的编号，如果没有明星返回-1。
//Leetcode题目 : https://leetcode.com/problems/find-the-celebrity/
public class Problem_0277_FindTheCelebrity {

    public static boolean knows(int x, int i) {
        return true;
    }

    public int findCelebrity(int n) {
        int cand = 0;//先假设0号是明星选手
        //寻找可能的明星候选人
        for (int i = 0; i < n; i++) {
            if (knows(cand, i)) {
                cand = i;
            }
        }
        //检查候选人是否不认识其他人
        for (int i = 0; i < cand; ++i) {
            if (knows(cand, i)) {
                return -1;
            }
        }
        //判断所有人是否都认识cand
        for (int i = 0; i < n; ++i) {
            if (!knows(i, cand)) {
                return -1;
            }
        }
        return cand;
    }
}
