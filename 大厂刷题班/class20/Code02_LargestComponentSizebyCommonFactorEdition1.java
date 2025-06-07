package class20;

import java.util.Arrays;
import java.util.HashMap;

// 本题为leetcode原题
// 测试链接：https://leetcode.com/problems/largest-component-size-by-common-factor/
// 方法1会超时，但是方法2直接通过
public class Code02_LargestComponentSizebyCommonFactorEdition1 {

    public static int largestComponentSize1(int[] arr) {
        int N = arr.length;
        UnionFind set = new UnionFind(N);
        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                if (gcd(arr[i], arr[j]) != 1) {
                    set.union(i, j);
                }
            }
        }
        return set.maxSize();
    }

    // O(1)
    // m,n 要是正数，不能有任何一个等于0
    public static int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    public static int largestComponentSize2(int[] arr) {
        int N = arr.length;
        // arr中，N个位置，在并查集初始时，每个位置自己是一个集合
        UnionFind unionFind = new UnionFind(N);
        //      key 某个因子   value 哪个位置拥有这个因子
        HashMap<Integer, Integer> fatorsMap = new HashMap<>();
        for (int i = 0; i < N; i++) {
            int num = arr[i];
            // 求出根号N， -> limit
            int limit = (int) Math.sqrt(num);
            for (int j = 1; j <= limit; j++) {
                if (num % j == 0) {
                    if (j != 1) {
                        if (!fatorsMap.containsKey(j)) {
                            fatorsMap.put(j, i);
                        } else {
                            unionFind.union(fatorsMap.get(j), i);
                        }
                    }
                    int other = num / j;
                    if (other != 1) {
                        if (!fatorsMap.containsKey(other)) {
                            fatorsMap.put(other, i);
                        } else {
                            unionFind.union(fatorsMap.get(other), i);
                        }
                    }
                }
            }
        }
        return unionFind.maxSize();
    }

    public static class UnionFind {
        private int[] parents;
        private int[] sizes;
        private int[] help;

        public UnionFind(int N) {
            parents = new int[N];
            sizes = new int[N];
            help = new int[N];
            for (int i = 0; i < N; i++) {
                parents[i] = i;
                sizes[i] = 1;
            }
        }

        public int maxSize() {
            int ans = 0;
            for (int size : sizes) {
                ans = Math.max(ans, size);
            }
            return ans;
        }

        private int find(int i) {
            int hi = 0;
            while (i != parents[i]) {
                help[hi++] = i;
                i = parents[i];
            }
            for (hi--; hi >= 0; hi--) {
                parents[help[hi]] = i;
            }
            return i;
        }

        public void union(int i, int j) {
            int f1 = find(i);
            int f2 = find(j);
            if (f1 != f2) {
                int big = sizes[f1] >= sizes[f2] ? f1 : f2;
                int small = big == f1 ? f2 : f1;
                parents[small] = big;
                sizes[big] += sizes[small];
            }
        }
    }

    /* 对数器实现 */
    // 生成随机测试数组
    public static int[] generateRandomArray(int maxSize, int maxValue) {
        int size = (int) (Math.random() * maxSize) + 1;
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = (int) (Math.random() * maxValue) + 1;
        }
        return arr;
    }

    // 运行测试用例并计时
    public static boolean runTestCase(int[] arr, boolean runBruteForce) {
        long start1 = 0, end1 = 0, start2 = 0, end2 = 0;
        int result1 = -1, result2 = -1;

        if (runBruteForce) {
            start1 = System.currentTimeMillis();
            result1 = largestComponentSize1(arr);
            end1 = System.currentTimeMillis();
        }

        start2 = System.currentTimeMillis();
        result2 = largestComponentSize2(arr);
        end2 = System.currentTimeMillis();

        if (runBruteForce) {
            System.out.printf("暴力方法: %d ms | 优化方法: %d ms | 加速比: %.1fx\n",
                    end1 - start1, end2 - start2,
                    (double) (end1 - start1) / (end2 - start2));

            if (result1 != result2) {
                System.out.println("结果不一致！");
                System.out.println("暴力方法结果: " + result1);
                System.out.println("优化方法结果: " + result2);
                System.out.println("输入数组: " + Arrays.toString(arr));
                return false;
            }
        } else {
            System.out.printf("优化方法执行时间: %d ms (数据量: %d)\n",
                    end2 - start2, arr.length);
        }
        return true;
    }

    // 主测试函数
    public static void main(String[] args) {
//        // 固定测试用例
//        int[][] testCases = {
//                {2, 3, 6, 7, 4, 12, 21},
//                {3, 5, 7, 11},
//                {1, 1, 1, 1},
//                {8, 4, 16, 32},
//                {15, 21, 35},
//                {9, 18, 27, 36},
//                {100, 200, 300, 400}
//        };
//
//        System.out.println("========== 固定测试用例验证 ==========");
//        for (int i = 0; i < testCases.length; i++) {
//            System.out.print("测试用例 #" + (i + 1) + ": ");
//            runTestCase(testCases[i], true);
//        }

        // 性能对比测试（小数据量）
        System.out.println("\n========== 小数据量性能对比 ==========");
        int smallTestTimes = 5;
//        int smallMaxSize = 1000;      // 小数据量
//        int smallMaxValue = 1000000;  // 数值范围
        int smallMaxSize = 80;      // 小数据量
        int smallMaxValue = 80;  // 数值范围

        for (int i = 0; i < smallTestTimes; i++) {
            int[] arr = generateRandomArray(smallMaxSize, smallMaxValue);
            System.out.printf("测试 %d/%d: ", i + 1, smallTestTimes);
            runTestCase(arr, true);
        }

        // 大数据量性能测试（仅优化方法）
        System.out.println("\n========== 大数据量性能测试 ==========");
        int largeTestTimes = 5;
        int[] largeSizes = {5000, 10000, 20000, 50000}; // 递增的数据量
        int largeMaxValue = 1000000000;

        for (int size : largeSizes) {
            System.out.println("\n----- 数据量: " + size + " -----");
            for (int i = 0; i < largeTestTimes; i++) {
                int[] arr = generateRandomArray(size, largeMaxValue);
                System.out.printf("测试 %d/%d: ", i + 1, largeTestTimes);
                runTestCase(arr, false); // 不运行暴力方法
            }
        }
    }

}
