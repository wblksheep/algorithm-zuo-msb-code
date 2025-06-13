package class22;

import java.util.Arrays;
import java.util.HashMap;

import static class22.ArrayPrinter.printArray;
import static class22.PositiveRandomArrayGenerator.generatePositiveRandomArray;
import static class22.Code05_TallestBillboardEdition1.tallestBillboard2;

// 本题测试链接 : https://leetcode.com/problems/tallest-billboard/
public class Code05_TallestBillboardEdition3 {

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

    public static int p2(int[] rods) {
        if (rods == null || rods.length < 2) {
            return 0;
        }
        int sum = 0;
        for (int rod : rods) {
            sum += rod;
        }
        int[] dp = new int[sum + 1];
        Arrays.fill(dp, -1);
        dp[0] = 0;
        int[] cur = null;
        for (int rod : rods) {
            cur = dp.clone();
            for (int d = 0; d <= sum; d++) {
                if (cur[d] == -1) continue;
//                if (d + rod <= sum) {
//                    dp[d + rod] = Math.max(cur[d + rod], cur[d]);
//                }
//                对比着看，按我的推理也是这里没有影响，我分析为什么，你设想一种情况，d+rod只会推高dp，后续的更新不会影响dp[d+rod]的值，所以cur[d+rod]一定等于dp[d+rod]的原值。如果cur[d]>dp[d+rod]推高了dp[d+rod]的值，循环进行下去dp[d+rod]也不会再被更新，只会推高dp[d2+rod]的值，一定是有d2>d的。
                if (d + rod <= sum) {
                    dp[d + rod] = Math.max(dp[d + rod], cur[d]);
                }//                         ^^^^^^^^^^^
                int diff = Math.abs(d - rod);
                int newHeight = cur[d] + Math.min(d, rod);
                dp[diff] = Math.max(dp[diff], newHeight);
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
//        System.out.println(tallestBillboard(arr));
        System.out.println(tallestBillboard2(arr));
    }

}
