package class23;

import java.util.HashSet;

public class Code01_LCATarjanAndTreeChainPartitionEdition3 {

    // 给定数组tree大小为N，表示一共有N个节点
    // tree[i] = j 表示点i的父亲是点j，tree一定是一棵树而不是森林
    // queries是二维数组，大小为M*2，每一个长度为2的数组都表示一条查询
    // [4,9], 表示想查询4和9之间的最低公共祖先
    // [3,7], 表示想查询3和7之间的最低公共祖先
    // tree和queries里面的所有值，都一定在0~N-1之间
    // 返回一个数组ans，大小为M，ans[i]表示第i条查询的答案

    // 暴力方法
    public static int[] query1(int[] father, int[][] queries) {
        int M = queries.length;
        int[] ans = new int[M];
        HashSet<Integer> path = new HashSet<>();
        for (int i = 0; i < M; i++) {
            int jump = queries[i][0];
            while (father[jump] != jump) {
                path.add(jump);
                jump = father[jump];
            }
            path.add(jump);
            jump = queries[i][1];
            while (!path.contains(jump)) {
                jump = father[jump];
            }
            ans[i] = jump;
            path.clear();
        }
        return ans;
    }

    public static int[] query2(int[] father, int[][] queries) {
        int N = father.length;
        int M = queries.length;
        int[][] mt = new int[N][];
        int h = 0;
        int[] cnts = new int[N];
        for (int i = 0; i < N; i++) {
            if (i == father[i]) {
                h = i;
            } else {
                cnts[father[i]]++;
            }
        }
        for (int i = 0; i < N; i++) {
            mt[i] = new int[cnts[i]];
        }
        for (int i = 0; i < N; i++) {
            if (h != i) {
                mt[father[i]][--cnts[father[i]]] = i;
            }
        }
        int[] ans = new int[M];
        for (int i = 0; i < M; i++) {
            int a = queries[i][0];
            int b = queries[i][1];
            if (a != b) {
                cnts[a]++;
                cnts[b]++;
            }
        }
        int[][] mq = new int[N][];
        int[][] mi = new int[N][];
        for (int i = 0; i < N; i++) {
            mq[i] = new int[cnts[i]];
            mi[i] = new int[cnts[i]];
        }
        for (int i = 0; i < M; i++) {
            int a = queries[i][0];
            int b = queries[i][1];
            if (a != b) {
                mq[a][--cnts[a]] = b;
                mi[a][cnts[a]] = i;
                mq[b][--cnts[b]] = a;
                mi[b][cnts[b]] = i;
            } else {
                ans[i] = a;
            }
        }
        UnionFind uf = new UnionFind(N);
        process(h, mt, mq, mi, ans, uf);
        return ans;
    }

    public static void process(int h, int[][] mt, int[][] mq, int[][] mi, int[] ans, UnionFind uf) {
        for (int s : mt[h]) {
            process(s, mt, mq, mi, ans, uf);
            uf.union(s, h);
            uf.setTag(h);
        }
        int[] q = mq[h];
        int[] i = mi[h];
        for (int k = 0; k < q.length; k++) {
            int tag = uf.getTag(q[k]);
            if (tag != -1) {
                ans[i[k]] = tag;
            }
        }
    }

    public static class UnionFind {
        private int[] parent;
        private int[] sizes;
        private int[] tag;
        private int[] help;

        public UnionFind(int N) {
            parent = new int[N];
            sizes = new int[N];
            tag = new int[N];
            help = new int[N];
            for (int i = 0; i < N; i++) {
                parent[i] = i;
                sizes[i] = 1;
                tag[i] = -1;
            }
        }

        private int find(int i) {
            int hi = 0;
            while (i != parent[i]) {
                help[hi++] = i;
                i = parent[i];
            }
            while (hi > 0) {
                parent[help[--hi]] = i;
            }
            return i;
        }

        public void union(int i, int j) {
            int fi = find(i);
            int fj = find(j);
            if (fi != fj) {
                int big = sizes[fi] > sizes[fj] ? fi : fj;
                int small = big == fi ? fj : fi;
                parent[small] = big;
                sizes[big] += sizes[small];
            }
        }

