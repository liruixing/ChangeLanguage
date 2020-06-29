# ChangeLanguage
这是一个应用内切换语言的库，使用方法如下：

1)在gradle中依赖

2）选择语言
 LocaleUtils.updateLocale(this, Locale.CHINESE);
 restartAct();//这是要求重新启动首页，仿照微信，自己写
 
3）在application 中 重载onConfigurationChanged
LocaleUtils.onConfigurationChanged(this,newConfig);
