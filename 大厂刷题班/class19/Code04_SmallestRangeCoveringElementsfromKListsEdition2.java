package class19;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

// 本题测试链接 : https://leetcode.com/problems/smallest-range-covering-elements-from-k-lists/
public class Code04_SmallestRangeCoveringElementsfromKListsEdition2 {

    public static class Node {
        public int value;
        public int arrid;
        public int index;

        public Node(int v, int ai, int i) {
            value = v;
            arrid = ai;
            index = i;
        }
    }

    public static class NodeComparator implements Comparator<Node> {

        @Override
        public int compare(Node o1, Node o2) {
            return o1.value != o2.value ? o1.value - o2.value : o1.arrid - o2.arrid;
        }

    }

    public static int[] smallestRange(List<List<Integer>> nums) {
        int N = nums.size();
        // 最小堆，按值排序
        PriorityQueue<Node> minHeap = new PriorityQueue<>((a, b) -> a.value - b.value);
        int currentMax = Integer.MIN_VALUE;

        // 初始化：添加每个列表的第一个元素
        for (int i = 0; i < N; i++) {
            int value = nums.get(i).get(0);
            minHeap.offer(new Node(value, i, 0));
            currentMax = Math.max(currentMax, value);
        }

        int minRange = Integer.MAX_VALUE;
        int start = -1, end = -1;

        while (minHeap.size() == N) {
            Node minNode = minHeap.poll();
            int currentMin = minNode.value;

            // 更新最小范围
            if (currentMax - currentMin < minRange) {
                minRange = currentMax - currentMin;
                start = currentMin;
                end = currentMax;
            }

            // 获取下一个元素
            int nextIndex = minNode.index + 1;
            if (nextIndex < nums.get(minNode.arrid).size()) {
                int nextValue = nums.get(minNode.arrid).get(nextIndex);
                minHeap.offer(new Node(nextValue, minNode.arrid, nextIndex));
                currentMax = Math.max(currentMax, nextValue);
            }
        }

        return new int[]{start, end};
    }

    public static void main(String[] args) {
        List<List<Integer>> m = Arrays.asList(Arrays.asList(1, 2, 3), Arrays.asList(1, 2, 3), Arrays.asList(1, 2, 3));
        int[] res = smallestRange(m);
        int n = res.length;
        for (int i = 0; i < n - 1; i++) {
            System.out.print(res[i] + " ");
        }
        System.out.println(res[n - 1]);
    }
}
