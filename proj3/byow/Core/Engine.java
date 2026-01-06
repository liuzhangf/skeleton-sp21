package byow.Core;
import java.awt.*;

import java.nio.file.Paths;
import java.nio.file.Path;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Engine {
    TERenderer ter = new TERenderer();
    public static final int WIDTH = 65;
    public static final int HEIGHT = 65;
    public static final int TEXT_BOX_HEIGHT = 10; // 上方文本框高度（像素，可自定义）
    public static final int TOTAL_WINDOW_HEIGHT = HEIGHT + TEXT_BOX_HEIGHT;
    private int posx, posy;
    private Random RANDOM;
    TETile[][] tiles = new TETile[WIDTH][HEIGHT];
    public int UNLOCKDOORX;
    public int UNLOCKDOORY;
    private ArrayList<GenerateWorld.Room> roomList;


    public void interactWithKeyboard() {
    //    playing();
    }

    private TETile[][] playingString(String s, long seed) {
        GenerateWorld worldGenerator = new GenerateWorld(seed);
        this.UNLOCKDOORX = worldGenerator.UNLOCKDOORX;
        this.UNLOCKDOORY = worldGenerator.UNLOCKDOORY;

        TERenderer ter = new TERenderer();
        RANDOM = new Random(seed);

        if (s.charAt(0) == 'n'){
            this.tiles = worldGenerator.generateTiles();
            this.roomList = worldGenerator.roomList;
            this.UNLOCKDOORY = worldGenerator.UNLOCKDOORY;
            this.UNLOCKDOORX = worldGenerator.UNLOCKDOORX;
            SetStart(tiles);
            for(int i = 1; i < s.length(); i++) {
                if (s.charAt(i) == 's'){
                    int tag = RenderThePicture(2,tiles);
                }
                else if (s.charAt(i) == 'w'){
                    int tag = RenderThePicture(1,tiles);
                }
                else if (s.charAt(i) == 'a'){
                    int tag = RenderThePicture(3,tiles);
                }
                else if (s.charAt(i) == 'd'){
                    int tag = RenderThePicture(4,tiles);
                }
                else if (s.charAt(i) == ':' && s.charAt(i+1) == 'q'){
                    saveGame(tiles);
                    break;
                }
            }
        }

        else if (s.charAt(0) == 'l'){
            GameState gamestate = loadGame();
            rebuild(gamestate);
            for(int i = 1; i < s.length(); i++) {
                if (s.charAt(i) == 's'){
                    int tag = RenderThePicture(2,tiles);
                }
                else if (s.charAt(i) == 'w'){
                    int tag = RenderThePicture(1,tiles);
                }
                else if (s.charAt(i) == 'a'){
                    int tag = RenderThePicture(3,tiles);
                }
                else if (s.charAt(i) == 'd'){
                    int tag = RenderThePicture(4,tiles);
                }
                else if (s.charAt(i) == ':' && s.charAt(i+1) == 'q'){
                    saveGame(tiles);
                }
            }
        }
        return tiles;
    }

    /*
    public void playing(){

        GenerateWorld worldGenerator = new GenerateWorld(0);
        this.UNLOCKDOORX = worldGenerator.UNLOCKDOORX;
        this.UNLOCKDOORY = worldGenerator.UNLOCKDOORY;

        TERenderer ter = new TERenderer();
        //ter.initialize(WIDTH, TOTAL_WINDOW_HEIGHT);
        RANDOM = new Random(0);
        drawMainMenu();
        boolean keyPressed = false;
        int key = captureMovementInput();

        while(( key == 5 || key == 6 || key == -1) && !keyPressed) {

            if (key == 5){
                //TETile[][] tiless = worldGenerator.generateTiles();
                this.tiles = worldGenerator.generateTiles();
                this.roomList = worldGenerator.roomList;
                this.UNLOCKDOORY = worldGenerator.UNLOCKDOORY;
                this.UNLOCKDOORX = worldGenerator.UNLOCKDOORX;
                keyPressed = true;
                SetStart(tiles);
                ter.renderFrame(tiles);
                int move = captureMovementInput();
                while (move != 0){
                    int tag = RenderThePicture(move, tiles);
                    drawFame (tag, tiles);
                    move = captureMovementInput();
                }
            }

            else if (key == 6){
                GameState gamestate = loadGame();
                if(gamestate == null){
                    StdDraw.clear(StdDraw.BLACK);
                    Font font0 = new Font("Monaco", Font.BOLD, 80);
                    StdDraw.setFont(font0);
                    StdDraw.setPenColor(StdDraw.WHITE);
                    StdDraw.text(WIDTH * 0.5, HEIGHT * 0.8 , "No files found");
                    StdDraw.show();
                    StdDraw.pause(2000);
                    drawMainMenu();
                }

                else {
                    rebuild(gamestate);
                    keyPressed = false;
                    ter.renderFrame(this.tiles);
                    int move = captureMovementInput();
                    while (move != 0){
                        int tag = RenderThePicture(move, this.tiles);
                        drawFame (tag, this.tiles);
                        move = captureMovementInput();
                    }
                }
            }

            else {
                System.exit(0);
            }
        }
    }
    */
    private void drawWall(TETile[][] tiles) {
        for (int i = 0; i < WIDTH; i += 1) {
            for (int j = 0; j < HEIGHT; j += 1) {
                if (tiles[i][j] == Tileset.NOTHING) {
                    boolean hasFloor = false;
                    if (j + 1 < HEIGHT && tiles[i][j + 1] == Tileset.FLOOR) {
                        hasFloor = true;
                    }
                    else if (j - 1 >= 0 && tiles[i][j - 1] == Tileset.FLOOR) {
                        hasFloor = true;
                    }
                    else if (i + 1 < WIDTH && tiles[i + 1][j] == Tileset.FLOOR) {
                        hasFloor = true;
                    }
                    else if (i - 1 >= 0 && tiles[i - 1][j] == Tileset.FLOOR) {
                        hasFloor = true;
                    }
                    if (hasFloor) {
                        tiles[i][j] = Tileset.WALL;
                    }
                }
            }
        }
    }

    private void rebuild(GameState gamestate){

        for(int i = 0; i < WIDTH; i += 1){
            for(int j = 0; j < HEIGHT; j += 1){
                this.tiles[i][j] = Tileset.NOTHING;
            }
        }

        for(int i = 0; i < gamestate.roomList.size(); i++){
            int posx = gamestate.roomList.get(i).generatex;
            int posy = gamestate.roomList.get(i).generatey;
            int width = gamestate.roomList.get(i).generatewidth;
            int height = gamestate.roomList.get(i).generationheight;
            for (int x = 0; x < width; x++){
                for (int y = 0; y < height; y++){
                    this.tiles[posx + x][posy + y] = Tileset.FLOOR;
                }
            }
        }

        ArrayList<GenerateWorld.Room> rooms = gamestate.roomList;
        Collections.sort(rooms);
        GeneratePath(tiles, rooms);

        drawWall(tiles);
        tiles[gamestate.PLAYERX][gamestate.PLAYERY] = Tileset.AVATAR;
        tiles[gamestate.unlockdoorx][gamestate.unlockdoory] = Tileset.UNLOCKED_DOOR;
        this.posx = gamestate.PLAYERX;
        this.posy = gamestate.PLAYERY;
        this.RANDOM = gamestate.rand;
    }

    private static final Path SAVE_FILE_PATH = Paths.get(
            System.getProperty("user.home"),
            "byog_game_save.txt"
    );

    private void saveGame (TETile[][] world) {
        this.tiles = world;
        File saveFile = SAVE_FILE_PATH.toFile();
        try (
                FileOutputStream fos = new FileOutputStream(saveFile);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        ){
            GameState state = new GameState(
                    this.UNLOCKDOORX,
                    this.UNLOCKDOORY,
                    this.posx,
                    this.posy,
                    this.roomList,
                    this.RANDOM
            );
            oos.writeObject(state);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private GameState loadGame () {

        File saveFile = SAVE_FILE_PATH.toFile();
        if(!saveFile.exists()) {
            System.out.println("Save file not found");
            return null;
        }

        else {
            try(
                FileInputStream  fis = new FileInputStream(saveFile);
                ObjectInputStream ois = new ObjectInputStream(fis);
            ){
                GameState state = (GameState) ois.readObject();
                return state;
            }catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    private void GeneratePath(TETile[][] tiles, ArrayList<GenerateWorld.Room> roomList) {
        for (int i = 1; i < roomList.size(); i++) {
            if (roomList.get(i - 1).generatex + roomList.get(i - 1).generatewidth - 1 >= roomList.get(i).generatex) {

                if (roomList.get(i - 1).generatey + roomList.get(i - 1).generationheight - 1 < roomList.get(i).generatey){

                    for (int j = roomList.get(i - 1).generatey + roomList.get(i - 1).generationheight - 1; j < roomList.get(i).generatey; j++){
                        tiles[roomList.get(i).generatex][j] = Tileset.FLOOR;
                    }
                }
                else if (roomList.get(i).generatey + roomList.get(i).generationheight - 1 < roomList.get(i - 1).generatey){

                    for (int j = roomList.get(i).generatey + roomList.get(i).generationheight - 1; j < roomList.get(i - 1).generatey; j++){
                        tiles[roomList.get(i).generatex][j] = Tileset.FLOOR;
                    }
                }
            }

            else {
                if (roomList.get(i - 1).generatey >= roomList.get(i).generatey && roomList.get(i - 1).generatey <= roomList.get(i).generatey + roomList.get(i).generationheight - 1){
                    for (int j = roomList.get(i - 1).generatex + roomList.get(i - 1).generatewidth - 1; j < roomList.get(i).generatex; j++){
                        tiles[j][roomList.get(i - 1).generatey] = Tileset.FLOOR;
                    }
                }

                else if (roomList.get(i).generatey >= roomList.get(i - 1).generatey && roomList.get(i).generatey <= roomList.get(i - 1).generatey + roomList.get(i - 1).generationheight - 1 ){
                    for (int j = roomList.get(i - 1).generatex + roomList.get(i - 1).generatewidth - 1; j < roomList.get(i).generatey; j++){
                        tiles[j][roomList.get(i - 1).generatey] = Tileset.FLOOR;
                    }
                }

                else {
                    if (roomList.get(i - 1).generatey + roomList.get(i - 1).generationheight - 1 < roomList.get(i).generatey){

                        int max = Math.max(roomList.get(i - 1).generatex + roomList.get(i - 1).generatewidth, roomList.get(i).generatex - 1);
                        int min = Math.min(roomList.get(i - 1).generatex + roomList.get(i - 1).generatewidth, roomList.get(i).generatex - 1);
                        int column = RANDOM.nextInt(max - min + 1 ) + min;

                        for (int j = min; j <= column; j++){
                            tiles[j][roomList.get(i - 1).generatey + roomList.get(i - 1).generationheight - 1] = Tileset.FLOOR;
                        }
                        for (int j = max; j >= column; j--){
                            tiles[j][roomList.get(i).generatey] = Tileset.FLOOR;
                        }
                        for (int j = roomList.get(i - 1).generatey + roomList.get(i - 1).generationheight; j <= roomList.get(i).generatey; j++){
                            tiles[column][j] = Tileset.FLOOR;
                        }
                    }

                    else {
                        int max = Math.max(roomList.get(i - 1).generatex + roomList.get(i - 1).generatewidth, roomList.get(i).generatex - 1);
                        int min = Math.min(roomList.get(i - 1).generatex + roomList.get(i - 1).generatewidth, roomList.get(i).generatex - 1);
                        int column = RANDOM.nextInt(max - min + 1 ) + min;

                        for (int j = min; j <= column; j++){
                            tiles[j][roomList.get(i - 1).generatey] = Tileset.FLOOR;
                        }
                        for (int j = max; j >= column; j--){
                            tiles[j][roomList.get(i).generatey + roomList.get(i).generationheight] = Tileset.FLOOR;
                        }
                        for (int j = roomList.get(i).generatey + roomList.get(i).generationheight; j <= roomList.get(i - 1).generatey; j++){
                            tiles[column][j] = Tileset.FLOOR;
                        }
                    }
                }
            }
        }
    }

    public void SetStart(TETile[][] world) {
        boolean flag = true;
        while(flag){
            for(int x = 0; x < WIDTH; x++) {
                for(int y = 0; y < HEIGHT; y++) {
                    if(world[x][y] == Tileset.FLOOR && flag) {
                        if(randomInt(0,1000) <= 1){
                            world[x][y] = Tileset.AVATAR;
                            flag = false;
                            posx = x;
                            posy = y;
                        }
                    }
                }
            }
        }
    }

    private void mouseTip() {
        double mouseX = StdDraw.mouseX();
        double mouseY = StdDraw.mouseY();
        int numx = (int) mouseX;
        int numy = (int) mouseY;
        description(numx, numy );
    }

    private void description(int numx, int numy ){
        String str = "";
        if (numx >= 0 && numx < WIDTH && numy >= 0 && numy < HEIGHT ) {
            if(tiles[numx][numy] == Tileset.FLOOR){
                str = "It is the Floor, keep moving!";
            }
            else if(tiles[numx][numy] == Tileset.AVATAR){
                str = "It is the Player, You are right there!";
            }
            else if (tiles[numx][numy] == Tileset.WALL){
                str = "It is the Wall, You cannot walk through it!";
            }
            else if (tiles[numx][numy] == Tileset.NOTHING) {
                str = "OUT OF THIS MAP";
            }
            else {
                str = "fuck";
            }

            clearRightTipArea();

            Font font1 = new Font("Monaco", Font.BOLD, 20);
            StdDraw.setFont(font1);
            StdDraw.setPenColor(StdDraw.RED);

            StdDraw.text(WIDTH - 10, HEIGHT + TEXT_BOX_HEIGHT/2.0 - 4, str);
            StdDraw.show();
        }
    }

    private void drawMainMenu(){
        StdDraw.clear(StdDraw.BLACK);
        Font font0 = new Font("Monaco", Font.BOLD, 80);
        StdDraw.setFont(font0);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(WIDTH * 0.5, HEIGHT * 0.8 , "CS61B: THE GAME");

        Font font1 = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(font1);
        StdDraw.text(WIDTH * 0.5, HEIGHT * 0.55 , "NEW GAME (N)");
        StdDraw.text(WIDTH * 0.5, HEIGHT * 0.5, "LOAD GAME (L)");
        StdDraw.text(WIDTH * 0.5, HEIGHT * 0.45 , "QUIT GAME (Q)");
        StdDraw.show();
    }

    private int randomInt(int min, int max) {
        if (min > max) {
            int temp = min;
            min = max;
            max = temp;
        }
        return min + RANDOM.nextInt(max - min + 1);
    }

    private void drawFame(int moveResult, TETile[][] tiles){

        ter.renderFrame(tiles);
        StdDraw.setPenColor(StdDraw.YELLOW);
        StdDraw.setFont(new Font("Monaco", Font.BOLD, 30));
        String tip = "";
        if (moveResult == 0) {
            tip = "You Touch the Wall!!!";
        } else if (moveResult == 2) {
            tip = "You Move One Step!!!";
        } else {
            StdDraw.clear(StdDraw.BLACK);
            Font font0 = new Font("Monaco", Font.BOLD, 80);
            StdDraw.setFont(font0);
            StdDraw.setPenColor(StdDraw.WHITE);
            StdDraw.text(WIDTH * 0.5, HEIGHT * 0.6 , "You Win");
            StdDraw.show();
        }

        double textX = 10;
        double textY = HEIGHT + TEXT_BOX_HEIGHT/2.0 - 4;
        StdDraw.text(textX, textY, tip);
        StdDraw.show();
        //StdDraw.rectangle(WIDTH/2.0, HEIGHT + TEXT_BOX_HEIGHT/2.0 - 4, WIDTH/2.0 - 2, TEXT_BOX_HEIGHT/2.0 - 4);
        StdDraw.pause(10);
    }

    private int RenderThePicture(int flag, TETile[][] tiles){

        if(flag == 7){
            if (captureMovementInput() == -1){
                saveGame(tiles);//System.exit(0);
            }
        }
        else if (flag == 1){
            if(tiles[posx][posy + 1] == Tileset.WALL){
                return 0;
            }

            else if (tiles[posx][posy + 1] == Tileset.UNLOCKED_DOOR){
                return 1;
            }

            else {
                tiles[posx][posy + 1] = Tileset.AVATAR;
                tiles[posx][posy] = Tileset.FLOOR;
                posy = posy + 1;
            }
        }

        else if (flag == 2){
            if(tiles[posx][posy - 1] == Tileset.WALL){
                return 0;
            }

            else if (tiles[posx][posy - 1] == Tileset.UNLOCKED_DOOR){
                return 1;
            }

            else {
                tiles[posx][posy - 1] = Tileset.AVATAR;
                tiles[posx][posy] = Tileset.FLOOR;
                posy = posy - 1;
            }
        }

        else if (flag == 3){

            if(tiles[posx - 1][posy] == Tileset.WALL){
                return 0;
            }
            else if (tiles[posx - 1][posy] == Tileset.UNLOCKED_DOOR){
                return 1;
            }

            else {
                tiles[posx - 1][posy] = Tileset.AVATAR;
                tiles[posx][posy] = Tileset.FLOOR;
                posx = posx - 1;
            }
        }

        else if(flag == 4) {
            if(tiles[posx + 1][posy] == Tileset.WALL){
                return 0;
            }

            else if (tiles[posx + 1][posy] == Tileset.UNLOCKED_DOOR){
                return 1;
            }

            else {
                tiles[posx + 1][posy] = Tileset.AVATAR;
                tiles[posx][posy] = Tileset.FLOOR;
                posx = posx + 1;
            }
        }
        return 2;
    }

    public int captureMovementInput( ) {
        while (true) {
            mouseTip();
            if (StdDraw.hasNextKeyTyped()) {
                char key = Character.toLowerCase(StdDraw.nextKeyTyped());
                switch (key) {
                    case 'q':
                        return -1;
                    case 'w':
                        return 1;
                    case 's':
                        return 2;
                    case 'a':
                        return 3;
                    case 'd':
                        return 4;
                    case 'n':
                        return 5;
                    case 'l':
                        return 6;
                    case ':':
                        return 7;
                    default:
                        break;
                }
            }
        }
    }

    private void clearRightTipArea() {
        Color originalColor = StdDraw.getPenColor();

        StdDraw.setPenColor(Color.BLACK);
        double centerX = 49;
        double centerY = 70;
        double halfWidth = 16;
        double halfHeight = 5;
        StdDraw.filledRectangle(centerX, centerY, halfWidth, halfHeight);
        StdDraw.setPenColor(originalColor);
    }

    public TETile[][] interactWithInputString (String s) {

        SeedCommandExtractor.Result parseResult = SeedCommandExtractor.extract(s);
        long seed = parseResult.seed;
        String commands = parseResult.command;
        //GenerateWorld worldGenerator = new GenerateWorld(seed);
        return playingString(commands, seed);
        //return worldGenerator.generateTiles();
    }

    public static class SeedCommandExtractor {
        public static class Result {
            public final long seed;
            public final String command;

            public Result(long seed, String command) {
                this.seed = seed;
                this.command = command;
            }
        }

        public static Result extract(String input) {
            if (input == null || input.isEmpty()) {
                return new Result(0, "");
            }

            String lowerInput = input.toLowerCase();
            long seed = 0;
            String command = "";

            int nIndex = lowerInput.indexOf('n');
            if (nIndex == -1) {
                return new Result(0, input);
            }

            int digitStart = nIndex + 1;
            int digitEnd = digitStart;
            while (digitEnd < input.length() && Character.isDigit(input.charAt(digitEnd))) {
                digitEnd++;
            }

            if (digitEnd > digitStart) {
                String seedStr = input.substring(digitStart, digitEnd);
                try {
                    seed = Long.parseLong(seedStr);
                } catch (NumberFormatException e) {
                    seed = 0;
                }
            }

            command = "n" + (digitEnd < input.length() ? input.substring(digitEnd) : "");
            return new Result(seed, command);
        }
    }

    private long parseSeedFromInput(String input) {
        if (input == null || input.isEmpty()) {
            return 0;
        }
        int nIndex = input.indexOf('n');
        int sIndex = input.indexOf('s');
        if (nIndex == -1 || sIndex == -1 || sIndex <= nIndex + 1) {
            return 0;
        }
        String seedStr = input.substring(nIndex + 1, sIndex);
        try {
            return Long.parseLong(seedStr);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

}