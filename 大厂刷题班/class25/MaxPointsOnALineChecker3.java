package class25;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MaxPointsOnALineChecker3 {
    // 原方法保持不变

    public static int maxPoints(int[][] points) {
        if (points == null || points.length == 0) return 0;
        int n = points.length;
        int ans = 1; // 至少一个点
        for (int i = 0; i < n; i++) {
            int x1 = points[i][0];
            int y1 = points[i][1];
            int samePoints = 1;
            int vertical = 0;   // 垂直线计数（x相同）
            int horizontal = 0; // 水平线计数（y相同）
            HashMap<Integer, Integer> slopeMap = new HashMap<>(); // 斜率编码 -> 计数

            for (int j = i + 1; j < n; j++) {
                int x2 = points[j][0];
                int y2 = points[j][1];
                int dx = x2 - x1;
                int dy = y2 - y1;

                if (dx == 0 && dy == 0) {
                    samePoints++;
                } else if (dx == 0) {
                    vertical++;
                } else if (dy == 0) {
                    horizontal++;
                } else {
                    // 计算最大公约数并化简斜率
                    int gcd = gcd(dx, dy);
                    dx /= gcd;
                    dy /= gcd;
                    // 归一化：确保分母为正（分子符号自适应）
                    if (dy < 0) {
                        dx = -dx;
                        dy = -dy;
                    }
                    // 编码斜率：利用坐标范围[-20000,20000]，偏移后避免冲突
                    int key = (dx + 20000) * 50000 + (dy + 20000);
                    slopeMap.put(key, slopeMap.getOrDefault(key, 0) + 1);
                }
            }

            int maxInLine = Math.max(vertical, horizontal); // 取水平/垂直最大值
            for (int count : slopeMap.values()) {
                if (count > maxInLine) {
                    maxInLine = count;
                }
            }
            maxInLine += samePoints; // 加上重合点
            ans = Math.max(ans, maxInLine);
        }
        return ans;
    }

    public static int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }


    // 暴力方法（对数器验证用）
    public static int bruteForceMaxPoints(int[][] points) {
        if (points == null) return 0;
        int n = points.length;
        if (n <= 2) return n;

        int max = 0;
        for (int i = 0; i < n; i++) {
            // 用字符串表示斜率，例如"3/7"
            Map<String, Integer> slopeMap = new HashMap<>();
            int samePosition = 1; // 包括自己
            int sameX = 0;
            int sameY = 0;

            for (int j = i + 1; j < n; j++) {
                int dx = points[j][0] - points[i][0];
                int dy = points[j][1] - points[i][1];

                if (dx == 0 && dy == 0) {
                    samePosition++;
                } else if (dx == 0) {
                    sameX++;
                } else if (dy == 0) {
                    sameY++;
                } else {
                    // 计算最大公约数并约分
                    int gcd = gcd(dx, dy);
                    dx /= gcd;
                    dy /= gcd;

                    // 统一符号，保证分母为正
                    if (dy < 0) {
                        dx = -dx;
                        dy = -dy;
                    }
                    String slope = dx + "/" + dy;
                    slopeMap.put(slope, slopeMap.getOrDefault(slope, 0) + 1);
                }
            }

            // 计算当前点的最大点数
            int currentMax = Math.max(sameX, sameY);
            for (int count : slopeMap.values()) {
                if (count > currentMax) currentMax = count;
            }
            currentMax += samePosition; // 加上基准点和重合点

            if (currentMax > max) max = currentMax;
        }
        return max;
    }

    // 生成随机测试数据
    public static int[][] generateRandomPoints(int maxSize, int maxValue) {
        int size = (int) (Math.random() * maxSize) + 1; // 至少1个点
        int[][] points = new int[size][2];

        for (int i = 0; i < size; i++) {
            points[i][0] = (int) (Math.random() * (2 * maxValue + 1)) - maxValue;
            points[i][1] = (int) (Math.random() * (2 * maxValue + 1)) - maxValue;
        }
        return points;
    }

    // 主函数作为对数器测试入口
    public static void main(String[] args) {
        int testTimes = 10000;
        int maxSize = 20;    // 最大点数
        int maxValue = 6;  // 坐标范围[-100, 100]
        System.out.println("测试开始");

        for (int i = 0; i < testTimes; i++) {
            int[][] points = generateRandomPoints(maxSize, maxValue);
//            int[][] points = {{3, 6}, {6, -2}, {-7, 3}, {0, -9}, {0, 2}, {-5, -5}, {2, 6}, {-10, 2}};
//            int[][] points = {{3, -6},
//                    {4, 1},
//                    {4, 1},
//                    {4, -3},
//                    {1, 4},
//                    {-6, 5},
//                    {-5, -2},
//                    {-6, -4},
//                    {1, 6},
//                    {1, 1},
//                    {5, -4},
//                    {3, -3}};
            int result1 = maxPoints(points);
            int result2 = bruteForceMaxPoints(points);

            if (result1 != result2) {
                System.out.println("出错了！");
                System.out.println("点数: " + points.length);
                System.out.println("points: " + Arrays.deepToString(points));
                System.out.println("优化方法结果: " + result1);
                System.out.println("暴力方法结果: " + result2);
                return;
            }
        }
        System.out.println("所有测试通过");
    }
}
