package controller;

import model.InvalidExpressionException;

/**
 * 一个用于正则表达式匹配处理文本的接口,详细见{@link ExpressionUnaryFilterTest}
 */
interface DoSomethingToString {
    String doSomething(String substring) throws InvalidExpressionException;
}
