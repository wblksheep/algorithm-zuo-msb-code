package class31;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Code01_SegmentTreeEdition2 {

    public static class SegmentTree {
        private int MAXN;
        private int[] arr;
        private int[] sum;
        private int[] lazy;
        private int[] change;
        private boolean[] update;

        private int start; // 记录整个区间的起始位置
        private int end;   // 记录整个区间的结束位置
        private Map<Integer, String> nodeInfoMap = new HashMap<>(); // 存储节点信息的映射

        public SegmentTree(int[] origin) {
            MAXN = origin.length + 1;
            arr = new int[MAXN];
            for (int i = 1; i < MAXN; i++) {
                arr[i] = origin[i - 1];
            }
            sum = new int[MAXN << 2];
            lazy = new int[MAXN << 2];
            change = new int[MAXN << 2];
            update = new boolean[MAXN << 2];


            // 打印树相关节点信息所需值
            start = 1;
            end = origin.length;
        }

        public void add(int L, int R, int C, int l, int r, int rt) {
            if (L <= l && r <= R) {
                sum[rt] += C * (r - l + 1);
                lazy[rt] += C;
                return;
            }
            int mid = (l + r) >> 1;
            pushDown(l, r, mid - l + 1, r - mid, rt);
            if (L <= mid) {
                add(L, R, C, l, mid, rt << 1);
            }
            if (R > mid) {
                add(L, R, C, mid + 1, r, rt << 1 | 1);
            }
            pushUp(rt);
        }

        private void pushDown(int l, int r, int ln, int rn, int rt) {
            if (update[rt]) {
                update[rt] = false;
                sum[rt << 1] = change[rt] * ln;
                lazy[rt << 1] = 0;
                change[rt << 1] = change[rt];
                update[rt << 1] = true;
                sum[rt << 1 | 1] = change[rt] * rn;
                lazy[rt << 1 | 1] = 0;
                change[rt << 1 | 1] = change[rt];
                update[rt << 1 | 1] = true;
            }
            if (lazy[rt] != 0) {
                sum[rt << 1] += lazy[rt] * ln;
                lazy[rt << 1] += lazy[rt];
                sum[rt << 1 | 1] += lazy[rt] * rn;
                lazy[rt << 1 | 1] += lazy[rt];
                lazy[rt] = 0;
            }
        }

        public void update(int L, int R, int C, int l, int r, int rt) {
            if (L <= l && r <= R) {
                sum[rt] = C * (r - l + 1);
                update[rt] = true;
                change[rt] = C;
                lazy[rt] = 0;
                return;
            }
            int mid = (l + r) >> 1;
            pushDown(l, r, mid - l + 1, r - mid, rt);
            if (L <= mid) {
                update(L, R, C, l, mid, rt << 1);
            }
            if (R > mid) {
                update(L, R, C, mid + 1, r, rt << 1 | 1);
            }
            pushUp(rt);
        }

        public long query(int L, int R, int l, int r, int rt) {
            if (L <= l && r <= R) {
                return sum[rt];
            }
            int mid = (l + r) >> 1;
            pushDown(l, r, mid - l + 1, r - mid, rt);
            long ans = 0;
            if (L <= mid) {
                ans += query(L, R, l, mid, rt << 1);
            }
            if (R > mid) {
                ans += query(L, R, mid + 1, r, rt << 1 | 1);
            }
            return ans;
        }

        private void pushUp(int rt) {
            sum[rt] = sum[rt << 1] + sum[rt << 1 | 1];
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

        /*
        打印树相关代码
        */
        // 打印整棵树结构
        public void printTree() {
            System.out.println("线段树结构（节点编号 | 区间范围 | 节点值 | 懒惰标记 | 更新值 | 更新标记）:");
            printTree(start, end, 1);
        }

        private void printTree(int l, int r, int rt) {
            System.out.printf("节点[%d] 区间[%d, %d] -> sum=%d, lazy=%d, change=%d, update=%b\n",
                    rt, l, r, sum[rt], lazy[rt], change[rt], update[rt]);
            if (l == r) {
                return;
            }
            int mid = (l + r) >> 1;
            printTree(l, mid, rt << 1);       // 递归打印左子树
            printTree(mid + 1, r, rt << 1 | 1); // 递归打印右子树
        }

        /**
         * 在图形界面中显示树形结构
         */
        public void displayTreeGUI() {
            // 创建根节点
            DefaultMutableTreeNode rootNode = buildGUITreeNode(start, end, 1, 0);

            // 创建树模型
            DefaultTreeModel treeModel = new DefaultTreeModel(rootNode);

            // 创建JTree并设置自定义渲染器
            JTree tree = new JTree(treeModel);
            tree.setCellRenderer(new SegmentTreeCellRenderer());

            // 添加鼠标悬停提示
            ToolTipManager.sharedInstance().registerComponent(tree);

            // 设置节点展开状态
            expandAllNodes(tree, 0, tree.getRowCount());

            // 创建树面板
            JPanel treePanel = new JPanel(new BorderLayout());
            treePanel.add(new JScrollPane(tree), BorderLayout.CENTER);

            // 添加工具栏
            JToolBar toolBar = new JToolBar();
            JButton refreshBtn = new JButton("刷新视图");
            JButton expandBtn = new JButton("展开全部");
            JButton collapseBtn = new JButton("折叠全部");

            refreshBtn.addActionListener(e -> refreshTreeView(tree));
            expandBtn.addActionListener(e -> expandAllNodes(tree, true));
            collapseBtn.addActionListener(e -> expandAllNodes(tree, false));

            toolBar.add(refreshBtn);
            toolBar.add(expandBtn);
            toolBar.add(collapseBtn);

            // 创建控制面板
            JPanel controlPanel = new JPanel(new GridLayout(0, 1));
            controlPanel.add(createNodeInfoPanel());

            // 创建主面板
            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                    treePanel, controlPanel);
            splitPane.setDividerLocation(600);

            // 创建主窗口
            JFrame frame = new JFrame("线段树结构可视化（美化版）");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(1200, 800);
            frame.add(splitPane, BorderLayout.CENTER);
            frame.add(toolBar, BorderLayout.NORTH);
            frame.setLocationRelativeTo(null); // 居中显示

            // 显示窗口
            frame.setVisible(true);
        }

        /**
         * 创建节点信息面板
         */
        private JPanel createNodeInfoPanel() {
            JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
            panel.setBorder(BorderFactory.createTitledBorder("节点统计信息"));

            // 添加统计信息
            int totalNodes = nodeInfoMap.size();
            int leafNodes = (end - start + 1);
            int internalNodes = totalNodes - leafNodes;

            panel.add(createInfoLabel("总节点数: " + totalNodes));
            panel.add(createInfoLabel("叶子节点: " + leafNodes));
            panel.add(createInfoLabel("内部节点: " + internalNodes));
            panel.add(createInfoLabel("根节点值: " + sum[1]));
            panel.add(createInfoLabel("区间范围: [" + start + ", " + end + "]"));

            // 添加图示说明
            JPanel legendPanel = new JPanel(new GridLayout(0, 1, 5, 5));
            legendPanel.setBorder(BorderFactory.createTitledBorder("节点颜色说明"));
            legendPanel.add(createLegendItem("根节点", Color.RED));
            legendPanel.add(createLegendItem("内部节点", Color.BLUE));
            legendPanel.add(createLegendItem("叶子节点", Color.GREEN));
            legendPanel.add(createLegendItem("已更新节点", new Color(255, 200, 0))); // 琥珀色
            legendPanel.add(createLegendItem("懒惰标记节点", new Color(128, 0, 128))); // 紫色

            panel.add(legendPanel);

            return panel;
        }

        private JLabel createInfoLabel(String text) {
            JLabel label = new JLabel(text);
            label.setFont(new Font("SansSerif", Font.BOLD, 14));
            label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            return label;
        }

        private JPanel createLegendItem(String text, Color color) {
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
            JLabel colorLabel = new JLabel();
            colorLabel.setOpaque(true);
            colorLabel.setBackground(color);
            colorLabel.setPreferredSize(new Dimension(20, 20));
            colorLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            JLabel textLabel = new JLabel(text);
            textLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));

            panel.add(colorLabel);
            panel.add(textLabel);
            return panel;
        }

        /**
         * 递归构建GUI树节点（带深度信息）
         */
        private DefaultMutableTreeNode buildGUITreeNode(int l, int r, int rt, int depth) {
            // 创建节点信息字符串
            String nodeInfo = String.format("[%d] [%d,%d] sum=%d lazy=%d change=%d update=%b",
                    rt, l, r, sum[rt], lazy[rt], change[rt], update[rt]);

            // 存储节点信息用于统计
            nodeInfoMap.put(rt, nodeInfo);

            // 创建树节点（添加深度信息）
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(
                    new SegmentTreeNodeInfo(nodeInfo, rt, l, r, depth));

            // 如果是叶子节点则返回
            if (l == r) {
                return node;
            }

            // 递归构建左右子树
            int mid = (l + r) >> 1;
            node.add(buildGUITreeNode(l, mid, rt << 1, depth + 1));
            node.add(buildGUITreeNode(mid + 1, r, rt << 1 | 1, depth + 1));

            return node;
        }

        /**
         * 刷新树视图
         */
        private void refreshTreeView(JTree tree) {
            DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
            DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
            root.removeAllChildren();
            root.setUserObject(updateNodeInfo(root, start, end, 1, 0));
            root.add(buildGUITreeNode(start, end, 1, 0));
            model.reload();
            expandAllNodes(tree, true);
        }

        private Object updateNodeInfo(DefaultMutableTreeNode node, int l, int r, int rt, int depth) {
            String nodeInfo = String.format("[%d] [%d,%d] sum=%d lazy=%d change=%d update=%b",
                    rt, l, r, sum[rt], lazy[rt], change[rt], update[rt]);
            return new SegmentTreeNodeInfo(nodeInfo, rt, l, r, depth);
        }

        /**
         * 展开所有节点
         */
        private void expandAllNodes(JTree tree, boolean expand) {
            int row = 0;
            while (row < tree.getRowCount()) {
                tree.expandRow(row);
                row++;
            }

            if (!expand) {
                tree.collapseRow(0); // 折叠所有节点（只保留根节点展开）
            }
        }

        /**
         * 展开所有节点到指定深度
         */
        private void expandAllNodes(JTree tree, int startRow, int rowCount) {
            for (int i = startRow; i < rowCount; i++) {
                tree.expandRow(i);
            }

            if (tree.getRowCount() != rowCount) {
                expandAllNodes(tree, rowCount, tree.getRowCount());
            }
        }
    }

    /**
     * 线段树节点信息封装类
     */
    static class SegmentTreeNodeInfo {
        String displayText;
        int nodeId;
        int start;
        int end;
        int depth;

        public SegmentTreeNodeInfo(String displayText, int nodeId, int start, int end, int depth) {
            this.displayText = displayText;
            this.nodeId = nodeId;
            this.start = start;
            this.end = end;
            this.depth = depth;
        }

        @Override
        public String toString() {
            return displayText;
        }
    }

    /**
     * 自定义树节点渲染器（美化版）
     */
    static class SegmentTreeCellRenderer extends DefaultTreeCellRenderer {
        private Font baseFont = new Font("微软雅黑", Font.BOLD, 14);
        private Font leafFont = new Font("微软雅黑", Font.PLAIN, 12);

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                      boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {

            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

            if (value instanceof DefaultMutableTreeNode) {
                Object userObj = ((DefaultMutableTreeNode) value).getUserObject();

                if (userObj instanceof SegmentTreeNodeInfo) {
                    SegmentTreeNodeInfo nodeInfo = (SegmentTreeNodeInfo) userObj;

                    // 设置不同节点的背景颜色
                    Color bgColor = getNodeColor(nodeInfo);
                    setBackground(bgColor);
                    setOpaque(true);

                    // 设置不同节点的字体
                    setFont(nodeInfo.depth == 0 ? baseFont : leaf ? leafFont : baseFont.deriveFont(12f));

                    // 添加深度缩进效果
                    String indent = "";
                    for (int i = 0; i < nodeInfo.depth * 2; i++) {
                        indent += "  ";
                    }
                    setText(indent + nodeInfo.displayText);

                    // 添加图标
                    if (nodeInfo.depth == 0) {
                        setIcon(UIManager.getIcon("FileView.computerIcon"));
                    } else if (leaf) {
                        setIcon(UIManager.getIcon("FileView.fileIcon"));
                    } else {
                        setIcon(UIManager.getIcon("FileView.directoryIcon"));
                    }

                    // 设置工具提示
                    setToolTipText(createTooltipText(nodeInfo));
                }
            }

            return this;
        }

        private Color getNodeColor(SegmentTreeNodeInfo nodeInfo) {
            if (nodeInfo.depth == 0) return new Color(255, 200, 200); // 根节点 - 浅红
            if (nodeInfo.start == nodeInfo.end) return new Color(200, 255, 200); // 叶子节点 - 浅绿

            // 根据节点属性设置颜色
            if (nodeInfo.depth <= 2) return new Color(200, 200, 255); // 上层节点 - 浅蓝

            // 特殊节点标记
            if (nodeInfo.nodeId % 5 == 0) return new Color(255, 255, 200); // 特殊节点 - 浅黄

            return Color.WHITE; // 默认背景色
        }

        private String createTooltipText(SegmentTreeNodeInfo nodeInfo) {
            return String.format("<html><b>节点详细信息</b><br>"
                            + "ID: %d<br>区间: [%d, %d]<br>深度: %d<br>"
                            + "</html>",
                    nodeInfo.nodeId, nodeInfo.start, nodeInfo.end, nodeInfo.depth);
        }
    }

    public static class Right {
        public int[] arr;

        public Right(int[] origin) {
            arr = new int[origin.length + 1];
            for (int i = 0; i < origin.length; i++) {
                arr[i + 1] = origin[i];
            }
        }

        public void update(int L, int R, int C) {
            for (int i = L; i <= R; i++) {
                arr[i] = C;
            }
        }

        public void add(int L, int R, int C) {
            for (int i = L; i <= R; i++) {
                arr[i] += C;
            }
        }

        public long query(int L, int R) {
            long ans = 0;
            for (int i = L; i <= R; i++) {
                ans += arr[i];
            }
            return ans;
        }

    }

    public static int[] genarateRandomArray(int len, int max) {
        int size = (int) (Math.random() * len) + 1;
        int[] origin = new int[size];
        for (int i = 0; i < size; i++) {
            origin[i] = (int) (Math.random() * max) - (int) (Math.random() * max);
        }
        return origin;
    }

    public static boolean test() {
        int len = 10;
        int max = 10;
        int testTimes = 5000;
        int addOrUpdateTimes = 1000;
        int queryTimes = 500;
        for (int i = 0; i < testTimes; i++) {
            int[] origin = genarateRandomArray(len, max);
            SegmentTree seg = new SegmentTree(origin);
            int S = 1;
            int N = origin.length;
            int root = 1;
            seg.build(S, N, root);
            Right rig = new Right(origin);
            for (int j = 0; j < addOrUpdateTimes; j++) {
                int num1 = (int) (Math.random() * N) + 1;
                int num2 = (int) (Math.random() * N) + 1;
                int L = Math.min(num1, num2);
                int R = Math.max(num1, num2);
                int C = (int) (Math.random() * max) - (int) (Math.random() * max);
                if (Math.random() < 0.5) {
                    seg.add(L, R, C, S, N, root);
                    rig.add(L, R, C);
                } else {
                    seg.update(L, R, C, S, N, root);
                    rig.update(L, R, C);
                }
            }
            for (int k = 0; k < queryTimes; k++) {
                int num1 = (int) (Math.random() * N) + 1;
                int num2 = (int) (Math.random() * N) + 1;
                int L = Math.min(num1, num2);
                int R = Math.max(num1, num2);
                long ans1 = seg.query(L, R, S, N, root);
                long ans2 = rig.query(L, R);
                if (ans1 != ans2) {
                    System.out.println(ans1);
                    System.out.println(ans2);
                    System.out.println(L);
                    System.out.println(R);
                    for (i = 0; i < origin.length - 1; i++) {
                        System.out.print(origin[i] + ", ");
                    }
                    System.out.println(origin[origin.length - 1]);
                    seg.displayTreeGUI();
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        // 在Swing事件调度线程中运行GUI
        SwingUtilities.invokeLater(() -> {
            int[] origin = {3, -8, -6};
            SegmentTree seg = new SegmentTree(origin);
            int S = 1;
            int N = origin.length;
            int root = 1;
            int L = 1;
            int R = 2;
            int C = 4;

            // 构建线段树
            seg.build(S, N, root);
            System.out.println("构建后的线段树:");
            // 在图形界面中显示树形结构
            seg.displayTreeGUI();

            // 执行add操作
            seg.add(L, R, C, S, N, root);
            System.out.println("\nAdd操作后的线段树:");
            // 在图形界面中显示树形结构
            seg.displayTreeGUI();

            // 执行update操作
            seg.update(L, R, C, S, N, root);
            System.out.println("\nUpdate操作后的线段树:");
            // 在图形界面中显示树形结构
            seg.displayTreeGUI();
//
//            // 查询操作
//            long sum = seg.query(L, R, S, N, root);
//            System.out.println("\n查询结果: " + sum);
        });

        System.out.println("对数器测试开始...");
        System.out.println("测试结果 : " + (test() ? "通过" : "未通过"));
    }

}
