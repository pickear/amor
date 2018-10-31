### 什么是amor
&emsp;&emsp;amor是一个基于netty的内网穿透的应用。在IPV6没普及的当下，拥有一个外网IP或者一个固定的外网IP对于很多普通用户来说是一件困难的事，我们很多计算机还是运行在内网环境。
amor的目的是能够提供一个解决方案，让更多运行在内网的计算机能够在外网自由访问。

### 为什么amor
&emsp;&emsp;市面上不缺类似的应用，例如goproxy,frp,ngrok。其中goproxy,frp是用golang写的，ngrok是C写的。golang擅长网络编程，但目前还缺少像java阵营的netty一样成熟的框架；
ngrok目前不是一样开源的应用。amor旨在能做到插件化，更大的吞吐量，能运行在生产环境。