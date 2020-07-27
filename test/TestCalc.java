import controller.CalcController;
import model.Calculation;

public class TestCalc {
    public static void main(String[] args) {
        CalcController c = CalcController.getInstance();
        c.updateModel("8+5");
        System.out.println(c.updateView());
    }
}
