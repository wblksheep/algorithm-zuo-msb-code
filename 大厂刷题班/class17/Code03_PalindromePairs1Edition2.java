package class17;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

// 课上讲了哈希表+manacher
// 有关manacher的解释，看这个帖子 : https://www.mashibing.com/question/detail/56727
// 从代码层次讲了一下，例子非常详细
// 测试链接 : https://leetcode.com/problems/palindrome-pairs/
public class Code03_PalindromePairs1Edition2 {
    public static List<List<Integer>> palindromePairs(String[] words) {
        HashMap<String, Integer> wordset = new HashMap<>();
        int n = words.length;
        for (int i = 0; i < n; i++) {
            wordset.put(words[i], i);
        }
        List<List<Integer>> res = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            res.addAll(findAll(words[i], i, wordset));
        }
        return res;
    }

    public static List<List<Integer>> findAll(String word, int index, HashMap<String, Integer> words) {
        List<List<Integer>> res = new ArrayList<>();
        String reverse = reverse(word);
//        Integer rest = words.get("");
//        if (rest != null && rest != index && word.equals(reverse)) {
//            res.add(Arrays.asList(index, rest));
//            res.add(Arrays.asList(rest, index));
//        }
        int[] rs = manacherss(word);
        int mid = rs.length >> 1;
        for (int i = 1; i <= mid; i++) {
            if (i - rs[i] == -1) {
                Integer rest = words.get(reverse.substring(0, reverse.length() - i));
                if (rest != null && rest != index) {
                    res.add(Arrays.asList(rest, index));
                }
            }
        }
        for (int i = mid; i < rs.length; i++) {
            if (i + rs[i] == rs.length) {
                Integer rest = words.get(reverse.substring(2 * mid - i));
                if (rest != null && rest != index) {
                    res.add(Arrays.asList(index, rest));
                }
            }
        }
        return res;
    }

    public static int[] manacherss(String word) {
        char[] mchs = manachercs(word);
        int[] rs = new int[mchs.length];
        int pr = -1;
        int center = -1;
        for (int i = 0; i < mchs.length; i++) {
            rs[i] = pr > i ? Math.min(pr - i, rs[2 * center - i]) : 1;
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

    public static char[] manachercs(String str) {
        char[] chs = str.toCharArray();
        char[] res = new char[chs.length * 2 + 1];
        int index = 0;
        for (int i = 0; i < res.length; i++) {
            res[i] = (i & 1) == 0 ? '#' : chs[index++];
        }
        return res;
    }

    public static String reverse(String word) {
        char[] chs = word.toCharArray();
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
        List<List<Integer>> res = palindromePairs(words);
        for (List<Integer> list : res) {
            System.out.println("[ " + list.get(0) + ", " + list.get(1) + "]");
        }
    }
}