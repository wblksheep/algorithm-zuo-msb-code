package class17;

import java.util.HashMap;

// 本题测试链接 : https://leetcode.com/problems/distinct-subsequences-ii/
public class Code05_DistinctSubseqValue {

    public static int distinctSubseqII(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        long m = 1000000007;
        char[] str = s.toCharArray();
        long[] count = new long[26];
        long all = 1; // 算空集
        for (char x : str) {
            long add = (all - count[x - 'a'] + m) % m;
            all = (all + add) % m;
            count[x - 'a'] = (count[x - 'a'] + add) % m;
        }
        all = (all - 1 + m) % m;
        return (int) all;
    }

    public static int zuo(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        int m = 1000000007;
        char[] str = s.toCharArray();
        HashMap<Character, Integer> map = new HashMap<>();
        int all = 1; // 一个字符也没遍历的时候，有空集
        for (char x : str) {
            int newAdd = all;
            int curAll = all;
            curAll = (curAll + newAdd) % m;
            curAll = (curAll - map.getOrDefault(x, 0) + m) % m;
            all = curAll;
            map.put(x, newAdd);
        }
        return (all - 1 + m) % m;
    }

    public static void main(String[] args) {
        String s = "aaaba";
        System.out.println(distinctSubseqII(s));
        System.out.println(zuo(s));
        s = "yezruvnatuipjeohsymapyxgfeczkevoxipckunlqjauvllfpwezhlzpbkfqazhexabomnlxkmoufneninbxxguuktvupmpfspwxiouwlfalexmluwcsbeqrzkivrphtpcoxqsueuxsalopbsgkzaibkpfmsztkwommkvgjjdvvggnvtlwrllcafhfocprnrzfoyehqhrvhpbbpxpsvomdpmksojckgkgkycoynbldkbnrlujegxotgmeyknpmpgajbgwmfftuphfzrywarqkpkfnwtzgdkdcyvwkqawwyjuskpvqomfchnlojmeltlwvqomucipcwxkgsktjxpwhujaexhejeflpctmjpuguslmzvpykbldcbxqnwgycpfccgeychkxfopixijeypzyryglutxweffyrqtkfrqlhtjweodttchnugybsmacpgperznunffrdavyqgilqlplebbkdopyyxcoamfxhpmdyrtutfxsejkwiyvdwggyhgsdpfxpznrccwdupfzlubkhppmasdbqfzttbhfismeamenyukzqoupbzxashwuvfkmkosgevcjnlpfgxgzumktsexvwhylhiupwfwyxotwnxodttsrifgzkkedurayjgxlhxjzlxikcgerptpufocymfrkyayvklsalgmtifpiczwnozmgowzchjiop";
        System.out.println(distinctSubseqII(s));
        System.out.println(zuo(s));
    }

}
