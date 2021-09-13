
import java.util.Stack;

import org.personal.leetcode.tree.TreeNode;

/**
 * 功能描述
 * https://leetcode.com/problems/validate-binary-search-tree/
 *
 * @since 2019-09-22
 */
class ValidateBinarySearchTree {


    public static void main(String[] args) {
        TreeNode root = new TreeNode(10);
        root.left = new TreeNode(5);
        root.right = new TreeNode(15);
        root.right.left = new TreeNode(6);
        root.right.right = new TreeNode(20);

//        TreeNode root = new TreeNode(2);
//        root.left = new TreeNode(1);
//        root.right = new TreeNode(3);

//        TreeNode root = new TreeNode(Integer.MIN_VALUE);
//        root.right = new TreeNode(Integer.MAX_VALUE);

//        TreeNode root = new TreeNode(2147483647);
//        TreeNode root = new TreeNode(0);


        boolean result = new ValidateBinarySearchTree().isValidBST(root);
        System.out.println(result);
    }


    public boolean isValidBST(TreeNode root) {


        return byIteratively(root);
//        return byRecursive(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    /**
     * 递归遍历的方法，判断是否是 BST
     *
     * @param node
     * @return
     */
    public boolean byRecursive(TreeNode node, long lower, long upper) {

        if (node == null) {
            return true;
        }

        if (node.val <= lower || node.val >= upper) {
            return false;
        }

        boolean res1 = true;
        if (node.left != null) {
            res1 = node.left.val < node.val;
        }
        boolean res2 = true;
        if (node.right != null) {
            res2 = node.right.val > node.val;
        }
        if (res1 && res2) {
            return byRecursive(node.left, lower, node.val) && byRecursive(node.right, node.val, upper);
        } else {
            return false;
        }
    }


    /**
     * 递归遍历的方法，判断是否是 BST
     *
     * @param root
     * @return
     */
    public boolean byIteratively(TreeNode root) {
        Stack<TreeNode> stack = new Stack<>();

        return false;
    }

}
