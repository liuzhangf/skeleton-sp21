package game2048;

import static game2048.Side.*;

/** The input/output and GUI controller for play of a game of 2048.
 *  @author P. N. Hilfinger. */
public class Game {

    /** Controller for a game represented by MODEL, using SOURCE as the
     *  the source of key inputs and random Tiles. */
    public Game(Model model, InputSource source) {
        _model = model;
        _source = source;
        _playing = true;
    }

    /** Return true iff we have not received a Quit command. */
    boolean playing() {
        return _playing;
    }

    /** Clear the board and play one game, until receiving a quit or
     *  new-game request.  Update the viewer with each added tile or
     *  change in the board from tilting. */
    void playGame() {
        _model.clear();
        _model.addTile(getValidNewTile());
        while (_playing) {
            if (!_model.gameOver()) {
                _model.addTile(getValidNewTile());
                _model.notifyObservers();
            }

            boolean moved;
            moved = false;
            while (!moved) {
                String cmnd = _source.getKey();
                switch (cmnd) {
                    case "Quit":
                        _playing = false;
                        return;
                    case "New Game":
                        return;
                    case "Up": case "Down": case "Left": case "Right":
                    case "\u2190": case "\u2191": case "\u2192": case "\u2193":
                        if (!_model.gameOver() && _model.tilt(keyToSide(cmnd))) {
                            _model.notifyObservers(cmnd);
                            moved = true;
                        }
                        break;
                    default:
                        break;
                }

            }
        }
    }

    /** Return the side indicated by KEY ("Up", "Down", "Left",
     *  or "Right"). */
    private Side keyToSide(String key) {
        switch (key) {
            case "Up": case "\u2191":
                return NORTH;
            case "Down": case "\u2193":
                return SOUTH;
            case "Left": case "\u2190":
                return WEST;
            case "Right": case "\u2192":
                return EAST;
            default:
                throw new IllegalArgumentException("unknown key designation");
        }
    }

    /** Return a valid tile, using our source's tile input until finding
     *  one that fits on the current board. Assumes there is at least one
     *  empty square on the board. */

    private Tile getValidNewTile() {
        while (true) {
            Tile tile = _source.getNewTile(_model.size());
            int col = tile.col();
            int row = tile.row();
            if (_model.tile(tile.col(), tile.row()) == null) {
                System.out.println(col + " " + row);
                return tile;
            }
        }
    }
    /*
    private Tile getValidNewTile() {
        // 获取棋盘尺寸（避免重复调用，提升效率）
        int boardSize = _model.size();

        while (true) {
            // ========== 第一步：打印当前棋盘所有位置的 null（空）情况 ==========
            System.out.println("=====================================");
            System.out.println("当前棋盘各位置空/非空状态（视觉视角）：");
            // 按「从上到下、从左到右」的视觉顺序遍历（和 Board.toString() 一致）
            for (int row = boardSize - 1; row >= 0; row--) {
                StringBuilder rowStatus = new StringBuilder();
                rowStatus.append("行 ").append(row).append("：");
                for (int col = 0; col < boardSize; col++) {
                    // 判断该位置是否为 null（空）
                    boolean isNull = _model.tile(col, row) == null;
                    rowStatus.append("列").append(col).append("=")
                            .append(isNull ? "空(null)" : "有瓷砖")
                            .append(" | ");
                }
                // 移除最后一个多余的 " | "，保证格式整洁
                if (rowStatus.length() > 0) {
                    rowStatus.delete(rowStatus.length() - 3, rowStatus.length());
                }
                System.out.println(rowStatus);
            }

            // ========== 第二步：生成候选新瓷砖 ==========
            Tile candidateTile = _source.getNewTile(boardSize);
            System.out.println("-------------------------------------");
            System.out.printf("本次随机生成的候选瓷砖：值=%d，列=%d，行=%d%n",
                    candidateTile.value(), candidateTile.col(), candidateTile.row());

            // ========== 第三步：检查候选瓷砖位置是否为空 ==========
            boolean isPositionEmpty = _model.tile(candidateTile.col(), candidateTile.row()) == null;
            if (isPositionEmpty) {
                System.out.printf("✅ 候选瓷砖位置（列%d，行%d）为空，返回该瓷砖！%n%n",
                        candidateTile.col(), candidateTile.row());
                return candidateTile;
            } else {
                System.out.printf("❌ 候选瓷砖位置（列%d，行%d）已有瓷砖，重新生成...%n%n",
                        candidateTile.col(), candidateTile.row());
            }
        }
    }*/

    /** The playing board. */
    private Model _model;

    /** Input source from standard input. */
    private InputSource _source;

    /** True while user is still willing to play. */
    private boolean _playing;

}
