package com.zanthia.minimax;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.zanthia.minimax.algorithm.GameController;
import com.zanthia.minimax.minimax.R;

/**
 * View with game field
 *
 * Created by Zanthia on 14/02/2015.
 */
public class GameView extends View {
    private static final int DEFAULT_SQUARE_SIZE = 200;
    public static final int PADDING_SIZE = 4;
    private static final String DEFAULT_NUMBER_OF_COLUMNS = "4";
    private static final String DEFAULT_NUMBER_OF_ROWS = "4";

    private int startX;
    private int startY;

    private int numberOfRows;
    private int numberOfColumns;
    private int squareSize;
    private boolean computerFirst;

    private int numberOfSelectedSquares;
    private FieldState[][] fieldStates;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initGameFieldAttributes();
    }

    public void initGameFieldAttributes() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        numberOfColumns = Integer.parseInt(sharedPrefs.getString("field_size", DEFAULT_NUMBER_OF_COLUMNS));
        numberOfRows = Integer.parseInt(sharedPrefs.getString("field_size", DEFAULT_NUMBER_OF_ROWS));
        computerFirst = sharedPrefs.getBoolean("computer_first", false);
        DisplayMetrics metrics = getRootView().getResources().getDisplayMetrics();
        numberOfSelectedSquares = 0;
        squareSize = calculateSquareSize(DEFAULT_SQUARE_SIZE, metrics.widthPixels, metrics.heightPixels, numberOfColumns, numberOfRows);
        startX = calculateStartCoordinate(metrics.widthPixels, squareSize, numberOfColumns);
        startY = calculateStartCoordinate(metrics.heightPixels, squareSize, numberOfRows) - squareSize;
        fieldStates = initFieldStates(numberOfRows, numberOfColumns);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j < numberOfColumns; j++) {
                int squareColor = getResources().getColor(getColorId(fieldStates, i, j));
                paint.setColor(squareColor);
                canvas.drawRect(startX + i * squareSize + PADDING_SIZE,
                        startY + j * squareSize + PADDING_SIZE,
                        startX + squareSize + i * squareSize,
                        startY + squareSize + j * squareSize, paint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (MotionEvent.ACTION_DOWN == event.getAction()) {
            selectSquare((int) event.getX(), (int) event.getY());
            Button endTurnButton = (Button) getRootView().findViewById(R.id.end_turn_button);
            if (numberOfSelectedSquares >= GameController.MIN_SELECTION) {
                endTurnButton.setVisibility(View.VISIBLE);
            } else {
                endTurnButton.setVisibility(View.GONE);
            }
            invalidate();
        }
        return true;
    }

    private void selectSquare(int x, int y) {
        int squareNumberHorizontal = calculateSquareNumber(x, startX, squareSize);
        int squareNumberVertical = calculateSquareNumber(y, startY, squareSize);
        if (squareNumberHorizontal < numberOfColumns && squareNumberHorizontal >= 0
                && squareNumberVertical < numberOfRows && squareNumberVertical >= 0) {
            FieldState fieldState = fieldStates[squareNumberHorizontal][squareNumberVertical];
            if (fieldState == FieldState.BLANK && numberOfSelectedSquares < GameController.MAX_SELECTION) {
                numberOfSelectedSquares++;
                fieldStates[squareNumberHorizontal][squareNumberVertical] = FieldState.SELECTED;
            } else if (fieldState == FieldState.SELECTED) {
                numberOfSelectedSquares--;
                fieldStates[squareNumberHorizontal][squareNumberVertical] = FieldState.BLANK;
            }
        }
    }

    protected int calculateStartCoordinate(int displayMetric, int squareSize, int numberOfSquares) {
        return (displayMetric - (getGameFieldSize(squareSize, numberOfSquares))) / 2 + PADDING_SIZE;
    }

    protected int getGameFieldSize(int squareSize, int numberOfSquares) {
        return numberOfSquares * squareSize + (numberOfSquares * PADDING_SIZE + PADDING_SIZE);
    }

    protected FieldState[][] initFieldStates(int numberOfRows, int numberOfColumns) {
        FieldState[][] fieldStates = new FieldState[numberOfRows][numberOfColumns];
        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j < numberOfColumns; j++) {
                fieldStates[i][j] = FieldState.BLANK;
            }
        }
        return fieldStates;
    }

    protected int calculateSquareNumber(int coordinate, int startCoordinate, int squareSize) {
        return (coordinate - startCoordinate) / (squareSize + PADDING_SIZE);
    }

    protected int getColorId(FieldState[][] fieldStates, int i, int j) {
        return (fieldStates[i][j] == FieldState.BLANK) ? R.color.orange_yellow : (fieldStates[i][j] == FieldState.SELECTED) ? R.color.light_blue : R.color.blue_violet;
    }

    protected int calculateSquareSize(int squareSize, int widthPixels, int heightPixels, int numberOfColumns, int numberOfRows) {
        int gameFieldSizeHorizontal = getGameFieldSize(squareSize, numberOfColumns);
        int gameFieldSizeVertical = getGameFieldSize(squareSize, numberOfRows);
        if (widthPixels < gameFieldSizeHorizontal || heightPixels < gameFieldSizeVertical) {
            return calculateSquareSize(squareSize / 2, widthPixels, heightPixels, numberOfColumns, numberOfRows);
        }
        return squareSize;
    }

    public int getNumberOfColumns() {
        return numberOfColumns;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public void setNumberOfSelectedSquares(int numberOfSelectedSquares) {
        this.numberOfSelectedSquares = numberOfSelectedSquares;
    }

    public FieldState[][] getFieldStates() {
        return fieldStates;
    }

    public boolean isComputerFirst() {
        return computerFirst;
    }


}
