package com.zanthia.minimax.algorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zanthia on 13/02/2015.
 */
public class Tree<T> {

    private T data;
    private Tree<T> parent;
    private List<Tree<T>> children;
    private int treeLevel;
    private Integer heuristicValue;

    public Tree(T data, int treeLevel) {
        this.data = data;
        this.treeLevel = treeLevel;
        this.children = new ArrayList<>();
    }

    public Tree<T> addChild(T child) {
        Tree<T> childNode = new Tree<>(child, treeLevel + 1);
        childNode.parent = this;
        this.children.add(childNode);
        return childNode;
    }

    public boolean isMaxLevel() {
        return treeLevel % 2 == 0;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Tree<T> getParent() {
        return parent;
    }

    public void setParent(Tree<T> parent) {
        this.parent = parent;
    }

    public List<Tree<T>> getChildren() {
        return children;
    }

    public void setChildren(List<Tree<T>> children) {
        this.children = children;
    }

    public Integer getHeuristicValue() {
        return heuristicValue;
    }

    public void setHeuristicValue(Integer heuristicValue) {
        this.heuristicValue = heuristicValue;
    }

    public int getTreeLevel() {
        return treeLevel;
    }

    public void setTreeLevel(int treeLevel) {
        this.treeLevel = treeLevel;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Tree)) return false;
        if (obj == this) return true;

        Tree<T> tree = (Tree) obj;
        if (tree.getData() != data) return false;
        if (tree.getTreeLevel() != treeLevel) return false;
        if (tree.getHeuristicValue() != heuristicValue) return false;
        if (tree.getChildren().isEmpty() && children.isEmpty()) return true;
        if (tree.getChildren().size() != children.size()) return false;
        for (int i = 0; i < children.size(); i++) {
            if (!tree.getChildren().get(i).equals(children.get(i))) {
                return false;
            }
        }
        return true;
    }

    public String toString() {
        return "Value: " + data.toString() +
                "; Tree level: " + treeLevel +
                "; Heuristic value: " + heuristicValue +
                "; Children: " + children.toString();
    }
}
