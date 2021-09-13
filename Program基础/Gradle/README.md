
# Android项目中Gradle脚本优化


## 目标：
**1. 通过引入一个upload.gradle文件，可以在构建过程中根据输入的运行参数，实现动态灵活的多版本（release和snapshot）发布**

详见：[上传maven仓库](./upload/上传maven仓库.md)

**2. 统一各个模块中“对同一种依赖包”的管理，并且实现在构建过程中动态选择所引入的依赖包的版本（release版和snapshot版）**

详见：[依赖统一管理](./dependencies/依赖统一管理.md)


gradlew ${taskname} -m 打印执行该task的所有task


