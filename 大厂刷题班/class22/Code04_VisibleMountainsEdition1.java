package class22;

import java.util.PriorityQueue;

import static class22.ArrayPrinter.printArray;

import static class22.Code04_VisibleMountains.getVisibleNum;
import static class22.PositiveRandomArrayGenerator.generatePositiveRandomArray;

public class Code04_VisibleMountainsEdition1 {

    // 栈中放的记录，
    // value就是指，times是收集的个数
    public static class Record {
        public int value;
        public int times;

        public Record(int value) {
            this.value = value;
            this.times = 1;
        }
    }

    public static int getVisibleNum2(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;
        }
        int n = arr.length;
        int maxIndex = 0;
        for (int i = 0; i < n; i++) {
            maxIndex = arr[i] > arr[maxIndex] ? i : maxIndex;
        }
        PriorityQueue<Record> minHeap = new PriorityQueue<>((a, b) -> a.value - b.value);
        minHeap.add(new Record(arr[maxIndex]));
        int index = nextIndex(maxIndex, n);
        int ans = 0;
        while (index != maxIndex) {
            while (arr[index] > minHeap.peek().value) {
                Record min = minHeap.poll();
                int k = min.times;
                ans += 2 * k + getInternal(k);
            }
            if (arr[index] == minHeap.peek().value) {
                minHeap.peek().times++;
            } else {
                minHeap.add(new Record(arr[index]));
            }
            index = nextIndex(index, n);
        }
        while (minHeap.size() > 2) {
            Record min = minHeap.poll();
            int k = min.times;
            ans += 2 * k + getInternal(k);
        }
        if (minHeap.size() == 2) {
            Record min = minHeap.poll();
            int k = min.times;
            int lastTimes = minHeap.peek().times;
            ans += (lastTimes == 1 ? 0 : k) + k + getInternal(k);
        }
        if (minHeap.size() == 1) {
            Record min = minHeap.poll();
            int k = min.times;
            ans += getInternal(k);
        }
        return ans;
    }

    public static int getInternal(int k) {
        return k == 1 ? 0 : (k * (k - 1) / 2);
    }

    public static int nextIndex(int i, int size) {
        return (i + 1) % size;
    }

    // 环形数组中当前位置为i，数组长度为size，返回i的上一个位置
    public static int lastIndex(int i, int size) {
        return (i - 1 + size) % size;
    }

    public static void main(String[] args) {
//        int[] arr = {6, 9, 9, 3, 1, 2, 7, 2, 7, 5};
//        int[] arr = {2, 10, 6, 5, 10, 9, 3, 6, 9, 1, 6, 1, 9, 4, 6, 10, 10, 2, 1, 8};
        int[] arr = generatePositiveRandomArray(100000, 100);
        printArray("arr", arr);
        System.out.println(getVisibleNum2(arr));
        System.out.println(getVisibleNum(arr));
    }


}