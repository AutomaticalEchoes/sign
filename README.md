# 简易标记(SimpleSign)
[![Build Status](https://img.shields.io/badge/MinecraftForge-1.20.x-brightgreen)](https://github.com/MinecraftForge/MinecraftForge?branch=1.20.x)

_阅前提示：本人喜欢用<sub title="如果影响你观看就先给你道个歉啦！>-<" >**`注`**</sub>来添加注释。_
## 介绍

本模组提供了一个简单标记功能，使用者可以通过快捷键对十字准心瞄准的目标进行标记。  
标记信息会发布到聊天框，按下快捷键或者点击标记信息<sub title="点击标记信息提供的是建议指令，因为本质上是指令模组，点击完提交即可" >**`注`**</sub>开始追踪标记。

## 使用

|行为|操作|
|---|:---:|
|提交标记|<kbd>ctrl</kbd> + <kbd>v</kbd> <br> `/mark <blockpos> or <entity>`<sub title="实体目标选项唯一，但原版提供的建议选项含有多目标选项，使用这些选项并不会执行" >`注`</sub>|
|追踪标记|鼠标左键点击聊天栏中的标记信息，提供建议指令，执行启用追踪 <br> `/getmark <markInfoNbt>` <sub title="不建议手写，因为是NBT" >`注`</sub>|
|追踪最新发布到聊天栏的标记|<kbd>ctrl</kbd> + <kbd>g</kbd>|
|移除最近启用追踪的标记|<kbd>ctrl</kbd> + <kbd>r</kbd>|
|移除所有正在追踪的标记|<kbd>ctrl</kbd> + <kbd>c</kbd>|
|启用/禁用目标边框高亮|`/ssi shouldEntityGlow <bool>`  <sub title="默认为true" >`注`</sub>|
|启用/禁用目标物品显示|`/ssi showDetail(Dev) <bool>`  <sub title="默认为true" >`注`</sub>|
|**标示目标装备栏·Beta**|<kbd>ctrl</kbd> + <kbd>f</kbd> <br>（原版六个装备栏，六个键都是这个，请自行设置）|

### 标记信息
|种类|颜色|事件|
|:---:|:---:|---|
|方块|#55FF55 绿|无|
|实体|#FF55FF 粉|鼠标悬浮显示详情<sub title="Mojang加入了这个功能，但是现在的显示非常单调，而且有bug，功能启动也是只在配置文件中，很隐秘，期待后续" >`注`</sub>|
|展示框 \ 掉落物|#5555FF 蓝紫|鼠标悬浮显示物品详情|
|坐标|#55FFFF 亮蓝|无|

___
**非专业moder,望大家多多海涵.  
如果你发现了什么问题或者有什么建议,可以发邮件给我.~~回不回复随缘~~  
email:AutomaticalEchoes@outlook.com.**
