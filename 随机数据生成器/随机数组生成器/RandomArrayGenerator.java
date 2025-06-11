import java.util.Arrays;
import java.util.Random;

public class RandomArrayGenerator {

    public static int[] generateRandomArray(int length, int maxValue) {
        if (length < 0) {
            throw new IllegalArgumentException("数组长度不能为负数");
        }

        // 处理maxValue为Integer.MIN_VALUE的特殊情况
        long absMax = (maxValue == Integer.MIN_VALUE)
                ? (long) Integer.MAX_VALUE + 1
                : Math.abs((long) maxValue);

        // 确保不溢出
        int safeMax = (absMax > Integer.MAX_VALUE) ? Integer.MAX_VALUE : (int) absMax;

        Random rand = new Random();
        int[] array = new int[length];

        for (int i = 0; i < length; i++) {
            // 随机生成[-maxValue, maxValue]范围内的整数
            if (safeMax == 0) {
                array[i] = 0;
            } else if (safeMax <= Integer.MAX_VALUE / 2) {
                // 直接计算方法（避免溢出）
                array[i] = rand.nextInt(2 * safeMax + 1) - safeMax;
            } else {
                // 大值安全计算方法
                int absVal = rand.nextInt(safeMax + 1);
                array[i] = (rand.nextBoolean() && absVal != 0) ? -absVal : absVal;
            }
        }

        return array;
    }

    public static void main(String[] args) {
        // 示例用法
        int length = 10;        // 数组长度
        int maxValue = 100;     // 最大值范围
        int[] randomArray = generateRandomArray(length, maxValue);

        System.out.println("生成长度为 " + length + " 的随机数组：");
        System.out.println(Arrays.toString(randomArray));
    }
}