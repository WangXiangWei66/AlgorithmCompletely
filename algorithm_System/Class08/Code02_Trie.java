package Class08;

import java.util.HashMap;

public class Code02_Trie {
    class Trie {
        //该前缀树的路由哈希表实现，如果路很多的情况下
        //如果字符种类很多，用哈希表形式表示下级路，每个KEY都是字符转成整型后的ASCII码值
        class Node {
            public int pass;
            public int end;
            public HashMap<Integer, Node> nexts;

            public Node() {
                pass = 0;
                end = 0;
                nexts = new HashMap<>();
            }
        }

        private Node root;

        public Trie() {
            root = new Node();
        }

        public void insert(String word) {
            if (word == null) {
                return;
            }
            char[] chs = word.toCharArray();
            Node node = root;
            node.pass++;
            int index = 0;
            for (int i = 0; i < chs.length; i++) {
                index = (int) chs[i];
                if (!node.nexts.containsKey(index)) {
                    node.nexts.put(index, new Node());
                }
                node = node.nexts.get(index);
                node.pass++;
            }
            node.end++;
        }

        public void erase(String word) {
            if (countWordsEqualTo(word) != 0) {
                char[] chs = word.toCharArray();
                Node node = root;
                node.pass--;
                int index = 0;
                for (int i = 0; i < chs.length; i++) {
                    index = (int) chs[i];
                    if (--node.nexts.get(index).pass == 0) {
                        node.nexts.remove(index);
                        return;
                    }
                    node = node.nexts.get(index);
                }
                node.end--;
            }
        }

        public int countWordsEqualTo(String word) {
            if (word == null) {
                return 0;
            }
            char[] chs = word.toCharArray();
            Node node = root;
            int index = 0;
            for (int i = 0; i < chs.length; i++) {
                index = (int) chs[i];
                if (!node.nexts.containsKey(index)) {
                    return 0;
                }
                node = node.nexts.get(index);
            }
            return node.end;
        }

        public int countWordsStartingWith(String pre) {
            if (pre == null) {
                return 0;
            }
            char[] chs = pre.toCharArray();
            Node node = root;
            int index = 0;
            for (int i = 0; i < chs.length; i++) {
                index = (int) chs[i];
                if (!node.nexts.containsKey(index)) {
                    return 0;
                }
                node = node.nexts.get(index);
            }
            return node.pass;
        }
    }
}