        public void setTag(int head) {
            tag[find(head)] = head;
        }

        public int getTag(int head) {
            return tag[find(head)];
        }
    }


    public static int[] query3(int[] father, int[][] queries) {
        int N = father.length;
        int M = queries.length;
        TreeChain tc = new TreeChain(father);
        int[] ans = new int[M];
        for (int i = 0; i < M; i++) {
            int a = queries[i][0];
            int b = queries[i][1];
            if (queries[i][0] != queries[i][1]) {
                ans[i] = tc.lca(a, b);
            } else {
                ans[i] = a;
            }
        }
        return ans;
    }

    public static class TreeChain {
        private int n;
        private int h;
        private int[][] tree;
        private int[] fa;
        private int[] dep;
        private int[] siz;
        private int[] son;
        private int[] top;

        public TreeChain(int[] father) {
            initTree(father);
            dfs1(h, 0);
            dfs2(h, h);
        }

        private void dfs1(int c, int f) {
            fa[c] = f;
            dep[c] = dep[f] + 1;
            siz[c] = 1;
            int maxSize = 0;
            for (int s : tree[c]) {
                dfs1(s, c);
                siz[c] += siz[s];
                if (maxSize < siz[s]) {
                    son[c] = s;
                    maxSize = s;
                }
            }
        }

        private void dfs2(int c, int t) {
            top[c] = t;
            if (son[c] != 0) {
                dfs2(son[c], t);
                for (int s : tree[c]) {
                    if (son[c] != s) {
                        dfs2(s, s);
                    }
                }
            }
        }

        private void initTree(int[] father) {
            n = father.length + 1;
            tree = new int[n][];
            fa = new int[n];
            dep = new int[n];
            siz = new int[n];
            son = new int[n];
            top = new int[n--];
            int[] cnts = new int[n];
            for (int i = 0; i < n; i++) {
                if (i == father[i]) {
                    h = i + 1;
                } else {
                    cnts[father[i]]++;
                }
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

        public int lca(int a, int b) {
            a++;
            b++;
            while (top[a] != top[b]) {
                if (dep[top[a]] > dep[top[b]]) {
                    a = fa[top[a]];
                } else {
                    b = fa[top[b]];
                }
            }
            return (dep[a] < dep[b] ? a : b) - 1;
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
    // 随机生成M条查询，点有N个，点的编号在0~N-1之间
    // 输入参数M和N都要大于0
    public static int[][] generateQueries(int M, int N) {
        int[][] ans = new int[M][2];
        for (int i = 0; i < M; i++) {
            ans[i][0] = (int) (Math.random() * N);
            ans[i][1] = (int) (Math.random() * N);
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
    public static boolean equal(int[] a, int[] b) {
        if (a.length != b.length) {
            return false;
        }
        for (int i = 0; i < a.length; i++) {
            if (a[i] != b[i]) {
                return false;
            }
        }
        return true;
    }

    // 为了测试
    public static void main(String[] args) {
        int N = 1000;
        int M = 1000;
        int testTime = 50000;
        System.out.println("测试开始");
        for (int i = 0; i < testTime; i++) {
            int size = (int) (Math.random() * N) + 1;
            int ques = (int) (Math.random() * M) + 1;
            int[] father = generateFatherArray(size);
            int[][] queries = generateQueries(ques, size);
//            int[] father = {9, 0, 4, 7, 7, 1, 7, 7, 7, 7};
//            int[][] queries = {
//                    {4, 3},
//                    {9, 4},
//                    {8, 9},
//                    {3, 4},
//                    {1, 2}
//            };
//            int[] father = {0, 0, 0, 1, 1, 2, 2, 5, 5, 6};
//            int[][] queries = {
//                    {9, 6},
//                    {6, 1},
//                    {6, 2}
//            };
//            int[] ans1 = query1(father, queries);
            int[] ans2 = query2(father, queries);
            int[] ans3 = query3(father, queries);
            if (
//                    !equal(ans1, ans2)
//                            ||
                    !equal(ans2, ans3)
            ) {
                System.out.println("出错了！");
                break;
            }
        }
        System.out.println("测试结束");
    }

}
