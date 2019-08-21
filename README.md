# jokerchat
基于Netty+spring构建的仿微信项目


后台流程：
	1. 
mybatis的逆向生成工具，将SQL中的表逆向生成：Mapper、pojo
	2. 
写一个Controller，检查spring是否正常运行   检验方法：localhost:8088
	3. 
将之前的nettyServer进行改造，main()方法改为start()方法，HelloServer用到了静态单例，将变量在构造器中初始化。
	4. 
检查netty服务器是否正常运行，webSocket能否连接      检验方法：用html页面校验websocket
	5. 
添加Utils:JsonResult\JsonUtils\Md5Utils
	6. 
创建UserController，添加用户登录/注册方法
	7. 
此时创建Service接口以及Impl。  新建方法：queryUserExist \saveUser\

前端流程：
mui项目流程：
1.index.html中， mui.init()   对通知栏的颜色处理  ：mui.plusReady()  
2.body中添加4个底部栏 ，对Icon进行选择（阿里巴巴矢量图库），修改class完成图标的选择。
3.底部栏的4个项目添加4个页面：chat-list.html  discover.html  contact.html  personal.html 
4.显示4个页面  ：    
	*         创建1个chatArray数组，pageId:  pageUrl  
	* mui.reday 将4个webview页面追加到主页面currentwebview上。 
	* 批量绑定tap事件，展示不同的页面
5.form绑定提交事件，  判断用户名和密码的逻辑：  A.用户名为空  B.密码为空  C.用户名、密码过长  D.登录成功  使用组件totast(),foucs() 
创建App.js  将逻辑写在JS中
6.使用AJAX，异步返回数据

