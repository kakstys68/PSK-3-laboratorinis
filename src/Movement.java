public class Movement {
    Board board;
    Renderer renderer;
    public Movement(Board board, Renderer renderer) {
        this.board = board;
        this.renderer = renderer;
    }

    public void move(char input) {
        switch (input) {
            case 'w' -> moveUp(board);
            case 'a' -> moveToSide(board, Direction.LEFT);
            case 's' -> moveDown(board);
            case 'd' -> moveToSide(board, Direction.RIGHT);
            case 'q' -> renderer.quitting();
            default -> renderer.badInput();
        }
    }

    public void moveUp(Board board){
        int border;
        for(int i = 0; i < board.getMatrix().length; i++) {
            border = 0;
            for (int j = 0; j < board.getMatrix().length; j++) {
                if(board.getMatrix()[j][i].getValue() != 0){
                    move(createTilePositionForMovement(j,i), Direction.UP, border);
                }
            }
        }
        board.addTile();
    }
    public void moveDown(Board board){
        int border;
        for(int i = 0; i < board.getMatrix().length; i++){
            border = board.getMatrix().length - 1;
            for(int j = board.getMatrix().length - 1; j>=0; j--) {
                if ( board.getMatrix()[j][i].getValue() != 0 ) {
                    if ( border >= j ) {
                        move(createTilePositionForMovement(j,i), Direction.DOWN, border);
                    }
                }
            }
        }
        board.addTile();
    }

    public void moveToSide(Board board, Direction direction){
        int border;
        int length = board.getMatrix().length;
        for ( int i = 0; i < length; i++ ) {
            border = setBorder(direction, length);
            checkRow(border, i, length, direction);
        }
        board.addTile();
    }

    public void move(TilePosition tilePosition, Direction direction, int border) {
        if(direction == Direction.RIGHT || direction == Direction.LEFT) {
            horizontalMove(tilePosition, direction, border);
        } else verticalMove(tilePosition, direction, border);
    }

    public void verticalMove(TilePosition tilePosition, Direction direction, int border){
        final Tile currentTile = fetchCurrentTile(board, tilePosition, border, true);
        final Tile targetTile = fetchTargetTile(board, tilePosition);

        if(checkTileValue(currentTile, targetTile) && checkIfTilesCanBeMerged(tilePosition, direction, border, true)){
            mergeTiles(currentTile, targetTile);
        } else{
            border = changeBorder(direction, border, Direction.DOWN);
            verticalMove(tilePosition, direction, border);
        }
    }

    public void horizontalMove(TilePosition tilePosition, Direction direction, int border){
        final Tile currentTile = fetchCurrentTile(board, tilePosition, border, false);
        final Tile targetTile = fetchTargetTile(board, tilePosition);

        if (checkTileValue(currentTile, targetTile) && checkIfTilesCanBeMerged(tilePosition, direction, border, false)) {
            mergeTiles(currentTile, targetTile);
        } else {
            border = changeBorder(direction, border, Direction.RIGHT);
            horizontalMove(tilePosition, direction, border);
        }
    }

    private int setBorder(Direction direction, int length){
        if(direction == Direction.LEFT){
            return 0;
        }else{
            return length - 1;
        }
    }

    private void moveIfPossible(int border,int i, int j, Direction direction){

        if (board.getMatrix()[i][j].getValue() != 0 ) {
            if (border >= j) {
                move(createTilePositionForMovement(i, j), direction, border);
            }
        }
    }

    private void checkRow(int border, int i, int length, Direction direction){
        if(direction == Direction.LEFT) {
            for (int j = 0; j < length; j++) {
                moveIfPossible(border, i, j, direction);
            }
        }else{
            for (int j = (board.getMatrix().length - 1 ); j >= 0; j--) {
                moveIfPossible(border, i, j, direction);
            }
        }
    }

    private Tile fetchCurrentTile(Board board, TilePosition tilePosition, int border, boolean isVertical) {
        if(isVertical) {
            return board.getMatrix()[border][tilePosition.getCol()];
        } else return board.getMatrix()[tilePosition.getRow()][border];
    }

    private Tile fetchTargetTile(Board board, TilePosition tilePosition) {
        return board.getMatrix()[tilePosition.getRow()][tilePosition.getCol()];
    }

    private int changeBorder(Direction targetDirection, int border, Direction direction) {
        if (targetDirection.equals(direction)) {
            border--;
        } else {
            border++;
        }
        return border;
    }

    private TilePosition createTilePositionForMovement(int row, int col){
        return new TilePosition(row, col);
    }

    private boolean checkTileValue(Tile currentTile, Tile targetTile) {
        return currentTile.getValue() == 0 || currentTile.getValue() == targetTile.getValue();
    }

    private boolean checkIfTilesCanBeMerged(TilePosition tilePosition, Direction direction, int border, boolean isVertical) {
        if(isVertical) {
            return tilePosition.getRow() > border || Direction.DOWN.equals(direction) && (tilePosition.getCol() < border);
        } else return tilePosition.getCol() > border || Direction.RIGHT.equals(direction) && (tilePosition.getCol() < border);
    }

    private void mergeTiles(Tile currentTile, Tile targetTile) {
        int score = currentTile.getValue() + targetTile.getValue();

        currentTile.setValue(score);
        currentTile.setUsed(true);
        targetTile.setValue(0);
        targetTile.setUsed(false);
    }
}
