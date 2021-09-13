# Method


## prctl()

> https://blog.csdn.net/fivedoumi/article/details/24407779

这个系统调用指令是为进程制定而设计的，明确的选择取决于 option


```c
#include <sys/prctl.h>

// 函数原型
int prctl(int option, unsigned long arg2, unsigned long arg3, unsigned long arg4, unsigned long arg5);

// 进程重命名:
prctl(PR_SET_NAME, “process_name”, NULL, NULL, NULL);

```

## fork()

创建子进程
```c
pid_t dumper_pid = fork();
    
if(-1 == dumper_pid)
{
    // 创建失败
    return -1;
}
else if(0 == dumper_pid)
{
    //child process ...
    
}
else
{
    //parent process ...
    
}
```

## execl

> 参考：https://www.cnblogs.com/mickole/p/3187409.html

函数说明：```execl()```用来执行参数path 字符串所代表的文件路径, 接下来的参数代表执行该文件时传递过去的```argv(0), argv[1], ...,``` 最后一个参数必须用空指针(```NULL```)作结束.

```c
#include <unistd.h>

// NULL 即 (char *)0
execl("/bin/ls", "ls", "-al", "/etc/passwd", NULL);

```

## exit()与_exit()
```exit()```函数定义在```stdlib.h```中，而```_exit()```定义在```unistd.h```中
- _exit()函数的作用最为简单：直接使进程停止运行，清除其使用的内存空间，并销毁其在内核中的各种数据结构；
- exit() 函数则在这些基础上作了一些包装，在执行退出之前加了若干道工序。也是因为这个原因，有些人认为exit已经不能算是纯粹的系统调用。
exit()函数与_exit()函数最大的区别就在于exit()函数在调用exit系统调用之前要检查文件的打开情况，把文件缓冲区中的内容写回文件，就是”清理I/O缓冲”。


## syscall 
直接调用系统函数
```c
#include <stdio.h>
#include <unistd.h>
#include <sys/syscall.h>
#include <errno.h>

int rc;
rc = syscall(SYS_chmod, "/etc/passwd", 0444);
if (rc == -1)
        fprintf(stderr, "chmod failed, errno = %d\n", errno);
else
        printf("chmod succeess!\n");
return 0;


```

## dup2
```c
#define STDIN_FILENO	0

#define STDOUT_FILENO	1

#define STDERR_FILENO	2
```
复制一个现存的文件描述符
```c
#include <unistd.h>
int dup(int oldfd);
int dup2(int oldfd, int newfd);
```

## pipe2
创建一个无名管道，然后通过管道实现父子进程间的通信
执行这个函数后，会返回两个文件描述符，一个指向管道的读端，一个指向管道的写端。

> https://blog.csdn.net/commshare/article/details/16981893

```c
#include <unistd.h>

//flags 包括O_NONBLOCK，O_NONBLOCK ，linux特有的。
// int pipe2(int pipefd[2], int flags); 

int pipefd[2];

if ( 0 != pipe2(pipefd, O_CLOEXEC))
{
    // create args pipe failed
    
}


// pipefd[0] 为读端

// pipefd[1] 为写端

```



## readv/writev
功能：散布读/聚集写


## sigaction信号操作函数
```c
int sigaction(int __signal, const struct sigaction* __new_action, struct sigaction* __old_action)
```
会依参数 ```__signal``` 指定的信号编号来设置该信号的处理函数
应用场景：
有时候希望某个进程正确执行，而不想进程受到一些信号的影响，此时就需要用到**信号集操作函数**完成对这些信号的屏蔽。


```__signal```: 第一个参数 int 类型，表示需要关注的信号量
```__new_action```: 第二个参数 ```sigaction``` 结构体指针，用于声明当某个特定信号发生的时候，应该如何处理。
```__old_action```: 第三个参数也是 ```sigaction``` 结构体指针，他表示的是默认处理方式，当我们自定义了信号量处理的时候，用他存储之前默认的处理方式。


>https://ixyzero.com/blog/archives/3431.html


