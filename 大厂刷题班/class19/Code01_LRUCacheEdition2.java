package class19;

import java.util.HashMap;

// 本题测试链接 : https://leetcode.com/problems/lru-cache/
// 提交时把类名和构造方法名改成 : LRUCache
public class Code01_LRUCacheEdition2 {

    private MyCache<Integer, Integer> cache;

    public Code01_LRUCacheEdition2(int capacity) {
        cache = new MyCache<>(capacity);
    }

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


    public static class NodeDoubleLinkedList<K, V> {
        public Node<K, V> head;
        public Node<K, V> tail;

        public NodeDoubleLinkedList() {
            head = null;
            tail = null;
        }

        public void moveNodeToTail(Node<K, V> node) {
            if (node == tail) {
                return;
            }
            if (head == node) {
                head = node.next;
            } else {
                node.last.next = node.next;
                node.next.last = node.last;
            }
            node.last = tail;
            tail.next = node;
            node.next = null;
            tail = node;
        }

        public void addNode(Node<K, V> node) {
            if (head == null) {
                head = node;
                tail = node;
            } else {
                tail.next = node;
                node.last = tail;
                tail = node;
            }
        }

        public Node<K, V> removeHead() {
            Node<K, V> node = head;
            if (head == tail) {
                head = null;
                tail = null;
            } else {
                head = node.next;
                head.last = null;
            }
            return node;
        }
    }

    public static class MyCache<K, V> {
        private int capacity;
        private HashMap<K, Node<K, V>> keyNodeMap;
        private NodeDoubleLinkedList nodeList;

        public MyCache(int cap) {
            keyNodeMap = new HashMap<>();
            nodeList = new NodeDoubleLinkedList();
            capacity = cap;
        }

        public void put(K key, V value) {
            if (keyNodeMap.containsKey(key)) {
                Node<K, V> node = keyNodeMap.get(key);
                node.value = value;
                nodeList.moveNodeToTail(node);
            } else {
                Node<K, V> node = new Node<>(key, value);
                keyNodeMap.put(key, node);
                nodeList.addNode(node);
                if (keyNodeMap.size() == capacity + 1) {
                    removeMostUnusedNode();
                }
            }
        }

        private void removeMostUnusedNode() {
            Node<K, V> head = nodeList.removeHead();
            keyNodeMap.remove(head.key);
        }

        public V get(K key) {
            if (keyNodeMap.containsKey(key)) {
                Node<K, V> node = keyNodeMap.get(key);
                nodeList.moveNodeToTail(node);
                return node.value;
            }
            return null;
        }
    }

    public void put(int key, int value) {
        cache.put(key, value);
    }

    public int get(int key) {
        Integer ans = cache.get(key);
        return ans == null ? -1 : ans;
    }

    public static void main(String[] args) {
        Code01_LRUCacheEdition2 lruCache = new Code01_LRUCacheEdition2(3);
        lruCache.put(1, 1);
        lruCache.put(2, 2);
        lruCache.put(3, 3);
        System.out.println(lruCache.get(1));
        System.out.println(lruCache.get(2));
        lruCache.put(4, 4);
        System.out.println(lruCache.get(3));
    }
}
