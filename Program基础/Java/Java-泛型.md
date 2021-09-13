
# Java-泛型

参考：  
[《搞懂Java高级特性--泛型》](https://juejin.cn/post/6924865021516201998)  
[《Java 泛型的见解》](https://juejin.cn/post/6924666790870155272)  

**为什么要使用泛型？**
泛型的本质是参数化类型，也就是说所操作的数据类型被指定为一个参数。
1. 代码复用：通常的代码复用是提取一个公共参数的函数，函数中的参数传的是各种不同的值。泛型也是类似，只不过泛型可以用于定义 class、interface、method 等等，泛型传递的是不同的 type。
2. 减少强转：如果没有泛型，很多时候我们都需要类型强转。但是，使用了泛型以后，因为编译时有 type check，所以自然可以不用写类型强转的代码。


## 1、基本语法

### 1.1 泛型方法

### 1.2 泛型类

## 2、类型通配符```<? extends T>```和```<? super T>```的区别

- <? extends T>表示包括T在内的任何T的子类，指 “上界通配符（Upper Bounds Wildcards）”
	上界<? extends T>不能往里存 (null除外) ，只能往外取

- <? super T> 表示包括T在内的任何T的父类，指 “下界通配符（Lower Bounds Wildcards）”
	下界<? super T>不影响往里存，但往外取只能放在Object对象里

- 无界通配符(unBounded Wildcards)
	```？``` 即为无界通配符
	```List<?>``` 和 ```List<Object>``` 却不相同，```List<?>``` 同样只能添加 ```null``` 作为元素

```java
class Fruit {}
class Apple extends Fruit {}
class Jonathan extends Apple {}
class Orange extends Fruit {}
public class Test {
    List<? extends Fruit> flist = new ArrayList<Apple>(); 
}

```
- flist不能添加任何对象，因为不知道list中存放的是Fruit、Apple还是Jonathan
- flist只能读到Fruit对象，但不知道该对象具体是Fruit、Apple还是Jonathan
- ```List<Fruit> list; List<Apple> list; List<Orange> list```等没有任何继承关系

- 如果是继承基类而来的泛型，就用 ```getGenericSuperclass()``` , 转型为 ParameterizedType 来获得实际类型
- 如果是实现接口而来的泛型，就用 ```getGenericInterfaces()``` , 针对其中的元素转型为 ParameterizedType 来获得实际类型
> https://blog.csdn.net/hj7jay/article/details/54889717








