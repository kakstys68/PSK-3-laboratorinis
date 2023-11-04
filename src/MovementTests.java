import org.junit.Test;
import org.junit.jupiter.api.Nested;

import static org.mockito.Mockito.*;


public class MovementTests {
    @Nested
    class when_move {

        @Test
        public void left_executes_horizontalMove() {
            Movement yourObject = new Movement(new Board(), new Renderer());
            TilePosition tilePosition = new TilePosition(1,1);
            Direction direction = Direction.LEFT;
            int border = 3;

            Movement spy = spy(yourObject);

            spy.move(tilePosition, direction, border);

            verify(spy).horizontalMove(eq(tilePosition), eq(direction), eq(border));
        }

        @Test
        public void right_executes_horizontalMove() {
            Movement yourObject = new Movement(new Board(), new Renderer());
            TilePosition tilePosition = new TilePosition(1,1);
            Direction direction = Direction.RIGHT;
            int border = 3;

            Movement spy = spy(yourObject);

            spy.move(tilePosition, direction, border);

            verify(spy).horizontalMove(eq(tilePosition), eq(direction), eq(border));
        }

        @Test
        public void up_or_down_executes_verticalMove() {
            Movement yourObject = new Movement(new Board(), new Renderer());
            TilePosition tilePosition = new TilePosition(1,1);
            Direction direction = Direction.UP;
            int border = 3;

            Movement spy = spy(yourObject);

            spy.move(tilePosition, direction, border);

            verify(spy).verticalMove(eq(tilePosition), eq(direction), eq(border));
        }
    }

    @Nested
    class when_vertical_move {
        @Test
        public void and_all_conditions_are_met_calls_mergeTiles() {
            Movement movement = new Movement(new Board(), new Renderer()); // Replace with your actual class
            TilePosition tilePosition = new TilePosition(1,1); // Create a valid TilePosition
            Direction direction = Direction.DOWN; // Replace with the desired direction
            int border = 3; // Replace with your desired border value

            Board board = mock(Board.class);
            Tile currentTile = mock(Tile.class);
            Tile targetTile = mock(Tile.class);

            when(movement.fetchCurrentTile(board, tilePosition, border, true)).thenReturn(currentTile);
            when(movement.fetchTargetTile(board, tilePosition)).thenReturn(targetTile);
            when(movement.checkTileValue(currentTile, targetTile)).thenReturn(true);
            when(movement.checkIfTilesCanBeMerged(tilePosition, direction, border, true)).thenReturn(true);

            movement.verticalMove(tilePosition, direction, border);

            verify(movement).mergeTiles(currentTile, targetTile);
        }

        @Test
        public void and_one_condition_is_met_calls_recursively() {
            Movement movement = new Movement(new Board(), new Renderer());
            TilePosition tilePosition = new TilePosition();
            Direction direction = Direction.DOWN;
            int border = 5;

            Board board = mock(Board.class);
            Tile currentTile = mock(Tile.class);
            Tile targetTile = mock(Tile.class);

            when(movement.fetchCurrentTile(board, tilePosition, border, true)).thenReturn(currentTile);
            when(movement.fetchTargetTile(board, tilePosition)).thenReturn(targetTile);
            when(movement.checkTileValue(currentTile, targetTile)).thenReturn(true);
            when(movement.checkIfTilesCanBeMerged(tilePosition, direction, border, true)).thenReturn(true);

            movement.verticalMove(tilePosition, direction, border);

            verify(movement).verticalMove(tilePosition, direction, border);
        }

        @Test
        public void and_none_conditions_are_met_calls_recursively() {
            Movement movement = new Movement(new Board(), new Renderer());
            TilePosition tilePosition = new TilePosition();
            Direction direction = Direction.DOWN;
            int border = 5;

            Board board = mock(Board.class);
            Tile currentTile = mock(Tile.class);
            Tile targetTile = mock(Tile.class);

            when(movement.fetchCurrentTile(board, tilePosition, border, true)).thenReturn(currentTile);
            when(movement.fetchTargetTile(board, tilePosition)).thenReturn(targetTile);
            when(movement.checkTileValue(currentTile, targetTile)).thenReturn(false);
            when(movement.checkIfTilesCanBeMerged(tilePosition, direction, border, true)).thenReturn(false);

            movement.verticalMove(tilePosition, direction, border);

            verify(movement).verticalMove(tilePosition, direction, border);
        }
    }
}
