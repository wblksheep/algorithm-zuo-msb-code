package class21;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import class21.TreeChainPartitionEdition1.TreeChain;

public class TreeChainVisualizer {
    public static void visualize(TreeChain treeChain) {
        // 创建主窗口
        JFrame frame = new JFrame("树链剖分结构可视化");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1200, 800);
        frame.setLayout(new BorderLayout());

        // 创建主面板
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(800);

        // 左侧面板：树结构可视化
        JPanel treePanel = new JPanel(new BorderLayout());
        DefaultMutableTreeNode rootNode = buildTreeNode(treeChain, treeChain.h);
        DefaultTreeModel treeModel = new DefaultTreeModel(rootNode);
        JTree tree = new JTree(treeModel);
        tree.setCellRenderer(new TreeChainCellRenderer(treeChain));
        tree.setShowsRootHandles(true);

        // 设置展开所有节点
        expandAllNodes(tree, true);

        JScrollPane treeScroll = new JScrollPane(tree);
        treePanel.add(treeScroll, BorderLayout.CENTER);

        // 右侧面板：详细信息展示
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 添加树链基本信息
        JPanel basicInfoPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        basicInfoPanel.setBorder(BorderFactory.createTitledBorder("树链基本信息"));

        basicInfoPanel.add(new JLabel("节点总数:"));
        basicInfoPanel.add(new JLabel(String.valueOf(treeChain.n)));

        basicInfoPanel.add(new JLabel("根节点:"));
        basicInfoPanel.add(new JLabel(String.valueOf(treeChain.h)));

        basicInfoPanel.add(new JLabel("DFS序列总数:"));
        basicInfoPanel.add(new JLabel(String.valueOf(treeChain.tim)));

        infoPanel.add(basicInfoPanel);

        // 添加节点选择器
        JPanel nodeSelectorPanel = new JPanel();
        nodeSelectorPanel.setBorder(BorderFactory.createTitledBorder("节点详情"));

        JComboBox<Integer> nodeSelector = new JComboBox<>();
        for (int i = 1; i <= treeChain.n; i++) {
            nodeSelector.addItem(i);
        }

        JTextArea nodeInfoArea = new JTextArea(10, 30);
        nodeInfoArea.setEditable(false);

