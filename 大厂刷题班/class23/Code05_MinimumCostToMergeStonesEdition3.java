package class23;

import java.util.Arrays;

public class Code05_MinimumCostToMergeStonesEdition3 {

    public static int mergeStones1(int[] stones, int K) {
        int n = stones.length;
        if ((n - 1) % (K - 1) > 0) {
            return -1;
        }
        int[] presum = new int[n + 1];
        for (int i = 0; i < n; i++) {
            presum[i + 1] = presum[i] + stones[i];
        }
        int[][][] dp = new int[n][n][K + 1];
        return process1(0, n - 1, 1, stones, K, presum, dp);
    }

    public static int process1(int L, int R, int P, int[] arr, int K, int[] presum, int[][][] dp) {
        if (dp[L][R][P] != 0) {
            return dp[L][R][P];
        }
        if (L == R) {
            return 0;
        }
        int ans = Integer.MAX_VALUE;
        if (P == 1) {
            ans = process1(L, R, K, arr, K, presum, dp) + presum[R + 1] - presum[L];
        } else {
            for (int mid = L; mid < R; mid += K - 1) {
                int next1 = process1(L, mid, 1, arr, K, presum, dp);
                int next2 = process1(mid + 1, R, P - 1, arr, K, presum, dp);
                ans = Math.min(ans, next1 + next2);
            }
        }
        dp[L][R][P] = ans;
        return ans;
    }

    public static int[][] memo;
    public static int[] s;
    public static int K;
    public static int mergeStones2(int[] stones, int k) {
        int n = stones.length;
        if ((n - 1) % (k - 1) > 0) {
            return -1;
        }
        s = new int[n + 1];
        for (int i = 0; i < n; i++) {
            s[i + 1] = s[i] + stones[i];
        }
        K = k;
        memo = new int[n][n];
        for (int i = 0; i < n; i++) {
            Arrays.fill(memo[i], -1);

        }
        return dfs(0, n - 1);

        // int n = stones.length;
        // if ((n - 1) % (k - 1) > 0) return -1;

        // int[] s = new int[n + 1];
        // for (int i = 0; i < n; i++) {
        //     s[i + 1] = s[i] + stones[i];
        // }

        // int[][] dp = new int[n][n];
        // for (int i = n - 1; i >= 0; i--) {
        //     for (int j = i + 1; j < n; j++) {
        //         dp[i][j] = Integer.MAX_VALUE;
        //         for (int m = i; m < j; m += k - 1) {
        //             dp[i][j] = Math.min(dp[i][j], dp[i][m] + dp[m + 1][j]);
        //         }
        //         if ((j - i) % (k - 1) == 0) {
        //             dp[i][j] += s[j + 1] - s[i];
        //         }
        //     }
        // }
        // return dp[0][n - 1];
    }

    public static int dfs(int i, int j) {
        if (i == j) return 0;
        if (memo[i][j] != -1) {
            return memo[i][j];
        }

        int res = Integer.MAX_VALUE;
        for (int m = i; m < j; m += K - 1) {
            res = Math.min(res, dfs(i, m) + dfs(m + 1, j));
        }
        if ((j - i) % (K - 1) == 0) {
            res += s[j + 1] - s[i];
        }
        return memo[i][j] = res;
    }

    // for test
    public static int[] generateRandomArray(int maxSize, int maxValue) {
        int[] arr = new int[(int) (maxSize * Math.random()) + 1];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) ((maxValue + 1) * Math.random());
        }
        return arr;
    }

    // for test
    public static void printArray(int[] arr) {
        if (arr == null) {
            return;
        }
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        int maxSize = 120;
        int maxValue = 100;
        System.out.println("Test begin");

        // 初始化时间统计变量
        long totalTimeMergeStones1 = 0;
        long totalTimeMergeStones2 = 0;
        int testCount = 0;
        int errorCount = 0;

        for (int testTime = 0; testTime < 100000; testTime++) {
            int[] arr = generateRandomArray(maxSize, maxValue);
            int K = (int) (Math.random() * 7) + 2;

            // 测试 mergeStones1 耗时
            long start1 = System.nanoTime();
            int ans1 = mergeStones1(arr, K);
            long end1 = System.nanoTime();
            totalTimeMergeStones1 += (end1 - start1);

            // 测试 mergeStones2 耗时
            long start2 = System.nanoTime();
            int ans2 = mergeStones2(arr, K);
            long end2 = System.nanoTime();
            totalTimeMergeStones2 += (end2 - start2);

            testCount++;
            if (ans1 != ans2) {
                errorCount++;
                System.out.println("Error! Test case #" + testCount);
                System.out.println("Array: ");
                printArray(arr);
                System.out.println("K = " + K);
                System.out.println("mergeStones1 result: " + ans1);
                System.out.println("mergeStones2 result: " + ans2);
            }
        }

        // 输出时间统计结果
        System.out.println("\nTime Statistics:");
        System.out.println("Total tests: " + testCount);
        System.out.println("Errors: " + errorCount);
        System.out.println("Total time for mergeStones1: " + totalTimeMergeStones1 / 1_000_000 + " ms");
        System.out.println("Average time per test for mergeStones1: " +
                (totalTimeMergeStones1 / testCount) / 1_000.0 + " μs");
        System.out.println("Total time for mergeStones2: " + totalTimeMergeStones2 / 1_000_000 + " ms");
        System.out.println("Average time per test for mergeStones2: " +
                (totalTimeMergeStones2 / testCount) / 1_000.0 + " μs");

        System.out.println("Test end");
    }
}