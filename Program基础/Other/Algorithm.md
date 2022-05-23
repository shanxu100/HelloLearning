# Algorithm: 算法

## 1、怎么判断一个链表是否有环？

方法一，步数检查：使用p、q两个指针，p总是向前走一步，但q每次都从头开始走。对于每个节点，检查p累计走的步数是否和q一样。如果一样，则说明有环，否则，无环。

方法二，快慢指针：使用p、q两个指针，p每次向前走一步，q每次向前走两步，若在某个时候p == q，则存在环。

## 2、判断两个链表是否有交叉
我们可以知道如果两个链表有公共节点，那么该公共节点之后的所有节点都是两个链表所共有的，所以长度一定也是相等的，如果两个链表的总长度是相等的，那么我们对两个链表进行遍历，则一定同时到达第一个公共节点。但是链表的长度实际上不一定相同，所以我们只需要计算出两个链表的长度之差n，然后让长的那个链表先移动n步，短的链表再开始向后遍历，这样他们一定同时到达第一个公共节点，我们只需要在向后移动的时候比较两个链表的节点是否相等就可以获得第一个公共节点。时间复杂度是O(m+n)

## 3、我们可以将其中一个链表的首尾相连，然后判断另一个链表是否含环。如果含环，则两链表交叉；否则，不交叉。时间复杂度是O(max[m,n])
返回链表倒数第N个节点


## 4、图的表示方法：
邻接表、邻接矩阵

图的遍历：（非递归）
广度优先：利用队列
深度优先：利用栈


## 5、讲一下堆排序的全过程
每个顶点大于等于左右子节点，则为大根堆；每个顶点小于等于左右子节点，则为小根堆。
以大根堆为例：
1、	首先根据已知元素，按照层序遍历的顺序，构建一个完全二叉树
2、	调整为大根堆
3、	移除堆顶元素，获取最大值：堆顶元素和末尾元素交换顺序，然后移除末尾元素
4、	将剩余元素重新调整为大根堆
5、	重复n次，得到一个有序队列


## 6、给你一个数组，其中有一个元素的个数大于数组总个数的一半，求出这个元素

方法一：先对数组排序，然后找到中间的那个数字，一定就是我们要找的元素。如果使用快速排序的话，该算法的平均时间复杂度为O(nlogn)，空间复杂度为O(1)。
方法二：用HashMap 去解决，key存放元素，value存放个数，如果个数超过一般，输出这个元素。该算法时复杂度为O(n),空间复杂度为O(n)。
方法三：这里有一种时间复杂度为O(n)，空间复杂度为O(1)的算法，主要思想如下：采用阵地攻守的思想：第一个数字作为第一个士兵，守阵地；count = 1；遇到相同元素，count++; 遇到不相同元素，即为敌人，同归于尽,count--；当遇到count为0的情况，又以新的i值作为守阵地的士兵，继续下去，到最后还留在阵地上的士兵，就是要找主元素。（ps：如果本题要求判断超过一半的元素是否存在，只要在遍历一次，记录剩余“士兵”的个数是否超过一半即可）。

## 7、给你一个数组，一个目标值sum，在里面寻找两个数使其和为target


## 8、找零钱：
零钱数量有限（或无限制）


## 9、设计一个Buffer（腾讯面试）


 
## 10、单链表反转：
1、非递归：
2个指针： 
node* reverseList(node* H)
{
    if (H == NULL || H->next == NULL) //链表为空或者仅1个数直接返回
        return H;
    node* p = H, *newH = NULL;
    while (p != NULL)                 //一直迭代到链尾
    {
        node* tmp = p->next;          //暂存p下一个地址，防止变化指针指向后找不到后续的数
        p->next = newH;               //p->next指向前一个空间
        newH = p;                     //新链表的头移动到p，扩长一步链表
        p    = tmp;                   //p指向原始链表p指向的下一个空间
    }
    return newH;
}
  

递归：
node* In_reverseList(node* H)
{
    if (H == NULL || H->next == NULL)       //链表为空直接返回，而H->next为空是递归基
        return H;
    node* newHead = In_reverseList(H->next); //一直循环到链尾 
    H->next->next = H;                       //翻转链表的指向
    H->next = NULL;                          //记得赋值NULL，防止链表错乱
    return newHead;                          //新链表头永远指向的是原链表的链尾
}
 


## 11、哈夫曼编码



## 12、最小的K个数、几百万个QQ号 ，找出前100个消费最高的QQ号。

一般：
思路1：n个数排序------选最小的k个数
时间复杂度取决于排序的时间复杂度，一般O(nlogn)；

思路2：遍历数组，选出最小的一个数；如此遍历k次
时间复杂度 O（m*n）
优化：
思路3：建立一个k个数的有序集合。遍历这n个数：如果集合未满，则把元素i直接插入到集合中；如果集合已满，则元素i与集合中最大的数max做比较——如果i>max，则忽略；如果i<max，则删除max，添加i。注意：封装添加和删除操作，添加时进行排序，删除时选择最小数。
如果这个集合使用二叉树（建立大根堆），则添加删除操作的时间复杂度为O(logk)
时间复杂度为O(n*logk)
注：实际可以使用TreeSet或者TreeMap实现logK的增删，来代替 堆 。


思路4:如果基于第m个数来调整，使得比第m个数字小的所有数字都位于数组的左边，比第m个数字大的所有数字都位于数组的右边。这样调整之后，位数数组中左边的m个数字就是最小的m个数字(这k个数字不一定是排序的)。这种思路时间复杂度为O(n)。(不明白，所谓的“第m个数”是什么意思？怎么确定“第m个数”)


