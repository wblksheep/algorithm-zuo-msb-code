package class25;

import java.util.*;

public class ThreeSumChecker2 {

    public static List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> ans = new ArrayList<>();
        int n = nums.length;
        Arrays.sort(nums);
        for (int i = 0; i < n - 1; i++) {
            if (i == 0 || (nums[i] != nums[i - 1])) {
                List<List<Integer>> res = twoSum(i + 1, nums, -nums[i]);
                for (List<Integer> list : res) {
                    list.add(nums[i]);
                    ans.add(list);
                }
            }
        }
        return ans;
    }

    public static List<List<Integer>> twoSum(int i, int[] nums, int target) {
        List<List<Integer>> res = new ArrayList<>();
        int l = i;
        int n = nums.length;
        int r = n - 1;
        while (l < r) {
            if (nums[l] + nums[r] == target) {
                if (r == n - 1 || nums[r] != nums[r + 1]) {
                    List<Integer> cur = new ArrayList<>();
                    cur.add(nums[l]);
                    cur.add(nums[r]);
                    res.add(cur);
                }
                r--;
            } else if (nums[l] + nums[r] < target) {
                l++;
            } else {
                r--;
            }
        }
        return res;
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
//            int[] nums = {-9, -5, -5, 0, 0, 3, 4, 5};
//            int[] nums = {-9, -6, -2, -1, 8, 10};
//            int[] nums = {-8, -6, -3, -1, -1, 2, 5, 8, 9, 10};
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
