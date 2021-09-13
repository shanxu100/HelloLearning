## CMake
> https://www.jianshu.com/p/8df5b2aba316
> https://aiden-dong.github.io/2019/07/20/CMake教程之CMake从入门到应用/

### 1. 常用配置
```bash
# 查找当前目录下的所有源文件
# 并将名称保存到 DIR_SRCS 变量
aux_source_directory(. DIR_SRCS)

# 利用源码文件生成目标可执行程序。
add_executable(FirstCpp ${DIR_SRCS})


# 根据源码文件生成library（目标库、依赖库）。
add_library(
        # Sets the name of the library.
        # 生成的library的名字
        native-lib-name

        # Sets the library as a shared library.
        #STATIC\SHARED\MODULE 
        #STATIC库是链接其他目标时使用的目标文件的存档。 SHARED库是动态链接的，并在运行时加载
        SHARED

        # Provides a relative path to your source file(s).
        ${DIR_SRCS})


# 添加子目录，由该子目录下的 CMakeLists.txt 负责编译源码.
add_subdirectory(${DIR_NAME})


# 添加链接库：给FirstCpp添加名字为native-lib-name的library
target_link_libraries(FirstCpp native-lib-name)


# 指定头文件的搜索路径，相当于指定gcc编译器的-I参数
# 需要放到 project("xxx") 后面
include_directories

# 包含子目录,当工程包含多个子目录时,此命令有用
# 需要放到 project("xxx") 后面
add_subdirectory

```