## 13、汉诺塔表达式



## 14、扑克牌自动洗牌的算法


## 15、实现一个堆：
不需要自己实现这个堆，利用Java中的TreeSet（或者TreeMap）即可实现相同功能。



## 16、统计一个数的二进制形式中的1的个数
参考Integer. toBinaryString(  int  I )方法

## 17、逆波兰：
逆波兰表达式又叫做后缀表达式
规则： 
技术点：对 栈 的利用

123456如何变为456123 
树：

## 字典树：

BST
二叉搜索树


AVL树
AVL树是最先发明的自平衡二叉查找树：每个结点的左右子树的高度之差的绝对值（平衡因子）最多为1。


红黑树：

一种自平衡的二叉查找树，它可以在 O(log n) 时间内做查找，插入和删除。
当我们在对红黑树进行插入和删除节点时，需要对树进行旋转，使其依然满足红黑规则。

红黑规则：
	每个节点不是红色就是黑色
	根节点是黑色。 
	所有叶子都是黑色
	每个红色节点的两个子节点都是黑色。(从每个叶子到根的所有路径上不能有两个连续的红色节点)
	从任一节点到其每个叶子的所有简单路径都包含相同数目的黑色节点

比较：在删除节点的时候，AVL的旋转次数要比红黑树多，所以红黑树的效率更高。
AVL比红黑树更加高度平衡，所以查找性能方面，AVL略高。


B（B-）树

B+树


 
牛客笔试
1、	使用Scanner.class.hasNext()方法进行循环输入，在输入行数不确定的情况下，window环境下使用ctrl+d结束输入循环，linux环境下使用ctrl+z结束输入循环。
2、	使用Scanner.class.hasNext()方法进行循环输入，尽量不要和nextInt、nextString等方法混用。避免由于输入问题浪费大量时间。

 
## 二分查找

### 1. 升序，允许重复

```java

    /**
     * 当有多个元素值与目标元素相等时，返回<P>最后一个</P>元素的下标
     */
    public static int BinarySearchLast(int[] data, int target) {
        int left = 0;
        int right = data.length - 1;

        while (left < right) {
            int mid = (left + right + 1) / 2;
            if (data[mid] <= target) {
                left = mid;
            } else {
                right = mid - 1;
            }
        }
        //此时，left==right
        if (data[right] == target) {
            return right;
        }
        return -1;
    }


    /**
     * 当有多个元素值与目标元素相等时，返回 <P>第一个</P>元素的下标
     */
    public static int BinarySearchFirst(int[] data, int target) {
        int left = 0;
        int right = data.length - 1;

        while (left < right) {
            int mid = (left + right) / 2;
            if (data[mid] < target) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        //此时，left==right
        if (data[left] == target) {
            return left;
        }
        return -1;
    }

```
 


### 2.升序、不重复    

```java
    /**
     * 当有多个元素值与目标元素相等时，返回 <P>第一个查找到的</P>元素的下标
     */
    public static int BinarySearchHand(int[] data, int target) {
        int left = 0;
        int right = data.length - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            if (data[mid] < target) {
                left = mid + 1;
            } else if (data[mid] > target) {
                right = mid - 1;
            } else {
                return mid;
            }
        }

        return -1;
}
```

 
 
快速排序

```java
    /**
     * 快速排序
     */
    private static void quickSort(int[] a, int l, int r) {
        if (l >= r) {
            return;
        }
        int mid = a[l], left = l, right = r;
        while (left < right) {
            while (left < right && a[right] > mid) {
                right--;
            }
            a[left] = a[right];
            while (left < right && a[left] <= mid) {
                left++;
            }
            a[right] = a[left];
        }
        //此时 left==right
        a[left] = mid;
        quickSort(data, l, left - 1);
        quickSort(data, left + 1, r);
    }

```


单例模式
使用双重检查进一步做了优化，可以避免整个方法被锁，只对需要锁的代码部分加锁，可以提高执行效率。
```java
    class Single {
        private Single() {
        }
        private volatile static Single s = null;
        public static Single getInstance() {
            if (s == null) {
                synchronized (Single.class) {
                    if (s == null) {
                        s = new Single();
                    }
                }
            }
            return s;
        }
    }

```


判断回文数

```java
// 方式一：
    String s1 = "xxxxxx";
    StringBuilder s2 = new StringBuilder(s1).reverse();
    return s1.toString().equals(s2.toString());

// 方式二：
    boolean isHuiwen(char[] chars) {
        for (int i = 0; i < chars.length / 2; i++) {
            if (chars[i] != chars[chars.length - 1 - i]) {
                return false;
            }
        }
        return true;
    }
```


冒泡排序
```java
    public static void maopaoSort(int[] data) {

        for (int i = data.length - 1; i >= 0; i--) {
            for (int j = 0; j < i; j++) {
                if (data[j] > data[j + 1]) {
                    swap(data, j, j + 1);
                }
            }
        }

    }
```






插入排序

```java
    public static void InsertSort(int[] data) {

        int i, j;
        int target;
        for (i = 1; i < data.length; i++) {
            j = i - 1;
            target = data[i];
            while (j >= 0 && data[j] > target) {
                data[j + 1] = data[j];
                j--;
            }
            //
            data[j + 1] = target;
        }

    }
```
 
动态规划、贪心

找零钱：指定面额，不限定数量、限定数量两种情况

sum N：





