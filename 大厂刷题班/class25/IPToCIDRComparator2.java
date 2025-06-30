package class25;

import java.util.*;

public class IPToCIDRComparator2 {

    public static List<String> ipToCIDR(String ip, int n) {

        int num = status(ip);
        int maxPower = 0;
        int power = 0;
        int solved = 0;
        List<String> ans = new ArrayList<>();
        while (n > 0) {
            maxPower = mostRightPower(num);
            power = 0;
            solved = 1;
            while ((solved << 1) <= n && power + 1 <= maxPower) {
                solved <<= 1;
                power++;
            }
            ans.add(content(num, power));
            n -= solved;
            num += solved;
        }
        return ans;
    }

    public static String content(int status, int power) {
        StringBuilder builder = new StringBuilder();
        for (int move = 24; move >= 0; move -= 8) {
            builder.append(((status >>> move) & 255) + ".");
        }
        builder.setCharAt(builder.length() - 1, '/');
        builder.append(32 - power);
        return builder.toString();
    }

    public static HashMap<Integer, Integer> map = new HashMap<>();

    public static int mostRightPower(int num) {
        if (map.isEmpty()) {
            map.put(0, 32);
            for (int i = 0; i < 32; i++) {
                map.put(1 << i, i);
            }
        }
        return map.get(num & (-num));
    }

    public static int status(String ip) {
        int num = 0;
        int move = 24;
        for (String s : ip.split("\\.")) {
            num |= Integer.parseInt(s) << move;
            move -= 8;
        }
        return num;
    }

    // 辅助函数：IP字符串转32位整数（长整型防止溢出）
    public static long ipToLong(String ip) {
        String[] parts = ip.split("\\.");
        long value = 0;
        for (String part : parts) {
            value = (value << 8) + Integer.parseInt(part);
        }
        return value;
    }

    // 辅助函数：CIDR块转覆盖的所有IP地址集合
    public static Set<Long> cidrToIPs(String cidr) {
        Set<Long> ips = new HashSet<>();
        String[] parts = cidr.split("/");
        String ip = parts[0];
        int mask = Integer.parseInt(parts[1]);
        long start = ipToLong(ip);
        long numIps = 1L << (32 - mask);
        for (long i = 0; i < numIps; i++) {
            ips.add(start + i);
        }
        return ips;
    }

    // 对比函数（暴力方法）
    public static List<String> comparator(String ip, int n) {
        long start = ipToLong(ip);
        List<String> result = new ArrayList<>();
        long remaining = n;
        long current = start;

        while (remaining > 0) {
            // 计算当前地址最右侧的1（lowbit）
            long lowbit = current & -current;
            // 计算覆盖能力（maxPower）：当前能覆盖的2的最大幂次
            long maxPower = 0;
            if (lowbit > 0) {
                maxPower = (long) (Math.log(lowbit) / Math.log(2));
            }
            // 实际覆盖大小：取 min(剩余数量, 覆盖能力)
            long size = 1;
            long power = 0;
            while (size <= remaining && power < maxPower) {
                size <<= 1;
                power++;
            }
            if (size > remaining) {
                size >>= 1;
                power--;
            }

            // 生成CIDR块
            String cidr = longToCIDR(current, (int) power);
            result.add(cidr);
            current += size;
            remaining -= size;
        }
        return result;
    }

    // 长整型IP转CIDR字符串
    public static String longToCIDR(long ip, int power) {
        int mask = 32 - power;
        int[] octets = new int[4];
        for (int i = 0; i < 4; i++) {
            octets[i] = (int) ((ip >>> (24 - i * 8)) & 0xFF);
        }
        return octets[0] + "." + octets[1] + "." + octets[2] + "." + octets[3] + "/" + mask;
    }

    // 随机测试生成器
    public static String generateRandomIP() {
        Random rand = new Random();
        return rand.nextInt(256) + "." +
                rand.nextInt(256) + "." +
                rand.nextInt(256) + "." +
                rand.nextInt(256);
    }

    // 对数器主函数
    public static void main(String[] args) {
        int testTimes = 10000;
        int maxN = 1000;
        Random rand = new Random();

        for (int i = 0; i < testTimes; i++) {
            String ip = generateRandomIP();
            // 确保n不会导致IP溢出（最大到1000）
            int n = rand.nextInt(maxN) + 1; // n 从 1 到 1000

            // 调用目标方法
            List<String> result1 = ipToCIDR(ip, n);
            // 调用对比方法
            List<String> result2 = comparator(ip, n);

            // 验证结果一致性
            if (!result1.equals(result2)) {
                System.out.println("Test Failed!");
                System.out.println("IP: " + ip + ", n: " + n);
                System.out.println("Target Method: " + result1);
                System.out.println("Comparator: " + result2);
                return;
            }
        }
        System.out.println("All Tests Passed!");
    }
}