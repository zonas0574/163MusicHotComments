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
1. 使用前修改App.java的参数
  - `maxThread` 专辑获取歌的多线程中最大线程数
  - `singerName` 歌手名
``` java
private static final int maxThread = 4;
private static final String singerName = "周杰伦";
```
2. 运行
### 借鉴
163music API：[知乎回答](https://www.zhihu.com/question/36081767/answer/140287795)
