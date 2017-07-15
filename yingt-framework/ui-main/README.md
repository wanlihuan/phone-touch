
##  一、启动、预览、主界面的接入流程
#### 方式 1：manifest 文件配置，如下有 4 处配置
```
<application
        android:name=".MyApplication"
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:supportsRtl="true"
        android:theme="@style/SplashTheme"><!-- 配置 1-->
        <activity android:name="com.hafu.uimain.ui.SplashActivity"><!-- 配置 2-->
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

        <!-- 配置 3-->
        <activity  android:name="com.hafu.uimain.ui.ProductTourActivity"
            android:theme="@style/Theme.AppCompat.Light.NoActionBar"/>
        <!-- 配置 4-->
        <activity android:name="com.hafu.uimain.ui.MainTabActivity" />
</application>
```
#### 方式 2：继承框架 Activity 进行扩展开发，详情参考主框架说明
- **启动页 SplashActivity**
```
在该类中添加回调方法，设置用户预览页面 和 主页面，如下:

public class InitActivity extends SplashActivity {

    @Override
    public Class onInitTagMainActivity() {
        return MainActivity.class;
    }

    @Override
    public Class onInitTagTourActivity() {
        return MyProductTourActivity.class;
    }
}

```
- **用户预览页 ProductTourActivity**
```
// 如下例子
public class MyProductTourActivity extends ProductTourActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
```
- **主界面页 MainTabActivity**
```
// 如下例子
public class MainActivity extends MainTabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
```
- **添加启动页 Activity mainfest  的配置**

``` 
<activity
    android:name=".InitActivity"
    android:label="@string/app_name"
    android:theme="@style/SplashTheme" ><!-- 配置 -->
        <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
</activity>
```
## 二、换肤
### 1. 自定义控件的自定义属性换肤的用法
##### 第一步： 继承 SkinAttr 类， 如下
```
public class HqCustomAttrSkin extends SkinAttr {

    @Override
    public void apply(View view) {
        // SegmentControl 就是自定义的控件
        if(view instanceof SegmentControl) {
            SegmentControl segmentControl = (SegmentControl) view;

            if (RES_TYPE_NAME_COLOR.equals(attrValueTypeName)) {
                if("backgroundColors".equals(attrName))// 设置背景颜色
                    segmentControl.setColors(SkinManager.getInstance().convertToColorStateList(attrValueRefId));
                else if("textColors".equals(attrName))// 设置文本颜色
                    segmentControl.setSelectedTextColors(SkinManager.getInstance().convertToColorStateList(attrValueRefId));
            }
        }
    }
}
```

##### 第二步：初始化注册到框架中，如下
```
public class MyApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
   
        // backgroundColors textColors 就是自定义控件的自定义属性名
        CustomAttrFactory.getInstance().builder(HqCustomAttrSkin.class)
                .addAttr("backgroundColors")
                .addAttr("textColors");

    }
```

### 2. 系统控件换肤的用法，如下
```xml
...
xmlns:skin="http://schemas.android.com/android/skin" // 注：一定是这么写，否则换肤会不起作用
...
  <TextView
     ...
     skin:enable="true" 
     ... />
```

### 3. 皮肤主题包的制作
##### 项目工程中有如下目录
- app-skin
    - app-skin-black 黑色主题基础资源字典
    - app-skin-redwhite 红白主题基础资源字典


```
以 assembleRelease 方式编译app-skin-black 或者 app-skin-redwhite 等主题工程，接下来就可以正常运行券商工程项目即可查看效果
```

### 4. 接口说明
```
// 夜间模式的切换接口
YtSkinPresenter.switchNightMode(ILoaderListener callback); 
```
### 【附】: 
   该换肤主题的[ **开源框架** ](https://github.com/wanlihuan/Android-Skin-Loader)

