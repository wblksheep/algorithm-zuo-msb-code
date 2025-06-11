package class22;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class MatrixGenerator {

    /**
     * 生成指定行列的随机正整数矩阵（默认范围1-100）
     *
     * @param rows 行数
     * @param cols 列数
     * @return 二维正整数数组
     */
    public static int[][] generate(int rows, int cols) {
        return generate(rows, cols, 1, 100);
    }

    /**
     * 生成指定行列和最大值的随机正整数矩阵（最小值默认为1）
     *
     * @param rows 行数
     * @param cols 列数
     * @param maxValue 最大值（包含）
     * @return 二维正整数数组
     */
    public static int[][] generate(int rows, int cols, int maxValue) {
        return generate(rows, cols, 1, maxValue);
    }

    /**
     * 生成指定行列和范围的随机正整数矩阵
     *
     * @param rows 行数
     * @param cols 列数
     * @param minValue 最小值（包含）
     * @param maxValue 最大值（包含）
     * @return 二维正整数数组
     */
    public static int[][] generate(int rows, int cols, int minValue, int maxValue) {
        validateParameters(rows, cols, minValue, maxValue);

        int[][] matrix = new int[rows][cols];
        Random rand = ThreadLocalRandom.current();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = rand.nextInt(maxValue - minValue + 1) + minValue;
            }
        }

        return matrix;
    }

    /**
     * 生成单位矩阵（对角线上为1，其余为0）
     *
     * @param size 矩阵大小
     * @return 单位矩阵
     */
    public static int[][] identity(int size) {
        validateSize(size);

        int[][] matrix = new int[size][size];
        for (int i = 0; i < size; i++) {
            matrix[i][i] = 1;
        }
        return matrix;
    }

    /**
     * 生成随机稀疏矩阵（大部分元素为0）
     *
     * @param rows 行数
     * @param cols 列数
     * @param density 非零元素密度（0.0-1.0）
     * @param maxValue 非零元素最大值
     * @return 稀疏矩阵
     */
    public static int[][] sparse(int rows, int cols, double density, int maxValue) {
        validateParameters(rows, cols, 1, maxValue);
        if (density < 0 || density > 1) {
            throw new IllegalArgumentException("密度必须在0.0到1.0之间");
        }

        int[][] matrix = new int[rows][cols];
        Random rand = ThreadLocalRandom.current();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (rand.nextDouble() < density) {
                    matrix[i][j] = rand.nextInt(maxValue) + 1;
                }
            }
        }

        return matrix;
    }

    /**
     * 生成帕斯卡三角形矩阵（杨辉三角）
     *
     * @param size 矩阵大小
     * @return 帕斯卡矩阵
     */
    public static int[][] pascal(int size) {
        validateSize(size);

        int[][] matrix = new int[size][size];
        for (int i = 0; i < size; i++) {
            matrix[i][0] = 1;
            matrix[0][i] = 1;
        }

        for (int i = 1; i < size; i++) {
            for (int j = 1; j < size; j++) {
                matrix[i][j] = matrix[i - 1][j] + matrix[i][j - 1];
            }
        }

        return matrix;
    }

    /**
     * 生成随机置换矩阵（每行每列只有一个1，其余为0）
     *
     * @param size 矩阵大小
     * @return 置换矩阵
     */
    public static int[][] permutation(int size) {
        validateSize(size);

        int[][] matrix = new int[size][size];
        java.util.List<Integer> positions = new java.util.ArrayList<>();
        for (int i = 0; i < size; i++) positions.add(i);

        java.util.Collections.shuffle(positions);

        for (int i = 0; i < size; i++) {
            matrix[i][positions.get(i)] = 1;
        }

        return matrix;
    }

    // 参数验证
    private static void validateParameters(int rows, int cols, int min, int max) {
        if (rows <= 0 || cols <= 0) {
            throw new IllegalArgumentException("行数和列数必须大于0");
        }
        if (min <= 0 || max <= 0) {
            throw new IllegalArgumentException("数值范围必须是正整数");
        }
        if (min > max) {
            throw new IllegalArgumentException("最小值不能大于最大值");
        }
    }

    private static void validateSize(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("矩阵大小必须大于0");
        }
    }

    // 测试用例
    public static void main(String[] args) {
        // 生成3x4矩阵，范围1-50
        int[][] matrix1 = generate(3, 4, 1, 50);
        System.out.println("随机矩阵:");
        printMatrix(matrix1);

        // 生成5x5单位矩阵
        int[][] matrix2 = identity(5);
        System.out.println("\n单位矩阵:");
        printMatrix(matrix2);

        // 生成稀疏矩阵（10%非零元素）
        int[][] matrix3 = sparse(6, 5, 0.1, 100);
        System.out.println("\n稀疏矩阵:");
        printMatrix(matrix3);

        // 生成帕斯卡矩阵
        int[][] matrix4 = pascal(5);
        System.out.println("\n帕斯卡矩阵:");
        printMatrix(matrix4);

        // 生成置换矩阵
        int[][] matrix5 = permutation(4);
        System.out.println("\n置换矩阵:");
        printMatrix(matrix5);
    }

    // 辅助方法：打印矩阵
    private static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int val : row) {
                System.out.print(val + "\t");
            }
            System.out.println();
        }
    }
}