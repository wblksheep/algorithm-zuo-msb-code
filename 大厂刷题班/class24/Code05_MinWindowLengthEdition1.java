package class24;

public class Code05_MinWindowLengthEdition1 {

    public static int minLength(String str, String tStr) {
        char[] s = str.toCharArray();
        char[] t = tStr.toCharArray();
        int n = s.length, m = t.length;
        int[] map = new int[10];
        int debt = 0;
        for (int i = 0; i < m; i++) {
            map[t[i] - '0']++;
            debt++;
        }
        int minLen = Integer.MAX_VALUE;
        int L = 0;
        for (int R = 0; R < n; R++) {
            if (map[s[R] - '0']-- > 0) {
                debt--;
            }
            if (debt == 0) {
                while (map[s[L] - '0'] < 0) {
                    map[s[L++] - '0']++;
                }
                minLen = Math.min(minLen, R - L + 1);
                map[s[L++] - '0']++;
                debt++;
            }
        }
        return minLen == Integer.MAX_VALUE ? 0 : minLen;
    }


    // 测试链接 : https://leetcode.com/problems/minimum-window-substring/
    public static String minWindow(String s, String t) {
        if (s.length() < t.length()) {
            return "";
        }
        char[] str = s.toCharArray();
        char[] target = t.toCharArray();
        int[] map = new int[256];
        for (char cha : target) {
            map[cha]++;
        }
        int all = target.length;
        int L = 0;
        int R = 0;
        int minLen = Integer.MAX_VALUE;
        int ansl = -1;
        int ansr = -1;
        while (R != str.length) {
            map[str[R]]--;
            if (map[str[R]] >= 0) {
                all--;
            }
            if (all == 0) {
                while (map[str[L]] < 0) {
                    map[str[L++]]++;
                }
                if (minLen > R - L + 1) {
                    minLen = R - L + 1;
                    ansl = L;
                    ansr = R;
                }
                all++;
                map[str[L++]]++;
            }
            R++;
        }
        return minLen == Integer.MAX_VALUE ? "" : s.substring(ansl, ansr + 1);
    }

}
