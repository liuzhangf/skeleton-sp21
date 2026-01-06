package byow.Core;

import java.io.Serializable;
import byow.TileEngine.TETile;

import java.util.ArrayList;
import java.util.Random;

public class GameState implements Serializable {
    public int unlockdoorx;
    public int unlockdoory;
    public int PLAYERX;
    public int PLAYERY;
    public ArrayList<GenerateWorld.Room> roomList;
    public Random rand ;

    int vis[][];

    public GameState(int unlockdoorx, int unlockdoory, int posx, int posy,ArrayList<GenerateWorld.Room> roomList, Random rand, int[][] vis) {
        this.unlockdoorx = unlockdoorx;
        this.unlockdoory = unlockdoory;
        this.PLAYERX = posx;
        this.PLAYERY = posy;
        this.roomList = roomList;
        this.rand = rand;
        this.vis = vis;
    }
}