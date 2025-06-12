package class19;

import java.util.*;

public class LFUCacheTester2 {
    // 暴力LFU实现（作为对数器参考）
    static class NaiveLFUCache {
        private final int capacity;
        private final HashMap<Integer, Integer> keyToValue;
        private final HashMap<Integer, Integer> keyToFreq;
        private final HashMap<Integer, Long> keyToLastOpSeq;
        private long opSeqCounter = 0;

        public NaiveLFUCache(int capacity) {
            this.capacity = capacity;
            this.keyToValue = new HashMap<>();
            this.keyToFreq = new HashMap<>();
            this.keyToLastOpSeq = new HashMap<>();
        }

        public void put(int key, int value) {
            opSeqCounter++;
            if (keyToValue.containsKey(key)) {
                // 更新已存在的key
                keyToValue.put(key, value);
                keyToFreq.put(key, keyToFreq.get(key) + 1);
                keyToLastOpSeq.put(key, opSeqCounter);
            } else {
                // 插入新key前执行淘汰
                if (keyToValue.size() >= capacity) {
                    removeLeastFrequent();
                }
                keyToValue.put(key, value);
                keyToFreq.put(key, 1);
                keyToLastOpSeq.put(key, opSeqCounter);
            }
        }

        public int get(int key) {
            if (keyToValue.containsKey(key)) {
                opSeqCounter++;
                keyToFreq.put(key, keyToFreq.get(key) + 1);
                keyToLastOpSeq.put(key, opSeqCounter);
                return keyToValue.get(key);
            }
            return -1;
        }

        // 淘汰访问次数最少且最久未使用的key
        private void removeLeastFrequent() {
            if (keyToValue.isEmpty()) return;

            // 找到最小访问次数
            int minFreq = Integer.MAX_VALUE;
            for (int freq : keyToFreq.values()) {
                minFreq = Math.min(minFreq, freq);
            }

            // 收集所有具有最小访问次数的key
            List<Integer> candidates = new ArrayList<>();
            for (Map.Entry<Integer, Integer> entry : keyToFreq.entrySet()) {
                if (entry.getValue() == minFreq) {
                    candidates.add(entry.getKey());
                }
            }

            // 若有多个候选key，选择最久未使用的
            long minOpSeq = Long.MAX_VALUE;
            int keyToRemove = -1;
            for (int key : candidates) {
                long opSeq = keyToLastOpSeq.get(key);
                if (opSeq < minOpSeq) {
                    minOpSeq = opSeq;
                    keyToRemove = key;
                }
            }

            // 执行淘汰
            if (keyToRemove != -1) {
                keyToValue.remove(keyToRemove);
                keyToFreq.remove(keyToRemove);
                keyToLastOpSeq.remove(keyToRemove);
            }
        }
    }

    // 生成随机测试
    public static void main(String[] args) {
        int capacity = 3;
        Code02_LFUCacheEditor2 cache1 = new Code02_LFUCacheEditor2(capacity);
        Code02_LFUCacheEditor1 cache2 = new Code02_LFUCacheEditor1(capacity);

        int a1 = 3;
        cache1.put(a1, 1);
        cache1.put(a1, 2);
        cache2.put(a1, 1);
        cache2.put(a1, 2);
        cache1.put(a1, 1);
        cache1.put(a1, 2);
        cache2.put(a1, 1);
        cache2.put(a1, 2);
        cache1.put(a1, 1);
        cache1.put(a1, 2);
        cache2.put(a1, 1);
        cache2.put(a1, 2);
        System.out.println(cache1.get(a1));
        System.out.println(cache2.get(a1));
        int a2 = 5;
        cache1.put(a2, 1);
        cache1.put(a2, 3);
        cache2.put(a2, 1);
        cache2.put(a2, 3);
        System.out.println(cache1.get(a2));
        System.out.println(cache2.get(a2));
        int a3 = 4;
        cache1.put(a3, 1);
        cache1.put(a3, 5);
        cache2.put(a3, 1);
        cache2.put(a3, 5);
        System.out.println(cache1.get(a1));
        System.out.println(cache1.get(a1));
        System.out.println(cache1.get(a3));
        System.out.println(cache1.get(a3));
    }
}