package byow.Core;


import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class EngineTest {
    public static void main(String[] args) {

        Engine engine = new Engine();
        TETile[][] tiles1 = engine.interactWithInputString("n9127564470038628925sdaddawwawaswasaasswadadaadds");
        TETile[][] tiles2 = engine.interactWithInputString("n9127564470038628925sdaddawwawas:q");
        tiles2 = engine.interactWithInputString("lwasaasswadada:q");
        tiles2 = engine.interactWithInputString("ladds");

        for (int i = 0; i < tiles1.length; i++) {
            for (int j = 0; j < tiles1[i].length; j++) {
                if (tiles1[i][j] != tiles2[i][j]) {
                    System.out.println(i + " " + j);
                //    tiles2[i][j] = Tileset.FLOWER;
                }
            }
        }

        TERenderer ter = new TERenderer();
        ter.initialize(45, 45);
        ter.renderFrame(tiles2);
     //   ter.renderFrame(tiles1);
    }
}
