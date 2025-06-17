package class23;

import class05.Hash;

import java.util.*;
import java.util.Map.Entry;

public class Code04_FindKMajorityEdition2 {

    public static void printHalfMajor(int[] arr) {
        if (arr == null || arr.length == 0) {
            System.out.println("No such major");
            return;
        }
        int cand = arr[0];
        int HP = 1;
        for (int i = 1; i < arr.length; i++) {
            if (HP == 0) {
                cand = arr[i];
                HP = 1;
            } else if (cand == arr[i]) {
                HP++;
            } else {
                HP--;
            }
        }
        if (HP != 0) {
            HP = 0;
            for (int i = 0; i < arr.length; i++) {
                if (arr[i] == cand) {
                    HP++;
                }
            }
        }
        System.out.println(HP > arr.length / 2 ? cand : "No such major");
    }

    public static void printKMajor(int[] arr, int K) {
        if (K < 2) {
            System.out.println("the value of K is invalid.");
            return;
        }
        if (arr == null || arr.length < K) {
            System.out.println("No such major");
            return;
        }
        // 攒候选，cands，候选表，最多K-1条记录！ > N / K次的数字，最多有K-1个
        HashMap<Integer, Integer> cands = new HashMap<>();
        for (int i = 0; i < arr.length; i++) {
            if (cands.containsKey(arr[i])) {
                cands.put(arr[i], cands.get(arr[i]) + 1);
            } else { // arr[i] 不是候选
                if (cands.size() == K - 1) { // 当前数肯定不要！，每一个候选付出1点血量，血量变成0的候选，要删掉！
                    allCandsMinusOne(cands);
                } else {
                    cands.put(arr[i], 1);
                }
            }
        }
        // 所有可能的候选，都在cands表中！遍历一遍arr，每个候选收集真实次数

        HashMap<Integer, Integer> reals = getReals(cands, arr);
        boolean printCands = false;
        for (Entry<Integer, Integer> entry : reals.entrySet()) {
            if (entry.getValue() > arr.length / K) {
                printCands = true;
                System.out.print(entry.getKey() + " ");
            }
        }
        System.out.println(printCands ? "" : "No such major");
    }

    public static void allCandsMinusOne(HashMap<Integer, Integer> map) {
        Iterator<Entry<Integer, Integer>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, Integer> entry = iterator.next();
            int value = entry.getValue();
            if (value == 1) {
                iterator.remove();  // 安全删除当前元素
            } else {
                entry.setValue(value - 1);  // 直接更新值
            }
        }
    }

    public static HashMap<Integer, Integer> getReals(HashMap<Integer, Integer> cands, int[] arr) {
        HashMap<Integer, Integer> reals = new HashMap<>();
        for (Entry<Integer, Integer> entry : cands.entrySet()) {
            reals.put(entry.getKey(), 0);
        }
        for (int i = 0; i < arr.length; i++) {
            if (reals.containsKey(arr[i])) {
                reals.put(arr[i], reals.get(arr[i]) + 1);
            }
        }
        return reals;
    }

    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 1, 1, 2, 1};
//        int[] arr = {};
        printHalfMajor(arr);
        int K = 4;
        printKMajor(arr, K);
    }

}
