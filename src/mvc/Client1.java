package mvc;

import controller.GameController;
import model.GameModel;
import view.GameView;


public class Client1 {

    public static void main(String[] args)
    {
        GameModel model = new GameModel("Huy", "Hien" , false);
        GameView view = new GameView("Client1");
        GameController controller = new GameController(model, view, 1, 1);
    }
}
