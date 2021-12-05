
# 集合:Map&Collection
## 关系

- Collection
	- List
		- LinkeList、ArrayList、vector
	- Set
		- HashSet
			- LinkedHashSet
		- TreeSet
	- Queue
		- ArrayQueue

- Map
	- HashMap
		- LinkedHashMap
	- TreeMap

------

## Map（接口）

AbstractMap（抽象类）

参考：[《进大厂系列》系列-ConcurrentHashMap & HashTable](https://zhuanlan.zhihu.com/p/97902016)

#### HashMap：原理与实现
- table，**存放node的数组**。默认初始大小为16，负载因子为0.75。最大容量为 1<<30 (即1*2^30，1073741824)。**扩容时大小总是2的幂次方**

- HashMap是基于 **数组+链表+红黑树** 实现的，是一种键值对的数据结构。JDK1.8中，链表长度超过8时会把长度超过8的链表转化成 **红黑树** 。HashMap 是由数组、链表和红黑树组成。
	- **hashmap的散列算法**是 **```i =（len-1） & hash```** 即是hash和len-1的与运算，相当于对length取模运算。其中，hash并不是key的hashcode()方法直接得到，而是将hashcode值原低16位与其高16位进行异或操作，同时保持原高16位却不变。这样可以让整个32位的哈希值都参与计算，减少碰撞。
		> 详细参考博客：https://www.jianshu.com/p/a239ad723c73
	- key和value允许为null
	- 扩容条件：元素个数 >= 数组长度*装载因子
	- 非线程安全：（以JDK1.8为例）两个线程AB往map中插入：两条数据key的hash值相同并且插入的位置为null，因为Java堆内存对于各个线程是共享的，且put操作不是原子操作，两个线程在put过程中可能由于时间片切换其中一个失去执行权，会导致数据被另一个线程的put操作覆盖。

#### TreeMap：
以 **红-黑树** 结构为基础，键值按顺序排列的Map。（按照升序排列，可以指定Comparator，比较不同的对象）
first()和last()操作，分别是返回集合中第一个元素（最小）和最后一个元素（最大），效率是**O(log n)**。

#### LinkedHashMap：
保存了插入顺序的Map。Map中的Entry除了保存当前对象的引用外，还保存了其上一个元素before和下一个元素after的引用
```accessOrder```参数： true：访问顺序；false：插入顺序（默认）

访问顺序：？？？？
每次调用get()方法，就会将更改访问的元素更新到链表头部（LRU算法）

#### HashTable：
线程安全的，key和value都不允许为null

--------
## Collection


## List
### Vector：
- 线程安全 使用数组存储 数组长度不够增长一倍

- ArrayList和Vector的区别：
ArrayList是非线程安全的，效率高；Vector是基于线程安全的，效率低

- 数组和链表的优缺点：
	数组：可以随机访问，速度快；不能动态添加删除；占用内存少（少去next、pre指针，即上下节点的信息）
	链表：只能顺序访问；可以动态添加、删除

### LinkedList：
- 线程不安全 使用链表存储，插入删除比较快，查询比较减慢
- 每一个元素被封装到具有头尾指针的Node中


### ArrayList：线程不安全 使用数组存储 数组长度不够增长50%
- ArrayList的原理：基于 **数组** 的实现。
- 自动扩容：默认第一次插入元素时创建 **大小为10的 Object[] 数组** 。超出限制时会增加到原来的 **1.5** 倍。
- 插入或者删除元素时，如果需要移动数据（比如岁数组中间某个元素进行操作），会使用 **System.arraycopy()** 方法，将旧数据复制到新的数组中。

细节：
1. 构造函数中，如果指定初始大小，则会创建一个该大小的Object[]；如果不指定大小，则会首先创建一个长度为0的Object[]（即 private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA={}）。然后第一次插入数据时，才会变成具有默认大小10的Object[]。
2. ArrayList中元素最大值为Integer.MAX_VALUE-8 。因为有些VM会在数组头部加入一些信息，如果还是设置最大的话，可能会导致OOM
3. 扩容的时候，采用 **int newCapacity = oldCapacity + (oldCapacity >> 1)** ，其中 >>1 等价于 /2，也就是 新数组=旧数组*1.5
4. 删除元素时，数组Object[]本身的长度不跟着减小。因为这样会造成多次复制数组，效率降低。

- 优化：初始化时，最好能给出数组大小的预估值。
- 特点：按数组下标访问元素—get(i)/set(i,e) 的性能很高，这是数组的基本优势。直接在数组末尾加入元素—add(e)的性能也高。
- 缺点：但如果按下标插入、删除元素—add(i,e), remove(i), remove(e)，则要用System.arraycopy()来移动部分受影响的元素，性能就变差了，这是基本劣势。

### 链表交换
- 单向链表
- 双向链表

### 数组和链表比较

---------

## Set
### Set原理：
- Set实现的基础是Map（HashMap）

### HashSet
- HashSet基于HashMap实现，，最大容量为 1<<30；
- add(e)操作时调用hashmap的put(e,PRESENT)方法(PRESENT是一个static final的对象)
- HashSet是非线程安全的。

### LinkedHashSet
- LinkedHashSet 基于 LinkedHashMap 实现，因此具有与 LinkedHashMap 相同的特点，即保证了插入的顺序。

### TreeSet
- TreeSet基于TreeMap实现。
- 插入的元素类型，需要实现 Comparable 接口
	- return -1; //-1表示放在红黑树的左边,即逆序输出
	- return 1;  //1表示放在红黑树的右边，即顺序输出
	- return 0;  //表示元素相同，仅存放第一个元素
- 如果其中一个元素的值改变了，只需要将该元素重新add到treeSet里面，触发treeMap的重新排序。

### containsAll()方法
- set1.containsAll( s2 ) : true-s1包含全部的s2，即s2是s1的子集；反之，false



--------

## 并发
- Map		
	- ConcurrentHashMap
	**使用CAS操作和Synchronized关键字**来保证线程安全。

	- HashTable
	是同步的(而HashMap是不同步的)，线程安全；使用**synchronized修饰方法（如put\get\remove\clear等方法），锁住整个对象**，所以效率比较低
	- HashMap
	非线程安全，高效，支持null

- List	——	CopyOnWriteArrayList
	CopyOnWriteArrayList是一个线程安全、并且在读操作时无锁的ArrayList

- Set		—— 	CopyOnWriteArraySet
	CopyOnWriteArraySet基于CopyOnWriteArrayList实现，其唯一的不同是在add时调用的是CopyOnWriteArrayList的addIfAbsent方法。保证了无重复元素，但在add时每次都要进行数组的遍历，因此性能会略低于上个。

### ConcurrentHashMap与HashTable的区别
见上面描述

### ConcurrentHashMap原理分析（以Java 8为例，Java 7不再考虑）

线程安全：
- put安全：根据key的hashCode来定位到需要将这个value放到数组的哪个位置。如果该位置为null，则使用**CAS方式（调用Unsafe的compareAndSwapObject()方法）**将值写入，如果失败，则自旋保证成功；如果该位置不为null（即发生了Hash碰撞），则**使用synchronized加锁（进入synchronized方法块中）**，然后插入到链表或者红黑树中
- get安全：**table数组中元素node使用volatile修饰**，使得在多线程环境下线程A修改结点的node或者新增节点的时候是对线程B可见的。

散列函数：**```(h ^ (h >>> 16)) & HASH_BITS```**，注意与HashMap不同

--------
## Arrays
- Arrays.asList(T… data)
	- 将数组转换为集合。但如果将基本数据类型的数组作为参数传入，该方法会把整个数组当作一个元素。 由于Arrays.ArrayList参数为可变长泛型，基本类型是无法泛型化的，所以它把int[] array 数组当成了一个泛型对象，所以集合中最终只有一个元素array 。
	-  由于asList产生的集合元素是直接引用作为参数的数组，所以当外部数组改变时，集合会同步变化。
	- 数组转换为集合后，无法增删集合的元素。如果需要对集合进行操作的时候我们可以通过 ```List list = new ArrayList(Arrays.asList(array));``` 来进行使用。
	
- Arrays.sort(Object[] array)
- Arrays.sort(T[] array, Comparator<? super T> comparator)
- Arrays.sort(Object[] array, int fromIndex, int toIndex)
- Arrays.sort(T[] array, int fromIndex, int toIndex, Comparator<? super T> c)

- Arrays.binarySearch(Object[] array, Object key)
	> 二分查找，但要求array必须是有序的

- Arrays.toString(Object[] array)
	> 返回数组元素的字符串形式



## Collections
- Collections.disjoint(Collection<?> c1, Collection<?> c2)
	> 表示两个collection中没有相同的元素
	> disjoint()方法不需要传入类型相同的集合，只要实现collection接口即可。如果 c1 集合和 c2 集合没有相同元素返回true。如果传入参数为 null 会引发空指针异常。
- Collections.reverse(list);
	> 反转集合中元素的顺序
- Collections.unmodifiableMap();


## 补充：
### 1. Arrays和Collections 对于sort的不同实现原理


serialVersionUID的作用

objectOutputStream.writeObject(person); //将person对象序列化，然后写入stream中
(Person)objectInputStream.readObject(); //从stream中读出Object，然后转为Person对象