        nodeSelector.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int node = (Integer) nodeSelector.getSelectedItem();
                displayNodeInfo(treeChain, node, nodeInfoArea);
            }
        });

        nodeSelectorPanel.add(new JLabel("选择节点:"));
        nodeSelectorPanel.add(nodeSelector);
        nodeSelectorPanel.add(new JScrollPane(nodeInfoArea));

        infoPanel.add(nodeSelectorPanel);

        // 添加重链信息
        JPanel chainInfoPanel = new JPanel(new BorderLayout());
        chainInfoPanel.setBorder(BorderFactory.createTitledBorder("重链信息"));

        JTextArea chainInfoArea = new JTextArea(10, 30);
        chainInfoArea.setEditable(false);
        chainInfoArea.setText(getChainInfo(treeChain));

        chainInfoPanel.add(new JScrollPane(chainInfoArea), BorderLayout.CENTER);
        infoPanel.add(chainInfoPanel);

        // 添加操作按钮
        JPanel buttonPanel = new JPanel();
        JButton expandBtn = new JButton("展开所有节点");
        JButton collapseBtn = new JButton("折叠所有节点");

        expandBtn.addActionListener(e -> expandAllNodes(tree, true));
        collapseBtn.addActionListener(e -> expandAllNodes(tree, false));

        buttonPanel.add(expandBtn);
        buttonPanel.add(collapseBtn);

        infoPanel.add(buttonPanel);

        // 添加到主面板
        splitPane.setLeftComponent(treePanel);
        splitPane.setRightComponent(infoPanel);
        frame.add(splitPane, BorderLayout.CENTER);

        // 显示窗口
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void expandAllNodes(JTree tree, boolean expand) {
        int row = 0;
        while (row < tree.getRowCount()) {
            tree.expandRow(row);
            row++;
        }

        if (!expand) {
            tree.collapseRow(0);
        }
    }

    private static DefaultMutableTreeNode buildTreeNode(TreeChain treeChain, int nodeIndex) {
        // 创建节点信息字符串
        String nodeInfo = String.format("节点%d (深度:%d 大小:%d)",
                nodeIndex, treeChain.dep[nodeIndex], treeChain.siz[nodeIndex]);

        // 创建树节点
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(nodeInfo);
        node.setAllowsChildren(true);

        // 存储节点索引作为用户对象
        node.setUserObject(new NodeInfo(nodeIndex, nodeInfo));

        // 递归构建子节点
        for (int child : treeChain.tree[nodeIndex]) {
            node.add(buildTreeNode(treeChain, child));
        }

        return node;
    }

    private static void displayNodeInfo(TreeChain treeChain, int node, JTextArea infoArea) {
        StringBuilder sb = new StringBuilder();
        sb.append("节点编号: ").append(node).append("\n");
        sb.append("父节点: ").append(treeChain.fa[node]).append("\n");
        sb.append("深度: ").append(treeChain.dep[node]).append("\n");
        sb.append("子树大小: ").append(treeChain.siz[node]).append("\n");
        sb.append("重儿子: ").append(treeChain.son[node]).append("\n");
        sb.append("重链头: ").append(treeChain.top[node]).append("\n");
        sb.append("DFS序号: ").append(treeChain.dfn[node]).append("\n");
        sb.append("节点权重: ").append(treeChain.val[node]).append("\n");
        sb.append("DFS序列权重: ").append(treeChain.tnw[treeChain.dfn[node]]).append("\n");

        infoArea.setText(sb.toString());
    }

    private static String getChainInfo(TreeChain treeChain) {
        // 统计重链信息
        Map<Integer, StringBuilder> chains = new HashMap<>();

        for (int i = 1; i <= treeChain.n; i++) {
            int top = treeChain.top[i];
            if (!chains.containsKey(top)) {
                chains.put(top, new StringBuilder());
            }
            chains.get(top).append(i).append(" -> ");
        }

        // 构建重链信息字符串
        StringBuilder sb = new StringBuilder();
        sb.append("重链总数: ").append(chains.size()).append("\n\n");

        for (Map.Entry<Integer, StringBuilder> entry : chains.entrySet()) {
            String chain = entry.getValue().toString();
            chain = chain.substring(0, chain.length() - 4); // 移除最后的" -> "
            sb.append("重链头: ").append(entry.getKey()).append("\n");
            sb.append("节点序列: ").append(chain).append("\n\n");
        }

        return sb.toString();
    }

    static class NodeInfo {
        int nodeIndex;
        String displayText;

        public NodeInfo(int nodeIndex, String displayText) {
            this.nodeIndex = nodeIndex;
            this.displayText = displayText;
        }

        @Override
        public String toString() {
            return displayText;
        }
    }

    static class TreeChainCellRenderer extends DefaultTreeCellRenderer {
        private TreeChain treeChain;
        private Font baseFont = new Font("微软雅黑", Font.BOLD, 14);
        private Font chainFont = new Font("微软雅黑", Font.BOLD, 13);
        private Font normalFont = new Font("微软雅黑", Font.PLAIN, 12);

        public TreeChainCellRenderer(TreeChain treeChain) {
            this.treeChain = treeChain;
        }

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                      boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {

            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

            if (value instanceof DefaultMutableTreeNode) {
                Object userObj = ((DefaultMutableTreeNode) value).getUserObject();

                if (userObj instanceof NodeInfo) {
                    NodeInfo nodeInfo = (NodeInfo) userObj;
                    int nodeIndex = nodeInfo.nodeIndex;

                    // 设置不同节点的背景颜色
                    setBackground(getNodeColor(treeChain, nodeIndex));
                    setOpaque(true);

                    // 设置字体
                    if (nodeIndex == treeChain.h) {
                        setFont(baseFont); // 根节点
                    } else if (treeChain.top[nodeIndex] == nodeIndex) {
                        setFont(chainFont); // 重链头节点
                    } else {
                        setFont(normalFont); // 普通节点
                    }

                    // 添加重儿子标记
                    String text = nodeInfo.displayText;
                    if (treeChain.son[treeChain.fa[nodeIndex]] == nodeIndex) {
                        text += " ★"; // 重儿子标记
                    }
                    setText(text);

                    // 设置工具提示
                    setToolTipText(createTooltip(treeChain, nodeIndex));
                }
            }

            return this;
        }

        private Color getNodeColor(TreeChain treeChain, int nodeIndex) {
            if (nodeIndex == treeChain.h) {
                return new Color(255, 200, 200); // 根节点 - 浅红
            }
            if (treeChain.top[nodeIndex] == nodeIndex) {
                return new Color(200, 200, 255); // 重链头节点 - 浅蓝
            }
            if (treeChain.son[treeChain.fa[nodeIndex]] == nodeIndex) {
                return new Color(220, 255, 220); // 重儿子节点 - 浅绿
            }
            return Color.WHITE; // 普通节点
        }

        private String createTooltip(TreeChain treeChain, int nodeIndex) {
            return String.format("<html><b>节点详细信息</b><br>"
                            + "编号: %d<br>父节点: %d<br>深度: %d<br>子树大小: %d<br>"
                            + "重儿子: %d<br>重链头: %d<br>DFS序号: %d</html>",
                    nodeIndex, treeChain.fa[nodeIndex], treeChain.dep[nodeIndex],
                    treeChain.siz[nodeIndex], treeChain.son[nodeIndex],
                    treeChain.top[nodeIndex], treeChain.dfn[nodeIndex]);
        }
    }

    // 测试方法
    public static void main(String[] args) {
        // 创建一个示例树链结构
        int[] father = {8, 2, 2, 2, 5, 2, 9, 0, 2, 5}; // 示例父节点数组
        int[] values = {6, 1, 3, 8, 1, 7, 9, 10, 4, 3}; // 节点权重

        TreeChain treeChain = new TreeChain(father, values);

        // 在事件调度线程中显示可视化界面
        SwingUtilities.invokeLater(() -> visualize(treeChain));
    }
}
