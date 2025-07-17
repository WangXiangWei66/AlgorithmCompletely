package Class19;

import java.util.HashMap;

//LFU(最不经常使用)内存/缓存替换算法
//Leetcode题目：https://leetcode.com/problems/lfu-cache/
public class Code02_LFUCache {

    public Code02_LFUCache(int K) {
        capacity = K; //缓存容量上限
        size = 0;//当前缓存中的节点数量
        records = new HashMap<>();//存储key到Node的映射（快速查找节点）
        heads = new HashMap<>();//存储Node到其所在桶的映射
        headList = null;//指向整个缓存结构中最左侧的桶（访问频率最低的桶）
    }

    private int capacity; //缓存的大小限制，即K
    private int size; //缓存目前有多少个节点
    private HashMap<Integer, Node> records;//表示key(Integer)由哪个节点(Node)代表
    private HashMap<Node, NodeList> heads; //表示节点（Node)在哪个桶(NodeList)里
    private NodeList headList; //整个结构中位于最左的桶

    //节点的数据结构
    public static class Node {
        public Integer key;
        public Integer value;
        public Integer times; // 这个节点发生get或者set的次数总和
        //同一桶内的节点排序
        public Node up; //节点之间是双向链表，所以有上个节点
        public Node down; // 节点之间是双向链表所以有下一个节点

        public Node(int k, int v, int t) {
            key = k;
            value = v;
            times = t;
        }
    }

    //桶结构
    public static class NodeList {
        public Node head;//最近访问的节点在头部
        public Node tail;
        //按照访问次数排序
        public NodeList last; // 桶之间是双向链表所以有一个桶
        public NodeList next;//桶之间是双向链表所以有后一个桶

        //用一个节点初始化桶，这个节点即是头也是尾巴
        public NodeList(Node node) {
            head = node;
            tail = node;
        }

        //把一个新的节点加入这个桶，新的节点都放在顶端变成新的头部
        public void addNodeFromHead(Node newHead) {
            newHead.down = head;
            head.up = newHead;
            head = newHead;
        }

        //判断这个桶是不是空的
        public boolean isEmpty() {
            return head == null;
        }

        //删除node节点并保证node的上下环境重新连接
        public void deleteNode(Node node) {
            if (head == tail) {
                head = null;
                tail = null;
            } else {
                if (node == head) {
                    head = node.down;
                    head.up = null;
                } else if (node == tail) {
                    tail = node.up;
                    tail.down = null;
                } else {
                    node.up.down = node.down;
                    node.down.up = node.up;
                }
            }
            //最后清理被删除节点的指针，避免内存泄漏
            node.up = null;
            node.down = null;
        }
    }

    //处理节点减少后的桶（可能为空），维护桶之间的双向链表关系——核心是保证桶的有序性（按访问次数递增排列）
    // removeNodeList：刚刚减少了一个节点的桶
    // 这个函数的功能是，判断刚刚减少了一个节点的桶是不是已经空了。
    // 1）如果不空，什么也不做
    // 2)如果空了，removeNodeList还是整个缓存结构最左的桶(headList)。
    // 删掉这个桶的同时也要让最左的桶变成removeNodeList的下一个。
    // 3)如果空了，removeNodeList不是整个缓存结构最左的桶(headList)。
    // 把这个桶删除，并保证上一个的桶和下一个桶之间还是双向链表的连接方式
    // 函数的返回值表示刚刚减少了一个节点的桶是不是已经空了，空了返回true；不空返回false
    // removeNodeList：双向链表中要移除的目标节点
    private boolean modifyHeadList(NodeList removeNodeList) {
        if (removeNodeList.isEmpty()) {
            if (headList == removeNodeList) {//headList：指向双向链表的头节点
                headList = removeNodeList.next;
                if (headList != null) {
                    headList.last = null;
                }
            } else {
                removeNodeList.last.next = removeNodeList.next;
                if (removeNodeList.next != null) {
                    removeNodeList.next.last = removeNodeList.last;
                }
            }
            return true;//节点成功移除的返回结果
        }
        return false;
    }

    // 函数的功能
    // node：要移动的目标节点
    // oldNodeList：节点当前所在的旧桶
    // 整个过程既要保证桶之间仍然是双向链表，也要保证节点之间仍然是双向链表
    private void move(Node node, NodeList oldNodeList) {
        oldNodeList.deleteNode(node);//将节点node从旧桶删除
        // preList表示次数+1的桶的前一个桶是谁
        // 如果oldNodeList删掉node之后还有节点，oldNodeList就是次数+1的桶的前一个桶
        // 如果oldNodeList删掉node之后空了，oldNodeList是需要删除的，所以次数+1的桶的前一个桶，是oldNodeList的前一个
        NodeList preList = modifyHeadList(oldNodeList) ? oldNodeList.last : oldNodeList;
        // nextList表示次数+1的桶的后一个桶是谁
        NodeList nextList = oldNodeList.next;
        if (nextList == null) {
            NodeList newList = new NodeList(node);//要去创建一个新桶
            if (preList != null) {
                preList.next = newList;
            }
            newList.last = preList;
            if (headList == null) {
                headList = newList;
            }
            heads.put(node, newList);//映射表
        } else {
            if (nextList.head.times.equals(node.times)) {
                nextList.addNodeFromHead(node);
                heads.put(node, nextList);
            } else {
                NodeList newList = new NodeList(node);//创建一个新桶
                if (preList != null) {
                    preList.next = newList;
                }
                newList.last = preList;
                newList.next = nextList;
                nextList.last = newList;
                if (headList == nextList) {
                    headList = newList;
                }
                heads.put(node, newList);
            }
        }
    }

    //用于添加或者更新键值对
    //key：用于标识缓存中的数据项
    //value：与键相关联的数据
    public void put(int key, int value) {
        if (capacity == 0) {
            return;
        }
        if (records.containsKey(key)) {
            Node node = records.get(key);
            node.value = value;
            node.times++;
            NodeList curNodeList = heads.get(node);
            move(node, curNodeList);//将该节点移动到对应的桶中
        } else {
            if (size == capacity) {
                Node node = headList.tail;//头节点的尾节点是访问频率最低的点
                headList.deleteNode(node);
                modifyHeadList(headList);
                records.remove(node.key);
                heads.remove(node);
                size--;
            }
            Node node = new Node(key, value, 1);//创建一个新的节点，并将他的访问次数初始化为1
            if (headList == null) {
                headList = new NodeList(node);
            } else {
                if (headList.head.times.equals(node.times)) {
                    headList.addNodeFromHead(node);
                } else {
                    NodeList newList = new NodeList(node);
                    newList.next = headList;
                    headList.last = newList;
                    headList = newList;
                }
            }
            records.put(key, node);
            heads.put(node, headList);
            size++;//最后将节点的数量++
        }
    }

    public int get(int key) {
        if (!records.containsKey(key)) {
            return -1;
        }
        Node node = records.get(key);
        node.times++;
        NodeList curNodeList = heads.get(node);
        move(node, curNodeList);
        return node.value;
    }
}
