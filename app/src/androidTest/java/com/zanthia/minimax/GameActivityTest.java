package com.zanthia.minimax;

import com.zanthia.minimax.algorithm.Tree;

import junit.framework.TestCase;

import static com.zanthia.minimax.TestHelper.assertArrayEquals;

public class GameActivityTest extends TestCase {
    private GameActivity gameActivity;

    public GameActivityTest(){
        gameActivity = new GameActivity(){
            @Override
            protected int getPlayerHeuristicValue() {
                return 1;
            }
        };
    }

    public void testChangeFieldStates(){
        FieldState[][] fieldStates = {
            { FieldState.SELECTED, FieldState.BLANK, FieldState.FILLED },
            { FieldState.SELECTED, FieldState.FILLED, FieldState.BLANK },
        };
        FieldState[][] expectedFieldStates = {
                { FieldState.FILLED, FieldState.BLANK, FieldState.FILLED },
                { FieldState.FILLED, FieldState.FILLED, FieldState.BLANK },
        };
        gameActivity.changeFieldStates(fieldStates);
        assertArrayEquals(expectedFieldStates, fieldStates);
    }

    public void testGetNumberOfFilledFieldStates(){
        FieldState[][] fieldStatesZeroFilled = {
                { FieldState.SELECTED, FieldState.BLANK},
                { FieldState.SELECTED, FieldState.BLANK },
        };
        FieldState[][] fieldStatesOneFilled = {
                { FieldState.SELECTED, FieldState.BLANK, FieldState.FILLED },
                { FieldState.SELECTED, FieldState.BLANK, FieldState.BLANK },
        };
        FieldState[][] fieldStatesThreeFilled = {
                { FieldState.SELECTED, FieldState.FILLED, FieldState.FILLED },
                { FieldState.SELECTED, FieldState.FILLED, FieldState.BLANK },
        };
        assertGetNumberOfFilledFieldStates(fieldStatesZeroFilled, 0);
        assertGetNumberOfFilledFieldStates(fieldStatesOneFilled, 1);
        assertGetNumberOfFilledFieldStates(fieldStatesThreeFilled, 3);
    }

    private void assertGetNumberOfFilledFieldStates(FieldState[][] fieldStates, int expected){
        assertEquals(expected, gameActivity.getNumberOfFilledFieldStates(fieldStates));
    }

    public void testGetCurrentTreeAfterHumanMove(){
        Tree<Integer> tree = new Tree<>(0, 0);
        tree.addChild(3).addChild(6);
        tree.addChild(4);
        tree.addChild(5);
        tree.addChild(6);
        assertGetCurrentTreeAfterHumanMove(tree, 3, new Tree<Integer>(3, 1).addChild(6));
        assertGetCurrentTreeAfterHumanMove(tree, 4, new Tree<Integer>(4, 1));
        assertGetCurrentTreeAfterHumanMove(tree, 6, new Tree<Integer>(6, 1));
    }

    private void assertGetCurrentTreeAfterHumanMove(Tree<Integer> tree, int humanMove, Tree<Integer> expected){
        gameActivity.getCurrentTreeAfterHumanMove(tree, humanMove);
    }

    public void testGetNewCurrentMoveTree(){
        Tree<Integer> tree = new Tree<>(0, 0);
        tree.addChild(3).setHeuristicValue(0);
        tree.addChild(4).setHeuristicValue(1);
        tree.addChild(5).setHeuristicValue(0);
        Tree<Integer> expectedTree = new Tree<>(4, 1);
        expectedTree.setHeuristicValue(1);
        assertEquals(expectedTree, gameActivity.getNewCurrentMoveTree(tree));
    }

    public void testIsGameEnded() {
        FieldState[][] fieldStatesZeroBlank = {
                { FieldState.SELECTED, FieldState.FILLED},
                { FieldState.SELECTED, FieldState.FILLED },
        };
        FieldState[][] fieldStatesOneBlank = {
                { FieldState.SELECTED, FieldState.BLANK, FieldState.FILLED },
                { FieldState.SELECTED, FieldState.FILLED, FieldState.FILLED },
        };
        FieldState[][] fieldStatesThreeBlank = {
                { FieldState.SELECTED, FieldState.BLANK, FieldState.BLANK },
                { FieldState.SELECTED, FieldState.FILLED, FieldState.BLANK },
        };
        assertGameEnded(fieldStatesZeroBlank, true);
        assertGameEnded(fieldStatesOneBlank, true);
        assertGameEnded(fieldStatesThreeBlank, false);
    }

    private void assertGameEnded(FieldState[][] fieldStates, boolean expected){
        assertEquals(expected, gameActivity.isGameEnded(fieldStates));
    }

    public void testFillFieldStates(){
        FieldState[][] fieldStates = {
                { FieldState.SELECTED, FieldState.BLANK, FieldState.FILLED },
                { FieldState.SELECTED, FieldState.FILLED, FieldState.BLANK },
        };
        FieldState[][] expectedFieldStates = {
                { FieldState.SELECTED, FieldState.FILLED, FieldState.FILLED },
                { FieldState.SELECTED, FieldState.FILLED, FieldState.FILLED },
        };
        gameActivity.fillFieldStates(2, fieldStates);
        assertArrayEquals(expectedFieldStates, fieldStates);
    }
}