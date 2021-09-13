
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

/**
 * 二叉树的多种遍历方法
 *
 * @since 2019-09-21
 */
class BinaryTreeAllOrderTraversal {


    public static void main(String[] args) {
        TreeNode root = new TreeNode(10);
        root.left = new TreeNode(5);
        root.right = new TreeNode(15);
        root.right.left = new TreeNode(6);
        root.right.right = new TreeNode(20);

        BinaryTreeAllOrderTraversal traversal = new BinaryTreeAllOrderTraversal();
        List<Integer> result = traversal.xOrderTraversal(root);
        for (Integer integer : result) {
            System.out.print(integer + "\t");
        }
    }


    /**
     * 调用多种遍历方法的示例
     * （前序、中序、后序）+（遍历、递归）
     *
     * @param root
     * @return
     */
    public List<Integer> xOrderTraversal(TreeNode root) {

        List<Integer> result = new ArrayList<>(256);
//        前序、中序、后序遍历
//        byIterativelyPreorder(root, result);
        byIterativelyPostorder(root, result);
//        byIterativelyInorder(root, result);
        return result;

    }

    //region 递归完成先序遍历、中序遍历、后序遍历

    /**
     * 递归：先序（先根）遍历
     *
     * @param node
     * @param result
     */
    private void byRecursivePreorder(TreeNode node, List<Integer> result) {
        if (node == null) {
            return;
        }
        result.add(node.val);
        byRecursivePreorder(node.left, result);
        byRecursivePreorder(node.right, result);
    }

    /**
     * 递归：中序（中根）遍历
     *
     * @param node
     * @param result
     */
    private void byRecursiveInorder(TreeNode node, List<Integer> result) {
        if (node == null) {
            return;
        }
        byRecursiveInorder(node.left, result);
        result.add(node.val);
        byRecursiveInorder(node.right, result);
    }

    /**
     * 递归：后序（后根）遍历
     *
     * @param node
     * @param result
     */
    private void byRecursivePostorder(TreeNode node, List<Integer> result) {
        if (node == null) {
            return;
        }
        byRecursivePostorder(node.left, result);
        byRecursivePostorder(node.right, result);
        result.add(node.val);
    }


    //endregion

    //region 迭代完成先序遍历、中序遍历、后序遍历

    /**
     * 非递归：先序遍历
     *
     * @param node
     * @param result
     */
    private void byIterativelyPreorder(TreeNode node, List<Integer> result) {
        Stack<TreeNode> stack = new Stack<>();
        stack.push(node);
        while (!stack.empty()) {
            TreeNode treeNode = stack.pop();
            if (treeNode == null) {
                continue;
            }
            result.add(treeNode.val);
            stack.push(treeNode.right);
            stack.push(treeNode.left);
        }

    }

    /**
     * 迭代:中序遍历
     *
     * @param node
     * @param result
     */
    private void byIterativelyInorder(TreeNode node, List<Integer> result) {
        Stack<TreeNode> stack = new Stack<>();
        if (node == null) {
            return;
        }
        pushIntoStack(node, stack);
        while (!stack.empty()) {
            TreeNode tmpNode = stack.pop();
            result.add(tmpNode.val);
            pushIntoStack(tmpNode.right, stack);
        }
    }

    /**
     * 关键点：将节点node以及左子节点都入栈
     *
     * @param node
     * @param stack
     */
    private void pushIntoStack(TreeNode node, Stack<TreeNode> stack) {
        if (node == null) {
            return;
        }
        stack.push(node);
        while (node.left != null) {
            node = node.left;
            stack.push(node);
        }
    }


    /**
     * 后序遍历
     *
     * @param node
     * @param result
     */
    private void byIterativelyPostorder(TreeNode node, List<Integer> result) {
        Stack<TreeNode> stack = new Stack<>();
        Set<TreeNode> set = new HashSet<>();
        stack.push(node);
        while (!stack.empty()) {
            TreeNode treeNode = stack.peek();
            if (treeNode == null) {
                stack.pop();
                continue;
            }
            if (set.contains(treeNode)) {
                TreeNode node1 = stack.pop();
                result.add(node1.val);
            } else {
                stack.push(treeNode.right);
                stack.push(treeNode.left);
                set.add(treeNode);
            }
        }

    }

    //endregion


}
