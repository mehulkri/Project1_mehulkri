package com.example.project1_mehulkri;


import java.util.HashSet;

public class GridFunctions {

    private static final int COLUMN_COUNT = 8;
    private static final int ROW_COUNT = 10;
    private static final int winningNum = COLUMN_COUNT*ROW_COUNT - 4;

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

    public static boolean didWin(boolean[] isVisited, HashSet<Integer> indices, int visits) {
        for(int index : indices) {
            if(isVisited[index]) {
                return false;
            }
        }
        if(visits == winningNum) {
            return true;
        } else {
            return false;
        }
    }

    public static int numVisited(boolean[] isVisited) {
        int i = 0;
        for(boolean visit: isVisited) {
            if(visit) {
                i++;
            }
        }
        return i;
    }


}
