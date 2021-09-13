## Annotation 注解
> 参考 :https://www.cnblogs.com/fnlingnzb-learner/p/9723699.html

### 1 元注解：
可以用在注解上的注解，是最基本的注解。
- ```@Retention```：保留期，标明这个注解的的存活时间
    > **取值：**
    ``RetentionPolicy.SOURCE`` :只在源码阶段保留，在编译器进行编译时它将被丢弃忽视。
    ```RetentionPolicy.CLASS``` :只被保留到编译进行的时候，它并不会被加载到 JVM 中。
    ```RetentionPolicy.RUNTIME``` :可以保留到程序运行的时候，它会被加载进入到 JVM 中，所以**在程序运行时可以获取到被修饰的注解**。

- ```@Documented```：能够将注解中的元素包含到 Javadoc 中去

- ```@Target```：标明注解运用的地方，限定被修饰的注解的运用场景
    > **取值:**
    ```ElementType.TYPE``` 可以给一个类型进行注解，比如类、接口、枚举
    ```ElementType.METHOD``` 可以给方法进行注解```ElementType.FIELD``` 可以给属性进行注解
    ```ElementType.PARAMETER``` 可以给一个方法内的参数进行注解
    ```ElementType.ANNOTATION_TYPE``` 可以给一个注解进行注解
    ```ElementType.CONSTRUCTOR``` 可以给构造方法进行注解
    ```ElementType.LOCAL_VARIABLE``` 可以给局部变量进行注解
    ```ElementType.PACKAGE``` 可以给一个包进行注解
 
- ```@Inherited```：果一个超类被 @Inherited 注解过的注解进行注解的话，那么如果它的子类没有被任何注解应用的话，那么这个子类就继承了超类的注解。

- ```@Repeatable```：重复注解，就是在声明创建注解的时候，指定该注解可以被同一个程序元素多次使用，Java 1.8 才加上的

### 2 自定义注解
- 语法```public @interface 注解名 {定义体}```
- 注解通过 ```@interface``` 关键字进行定义。
- 注解的属性：注解只有成员变量，没有方法。
- 在注解中定义属性时它的类型必须是 8 种基本数据类型外加 类、接口、注解及它们的数组。
```java
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface TestAnnotation {

    // 常量：默认是 public static final 类型的
    int LEVEL_WARNING = 1;

    // 属性，不是方法
    int id();

    // 属性，不是方法
    String msg() default "Hi";
}
```

**简写：**
- 如果一个注解内仅仅只有一个名字为 value 的属性时，应用这个注解时可以直接接属性值填写到括号内。
```java
public @interface Check {
    String value();
}

@Check("hi")
int a;

// 两者等价

@Check(value="hi")
int a;
```
- 如果一个注解没有任何属性，那么在应用这个注解的时候，括号都可以省略
```java
// 定义注解
public @interface Perform {}


// 使用注解
@Perform
public void testMethod(){}
```

### 3 获取注解
应用对象：class、field、method 
**方法：**
- ```isAnnotationPresent(annotationType)```：判断当前 class、field或method 是否应用了annotationType类型的注解
- ```getAnnotation(annotationType)```：获取当前 class、field或method 的annotationType类型的注解
- ```getAnnotations()```：获取当前 class、field或method 的所有注解
- ```getDeclaredAnnotations```：返回直接存在于此元素上的所有注释。该方法将**忽略继承的注释**。（如果没有注释直接存在于此元素上，则返回长度为零的一个数组。）

**补充：**
- 获取构造方法上的注解：```Constructor```类的```getAnnotation(annotationType)```
- 获取包上的注解：```Package```类的```getAnnotation(annotationType)```

### 4 示例代码

定义注解 ```TestAnnotation``` 和 ```Check```：

```java
@TestAnnotation(msg="hello")
public class Test {

    @Check(value="hi")
    int a;


    @Perform
    public void testMethod(){}


    @SuppressWarnings("deprecation")
    public void test1(){
        Hero hero = new Hero();
        hero.say();
        hero.speak();
    }


    public static void main(String[] args) {

        boolean hasAnnotation = Test.class.isAnnotationPresent(TestAnnotation.class);

        if ( hasAnnotation ) {
            TestAnnotation testAnnotation = Test.class.getAnnotation(TestAnnotation.class);
            //获取类的注解
            System.out.println("id:"+testAnnotation.id());
            System.out.println("msg:"+testAnnotation.msg());
        }

        try {
            Field a = Test.class.getDeclaredField("a");
            a.setAccessible(true);
            // 获取一个成员变量上的注解
            Check check = a.getAnnotation(Check.class);

            if ( check != null ) {
                System.out.println("check value:"+check.value());
            }

            Method testMethod = Test.class.getDeclaredMethod("testMethod");

            if ( testMethod != null ) {
                // 获取方法中的注解
                Annotation[] ans = testMethod.getAnnotations();
                for( int i = 0;i < ans.length;i++) {
                    System.out.println("method testMethod annotation:"+ans[i].annotationType().getSimpleName());
                }
            }
        } catch (NoSuchFieldException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}

```


