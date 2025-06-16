package class23;

import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

public class Code04_FindKMajorityEdition1 {

    public static void printHalfMajor(int[] arr) {
        int cand = 0;
        int HP = 0;
        for (int i = 0; i < arr.length; i++) {
            if (HP == 0) {
                cand = arr[i];
                HP++;
            } else if (arr[i] == cand) {
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
        } else {
            System.out.println("No Major");
        }
        if (HP > arr.length / 2) {
            System.out.println(cand);
        }
    }

    public static void printKMajor(int[] arr, int K) {
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < arr.length; i++) {
            if (map.containsKey(arr[i])) {
                map.put(arr[i], map.get(arr[i]) + 1);
            } else if (map.size() == K - 1) {
                allCandsMinusOne(map);
            } else {
                map.put(arr[i], 1);
            }
        }
        HashMap<Integer, Integer> reals = getReal(arr, map);
        boolean hasPrint = false;
        for (Entry<Integer, Integer> entry : reals.entrySet()) {
            if (entry.getValue() > arr.length / K) {
                System.out.print(entry.getKey() + " ");
                hasPrint = true;
            }
        }
        System.out.println(hasPrint?"":"No such major");
    }

    public static HashMap<Integer, Integer> getReal(int[] arr, HashMap<Integer, Integer> cands) {
        HashMap<Integer, Integer> ans = new HashMap<>();
        for (Integer i : cands.keySet()) {
            ans.put(i, 0);
        }
        for (int i = 0; i < arr.length; i++) {
            if (ans.containsKey(arr[i])) {
                ans.put(arr[i], ans.get(arr[i]) + 1);
            }
        }
        return ans;
    }

    public static void allCandsMinusOne(HashMap<Integer, Integer> map) {
        List<Integer> removeList = new LinkedList<>();
        for (Entry<Integer, Integer> entry : map.entrySet()) {
            if (entry.getValue() == 1) {
                removeList.add(entry.getKey());
            }
            map.put(entry.getKey(), map.get(entry.getKey()) - 1);
        }
        for (Integer i : removeList) {
            map.remove(i);
        }
    }

    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 1, 1, 2, 1};
        printHalfMajor(arr);
        int K = 4;
        printKMajor(arr, K);
    }

}
