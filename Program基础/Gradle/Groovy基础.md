## groovy基础：

[toc]


### 1、Groovy的API文档：
  > http://www.groovy-lang.org/api.html

  Groovy语言 基于Java。groovy脚本编译成Java字节码，运行在JVM上。
其核心思想：“一切皆是对象”。
### 2、 定义变量
  - 强类型定义方式 int a=1
  - 弱类型定义方式 def a=1 （def关键字虽然不是必须的，但为了清晰，建议不要省略def关键字）
  - ***在groovy中，int、boolean这些基本数据类型皆是其包装类型，即Integer、Boolean？？？***
  - 字符串：单引号和双引号包裹的均是字符串。
      - 其中，单引号没有计算能力，就是一个普通的字符串
      - 双引号可以进行表达式计算。例如：

      ```Java
      //运行代码——字符串
      String a = 'zhansan'
      println("I am ${a}");
      println('I am ${a}');

      //-----------------
      //控制台输出
      I am zhansan
      I am ${a}
      ```


### 3、 函数
#### 3.1 参数
  - 可以无需指定参数的类型
#### 3.2 调用
- 调用的函数在最后可以不加括号（**经常容易将函数和属性混淆，慎重**）
 ```java
  //运行的代码——函数
  myFunction(123, "string")
  //调用的函数在最后可以不加括号，参数之间用 , 分隔
  myFunction 123,"string"

  def myFunction(param1, String param2) {
      println("this is a function . Param is ${param1}、${param2}")
  }

    //------------
    //控制台输出
    this is a function . Param is 123、string
```

#### 3.3 返回值
- 无返回值的函数 def function(){    }
- 有返回值的函数 String function(){      }
- 返回值：
  - 根据return确定
  - 最后一行代码的执行结果作为返回值

```java
//定义有返回值的方法，并且return可以省略
//如果省略的话，那么最后一行代码的执行结果作为返回值
String myFunctionCanReturn() {
    int a = 1;
    String s = "this is myFunctionCanReturn()"
}
println(myFunctionCanReturn())

//------------

控制台输出：
this is myFunctionCanReturn()
```




### 4、容器类 Map、List、Range

#### 4.1 Map
- map中key **默认且必须** 是字符串
- **建议添加可以的时候最好用 引号 包裹起来，否则很容易引起混淆**

  ```java
  //定义
  def map1 = [:]
  def map2 = [myKey: 'myValue', 'myKey2': "myValue2"]
  //使用[key:value]定义map的时候，key默认都是字符串，并且引号可以省略
  //在上面例子中，很难区分 变量myKey和字符串‘myKey’。因此容易产生混淆

  //put
  map1.myKey3 = "myValue3"
  map1.put("myKey4", "myValue4")
 
  //get
  map1.get('myKey3')
  println('map1.get(\'myKey3\') = ' + map1.get('myKey3'))

  map1['myKey3']
  println('map1[\'myKey3\'] = ' + map1['myKey3'])

  map1.mykey3
  println('map1.myKey3 = ' + map1.myKey3)

  //-------
  //控制台输出
  map1.get('myKey3') = myValue3
  map1['myKey3'] = myValue3
  map1.myKey3 = myValue3

  //----------
  //TODO 遍历
  ```

#### 4.2 List
略......


### 5、 类和接口
- 定义：基本和Java类似
- 同样支持class和interface，也支持 extends和implements
- 如果不声明public/private等访问权限的话，Groovy 中 **类及其变量** 默认都是 **public** 的
```Java
  //类和接口
  interface IMyInterface {
      void classFunction();
  }

  class MyClass implements IMyInterface {
      int a
      //构造方法
      MyClass() { }

      @Override
      void classFunction() {
          println("this is a classFunction")
      }

      @Override
      String toString() {
          return "this is a instance of MyClass. a=${a}"
      }
  }
```
#### 5.1 实例化：有参无参均可。因此，可以省略定义构造方法的
  ```java
  //实例化
  //实例化：有参无参均可。因此，可以省略定义构造方法的
  MyClass myClass1 = new MyClass()
  MyClass myClass2 = new MyClass(a: 1)
  ```
#### 5.2 闭包：
  - 就是一段匿名代码块，可以接受参数和返回值。可以被当做变量，作为参数传递给方法.
  - 闭包可以类比为C++中的 “函数指针”。定义和调用过程如下所示：
  ```java
  def clos = {
        param1, param2 -> //小箭头作为分隔，前面是参数，后面是具体的执行
          println("  I am a closure ${param1},${param2}")
    }
  clos.call(123,456)
  ```

**注意：**
  - 由于脚本是顺序执行的，因此要先定义后执行（**函数（方法）与Closure的区别是什么？**）
  - 如果闭包没定义参数的话，则隐含有一个参数，这个参数名字叫it，和this 的作用类似
  - **Closure中还有好多东西。由于个人理解不是很深，这里就不赘述了**
