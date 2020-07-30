package controller;

/**
 * Controller interface<br>
 * 调用{@code updateModel(para)}是传一个参数给控制器,
 * 控制器转换成一个模型,通过调用{@code updateView()}
 * 返回一个给视图更新视图的对象,
 * 视图通过获得用户输入
 * 的表达式,传给controller,
 * controller搞一个model出来,然后让controller
 * 来操作model,view只需要调用controller而不用关心
 * 具体的数据模型是如何运作的.
 */
public interface Controller{
    /**
     * 通过传入参数更新Model
     * @param parameter 参数
     */
    void updateModel(Object parameter);

    /**
     * 获取更新后的视图
     * @return 返回一个视图要展现的东西
     */
    Object updateView();
}
