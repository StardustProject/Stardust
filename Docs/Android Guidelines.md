# 安卓开发规范

## 摘要

* [1 前言](#1-前言)
* [2 AS规范](#2-as规范)
* [3 命名规范](#3-命名规范)
* [4 资源文件规范](#4-资源文件规范)
* [5 版本统一规范](#5-版本统一规范)
* [6 第三方库规范](#6-第三方库规范)
* [7 注释规范](#7-注释规范)
* [8 测试规范](#8-测试规范)
* [9 代码格式](#9-代码格式)



### 1 前言

为了利于项目维护以及规范开发，促进成员之间Code Review的效率，故提出以下开发规范。
该规范参考自[AndroidStandardDevelop][AndroidStandardDevelop]


### 2 AS规范

工欲善其事，必先利其器。

1. 使用最新版的IDE进行开发；
2. 编码格式统一为**UTF-8**；
3. 缩进统一为4个空格（将Tab size设置为4则可以保证tab键按4个空格缩进。另外，不要勾选上Use tab character，可以保证切换到不同tab长度的环境时还能继续保持统一的4个空格的缩进样式）；
4. 编辑完`.java`、 `.xml`等文件后一定要**格式化**（基本格式方面使用 AS 默认模板即可）；
5. 删除多余的import，减少警告出现，可利用AS的`Optimize Imports`(Settings → Keymap → Optimize Imports)快捷键；



### 3 命名规范

**【强调】：** 代码中的命名严禁使用拼音与英文混合的方式,更不允许直接使用中文的方式。正确的英文拼写和语法可以让阅读者易于理解,避免歧义。


#### 3.1 包名

包名全部小写，连续的单词只是简单地连接起来，不使用下划线，采用反域名命名规则，全部使用小写字母。一级包名是顶级域名，通常为`com`,`edu`,`gov`,`net`,`org`等，二级包名为公司名，三级包名根据应用进行命名，后面就是对包名的划分了，关于包名的划分，使用按功能划分。
Google sample～ iosched结构如下，很值得学习。

```
java
└─com
    └─google
        └─samples
            └─apps
                └─iosched
                    │  AppApplication.java  定义Application类
                    │  Config.java          定义配置数据（常量）
                    │
                    ├─about
                    │      AboutActivity.java
                    │
                    ├─appwidget
                    │      ScheduleWidgetProvider.java
                    │      ScheduleWidgetRemoteViewsService.java
                    │
                    ├─debug
                    │  │  DebugAction.java
                    │  │  DebugActivity.java
                    │  │  DebugFragment.java
                    │  │
                    │  └─actions
                    │          DisplayUserDataDebugAction.java
                    │          ForceAppDataSyncNowAction.java
                    │          ForceSyncNowAction.java
                    │          ...
                    │
                    ├─explore
                    │  │  ExploreIOActivity.java
                    │  │  ExploreIOFragment.java
                    │  │  ExploreModel.java
                    │  │  ...
                    │  │
                    │  └─data
                    │          ItemGroup.java
                    │          LiveStreamData.java
                    │          MessageData.java
                    │          ...
                    │
                    ├─feedback
                    │      FeedbackApiHelper.java
                    │      FeedbackConstants.java
                    │      FeedbackHelper.java
                    │      ...
                    │
                    ├─framework
                    │      FragmentListener.java
                    │      LoaderIdlingResource.java
                    │      Model.java
                    │      ...定义interface并实现
                    │
                    ├─gcm
                    │  │  GCMCommand.java
                    │  │  GCMIntentService.java
                    │  │  GCMRedirectedBroadcastReceiver.java
                    │  │  ...
                    │  │
                    │  └─command
                    │          AnnouncementCommand.java
                    │          NotificationCommand.java
                    │          SyncCommand.java
                    │          ...
                    │
                    ├─io
                    │  │  BlocksHandler.java
                    │  │  HandlerException.java
                    │  │  HashtagsHandler.java
                    │  │  ...处理model
                    │  │
                    │  ├─map
                    │  │  └─model
                    │  │          MapData.java
                    │  │          Marker.java
                    │  │          Tile.java
                    │  │
                    │  └─model
                    │          Block.java
                    │          DataManifest.java
                    │          Hashtag.java
                    │          ...
                    │
                    ├─map
                    │  │  InlineInfoFragment.java
                    │  │  MapActivity.java
                    │  │  MapFragment.java
                    │  │  ...
                    │  │
                    │  └─util
                    │          CachedTileProvider.java
                    │          MarkerLoadingTask.java
                    │          MarkerModel.java
                    │          ...
                    │
                    ├─model
                    │      ScheduleHelper.java
                    │      ScheduleItem.java
                    │      ScheduleItemHelper.java
                    │      ...定义model以及实现model相关操作
                    │
                    ├─myschedule
                    │      MyScheduleActivity.java
                    │      MyScheduleAdapter.java
                    │      MyScheduleFragment.java
                    │      ...
                    │
                    ├─provider
                    │      ScheduleContract.java
                    │      ScheduleContractHelper.java
                    │      ScheduleDatabase.java
                    │      ...实现ContentProvider
                    │      （也在此处定义provider依赖的其它类，比如db操作）
                    │
                    ├─receiver
                    │      SessionAlarmReceiver.java
                    │
                    ├─service
                    │      DataBootstrapService.java
                    │      SessionAlarmService.java
                    │      SessionCalendarService.java
                    │
                    ├─session
                    │      SessionDetailActivity.java
                    │      SessionDetailConstants.java
                    │      SessionDetailFragment.java
                    │      ...
                    │
                    ├─settings
                    │      ConfMessageCardUtils.java
                    │      SettingsActivity.java
                    │      SettingsUtils.java
                    │
                    ├─social
                    │      SocialActivity.java
                    │      SocialFragment.java
                    │      SocialModel.java
                    │
                    ├─sync
                    │  │  ConferenceDataHandler.java
                    │  │  RemoteConferenceDataFetcher.java
                    │  │  SyncAdapter.java
                    │  │  ...
                    │  │
                    │  └─userdata
                    │      │  AbstractUserDataSyncHelper.java
                    │      │  OnSuccessListener.java
                    │      │  UserAction.java
                    │      │  ...
                    │      │
                    │      ├─gms
                    │      │      DriveHelper.java
                    │      │      GMSUserDataSyncHelper.java
                    │      │
                    │      └─util
                    │              UserActionHelper.java
                    │              UserDataHelper.java
                    │
                    ├─ui
                    │  │  BaseActivity.java
                    │  │  CheckableLinearLayout.java
                    │  │  SearchActivity.java
                    │  │  ...BaseActivity以及自定义UI组件
                    │  │
                    │  └─widget
                    │          AspectRatioView.java
                    │          BakedBezierInterpolator.java
                    │          BezelImageView.java
                    │          ...自定义小UI控件
                    │
                    ├─util
                    │      AboutUtils.java
                    │      AccountUtils.java
                    │      AnalyticsHelper.java
                    │      ...工具类，提供静态方法
                    │
                    ├─videolibrary
                    │      VideoLibraryActivity.java
                    │      VideoLibraryFilteredActivity.java
                    │      VideoLibraryFilteredFragment.java
                    │      ...
                    │
                    └─welcome
                            AccountFragment.java
                            AttendingFragment.java
                            ConductFragment.java
                            ...
```

参考Google I/O 2015的代码结构，按功能分包具体可以这样做：

```
src
└─com
    └─domain
        └─app
            │  AppApplication.java  定义Application类
            │  Config.java          定义配置数据（常量）
            │
            ├─framework
            │      定义interface以及相关基类
            │
            ├─io
            │      数据定义（model）、数据操作（比如json解析，但不包括db操作）
            │
            ├─model
            │      定义model（数据结构以及getter/setter、compareTo、equals等等，不含复杂操作）
            │      以及modelHelper（提供便于操作model的api）
            │
            ├─provider
            │      实现ContentProvider，及其依赖的db操作
            │
            ├─receiver
            │      实现Receiver
            │
            ├─service
            │      实现Service（比如IntentService），用于在独立线程中异步do stuff
            │
            ├─ui
            │      实现BaseActivity，以及自定义view和widget，相关的Adapter也放这里
            │
            ├─util
            │      实现工具类，提供静态方法
            │
            ├─feature1
            │      Item.java                定义model
            │      ItemHelper.java          实现modelHelper
            │      feature1Activity.java    定义UI
            │      feature1DAO.java         私有db操作
            │      feature1Utils.java       私有工具函数
            │      ...其它私有class
            │
            ├─...其它feature
```


#### 3.2 类名

类名都以`UpperCamelCase`风格编写。


名词，采用大驼峰命名法，尽量避免缩写，除非该缩写是众所周知的， 比如`HTML`, `URL`，如果类名称中包含单词缩写，则单词缩写的每个字母均应大写。

| 类                  | 描述                                       | 例如                                       |
| :----------------- | :--------------------------------------- | :--------------------------------------- |
| Activity 类         | Activity为后缀标识                            | 欢迎页面类WelcomeActivity                     |
| Adapter类           | Adapter 为后缀标识                            | 新闻详情适配器 NewDetailAdapter                 |
| 解析类                | Parser为后缀标识                              | 首页解析类HomePosterParser                    |
| 工具方法类              | Utils或Manager为后缀标识（与系统或第三方的Utils区分）或功能+Utils | 线程池管理类：ThreadPoolManager日志工具类：LogUtils（Logger也可）打印工具类：PrinterUtils |
| 数据库类               | 以DBHelper后缀标识                            | 新闻数据库：NewDBHelper                        |
| Service类           | 以Service为后缀标识                            | 时间服务TimeService                          |
| BroadcastReceiver类 | 以Receiver为后缀标识                           | 推送接收JPushReceiver                        |
| ContentProvider类   | 以Provider为后缀标识                           | ShareProvider                            |
| 自定义的共享基础类          | 以Base开头                                  | BaseActivity,BaseFragment                |

测试类的命名以它要测试的类的名称开始，以Test结束。例如：`HashTest`或`HashIntegrationTest`。

接口（interface）：命名规则与类一样采用大驼峰命名法，多以able或ible结尾，如
`interface Runnable`、`interface Accessible`。



#### 3.3 方法名

方法名都以`lowerCamelCase`风格编写。

方法名通常是动词或动词短语。

| 方法                     | 说明                                   |
| :--------------------- | ------------------------------------ |
| initXX()               | 初始化相关方法,使用init为前缀标识，如初始化布局initView() |
| isXX() checkXX()       | 方法返回值为boolean型的请使用is或check为前缀标识      |
| getXX()                | 返回某个值的方法，使用get为前缀标识                  |
| setXX()                | 设置某个属性值                              |
| handleXX()/processXX() | 对数据进行处理的方法                           |
| displayXX()/showXX()   | 弹出提示框和提示信息，使用display/show为前缀标识       |
| updateXX()             | 更新数据                                 |
| saveXX()               | 保存数据                                 |
| resetXX()              | 重置数据                                 |
| clearXX()              | 清除数据                                 |
| removeXX()             | 移除数据或者视图等，如removeView();             |
| drawXX()               | 绘制数据或效果相关的，使用draw前缀标识                |

#### 3.4 常量名

常量名命名模式为`CONSTANT_CASE`，全部字母大写，用下划线分隔单词。
每个常量都是一个静态`final`字段，但不是所有静态`final`字段都是常量。在决定一个字段是否是一个常量时，考虑它是否真的感觉像是一个常量。例如，如果任何一个该实例的观测状态是可变的，则它几乎肯定不会是一个常量。只是永远不打算改变对象一般是不够的，它要真的一直不变才能将它示为常量。

```java
// Constants
static final int NUMBER = 5;
static final ImmutableListNAMES = ImmutableList.of("Ed", "Ann");
static final Joiner COMMA_JOINER = Joiner.on(','); // because Joiner is immutable
static final SomeMutableType[] EMPTY_ARRAY = {};
enum SomeEnum { ENUM_CONSTANT }

// Not constants
static String nonFinal = "non-final";
final String nonStatic = "non-static";
static final SetmutableCollection = new HashSet();
static final ImmutableSetmutableElements = ImmutableSet.of(mutable);
static final Logger logger = Logger.getLogger(MyClass.getName());
static final String[] nonEmptyArray = {"these", "can", "change"};
```


#### 3.5 非常量字段名

非常量字段名以`lowerCamelCase`风格的基础上改造为如下风格：基本结构为`scopeVariableNameType`。

**scope：范围**

非公有，非静态字段命名以m开头。

静态字段命名以s开头。

公有非静态字段命名以p开头。

公有静态字段（全局变量）命名以g开头。

例子：

```java
public class MyClass {
      int mPackagePrivate;
      private int mPrivate;
      protected int mProtected;
      private static MyClass sSingleton;
      public int pField;
      public static int gField;
}
```

使用1字符前缀来表示作用范围，1个字符的前缀必须小写，前缀后面是由表意性强的一个单词或多个单词组成的名字，而且每个单词的首写字母大写，其它字母小写，这样保证了对变量名能够进行正确的断句。

**Type：类型**

考虑到Android中使用很多UI控件，为避免控件和普通成员变量混淆以及更好达意，所有用来表示控件的成员变量统一加上控件缩写作为前缀。例：ImageView mIvXxx，ListView mLvXxx，CardView mCvXxxx。



> 注意：如果项目中使用`ButterKnife`，则不添加m前缀，以`lowerCamelCase`风格命名。

例如，请使用`mCustomerStrFirst`和`mCustomerStrLast`，而不要使用`mFirstCustomerStr`和`mLastCustomerStr`。

| 量词列表  | 量词后缀说明      |
| ----- | ----------- |
| First | 一组变量中的第一个   |
| Last  | 一组变量中的最后一个  |
| Next  | 一组变量中的下一个变量 |
| Prev  | 一组变量中的上一个   |
| Cur   | 一组变量中的当前变量  |

说明：

集合添加如下后缀：List、Map、Set
数组添加如下后缀：Arr

> 注意：所有的VO（值对象）统一采用标准的lowerCamelCase风格编写，所有的DTO（数据传输对象）就按照接口文档中定义的字段名编写。


#### 3.6 参数名

参数名以`lowerCamelCase`风格编写。
参数应该避免用单个字符命名。


#### 3.7 局部变量名

局部变量名以`lowerCamelCase`风格编写，比起其它类型的名称，局部变量名可以有更为宽松的缩写。

虽然缩写更宽松，但还是要避免用单字符进行命名，除了临时变量和循环变量。

即使局部变量是final和不可改变的，也不应该把它示为常量，自然也不能用常量的规则去命名它。


#### 3.8 临时变量

临时变量通常被取名为`i`、`j`、`k`、`m`和`n`，它们一般用于整型；`c`、`d`、`e`，它们一般用于字符型。 如：`for (int i = 0; i < len ; i++)`。



### 4 资源文件规范

#### 4.1 资源布局文件（XML文件（layout布局文件））

全部小写，采用下划线命名法

##### 4.1.1 contentView命名

必须以全部单词小写，单词间以下划线分割，使用名词或名词词组。

所有Activity或Fragment的contentView必须与其类名对应，对应规则为：将所有字母都转为小写，将类型和功能调换（也就是后缀变前缀）。

例如：`activity_main.xml`


##### 4.1.2 Dialog命名

规则：`dialog_描述.xml`

例如：`dialog_hint.xml`


##### 4.1.3 PopupWindow命名

规则：`ppw_描述.xml`

例如：`ppw_info.xml`


##### 4.1.4 列表项命名

规则：`item_描述.xml`

例如：`item_city.xml`


##### 4.1.5 包含项命名

规则：`模块_(位置)描述.xml`

例如：`activity_main_head.xml`、`activity_main_bottom.xml`

注意：通用的包含项命名采用：`项目名称缩写_描述.xml`

例如：`xxxx_title.xml`


#### 4.2 资源文件（图片drawable文件夹下）

全部小写，采用下划线命名法，加前缀区分

命名模式：可加后缀 `_small` 表示小图， `_big` 表示大图，逻辑名称可由多个单词加下划线组成，采用以下规则：

* `用途_模块名_逻辑名称`
* `用途_模块名_颜色`
* `用途_逻辑名称`
* `用途_颜色`

说明：用途也指控件类型（具体见附录[UI控件缩写表](#ui控件缩写表)）

例如：

| 名称                      | 说明                      |
| ----------------------- | ----------------------- |
| btn_main_home.png       | 按键`用途_模块名_逻辑名称`         |
| divider_maket_white.png | 分割线`用途_模块名_颜色`          |
| ic_edit.png             | 图标`用途_逻辑名称`             |
| bg_main.png             | 背景`用途_逻辑名称`             |
| btn_red.png             | 红色按键`用途_颜色`             |
| btn_red_big.png         | 红色大按键`用途_颜色`            |
| ic_head_small.png       | 小头像`用途_逻辑名称`            |
| bg_input.png            | 输入框背景`用途_逻辑名称`          |
| divider_white.png       | 白色分割线`用途_颜色`            |
| bg_main_head            | 主模块头部背景图片`用途_模块名_逻辑名称`  |
| def_search_cell         | 默认搜索界面单元图片`用途_模块名_逻辑名称` |
| ic_more_help            | 更多帮助图标`用途_逻辑名称`         |
| divider_list_line       | 列表分割线`用途_逻辑名称`          |
| sel_search_ok           | 搜索界面确认选择器`用途_模块名_逻辑名称`  |
| shape_music_ring        | 音乐界面环形形状`用途_模块名_逻辑名称`   |

如果有多种形态，如按钮选择器：`sel_btn_xx.xml`

| 名称                   | 说明                           |
| -------------------- | ---------------------------- |
| sel_btn_xx           | 按钮图片使用`btn_整体效果`（selector）   |
| btn_xx_normal        | 按钮图片使用`btn_正常情况效果`           |
| btn_xx_pressed       | 按钮图片使用`btn_点击时候效果`           |
| btn_xx_focused       | `state_focused`聚焦效果          |
| btn_xx_disabled      | `state_enabled` (false)不可用效果 |
| btn_xx_checked       | `state_checked`选中效果          |
| btn_xx_selected      | `state_selected`选中效果         |
| btn_xx_hovered       | `state_hovered`悬停效果          |
| btn_xx_checkable     | `state_checkable`可选效果        |
| btn_xx_activated     | `state_activated`激活的         |
| btn_xx_windowfocused | `state_window_focused`       |




#### 4.3 动画文件（anim文件夹下）

全部小写，采用下划线命名法，加前缀区分。

具体动画采用以下规则：`模块名_逻辑名称`。

例如：`refresh_progress.xml`、`market_cart_add.xml`、`market_cart_remove.xml`。

普通的tween动画采用如下表格中的命名方式：`动画类型_方向`

| 名称                | 说明      |
| ----------------- | ------- |
| fade_in           | 淡入      |
| fade_out          | 淡出      |
| push_down_in      | 从下方推入   |
| push_down_out     | 从下方推出   |
| push_left         | 推向左方    |
| slide_in_from_top | 从头部滑动进入 |
| zoom_enter        | 变形进入    |
| slide_in          | 滑动进入    |
| shrink_to_middle  | 中间缩小    |


#### 4.4 values中name命名

##### 4.4.1 colors.xml

`colors.xml`的`name`命名使用下划线命名法，在你的`colors.xml`文件中应该只是映射颜色的名称一个ARGB值，而没有其它的。不要使用它为不同的按钮来定义ARGB值。

**不要这样做**

```xml
  <resources>
      <color name="button_foreground">#FFFFFF</color>
      <color name="button_background">#2A91BD</color>
      <color name="comment_background_inactive">#5F5F5F</color>
      <color name="comment_background_active">#939393</color>
      <color name="comment_foreground">#FFFFFF</color>
      <color name="comment_foreground_important">#FF9D2F</color>
      ...
      <color name="comment_shadow">#323232</color>
```

使用这种格式，你会非常容易的开始重复定义ARGB值，这时如果需要改变基本色变的很复杂。同时，这些定义是跟一些环境关联起来的，如`button`或者`comment`, 应该放到一个按钮风格中，而不是在`colors.xml`文件中。

**相反，这样做**

```xml
  <resources>

      <!-- grayscale -->
      <color name="white"     >#FFFFFF</color>
      <color name="gray_light">#DBDBDB</color>
      <color name="gray"      >#939393</color>
      <color name="gray_dark" >#5F5F5F</color>
      <color name="black"     >#323232</color>

      <!-- basic colors -->
      <color name="green">#27D34D</color>
      <color name="blue">#2A91BD</color>
      <color name="orange">#FF9D2F</color>
      <color name="red">#FF432F</color>

  </resources>
```

向应用设计者那里要这个调色板，名称不需要跟 `"green"`、`"blue"` 等等相同。`"brand_primary"`、`"brand_secondary"`、`"brand_negative"`这样的名字也是完全可以接受的。 像这样规范的颜色很容易修改或重构，会使应用一共使用了多少种不同的颜色变得非常清晰。 通常一个具有审美价值的UI来说，减少使用颜色的种类是非常重要的。

> 注意：如果某些颜色和主题有关，那就单独写一个`colors_theme.xml`。


##### 4.4.2 dimens.xml

像对待`colors.xml`一样对待`dimens.xml`文件 与定义颜色调色板一样，你同时也应该定义一个空隙间隔和字体大小的“调色板”。 一个好的例子，如下所示：

```xml
<resources>

    <!-- font sizes -->
    <dimen name="font_22">22sp</dimen>
    <dimen name="font_18">18sp</dimen>
    <dimen name="font_15">15sp</dimen>
    <dimen name="font_12">12sp</dimen>

    <!-- typical spacing between two views -->
    <dimen name="spacing_40">40dp</dimen>
    <dimen name="spacing_24">24dp</dimen>
    <dimen name="spacing_14">14dp</dimen>
    <dimen name="spacing_10">10dp</dimen>
    <dimen name="spacing_4">4dp</dimen>

    <!-- typical sizes of views -->
    <dimen name="button_height_60">60dp</dimen>
    <dimen name="button_height_40">40dp</dimen>
    <dimen name="button_height_32">32dp</dimen>

</resources>

```

布局时在写`margins`和`paddings`时，你应该使用`spacing_xx`尺寸格式来布局，而不是像对待`string`字符串一样直接写值，像这样规范的尺寸很容易修改或重构，会使应用所有用到的尺寸一目了然。 这样写会非常有感觉，会使组织和改变风格或布局是非常容易。


##### 4.4.3 strings.xml

`strings`的`name`命名使用下划线命名法，采用以下规则：`模块名+逻辑名称`，这样方便同一个界面的所有string都放到一起，方便查找。

| 名称                | 说明      |
| ----------------- | ------- |
| main_menu_about   | 主菜单按键文字 |
| friend_title      | 好友模块标题栏 |
| friend_dialog_del | 好友删除提示  |
| login_check_email | 登录验证    |
| dialog_title      | 弹出框标题   |
| button_ok         | 确认键     |
| loading           | 加载文字    |


##### 4.4.4 styles.xml

`style`的`name`命名使用大驼峰命名法，几乎每个项目都需要适当的使用`style`文件，因为对于一个视图来说有一个重复的外观是很常见的，将所有的外观细节属性（`colors`、`padding`、`font`）放在`style`文件中。 在应用中对于大多数文本内容，最起码你应该有一个通用的`style`文件，例如：

```
<style name="ContentText">
    <item name="android:textSize">@dimen/font_normal</item>
    <item name="android:textColor">@color/basic_black</item>
</style>
```

应用到`TextView`中:

```
<TextView
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="@string/price"
    style="@style/ContentText"
    />
```

你或许需要为按钮控件做同样的事情，不要停止在那里。将一组相关的和重复`android:****`的属性放到一个通用的`style`中。

将一个大的`styles.xml`文件分割成多个文件，你可以有多个`styles.xml` 文件。Android SDK支持其它文件，`styles.xml`这个文件名称并没有作用，起作用的是在文件里的`<style>`标签。因此你可以有多个style文件，如`styles.xml`、`styles_home.xml`、`styles_item_details.xml`、`styles_forms.xml`。 不同于资源文件路径需要为系统构建起的有意义，在`res/values`目录下的文件可以任意命名。


#### 4.5 layout中的id命名

命名模式为：`view缩写_模块名_逻辑名`，比如`btn_main_search`
使用`AndroidStudio`的插件`ButterKnife Zelezny`，生成注解非常方便，原生的话可以使用`Android Code Generator`插件。



### 5 版本统一规范

Android开发存在着众多版本的不同，比如`compileSdkVersion`、`minSdkVersion`、`targetSdkVersion`以及项目中依赖第三方库的版本，不同的module及不同的开发人员都有不同的版本，所以需要一个统一版本规范的文件。
//待定


### 6 第三方库规范

别再闭门造车了，用用最新最火的技术吧，安利一波～ **[Android 流行框架查速表][Android 流行框架查速表]**，顺便带上自己的干货～ **[Android开发人员不得不收集的代码][Android开发人员不得不收集的代码]**

希望Team能用时下较新的技术，对开源库的选取，一般都需要选择比较稳定的版本，作者在维护的项目，要考虑作者对issue的解决，以及开发者的知名度等各方面。选取之后，一定的封装是必要的。

个人推荐Team可使用如下优秀轮子：

* **[Retrofit][Retrofit]**
* **[RxAndroid][RxAndroid]**
* **[OkHttp][OkHttp]**
* **[Glide][Glide]** / **[Fresco][Fresco]**
* **[Gson][Gson]** / **[Fastjson][Fastjson]**
* **[EventBus][EventBus]** / **[AndroidEventBus][AndroidEventBus]**
* **[GreenDao][GreenDao]**
* **[Dagger2][Dagger2]**(选用)
* **[Tinker][Tinker]**(选用)


### 7 注释规范

为了减少他人阅读你代码的痛苦值，请在关键地方做好注释。

#### 7.1 类注释

每个类完成后应该有作者姓名的注释，对自己的代码负责。

```java
/**
 * <pre>
 *     author : Blankj
 *     time   : 2017/03/07
 *     desc   : xxxx描述
 *     version: 1.0
 * </pre>
 */
public class WelcomeActivity {
       ...
}
```

具体可以在AS中自己配制，Settings → Editor → File and Code Templates → Includes → File Header，输入

```java
/**
 * <pre>
 *     author : ${USER}
 *     time   : ${YEAR}/${MONTH}/${DAY}
 *     desc   :
 *     version: 1.0
 * </pre>
 */
```

这样便可在每次新建类的时候自动加上该头注释。


#### 7.2 方法注释

每一个成员方法（包括自定义成员方法、覆盖方法、属性方法）的方法头都必须做方法头注释，在方法前一行输入`/** + 回车`或者设置`Fix doc comment`(Settings → Keymap → Fix doc comment)快捷键，AS便会帮你生成模板，我们只需要补全参数即可，如下所示。

```java
/**
 * bitmap转byteArr
 *
 * @param bitmap bitmap对象
 * @param format 格式
 * @return 字节数组
 */
public static byte[] bitmap2Bytes(Bitmap bitmap, CompressFormat format) {
    if (bitmap == null) return null;
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    bitmap.compress(format, 100, baos);
    return baos.toByteArray();
}
```

#### 7.3 块注释

块注释与其周围的代码在同一缩进级别。它们可以是`/* ... */`风格，也可以是`// ...`风格(**`//`后最好带一个空格**）。对于多行的`/* ... */`注释，后续行必须从`*`开始， 并且与前一行的`*`对齐。以下示例注释都是OK的。

```java
/*
 * This is          // And so           /* Or you can
 * okay.            // is this.          * even do this. */
 */
```

注释不要封闭在由星号或其它字符绘制的框架里。

> Tip：在写多行注释时，如果你希望在必要时能重新换行(即注释像段落风格一样)，那么使用`/* ... */`。

#### 7.4 其他一些注释

AS已帮你集成了一些注释模板，我们只需要直接使用即可，在代码中输入`todo`、`fixme`等这些注释模板，回车后便会出现如下注释

``` java
// TODO: 17/3/14 需要实现，但目前还未实现的功能的说明
// FIXME: 17/3/14 需要修正，甚至代码是错误的，不能工作，需要修复的说明
```


### 8 测试规范

// TODO...


### 9 代码格式

1. 代码行之内应该留有适当的空格，即采用松散方式编写代码
- 关键字之后要留空格。象if、for、while 等关键字之后应留一个空格再跟左括号`（ `， 以突出关键字。
- 函数名之后不要留空格， 紧跟左括号`(` ， 以与关键字区别。
- `( ` 向后紧跟，` )`、` ，`、` ;` 向前紧跟， 紧跟处不留空格。
- ` ,` 之后要留空格， 如Function(x, y, z)。如果` ;` 不是一行的结束符号， 其后也要留空格， 如for  (initialization;  condition; update)。
- 值操作符、比较操作符、算术操作符、逻辑操作符、位域操作符，如` =`、` +=`  ` >=`、` <=`、` +`、` *`、` %`、` &&`、` ||`、` <<` 、` ^` 等二元操作符的前后应当加空格。
- 一元操作符如` !`、` ~`、` ++`、` --`、` &`（ 地址运算符） 等前后不加空格。
- 象`［ ］`、` .`、` ->` 这类操作符前后不加空格。

2. 大括号的使用约定。如果是大括号内为空，则简洁地写成{}即可，不需要换行； 如果是非空代码块则花括号不要单独一行，和它前面的代码同一行。而且，花括号与前面的代码之间用一个空格隔开。
正例：
```
if () {
    // TODO
} else {
    // TODO
}
```
反例：
```
if ()
{

}
else
{

}
```

3. 单行字符数限制不超过 120 个，超出需要换行，换行时遵循如下原则：
- 第二行相对第一行缩进 4 个空格，从第三行开始，不再继续缩进，参考示例。
- 运算符与下文一起换行。
- 方法调用的点符号与下文一起换行。
- 方法调用时，多个参数， 需要换行时， 在逗号后进行。
- 在括号前不要换行，见反例。
正例：
```
StringBuffer sb = new StringBuffer();
// 超过 120 个字符的情况下，换行缩进 4 个空格， 点号和方法名称一起换行
sb.append("zi").append("xin")...
    .append("huang")...
    .append("huang")...
    .append("huang");
```
反例：
```
StringBuffer sb = new StringBuffer();
// 超过 120 个字符的情况下，不要在括号前换行
sb.append("zi").append("xin")...append
    ("huang");
// 参数很多的方法调用可能超过 120 个字符， 不要在逗号前换行
method(args1, args2, args3, ...
    , argsX);
```



---


## 附录

### UI控件缩写表

| 名称             | 缩写   |
| -------------- | ---- |
| TextView       | tv   |
| EditText       | et   |
| ImageButton    | ib   |
| Button         | btn  |
| ImageView      | iv   |
| ListView       | lv   |
| GridView       | gv   |
| ProgressBar    | pb   |
| SeekBar        | sb   |
| RadioButtion   | rb   |
| CheckBox       | cb   |
| ScrollView     | sv   |
| LinearLayout   | ll   |
| FrameLayout    | fl   |
| RelativeLayout | rl   |
| RecyclerView   | rv   |
| WebView        | wv   |
| VideoView      | vv   |
| Spinner        | spn  |
| ToggleButton   | tb   |

### 常见的英文单词缩写表

| 名称                   | 缩写                                       |
| -------------------- | ---------------------------------------- |
| icon                 | ic （主要用在app的图标）                          |
| color                | cl（主要用于颜色值）                              |
| average              | avg                                      |
| background           | bg（主要用于布局和子布局的背景）                        |
| selector             | sel主要用于某一view多种状态，不仅包括Listview中的selector，还包括按钮的selector） |
| buffer               | buf                                      |
| control              | ctrl                                     |
| default              | def                                      |
| delete               | del                                      |
| document             | doc                                      |
| error                | err                                      |
| escape               | esc                                      |
| increment            | inc                                      |
| infomation           | info                                     |
| initial              | init                                     |
| image                | img                                      |
| Internationalization | I18N                                     |
| length               | len                                      |
| library              | lib                                      |
| message              | msg                                      |
| password             | pwd                                      |
| position             | pos                                      |
| server               | srv                                      |
| string               | str                                      |
| temp                 | tmp                                      |
| window               | wnd(win)                                 |

程序中使用单词缩写原则：不要用缩写，除非该缩写是约定俗成的。




[Package by features, not layers]: https://medium.com/@cesarmcferreira/package-by-features-not-layers-2d076df1964d#.mp782izhh
[iosched]: https://github.com/google/iosched/tree/master/android/src/main/java/com/google/samples/apps/iosched
[安卓开发规范(updating)]: https://github.com/Blankj/AndroidStandardDevelop
[Android开发者工具]: http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2017/0526/7973.html
[Android Studio下对资源进行分包]: http://www.jianshu.com/p/8e893581b9c7
[Android开发之版本统一规范]: http://www.jianshu.com/p/db6ef4cfa5d1
[Android 流行框架查速表]: http://www.ctolib.com/cheatsheets-Android-ch.html
[Android开发人员不得不收集的代码]: https://github.com/Blankj/AndroidUtilCode
[Retrofit]: https://github.com/square/retrofit
[RxAndroid]: https://github.com/ReactiveX/RxAndroid
[OkHttp]: https://github.com/square/okhttp
[Glide]: https://github.com/bumptech/glide
[Fresco]: https://github.com/facebook/fresco
[Gson]: https://github.com/google/gson
[Fastjson]: https://github.com/alibaba/fastjson
[EventBus]: https://github.com/greenrobot/EventBus
[AndroidEventBus]: https://github.com/bboyfeiyu/AndroidEventBus
[GreenDao]: https://github.com/greenrobot/greenDAO
[Dagger2]: https://github.com/google/dagger
[Tinker]: https://github.com/Tencent/tinker
[Android包命名规范]: http://www.ayqy.net/blog/android%E5%8C%85%E5%91%BD%E5%90%8D%E8%A7%84%E8%8C%83/
[Android 开发最佳实践]: https://github.com/futurice/android-best-practices/blob/master/translations/Chinese/README.cn.md
[Android 编码规范]: http://www.jianshu.com/p/0a984f999592
[阿里巴巴Java开发手册]: https://102.alibaba.com/newsInfo.htm?newsId=6
[Google Java编程风格指南]: http://www.hawstein.com/posts/google-java-style.html
[小细节，大用途，35 个 Java 代码性能优化总结！]: http://www.jianshu.com/p/436943216526
[AndroidStandardDevelop]: https://github.com/Blankj/AndroidStandardDevelop
