package Class08;


public class Code01_Trie {

    class Trie {
        class Node {
            public int pass;
            public int end;
            //nexts数组的初始长度为26.在字符数量不多时，小写字母即可表示
            public Node[] nexts;

            public Node() {
                pass = 0;
                end = 0;
                nexts = new Node[26];
            }
        }

        private Node root;

        public Trie() {
            root = new Node();
        }

        //用底层节点是否为空标记路是否存在
        public void insert(String word) {
            if (word == null) {
                return;
            }
            char[] str = word.toCharArray();
            Node node = root;
            node.pass++;
            int path = 0;//临时变量，当前字符走哪条路
            for (int i = 0; i < str.length; i++) {//从左往右遍历字符
                path = str[i] - 'a';//由字符对应走向哪条路
                if (node.nexts[path] == null) {
                    node.nexts[path] = new Node();
                }
                node = node.nexts[path];
                node.pass++;
            }
            node.end++;
        }

        public void erase(String word) {
            if (countWordsEqualTo(word) != 0) {//先查是否有，为了防止内存泄露
                char[] chs = word.toCharArray();
                Node node = root;
                node.pass--;
                int path;
                for (int i = 0; i < chs.length; i++) {
                    path = chs[i] - 'a';
                    if (--node.nexts[path].pass == 0) {
                        node.nexts[path] = null;
                        return;
                    }
                    node = node.nexts[path];
                }
                node.end--;
            }
        }

        //匹配完全给定字符串的单词数量
        public int countWordsEqualTo(String word) {
            if (word == null) {
                return 0;
            }
            char[] chs = word.toCharArray();
            Node node = root;
            int index = 0;
            for (int i = 0; i < chs.length; i++) {
                index = chs[i] - 'a';
                if (node.nexts[index] == null) {
                    return 0;
                }
                node = node.nexts[index];
            }
            return node.end;
        }

        //用来匹配前缀的数量
        public int countWordsStringWith(String pre) {
            if (pre == null) {
                return 0;
            }
            char[] chs = pre.toCharArray();
            Node node = root;
            int index = 0;
            for (int i = 0; i < chs.length; i++) {
                index = chs[i] - 'a';
                if (node.nexts[index] == null) {
                    return 0;
                }
                node = node.nexts[index];
            }
            return node.pass;
        }
    }
}
