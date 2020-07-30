package view;

/**
 * 这是一个声明能进入错误状态的接口
 * 因为如果表达式出现错误的时候(比如出现除0的情况),
 * 文本框要提示错误信息,这时候<b>按键必须转换为不可用状态</b>
 * 就是<b>让按钮变灰</b>的意思
 * 现在想到的是回传错误信息后,{@link CalculatorFrame}的
 * {@code setErrorState()}方法调用每个组件的的
 * {@code setErrorState()}方法来完成设置
 * 考虑到还有监听键盘事件{@link CalculatorFrame}
 * 那个稍后搞一个flag好了
 * 现在只有在{@link FunctionPad}里写了,可以参考
 * @see FunctionPad#setErrorState(boolean)
 */
public interface CanTurnErrorState {
    /**
     * 设置UI组件进入错误状态
     * @param bool true则错误state
     */
    public void setErrorState(boolean bool);
}
