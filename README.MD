 
# 监控组件api  [![Build Status](https://travis-ci.org/SilenceSu/Easy3dNav.svg?branch=master)](https://travis-ci.org/SilenceSu/recastNav)

 
## Introduction - 介绍
     该组件基于micrometer将api于registry实现分离。方便项目中切换registry实现，兼容不同的监控平台。      

### Summary - 概要
      目前registry 只支持prometheus，后期视实际使用增加更多实现。
    

### Usage - 用法
   1. 引入core 与 监控平台实现类。
   2. 调用Metrics类 com.sh_game.metrics.core.Metrics  
   
````
       //计数器
        Counter count = Metrics.counter("game.login.qps");
       //gauge
       AtomicInteger onlineNum = new AtomicInteger(0);
       Metrics.gauge("game.online.num",onlineNUm);
````

### 更多类型
````
 //暴漏原始registry
 MeterRegistry  meter=      Metrics.meter();
````
