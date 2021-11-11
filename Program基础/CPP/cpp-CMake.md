## CMake
[《CMake 教程》](https://www.jianshu.com/p/8df5b2aba316)
[《CMake 教程 | CMake 从入门到应用》](https://aiden-dong.github.io/2019/07/20/CMake教程之CMake从入门到应用/)
[《CMake多模块的构建方式》](leadroyal.cn/p/781/)

### 1. 常用配置
```bash

# cmake 最低版本需求
cmake_minimum_required(VERSION 3.13)



# 配置工程名称
project(cmake_study)
# 或
project(mainDemo_name)



# 设置
set(CMAKE_CXX_STANDARD 11)



# 增加宏定义， 如果代码中定义了#ifdef ENABLE_DEBUG #endif,这个代码块就会生效。
# 语法 : ADD_DEFINITIONS(-D<宏> -DABC)
ADD_DEFINITIONS(-DRTSA_VERSION_NAME=${VERSION_NAME})
ADD_DEFINITIONS(-DENABLE_DEBUG)



# 查找当前目录下的所有源文件，并将名称保存到变量中
# 这个指令临时被用来自动构建源文件列表。因为目前 cmake 还不能自动发现新添加的源文件。
aux_source_directory(. DIR_SRCS)
# 或
aux_source_directory(. BASE_SRC_LIST)



# 当工程包含多个子目录时,添加子目录。
# 由该子目录下的 CMakeLists.txt 负责编译源码.
# 需要放到 project("xxx") 后面
add_subdirectory(${DIR_NAME})
# 或
add_subdirectory(../common/websocket websocket.out)
# 或
add_subdirectory(channel/src/video)


# 生成可执行程序：编译源码文件生成目标可执行程序
# 语法 : ADD_EXECUTABLE(<name> [source1] [source2 ...])
add_executable(cmake_study src/main.cc)
# 或
add_executable(mainDemo ${DIR_SRCS})

# 生成链接库：根据源码文件生成library
# 语法 : ADD_LIBRARY(<name> [STATIC | SHARED | MODULE] [source1] [source2 ...])
add_library(
        # Sets the name of the library.
        # 生成的library的名字
        subDemo

        # Sets the library as a shared library.
        # STATIC\SHARED\MODULE 
        # STATIC库是链接其他目标时使用的目标文件的存档。 SHARED库是动态链接的，并在运行时加载
        SHARED

        # Provides a relative path to your source file(s).
        ${DIR_SRCS})
# 或
add_library(miniz
        SHARED
        IMPORTED)
# 或
add_library(jsoncppLib
        STATIC
        IMPORTED
        GLOBAL)
# GLOBAL 参数，可选。可用于设置这个library为全局可见。


# 添加外部链接库：给mainDemo添加名字为subDemo、cryptoLib和sslLib的library
target_link_libraries(
        # 当前的库的名字
        mainDemo 
        
        # 需要连接进来的库
        subDemo
        cryptoLib
        sslLib)

set_target_properties( # Specifies the target library.
        cryptoLib

        # Specifies the parameter you want to define.
        PROPERTIES IMPORTED_LOCATION

        # Provides the path to the library you want to import.
        ${JNI_LIBS_DIR}/${ANDROID_ABI}/libcrypto.1.1.so)









# 指定头文件的搜索路径，相当于指定gcc编译器的-I参数
# 需要放到 project("xxx") 后面
include_directories


```




