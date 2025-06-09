package class17;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

// 再次优化，不用哈希表，不用前缀树，用字符串哈希！
// 在算法周更班 : https://www.mashibing.com/live/1240
// 字符串哈希代码 : https://github.com/algorithmzuo/weekly-problems/blob/main/src/class_2023_05_5_week/Code04_StringHash.java
// 2023年5月第5周的课，看会！
// 本题测试链接 : https://leetcode.com/problems/palindrome-pairs/
// 字符串哈希 + Manacher算法！打败95%的人！
public class Code03_PalindromePairs2Edition2 {

    // 选一个质数做进制数
    public static int BASE = 499;

    // 字符串最大长度
    // 以下内容看字符串哈希的内容
    public static int MAXN = 301;

    public static long[] pow = new long[MAXN];

    static {
        pow[0] = 1;
        for (int i = 1; i < MAXN; i++) {
            pow[i] = pow[i - 1] * BASE;
        }
    }

    public static long[] hash = new long[MAXN];

    public static long hash(int l, int r) {
        if (l > r) {
            return 0;
        }
        long ans = hash[r];
        ans -= l == 0 ? 0 : (hash[l - 1] * pow[r - l + 1]);
        return ans;
    }

    public static long hashValue(String str) {
        if (str.equals("")) {
            return 0;
        }
        long ans = str.charAt(0) - 'a' + 1;
        for (int i = 1; i < str.length(); i++) {
            ans = ans * BASE + str.charAt(i) - 'a' + 1;
        }
        return ans;
    }

    public static List<List<Integer>> palindromePairs(String[] words) {
        HashMap<Long, Integer> hash = new HashMap<>();
        for (int i = 0; i < words.length; i++) {
            hash.put(hashValue(words[i]), i);
        }
        List<List<Integer>> res = new ArrayList<>();
        for (int i = 0; i < words.length; i++) {
            res.addAll(findAll(words[i], i, hash));
        }
        return res;
    }

    public static List<List<Integer>> findAll(String word, int index, HashMap<Long, Integer> hash) {
        List<List<Integer>> res = new ArrayList<>();
        String reverse = reverse(word);
//        Integer rest = hash.get(0L);
//        if (rest != null && rest != index && word.equals(reverse)) {
//            res.add(Arrays.asList(index, rest));
//            res.add(Arrays.asList(rest, index));
//        }
        build(reverse);
        int[] rs = manacherss(word);
        int mid = rs.length >> 1;
        for (int i = 1; i <= mid; i++) {
            if (i - rs[i] == -1) {
                Integer rest = hash.get(hash(0, mid - i - 1));
                if (rest != null && rest != index) {
                    res.add(Arrays.asList(rest, index));
                }
            }
        }
        for (int i = mid; i < rs.length; i++) {
            if (i + rs[i] == rs.length) {
                Integer rest = hash.get(hash((mid << 1) - i, reverse.length() - 1));
                if (rest != null && rest != index) {
                    res.add(Arrays.asList(index, rest));
                }
            }
        }
        return res;
    }

    public static void build(String word) {
        if (word == null || word.length() == 0) {
            return;
        }
        hash[0] = word.charAt(0) - 'a' + 1;
        for (int i = 1; i < word.length(); i++) {
            hash[i] = hash[i - 1] * BASE + word.charAt(i) - 'a' + 1;
        }
    }

    public static int[] manacherss(String word) {
        char[] mchs = manachercs(word);
        int[] rs = new int[mchs.length];
        int center = -1;
        int pr = -1;
        for (int i = 0; i != mchs.length; i++) {
            rs[i] = pr > i ? Math.min(rs[(center << 1) - i], pr - i) : 1;
            while (i + rs[i] < mchs.length && i - rs[i] > -1) {
                if (mchs[i + rs[i]] != mchs[i - rs[i]]) {
                    break;
                }
                rs[i]++;
            }
            if (i + rs[i] > pr) {
                pr = i + rs[i];
                center = i;
            }
        }
        return rs;
    }

    public static char[] manachercs(String word) {
        char[] chs = word.toCharArray();
        char[] mchs = new char[chs.length * 2 + 1];
        int index = 0;
        for (int i = 0; i != mchs.length; i++) {
            mchs[i] = (i & 1) == 0 ? '#' : chs[index++];
        }
        return mchs;
    }

    public static String reverse(String str) {
        char[] chs = str.toCharArray();
        int l = 0;
        int r = chs.length - 1;
        while (l < r) {
            char tmp = chs[l];
            chs[l++] = chs[r];
            chs[r--] = tmp;
        }
        return String.valueOf(chs);
    }

    public static void main(String[] args) {
        String[] words = {"abcd", "dcba", "", "aa"};
//        String[] words = {""};
        List<List<Integer>> res = palindromePairs(words);
        for (List<Integer> list : res) {
            System.out.println("[ " + list.get(0) + ", " + list.get(1) + "]");
        }
    }
}