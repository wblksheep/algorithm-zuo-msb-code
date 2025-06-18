package class20;

import java.util.Arrays;
import java.util.HashMap;

import static class20.ArrayPrinter.printArray;

// 本题为leetcode原题
// 测试链接：https://leetcode.com/problems/largest-component-size-by-common-factor/
// 方法1会超时，但是方法2直接通过
public class Code02_LargestComponentSizebyCommonFactorEdition2 {

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
        printArray("parents", unionFind.parents);
        printArray("sizes", unionFind.sizes);
        return unionFind.maxSize();
    }

    public static class UnionFind {
        public int[] parents;
        public int[] sizes;
        public int[] help;

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

        public int find(int i) {
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
//        int[] arr = generateRandomArray(20, 20);
        int[] arr = {4, 2, 2, 9, 7, 15};
        printArray("arr", arr);
        runTestCase(arr, false);
        arr = new int[]{17, 10, 18, 14, 13, 15, 14, 10, 14, 7, 6, 10, 20, 20, 20, 6};
//        arr = generateRandomArray(20, 20);
        printArray("arr", arr);
        runTestCase(arr, false);

        arr = new int[]{9, 8, 3, 2, 1, 2, 3, 5, 7, 8, 9};
//        arr = generateRandomArray(20, 20);
        printArray("arr", arr);
        runTestCase(arr, false);
    }

}
