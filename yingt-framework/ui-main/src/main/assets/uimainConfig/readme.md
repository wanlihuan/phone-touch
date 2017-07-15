## bottom_tab_config
> 主界面的底部 tab 配置说明
```
{
    "version": "20170508", # 版本号
    "backgroundColor": "0xff0000",# 颜色值，采用十六进制字符串表示，使用时 str 转 int 即可
    "tabList": [ # tab 选项列表
        {
            "functionCode": "TAB_1", # 选项的功能 ID
            "fragmentName":"",# 页面的 fragment 包名 + Fragment 类名
            "title": "tab1",# 选项的 文本

            # 图片引用的格式：注,图片大小为 56x56
                1. http://.....  网络图片 url
                2. file:///android_asset/..... 本地 assets 目录下的图片
            "norImage": "http://oobrintj0.bkt.clouddn.com/tab_contest_gray.png",
            "selectImage": "http://oobrintj0.bkt.clouddn.com/tab_contest_gray.png"
        }
    ]
}
```