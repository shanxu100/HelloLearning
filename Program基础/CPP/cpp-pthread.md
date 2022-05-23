## pthread 线程相关


### 1、创建线程私有数据

参考： 
- [《pthread_key_create()--创建线程私有数据|pthread_key_delete()--注销线程私有数据》](https://www.i4k.xyz/article/u011676589/11486651)
- [《pthread_key多次调用pthread_key_create》](https://blog.csdn.net/zzxding888/article/details/81544282)

```c++
#include <pthread.h>
int pthread_key_create(pthread_key_t *key, void ( *destructor)( void *));
void* pthread_getspecific(pthread_key_t __key);
int pthread_setspecific(pthread_key_t __key, const void* __value);
int pthread_key_delete(pthread_key_t key);
```

#### 1.1 pthread_key_create

函数 pthread_key_create() 用来创建线程私有数据。该函数从 TSD 池中分配一项，将其地址值赋给 key 供以后访问使用。第 2 个参数是一个销毁函数，它是可选的，可以为 NULL，为 NULL 时，则系统调用默认的销毁函数进行相关的数据注销。如果不为空，则在线程退出时(调用 pthread_exit() 函数)时将以 key 锁关联的数据作为参数调用它，以释放分配的缓冲区，或是关闭文件流等。

不论哪个线程调用了 pthread_key_create()，所创建的 key 都是所有线程可以访问的，但各个线程可以根据自己的需要往 key 中填入不同的值，相当于提供了一个同名而不同值的全局变量(这个全局变量相对于拥有这个变量的线程来说)




