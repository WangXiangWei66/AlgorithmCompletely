package Class19;
//本代码使得LRU的时间复杂度都是O(1)
import java.util.HashMap;
//LRU最近最少使用缓存，核心是当缓存满时，优先淘汰最近未使用的数据
//LRU内存/缓存替换算法
//Leetcode题目：https://leetcode.com/problems/lru-cache/
public class Code01_LRUCache {
    //封装缓存操作的公共接口
    //构造函数初始化缓存容量
    public Code01_LRUCache(int capacity) {
        cache = new MyCache<>(capacity);
    }

    private MyCache<Integer, Integer> cache;

    public int get(int key) {
        Integer ans = cache.get(key);
        return ans == null ? -1 : ans;
    }
    //负责更新或添加缓存项
    public void put(int key, int value) {
        cache.set(key, value);
    }
    //双向链表单元，存储键值对及前后引用
    public static class Node<K, V> {
        public K key;
        public V value;
        public Node<K, V> last;
        public Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
    //维护节点的访问顺序，尾部为最近使用的节点
    public static class NodeDoubleLinkedList<K, V> {
        private Node<K, V> head;
        private Node<K, V> tail;

        public NodeDoubleLinkedList() {
            head = null;
            tail = null;
        }

        //现在来了一个新的Node，请挂在尾巴上去
        public void addNode(Node<K, V> newNode) {
            if (newNode == null) {
                return;
            }
            if (head == null) {
                head = newNode;
                tail = newNode;
            } else {
                tail.next = newNode;
                newNode.last = tail;
                tail = newNode;
            }
        }

        // node 入参，一定保证！node在双向链表里！
        // node原始的位置，左右重新连好，然后把node分离出来
        // 移动节点到尾部
        public void moveNodeToTail(Node<K, V> node) {
            if (tail == node) {//如果已经是尾部节点，直接移开
                return;
            }
            //将节点分离
            if (head == node) {
                head = node.next;
                head.last = null;
            } else {
                node.last.next = node.next;
                node.next.last = node.last;
            }
            node.last = tail;
            node.next = null;
            tail.next = node;
            tail = node;
        }
        //将头节点移除
        public Node<K, V> removeHead() {
            if (head == null) {
                return null;
            }
            Node<K, V> res = head;
            if (head == tail) {
                head = null;
                tail = null;
            } else {
                head = res.next;
                res.next = null;
                head.last = null;
            }
            return res;
        }
    }
    //缓存核心逻辑
    public static class MyCache<K, V> {
        private HashMap<K, Node<K, V>> keyNodeMap;//hashMap存储键到节点的映射
        private NodeDoubleLinkedList<K, V> nodeList;//双向链表维护节点的访问顺序
        private final int capacity;//缓存容量上限

        public MyCache(int cap) {
            keyNodeMap = new HashMap<K, Node<K, V>>();
            nodeList = new NodeDoubleLinkedList<K, V>();
            capacity = cap;
        }

        public V get(K key) {
            if (keyNodeMap.containsKey(key)) {
                Node<K, V> res = keyNodeMap.get(key);
                nodeList.moveNodeToTail(res);//将节点移动到尾部
                return res.value;
            }
            return null;
        }

        // set(Key, Value)
        // 新增  更新value的操作
        public void set(K key, V value) {
            if (keyNodeMap.containsKey(key)) {
                Node<K, V> node = keyNodeMap.get(key);
                node.value = value;
                nodeList.moveNodeToTail(node);
            } else {//添加新的节点
                Node<K, V> newNode = new Node<K, V>(key, value);
                keyNodeMap.put(key, newNode);
                nodeList.addNode(newNode);
                if (keyNodeMap.size() == capacity + 1) {
                    removeMostUnusedCache();
                }
            }
        }
        //双向链表的头节点即为最近未使用的节点
        private void removeMostUnusedCache() {
            Node<K, V> removeNode = nodeList.removeHead();
            keyNodeMap.remove(removeNode.key);
        }
    }
}
