package class25;

import java.util.*;

public class ThreeSumChecker {

    // 待测试方法（原题解法）
    public static List<List<Integer>> threeSum(int[] nums) {
        Arrays.sort(nums);
        int N = nums.length;
        List<List<Integer>> ans = new ArrayList<>();
        for (int i = N - 1; i > 1; i--) {
            if (i == N - 1 || nums[i] != nums[i + 1]) {
                List<List<Integer>> nexts = twoSum(nums, i - 1, -nums[i]);
                for (List<Integer> cur : nexts) {
                    cur.add(nums[i]);
                    ans.add(cur);
                }
            }
        }
        return ans;
    }

    public static List<List<Integer>> twoSum(int[] nums, int end, int target) {
        int L = 0;
        int R = end;
        List<List<Integer>> ans = new ArrayList<>();
        while (L < R) {
            if (nums[L] + nums[R] > target) {
                R--;
            } else if (nums[L] + nums[R] < target) {
                L++;
            } else {
                if (L == 0 || nums[L - 1] != nums[L]) {
                    List<Integer> cur = new ArrayList<>();
                    cur.add(nums[L]);
                    cur.add(nums[R]);
                    ans.add(cur);
                }
                L++;
            }
        }
        return ans;
    }

    // 暴力解法（保证正确性）
    public static List<List<Integer>> bruteForce(int[] nums) {
        Arrays.sort(nums);
        Set<String> set = new HashSet<>(); // 用于三元组去重
        List<List<Integer>> res = new ArrayList<>();
        int n = nums.length;

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                for (int k = j + 1; k < n; k++) {
                    if (nums[i] + nums[j] + nums[k] == 0) {
                        // 生成排序后三元组的唯一标识
                        String key = nums[i] + "," + nums[j] + "," + nums[k];
                        if (!set.contains(key)) {
                            set.add(key);
                            res.add(Arrays.asList(nums[i], nums[j], nums[k]));
                        }
                    }
                }
            }
        }
        return res;
    }

    // 比较两个结果是否等价
    private static boolean isSame(List<List<Integer>> res1, List<List<Integer>> res2) {
        if (res1.size() != res2.size()) return false;

        // 排序每个三元组
        List<List<Integer>> sortedRes1 = new ArrayList<>();
        for (List<Integer> list : res1) {
            List<Integer> sortedList = new ArrayList<>(list);
            Collections.sort(sortedList);
            sortedRes1.add(sortedList);
        }

        List<List<Integer>> sortedRes2 = new ArrayList<>();
        for (List<Integer> list : res2) {
            List<Integer> sortedList = new ArrayList<>(list);
            Collections.sort(sortedList);
            sortedRes2.add(sortedList);
        }

        // 排序整个结果列表
        Comparator<List<Integer>> comparator = (a, b) -> {
            int cmp = a.get(0) - b.get(0);
            if (cmp != 0) return cmp;
            cmp = a.get(1) - b.get(1);
            if (cmp != 0) return cmp;
            return a.get(2) - b.get(2);
        };
        Collections.sort(sortedRes1, comparator);
        Collections.sort(sortedRes2, comparator);

        // 逐项比较
        for (int i = 0; i < sortedRes1.size(); i++) {
            List<Integer> a = sortedRes1.get(i);
            List<Integer> b = sortedRes2.get(i);
            if (!a.get(0).equals(b.get(0)) || !a.get(1).equals(b.get(1)) || !a.get(2).equals(b.get(2))) {
                return false;
            }
        }
        return true;
    }

    // 生成随机测试数组
    private static int[] generateRandomArray(int maxSize, int maxValue) {
        int size = (int) (Math.random() * (maxSize + 1));
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = (int) (Math.random() * (2 * maxValue + 1)) - maxValue;
        }
        return arr;
    }

    // 主测试函数
    public static void main(String[] args) {
        int testTimes = 10000;
        int maxSize = 10;   // 控制数组大小（避免暴力解法过慢）
        int maxValue = 10;  // 数值范围[-50, 50]
        boolean success = true;

        for (int i = 0; i < testTimes; i++) {
            int[] nums = generateRandomArray(maxSize, maxValue);
//            int[] nums = {3, 2, 4, 5, 1, 1, 2, 3, 4, -7};
            List<List<Integer>> res1 = threeSum(nums);
            List<List<Integer>> res2 = bruteForce(nums);

            if (!isSame(res1, res2)) {
                success = false;
                System.out.println("测试失败！");
                System.out.println("输入数组: " + Arrays.toString(nums));
                System.out.println("待测方法结果: " + res1);
                System.out.println("暴力解法结果: " + res2);
                break;
            }
        }
        System.out.println(success ? "所有测试通过！" : "存在错误！");
    }
}
