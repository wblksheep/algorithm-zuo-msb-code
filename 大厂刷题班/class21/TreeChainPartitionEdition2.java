package class21;

import java.util.HashMap;

public class TreeChainPartitionEdition2 {


    public static class TreeChain {
        public int n;
        public int[] fa;
        public int[] val;
        public int[] siz;
        public int[] son;
        public int[] dep;
        public int[] dfn;
        public int[] tnw;
        public int[] top;
        public int[][] tree;
        public int h;
        public int tim = 0;
        public SegmentTree seg;

        public TreeChain(int[] father, int[] values) {
            initTree(father, values);

            dfs1(h, 0);

            dfs2(h, h);

            seg = new SegmentTree(tnw);

            seg.build(1, n, 1);
        }

        private void dfs2(int c, int t) {
            dfn[c] = ++tim;
            tnw[tim] = val[c];
            top[c] = t;
            if (son[c] != 0) {
                dfs2(son[c], t);
                for (int s : tree[c]) {
                    if (s != son[c]) {
                        dfs2(s, s);
                    }
                }
            }
        }

        private void dfs1(int c, int f) {
            fa[c] = f;
            siz[c] = 1;
            dep[c] = dep[f] + 1;
            int maxSize = 0;
            for (int s : tree[c]) {
                dfs1(s, c);
                siz[c] += siz[s];
                if (siz[s] > maxSize) {
                    maxSize = siz[s];
                    son[c] = s;
                }
            }
        }

        public void initTree(int[] father, int[] values) {
            n = father.length + 1;
            fa = new int[n];
            val = new int[n];
            siz = new int[n];
            son = new int[n];
            dep = new int[n];
            dfn = new int[n];
            tnw = new int[n];
            top = new int[n];
            tree = new int[n--][];
            int[] cnts = new int[n];
            for (int i = 0; i < n; i++) {
                if (i == father[i]) {
                    h = i + 1;
                } else {
                    cnts[father[i]]++;
                }
                val[i + 1] = values[i];
            }
            for (int i = 0; i < n; i++) {
                tree[i + 1] = new int[cnts[i]];
            }
            for (int i = 0; i < n; i++) {
                if (i + 1 != h) {
                    tree[father[i] + 1][--cnts[father[i]]] = i + 1;
                }
            }
        }

        public void addSubtree(int head, int v) {
            head++;
            seg.add(dfn[head], dfn[head] + siz[head] - 1, v, 1, n, 1);
        }

        public long querySubtree(int head) {
            head++;
            return seg.query(dfn[head], dfn[head] + siz[head] - 1, 1, n, 1);
        }

        public void addChain(int a, int b, int v) {
            a++;
            b++;
            while (top[a] != top[b]) {
                if (dep[top[a]] > dep[top[b]]) {
                    seg.add(dfn[top[a]], dfn[a], v, 1, n, 1);
                    a = fa[top[a]];
                } else {
                    seg.add(dfn[top[b]], dfn[b], v, 1, n, 1);
                    b = fa[top[b]];
                }
            }
            if (dep[a] > dep[b]) {
                seg.add(dfn[b], dfn[a], v, 1, n, 1);
            } else {
                seg.add(dfn[a], dfn[b], v, 1, n, 1);
            }
        }

        public long queryChain(int a, int b) {
            a++;
            b++;
            long ans = 0;
            while (top[a] != top[b]) {
                if (dep[top[a]] > dep[top[b]]) {
                    ans += seg.query(dfn[top[a]], dfn[a], 1, n, 1);
                    a = fa[top[a]];
                } else {
                    ans += seg.query(dfn[top[b]], dfn[b], 1, n, 1);
                    b = fa[top[b]];
                }
            }
            if (dep[a] > dep[b]) {
                ans += seg.query(dfn[b], dfn[a], 1, n, 1);
            } else {
                ans += seg.query(dfn[a], dfn[b], 1, n, 1);
            }
            return ans;
        }
    }

    public static class SegmentTree {
        private int n;
        private int[] arr;
        private int[] sum;
        private int[] lazy;

        public SegmentTree(int[] origin) {
            n = origin.length;
            arr = new int[n];
            for (int i = 1; i < n; i++) {
                arr[i] = origin[i];
            }
            sum = new int[n << 2];
            lazy = new int[n << 2];
        }

        public void build(int l, int r, int rt) {
            if (l == r) {
                sum[rt] = arr[l];
                return;
            }
            int mid = (l + r) >> 1;
            build(l, mid, rt << 1);
            build(mid + 1, r, rt << 1 | 1);
            pushUp(rt);
        }

        private void pushUp(int rt) {
            sum[rt] = sum[rt << 1] + sum[rt << 1 | 1];
        }

        public void add(int L, int R, int C, int l, int r, int rt) {
            if (L <= l && r <= R) {
                sum[rt] += C * (r - l + 1);
                lazy[rt] += C;
                return;
            }
            int mid = (l + r) >> 1;
            pushDown(mid - l + 1, r - mid, rt);
            if (L <= mid) {
                add(L, R, C, l, mid, rt << 1);
            }
            if (R > mid) {
                add(L, R, C, mid + 1, r, rt << 1 | 1);
            }
            pushUp(rt);
        }

