# DeepLink && AppLink
[toc]
## 1. Activity的隐式启动
详见:[《Android-四大组件》](./Android-四大组件.md)



## 2. Action、Data、Category的匹配规则

- intent只有和```<intent-filter>```中的 **Action、Data、Category** 同时匹配成功，才能算与```<intent-filter>```匹配成功。
- ```<activity android:name=".xxx"/>```中可以设置多个```<intent-filter>```。intent只要和其中某一个```<intent-filter>```匹配成功，就能启动该Activity。
- 

### Action
- intent中的只能设置一个Action，```<intent-filter>```中可以设置多个Action
- intent中的Action必须与```<intent-filter>```中某一个Action完全相同，才能匹配成功
- **<font color="red"> 深入思考 </font>**
    > 1. 如果一个```<intent-filter>```没有设置Action的值，那么任何一个Intent都不会匹配成功；
    > 2. 如果一个Intent对象没有设置Action值，那么它能通过所有的```<intent-filter>```的Action检查（上一条规则的情况除外）。

### Category
- intent和```<intent-filter>```中都可以设置多个Category
- intent中**所有的Category**必须和```<intent-filter>```中Category完全相同，才能匹配成功


### Data
> https://developer.android.google.cn/guide/topics/manifest/data-element.html?hl=zh_cn
```xml
<data
     android:scheme="xxxx"
     android:host="xxxx"
     android:port="xxxx"
     android:path="xxxx"
     android:pathPattern="xxxx"
     android:pathPrefix="xxxx"
     android:mimeType="xxxx"/>
```
- scheme：协议类型，我们可以自定义，一般是项目或公司缩写，String

- host：域名地址，String，支持通配符

- port：端口，int。

- path：用来匹配完整的路径，如：http://example.com/blog/abc.html，这里将 path 设置为 /blog/abc.html 才能够进行匹配。

- pathPrefix：用来匹配路径的开头部分，拿上面的 Uri 来说，这里将 pathPrefix 设置为 /blog 就能进行匹配了。

- pathPattern：访问路径的匹配格式，相对于path和pathPrefix更为灵活，String

    -  匹配符号：
    “” 用来匹配0次或更多，如：“a” 可以匹配“a”、“aa”、“aaa”…
    “.” 用来匹配任意字符，如：“.” 可以匹配“a”、“b”，“c”…
    因此 “.* ” 就是用来匹配任意字符0次或更多，如：“.*html” 可以匹配 “abchtml”、“chtml”，“html”，“sdf.html”…
    - 转义：
    因为当读取 Xml 的时候，“\” 是被当作转义字符的（当它被用作 pathPattern 转义之前），因此这里需要两次转义，读取 Xml 是一次，在 pathPattern 中使用又是一次。如：“” 这个字符就应该写成 “\”，“\” 这个字符就应该写成 “\\”。

- mimeType：资源类型，例如常见的：video/*, image/png, text/plain。



## 3. Intent对象
### 3.1 实例化
```Java
Intent intent = new Intent();

// action
Intent intent = new Intent(Intent.ACTION_VIEW);

// action 和 data
Intent intent = new Intent(Intent.ACTION_VIEW, uri);

intent.setAction(Intent.ACTION_VIEW);
intent.addCategory(Intent.CATEGORY_DEFAULT);
intent.addCategory(Intent.CATEGORY_BROWSABLE);

// setType会覆盖setData，setData会覆盖setType，
// 因此需要使用setDataAndType方法来设置data和mimeType
intent.setData(Uri.parse(uri));

```


## 4. URI

结构：
```
<scheme> :// <host> : <port> / [ <path> | <pathPrefix> | <pathPattern> ] [ ?<query> ]
```

## 5. Deep Link
官方文档：
> https://developer.android.google.cn/training/app-links/deep-linking?hl=zh_cn
> 深层链接是指将用户直接转到应用中的特定内容的url。在 Android 中，可以通过设置 ```<intent-filter>```以及从传入的 intent 提取数据来设置深层链接，以便将用户吸引到正确的 Activity。

### 5.1 正确配置Activity
```xml
<activity android:name=".DeepLinkActivity">
    <intent-filter>
        <!--使用data启动Activity-->
        <action android:name="android.intent.action.VIEW" />
        <category android:name="android.intent.category.DEFAULT" />
        <category android:name="android.intent.category.BROWSABLE" />
        <data
                android:host="com.example.deeplink.demo"
                android:path="/DeepLinkActivity"
                android:port="8888"
                android:scheme="myscheme" />
    </intent-filter>
</activity>
```
- **```<intent-filter>```元素必须包含一个或多个Action**。如果 Intent 过滤器中没有 Action元素，则过滤器不接受任何 Intent 对象。
- **对于隐式调用，必须将```<category android:name="android.intent.category.DEFAULT" />```加入到```<intent-filter>```中。** 因为调用startActivity()或startActivityForResult()方法，会默认的将```android.intent.category.DEFAULT```加入到intent中。
- 如果要**从网络浏览器中**访问 intent 过滤器，则必须提供```<category android:name="android.intent.category.BROWSABLE" />```

### 5.2 正确设置Intent实例
```java
Intent intent = new Intent(Intent.ACTION_VIEW);


intent.setAction(Intent.ACTION_VIEW);

//intent.addCategory(Intent.CATEGORY_DEFAULT);
//intent.addCategory(Intent.CATEGORY_BROWSABLE);

//setType会覆盖setData，setData会覆盖setType，因此需要使用setDataAndType方法来设置data和mimeType
intent.setData(Uri.parse("myscheme://com.example.deeplink.demo:8888/DeepLinkActivity"));
startActivity(intent);
```

### 5.3 正确设置Webview
```java
// 1. 设置 WebviewClient
webView.setWebViewClient(myWebViewClient);

// 2. override WebviewClient 的 shouldOverrideUrlLoading方法
@Override
public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
    String scheme = request.getUrl().getScheme();
    //还需要判断host
    if ("myscheme".equals(scheme)) {
        Intent intent = new Intent(Intent.ACTION_VIEW, request.getUrl());
        Log.e(TAG, PrintUtil.printUri(request.getUrl()));
        mContext.startActivity(intent);
        // true，表明这次请求交给系统来处理
        return true;
    }
    return super.shouldOverrideUrlLoading(view, request);
}

```

## 6. App Link
官方文档：
> https://developer.android.google.cn/training/app-links/index.html?hl=zh_cn

> 对于DeepLink，如果用户设备上安装的其他应用可以处理相同的 intent，则用户可能无法直接进入您的应用。例如，点击银行发来的电子邮件中的网址可能会显示一个对话框，询问用户是使用浏览器还是银行自己的应用打开此链接。
> 但是，App Link 可以使应用能将自己指定为给定类型链接的默认处理程序。

>https://www.jianshu.com/p/b3ee359bb87b

**对比：**
| | 深层链接	| 应用链接 
| --- | --- | --- | 
intent scheme协议 | http、https 或自定义协议 | 需要 http 或 https
intent Action | 任何Action | 需要 android.intent.action.VIEW
intent Category | 任何Category	需要 android.intent.category.BROWSABLE 和 android.intent.category.DEFAULT
链接验证 | 无 | 需要通过 HTTPS 协议在您的网站上发布 Digital Asset Links 文件
用户体验 | 可能会显示一个消除歧义对话框，以供用户选择用于打开链接的应用 | 无对话框；您的应用会打开以处理您的网站链接
兼容性 | 所有 Android 版本 | Android 6.0 及更高版本







