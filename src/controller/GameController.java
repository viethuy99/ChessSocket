package controller;

import model.GameModel;
import view.GameView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import model.config;

public class GameController extends Thread{

    private final GameModel theModel;
    private final GameView theView;
//    private ClientControl clientCtr;

    //game loop components
    public int rotation = 0;
    public int inTurn;
    public int who;
    public int[] selectedBox = new int[2];
    public static final String STARTING_PLAYER_COLOR = "white";

    //socket
    private Socket mySocket;
//    private final String serverHost = "localhost";
    private final String serverHost = new config().IP;
    private final int serverPort = 8888;

    /**
     * Constructor which connects the Model and the View
     * @param theModel
     * @param theView
     */
    public GameController(GameModel theModel, GameView theView, int inTurn, int who)
    {
        this.theModel = theModel;
        this.theView = theView;
        this.inTurn = inTurn;
        this.who = who;
        mySocket = openConnection();
        ReadClient read = new ReadClient(mySocket);
        read.start();

        theView.show();
        theView.addBoxListeners(new ButtonListener());
        theView.addForFeitAndResetListeners(new ForfeitAndNewListener());
        theView.addUndoListeneer(new UndoListener());
        theView.changePlayerPaneColor(STARTING_PLAYER_COLOR);
    }

    /**
     * Inner class to represent Listeners for Forfeit and New
     */
    class ForfeitAndNewListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            char colorForfeited = e.getActionCommand().charAt(0);
            if(colorForfeited=='w')
                theModel.incrementPlayerScore("white");
            else if(colorForfeited=='b')
                theModel.incrementPlayerScore("black");
            else if(colorForfeited=='n')
                theModel.newGame();
            else if(colorForfeited == 'r')
                theModel.resetBoard();
//            theView.setWhiteScore(theModel.getPlayerScore("white"));
//            theView.setBlackScore(theModel.getPlayerScore("black"));
            theView.resetGame(false);
            theModel.resetBoard();
            theView.changePlayerPaneColor(theModel.getPlayerInTurnColor());

        }
    }

    /**
     * Listens on the undo button only
     */
    class UndoListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {

            theModel.undoGame();
            theView.undoGame();
            theModel.toggleTurn();
            theView.changePlayerPaneColor(theModel.getPlayerInTurnColor());

        }
    }


    public void setInTurn(int inTurn) {
        this.inTurn = inTurn;
    }
    public int getInTurn() {
        return this.inTurn;
    }

    /**
     * Listens to all chess boxes
     */
    class ButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            //System.out.println(e.getActionCommand());
            int row = e.getActionCommand().charAt(0) - '0';
            int col = e.getActionCommand().charAt(2)- '0';
            switch(rotation)
            {
                case 0: if(!theModel.isOccupied(row,col))
                    break;
                    if(theModel.notInTurnBox(row,col))
                        break;
                    selectBox(row,col);
                    rotation=1;
                    break;
                case 1: boolean success = moveBoxPiece(row,col);
                    if(success && inTurn == 1)
                    {
                        sendData(selectedBox[0],selectedBox[1],row,col);
//                            theModel.toggleTurn();
//                            theView.changePlayerPaneColor(theModel.getPlayerInTurnColor());
//                            theView.moveBox(selectedBox[0],selectedBox[1],row,col);
                    }
                    rotation = 0;
                    theView.setBoxesToNormal();
                    int[] position = theModel.kingInCheck();
                    if(position!=null)
                    {
                        theView.setBoxAsInDanger(position[0],position[1]);
                    }
            }


        }
    }

    /**
     * function to try moving a piece
     * Uses the selectedBox to find the coordinates
     * @param row
     * @param col
     * @return
     */
    private boolean moveBoxPiece(int row, int col) {
        boolean success = theModel.makeMove(selectedBox[0],selectedBox[1],row,col);
        return success;
    }


    /**
     * Selects a box both in View and Model
     * also highlights all the possible boxes
     * @param row
     * @param col
     */
    public void selectBox(int row, int col) {
        selectedBox[0] = row;
        selectedBox[1] = col;
        theView.setBoxAsSelected(row, col);
        ArrayList<int[]> possibleMoves = theModel.getPossibleMoves(row, col);
        for(int[] box: possibleMoves)
        {
            if(theModel.isOccupied(box[0],box[1]))
            {
                theView.setBoxAsInDanger(box[0],box[1]);
                continue;
            }
            theView.setBoxAsPossible(box[0],box[1]);
        }
    }




//---------------------------------------------------------
// Socket



    public Socket openConnection() {
//        System.out.println("Open Connect1");
        try {
            mySocket = new Socket(serverHost, serverPort);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
//        System.out.println("Open Connect2");
        return mySocket;
    }

    public boolean sendData(int row0, int col0, int row1, int col1) {
//        System.out.println("Nhay vao send data");
        try {
            DataOutputStream dos = new DataOutputStream(mySocket.getOutputStream());
            System.out.println("m"+row0+col0+row1+col1+who);
            dos.writeUTF("m"+row0+col0+row1+col1+who);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }


    public boolean closeConnection() {
        try {
            mySocket.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }
    class ReadClient extends Thread {
        private final Socket client;
        public ReadClient(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {
            try {
                DataInputStream dis = new DataInputStream(client.getInputStream());
                while (true) {
                    String sms = dis.readUTF();
                    System.out.println("Nhan duoc data tu server la : " + sms);
                    if (sms.charAt(0) == 'm') {
                        if (Character.getNumericValue(sms.charAt(5)) == 1 - who) {
                            boolean success = theModel.makeMove(Character.getNumericValue(sms.charAt(1)), Character.getNumericValue(sms.charAt(2)), Character.getNumericValue(sms.charAt(3)), Character.getNumericValue(sms.charAt(4)));
                        }

                        theModel.toggleTurn();
                        theView.changePlayerPaneColor(theModel.getPlayerInTurnColor());
                        theView.moveBox(Character.getNumericValue(sms.charAt(1)), Character.getNumericValue(sms.charAt(2)), Character.getNumericValue(sms.charAt(3)), Character.getNumericValue(sms.charAt(4)));

                        setInTurn((1 - getInTurn()));
                    }
                }
            } catch (Exception e) {

            }
        }
    }
}


