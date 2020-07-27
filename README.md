## IOSDialog
Android仿IOS弹窗

[![](https://jitpack.io/v/Mustang4W/IOSDialog.svg)](https://jitpack.io/#Mustang4W/IOSDialog)
[![License](https://img.shields.io/badge/License%20-Apache%202-337ab7.svg)](https://www.apache.org/licenses/LICENSE-2.0)

## 功能演示
|基本弹窗|
|:---:|
|![](https://github.com/Mustang4W/IOSDialog/blob/master/gif/screencast-Genymotion-2020-07-27_14.30.03.27.gif)|


## 关于集成：
- **在项目的根目录的`build.gradle`添加：**
```
allprojects {
    repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
- **在应用模块的`build.gradle`添加：**
```
dependencies {
    implementation 'com.github.Mustang4W:IOSDialog:v0.5.0'
}
```

## 弹窗使用

```
IOSDialog.with(MainActivity.this)
                .setTitle("提示")
                .setMessage("这是一个弹窗")
                .setInputHint("请输入")
                .setLeftButton("取消", null)
                .setRightButton("OK", new IOSDialog.OnRightButtonClickListener() {
                    @Override
                    public void onClickListener(String input) {
                        Log.d(TAG, "Input Content is " + input);
                    }
                })
                .setCanceledOnTouchOutside(true)
                .show();
```
