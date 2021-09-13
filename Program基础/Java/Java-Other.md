## URI
**格式:**
> scheme:[//[user:password@]host[:port]][/]path[?query][#fragment]
```java
URL url = new URL("http://www.runoob.com/index.html?language=cn#j2se");
         System.out.println("URL 为：" + url.toString());
         System.out.println("协议为：" + url.getProtocol());
         System.out.println("验证信息：" + url.getAuthority());
         System.out.println("文件名及请求参数：" + url.getFile());
         System.out.println("主机名：" + url.getHost());
         System.out.println("路径：" + url.getPath());
         System.out.println("端口：" + url.getPort());
         System.out.println("默认端口：" + url.getDefaultPort());
         System.out.println("请求参数：" + url.getQuery());
         System.out.println("定位位置：" + url.getRef());
```

## Buffer
- capacity是它所包含的元素的数量。缓冲区的容量不能为负并且不能更改。
- limit是第一个不应该读取或写入的元素的索引。缓冲区的限制不能为负，并且不能大于其容量。
- position是下一个要读取或写入的元素的索引。缓冲区的位置不能为负，并且不能大于其限制。

```clear()```:
```java
public final Buffer clear() { 
         position = 0; //设置当前下标为0
         limit = capacity; //设置写越界位置与和Buffer容量相同
         mark = -1; //取消标记
         return this; 
}
```
```flip()```:函数的作用是将写模式转变为读模式
```java
public final Buffer flip() { 
        limit = position; 
        position = 0; 
        mark = -1; 
        return this; 
 }
 ````
 ```
  总结：创建一个buffer------>存储数据------->调用flip()
                              /\                  |
                              |         循环      |
                              |                  \/
                           调用clear()<------读取数据
```

