

# CalcViewTest Java Swing AWT计算器

java swing awt 计算器(标准,带括号)



### 协同说明:

直接在dev 分支开发 merge 时解决冲突, 或者新建 个人/任务 命名的branch 再 merge

dev分支保持最新进度

默认分支是master,

master保持最新完整子模块(以完整的事件/方法为一个子),更新的时候要解决冲突

single_pattern是之前的只有GUI类结构的版本



### 任务列表:

#### 结构:

- [x] 类和文件层次
- [ ] Caculation model 类的属性怎么写(怎么表示一个计算对象)
- [ ] Caculation model 类有什么方法方法
- [ ] CalcController 属性同上上
- [ ] CalcController 方法同上上
- [ ] View 怎么调用Controller的方法
- [ ] Memory 的类属性方法,以及和M按钮之间的绑定

#### 逻辑代码:

- [ ] 中缀表达式转后缀表达式的试验函数(堆栈结构)
- [ ] 怎么用Caculation类表示一个中缀表达式(类的属性)
- [ ] CalcController的过滤输入
- [ ] Calculation的计算成员方法


#### GUI:

- [x] 整体 GUI
- [ ] Memory Frame 内容
- [ ] History Frame 内容
- [ ] GUI响应事件
  - [ ] 文本框自适应
  - [ ] 菜单栏
    - [x] 内存
    - [ ] 历史
    - [ ] 帮助
  - [ ] 文本框
  	- [ ] 复制
  	- [ ] 粘贴
  	- [x] 全选
  	- [ ] 复制等式
  - [ ] 与Controller 和View 连接的事件
    - [ ] M button 
    - [ ] 按键监听过滤
    - [ ] 按键监听处理
    - [ ] 退格键
    - [ ] CE
    - [ ] C
    - [ ] 其他按键绑定



### 记录(重构/改动较多):

6.26 重命名部分文件,添加一些内容:

- 添加两个类内存toolframe和历史记录toolframe
- 改写一些事件
- 改写一些按钮布局,添加括号按钮(后面不写括号再删除)

6.25重构,master/dev保持最新,(single_pattern 分支保留上一个版本)



### 注释/摘要/附录:

#### 程序写法: 

MVC

#### UI的命名(静态/局部变量):

 - TextHeader: 两个文本框(JtextField)
   	- 上面那个显示算式的,约定命名为expressionTextField
    - 下面那个显示当前数字的,约定命名为resultTextField
    
 - MemoryButtonBar: 那一排M键,M键的文字都可以通过MemoryButtonBar.XXXXX访问,方便写事件处理时调用String.equals(this.XXXXX)来比较

    

#### 部分UI对应的事件:

##### 菜单栏

| 内存                                                         | 历史                                                      | 帮助           |      | ⭕置顶      |
| ------------------------------------------------------------ | --------------------------------------------------------- | -------------- | ---- | ---------- |
| 读取内存:这个直接复制MS按键的<br />查看记录:写好了,这里会弹出一个MemoryToolFrame | 读取最新一条历史记录<br />查看记录:后期复制内存菜单的写法 | 这个后期不写了 |      | 这个写完了 |

##### M键:

数据绑定一个栈(用Arraylist),或者就单单一个static寄存器变量算了(如果这样后期去掉查看记录的功能)

| MC           | MR                     | M+                                                      | M-                                                | MS                                     |
| ------------ | ---------------------- | ------------------------------------------------------- | ------------------------------------------------- | -------------------------------------- |
| 清除所有内存 | 从内存栈中读入第一个数 | 把当前 resultTextfield 的数字加到内存栈的第一个数里面去 | 把内存栈的第一个数减去当前 resultTextfield 的数字 | 把当前resultTextfield 的数加到内存栈里 |



#### 项目结构(内容已过时):

##### controller/

- CalcController : 计算器
- HistoryController: 历史记录
- MemoryController:内存

##### model/

- Calculation:计算器模型
- History:
- Memory:

##### View/

- CalculatorFrame.java:计算器框架,继承JFrame,这里用单例
	- FunctionPadControl:函数(^2,%)
	- MenuButtonBarControl :M键,应该改成MemoryButtonBarControl
	- NumberPadControl:数字键(0到9,+/-,= 12个)
	-  OperationPadControl:+-*/(4个)
	- TextHeaderControl:两个文本框

- EventHandler.java:只是handler,通用的(父类)或者全局

App: application 类,应用的入口



#### 类函数方法表格:

注: 重新改过之后这里部分内容被修改了,其中包括类的命名和事件的域

(以下表格内容已过期,参考最新的java源文件结构)

| 类/函数                          | 注释                                                         |
| :------------------------------- | :----------------------------------------------------------- |
| BasicOperationButtonClickHandler | 在这里编写基本按钮点击的事件处理 需要重写方法: public void actionPerformed(ActionEvent e); |
| view.BasicOperationPad           | 这里就是普通的退格加减乘除等 值得注意的是: 这里用了数学符号,和直接的=+-不一样, 比较时要复制字符串数组的符号,或者改成直接的-=+ |
| view.ButtonClickHandler          | 这个类就是ButtonClickHandler                                 |
| FunctionButtonClickHandler       | 在这里编写功能按钮点击的事件处理 需要重写方法: public void actionPerformed(ActionEvent e); |
| view.FunctionPad                 | 这个Function 写的是除了加减乘除等之外的按键 具体见字符串数组 |
| view.Header                      | 这个是那个000框框                                            |
| view.MainGridBagLayoutWindows    | 使用GridBagLayout作为布局的JFrame之类 构造函数会创建一个完整的计算器窗口 |
| view.CalculatorWindow            | Main                                                         |
| view.MButtonBar                  | 这个类是 M 键,M键的解释是: 计算器里面有一个内存,M就是Memory M+：记忆当前数字，累加数bai字当中。 |
| view.MButtonClickHandler         | 在这里编写M键按钮点击的事件处理 需要重写方法: public void actionPerformed(ActionEvent e); |
| NumberButtonClickHandler         | 在这里编写数字按钮点击的事件处理 需要重写方法: public void actionPerformed(ActionEvent e); |
| view.NumberPad                   | 这里只有 0-9 , +/- , .                                       |