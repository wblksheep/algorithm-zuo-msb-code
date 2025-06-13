package class22;

import java.util.Arrays;
import java.util.PriorityQueue;

import static class22.MatrixGenerator.generate;

// 本题测试链接 : https://leetcode.com/problems/trapping-rain-water-ii/
public class Code03_TrappingRainWaterIIEdition2 {

    public static class Node {
        public int value;
        public int row;
        public int col;

        public Node(int v, int r, int c) {
            value = v;
            row = r;
            col = c;
        }

    }

    public static int trapRainWater(int[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) return 0;
        int n = matrix.length;
        int m = matrix[0].length;
        boolean[][] isEntered = new boolean[n][m];
        PriorityQueue<Node> minHeap = new PriorityQueue<>((a, b) -> a.value - b.value);
        for (int i = 0; i < n - 1; i++) {
            isEntered[i][0] = true;
            minHeap.add(new Node(matrix[i][0], i, 0));
        }
        for (int j = 0; j < m - 1; j++) {
            isEntered[n - 1][j] = true;
            minHeap.add(new Node(matrix[n - 1][j], n - 1, j));
        }
        for (int i = n - 1; i > 0; i--) {
            isEntered[i][m - 1] = true;
            minHeap.add(new Node(matrix[i][m - 1], i, m - 1));
        }
        for (int j = m - 1; j > 0; j--) {
            isEntered[0][j] = true;
            minHeap.add(new Node(matrix[0][j], 0, j));
        }
        int max = 0;
        int water = 0;
        while (!minHeap.isEmpty()) {
            Node min = minHeap.poll();
            max = Math.max(max, min.value);
            int i = min.row;
            int j = min.col;
            if (i - 1 >= 0 && !isEntered[i - 1][j]) {
                water += Math.max(max - matrix[i - 1][j], 0);
                isEntered[i - 1][j] = true;
                minHeap.add(new Node(matrix[i - 1][j], i - 1, j));
            }
            if (j - 1 >= 0 && !isEntered[i][j - 1]) {
                water += Math.max(max - matrix[i][j - 1], 0);
                isEntered[i][j - 1] = true;
                minHeap.add(new Node(matrix[i][j - 1], i, j - 1));
            }
            if (i + 1 < n && !isEntered[i + 1][j]) {
                water += Math.max(max - matrix[i + 1][j], 0);
                isEntered[i + 1][j] = true;
                minHeap.add(new Node(matrix[i + 1][j], i + 1, j));
            }
            if (j + 1 < m && !isEntered[i][j + 1]) {
                water += Math.max(max - matrix[i][j + 1], 0);
                isEntered[i][j + 1] = true;
                minHeap.add(new Node(matrix[i][j + 1], i, j + 1));
            }
        }
        return water;
    }

    public static int trapRainWater2(int[][] heightMap) {
        if (heightMap == null || heightMap.length == 0 || heightMap[0] == null || heightMap[0].length == 0) {
            return 0;
        }
        int N = heightMap.length;
        int M = heightMap[0].length;
        boolean[][] isEnter = new boolean[N][M];
        PriorityQueue<Code03_TrappingRainWaterIIEdition1.Node> heap = new PriorityQueue<>((a, b) -> a.value - b.value);
        for (int j = 0; j < M - 1; j++) {
            isEnter[0][j] = true;
            heap.add(new Code03_TrappingRainWaterIIEdition1.Node(heightMap[0][j], 0, j));
        }
        for (int i = 0; i < N - 1; i++) {
            isEnter[i][M - 1] = true;
            heap.add(new Code03_TrappingRainWaterIIEdition1.Node(heightMap[i][M - 1], i, M - 1));
        }
        for (int j = M - 1; j > 0; j--) {
            isEnter[N - 1][j] = true;
            heap.add(new Code03_TrappingRainWaterIIEdition1.Node(heightMap[N - 1][j], N - 1, j));
        }
        for (int i = N - 1; i > 0; i--) {
            isEnter[i][0] = true;
            heap.add(new Code03_TrappingRainWaterIIEdition1.Node(heightMap[i][0], i, 0));
        }
        int water = 0;
        int max = 0;
        while (!heap.isEmpty()) {
            Code03_TrappingRainWaterIIEdition1.Node cur = heap.poll();
            max = Math.max(max, cur.value);
            int i = cur.row;
            int j = cur.col;
            if (i > 0 && !isEnter[i - 1][j]) {
                water += Math.max(0, max - heightMap[i - 1][j]);
                isEnter[i - 1][j] = true;
                heap.add(new Code03_TrappingRainWaterIIEdition1.Node(heightMap[i - 1][j], i - 1, j));
            }
            if (i < N - 1 && !isEnter[i + 1][j]) {
                water += Math.max(0, max - heightMap[i + 1][j]);
                isEnter[i + 1][j] = true;
                heap.add(new Code03_TrappingRainWaterIIEdition1.Node(heightMap[i + 1][j], i + 1, j));
            }
            if (j > 0 && !isEnter[i][j - 1]) {
                water += Math.max(0, max - heightMap[i][j - 1]);
                isEnter[i][j - 1] = true;
                heap.add(new Code03_TrappingRainWaterIIEdition1.Node(heightMap[i][j - 1], i, j - 1));
            }
            if (j < M - 1 && !isEnter[i][j + 1]) {
                water += Math.max(0, max - heightMap[i][j + 1]);
                isEnter[i][j + 1] = true;
                heap.add(new Code03_TrappingRainWaterIIEdition1.Node(heightMap[i][j + 1], i, j + 1));
            }
        }
        return water;
    }


    public static void main(String[] args) {
        int[][] matrix = generate(25, 25, 25);
//        int[][] matrix = {
//                {3, 1, 2, 2, 1},
//                {2, 5, 2, 4, 4},
//                {2, 3, 2, 5, 5},
//                {5, 1, 1, 5, 5},
//                {3, 4, 4, 2, 1}
//        };
        print(matrix);
        System.out.println(trapRainWater(matrix));
        System.out.println(trapRainWater2(matrix));
    }


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
}
