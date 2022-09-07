package com.example.project1_mehulkri;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class GridFunctions {

    private static final int COLUMN_COUNT = 8;

    public GridFunctions() {

    }

    public static int coordinateToIndex(int row, int col) {

        return row*8+col;
    }

    public static Coordinate indexToCoordinate(int n) {
        int i = n/COLUMN_COUNT;  // row
        int j = n%COLUMN_COUNT;  // column
        return new Coordinate(i, j);
    }

    public static boolean didWin(boolean[] isVisited, HashSet<Integer> indices) {
        for(int index : indices) {
            if(isVisited[index]) {
                return false;
            }
        }
        return true;
    }


}
