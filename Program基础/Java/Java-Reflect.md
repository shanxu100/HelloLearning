# Java-Reflect反射
- [《Java 反射机制浅析》](https://mp.weixin.qq.com/s/eg5Gx7MEKI0F8mQ45c9kPg)

在运行时判断任意一个对象所属的类
在运行时构造任意一个类的对象
在运行时判断任意一个类所具有的成员变量和方法
在运行时调用任意一个对象的方法

## 1、获取Class对象
获取class对象的三种方式
1. 对象.getClass()	——	缺点：对象都有了还要反射干什么
2. 类.class；	——	缺点：需要导入类的包，依赖太强，不导包就抛编译错误
3. Class.forName("完整路径：包名.类名")	——	最常用

- 比较
**优点**：可以动态的创建对象和编译，最大限度发挥了java的灵活性。
**缺点**：对性能有影响。
```java
/**
 * 获取class的多种方式
 */
Person person = new Person();
Class clazz1 = person.getClass();
Class clazz2 = Person.class;
Class clazz3 = Class.forName("builder.Person");

// 比较的两种方法：clazz1==clazz2 或者 clazz1.equals(clazz2)
// 相同的classloader，那么class对象就会相等；如果classloader不同，那么class对象就不相等
System.out.println("1==2?\t" + clazz1.equals(clazz2));
System.out.println("1==3?\t" + clazz1.equals(clazz3));
System.out.println("2==3?\t" + clazz2.equals(clazz3));
```


## 2、获取属性和方法
在获得类的方法、属性、构造函数时，会有 getXXX() 和 getDeclaredXXX() 两种对应的方法。
**区别:**
- getXXX()返回的是访问权限为public的方法和属性，包括从父类中继承的；
- getDeclaredXXX()返回的是本类中所有访问权限的方法和属性，不包括从父类中继承的。

```java
// 父类
class ParentObj {
    public String pPublicStr = "ParentObj - public string";
    protected String pProtectedStr = "ParentObj - protected string";
    private String pPrivateStr = "ParentObj - private string";
    public int pPublicMethod() {
        System.out.println("pPublicMethod---");
        return 0;
    }
    protected int pProtectedMethod() {
        System.out.println("pProtectedMethod---");
        return 0;
    }
    private int pPrivateMethod() {
        System.out.println("pPrivateMethod---");
        return 0;
    }
}

// 子类
class ChildObj extends ParentObj {
    public String cPublicStr = "ChildObj - public string";
    protected String cProtectedStr = "ChildObj - protected string";
    private String cPrivateStr = "ChildObj - private string";
    public int cPublicMethod() {
        System.out.println("cPublicMethod---");
        return 0;
    }
    protected int cProtectedMethod() {
        System.out.println("cProtectedMethod---");
        return 0;
    }
    private int cPrivateMethod() {
        System.out.println("pPrivateMethod---");
        return 0;
    }
}
```
```java
// 测试类
class Main {
    public static void main(String[] args) throws Exception {
        Class clz = Class.forName("reflect.ChildObj");
        Object cObj = clz.newInstance();
        Field[] fields = clz.getFields();
        Field[] declaredFields = clz.getDeclaredFields();
        Method[] methods = clz.getMethods();
        Method[] declaredMethod = clz.getDeclaredMethods();
        print(fields, "clz.getFields()");
        print(declaredFields, "clz.getDeclaredFields()");
        print(methods, "clz.getMethods()");
        print(declaredMethod, "clz.getDeclaredMethods()");
    }
    private static void print(Object[] objects, String tag) {
        System.out.println("---" + tag + "---");
        for (Object obj : objects) {
            System.out.println(obj.toString());
        }
        System.out.println();
    }
}

// 结果
---clz.getFields()---
public java.lang.String reflect.ChildObj.cPublicStr
public java.lang.String reflect.ParentObj.pPublicStr

---clz.getDeclaredFields()---
public java.lang.String reflect.ChildObj.cPublicStr
protected java.lang.String reflect.ChildObj.cProtectedStr
private java.lang.String reflect.ChildObj.cPrivateStr

---clz.getMethods()---
public int reflect.ChildObj.cPublicMethod()
public int reflect.ParentObj.pPublicMethod()
public final void java.lang.Object.wait() throws java.lang.InterruptedException
public final void java.lang.Object.wait(long,int) throws java.lang.InterruptedException
public final native void java.lang.Object.wait(long) throws java.lang.InterruptedException
public boolean java.lang.Object.equals(java.lang.Object)
public java.lang.String java.lang.Object.toString()
public native int java.lang.Object.hashCode()
public final native java.lang.Class java.lang.Object.getClass()
public final native void java.lang.Object.notify()
public final native void java.lang.Object.notifyAll()

---clz.getDeclaredMethods()---
private int reflect.ChildObj.cPrivateMethod()
protected int reflect.ChildObj.cProtectedMethod()
public int reflect.ChildObj.cPublicMethod()
```


## 获取接口interface列表

```java
// 定义一个类
public class OTest implements Itest1,Itest2{
	// 略...
}

// 测试代码
Class clazz = Class.forName("com.example.designmodelib.proxy.TestReflex$OTest");
Class[] interfaceArray = clazz.getInterfaces();
for (Class itf: interfaceArray){
    System.out.println(itf.getName());
}

// 输出
com.example.designmodelib.proxy.TestReflex$Itest1
com.example.designmodelib.proxy.TestReflex$Itest2
```





## 3、访问属性Field与调用方法Method
### 3.1 例子
```java
//=================================================================
//例子
//=================================================================
Class clazz = Class.forName("proxy.Person");

/**
	* 在运行时判断任意一个对象所属的类
	* 作用与 "obj instanceOf Person" 相同。
	**/
clazz.isInstance(obj);

 /**
	* 通过Class对象可以获取某个类中的：构造方法、成员变量、成员方法；并访问成员；
	*
	* 1.获取构造方法：
	*      1).批量的方法：
	*          public Constructor[] getConstructors()：所有"公有的"构造方法public Constructor[] getDeclaredConstructors()：获取所有的构造方法(包括私有、受保护、默认、公有)
	*      2).获取单个的方法，并调用：
	*          public Constructor getConstructor(Class... parameterTypes):获取单个的"公有的"构造方法：
	*          public Constructor getDeclaredConstructor(Class... parameterTypes):获取"某个构造方法"可以是私有的，或受保护、默认、公有；
	*
	*      调用构造方法：
	*          Constructor-->newInstance(Object... initargs)
	**/
 Constructor constructor1 = clazz.getConstructor(String.class, String.class);
 Object object = constructor1.newInstance("zhangsan", "man");
 if (object instanceof Person) {
		 System.out.println("succeed in creating Person");
		 System.out.println(object.toString());
 }
 /**
	* 获取成员变量并调用：
	*
	* 1.批量的
	*      1).Field[] getFields():获取所有的”公有字段”
	*      2).Field[] getDeclaredFields():获取所有字段，包括：私有、受保护、默认、公有；
	* 2.获取单个的：
	*      1).public Field getField(String fieldName):获取某个”公有的”字段；
	*      2).public Field getDeclaredField(String fieldName):获取某个字段(可以是私有的)
	*
	*   设置字段的值：
	*      Field –> public void set(Object obj,Object value):
	*                  参数说明：
	*                  1.obj:要设置的字段所在的对象；
	*                  2.value:要为字段设置的值；
	*
	**/
 Field field = clazz.getDeclaredField("name");
 field.setAccessible(true);
 field.set(object, "lisi");
 System.out.println("修改字段后：\n" + object.toString());
 Field field1 = clazz.getDeclaredField("pubName");
 field1.set(object, "pub lisi");
 System.out.println(object.toString());
 /**
	* 获取成员方法并调用：
	*
	* 1.批量的：
	*      public Method[] getMethods():获取所有”公有方法”；（包含了父类的方法也包含Object类）
	*      public Method[] getDeclaredMethods():获取所有的成员方法，包括私有的(不包括继承的)
	* 2.获取单个的：
	*      public Method getMethod(String name,Class<?>… parameterTypes):
	*                  参数：
	*                      name : 方法名；
	*                      Class … : 形参的Class类型对象
	*      public Method getDeclaredMethod(String name,Class<?>… parameterTypes)
	*
	*   调用方法：
	*      Method –> public Object invoke(Object obj,Object… args):
	*                  参数说明：
	*                  obj : 要调用方法的对象；
	*                  args:调用方式时所传递的实参；
	):
	**/
 Method method = clazz.getDeclaredMethod("doSomethingPrivately", String.class);
 method.setAccessible(true);
 method.invoke(object, "这是参数");
 /**
	* 获取静态方法并调用：
	*      1. 获取静态方法与获取成员方法的方式相同
	*      2. 调用静态方法。因为因为静态方法调用方式是 类名.方法名，所以不需要类的实例
	*/
 Method staticMethod = clazz.getDeclaredMethod("doSomethingStatic", String.class);
 staticMethod.setAccessible(true);
 staticMethod.invoke(null, "这是参数");

```
### 3.2 Method
#### 3.2.1 获取返回结果
**调用invoke()得到一个Object类型的返回值，即为原方法的返回值。**
```java
Class clz = Class.forName("reflect.ChildObj");
Object cObj = clz.newInstance();
Method method = clz.getDeclaredMethod("cPrivateMethod");
method.setAccessible(true);
Object result = method.invoke(cObj);
if (result instanceof Integer) {
    System.out.println("invoke Integer result :" + result);
} else {
    System.out.println("invoke result :" + result);
}

// 结果
pPrivateMethod---
invoke Integer result :0
```
#### 3.2.2 获取返回值的类型

```getReturnType()```

```java
Type genericRetType = method.getGenericReturnType()
// 判断是否是参数化类型
if (!(genericRetType instanceof ParameterizedType)) {
    // throw makeException("Return type must be parameterized, eg. Response<Foo>.");
}
```



**例子：**
```java

// 判断方法的返回值是否为void
void.class.equals(method.getReturnType());
// 或
method.getReturnType().equals(Void.TYPE);
```


### 3.3 总结：
1. 凡是有 Declared 字样的表示获取全部，没有Declared的表示获取public的
2. 对于private的属性或者方法，需要设置setAccessible(true)后才能调动
3. 对于静态方法，调用不需要实例。因此invoke方法的第一个参数设置为null就可以



## 4、Method

```java
// 取得此方法声明类Class对象（方法在继承层次中哪个类声明的就返回那个类）
method.getDeclaringClass() 

// 注意这个取得的结果是“Method”，相当于上边的获取Method.Class类，不要和getDeclaringClass()混淆
method.getClass()
```

## 5、Modifier

```java
// field类型
Class<?> fieldType = field.getType();

// 是否是static字段
Modifier.isStatic(fieldType);
// 是否是final字段
Modifier.isFinal(field.getModifiers());
// 是否是基本数据类型
fieldType.isPrimitive();
// 是否是数组
fieldType.isArray();

```





