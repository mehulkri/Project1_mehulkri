package com.example.project1_mehulkri;

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



}
