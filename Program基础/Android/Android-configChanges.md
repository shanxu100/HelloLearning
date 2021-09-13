## configChanges
在AndroidManifest文件中的Activity中增加配置
```android:configChanges="orientation|keyboard|keyboardHidden"```
- orientation:设备旋转
- keyboard:显示键盘
- keyboardHidden:隐藏键盘

-------
## Activity在旋转的时候发生的变化

1. Resources 来获取 res 目录下的各种与设备相关的资源。
2. AssetManager 来获取 assets 目录下的资源

### 情况一：
在AndroidManifest文件中的Activity中无配置```android:configChanges="orientation"```
```bash
## 在onCreate中
-------onCreate
context.hashCode():14058213
context.getAssets().hashCode():211719304
context.getResources().hashCode():167173665
-------onStart

## 第一次旋转屏幕
-------onStop
-------onDestroy
-------onCreate
context.hashCode():195933645
context.getAssets().hashCode():113635667
context.getResources().hashCode():167173665
-------onStart

## 第二次旋转屏幕
-------onStop
-------onDestroy
-------onCreate
context.hashCode():192952267
context.getAssets().hashCode():211719304
context.getResources().hashCode():167173665
-------onStart

## 第三次旋转屏幕
-------onStop
-------onDestroy
-------onCreate
context.hashCode():89104795
context.getAssets().hashCode():113635667
context.getResources().hashCode():167173665
-------onStart
```
结论：
- context：每次旋转都会变化
- assetsmanager：发生了变化。再旋转回来就恢复了原来的对象。话句话说，Android横竖屏状态下有俩assetsmanager
- resources：没有变化，旋转前后还是原来的对象

### 情况二：
在AndroidManifest文件中的Activity中增加配置```android:configChanges="orientation"```，表示在旋转屏幕的时候不重绘该Activity，即不回调```OnCreate、onStart```等Activity生命周期方法，而是回调```onConfigurationChanged(Configuration newConfig)```方法。

```bash
## 在onCreate中
-------onCreate
context.hashCode():14058213
context.getAssets().hashCode():211719304
context.getResources().hashCode():167173665
-------onStart

## 第一次旋转屏幕
-------onConfigurationChanged:发生了旋转
context.hashCode():14058213
context.getAssets().hashCode():84447076
context.getResources().hashCode():167173665

## 第二次旋转屏幕
-------onConfigurationChanged:发生了旋转
context.hashCode():14058213
context.getAssets().hashCode():211719304
context.getResources().hashCode():167173665

## 第三次旋转屏幕
-------onConfigurationChanged:发生了旋转
context.hashCode():14058213
context.getAssets().hashCode():84447076
context.getResources().hashCode():167173665
```
结论：
- context：没有变化，旋转前后还是原来的对象
- assetsmanager：发生了变化。再旋转回来就恢复了原来的对象。话句话说，Android横竖屏状态下有俩assetsmanager
- resources：没有变化，旋转前后还是原来的对象
