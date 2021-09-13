## URI
- URI包含URL和URN，**URL是URI的子集**
- URI:统一资源标识符 用于唯一标识某个资源
- URL:统一资源定位符 强调定位能力



**格式:**
``` scheme:[//[user:password@]host[:port]][/]path[?query][#fragment]```
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


## 会看lambda表达式