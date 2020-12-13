/**
 * 视图包
 * <ul>
 *  <li>CalculatorFrame:整个计算器的主窗口,它负责排列其他组件</li>
 *  <li>{@link view.TextHeader}:2行JPanel(窗口上方), 包含一个显示表达式的小文本框,一个显示结果的大文本框</li>
 *  <li>{@link view.MemoryButtonBar}:1行5个按钮(文本框下方),是内存按键的按钮MC,MR,M+</li>
 *  <li>{@link view.FunctionPad}:3*3的网格JPanel组件(窗口中间),包含括号,CE/C,以及与x有关的一元/二元运算</li>
 *  <li>{@link view.NumberPad}:4*3的数字按钮(窗口左下),小数点,以及正负号转变按钮</li>
 *  <li>{@link view.OperationPad}:7*1的运算按钮(窗口最右边),包括退格键到等号的按钮</li>
 * </ul>
 * 图标ICON需要放到类加载器目录的src/icon.png内
 */
package view;