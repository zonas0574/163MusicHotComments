# 163MusicHotComments
抓取网易云音乐指定歌手的所有歌的热门评论
### 技术介绍
- [maven](https://maven.apache.org/) 管理项目
- [jsoup](https://jsoup.org/) 解析页面元素
- [requests](https://github.com/clearthesky/requests) 获取API数据
- [Commons Codec](https://commons.apache.org/proper/commons-codec/) 处理api请求参数
- [gosn](https://github.com/google/gson) 解析API数据
- [log4j](https://logging.apache.org/log4j/2.x/) 输出信息

### 程序流程
歌手名>歌手Id>专辑Id>歌Id>热门评论

### 使用
1. 下载[Music163HotComments-1.0.jar](https://github.com/zonas0574/163MusicHotComments/releases/tag/1.0)
2. 运行`java -jar Music163HotComments-1.0.jar`
3. 按命令行中提醒依次输入以下参数
  - `singerName` 歌手名（回车默认周杰伦）
  - `maxThread` 专辑获取歌的多线程中最大线程数

### 借鉴
163music API：[知乎回答](https://www.zhihu.com/question/36081767/answer/140287795)
