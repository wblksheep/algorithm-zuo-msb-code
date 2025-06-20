package class24;

import java.util.Arrays;
import java.util.Comparator;

public class Code02_KthMinPairEdition1 {

    public static class Pair {
        public int x;
        public int y;

        Pair(int a, int b) {
            x = a;
            y = b;
        }
    }

    public static class PairComparator implements Comparator<Pair> {

        @Override
        public int compare(Pair arg0, Pair arg1) {
            return arg0.x != arg1.x ? arg0.x - arg1.x : arg0.y - arg1.y;
        }

    }

    // O(N^2 * log (N^2))的复杂度，你肯定过不了
    // 返回的int[] 长度是2，{3,1} int[2] = [3,1]
    public static int[] kthMinPair1(int[] arr, int k) {
        int N = arr.length;
        if (k > N * N) {
            return null;
        }
        Pair[] pairs = new Pair[N * N];
        int index = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                pairs[index++] = new Pair(arr[i], arr[j]);
            }
        }
        Arrays.sort(pairs, new PairComparator());
        return new int[]{pairs[k - 1].x, pairs[k - 1].y};
    }

    // O(N*logN)的复杂度，你肯定过了
    public static int[] kthMinPair2(int[] arr, int k) {
        int N = arr.length;
        if (k > N * N) {
            return null;
        }
        // O(N*logN)
        Arrays.sort(arr);
        // 第K小的数值对，第一维数字，是什么 是arr中
        int fristNum = arr[(k - 1) / N];
        int lessFristNumSize = 0;// 数出比fristNum小的数有几个
        int fristNumSize = 0; // 数出==fristNum的数有几个
        // <= fristNum
        for (int i = 0; i < N && arr[i] <= fristNum; i++) {
            if (arr[i] < fristNum) {
                lessFristNumSize++;
            } else {
                fristNumSize++;
            }
        }
        int rest = k - (lessFristNumSize * N);
        return new int[]{fristNum, arr[(rest - 1) / fristNumSize]};
    }

    // 为了测试，生成值也随机，长度也随机的随机数组
    public static int[] getRandomArray(int max, int len) {
        int[] arr = new int[(int) (Math.random() * len) + 1];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) (Math.random() * max) - (int) (Math.random() * max);
        }
        return arr;
    }

    // 为了测试
    public static int[] copyArray(int[] arr) {
        if (arr == null) {
            return null;
        }
        int[] res = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            res[i] = arr[i];
        }
        return res;
    }

    // 随机测试了百万组，保证三种方法都是对的
    public static void main(String[] args) {
        int max = 3;
        int len = 30;
        int testTimes = 100000;
        System.out.println("test bagin, test times : " + testTimes);
        for (int i = 0; i < testTimes; i++) {
            int[] arr = getRandomArray(max, len);
//            int[] arr = {0, 0, -1, 2, -1, 1, 1, -2};
            int[] arr1 = copyArray(arr);
            int[] arr2 = copyArray(arr);
            int[] arr3 = copyArray(arr);
            int N = arr.length * arr.length;
            int k = (int) (Math.random() * N) + 1;
//            int k = 38;
            int[] ans1 = kthMinPair1(arr1, k);
            int[] ans2 = kthMinPair2(arr2, k);
            int[] ans3 = kthMinPair3(arr3, k);
            if (ans1[0] != ans2[0] || ans2[0] != ans3[0] || ans1[1] != ans2[1] || ans2[1] != ans3[1]) {
                System.out.println("Oops!");
            }
        }
        System.out.println("test finish");
    }

    public static int[] kthMinPair3(int[] arr, int k) {
        int n = arr.length;
        int firstNum = getMin(arr, (k - 1) / n);
        int lessNum = 0;
        int allFirst = 0;
        for (int i = 0; i < n; i++) {
            if (arr[i] < firstNum) {
                lessNum++;
            }
            if (arr[i] == firstNum) {
                allFirst++;
            }
        }
        k = k - lessNum * n;
        return new int[]{firstNum, getMin(arr, (k - 1) / allFirst)};
    }

    public static int getMin(int[] arr, int k) {
        int L = 0;
        int R = arr.length - 1;
        while (L < R) {
            int x = arr[L + (int) (Math.random() * (R - L + 1))];
            int[] part = partition(arr, L, R, x);
            if (k < part[0]) {
                R = part[0] - 1;
            } else if (k > part[1]) {
                L = part[1] + 1;
            } else {
                return x;
            }
        }
        return arr[L];
    }

    public static int[] partition(int[] arr, int l, int r, int x) {
        int i = l;
        while (i <= r) {
            if (arr[i] == x) {
                i++;
            } else if (arr[i] < x) {
                swap(arr, l++, i++);
            } else {
                swap(arr, i, r--);
            }
        }
        return new int[]{l, r};
    }

    public static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

}
