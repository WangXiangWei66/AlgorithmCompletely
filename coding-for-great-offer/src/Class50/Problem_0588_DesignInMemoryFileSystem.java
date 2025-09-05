package Class50;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

//设计一个数据结构，和文件系统很像
//类中的方法如下：
//FileSystem()：初始化这个数据结构
//List<String> ls(String path)：如果根据路径path找到的是文件，列出这个文件的名字，如果path找到的是文件夹，列出这个文件夹里的所有文件夹和文件名。（就像ls命令一样）
//void mkdir(String path)：根据path路径建出文件夹，如果中间的文件夹缺失，一路都建立出来。
//void addContentToFile(String filePath, String content)：根据filePath路径找到文件，如果不存在就新建。然后把内容content加在这个文件的尾部。
//String readContentFromFile(String filePath)：根据filePath读出文件内容并返回。
//leetcode题目：https://leetcode.com/problems/design-in-memory-file-system/
public class Problem_0588_DesignInMemoryFileSystem {

    class FileSystem {

        public class Node {
            public String name;//节点的名称（文件名或者目录名）
            public StringBuilder content;//文件内容
            public TreeMap<String, Node> nexts;//按名称排序的子节点集合

            public Node(String n) {
                name = n;
                content = null;
                nexts = new TreeMap<>();//保证ls操作的结果是按照名称排序
            }

            public Node(String n, String c) {
                name = n;
                content = new StringBuilder(c);
                nexts = new TreeMap<>();
            }
        }

        public Node head;//根节点
        //构造函数初始化文件系统
        public FileSystem() {
            head = new Node("");//根节点名称为空字符串
        }

        public List<String> ls(String path) {
            List<String> ans = new ArrayList<>();
            Node cur = head;
            String[] parts = path.split("/");//分割路径
            int n = parts.length;
            for (int i = 1; i < n; i++) {
                if (!cur.nexts.containsKey(parts[i])) {
                    return ans;
                }
                cur = cur.nexts.get(parts[i]);
            }

            if (cur.content == null) {
                ans.addAll(cur.nexts.keySet());//目录返回所有子节点名称
            } else {
                ans.add(cur.name);
            }
            return ans;
        }

        public void mkdir(String path) {
            Node cur = head;
            String[] parts = path.split("/");
            int n = parts.length;
            for (int i = 1; i < n; i++) {
                if (!cur.nexts.containsKey(parts[i])) {
                    cur.nexts.put(parts[i], new Node(parts[i]));
                }
                cur = cur.nexts.get(parts[i]);
            }
        }

        public void addContentToFile(String path, String content) {
            Node cur = head;
            String[] parts = path.split("/");
            int n = parts.length;
            for (int i = 1; i < n - 1; i++) {
                if (!cur.nexts.containsKey(parts[i])) {
                    cur.nexts.put(parts[i], new Node(parts[i]));
                }
                cur = cur.nexts.get(parts[i]);
            }
            //处理文件自身
            if (!cur.nexts.containsKey(parts[n - 1])) {
                cur.nexts.put(parts[n - 1], new Node(parts[n - 1], ""));
            }
            cur.nexts.get(parts[n - 1]).content.append(content);
        }

        public String readContentFromFile(String path) {
            Node cur = head;
            String[] parts = path.split("/");
            int n = parts.length;
            for (int i = 1; i < n; i++) {
                if (!cur.nexts.containsKey(parts[i])) {
                    cur.nexts.put(parts[i], new Node(parts[i]));
                }
                cur = cur.nexts.get(parts[i]);
            }
            return cur.content.toString();
        }
    }
}
