# MaFengWoTab
自定义蚂蜂窝app滑动tab，实现波浪线滑动

**DEMO 演示：**

<img src="https://github.com/MROU/MaFengWoTab/blob/master/app/src/main/res/mipmap-hdpi/mafengwotab.gif?raw=true" width="50%" height="50%">


**简要描述：**

 这是模仿蚂蜂窝app滑动tab所做的一个demo，如上图所示，其中的BizieViewPagerIndicator与BizierView就是实现波浪滑线的核心，主要实现的原理是利用贝塞尔曲线，这个demo使用的是quadTo来控制一个点，实现波浪，可以看到蚂蜂窝app应该使用的是cubicTo来实现的，控制两个点，看起来更加顺滑。

 **使用说明：**
 
 在我们的布局文件中添加：
 
 

```
  <com.mafengwotab.mafengwotab.view.BizieViewPagerIndicator
        android:id="@+id/id_indicator"
        android:layout_width="0dp"
        android:layout_weight="1"
        android:layout_height="55dp"
        >
    </com.mafengwotab.mafengwotab.view.BizieViewPagerIndicator>
```

1，获取我们的BizieViewPagerIndicator后，可以观察到我们在BizieViewPagerIndicator里面定义了BBizierView，这个BizierView就是我们的下划线，它的滑动依靠我们的viewpager的scroll。

2，BizieViewPagerIndicator的组成主要分为两块，第一是我们的文字区域，第二块是我们的下划线区域

3，BizieViewPagerIndicator里面的HorizontalScrollView主要是来实现滚动的

4，波浪线实现原理就是通过贝塞尔曲线来实现的，对应的函数有cubicTo quadTo

**关于我：**

喜欢用技术的方式去解决问题，深度认同MVP的创业模式，可以搭建android app，熟悉js，如最近的小程序，在生活方面，喜欢尝试不同技能，是个彻底的不正经热血青年。

微博：https://weibo.com/2415138442/profile

自频道：http://i.youku.com/startplan

个人网站：http://www.startplan.cn/

微信公众号：ncistedu

小程序：https://minapp.com/miniapp/7153/

开发的插件： https://addons.mozilla.org/developers/addon/photos-download/validation-result/617663

**参考链接：**

Android 教你打造炫酷的ViewPagerIndicator:https://blog.csdn.net/lmj623565791/article/details/42160391

Android 自定义 View——贝塞尔曲线：
https://juejin.im/entry/5771cb966be3ff00639ffcb2

黑色半透明背景的popupwindow:
https://github.com/lololiu/demo4popupwindow





