# 数据结构补充：

## 线性数据结构：线性表、链表、栈、队列

### 单链表：r -> p -> q
	
插入：在p、q中间插入s，已知p
```c	
s.next=p.next;
p.next=s;
```
删除：q
```c
p.next=q.next.next;
free(q);
```

交换：
Node s

```c
	s=r.next;
	r.next=q;
	p.next=q.next;
	q.next=p;
```

### 双向链表：a b c

删除：b
```c
b.pre.next=b.next;
b.next.pre=b.pre;
free(b);
```

插入：a c中插入b,即已知c
```c
b.pre=c.pre;
c.pre.next=b;
b.next=c;
c.pre=b;
```




### 非线性数据结构：树（二叉树）

## 最小生成树

## 哈夫曼树和哈夫曼编码
哈夫曼树：哈夫曼树也叫最优二叉树，即带权路径长度WPL最小。

构造哈夫曼树：
例：有4 个结点 a, b, c, d，权值分别为 7, 5, 2, 4，试构造以此 4 个结点为叶子结点的二叉树。
方法：从集合中找到两个权值最小的节点。组成一个父节点。该父节点的权值为两个叶子结点的和。从集合中移除上述两个节点，并添加新的父节点。
重复上述操作，直到只有一棵树为止。

哈夫曼编码：使用哈夫曼树设计的最短二进制前缀编码

例：如果需传送的电文为 ‘ABACCDA’，即：A, B, C, D ，权重分别为3，1，2，1。然后构造哈夫曼树。之后树的路径上左0右1。到叶子结点的路径即为该字符的编码


## 堆
- 小跟堆
- 大根堆

java中的实现：
TreeSet：
PriorityQueue：使用 PriorityQueue优先队列 实现堆。
	入队：add offer
    出队：poll
    注意：peek方法-只取出队列中头部元素的值，不删除该元素
### 例题：在未排序的数组中找到第 k 个最大的元素。数组元素可能重复
```java
/**
 * https://leetcode-cn.com/problems/kth-largest-element-in-an-array/
 * 
 */
public class FindKthLargest {
    public static void main(String[] args) {

        int[] nums = {3, 2, 3, 1, 2, 4, 5, 5, 6};
        int result = new FindKthLargest().findKthLargest2(nums, 4);
        System.out.println(result);
    }

    /**
     * 使用 treeset 来模拟堆。
     * 但因为set中不允许元素重复，所以使用一个map来辅助计数
     *
     */
    public int findKthLargest(int[] nums, int k) {

        // TreeSet默认升序排序
        TreeSet<Integer> treeSet = new TreeSet<>();
        Map<Integer, Integer> map = new HashMap();
        for (int i : nums) {
            treeSet.add(i);
            if (map.containsKey(i)) {
                int count = map.get(i) + 1;
                map.put(i, count);
            } else {
                map.put(i, 1);
            }
        }
        int count = 0;
        for (int i : treeSet) {
            count += map.get(i);
            // 第k大，也就是 (nums.length - k + 1) 小
            if (count >= (nums.length - k + 1)) {
                return i;
            }
        }
        return -1;
    }


    /**
     * 使用 PriorityQueue优先队列 实现堆
     * 入队：add offer
     * 出队：poll
     * 注意：peek方法-只取出队列中头部元素的值，不删除该元素
     */
    public int findKthLargest2(int[] nums, int k) {
        PriorityQueue<Integer> queue = new PriorityQueue<>();
        for (int i : nums) {
            queue.add(i);
        }

        for (int i = 0; i < nums.length - k; i++){
            queue.poll();
        }
        return -1;
    }
}
```

## 队列Queue
- 实现：使用LinkedList
```java
// 效率高些
Queue<TreeNode> queue = new LinkedList<>();
// 效率差，线程安全
Queue<TreeNode> queue = new LinkedBlockingQueue<>();
```
- 应用
> 1. 二叉树的广度优先搜索（BFS）

## 栈Stack
- 应用
> 1. 二叉树的深度优先搜索（DFS）
> 2. https://leetcode-cn.com/problems/daily-temperatures/


