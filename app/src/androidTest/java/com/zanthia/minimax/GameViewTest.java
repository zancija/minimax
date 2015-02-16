package com.zanthia.minimax;

import android.content.Intent;
import android.test.ActivityUnitTestCase;

import com.zanthia.minimax.minimax.R;

import static com.zanthia.minimax.TestHelper.assertArrayEquals;

public class GameViewTest extends ActivityUnitTestCase<GameActivity> {
    private GameView gameView;

    public GameViewTest() {
        super(GameActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Intent intent = new Intent(getInstrumentation().getTargetContext(), GameActivity.class);
        GameActivity gameActivity = startActivity(intent, null, null);
        gameView = (GameView) gameActivity.findViewById(R.id.game_field);
    }

    public void testCalculateStartCoordinate(){
        assertCalculateStartCoordinate(1000, 100, 1, 450);
        assertCalculateStartCoordinate(1000, 100, 2, 398);
        assertCalculateStartCoordinate(1000, 100, 5, 242);
    }

    private void assertCalculateStartCoordinate(int displayMetric, int squareSize, int numberOfSquares, int expected){
        assertEquals(expected, gameView.calculateStartCoordinate(displayMetric, squareSize, numberOfSquares));
    }

    public void testGetGameFieldSize(){
        assertGetGameFieldSize(100, 1, 108);
        assertGetGameFieldSize(100, 2, 212);
        assertGetGameFieldSize(100, 5, 524);
    }

    private void assertGetGameFieldSize(int squareSize, int numberOfSquares, int expected){
        assertEquals(expected, gameView.getGameFieldSize(squareSize, numberOfSquares));
    }

    public void testInitFieldStates(){
        FieldState[][] expectedFieldStates = {
                { FieldState.BLANK, FieldState.BLANK},
                { FieldState.BLANK, FieldState.BLANK},
        };
        assertArrayEquals(expectedFieldStates, gameView.initFieldStates(2, 2));
    }

    public void testCalculateSquareNumber(){
        assertCalculateSquareNumber(110, 100, 100, 0);
        assertCalculateSquareNumber(220, 100, 100, 1);
        assertCalculateSquareNumber(220, 100, 50, 2);
    }

    private void assertCalculateSquareNumber(int coordinate, int startCoordinate, int squareSize, int expected){
        assertEquals(expected, gameView.calculateSquareNumber(coordinate, startCoordinate, squareSize));
    }

    public void testGetColorId(){
        FieldState[][] fieldStates = {
                { FieldState.FILLED, FieldState.BLANK, FieldState.FILLED },
                { FieldState.FILLED, FieldState.SELECTED, FieldState.BLANK },
        };
        assertGetColorId(fieldStates, 0, 0, R.color.blue_violet);
        assertGetColorId(fieldStates, 0, 1, R.color.orange_yellow);
        assertGetColorId(fieldStates, 1, 1, R.color.light_blue);
    }

    private void assertGetColorId(FieldState[][] fieldStates, int i, int j, int expected){
        assertEquals(expected, gameView.getColorId(fieldStates, i, j));
    }

    public void testCalculateSquareSize(){
        assertCalculateSquareSize(100, 1000, 1000, 1, 1, 100);
        assertCalculateSquareSize(100, 1000, 1000, 10, 1, 50);
        assertCalculateSquareSize(100, 1000, 1000, 1, 10, 50);
        assertCalculateSquareSize(100, 100, 100, 1, 1, 50);
    }

    private void assertCalculateSquareSize(int squareSize, int widthPixels, int heightPixels, int numberOfColumns, int numberOfRows, int expected){
        assertEquals(expected, gameView.calculateSquareSize(squareSize, widthPixels, heightPixels, numberOfColumns, numberOfRows));
    }
}