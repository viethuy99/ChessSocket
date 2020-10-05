package boardSetUp;
import components.Player;


public class GameState {

    Player playerInTurn;
    Player playerNotInTurn;
    String gameStatus;

    public GameState()    {

        playerInTurn = new Player("White");
        playerInTurn = new Player("Black");

    }

    public GameState(GameState toCopy)
    {

    }

    public void switchTurn()
    {

        Player temp  = playerInTurn;
        playerInTurn = playerNotInTurn;
        playerNotInTurn = playerInTurn;
    }

    public Player getPlayerInTurn()
    {
        return playerInTurn;
    }

}
