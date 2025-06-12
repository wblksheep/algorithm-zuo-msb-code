package class19;

import java.util.*;

public class LFUCacheTester {
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
        int testTimes = 10000;   // 总测试次数
        int maxKey = 5;         // Key范围 [0, maxKey]
        int maxValue = 10;      // Value范围 [0, maxValue]
        double putProb = 0.5;    // PUT操作概率
        Random rand = new Random();

        for (int test = 0; test < testTimes; test++) {
            int capacity = rand.nextInt(10) + 1; // 随机容量 [1,10]
            Code02_LFUCacheEditor2 cache1 = new Code02_LFUCacheEditor2(capacity);
            Code02_LFUCacheEditor1 cache2 = new Code02_LFUCacheEditor1(capacity);

            int ops = 100; // 每次测试的操作次数
            for (int i = 0; i < ops; i++) {
                int key = rand.nextInt(maxKey);
                int value = rand.nextInt(maxValue);

                // 随机决定操作类型 (PUT/GET)
                if (rand.nextDouble() < putProb) {
                    cache1.put(key, value);
                    cache2.put(key, value);
                } else {
                    int result1 = cache1.get(key);
                    int result2 = cache2.get(key);
                    if (result1 != result2) {
                        System.out.println("测试失败!");
                        System.out.println("容量: " + capacity);
                        System.out.println("操作: GET(" + key + ")");
                        System.out.println("预期: " + result2 + ", 实际: " + result1);
                        return;
                    }
                }
            }
        }
        System.out.println("所有测试通过！");
    }
}