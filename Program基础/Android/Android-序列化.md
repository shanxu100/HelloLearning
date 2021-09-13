# 序列化

- 序列化和反序列化，就是在JVM中运行的对象与二进制字节之间相互转换的过程。
- 将对象进行序列化后，可以**存储到本地文件、数据库或通过网络传输**等


## 1、Serializable
Java的序列化接口，使用简单，但开销大，序列化和反序列化需要大量IO操作。
Serializable的作用是为了保持对象的属性到**本地文件、数据库、网络流**等以方便数据传输，当然这种传输可以是程序内的也可以是两个程序间的。

### 1.1 ```serialVersionUID```含义
- 与每个可序列化的类关联一个版本号，称为 ```serialVersionUID```，即是用来辅助序列化和反序列化的，原则上**序列化后的数据中的```serialVersionUID```和当前类的```serialVersionUID```相同，才能被反序列化**，否则会报 ```InvalidClassException``` 异常。
- 还有一种特殊的情况，当类的结构发生了根本性变化。比如类名改变了，那么反序列化就会失败。即使我们设置的```serialVersionUID```通过了验证，这是因为类的结构发生了毁灭性的改变，无法从旧版本的数据中还原出一个新的类结构的对象。
- 默认的```serialVersionUID```值由JVM根据类的信息生成。因为不同JVM的实现不同，所以计算 ```serialVersionUID``` 的方法也可能不同。这样可能导致反序列化失败。所以强烈建议显式 声明```serialVersionUID``` 属性。如：
```java
//序列化版本号    
private static final long serialVersionUID = 1111013L;
```

### 1.2 为什么Serializable序列化开销大？


## Parcelable
是Android的序列化方式，使用复杂，但效率高。
设计Parcelable的目的是为了**支持对象跨进程通信的功能**（对象是不能直接跨进程传输的。对象的跨进程传输，其本质是序列化和反序列化的过程）。这些数据仅在内存中存在，Parcelable是通过IBinder通信的消息的载体。因此它**不具备类似Serializable的版本功能，所以Parcelable 不适合永久存储**。
- 使用示例：
```java
public class Animal implements Parcelable {

    private String name;

    private int age;

    private List<String> hobbies;

    public Animal(String name, int age) {
        this.name = name;
        this.age = age;
        this.hobbies = new LinkedList<>();
    }

    /**
     * 从序列化后的对象中创建原始对象
     * 与 writeToParcel 写的顺序一致
     *
     * @param in in
     */
    protected Animal(Parcel in) {
        name = in.readString();
        age = in.readInt();
        hobbies = in.createStringArrayList();
    }

    /**
     * 返回当前对象的内容描述，几乎绝大多数情况下都会返回0
     * 只有当含有文件描述符时，才会返回1
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * 实现序列化
     * 通过pancel的一系列write方法实现序列化
     */
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeInt(age);
        parcel.writeStringList(hobbies);
    }

    /**
     * 实现反序列化
     */
    public static final Creator<Animal> CREATOR = new Creator<Animal>() {

        /**
         * 反序列化对象
         */
        @Override
        public Animal createFromParcel(Parcel in) {
            return new Animal(in);
        }

        /**
         * 创建指定长度的原始对象数组
         */
        @Override
        public Animal[] newArray(int size) {
            return new Animal[size];
        }
    };

}

```






