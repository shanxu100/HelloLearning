# Java基础

## 1、基本数据类型
- byte
8位有符号字节，范围是 -128~127

- char
16位Unicode字符，'\u0000'~'\uFFFF'，即0~65535

- short
16位有符号整数

- int
32位有符号整数，最小值为-2^31，最大值为2^31 -1

- long
64位有符号整数，最小值为-2^63，最大值为2^63 -1

- float
32位单精度浮点数，不能用于计算精确值(例如：货币)

- double
64位双精度浮点数，不能用于计算精确值(例如：货币)

- boolean：
boolean数据类型只有两个可能的值：true和false。将此数据类型用于跟踪真/假条件的简单标志。此数据类型表示一位信息，但其“大小”不是精确定义的内容。


## 2、对象
1. Integer的缓存对象
```Integer n1 = 127;```使用这种方式得到的 **数值在-128~127之间**Integer的缓存对象。

```Integer.valueOf("0")```
Integer类型的对象：多次解析相同的一个字符串时，得到的是Integer类型的对象，得到的对象有**时是同一个对象，有时是不同的对象**，要根据把s字符串解析的整数值的大小进行决定：如果s字符串对应的整数值在 **-128~127**之间，则解析出的是Integer缓存对象；反之，则是一个新的Integer对象。

```IntegerCache类```
实现了Integer的缓存，即创建了数组```static final Integer cache[];```
IntegerCache 默认的取值范围为 -128 到 127。
通过调整JVM参数可以修改此值，如```-XX:AutoBoxCacheMax=1000```

```Integer.parseInt("0")```
返回一个**基本数据类型int** 
例如：
```java
Integer n3 = 200;
Integer n4 = 200;
System.out.println(n3 == n4);//false
System.out.println(n3.equals(n4));//true
```

2. 
```java
int i = 0; 
Integer j = new Integer(0); 
System.out.println(i == j);  // true，Integer与int相比较，会拆箱
System.out.println(j.equals(i)); // true
```

## clone
### 数组对象clone
```java
int[] a = {1, 2, 3, 4, 5};
int[] b = a.clone(); 
```
数组b和a是两个不同的对象，指向不同的内存，改变数组a中元素的值不影响数组b中元素的值。

## 3、super&this
super常用于子类访问父类中的**构造函数、函数、属性**
- ```super()```：访问父类的构造函数，**必须放在子类构造函数的第一行**
- ```super.xxxx()```：访问父类中的函数，像普通方法一样调用，无特殊限制
**注意**：this常用于访问**本类**中的**构造函数、函数、属性**，使用方法与super大致相同。this是一个指向本对象的指针, 而super是一个Java关键字。


## 4、switch-case用法
**支持的数据类型：**
- 基本数据类型：byte, short, char, int
- 包装数据类型：Byte, Short, Character, Integer
- 枚举类型：Enum
- 字符串类型：String（Jdk 7+ 开始支持）

## 5、break
使用break的场景有两种：一、switch语句中。二、循环语句。
- ```break;```默认跳出一层循环（不再多解释）

- 跳出指定循环: 为循环起一个名字，```break 名字;``` 即可跳出指定循环。

```java
first:
for (int i = 0; i < 5; i++) {
    second:
    for (int j = 0; j < 5; j++) {
        if (j == 0) {
            System.out.println(i);
            break first;
        }
    }
    System.out.println("跳出1层for循环到这啦");
    if (i == 0) {
        System.out.println("终结者");
        break;
    }
}
```

## 6、Void类
Void类是一个不可实例化的占位符类，如果方法返回值是Void类型，那么该方法只能返回null类型。
```java
public Void test() { 
  return null;
}
```
**使用场景一：泛型**
因此当你使用泛型时函数并不需要返回结果或某个对象不需要值时候这是可以使用java.lang.Void类型表示。
```java
Future<Void> f = pool.submit(new Callable() {
  @Override
  public Void call() throws Exception {
    ......
    return null;
  }
     
});
```

**使用场景二：反射**
通过反射获取所有返回值为void的方法。
```java
public class Test {
  public void hello() { }
  public static void main(String args[]) {
    for (Method method : Test.class.getMethods()) {
      if (method.getReturnType().equals(Void.TYPE)) {
        System.out.println(method.getName());
      }
    }
  }
}
```
```Void.TYPE```等价于```void.class```

## 7、String

参考：
[《Java提高篇——理解String 及 String.intern() 在实际中的应用》](https://www.cnblogs.com/Qian123/p/5707154.html)

### 7.1 intern()方法

**概述**：Java查找常量池中是否有相同Unicode的字符串常量，如果有，则返回其的引用，如果没有，则在常量池中增加一个Unicode等于str的字符串并返回它的引用

**常量池(constant pool)** 指的是在编译期被确定，并被保存在已编译的.class文件中的一些数据。它包括了关于类、方法、接口等中的常量，也包括字符串常量。
```java
String s = "guan";
String s1 = "guan";
String s2 = new String("guan");
String s3 = s2.intern();
System.out.println(s == s1);
System.out.println(s == s2);
System.out.println(s == s3);
System.out.println(s2 == s3);
```
**解析：**
1. s 与 s1 都是字符串常量，在编译期就确定，所以```s == s1```为```true```
2. s2不是字符串常量，所以```s == s2```为```false```
3. ```intern()```方法返回字符串```"guan"```在常量池的地址，所以```s == s3```为```true```
4. ```intern()```方法并不改变s2，所以所以```s2 == s3```为```false```


new String("LOCK").intern();


## Other
1. 常量与静态变量
```java
public final static String MAN_TYPE = "man"; // （编译期）常量
public static String WOMAN_TYPE = "woman";  // 静态变量
```
注：编译期常量是```static final```修饰的在编译期就能确定其值的变量，会在jvm指令中ConstantValue标记

2. Class和Interface
- interface中的变量默认 ```public static final```
- interface中的方法默认 ```publis abstract``` 

3. 不可变类(immutable)
- 不可变类是指这个类的实例一旦创建完成后，就不能改变其成员变量值，也就是不能改变对象的状态。
- Java 中八个基本类型的包装类和 String 类都属于不可变类，而其他的大多数类都属于可变类。
- 不可变对象是线程安全的。

