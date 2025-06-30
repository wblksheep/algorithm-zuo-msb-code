package class25;

import java.util.Arrays;

// 本题测试链接 : https://leetcode.com/problems/gas-station/
public class Code04_GasStationChecker2 {

    public static int canCompleteCircuit(int[] gas, int[] cost) {
        if (gas == null || gas.length == 0) {
            return -1;
        }
        if (gas.length == 1) {
            return gas[0] < cost[0] ? -1 : 0;
        }
        boolean[] good = stations(cost, gas);
        for (int i = 0; i < gas.length; i++) {
            if (good[i]) {
                return i;
            }
        }
        return -1;
    }

    public static boolean[] stations(int[] cost, int[] gas) {
        if (cost == null || gas == null || cost.length < 2 || gas.length < 2) {
            return null;
        }
        int init = getInit(cost, gas);
        return init == -1 ? new boolean[cost.length] : getAns(cost, init);
    }

    public static boolean[] getAns(int[] cost, int init) {
        int n = cost.length;
        boolean[] goods = new boolean[n];
        int rest = 0;
        int need = 0;
        int start = init;
        int end = next(init, n);
        do {

//            if (start != init && start == last(end, n)) {
//                break;
//            }//感觉这个就是连通区域有多长能减少一些常数时间上的复杂度，但并不影响很多。

            if (cost[start] < need) {
                need -= cost[start];
            } else {
                rest += cost[start] - need;
                need = 0;
                while (rest >= 0 && start != end) {
                    rest += cost[end];
                    end = next(end, n);
                }
                if (rest >= 0) {
                    goods[start] = true;
                    connectGoods(goods, cost, last(start, n), init, n);
                    break;
                }
            }
            start = last(start, n);
        } while (start != init);
        return goods;
    }

    public static void connectGoods(boolean[] goods, int[] cost, int start, int init, int n) {
        int need = 0;
        while (start != init) {
            if (cost[start] < need) {
                need -= cost[start];
            } else {
                goods[start] = true;
                need = 0;
            }
            start = last(start, n);
        }
    }

    public static int last(int init, int n) {
        return init - 1 >= 0 ? init - 1 : n - 1;
    }

    public static int next(int init, int n) {
        return init + 1 != n ? init + 1 : 0;
    }

    public static int getInit(int[] cost, int[] gas) {
        int n = cost.length;
        int init = -1;
        for (int i = 0; i < n; i++) {
            cost[i] = gas[i] - cost[i];
            if (cost[i] >= 0) {
                init = i;
            }
        }
        return init;
    }

    // 对数器部分：暴力方法
    public static boolean[] bruteForce(int[] gas, int[] cost) {
        if (gas == null || cost == null || gas.length != cost.length || gas.length == 0) {
            return new boolean[0];
        }
        int n = gas.length;
        boolean[] res = new boolean[n];
        for (int i = 0; i < n; i++) {
            int rest = 0;
            for (int step = 0, cur = i; step < n; step++, cur = (cur + 1) % n) {
                rest += gas[cur] - cost[cur];
                if (rest < 0) {
                    break;
                }
            }
            res[i] = rest >= 0;
        }
        return res;
    }

    // 生成随机测试数据
    public static int[][] generateRandomArray(int maxSize, int maxValue) {
        int n = (int) (Math.random() * maxSize) + 2; // 长度至少为2
        int[] gas = new int[n];
        int[] cost = new int[n];
        for (int i = 0; i < n; i++) {
            gas[i] = (int) (Math.random() * (maxValue + 1));
            cost[i] = (int) (Math.random() * (maxValue + 1));
        }
        return new int[][]{gas, cost};
    }

    // 主函数作为对数器测试入口
    public static void main(String[] args) {
        int testTimes = 10000;
        int maxSize = 150;
        int maxValue = 60;
        System.out.println("测试开始");
        for (int i = 0; i < testTimes; i++) {
            int[][] data = generateRandomArray(maxSize, maxValue);
//            int[][] data = {{5, 4, 4, 4, 0, 3, 3, 1, 6, 5, 1, 1, 0, 5},
//                    {5, 1, 2, 4, 0, 4, 5, 6, 2, 2, 2, 0, 1, 3}};
//            int[][] data = {{-1, -2, 2, -2, -6, 2, 1, -1, 1, 1, -3, -3, 0, -4, -2, -3},
//                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
//            int[][] data = {{3, 3},
//                    {3, 2}};
            int[] gas = data[0];
            int[] cost = data[1];
            int[] costCopy = Arrays.copyOf(cost, cost.length); // 防止原数组被修改
            boolean[] res1 = stations(costCopy, gas);
            boolean[] res2 = bruteForce(gas, cost);
            if (!Arrays.equals(res1, res2)) {
                System.out.println("出错了！");
                System.out.println("gas: " + Arrays.toString(gas));
                System.out.println("cost: " + Arrays.toString(cost));
                System.out.println("优化方法结果: " + Arrays.toString(res1));
                System.out.println("暴力方法结果: " + Arrays.toString(res2));
                break;
            }
        }
        System.out.println("测试结束");
    }
}