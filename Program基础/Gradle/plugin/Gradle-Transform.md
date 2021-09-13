# Gradle-Transform

前言：本人正在学习Gradle插件开发。这里拿“字节码替换”的小demo练手
参考：
[《Gradle 学习之 Android 插件的 Transform API》](https://juejin.cn/post/6844903891138674696)
[《一文学会Android Gradle Transform基础使用》](https://juejin.cn/post/6914485867029463054)
[《Javassist用法》](https://ljd1996.github.io/2020/04/23/Javassist用法/)

## 1、概述：
```Transform``` 是 Android Gradle 提供的操作字节码的一种方式。或者说Gradle为我们提供了一个修改 Class 字节码的契机。它在 class 编译成 dex 之前通过一系列 ```Transform``` 处理来实现代码注入。
Javassist 可以方便地修改 .class 文件。对于实现更加复杂的字节码修改功能，需要用到更强大的 ASM 字节码处理工具。

## 2、```Transform``` 的工作原理
```
class   |
jar     |----> TransformA ----> TransformB ----> TransformN
resource|
```
1. ```Transform``` 就是一个链式结构。每个```Transform```对应一个Task，Gradle会将这些Task串联起来。
2. 第一个```Transform```可以处理javac的编译结果、第三方依赖jar包和resource资源。每个```Transform```处理完成后都可以传递给下一个```Transform```
3. 自定义的```Transform```会被插到链路的首节点
4. ```Transform``` 的数据输入 key 通过 Scope 和 ContentType 两个维度进行过滤，筛选出需要处理的数据。
    - ContentType 就是数据类型，在开发中一般只能使用 CLASSES 和 RESOURCES 两种类型。
    - 其他的一些类型如 DEX 是留给 Android 编译器的，我们无法使用。
    - 至于 Scope ，开发可用的相对较多（详细见 TransformManager 类），处理 class 字节码时一般使用 SCOPE_FULL_PROJECT 。

## 3、```Transform``` 的注意点
- 自定义 ```Transform``` 无法处理 Dex ；
- 自定义 ```Transform``` 无法使用自定义 ```Transform``` ；
- 可以使用 isIncremental 来支持增量编译以及并发处理来加快 ```Transform``` 编译速度；
- ```Transform``` 只能在全局注册，并将其应用于所有变体（variant）。

## 4、例子



