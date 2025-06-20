package class24;

import java.util.Random;

public class Code05_MinWindowLengthComparator {

    // 暴力方法：用于小数据量验证
    public static String minWindowBruteForce(String s, String t) {
        if (s.length() < t.length()) return "";
        char[] str = s.toCharArray();
        char[] target = t.toCharArray();
        int minLen = Integer.MAX_VALUE;
        int minStart = -1, minEnd = -1;

        for (int L = 0; L < str.length; L++) {
            for (int R = L; R < str.length; R++) {
                // 检查子串str[L..R]是否涵盖t的所有字符
                if (isValidSubstring(str, L, R, target)) {
                    int len = R - L + 1;
                    if (len < minLen) {
                        minLen = len;
                        minStart = L;
                        minEnd = R;
                    }
                }
            }
        }
        return minStart == -1 ? "" : s.substring(minStart, minEnd + 1);
    }

    private static boolean isValidSubstring(char[] str, int L, int R, char[] target) {
        int[] map = new int[256];
        // 统计子串中字符频次
        for (int i = L; i <= R; i++) {
            map[str[i]]++;
        }
        // 检查是否覆盖target
        for (char c : target) {
            if (map[c]-- <= 0) {
                return false;
            }
        }
        return true;
    }

    // 随机字符串生成器
    private static String generateRandomString(int maxLen, char[] charset) {
        Random rand = new Random();
        int len = rand.nextInt(maxLen) + 1; // 长度[1, maxLen]
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            int idx = rand.nextInt(charset.length);
            sb.append(charset[idx]);
        }
        return sb.toString();
    }

    // 对数器主函数
    public static void main(String[] args) {
//        char[] charset = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        char[] charset = "0123456789".toCharArray();
        int testTimes = 10000;
        int maxLenSmall = 10; // 小数据量上限
        int maxLenLarge = 10; // 大数据量上限

        for (int i = 0; i < testTimes; i++) {
            // 动态生成s1和s2
//            String s1 = generateRandomString(i % 2 == 0 ? maxLenSmall : maxLenLarge, charset);
//            String s2 = generateRandomString(Math.min(s1.length(), randLen(s1.length())), charset);
            String s1 = "5899467998";
            String s2 = "87";

            // 调用待测方法
            int len1 = Code05_MinWindowLengthEdition1.minLength(s1, s2);
            String window = Code05_MinWindowLengthEdition1.minWindow(s1, s2);
            int len2 = window.isEmpty() ? 0 : window.length();

            // 验证一致性
            if (len1 != len2) {
                System.out.println("Error: Length mismatch!");
                System.out.println("s1: " + s1);
                System.out.println("s2: " + s2);
                System.out.println("minLength: " + len1);
                System.out.println("minWindow: " + len2);
                return;
            }

            // 小数据量额外验证暴力法
            if (s1.length() <= maxLenSmall) {
                String bruteResult = minWindowBruteForce(s1, s2);
                int bruteLen = bruteResult.isEmpty() ? 0 : bruteResult.length();
                if (len1 != bruteLen) {
                    System.out.println("Error: Brute force mismatch!");
                    System.out.println("s1: " + s1);
                    System.out.println("s2: " + s2);
                    System.out.println("minLength: " + len1);
                    System.out.println("Brute force: " + bruteLen);
                    return;
                }
            }
        }
        System.out.println("All tests passed!");
    }

    private static int randLen(int max) {
        Random rand = new Random();
        return max > 0 ? rand.nextInt(max) + 1 : 0;
    }
}
