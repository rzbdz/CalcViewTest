#  一个  Java Swing AWT 计算器

基于java swing awt 的GUI计算器,为课程作业 (标准/科学,带括号)

使用栈作为运算结构（适合课程要求和学习需要）

#### $/src
##### controller/

- CalcController : 计算器
- HistoryController: 历史记录
- MemoryController:内存

##### model/

- Calculation:计算器模型
- History:
- Memory:

##### View/
- 见源代码

#### $/test
- TestCalcStack.java 测试类,用于测试中缀表达式转换后缀表达式的子模块


### 5.5 类函数方法表格:

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
