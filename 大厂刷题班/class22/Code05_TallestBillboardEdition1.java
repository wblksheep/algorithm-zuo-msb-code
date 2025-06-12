package class22;

import java.util.Arrays;
import java.util.HashMap;

import static class22.PositiveRandomArrayGenerator.generatePositiveRandomArray;

import static class22.ArrayPrinter.printArray;

// 本题测试链接 : https://leetcode.com/problems/tallest-billboard/
public class Code05_TallestBillboardEdition1 {

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
        int sum = 0;
        // 计算所有钢筋的总长度
        for (int rod : rods) {
            sum += rod;
        }

        // dp数组：dp[d]表示高度差为d时，公共高度（较短支架的高度）的最大值
        int[] dp = new int[sum + 1];
        // 初始化所有状态为-1（不可达）
        Arrays.fill(dp, -1);
        // 高度差为0时，公共高度初始化为0
        dp[0] = 0;

        // 遍历每根钢筋
        for (int rod : rods) {
            // 复制当前状态，用于本轮的更新
            int[] current = dp.clone();

            // 遍历所有可能的高度差
            for (int d = 0; d <= sum; d++) {
                // 跳过不可达状态
                if (current[d] < 0) continue;

                // 1. 将钢筋加到较长支架上
                if (d + rod <= sum) {
                    // 更新dp[d+rod]状态：公共高度保持不变
                    dp[d + rod] = Math.max(dp[d + rod], current[d]);
                }

                // 2. 将钢筋加到较短支架上
                int diff = Math.abs(d - rod);
                // 计算新的公共高度：原公共高度加上两者中较小值
                int newPublic = current[d] + Math.min(d, rod);
                // 更新状态
                if (newPublic > dp[diff]) {
                    dp[diff] = newPublic;
                }
            }
        }

        // 返回高度差为0时的公共高度（即最大高度）
        return dp[0];
    }


    public static void main(String[] args) {
//        int[] arr = generatePositiveRandomArray(5, 5);
        int[] arr = {5, 1, 3, 5, 2};
//        int[] arr = {6, 9, 10, 6, 4, 10, 9, 9, 9, 9, 5, 1, 4, 7, 6, 5, 8, 8, 3, 8};
        printArray("arr", arr);
        System.out.println(p2(arr));
        System.out.println(tallestBillboard(arr));
    }

}
