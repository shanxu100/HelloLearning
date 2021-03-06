# 密码学基础Cryptography

- 对称加密算法：DES、3DES、AES

- 非对称加密算法：RSA、Diffie-Hellman、ECC（椭圆曲线加密算法）

- 散列函数（消息摘要）算法:MD5，Sha-1,Sha-256,Sha-512  
    **特点**：  
    将任意长度的输入转换为固定长度的输出算法  
    加密无需密钥，加密不可逆  
    **缺点**：  
    用于验证数据完整性，仅在数据的散列和数据本身分开传输的条件下才有效


消息验证代码（MAC）

MAC以身份验证扩展了散列函数的密码学函数

任何散列函数都可以用作MAC的基础，另一基础是基于散列的消息验证代码HMAC，其本质是将散列密钥和消息以一种安全的方式交织在一起

## DH秘钥交换算法
DH 依赖的是**求解离散对数问题的复杂性**

(非对称加密算法依赖的是**大整数素因子分解的复杂性**)


**过程：**
[《DH密钥交换算法》](https://blog.csdn.net/fw0124/article/details/8462373)


**缺点：**
- 没有提供通信双方的身份信息，所以不能鉴别双方身份，容易遭受中间人攻击
- 是密集型计算，容易遭受拒绝服务攻击，即攻击者请求大量密钥，被攻击者花费大量计算资源求解无用的幂系数
- 无法防止重放攻击


