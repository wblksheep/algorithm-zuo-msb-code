package class22;

import java.util.Arrays;
import java.util.Random;
import java.util.StringJoiner;
import java.util.concurrent.ThreadLocalRandom;

// 本题测试链接 : https://leetcode.com/problems/trapping-rain-water/
public class Code02_TrappingRainWaterEdition1 {

    public static int trap(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;
        }
        int N = arr.length;
        int L = 1;
        int leftMax = arr[0];
        int R = N - 2;
        int rightMax = arr[N - 1];
        int water = 0;
        while (L <= R) {
            if (leftMax <= rightMax) {
                water += Math.max(0, leftMax - arr[L]);
                leftMax = Math.max(leftMax, arr[L++]);
            } else {
                water += Math.max(0, rightMax - arr[R]);
                rightMax = Math.max(rightMax, arr[R--]);
            }
        }
        return water;
    }

    public static void main(String[] args) {
        int[] arr = generatePositiveRandomArray(10, 10);
        printArray("arr", arr);
        System.out.println(trap(arr));
    }

    /**
     * 生成一个只包含正整数的随机数组
     *
     * @param length   数组长度
     * @param maxValue 随机数的最大值（包含）
     * @return 包含正整数的随机数组
     * @throws IllegalArgumentException 如果参数无效
     */
    public static int[] generatePositiveRandomArray(int length, int maxValue) {
        // 参数验证
        if (length < 0) {
            throw new IllegalArgumentException("数组长度不能为负数: " + length);
        }
        if (maxValue < 1) {
            throw new IllegalArgumentException("最大值必须至少为1: " + maxValue);
        }
        if (length > Integer.MAX_VALUE - 5) { // 防止接近极限值时溢出
            throw new IllegalArgumentException("数组长度过大: " + length);
        }

        // 使用ThreadLocalRandom提高并发性能
        Random rand = ThreadLocalRandom.current();
        int[] array = new int[length];

        // 填充数组
        for (int i = 0; i < length; i++) {
            // 生成1到maxValue之间的随机整数（包含两端）
            array[i] = rand.nextInt(maxValue) + 1;
        }

        return array;
    }

    /**
     * 打印任意类型数组，格式为"变量名: [元素1, 元素2, ...]"
     *
     * @param varName 变量名
     * @param array   要打印的数组
     */
    public static void printArray(String varName, Object array) {
        if (!array.getClass().isArray()) {
            throw new IllegalArgumentException("输入必须是数组类型");
        }

        String arrayString;
        Class<?> componentType = array.getClass().getComponentType();

        // 处理各种基本类型数组
        if (componentType == int.class) {
            arrayString = Arrays.toString((int[]) array);
        } else if (componentType == double.class) {
            arrayString = Arrays.toString((double[]) array);
        } else if (componentType == boolean.class) {
            arrayString = Arrays.toString((boolean[]) array);
        } else if (componentType == char.class) {
            arrayString = formatCharArray((char[]) array);
        } else if (componentType == byte.class) {
            arrayString = Arrays.toString((byte[]) array);
        } else if (componentType == short.class) {
            arrayString = Arrays.toString((short[]) array);
        } else if (componentType == long.class) {
            arrayString = Arrays.toString((long[]) array);
        } else if (componentType == float.class) {
            arrayString = Arrays.toString((float[]) array);
        } else {
            // 处理对象数组
            arrayString = Arrays.deepToString((Object[]) array);
        }

        System.out.println(varName + ": " + arrayString);
    }

    /**
     * 自定义格式化字符数组输出，避免默认输出使用UTF-16编码
     */
    private static String formatCharArray(char[] array) {
        StringJoiner joiner = new StringJoiner(", ", "[", "]");
        for (char c : array) {
            joiner.add("'" + c + "'");
        }
        return joiner.toString();
    }

}
