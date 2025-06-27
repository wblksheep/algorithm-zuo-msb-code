package class25;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// 本题测试链接 : https://leetcode.com/problems/ip-to-cidr/
public class Code01_IPToCIDREdition1 {


    public static List<String> ipToCIDR(String ip, int n) {

        List<String> ans = new ArrayList<>();

        int num = getNum(ip);

        while (n > 0) {
            int rightOne = getMap(num);
            int pow = 1;
            int base = 0;

            while ((pow << 1) < n && (base + 1) <= rightOne) {
                base++;
                pow <<= 1;
            }

            ans.add(getAns(pow, num, base));

            n -= pow;
            num += pow;
        }
        return ans;
    }

    public static String getAns(int pow, int num, int base) {

        StringBuilder builder = new StringBuilder();
        for (int move = 24; move >= 0; move -= 8) {
            builder.append(((num & (255 << move)) >>> move) + ".");
        }
        builder.setCharAt(builder.length() - 1, '/');
        builder.append(32 - base);
        return builder.toString();
    }

    public static int getNum(String ip) {
        String[] split = ip.split("\\.");
        int num = 0;
        num |= Integer.parseInt(split[0]) << 24;
        num |= Integer.parseInt(split[1]) << 16;
        num |= Integer.parseInt(split[2]) << 8;
        num |= Integer.parseInt(split[3]);
        return num;
    }

    public static HashMap<Integer, Integer> map = new HashMap<>();

    public static int getMap(int num) {
        if (map.isEmpty()) {
            for (int i = 0; i < 32; i++) {
                map.put(1 << i, i);
            }
        }
        return map.get(num & (-num));
    }
}
