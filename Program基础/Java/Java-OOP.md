# Java OOP面向对象

## 封装


## 继承

### override方法
- 方法的可见性：子类 >= 父类
- 抛出异常：子类 <= 父类

例如：
```java

static class Parent {
    protected void a() throws RuntimeException {
    }
}

static class Child extends Parent {
    @Override
    public void a() throws NullPointerException{
    }
}

public static void main(String[] args){
  Parent child = new Child();
  child.a();
}

```
说明：对于方法a(),可以为public或protected；抛出的异常只能是RuntimeException及其子类。


## 多态

### 重写 override


### 重载 overload
- 方法名称相同，方法参数不同
**以下两个方法不构成重载**
```java
public void myFunction(){
}

public int myFunction(int a){
    return 1;
}
```

### 隐藏 hide
子类**隐藏**父类的一个域、静态方法或成员类型

### 遮蔽 shadow
一个变量、方法或类型可以**遮蔽**在具有相同名字的所有变量、方法或类型，只要他们在同一个闭合的文本范围内
比如局部变量遮蔽全局变量

### 遮掩 obscure
一个变量可以遮掩具有相同名字的一个类型，只要它们都在同一个范围内





