package com.zanthia.minimax.algorithm;

import junit.framework.TestCase;

public class GameControllerTest extends TestCase {

    public void testGenerateTree() {
        GameController gameController = new GameController(6);
        Tree<Integer> expectedTree = prepareTreeWithoutHeuristicValues();
        Tree<Integer> tree = gameController.generateTree();
        assertEquals("Failed to test generateTree()", expectedTree, tree);
    }

    public void testCalculateTreeHeuristicValues() {
        GameController gameController = new GameController(6);
        Tree<Integer> expectedTree = prepareTreeWithHeuristicValues();
        Tree<Integer> tree = gameController.calculateTreeHeuristicValues(gameController.generateTree());
        assertEquals("Failed to test calculateTreeHeuristicValues", expectedTree, tree);
    }

    private Tree<Integer> prepareTreeWithoutHeuristicValues(){
        Tree<Integer> tree = new Tree<>(0, 0);
        tree.addChild(3).addChild(6);
        tree.addChild(4);
        tree.addChild(5);
        tree.addChild(6);
        return tree;
    }

    private Tree<Integer> prepareTreeWithHeuristicValues(){
        Tree<Integer> tree = new Tree<>(0, 0);
        tree.setHeuristicValue(1);
        Tree<Integer> treeChild = tree.addChild(3);
        treeChild.setHeuristicValue(0);
        treeChild.addChild(6).setHeuristicValue(0);
        tree.addChild(4).setHeuristicValue(1);
        tree.addChild(5).setHeuristicValue(1);
        tree.addChild(6).setHeuristicValue(1);
        return tree;
    }
}