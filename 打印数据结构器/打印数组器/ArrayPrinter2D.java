
import java.util.Arrays;

public class ArrayPrinter2D {

    /**
     * 打印二维数组的所有元素（简单格式）
     *
     * @param array 二维数组
     */
    public static void print(Object array) {
        print(array, true, "", "", 2, 0);
    }

    /**
     * 高级打印方法，支持自定义格式
     *
     * @param array         二维数组
     * @param gridLines     是否显示网格线
     * @param rowPrefix     行前缀
     * @param colSeparator  列分隔符
     * @param minColWidth   最小列宽（字符数）
     * @param decimalPlaces 浮点数小数位数（0表示整数格式）
     */
    public static void print(Object array,
                             boolean gridLines,
                             String rowPrefix,
                             String colSeparator,
                             int minColWidth,
                             int decimalPlaces) {

        // 验证输入是否为二维数组
        if (!array.getClass().isArray() ||
                !array.getClass().getComponentType().isArray()) {
            throw new IllegalArgumentException("输入必须是二维数组");
        }

        // 获取数组维度
        int rows = java.lang.reflect.Array.getLength(array);
        if (rows == 0) {
            System.out.println("[]");
            return;
        }

        // 获取第一行判断列数
        Object firstRow = java.lang.reflect.Array.get(array, 0);
        int cols = java.lang.reflect.Array.getLength(firstRow);

        // 创建字符串矩阵
        String[][] stringMatrix = new String[rows][cols];

        // 计算每列最大宽度
        int[] colWidths = new int[cols];
        Arrays.fill(colWidths, minColWidth);

        // 转换所有元素为字符串并计算列宽
        for (int i = 0; i < rows; i++) {
            Object row = java.lang.reflect.Array.get(array, i);
            int currentCols = java.lang.reflect.Array.getLength(row);

            if (currentCols != cols) {
                throw new IllegalArgumentException("数组必须是矩阵（每行长度相同）");
            }

            for (int j = 0; j < cols; j++) {
                Object element = java.lang.reflect.Array.get(row, j);
                String elementStr = formatElement(element, decimalPlaces);
                stringMatrix[i][j] = elementStr;

                if (elementStr.length() > colWidths[j]) {
                    colWidths[j] = elementStr.length();
                }
            }
        }

        // 打印数组
        printMatrix(stringMatrix, colWidths, gridLines, rowPrefix, colSeparator);
    }

    // 格式化单个元素
    private static String formatElement(Object element, int decimalPlaces) {
        if (element == null) {
            return "null";
        }

        if (element instanceof Float || element instanceof Double) {
            if (decimalPlaces > 0) {
                return String.format("%." + decimalPlaces + "f", element);
            } else {
                return String.format("%.0f", element);
            }
        }

        return element.toString();
    }

    // 打印格式化后的矩阵
    private static void printMatrix(String[][] matrix, int[] colWidths,
                                    boolean gridLines, String rowPrefix,
                                    String colSeparator) {
        int rows = matrix.length;
        int cols = matrix[0].length;

        // 计算总宽度
        int totalWidth = rowPrefix.length() + Arrays.stream(colWidths).sum() +
                colSeparator.length() * (cols - 1);

        // 打印顶部边框
        if (gridLines) {
            System.out.println(rowPrefix + "┌" + StringUtils.repeat("─", totalWidth - rowPrefix.length()) + "┐");
        }

        // 打印每一行
        for (int i = 0; i < rows; i++) {
            StringBuilder sb = new StringBuilder(rowPrefix);

            if (gridLines) sb.append("│");

            for (int j = 0; j < cols; j++) {
                String element = matrix[i][j];
                // 居中格式化
                int padding = colWidths[j] - element.length();
                int leftPadding = padding / 2;
                int rightPadding = padding - leftPadding;

                sb.append(StringUtils.repeat(" ", leftPadding))
                        .append(element)
                        .append(StringUtils.repeat(" ", rightPadding));

                if (j < cols - 1) {
                    sb.append(colSeparator);
                }
            }

            if (gridLines) sb.append("│");

            System.out.println(sb);

            // 打印网格线
            if (gridLines && i < rows - 1) {
                sb = new StringBuilder(rowPrefix + "├");
                for (int j = 0; j < cols; j++) {
                    sb.append(StringUtils.repeat("─", colWidths[j]));
                    if (j < cols - 1) sb.append("┼");
                }
                sb.append("┤");
                System.out.println(sb);
            }
        }

        // 打印底部边框
        if (gridLines) {
            System.out.println(rowPrefix + "└" + StringUtils.repeat("─", totalWidth - rowPrefix.length()) + "┘");
        }
    }

    // 测试用例
    public static void main(String[] args) {
        // 测试1: 基本整型数组
        int[][] grid1 = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        System.out.println("测试1: 基本整型数组");
        print(grid1);

        // 测试2: 浮点数数组（指定小数位）
        double[][] grid2 = {
                {1.23456, 2.34567},
                {3.45678, 4.56789}
        };
        System.out.println("\n测试2: 浮点数数组（保留3位小数）");
        print(grid2, true, "", " ", 8, 3);

        // 测试3: 字符串数组（自定义格式）
        String[][] grid3 = {
                {"Alice", "Engineer", "New York"},
                {"Bob", "Designer", "San Francisco"},
                {"Charlie", "Manager", "London"}
        };
        System.out.println("\n测试3: 字符串数组（自定义格式）");
        print(grid3, true, "  ", " | ", 10, 0);

        // 测试4: 混合类型数组
        Object[][] grid4 = {
                {"ID", "Name", "Score", "Passed"},
                {101, "Alice", 95.5, true},
                {102, "Bob", 87.3, true},
                {103, "Charlie", 59.9, false}
        };
        System.out.println("\n测试4: 混合类型数组");
        print(grid4, true, "  ", " | ", 8, 1);

        // 测试5: 无网格线
        System.out.println("\n测试5: 无网格线");
        print(grid1, false, "", "  ", 3, 0);
    }
}