# image
实现了图片的上传与获取：
  1、上传图片，保存到本地
  2、将本地路径及图片名称存储到数据库
  3、保存及存储后，返回json数据，包括cord,success,data,message
  4、根据ID ，数据库查询获取图片路径，显示在客户端
采用了springboot模式，数据库使用MySql 项目是用Gradle管理。
## 具体使用
*  运行ImageApplication ,启动SpringBoot服务

*  输入 localhost:8080  出现“测试跳转” 则启动成功

*  输入 localhost:8080/file.html   进入图片上传页面，选择本地图片上传

*  输入 localhost:8080/image/id    这里的id,由图片上传成功后的返回信息中获取

