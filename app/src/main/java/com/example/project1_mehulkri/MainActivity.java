package com.example.project1_mehulkri;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final int COLUMN_COUNT = 8;
    private static final int ROW_COUNT = 10;

    // save the TextViews of all cells in an array, so later on,
    // when a TextView is clicked, we know which cell it is
    private ArrayList<TextView> cell_tvs;
    private boolean[] isSquareAMine;
    private int[] adjacentMines;

    // And arrayList that keeps track of which s

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        cell_tvs = new ArrayList<>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GridLayout grid = (GridLayout) findViewById(R.id.board);
        LayoutInflater li = LayoutInflater.from(this);
        for (int i = 0; i<=9; i++) {
            for (int j=0; j<=7; j++) {
                TextView tv = (TextView) li.inflate(R.layout.custom_cell_layout, grid, false);
                tv.setTextColor(Color.GRAY);
                tv.setBackgroundColor(Color.GRAY);
                tv.setOnClickListener(this::onClickTV);

                GridLayout.LayoutParams lp = (GridLayout.LayoutParams) tv.getLayoutParams();
                lp.rowSpec = GridLayout.spec(i);
                lp.columnSpec = GridLayout.spec(j);

                grid.addView(tv, lp);

                cell_tvs.add(tv);
            }
        }
        initializeMines(cell_tvs.size());

    }

    public void onClickTV(View view){
        TextView tv = (TextView) view;
        int n = findIndexOfCellTextView(tv);
        tv.setText(String.valueOf(adjacentMines[n]));
        if (tv.getCurrentTextColor() == Color.GRAY) {
            tv.setTextColor(Color.GREEN);
            tv.setBackgroundColor(Color.parseColor("lime"));
        }else {
            tv.setTextColor(Color.GRAY);
            tv.setBackgroundColor(Color.LTGRAY);
        }
    }

    private int findIndexOfCellTextView(TextView tv) {
        for (int n=0; n<cell_tvs.size(); n++) {
            if (cell_tvs.get(n) == tv)
                return n;
        }
        return -1;
    }

    private void initializeMines(int size) {
        isSquareAMine = new boolean[size];
        adjacentMines = new int[size];
        for(int i=0; i < size; i++) {
            isSquareAMine[i] = false;
            adjacentMines[i] = 0;
        }
        HashSet<Integer> randomIndices = new HashSet<>();
        Random rand = new Random();
        while(randomIndices.size() < 4) {
            randomIndices.add(rand.nextInt(size));
        }
        randomIndices.forEach(index -> {
            isSquareAMine[index] = true;
            adjacentMines[index] = -1;
        });
        try {
            adjustSquaresAdjacentToMines(randomIndices);
        } catch (Exception e) {
            String l = e.toString();
        }


    }

    private TextView findTextViewBasedOnRowAndColumn(int row, int col) {
        int index = coordinateToIndex(row, col);
        return cell_tvs.get(index);
    }

    private int coordinateToIndex(int row, int col) {
        return row*8+col;
    }

    private Coordinate indexToCoordinate(int n) {
        int i = n/COLUMN_COUNT;  // row
        int j = n%COLUMN_COUNT;  // column
        return new Coordinate(i, j);
    }

    private void adjustSquaresAdjacentToMines(HashSet<Integer> indices) {
        indices.forEach(index -> {
            Coordinate point = indexToCoordinate(index);
            // Code comes from StackOverflow: https://stackoverflow.com/questions/2035522/get-adjacent-elements-in-a-two-dimensional-array
            for (int dx = -1; dx <= 1; ++dx) {
                for (int dy = -1; dy <= 1; ++dy) {
                    if (dx != 0 || dy != 0) {
                        int row = point.getX() + dx;
                        int col = point.getY() + dy;
                        if(row < ROW_COUNT && row >= 0 && col < COLUMN_COUNT && col >= 0) {
                            int newIndex = coordinateToIndex(row, col);
                            if(adjacentMines[newIndex] != -1 && newIndex < adjacentMines.length) {
                                adjacentMines[newIndex] += 1;
                            }
                        }
                    }
                }
            }
        });
    }

}