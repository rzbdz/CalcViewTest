# CalcViewTest
java swing awt

协同:

dev分支保持最新

在dev 分支开发或者新建branch

| 类/函数 | 注释|
| :----------------------------------------------------------- | :----------------------------------------------------------- |
|BasicOperationButtonClickHandler | 在这里编写基本按钮点击的事件处理 需要重写方法: public void actionPerformed(ActionEvent e); |
|BasicOperationPad              | 这里就是普通的退格加减乘除等 值得注意的是: 这里用了数学符号,和直接的=+-不一样, 比较时要复制字符串数组的符号,或者改成直接的-=+ |
| ButtonClickHandler            | 这个类就是ButtonClickHandler                                 |
| FunctionButtonClickHandler | 在这里编写功能按钮点击的事件处理 需要重写方法: public void actionPerformed(ActionEvent e); |
| FunctionPad                 | 这个Function 写的是除了加减乘除等之外的按键 具体见字符串数组 |
| Header                                 |    这个是那个000框框  |
| MainGridBagLayoutWindows | 使用GridBagLayout作为布局的JFrame之类 构造函数会创建一个完整的计算器窗口 |
| MainWindow      | Main                                                         |
| MButtonBar                  | 这个类是 M 键,M键的解释是: 计算器里面有一个内存,M就是Memory M+：记忆当前数字，累加数bai字当中。 |
| MButtonClickHandler           | 在这里编写M键按钮点击的事件处理 需要重写方法: public void actionPerformed(ActionEvent e); |
| NumberButtonClickHandler | 在这里编写数字按钮点击的事件处理 需要重写方法: public void actionPerformed(ActionEvent e); |
| NumberPad                                | 这里只有 0-9 , +/- , .                                       |