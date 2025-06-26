package class25;

import java.util.Arrays;

// 本题测试链接 : https://leetcode.com/problems/gas-station/
public class Code04_GasStationChecker {

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
        if (cost == null || gas == null || cost.length < 2 || cost.length != gas.length) {
            return null;
        }
        int init = changeDisArrayGetInit(cost, gas);
        return init == -1 ? new boolean[cost.length] : enlargeArea(cost, init);
    }

    public static int changeDisArrayGetInit(int[] dis, int[] oil) {
        int init = -1;
        for (int i = 0; i < dis.length; i++) {
            dis[i] = oil[i] - dis[i];
            if (dis[i] >= 0) {
                init = i;
            }
        }
        return init;
    }

    public static boolean[] enlargeArea(int[] dis, int init) {
        boolean[] res = new boolean[dis.length];
        int start = init;
        int end = nextIndex(init, dis.length);
        int need = 0;
        int rest = 0;
        do {
            // 当前来到的start已经在连通区域中，可以确定后续的开始点一定无法转完一圈
            if (start != init && start == lastIndex(end, dis.length)) {
                break;
            }
            // 当前来到的start不在连通区域中，就扩充连通区域
            // start(5) ->  联通区的头部(7) -> 2
            // start(-2) -> 联通区的头部(7) -> 9
            if (dis[start] < need) { // 当前start无法接到连通区的头部
                need -= dis[start];
            } else { // 当前start可以接到连通区的头部，开始扩充连通区域的尾巴
                // start(7) -> 联通区的头部(5)
                rest += dis[start] - need;
                need = 0;
                while (rest >= 0 && end != start) {
                    rest += dis[end];
                    end = nextIndex(end, dis.length);
                }
                // 如果连通区域已经覆盖整个环，当前的start是良好出发点，进入2阶段
                if (rest >= 0) {
                    res[start] = true;
                    connectGood(dis, lastIndex(start, dis.length), init, res);
                    break;
                }
            }
            start = lastIndex(start, dis.length);
        } while (start != init);
        return res;
    }

    // 已知start的next方向上有一个良好出发点
    // start如果可以达到这个良好出发点，那么从start出发一定可以转一圈
    public static void connectGood(int[] dis, int start, int init, boolean[] res) {
        int need = 0;
        while (start != init) {
            if (dis[start] < need) {
                need -= dis[start];
            } else {
                res[start] = true;
                need = 0;
            }
            start = lastIndex(start, dis.length);
        }
    }

    public static int lastIndex(int index, int size) {
        return index == 0 ? (size - 1) : index - 1;
    }

    public static int nextIndex(int index, int size) {
        return index == size - 1 ? 0 : (index + 1);
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
        int maxSize = 15;
        int maxValue = 6;
        System.out.println("测试开始");
        for (int i = 0; i < testTimes; i++) {
//            int[][] data = generateRandomArray(maxSize, maxValue);
            int[][] data = {{5, 4, 4, 4, 0, 3, 3, 1, 6, 5, 1, 1, 0, 5},
                    {5, 1, 2, 4, 0, 4, 5, 6, 2, 2, 2, 0, 1, 3}};
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