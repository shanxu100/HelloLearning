# Object Copy 对象拷贝

## 1、基础

### 1.1 定义
- 浅拷贝
重新生成一个新的对象：在该对象中，如果属性是基本数据类型，则拷贝值；如果属性是引用数据类型，则拷贝该属性的引用（即**堆地址**）。浅拷贝前后，引用数据类型的属性指向同一对象。

- 深拷贝
重新生成一个新的对象：在该对象中，如果属性是基本数据类型，则拷贝值；对引用数据类型，创建一个新的对象，并复制其内容。

### 1.2 变量类型分为两类:
- 基本数据类型：```number，string，boolean，null，undefined，symbol```
- 引用数据类型：统称为Object类型，细分的话，有：```Object，Array，Date，Function```等。

注：Java 将内存空间分为堆和栈。基本类型直接在栈中存储数值，而引用类型是将引用（**堆地址**）放在栈中，实际存储的值是放在堆中，通过栈中的引用指向堆中存放的数据。

JVM栈

| 变量名 | 值 |
| ---  | ---|
|   a   |  1 |
|   b   |  2 |

      JVM栈                 ----->     Java堆
| objA  |  堆地址  |  ----->         |  xxxxx  |
| objB  |  堆地址  |  ----->         |  xxxxx  |



## 2、浅拷贝
实现  ```Cloneable``` 接口：```implements Cloneable```
然后直接调用父类的```clone()```方法：```super.clone();```
```java
public class Student implements Cloneable { 
 
   private String name; 
 
   public Student(String s) { 
      name = s; 
   } 
 
   /** 
    *  重写clone()方法 
    * @return 
    */ 
   public Object clone() { 
      //浅拷贝 
      try { 
         // 直接调用父类的clone()方法
         return super.clone(); 
      } catch (CloneNotSupportedException e) { 
         return null; 
      } 
   } 
}

```


## 3、深拷贝
```java
public class Student implements Cloneable { 

   private String name; 
 
   public Student(String s) { 
  
   } 

   /** 
    * 重写clone()方法 
    * 
    * @return 
    */ 
   public Object clone() { 
      // 深拷贝，创建拷贝类的一个新对象，这样就和原始对象相互独立
      Student s = new Student(name); 
      return s; 
   } 
}

```

## 4、补充

### 4.1 序列化
序列化属于深拷贝

### 4.2 数组的拷贝
数组除了默认实现了```clone()```方法之外，还提供了```Arrays.copyOf()```方法用于拷贝，这两者都是浅拷贝。

### 4.3 集合的拷贝







