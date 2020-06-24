# CalcViewTest
java swing awt 计算器(标准)

协同:

dev分支保持最新

在dev 分支开发或者新建branch



记录:

6.25重构,master/dev保持最新,(single_pattern 分支保留上一版本)

 [MVC 模式](https://www.runoob.com/design-pattern/mvc-pattern.html)



项目结构:

controller/

​	CalcController : 计算器

​	HistoryController: 历史记录

​	MemoryController:内存

model/

​	Calculation:计算器模型

​	History:

​	Memory:

Vlew/

​	CalculatorFrame.java:计算器框架,继承JFrame,这里用单例

​			FunctionPadControl:函数(^2,%)

​		   MenuButtonBarControl :M键,应该改成MemoryButtonBarControl

​		   NumberPadControl:数字键(0到9,+/-,= 12个)

​		   OperationPadControl:+-*/(4个)

​		   TextHeaderControl:两个文本框

​	EventHandler.java:只是handler,通用的(父类)或者全局

App: application 类





以下表格内容可能已过期,请参考最新的java源文件

| 类/函数 | 注释|
| :----------------------------------------------------------- | :----------------------------------------------------------- |
|BasicOperationButtonClickHandler | 在这里编写基本按钮点击的事件处理 需要重写方法: public void actionPerformed(ActionEvent e); |
|view.BasicOperationPad              | 这里就是普通的退格加减乘除等 值得注意的是: 这里用了数学符号,和直接的=+-不一样, 比较时要复制字符串数组的符号,或者改成直接的-=+ |
| view.ButtonClickHandler            | 这个类就是ButtonClickHandler                                 |
| FunctionButtonClickHandler | 在这里编写功能按钮点击的事件处理 需要重写方法: public void actionPerformed(ActionEvent e); |
| view.FunctionPad                 | 这个Function 写的是除了加减乘除等之外的按键 具体见字符串数组 |
| view.Header                                 |    这个是那个000框框  |
| view.MainGridBagLayoutWindows | 使用GridBagLayout作为布局的JFrame之类 构造函数会创建一个完整的计算器窗口 |
| view.CalculatorWindow      | Main                                                         |
| view.MButtonBar                  | 这个类是 M 键,M键的解释是: 计算器里面有一个内存,M就是Memory M+：记忆当前数字，累加数bai字当中。 |
| view.MButtonClickHandler           | 在这里编写M键按钮点击的事件处理 需要重写方法: public void actionPerformed(ActionEvent e); |
| NumberButtonClickHandler | 在这里编写数字按钮点击的事件处理 需要重写方法: public void actionPerformed(ActionEvent e); |
| view.NumberPad                                | 这里只有 0-9 , +/- , .                                       |