package class_2022_03_2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

//来自字节飞书团队
//语法补全功能，比如"as soon as possible"
//当我们识别到"as soon as"时, 基本即可判定用户需要键入"possible"
//设计一个统计词频的模型，用于这个功能
//类似(prefix, next word)这样的二元组
//比如一个上面的句子"as soon as possible"
//有产生如下的二元组(as, soon, 1)、(as soon, as, 1)、(as soon as, possible, 1)
//意思是这一个句子产生了如下的统计：
//当前缀为"as"，接下来的单词是"soon"，有了1个期望点
//当前缀为"as soon"，接下来的单词是"as"，有了1个期望点
//当前缀为"as soon as"，接下来的单词是"possible"，有了1个期望点
//那么如果给你很多的句子，当然就可以产生很多的期望点，同一个前缀下，同一个next word的期望点可以累加
//现在给你n个句子，让你来建立统计
//然后给你m个句子，作为查询
//最后给你k，表示每个句子作为前缀的情况下，词频排在前k名的联想
//返回m个结果，每个结果最多k个单词
public class Code03_AiFill {

    public static class TrieNode {
        public String word;//当前节点存储的单词
        public int times;//该单词作为后续单词出现的次数
        public HashMap<String, TrieNode> nextNodes;//单词到节点的映射
        public TreeSet<TrieNode> nextRanks;//按出现次数排序的子节点集合

        public TrieNode(String w) {
            word = w;
            times = 1;
            nextNodes = new HashMap<>();//用于快速查找子节点
            nextRanks = new TreeSet<>((a, b) -> a.times != b.times ? (b.times - a.times) : a.word.compareTo(b.word));
        }
    }

    public static class AI {
        public TrieNode root;
        public int topK;//需要返回的前K个建议

        public AI(List<String> sentences, int k) {
            root = new TrieNode("");
            topK = k;
            for (String sentence : sentences) {
                fill(sentence);
            }
        }

        public void fill(String sentence) {
            TrieNode cur = root;
            TrieNode next = null;
            //按空格分割句子为单词数组
            for (String word : sentence.split(" ")) {
                if (!cur.nextNodes.containsKey(word)) {
                    next = new TrieNode(word);
                    cur.nextNodes.put(word, next);
                    cur.nextRanks.add(next);
                } else {
                    next = cur.nextNodes.get(word);
                    cur.nextRanks.remove(next);
                    next.times++;
                    cur.nextRanks.add(next);
                }
                cur = next;
            }
        }
        //根据输入的前缀句子，返回联想的建议
        public List<String> suggest(String sentence) {
            List<String> ans = new ArrayList<>();
            TrieNode cur = root;
            for (String word : sentence.split(" ")) {
                if (!cur.nextNodes.containsKey(word)) {
                    return ans;
                } else {
                    cur = cur.nextNodes.get(word);
                }
            }
            //从当前节点的排序集合中获取前K个单词
            for (TrieNode n : cur.nextRanks) {
                ans.add(n.word);
                if (ans.size() == topK) {
                    break;
                }
            }
            return ans;
        }
    }

    public static void main(String[] args) {
        ArrayList<String> sentences = new ArrayList<>();
        sentences.add("i think you are good");
        sentences.add("i think you are fine");
        sentences.add("i think you are good man");
        int k = 2;
        AI ai = new AI(sentences, k);
        for (String ans : ai.suggest("i think you are")) {
            System.out.println(ans);
        }
        System.out.println("=====");
        //追加训练模型
        ai.fill("i think you are fucking good");
        ai.fill("i think you are fucking great");
        ai.fill("i think you are fucking genius");
        for (String ans : ai.suggest("i think you are")) {
            System.out.println(ans);
        }
        System.out.println("=====");
    }

}
