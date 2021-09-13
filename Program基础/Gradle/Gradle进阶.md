# Gradle进阶
> https://www.jianshu.com/p/e7a40a592f15

## dependsOn与finalizedBy
```java
// taskA在taskB后执行
taskA.dependsOn(taskB)

// taskA先执行，taskB后执行
taskA.finalizedBy(taskB)

dependsOn(copy_aar, zip_maven, zip_mapping)

```

## mustRunAfter

> https://blog.csdn.net/yao_94/article/details/82627582

## preBuild——在build前先执行指定的task

```java
task deleteHmsSdkAssetsApk {
    doLast{
        //do something
    }
}
preBuild.dependsOn deleteHmsSdkAssetsApk

```

## project.afterEvaluate——在build之后执行指定task
```java
  project.afterEvaluate {
  
  }

allprojects{
    afterEvaluate{ project,state->
        println "$project 评估成功否：${state.failure==null}"
    }

   beforeEvaluate { project ->
       println "开始评估 $project"
   }
}
```



## 查找task

使用find查找task，如果没有找到，则返回 null
- ```tasks.findByName```
```java
tasks.findByName("tetsTask")
```

- ```tasks.findByPath```
```java
tasks.findByPath(":testProject:testTask")
```

同理，使用get查找task，get方式在找不到该任务就会抛出UnknowTaskException异常
- ```tasks.getByName```
```java
tasks.getByName("tetsTask")

tasks.getByPath(":testProject:testTask")
```

## Transfrom
Transfrom: 利用Transform API，第三方的插件可以在.class文件转为dex文件之前，对一些.class 文件进行处理

```java
project
    .getExtensions()
    .getByType(AppExtension.class)
    .registerTransform(new ZedTransform(project));
```
