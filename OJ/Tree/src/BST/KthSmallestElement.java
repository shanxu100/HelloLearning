
import org.personal.leetcode.tree.TreeNode;

/**
 * https://leetcode.com/problems/kth-smallest-element-in-a-bst/
 * 第k小的node：本质就是中序遍历
 * 递归？非递归？
 *
 * @since 2019-09-22
 */
class KthSmallestElement {

    public static void main(String[] args) {
        TreeNode root = new TreeNode(3);
        root.left = new TreeNode(1);
        root.right = new TreeNode(4);
        root.left.right = new TreeNode(2);
        int value = new KthSmallestElement().kthSmallest(root, 1);
        System.out.println(value);
    }


    public int kthSmallest(TreeNode root, int k) {

        this.k = k;
        byRecursiveInorder(root);
        return result;

    }

    int count = 0;
    int result = -1;
    boolean stop = false;
    int k;

    public void byRecursiveInorder(TreeNode node) {
        if (node == null) {
            return;
        }
        if (stop) {
            return;
        }
        byRecursiveInorder(node.left);
        count++;
        result = count == k ? node.val : result;
        byRecursiveInorder(node.right);
    }


}
