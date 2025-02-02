package minefield;

import mvc.*;
import tools.*;
import java.io.Serializable;

public class Minefield extends Model{ //The minefield is a 20x20 grid

    private Square[][] minefield;
    private int userX,userY;
    private boolean gameOver;
    public Minefield(int Mines){

        minefield = new Square[20][20];
        userX = 0;
        userY = 0;
        gameOver = false;

        for(int i=0; i<minefield.length; i++){
            for(int j=0; j<minefield[0].length; j++){
                minefield[i][j] = new Square(false,false, false);
            }
        }
        //Generates random x and y coords to put in Mines based on amount of specified mines (in constructor)
        for(int i = 0; i < Mines; i++){
            int x = (int)(Math.random() * 20 );
            int y = (int)(Math.random() * 20 );
            if(minefield[x][y].hasMine || (x == 0 && y == 0) || (x == 19 && y == 19)){ i--;}
            else{minefield[x][y].hasMine = true;}
        }
        for(int i =0; i < minefield.length; i++){
            for(int j =0; j < minefield[0].length; j++){
                findNearMines(i,j);
            }
        }
        //setting entrance and exit
        minefield[0][0].discovered = true;
        minefield[19][19].isExit = true;
    }

    private void findNearMines(int xPos, int yPos){
        int localMines = 0;
        for(int i = xPos-1; i < xPos+2; i++) {
            for (int j = yPos - 1; j < yPos + 2; j++) {
                if (!(i == -1 || j == -1 || i == 20 || j == 20 || (i == xPos && j == yPos)))//check all boarders
                    localMines += minefield[i][j].hasMine ? 1 : 0;
            }
        }
        minefield[xPos][yPos].nearMines = localMines;
    }

    void changeState(String heading) {
        if(!gameOver){
            switch (heading) {
                case "S": userX++;break;
                case "W": userY--;break;
                case "E": userY++;break;
                case "N": userX--;break;
                case "NW": userY--;userX--;break;
                case "NE": userY++;userX--;break;
                case "SW": userY--;userX++;break;
                case "SE": userY++;userX++;break;
            }
            try{
                minefield[userX][userY].discovered = true;
            }catch(Exception e){
                switch (heading) {
                    case "S": userX--;break;
                    case "W": userY++;break;
                    case "E": userY--;break;
                    case "N": userX++;break;
                    case "NW": userY++;userX++;break;
                    case "NE": userY--;userX++;break;
                    case "SW": userY++;userX--;break;
                    case "SE": userY--;userX--;break;
                }
                Utilities.error("Out of bounds");
            }
            if(minefield[userX][userY].hasMine){
                Utilities.error("Stepped on mine–Gameover!");
                gameOver = true;
            }
            if(minefield[userX][userY].isExit){
                Utilities.inform("You win!!");
                gameOver = true;
            }
            changed(); // from Model, sets changed flag and fires changed event
        }
        else{
            Utilities.inform("Gameover! Start a new game");
        }

    }

    Square[][] getMinefield(){return minefield;}
    int userX(){return userX;}
    int userY(){return userY;}

    class Square implements Serializable{

        boolean hasMine, discovered, isExit;
        int nearMines;

        Square(boolean hasMine, boolean discovered, boolean isExit){
            this.hasMine = hasMine;
            this.discovered = discovered;
            this.isExit = isExit;
            this.nearMines = -1;
        }
    }
}