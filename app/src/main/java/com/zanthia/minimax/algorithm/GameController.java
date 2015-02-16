package com.zanthia.minimax.algorithm;

import java.util.List;

/**
 * Class allow to create tree with heuristic values for Minimax algorithm
 *
 * Created by Zanthia on 13/02/2015.
 */
public class GameController {
    public static final int MIN_SELECTION = 3;
    public static final int MAX_SELECTION = 6;
    private int fieldSize;

    public GameController(int fieldSize) {
        this.fieldSize = fieldSize;
    }

    /**
     * Creates game tree for defined field size
     *
     * @return game tree
     */
    public Tree<Integer> generateTree() {
        Tree<Integer> tree = new Tree(0, 0);
        generateChildren(tree);
        return tree;
    }

    /**
     * Calculates heuristic values for game tree
     *
     * @param tree game tree
     * @return game tree with heuristic values
     */

    public Tree<Integer> calculateTreeHeuristicValues(Tree<Integer> tree) {
        if (tree.getChildren().isEmpty()) {
            tree.setHeuristicValue(calculateLastNodeHeuristicValue(tree));
        } else {
            for (Tree<Integer> child : tree.getChildren()) {
                calculateTreeHeuristicValues(child);
            }
            tree.setHeuristicValue(calculateHeuristicValue(tree));
        }
        return tree;
    }

    private Tree<Integer> generateChildren(Tree<Integer> parentTree) {
        int alreadySelected = parentTree.getData();
        int moveSelected = 0;
        for (int i = MIN_SELECTION; i <= MAX_SELECTION; i++) {
            moveSelected = alreadySelected + i;
            if (moveSelected <= fieldSize) {
                generateChildren(parentTree.addChild(moveSelected));
            } else {
                return parentTree;
            }
        }
        return parentTree;
    }

    private Integer calculateHeuristicValue(Tree<Integer> tree) {
        List<Tree<Integer>> children = tree.getChildren();
        return tree.isMaxLevel() ? getMaxValue(children) : getMinValue(children);
    }

    private Integer getMinValue(List<Tree<Integer>> children) {
        int minValue = children.iterator().next().getHeuristicValue();
        for (Tree child : children) {
            Integer childHeuristicValue = child.getHeuristicValue();
            if (childHeuristicValue < minValue) {
                return childHeuristicValue;
            }
        }
        return minValue;
    }

    private Integer getMaxValue(List<Tree<Integer>> children) {
        int maxValue = children.iterator().next().getHeuristicValue();
        for (Tree child : children) {
            Integer childHeuristicValue = child.getHeuristicValue();
            if (childHeuristicValue > maxValue) {
                return childHeuristicValue;
            }
        }
        return maxValue;
    }

    private Integer calculateLastNodeHeuristicValue(Tree<Integer> tree) {
        return tree.isMaxLevel() ? 0 : 1;
    }

    public int getFieldSize() {
        return fieldSize;
    }
}
