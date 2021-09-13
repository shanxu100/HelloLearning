# Log

## 对trace/debug/info级别的日志输出必须使用条件输出形式或者使用占位符的方式

比如以下例子：
```logger.debug(“Processing trade with id: ” + id + ” symbol: ” + symbol);```
如果日志是warn，上述日志不会打印，但是在执行字符串的操作时，如果symbol是对象，就会执行toString()方法，浪费了很多系统资源，但是日志又不需要打印。

正例：（条件）建设采用如下方式
```java
if (logger.isDebugEnabled()) {
    logger.debug("Processing trade with id: " + id + " and symbol: " + symbol);
}
```
正例：（占位符）
```java
logger.debug("Processing trade with id: {} and symbol : {} ", id, symbol);
```






