package class25;

import java.lang.reflect.Array;
import java.util.StringJoiner;

public class ArrayPrinter {

    /**
     * 打印数组（支持一维和二维数组）
     *
     * @param name  数组名称
     * @param array 要打印的数组
     */
    public static void printArray(String name, Object array) {
        if (!array.getClass().isArray()) {
            System.out.println(name + ": " + array);
            return;
        }

        int dimensions = getArrayDimensions(array);
        switch (dimensions) {
            case 1:
                print1DArray(name, array);
                break;
            case 2:
                print2DArray(name, array);
                break;
            default:
                System.out.println("Unsupported array dimensions: " + dimensions);
        }
    }

    /**
     * 获取数组的维度
     *
     * @param array 数组对象
     * @return 数组的维度
     */
    private static int getArrayDimensions(Object array) {
        int dimensions = 0;
        Class<?> clazz = array.getClass();
        while (clazz.isArray()) {
            dimensions++;
            clazz = clazz.getComponentType();
        }
        return dimensions;
    }

    /**
     * 打印一维数组
     *
     * @param name  数组名称
     * @param array 一维数组
     */
    private static void print1DArray(String name, Object array) {
        int length = Array.getLength(array);
        StringJoiner joiner = new StringJoiner(", ", "[", "]");

        for (int i = 0; i < length; i++) {
            Object element = Array.get(array, i);
            joiner.add(element.toString());
        }

        System.out.println(name + ": " + joiner);
    }

    /**
     * 打印二维数组
     *
     * @param name  数组名称
     * @param array 二维数组
     */
    private static void print2DArray(String name, Object array) {
        int outerLength = Array.getLength(array);
        StringJoiner outerJoiner = new StringJoiner("\n");

        for (int i = 0; i < outerLength; i++) {
            Object innerArray = Array.get(array, i);
            if (!innerArray.getClass().isArray()) {
                throw new IllegalArgumentException("Expected inner element to be an array");
            }

            StringJoiner innerJoiner = new StringJoiner(", ", "[", "]");
            int innerLength = Array.getLength(innerArray);

            for (int j = 0; j < innerLength; j++) {
                Object element = Array.get(innerArray, j);
                innerJoiner.add(element.toString());
            }

            outerJoiner.add(name + i + ": " + innerJoiner);
        }
        System.out.println(outerJoiner);
    }

    // 测试用例
    public static void main(String[] args) {
        // 测试一维数组
        int[] intArray = {1, 3, 5, 2, 1};
        printArray("arr", intArray);

        // 测试二维数组
        int[][] twoDArray = {
                {1, 3, 5, 2, 1},
                {2, 3, 3, 2, 1},
                {1, 2, 5, 2, 1}
        };
        printArray("arr", twoDArray);

        // 测试不同类型数组
        double[] doubleArray = {1.1, 2.2, 3.3};
        printArray("doubleArr", doubleArray);

        String[][] stringArray = {
                {"Hello", "World"},
                {"Java", "Array"}
        };
        printArray("strArr", stringArray);
    }
}