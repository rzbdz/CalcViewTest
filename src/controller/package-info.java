/**
 * 控制器包<br>
 * <p>
 * 这里的控制器负责String到结果的部分,
 * 其中CalculationController负责进行
 * 传入String更新Calcutlation到回传结果的过程.<br></p>
 * <p>
 * DoSomethingToString是给ExpressionUnaryFilter用的一个接口.
 * ExpressionUnaryFilter是一个用来过滤缺陷尝试修复问题表达式
 * (健壮性)和进行一元运算过滤(调用model的UnaryCalculator静态类运算)的过程.</p><br>
 * <br><p>
 * 但是view文本框视图的部分 {@code view.TextHeader} 部分要处理的具体操作太多了,
 * 目前没有抽离出来(可以再搞一个ViewModel),view主要处理的是获得各种按键
 * 之后要进行不同的计算状态和文本的处理,之后再调用Controller来进行运算</p>
 */
package controller;