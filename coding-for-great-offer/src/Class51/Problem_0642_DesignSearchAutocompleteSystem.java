package Class51;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

//为搜索引擎设计一个搜索自动完成系统。用户可以输入一个句子(至少一个单词，并以一个特殊的字符'#'结尾)。
//对于除'#'之外的每个字符，您需要返回与已输入的句子部分前缀相同的前3个历史热门句子。具体规则如下:
//一个句子的热度定义为用户输入完全相同句子的次数。 返回的前3个热门句子应该按照热门程度排序(第一个是最热的)。
//如果几个句子的热度相同，则需要使用ascii代码顺序(先显示较小的一个)。 如果少于3个热门句子，那么就尽可能多地返回。
//当输入是一个特殊字符时，它意味着句子结束，在这种情况下，您需要返回一个空列表。 您的工作是实现以下功能:
//构造函数:
//AutocompleteSystem(String[] sentence, int[] times):这是构造函数。输入是历史数据。
//句子是由之前输入的句子组成的字符串数组。Times是输入一个句子的相应次数。您的系统应该记录这些历史数据。
//现在，用户想要输入一个新句子。下面的函数将提供用户类型的下一个字符:
//List input(char c)：输入c是用户输入的下一个字符。字符只能是小写字母(“a”到“z”)、空格(“”)或特殊字符(“#”)。
//另外，前面输入的句子应该记录在系统中。输出将是前3个历史热门句子，它们的前缀与已经输入的句子部分相同。
//例子:
//操作:AutocompleteSystem(["i love you"， "island"，"ironman"， "i love leetcode"]， [5,3,2,2]) 系统已经追踪到以下句子及其对应的时间:
//"i love you" : 5 times
//"island" : 3 times
//"ironman" : 2 times
//"i love leetcode" : 2 times
//现在，用户开始另一个搜索:
//操作:输入(“i”) 输出:["i love you"， "island"，"i love leetcode"]
//解释: 有四个句子有前缀“i”。其中，《ironman》和《i love leetcode》有着相同的热度。既然“ ” ASCII码为32，“r”ASCII码为114，
//那么“i love leetcode”应该在“ironman”前面。此外，我们只需要输出前3个热门句子，所以“ironman”将被忽略。
//操作:输入(' ') 输出:[“i love you”，“i love leetcode”] 解释: 只有两个句子有前缀“i”。
//操作:输入(' a ') 输出:[] 解释: 没有以“i a”为前缀的句子。
//操作:输入(“#”) 输出:[] 解释: 用户完成输入后，在系统中将句子“i a”保存为历史句。下面的输入将被计算为新的搜索。
//注意:
//输入的句子总是以字母开头，以“#”结尾，两个单词之间只有一个空格。
//要搜索的完整句子不会超过100个。包括历史数据在内的每句话的长度不会超过100句。
//设计一个搜索自动补全系统，它需要包含如下两个方法：
//构造方法：
//AutocompleteSystem(String[] sentences, int[] times): 输入句子sentences，及其出现次数times
//输入方法：
//List input(char c): 输入字符c可以是26个小写英文字母，也可以是空格，以'#'结尾。返回输入字符前缀对应频率最高的至多3个句子，频率相等时按字典序排列。
//leetcode题目：https://leetcode.com/problems/design-search-autocomplete-system/
public class Problem_0642_DesignSearchAutocompleteSystem {

    class AutoCompleteSystem {
        //前缀树节点类
        public class TrieNode {
            public TrieNode father;
            public String path;//前缀
            public TrieNode[] nexts;//子节点数组，对应26个字母和空格

            public TrieNode(TrieNode f, String p) {
                father = f;
                path = p;
                nexts = new TrieNode[27];
            }
        }
        //词频统计类
        public class WordCount implements Comparable<WordCount> {

            public String word;//句子
            public int count;//出现次数

            public WordCount(String w, int c) {
                word = w;
                count = c;
            }
            //先按次数降序，次数相同按照字典序升序
            public int compareTo(WordCount o) {
                return count != o.count ? (o.count - count) : word.compareTo(o.word);
            }
        }

        public final int top = 3;//最多返回三个结果
        public final TrieNode root = new TrieNode(null, "");//前缀树根节点
        //每个前缀树节点对应的热门句子集合
        public HashMap<TrieNode, TreeSet<WordCount>> nodeRankMap = new HashMap<>();
        //记录所有句子的出现次数
        public HashMap<String, WordCount> wordCountMap = new HashMap<>();

        public String path;//当前输入的路径
        public TrieNode cur;//当前前缀树节点
        //将字符映射为0~26索引
        private int f(char c) {
            return c == ' ' ? 0 : (c - '`');
        }


        public AutoCompleteSystem(String[] sentences, int[] times) {
            path = "";
            cur = root;
            for (int i = 0; i < sentences.length; i++) {
                String word = sentences[i];
                int count = times[i];
                WordCount wc = new WordCount(word, count - 1);
                wordCountMap.put(word, wc);
                //模拟用户输入每个字符的过程
                for (char c : word.toCharArray()) {
                    input(c);
                }
                input('#');
            }
        }

        public List<String> input(char c) {
            List<String> ans = new ArrayList<>();
            if (c != '#') {
                path += c;//将字符添加到当前路径
                int i = f(c);
                //子节点不存在，则创建
                if (cur.nexts[i] == null) {
                    cur.nexts[i] = new TrieNode(cur, path);
                }
                cur = cur.nexts[i];
                if (!nodeRankMap.containsKey(cur)) {
                    nodeRankMap.put(cur, new TreeSet<>());
                }
                //从排序中取前三个结果
                int k = 0;
                for (WordCount wc : nodeRankMap.get(cur)) {
                    if (k == top) {
                        break;
                    }
                    ans.add(wc.word);
                    k++;
                }
            }
            if (c == '#' && !path.equals("")) {
                if (!wordCountMap.containsKey(path)) {
                    wordCountMap.put(path, new WordCount(path, 0));
                }
                //创建新的wordCount
                WordCount oldOne = wordCountMap.get(path);
                WordCount newOne = new WordCount(path, oldOne.count + 1);
                //回溯更新所有前缀节点的排序集合
                while (cur != root) {
                    nodeRankMap.get(cur).remove(oldOne);
                    nodeRankMap.get(cur).add(newOne);
                    cur = cur.father;
                }
                wordCountMap.put(path, newOne);
                path = "";
            }
            return ans;
        }
    }
}
