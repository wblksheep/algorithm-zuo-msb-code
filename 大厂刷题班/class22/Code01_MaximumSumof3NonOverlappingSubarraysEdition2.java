package class22;

import java.util.Arrays;
import java.util.Random;
import java.util.StringJoiner;
import java.util.concurrent.ThreadLocalRandom;

// 本题测试链接 : https://leetcode.com/problems/maximum-sum-of-3-non-overlapping-subarrays/
public class Code01_MaximumSumof3NonOverlappingSubarraysEdition2 {

    //	public static int[] maxSumArray1(int[] arr) {
//		int N = arr.length;
//		int[] help = new int[N];
//		// help[i] 子数组必须以i位置结尾的情况下，累加和最大是多少？
//		help[0] = arr[0];
//		for (int i = 1; i < N; i++) {
//			int p1 = arr[i];
//			int p2 = arr[i] + help[i - 1];
//			help[i] = Math.max(p1, p2);
//		}
//		// dp[i] 在0~i范围上，随意选一个子数组，累加和最大是多少？
//		int[] dp = new int[N];
//		dp[0] = help[0];
//		for (int i = 1; i < N; i++) {
//			int p1 = help[i];
//			int p2 = dp[i - 1];
//			dp[i] = Math.max(p1, p2);
//		}
//		return dp;
//	}
//
//	public static int maxSumLenK(int[] arr, int k) {
//		int N = arr.length;
//		// 子数组必须以i位置的数结尾，长度一定要是K，累加和最大是多少？
//		// help[0] help[k-2] ...
//		int sum = 0;
//		for (int i = 0; i < k - 1; i++) {
//			sum += arr[i];
//		}
//		// 0...k-2 k-1 sum
//		int[] help = new int[N];
//		for (int i = k - 1; i < N; i++) {
//			// 0..k-2
//			// 01..k-1
//			sum += arr[i];
//			help[i] = sum;
//			// i == k-1
//			sum -= arr[i - k + 1];
//		}
//		// help[i] - > dp[i]  0-..i  K
//
//	}
    public static int[] maxSumOfThreeSubarrays(int[] arr, int k) {
        if (arr == null || arr.length < k) {
            return null;
        }
        int n = arr.length;
        int sum = 0;
        int[] range = new int[n];
        for (int i = 0; i < k; i++) {
            sum += arr[i];
        }
        range[0] = sum;
        int[] left = new int[n];
        int max = range[0];
        for (int i = k; i < n; i++) {
            sum = sum - arr[i - k] + arr[i];
            range[i - k + 1] = sum;
            left[i] = left[i - 1];
            if (sum > max) {
                max = sum;
                left[i] = i - k + 1;
            }
        }
        int[] right = new int[n];
        max = range[n - k];
        right[n - k] = n - k;
        for (int i = n - k - 1; i >= 0; i--) {
            right[i] = right[i + 1];
            if (range[i] >= max) {
                max = range[i];
                right[i] = i;
            }
        }
        int a = 0;
        int b = 0;
        int c = 0;
        max = 0;
        for (int i = k; i <= n - 2 * k; i++) {
            int part1 = range[left[i - 1]];
            int part2 = range[i];
            int part3 = range[right[i + k]];
            if (part1 + part2 + part3 > max) {
                max = part1 + part2 + part3;
                a = left[i - 1];
                b = i;
                c = right[i + k];
            }
        }
        return new int[]{a, b, c};
    }

    public static int[] maxSumOfThreeSubarrays2(int[] arr, int k) {
        if (arr == null || arr.length < k) {
            return null;
        }
        int n = arr.length;
        int[] range = new int[n];
        int[] sum = new int[n];
        sum[0] = arr[0];
        for (int i = 1; i < n; i++) {
            sum[i] = sum[i - 1] + arr[i];
        }
        range[0] = sum[k - 1];
        for (int i = 1; i < n - k + 1; i++) {
            range[i] = sum[i + k - 1] - sum[i - 1];
        }
        int max = range[0];
        int[] left = new int[n];
        for (int i = k; i < n; i++) {
            left[i] = left[i - 1];
            if (range[i - k + 1] >= max) {
                max = range[i - k + 1];
                left[i] = i - k + 1;
            }
        }
        int[] right = new int[n];
        max = range[n - k];
        right[n - k] = n - k;
        for (int i = n - k - 1; i >= 0; i--) {
            right[i] = right[i + 1];
            if (range[i] >= max) {
                max = range[i];
                right[i] = i;
            }
        }
//        range[0] = sum;
//        int[] left = new int[n];
//        int max = sum;
//        for (int i = k; i < n; i++) {
//            sum = sum - arr[i - k] + arr[i];
//            range[i - k + 1] = sum;
//            left[i] = left[i - 1];
//            if (sum > max) {
//                max = sum;
//                left[i] = i - k + 1;
//            }
//        }
//        int[] right = new int[n];
//        max = range[n - k];
//        right[n - k] = n - k;
//        for (int i = n - k - 1; i >= 0; i--) {
//            right[i] = right[i + 1];
//            if (range[i] > max) {
//                max = range[i];
//                right[i] = i;
//            }
//        }
        return null;
    }


    public static void main(String[] args) {
//        int[] arr = generatePositiveRandomArray(10, 10);
//        int[] arr = {4, 1, 6, 2, 6, 3, 5, 6, 1, 9};
        int[] arr = {1, 2, 1, 2, 1, 2, 1, 2, 1};
        printArray("arr", arr);
        printArray("res", maxSumOfThreeSubarrays(arr, 2));
//        printArray("res", maxSumOfThreeSubarrays2(arr, 2));
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
