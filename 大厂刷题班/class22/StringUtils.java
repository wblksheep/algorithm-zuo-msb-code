package class22;

public class StringUtils {

    /**
     * 兼容 Java 8 的字符串重复方法
     * @param str 原始字符串
     * @param count 重复次数
     * @return 重复后的字符串
     */
    public static String repeat(String str, int count) {
        // 验证参数合法性
        if (str == null) return null;
        if (count < 0) {
            throw new IllegalArgumentException("count 不能为负数: " + count);
        }
        if (count == 0 || str.isEmpty()) {
            return "";
        }

        // 使用 StringBuilder 高效拼接字符串
        StringBuilder sb = new StringBuilder(str.length() * count);
        for (int i = 0; i < count; i++) {
            sb.append(str);
        }
        return sb.toString();
    }

    // 测试用例
    public static void main(String[] args) {
        System.out.println(repeat("-", 10));    // 输出: ----------
        System.out.println(repeat("abc", 3));   // 输出: abcabcabc
        System.out.println(repeat("", 5));      // 输出: (空字符串)
    }
}