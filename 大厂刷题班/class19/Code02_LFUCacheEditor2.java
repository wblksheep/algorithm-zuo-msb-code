package class19;

import java.util.HashMap;

// 本题测试链接 : https://leetcode.com/problems/lfu-cache/
// 提交时把类名和构造方法名改为 : LFUCache
public class Code02_LFUCacheEditor2 {

    public Code02_LFUCacheEditor2(int capacity) {
        this.capacity = capacity;
        size = 0;
        records = new HashMap<>();
        heads = new HashMap<>();
        headList = null;
    }

    private final int capacity;
    private int size;
    private HashMap<Integer, Node> records;
    private HashMap<Node, NodeList> heads;
    private static NodeList headList;

    public static class Node {
        private Integer key;
        private Integer value;
        private Integer times;
        private Node up;
        private Node down;

        public Node(Integer k, Integer v, Integer t) {
            key = k;
            value = v;
            times = t;
        }
    }

    public static class NodeList {
        private Node head;
        private Node tail;
        private NodeList last;
        private NodeList next;

        public NodeList(Node node) {
            head = node;
            tail = node;
        }

        public void addNodeFromHead(Node node) {
            node.down = head;
            head.up = node;
            head = node;
        }

        public boolean isEmpty() {
            return head == null;
        }

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
                node.up = null;
                node.down = null;
            }
        }

        public boolean modifyNodeList() {
            if (this.isEmpty()) {
                if (headList == this) {
                    headList = this.next;
                    if (headList != null) {
                        headList.last = null;
                    }
                } else {
                    if (this.last != null) {
                        this.last.next = this.next;
                    }
                    if (this.next != null) {
                        this.next.last = this.last;
                    }
                }
                this.next = null;
                this.last = null;
                return true;
            }
            return false;
        }


    }


    private void move(Node node, NodeList oldNodeList) {
        oldNodeList.deleteNode(node);
//        是否空了
        NodeList preList = oldNodeList.modifyNodeList() ? oldNodeList.last : oldNodeList;
        NodeList nextList = oldNodeList.next;
        if (nextList == null) {
            NodeList newList = new NodeList(node);
            if (preList != null) {
                preList.next = newList;
            }
            newList.last = preList;
            if (headList == null) {
                headList = newList;
            }
            heads.put(node, newList);
        } else {
            if (nextList.head.times.equals(node.times)) {
                nextList.addNodeFromHead(node);
                heads.put(node, nextList);
            } else {
                NodeList newList = new NodeList(node);
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

    public void put(int key, int value) {
        if (capacity == 0) {
            return;
        }
        if (records.containsKey(key)) {
            Node node = records.get(key);
            node.value = value;
            node.times++;
            NodeList curNodeList = heads.get(node);
            move(node, curNodeList);
        } else {
            if (capacity == size) {
                Node deleteNode = headList.tail;
                headList.deleteNode(deleteNode);
                headList.modifyNodeList();
                records.remove(deleteNode.key);
                heads.remove(deleteNode);
                size--;
            }
            Node node = new Node(key, value, 1);
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
            size++;
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