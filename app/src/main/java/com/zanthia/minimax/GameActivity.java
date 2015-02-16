package com.zanthia.minimax;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zanthia.minimax.algorithm.GameController;
import com.zanthia.minimax.algorithm.Tree;
import com.zanthia.minimax.minimax.R;

/**
 * Main activity class
 */
public class GameActivity extends Activity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private GameController gameController;
    private GameView gameView;
    private TextView gameResult;
    private Button endTurnButton;
    private Tree<Integer> gameTree;
    private Tree<Integer> currentMoveTree;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
        gameView = (GameView) findViewById(R.id.game_field);
        gameResult = (TextView) findViewById(R.id.game_result);
        gameController = new GameController(gameView.getNumberOfRows() * gameView.getNumberOfColumns());
        gameTree = gameController.calculateTreeHeuristicValues(gameController.generateTree());
        if (gameView.isComputerFirst()) {
            makeMove(gameView.getFieldStates());
            gameView.invalidate();
        }
        initButtons();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_game:
                startNewGame();
                return true;
            case R.id.settings:
                openSettings();
                return true;
            case R.id.help:
                showHelp();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        startNewGame();
    }

    private void initButtons() {
        endTurnButton = (Button) findViewById(R.id.end_turn_button);
        endTurnButton.setTextColor(Color.WHITE);
        endTurnButton.setVisibility(View.GONE);
        endTurnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endTurnButton.setVisibility(View.GONE);
                FieldState[][] fieldStates = gameView.getFieldStates();
                changeFieldStates(fieldStates);
                gameView.setNumberOfSelectedSquares(0);
                boolean gameEnded = isGameEnded(fieldStates);
                if (!gameEnded) {
                    makeMove(gameView.getFieldStates());
                } else {
                    gameResult.setText(R.string.human_won);
                }
                gameView.invalidate();
            }
        });
    }

    private void openSettings() {
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new GamePreferenceFragment())
                .addToBackStack("settings")
                .commit();
    }

    private void showHelp() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.help);
        dialog.setTitle(R.string.game_rules);
        dialog.setCancelable(true);
        dialog.show();
    }

    private void startNewGame() {
        endTurnButton.setVisibility(View.GONE);
        currentMoveTree = null;
        gameResult.setText("");
        gameView.initGameFieldAttributes();
        int currentFieldSize = gameView.getNumberOfRows() * gameView.getNumberOfColumns();
        if (gameController == null || gameController.getFieldSize() != currentFieldSize) {
            gameController = new GameController(currentFieldSize);
            gameTree = gameController.calculateTreeHeuristicValues(gameController.generateTree());
        }
        if (gameView.isComputerFirst()) {
            makeMove(gameView.getFieldStates());
        }
        gameView.invalidate();
    }

    /**
     * Makes game move
     * @param fieldStates game field state
     */
    private void makeMove(FieldState[][] fieldStates) {
        int numberOfFilledFieldStates = getNumberOfFilledFieldStates(fieldStates);
        Tree<Integer> currentTreeAfterHumanMove = getCurrentTreeAfterHumanMove(currentMoveTree != null ? currentMoveTree : gameTree, numberOfFilledFieldStates);
        currentMoveTree = getNewCurrentMoveTree(currentTreeAfterHumanMove);
        int shouldFill = currentMoveTree.getData() - currentTreeAfterHumanMove.getData();
        fillFieldStates(shouldFill, fieldStates);
        if (isGameEnded(fieldStates)) {
            gameResult.setText(R.string.computer_won);
        }
    }

    /**
     * Changes square states from Selected to Filled
     * @param fieldStates game field state
     */
    protected void changeFieldStates(FieldState[][] fieldStates) {
        for (int i = 0; i < fieldStates.length; i++) {
            for (int j = 0; j < fieldStates[i].length; j++) {
                if (fieldStates[i][j] == FieldState.SELECTED) {
                    fieldStates[i][j] = FieldState.FILLED;
                }
            }
        }
    }

    /**
     * Returns number of filled squares in game field
     * @param fieldStates game field state
     * @return number of filled squares in game field
     */
    protected int getNumberOfFilledFieldStates(FieldState[][] fieldStates) {
        int numberOfFilledFieldStates = 0;
        for (int i = 0; i < fieldStates.length; i++) {
            for (int j = 0; j < fieldStates[i].length; j++) {
                if (fieldStates[i][j] == FieldState.FILLED) {
                    numberOfFilledFieldStates++;
                }
            }
        }
        return numberOfFilledFieldStates;
    }

    protected Tree<Integer> getCurrentTreeAfterHumanMove(Tree<Integer> currentGameTree, int numberOfFilledFieldStates) {
        for (Tree<Integer> tree : currentGameTree.getChildren()) {
            if (tree.getData() == numberOfFilledFieldStates) {
                return tree;
            }
        }
        return currentGameTree;
    }

    /**
     * Returns tree for next move
     * @param currentMoveTree tree for current move
     * @return tree for next move
     */
    protected Tree<Integer> getNewCurrentMoveTree(Tree<Integer> currentMoveTree) {
        for (Tree<Integer> tree : currentMoveTree.getChildren()) {
            if (tree.getHeuristicValue() == getPlayerHeuristicValue()) {
                return tree;
            }
        }
        return currentMoveTree.getChildren().iterator().next();
    }

    protected int getPlayerHeuristicValue() {
        return gameView.isComputerFirst() ? 1 : 0;
    }

    protected boolean isGameEnded(FieldState[][] fieldStates) {
        int numberOfBlankSquares = 0;
        for (int i = 0; i < fieldStates.length; i++) {
            for (int j = 0; j < fieldStates[i].length; j++) {
                if (fieldStates[i][j] == FieldState.BLANK) {
                    numberOfBlankSquares++;
                }
            }
        }
        return numberOfBlankSquares < GameController.MIN_SELECTION;
    }

    /**
     * Change squares state from Blank to Filled
     * @param numberOfSelectedSquares number of squares need to be filled
     * @param fieldStates game field states
     */
    protected void fillFieldStates(int numberOfSelectedSquares, FieldState[][] fieldStates) {
        for (int i = 0; i < fieldStates.length; i++) {
            for (int j = 0; j < fieldStates[i].length; j++) {
                if (fieldStates[i][j] == FieldState.BLANK) {
                    fieldStates[i][j] = FieldState.FILLED;
                    numberOfSelectedSquares--;
                }
                if (numberOfSelectedSquares <= 0) {
                    return;
                }
            }
        }
    }
}
