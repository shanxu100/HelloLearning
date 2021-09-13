
## 代理模式(Proxy)

### 1、静态代理
代理对象和委托对象（被代理对象）实现同一个接口，
代理对象维持一个委托对象的引用，同时在实现方法过程中，可以在委托对象的方法的前后加入某些新的功能。
**弊端：**
每一个代理类都必须实现一遍委托的接口，代理类每一个接口对象对应一个委托对象。  
因此，静态代理对委托接口修改和对委托对象的数量比较敏感。  

## 2、动态代理：通过反射来动态的创建代理对象
> https://segmentfault.com/a/1190000016728200
> https://juejin.im/post/5c1ca8df6fb9a049b347f55c
### 2.1 JDK动态代理
1. 定义接口并实现
```java
public interface IEat {
    void eat();
}

public interface ICook {
    int dealWithFood();
    String cook();
}
/**
* 被代理的类，实现接口
*/
public class CookManager implements ICook, IEat {

    @Override
    public int dealWithFood() {
        System.out.println("food had been dealed with");
        return 1;
    }

    @Override
    public String cook() {
        System.out.println("cook food");
        return "cook food";
    }

    @Override
    public void eat() {
        System.out.println("eat food");
    }
}

```

2. 实现 InvocationHandler 接口，确定代理类的逻辑
```java
class DynamicProxyHandler implements InvocationHandler {

    private Object target;

    public DynamicProxyHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        System.out.println("proxy-----before");
        // 调用target原生的方法，并获得返回值
        Object result = method.invoke(target, args);
        System.out.println("proxy-----after");

        // 修改返回值，搞事情...
        if (result instanceof String) {
            result = "proxy:" + result;
        } else if (result instanceof Integer) {
            result = 2 + ((Integer) result).intValue();
        }
        // 调用method方法的返回值
        // 这里是proxy用来处理target的逻辑，因此可以对target的逻辑做一些手脚，体现proxy的价值
        return result;
    }
}
```

3. 创建代理类并调用
```java
// 被代理的对象 target
CookManager target = new CookManager();

// proxy用来处理target的逻辑
DynamicProxyHandler handler = new DynamicProxyHandler(target);

// 生成代理对象proxy
Object proxy = Proxy.newProxyInstance(CookManager.class.getClassLoader(), CookManager.class.getInterfaces(), handler);

// 调用代理proxy的方法，并搞事情
String s1 = ((ICook) proxy).cook();
int i1 = ((ICook) proxy).dealWithFood();
((IEat) proxy).eat();

System.out.println("s1: " + s1 + " i1: " + i1);
```


**关键方法：**
- ```Proxy.newProxyInstance();```
    生成一个实现若干接口的代理类。第二个参数就是需要被代理的实例所实现的接口列表。这些代理类的实现逻辑交给```InvocationHandler```处理。
- ```InvocationHandler```接口
    当代理类执行方法时，会进入该接口的```invoke```方法中。开发者可以在```invoke```方法中实现代理类的逻辑

**实现难点：**
在```Proxy.newProxyInstance```中，如何根据```Class<?>[] interfaces```生成代理类？  
使用```ProxyGenerator.generateProxyClass( proxyName, interfaces);```生成实现上述接口的代理类的二进制字节流。
可参考：
> https://juejin.im/post/5c1ca8df6fb9a049b347f55c

**限制：**
使用JDK创建代理有一个限制,即它只能为接口创建代理实例。即，如果想为```A.java```创建代理类，那么必须至少存在一个接口类 ```IA.java```。

### 2.2 CGLIB动态代理


