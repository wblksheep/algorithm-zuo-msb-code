package class21;

public class TreeChainPartitionEdition5 {

    public static class TreeChain {
        public int h;
        public int[] fa;
        public int[] dep;
        public int[] siz;
        public int[] val;
        public int[] son;
        public int[] dfn;
        public int[] tnw;
        public int[] top;
        public int[][] tree;
        public int tim = 0;
        public int n;
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

        private void initTree(int[] father, int[] values) {
            n = father.length + 1;
            fa = new int[n];
            dep = new int[n];
            siz = new int[n];
            val = new int[n];
            son = new int[n];
            dfn = new int[n];
            tnw = new int[n];
            tree = new int[n][];
            top = new int[n--];
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
            long ans = 0;
            a++;
            b++;
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


    public static void main(String[] args) {
        int[] father = {6, 4, 2, 5, 7, 7, 2, 2, 0, 2};
        int[] values = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        TreeChain tc = new TreeChain(father, values);
        printTC(tc.fa, "fa");
//        [0, 7, 5, 0, 6, 8, 8, 3, 3, 1, 3]
        printTC(tc.val, "val");
//        [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
        printTC(tc.dep, "dep");
//        [0, 3, 4, 1, 4, 3, 3, 2, 2, 4, 2]
        printTC(tc.siz, "siz");
//        [0, 2, 1, 10, 1, 2, 2, 3, 5, 1, 1]
        printTC(tc.son, "son");
//        [0, 9, 0, 8, 0, 2, 4, 1, 6, 0, 0]
        printTC(tc.dfn, "dfn");
//        [0, 9, 6, 1, 4, 5, 3, 8, 2, 10, 7]
        printTC(tc.tnw, "tnw");
//        [0, 3, 8, 6, 4, 5, 2, 10, 7, 1, 9]
        printTC(tc.top, "top");
//        [0, 7, 5, 3, 3, 5, 3, 7, 3, 7, 10]
        for (int i = 0; i < tc.tree.length; i++) {
            printTC(tc.tree[i], "tree[" + i + "]");
        }
//        tree[0]: []
//        tree[1]: [9]
//        tree[2]: []
//        tree[3]: [10, 8, 7]
//        tree[4]: []
//        tree[5]: [2]
//        tree[6]: [4]
//        tree[7]: [1]
//        tree[8]: [6, 5]
//        tree[9]: []
//        tree[10]: []
    }

    public static void printTC(int[] arr, String name) {
        if (arr == null || arr.length == 0) {
            System.out.println(name + ": []");
            return;
        }
        System.out.print(name + ": [");
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            System.out.print(arr[i] + ", ");
        }
        System.out.println(arr[n - 1] + "]");
    }
}