        public long query(int L, int R, int l, int r, int rt) {
            if (L <= l && r <= R) {
                return sum[rt];
            }
            int mid = (l + r) >> 1;
            pushDown(mid - l + 1, r - mid, rt);
            long ans = 0;
            if (L <= mid) {
                ans += query(L, R, l, mid, rt << 1);
            }
            if (R > mid) {
                ans += query(L, R, mid + 1, r, rt << 1 | 1);
            }
            return ans;
        }

        private void pushDown(int ln, int rn, int rt) {
            if (lazy[rt] != 0) {
                sum[rt << 1] += lazy[rt] * ln;
                lazy[rt << 1] += lazy[rt];
                sum[rt << 1 | 1] += lazy[rt] * rn;
                lazy[rt << 1 | 1] += lazy[rt];
                lazy[rt] = 0;
            }
        }
    }


    // 为了测试，这个结构是暴力但正确的方法
    public static class Right {
        public int n;
        public int[][] tree;
        public int[] fa;
        public int[] val;
        public HashMap<Integer, Integer> path;

        public Right(int[] father, int[] value) {
            n = father.length;
            tree = new int[n][];
            fa = new int[n];
            val = new int[n];
            for (int i = 0; i < n; i++) {
                fa[i] = father[i];
                val[i] = value[i];
            }
            int[] help = new int[n];
            int h = 0;
            for (int i = 0; i < n; i++) {
                if (father[i] == i) {
                    h = i;
                } else {
                    help[father[i]]++;
                }
            }
            for (int i = 0; i < n; i++) {
                tree[i] = new int[help[i]];
            }
            for (int i = 0; i < n; i++) {
                if (i != h) {
                    tree[father[i]][--help[father[i]]] = i;
                }
            }
            path = new HashMap<>();
        }

        public void addSubtree(int head, int value) {
            val[head] += value;
            for (int next : tree[head]) {
                addSubtree(next, value);
            }
        }

        public int querySubtree(int head) {
            int ans = val[head];
            for (int next : tree[head]) {
                ans += querySubtree(next);
            }
            return ans;
        }

        public void addChain(int a, int b, int v) {
            path.clear();
            path.put(a, null);
            while (a != fa[a]) {
                path.put(fa[a], a);
                a = fa[a];
            }
            while (!path.containsKey(b)) {
                val[b] += v;
                b = fa[b];
            }
            val[b] += v;
            while (path.get(b) != null) {
                b = path.get(b);
                val[b] += v;
            }
        }

        public int queryChain(int a, int b) {
            path.clear();
            path.put(a, null);
            while (a != fa[a]) {
                path.put(fa[a], a);
                a = fa[a];
            }
            int ans = 0;
            while (!path.containsKey(b)) {
                ans += val[b];
                b = fa[b];
            }
            ans += val[b];
            while (path.get(b) != null) {
                b = path.get(b);
                ans += val[b];
            }
            return ans;
        }

    }

    // 为了测试
    // 随机生成N个节点树，可能是多叉树，并且一定不是森林
    // 输入参数N要大于0
    public static int[] generateFatherArray(int N) {
        int[] order = new int[N];
        for (int i = 0; i < N; i++) {
            order[i] = i;
        }
        for (int i = N - 1; i >= 0; i--) {
            swap(order, i, (int) (Math.random() * (i + 1)));
        }
        int[] ans = new int[N];
        ans[order[0]] = order[0];
        for (int i = 1; i < N; i++) {
            ans[order[i]] = order[(int) (Math.random() * i)];
        }
        return ans;
    }

    // 为了测试
    public static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    // 为了测试
    public static int[] generateValueArray(int N, int V) {
        int[] ans = new int[N];
        for (int i = 0; i < N; i++) {
            ans[i] = (int) (Math.random() * V) + 1;
        }
        return ans;
    }

    // 对数器
    public static void main(String[] args) {
        int N = 10;
        int V = 10;
        int[] father = generateFatherArray(N);
        int[] values = generateValueArray(N, V);
        TreeChain tc = new TreeChain(father, values);
        Right right = new Right(father, values);
        int testTime = 1000000;
        System.out.println("测试开始");
        for (int i = 0; i < testTime; i++) {
            double decision = Math.random();
            if (decision < 0.25) {
                int head = (int) (Math.random() * N);
                int value = (int) (Math.random() * V);
                tc.addSubtree(head, value);
                right.addSubtree(head, value);
            } else if (decision < 0.5) {
                int head = (int) (Math.random() * N);
                if (tc.querySubtree(head) != right.querySubtree(head)) {
                    System.out.println("出错了!");
                }
            } else if (decision < 0.75) {
                int a = (int) (Math.random() * N);
                int b = (int) (Math.random() * N);
                int value = (int) (Math.random() * V);
                tc.addChain(a, b, value);
                right.addChain(a, b, value);
            } else {
                int a = (int) (Math.random() * N);
                int b = (int) (Math.random() * N);
                if (tc.queryChain(a, b) != right.queryChain(a, b)) {
                    System.out.println("出错了!");
                }
            }
        }
        System.out.println("测试结束");
    }

}
