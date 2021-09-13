# 一、树

## 1.1 Binary Search Tree（称为 二分查找树、二分搜索树、二叉**** ）

> 参考：https://blog.csdn.net/u010606397/article/details/98464575

### 1.1.1 概念、效率
**BST需要满足以下几个条件：**
若它的左子树不为空，左子树上所有节点的值都小于它的根节点
若它的右子树不为空，右子树上所有的节点的值都大于它的根节点
它的左、右子树也都是BST。

**节点重复**
- 如果不允许重复， 直接抛弃即可
- 如果允许重复可以使用计数的方式，即每个节点保存自己的个数

**时间复杂度**
平均时间的时间复杂度为 O(log n)，最差情况为 O(n)。
当出现左右子树不平衡时，查找效率会退化成O(n)。因此，**平衡二叉树（AVL）** 是对BST的一种改进。AVL的核心思想就是：**BST + 左右子树高度差不超过1**

### 1.1.2 常用操作

**遍历**
Inorder/Preorder/Postorder
先序遍历（先根遍历）、中序遍历、后序遍历
**对于树的考察，遍历是极容易考的点**

**插入**

**删除**


----------------


## 2 多叉树


-----------

## 3 例题：

### 3.1 判断给出的二叉树是否是 BST
提示：
- 注意给出的数据是Integer.MAX_VALUE和Integer.MIN_VALUE的情况
- 递归和非递归两种方法
> https://leetcode.com/problems/validate-binary-search-tree/

### 3.2 寻找BST中第k小的数
提示：本质就是中序遍历
> https://leetcode.com/problems/kth-smallest-element-in-a-bst/

> https://www.nowcoder.com/practice/ef068f602dde4d28aab2b210e859150a?tpId=13&&tqId=11215&rp=5&ru=/activity/oj&qru=/ta/coding-interviews/question-ranking


### 3.3 输入一个BST，然后为该BST创建一个迭代器。要求：调用迭代器的next()返回当前最小的值，hasNext()方法返回迭代器中是否还有下一个节点
提示：本质依旧是中序遍历
> https://leetcode.com/problems/binary-search-tree-iterator/submissions/

### 3.4 多叉树的遍历
// 先序、后序遍历
// 以及层序遍历
> https://leetcode.com/problems/n-ary-tree-preorder-traversal/ 
> https://leetcode.com/problems/n-ary-tree-postorder-traversal/
> https://leetcode.com/problems/n-ary-tree-level-order-traversal/

----------------
## 4 核心代码举例

## 4.1 定义相关数据结构
```java
//定义二叉树节点
public class TreeNode {
    public int val;
    public TreeNode left;
    public TreeNode right;

    public TreeNode(int x) {
        val = x;
    }
}

//result存放遍历的节点的值
List<Integer> result = new ArrayList<>();
```

## 4.2 递归 Recursive
```java
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

```

## 4.3 非递归遍历 Iteratively
```java
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
 * 将节点node以及左子节点都入栈
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

```

---------

# FAQ

## 1. 区分“Binary Indexed Tree(二分索引树、树状数组）”