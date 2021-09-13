Websocket状态监听需要做的动作：
 

onClosing回调：
- 正常关闭，code = 1000：什么也不做
- 被T，code = 4000：向上层抛错误码131，然后调用本地websocket的close方法
- 其他code，什么也不做


onFailure回调：此处回调可能由于网络断开或者access重启导致
- 401错误：token无效，向上requestToken，待新的token下来后重新建立websocket
- 如果当前已加入房间，就尝试重连：共尝试5次，每次都是向上requestToken，待新的token下来后重新建立websocket
- 非手动close websocket，则向上抛 网络异常
- 本地手动close websocket，忽略该异常，什么也不做（该异常是由于websocket断开后依然sendMsg导致，如果设置了retryTimeOnConnectionFailure参数，那么restclient会自动将websocket重连上）



websocket总结：

1、总原则：websocket是一个纯粹的网络通信模块   messageCenter不用关心websocket的具体状态，只保留 开关websocket、发送消息 等基本操作; websocket向messageCenter回调msg和错误码；

2、websocket设计：

建立连接：略。。。（和原来的代码保持一致）

发送消息：所有从上层到达wsManager的消息统一入队，然后触发wsSender这个辅助类实现发送
	小细节1：若websocket从初始化到建立连接onOpen这段时间内如果有消息发送，那么先入队，待连接建立成功后再从队列中取出消息发送
	小细节2：如果websocket意外断开了（onFailure），那么待第二次连接成功前的这一段消息同样只入队不发送，待websocket初始化时先清空队列消息，防止旧的消息污染新的连接
	
断网重连：发生意外断网后（onFailure），通过wsReconnector辅助类进行重连操作的管理。
	即，由wsManager提交重连请求，wsReconnector根据重连策略进行规划，在允许的时机回调给wsManager进行重连操作或回调给wsManager目前到达重连次数限制

3、messageCenter设计：
监听onMessage回调处理消息
监听onCode回调，处理异常事件，如被T，鉴权失败，网络异常，重连成功结束 等
发送消息时直接send即可，与websocket状态解耦

4、验证场景：
1、入会成功，并正常收发消息（主要看websocket从初始化--onOpen这一段空闲时间中，如果发消息有没有入队，待onOpen了，入队的消息有没有成功发出去）
2、断网重连：当前策略是 6s*10次，即发现断网后立即执行一次，以后每6秒重试一次，共10次
3、token失效：onFailure回调401错误，ws需要向上层requestToken
4、被T
5、服务器access重启

5、补充一个小bug
在初始化websocket的时候设置了参数 retryTimeOnConnectionFailure(2) 默认为1 。这意味着当发生onFailure的时候restclient会自动重连 n 次。
之前遇到的情况是已经调用了close关闭websocket，但发生了 onFailure回调，这是因为websocket关闭后，依然调用send接口发送。由于 retryTimeOnConnectionFailure 参数生效，导致websocket被自动重连成功。
进而导致了“明明close了，websocket却又自动onOpen了”