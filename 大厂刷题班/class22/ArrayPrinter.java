package class22;

import java.util.Arrays;
import java.util.StringJoiner;

public class ArrayPrinter {

    /**
     * 打印任意类型数组，格式为"变量名: [元素1, 元素2, ...]"
     *
     * @param varName 变量名
     * @param array 要打印的数组
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

    // 测试用例
    public static void main(String[] args) {
        // 基本类型数组测试
        int[] intArr = {1, 2, 3};
        printArray("intArr", intArr);  // 输出: intArr: [1, 2, 3]

        double[] doubleArr = {1.1, 2.2, 3.3};
        printArray("doubleArr", doubleArr);  // 输出: doubleArr: [1.1, 2.2, 3.3]

        char[] charArr = {'a', 'b', '中'};
        printArray("charArr", charArr);  // 输出: charArr: ['a', 'b', '中']

        boolean[] boolArr = {true, false, true};
        printArray("boolArr", boolArr);  // 输出: boolArr: [true, false, true]

        // 对象数组测试
        String[] strArr = {"hello", "world"};
        printArray("strArr", strArr);  // 输出: strArr: [hello, world]

        Integer[] integerArr = {1, null, 3};
        printArray("integerArr", integerArr);  // 输出: integerArr: [1, null, 3]

        // 多维数组测试
        int[][] multiArr = {{1, 2}, {3, 4}};
        printArray("multiArr", multiArr);  // 输出: multiArr: [[1, 2], [3, 4]]

        // 空数组测试
        Object[] emptyArr = {};
        printArray("emptyArr", emptyArr);  // 输出: emptyArr: []
    }
}