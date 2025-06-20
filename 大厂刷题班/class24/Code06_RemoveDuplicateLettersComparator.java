package class24;

import java.util.Random;

public class Code06_RemoveDuplicateLettersComparator {

    public static String removeDuplicateLetters(String s) {

        char[] str = s.toCharArray();
        int n = str.length;
        int[] cnt = new int[26];
        boolean[] entered = new boolean[26];
        for (int i = 0; i < n; i++) {
            cnt[str[i] - 'a']++;
        }
        char[] stack = new char[26];
        int size = 0;
        for (int i = 0; i < n; i++) {
            if (entered[str[i] - 'a']) {
                cnt[str[i] - 'a']--;
                continue;
            }
            while (size - 1 >= 0 && stack[size - 1] > str[i] && cnt[stack[size - 1] - 'a']-- > 1) {
                entered[stack[--size] - 'a'] = false;
            }
            stack[size++] = str[i];
            entered[str[i] - 'a'] = true;
        }
        return String.valueOf(stack, 0, size);
    }

    // 暴力解法（生成所有可能的去重子序列并选择字典序最小的）
    public static String bruteForce(String s) {
        int n = s.length();
        String best = null;// 最优结果

        // 枚举所有子序列：2^n种可能
        for (int mask = 1; mask < (1 << n); mask++) {
            StringBuilder sb = new StringBuilder();
            // 记录每个字符是否出现过
            boolean[] used = new boolean[26];
            // 记录当前子序列在原字符串中的位置索引
            int lastIndex = -1;
            boolean valid = true;

            // 构建子序列
            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) != 0) {
                    char c = s.charAt(i);
                    // 确保字符不重复且顺序正确
                    if (used[c - 'a']) {
                        valid = false;
                        break;
                    }
                    // 确保顺序不变
                    if (i < lastIndex) {
                        valid = false;
                        break;
                    }
                    lastIndex = i;
                    used[c - 'a'] = true;
                    sb.append(c);
                }
            }

            if (!valid) continue;

            String candidate = sb.toString();
            // 检查是否包含所有distinct字符
            if (countDistinct(s) != candidate.length()) continue;

            // 更新最优解（字典序最小）
            if (best == null || candidate.compareTo(best) < 0) {
                best = candidate;
            }
        }
        return best != null ? best : "";
    }

    // 计算字符串中不同字符的数量
    private static int countDistinct(String s) {
        boolean[] marked = new boolean[26];
        int count = 0;
        for (char c : s.toCharArray()) {
            if (!marked[c - 'a']) {
                marked[c - 'a'] = true;
                count++;
            }
        }
        return count;
    }

    // 验证结果是否符合要求
    private static boolean isValid(String s, String result) {
        // 1. 长度验证：结果应包含所有distinct字符
        if (countDistinct(s) != result.length()) {
            return false;
        }

        // 2. 字符唯一性验证
        boolean[] seen = new boolean[26];
        for (char c : result.toCharArray()) {
            if (seen[c - 'a']) return false;
            seen[c - 'a'] = true;
        }

        // 3. 子序列验证（是否保持原顺序）
        int index = 0;
        for (char c : result.toCharArray()) {
            int pos = s.indexOf(c, index);
            if (pos == -1) return false;
            index = pos + 1;
        }

        return true;
    }

    // 生成随机测试用例
    private static String generateRandomTestCase(Random rand) {
        int length = rand.nextInt(10) + 3; // 长度3-12
        StringBuilder sb = new StringBuilder();
        // 确保包含重复字符
        for (int i = 0; i < length; i++) {
            char c = (char) ('a' + rand.nextInt(8)); // 限制字符范围增加重复概率
            sb.append(c);
        }
        return sb.toString();
    }

    // 执行对比测试
    public static void main(String[] args) {
        Random rand = new Random();
        int tests = 3000; // 测试次数
        int maxLength = 12; // 最大长度

        for (int i = 0; i < tests; i++) {
            // 生成测试用例
            String s;
            if (i < 50) {
                // 前50个测试用例使用固定边界案例
                String[] boundaryCases = {"abgfebbedch",
                        "bfa", "bfaf", "bfafa", "bfafabc", "bfafabfc",
                        "bfafabfcf", "abb", "abcba", "bcabc",
                        "cbacdcbc", "leetcode", "bcab"
                };
                s = boundaryCases[i % boundaryCases.length];
            } else {
                // 随机测试用例
                s = generateRandomTestCase(rand);
            }

            // 调用目标方法
            String result = removeDuplicateLetters(s);

            // 验证结果
            if (!isValid(s, result)) {
                System.out.println("Invalid test case result: " + bruteForce(s));
                System.out.println("Test failed for input: " + s);
                System.out.println("Output: " + result);
                removeDuplicateLetters(s);
                return;
            }

            // 小规模数据对比暴力解
            if (s.length() <= 10) {
                String bruteResult = bruteForce(s);
                if (!result.equals(bruteResult)) {
                    System.out.println("Mismatch found!");
                    System.out.println("Input: " + s);
                    System.out.println("Target method: " + result);
                    System.out.println("Brute force: " + bruteResult);
                    return;
                }
            }
        }
        System.out.println("All tests passed!");
    }
}
