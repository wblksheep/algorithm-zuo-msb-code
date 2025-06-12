package class22;

import java.util.Arrays;
import java.util.HashMap;

import static class22.ArrayPrinter.printArray;
import static class22.PositiveRandomArrayGenerator.generatePositiveRandomArray;

// 本题测试链接 : https://leetcode.com/problems/tallest-billboard/
public class Code05_TallestBillboardEdition2 {

    public static int tallestBillboard(int[] rods) {
        // key 集合对的某个差
        // value 满足差值为key的集合对中，最好的一对，较小集合的累加和
        // 较大 -> value + key
        HashMap<Integer, Integer> dp = new HashMap<>(), cur;
        dp.put(0, 0);// 空集 和 空集
        for (int num : rods) {
            if (num != 0) {
                // cur 内部数据完全和dp一样
                cur = new HashMap<>(dp); // 考虑x之前的集合差值状况拷贝下来
                for (int d : cur.keySet()) {
                    int diffMore = cur.get(d); // 最好的一对，较小集合的累加和
                    // x决定放入，比较大的那个
                    dp.put(d + num, Math.max(diffMore, dp.getOrDefault(num + d, 0)));
                    // x决定放入，比较小的那个
                    // 新的差值 Math.abs(x - d)
                    // 之前差值为Math.abs(x - d)，的那一对，就要和这一对，决策一下
                    // 之前那一对，较小集合的累加和diffXD
                    int diffXD = dp.getOrDefault(Math.abs(num - d), 0);
                    if (d >= num) { // x决定放入比较小的那个, 但是放入之后，没有超过这一对较大的那个
                        dp.put(d - num, Math.max(diffMore + num, diffXD));
                    } else { // x决定放入比较小的那个, 但是放入之后，没有超过这一对较大的那个
                        dp.put(num - d, Math.max(diffMore + d, diffXD));
                    }
                }
            }
        }
        return dp.get(0);
    }


    public static int p2(int[] arr) {
        if (arr == null || arr.length < 2) return 0;
        int n = arr.length;
        int sum = 0;
        for (int i = 0; i < n; i++) {
            sum += arr[i];
        }
        int[] dp = new int[sum + 1];
        Arrays.fill(dp, -1);
        dp[0] = 0;
        int[] cur = new int[sum + 1];
        for (int rod : arr) {
            cur = dp.clone();
            for (int d = 0; d <= sum; d++) {
                if (cur[d] == -1) {
                    continue;
                }
                if (d + rod <= sum) {
                    dp[d + rod] = Math.max(cur[d], dp[d + rod]);
                }
                int diff = Math.abs(d - rod);
                int newHeight = cur[d] + Math.min(d, rod);
                dp[diff] = Math.max(cur[diff], newHeight);
            }
        }
        return dp[0];
    }

    public static void main(String[] args) {
        int[] arr = generatePositiveRandomArray(1000, 1000);
//        int[] arr = {5, 1, 3, 5, 2};
//        int[] arr = {6, 9, 10, 6, 4, 10, 9, 9, 9, 9, 5, 1, 4, 7, 6, 5, 8, 8, 3, 8};
        printArray("arr", arr);
        System.out.println(p2(arr));
        System.out.println(tallestBillboard(arr));
    }

}
