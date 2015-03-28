# Free Reading

This is a android txt story reader which uses Android Studio + Gradle, and follows Material Design.

# About

P.S: The following content is just a little story about this project :)

今天是2015年3月28日，星期六，天气不错，刚吃完午饭，嗯。

这个，呃，就叫阅读器吧，是我给一朋友的朋友写的，当作他的毕业设计。在实习期间有空就写，大概写了两个星期，开发过程挺顺畅的，功能上面基本满足了课题要求，电子书的网络存储用的七牛的云存储服务。

中间卡在了阅读界面那块，本来想做成豆瓣阅读那种样子的，无奈水平不够，呛了几天，想想时间不够，去荡了个`PageWidget`，勉强凑合着用，这个控件实现了阅读的翻页效果，不过显示效果不好，这算是一个BUG，以后有机会再来实现一个阅读UI框架。

加载TXT电子书用的是`BookPageFactory`这个类，这个和之前的`PageWidget`配套使用，一次性加载所有的文本内容到内存中，再利用分页算法排版页面，这个也算是一个BUG，加载一本书内存消耗一下彪上去20MB……暂时不进行优化了。

有同学可能也需要做类似毕业设计课程设计什么的，如果有人要实现阅读器，可以参考一下，写的不好的地方多多包涵。

# Features

- Online bookstore powered by [QiNiu](https://portal.qiniu.com/)
- Bookmark
- Paged reading view
- File system browser
- File search
- Browser history

# Screenshots

![Bookshelf](http://i3.tietuku.com/ec96fb147e92bd02.png)
![Bookstore](http://i3.tietuku.com/41028194efeef91a.png)
![File Browser](http://i3.tietuku.com/e765d5730f8f58ee.png)
![Reading](http://i3.tietuku.com/48a68c917991e68d.png)

# Open Source Projects

- [Support libraries](https://developer.android.com/tools/support-library/features.html)(appcompat-v7)
- [retrofit](https://github.com/square/retrofit)
- [okhttp](https://github.com/square/okhttp)
- [Android-Universal-Image-Loader](https://github.com/nostra13/Android-Universal-Image-Loader)
- [material-dialogs](https://github.com/afollestad/material-dialogs)

# Developed By

Jun Gu - <2dxgujun@gmail.com>

<a href="http://weibo.com/2dxgujun">
  <img alt="Follow me on Weibo" src="http://ww4.sinaimg.cn/large/bce2dea9gw1epjhk9h9m6j20230233yb.jpg"/>
</a>
<a href="https://plus.google.com/u/0/113657331852211913645">
  <img alt="Follow me on Google Plus" src="http://ww1.sinaimg.cn/large/bce2dea9gw1epjhbx0ouij2023023jr6.jpg"/>
</a>

# License

    Copyright 2015 Jun Gu

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.