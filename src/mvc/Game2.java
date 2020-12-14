package mvc;

import controller.GameController;
import model.GameModel;
import view.GameView;

public class Game2 {
    public static void main(String[] args) {
        GameModel model = new GameModel("Huy", "Hien" , false);
        GameView view = new GameView("Client2");
        GameController controller = new GameController(model, view, 0, 0);
    }
}
