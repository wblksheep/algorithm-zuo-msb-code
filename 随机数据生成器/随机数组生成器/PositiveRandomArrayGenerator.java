import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class PositiveRandomArrayGenerator {

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

    public static void main(String[] args) {
        // 示例用法
        int length = 20;       // 数组长度
        int maxValue = 100;    // 最大值（正整数）

        int[] randomArray = generatePositiveRandomArray(length, maxValue);

        System.out.println("生成长度为 " + length + " 的正整数随机数组（范围[1, " + maxValue + "]）：");
        System.out.println(Arrays.toString(randomArray));
    }
}