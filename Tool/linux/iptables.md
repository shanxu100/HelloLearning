## iptables

iptables是liunx提供给用户层的命令接口, 可以通过iptables为Netfilter添加策略, 从而实现报文修改和过滤功能。

### 概念：
根据防火墙功能的不同, 引入"表"的概念, 常用的有:

```filter```: 定义允许/允许的功能, 只能在INPUT, FORWARD, OUTPUT三条链中修改
```nat```: 定义地址转换, 只能在PREROUTING, OUTPUT, POSTROUTING三条链中修改
```managle```: 用于修改数据包，在5条链中都可以实现
目前防火墙主要使用定义策略来定义功能，主要分阻止和接收两种动作, 防火墙根据网络数据包中的信息与**依次从前到后**与定义的策略中比较, **匹配到一条策略则执行策略定义的动作**, 若未匹配则执行默认动作

### 命令格式:
```iptables [-t table] COMMAND chain [options] [-j] ACTION```
**说明:**

- ``-t`` table: 用于指定最那个表操作, 主要是filter, nat, managle, 默认为filter
- COMMAND: 常用-P(设置默认动作), -A(添加), -D(删除), -I(插入), -R(替换), -L(列举策略), -S(打印策略), -F(删除所策略)
- chain: 定义策略应用于五个规则链中的哪条上
    - 对于filter：INPUT, FORWARD, OUTPUT
    - 对于nat：PREROUTING, OUTPUT, POSTROUTING
    - 对于managle：用于修改数据包，在5条链中都可以实现
- options: 定义数据包中的特征, 比如源ip，目的ip，源端口，目的端口，协议类型等
- ``-j``: 定义匹配到策略数据包的执行动作, 常用ACCEPT, DROP, REJECT

**对于使用iptables修改策略后, 策略会立即生效**


### options:
- ```-s```: 指定源ip地址, 必须为ip地址、CIDR或者为0.0.0.0/0.0.0.0表示全部, 可在ip地址之前添加!表示取反操作
- ```-d```: 表示目的ip地址
- ```-p```: 表示协议类型, 可使用协议编号或者协议名, tcp, udp, icmp
- ```-j```:
    常用的ACTION主要有:
    DROP: 表示丢弃, 常用于隐藏服务
    REJECT: 明确拒绝
    ACCEPT: 接受




### 举例：

```bash
## 禁止所有udp协议
iptables -A INPUT -p udp -j DROP

## 禁止某个IP的进出
iptables -A INPUT -d 116.31.96.207 -j DROP
iptables -A OUTPUT -d 116.31.96.207 -j DROP

## 查看
iptables -L INPUT -n --line-number
## -n: 将策略中的ip信息直接打印, 若不添加则会将ip反向域名解析，显示为域名信息
## --line-num: 显示策略编号

## 删除 
## 删除 INPUT 上的第3条数据 
iptables -D INPUT 3
## 删除 OUTPUT 上的第7条数据
iptables -D OUTPUT 7

```







