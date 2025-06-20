package class24;

public class Code05_MinWindowLengthEdition1 {

    public static int minLength(String str, String tStr) {
        if (str == null || tStr == null || str.length() < tStr.length()) {
            return Integer.MAX_VALUE;
        }
        char[] s = str.toCharArray();
        char[] t = tStr.toCharArray();
        int n = s.length;
        int m = t.length;
        int[] cnt = new int[10];
        int all = 0;
        for (int i = 0; i < m; i++) {
            cnt[t[i] - '0']++;
            all++;
        }
        int L = 0;
        int R = 0;
        int minLen = Integer.MAX_VALUE;
        while (R < n) {
            if (--cnt[s[R] - '0'] >= 0) {
                all--;
            }
            if (all == 0) {
                while (cnt[s[L] - '0'] < 0) {
                    cnt[s[L++] - '0']++;
                }
                minLen = Math.min(minLen, R - L + 1);
                cnt[s[L++] - '0']++;
                all++;
            }
            R++;
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
