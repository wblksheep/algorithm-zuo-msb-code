package class25;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Code01_IPToCIDR_Checker {

    // 生成随机IP地址
    public static String generateRandomIP() {
        Random rand = new Random();
        return rand.nextInt(256) + "." +
                rand.nextInt(256) + "." +
                rand.nextInt(256) + "." +
                rand.nextInt(256);
    }

    // 把整数转换为IP字符串
    public static String intToIP(long addr) {
        return ((addr >> 24) & 0xFF) + "." +
                ((addr >> 16) & 0xFF) + "." +
                ((addr >> 8) & 0xFF) + "." +
                (addr & 0xFF);
    }

    // 验证CIDR块是否合法且覆盖所有IP
    public static boolean verify(String startIP, int n, List<String> cidrs) {
        long start = ipToLong(startIP);
        long end = start + n - 1;
        long currentEnd = start - 1; // 当前覆盖的最后一个IP

        for (String cidr : cidrs) {
            // 拆分CIDR字符串
            String[] parts = cidr.split("/");
            if (parts.length != 2) return false;

            // 校验IP格式
            String ip = parts[0];
            if (!ip.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")) {
                return false;
            }

            // 解析IP和掩码
            long base = ipToLong(ip);
            int maskBits = Integer.parseInt(parts[1]);

            // 验证掩码位在有效范围
            if (maskBits < 0 || maskBits > 32) return false;

            // 计算CIDR块大小
            long blockSize = 1L << (32 - maskBits);
            long blockEnd = base + blockSize - 1;

            // 检查对齐：base必须能被块大小整除
            if ((base & (blockSize - 1)) != 0) return false;

            // 检查CIDR块是否连续
            if (base != currentEnd + 1) return false;

            // 更新当前覆盖范围
            currentEnd = blockEnd;
        }

        // 验证是否完整覆盖
        return currentEnd == end;
    }

    // IP字符串转长整型（无符号）
    public static long ipToLong(String ip) {
        long result = 0;
        String[] parts = ip.split("\\.");
        for (String part : parts) {
            int num = Integer.parseInt(part);
            if (num < 0 || num > 255) return -1; // 非法IP
            result = (result << 8) | num;
        }
        return result & 0xFFFFFFFFL; // 确保无符号
    }

    // 对数器主测试函数
    public static void main(String[] args) {
        int testTimes = 10000;
        Random rand = new Random();

        for (int i = 0; i < testTimes; i++) {
            // 生成合法起始IP
            String ip;
            int n;
            long startLong;

            do {
//                ip = generateRandomIP();
                ip = "220.143.158.191";
//                n = rand.nextInt(10) + 1; // n: 1~1000
                n = 10;
                startLong = ipToLong(ip);
            } while (startLong + n > 0xFFFFFFFFL); // 确保不越界

            // 调用待测方法
            List<String> cidrs = Code01_IPToCIDR.ipToCIDR(ip, n);

            // 验证结果
            if (!verify(ip, n, cidrs)) {
                System.out.println("测试失败！");
                System.out.println("起始IP: " + ip);
                System.out.println("需要覆盖IP数量: " + n);
                System.out.println("生成的CIDR块:");
                for (String cidr : cidrs) System.out.println(cidr);
                System.out.println("覆盖范围: " +
                        intToIP(startLong) + " - " +
                        intToIP(startLong + n - 1));
                return;
            }
        }
        System.out.println("所有测试通过！");
    }
}
