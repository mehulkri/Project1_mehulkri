package com.example.project1_mehulkri;

import static com.example.project1_mehulkri.GridFunctions.coordinateToIndex;
import static com.example.project1_mehulkri.GridFunctions.indexToCoordinate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private static final int COLUMN_COUNT = 8;
    private static final int ROW_COUNT = 10;

    // save the TextViews of all cells in an array, so later on,
    // when a TextView is clicked, we know which cell it is
    private ArrayList<TextView> cell_tvs;
    private HashSet<Integer> randomIndices;
    private boolean[] isSquareAMine;
    private int[] adjacentMines;
    private boolean[] isVisited = new boolean[COLUMN_COUNT*ROW_COUNT];
    private boolean[] hasFlag = new boolean[COLUMN_COUNT*ROW_COUNT];
    private boolean flagMode = false;
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
        Button flag = (Button) findViewById(R.id.flagButton);
        flag.setText(R.string.pick);
        flag.setTextColor(Color.GRAY);
        flag.setOnClickListener(this::onClickFlagButton);
        flag.setBackgroundColor(Color.TRANSPARENT);
        initializeMines(cell_tvs.size());

    }

    public void onClickTV(View view) {
        TextView tv = (TextView) view;
        int n = findIndexOfCellTextView(tv);
        isVisited[n] = true;
        tv.setText(String.valueOf(adjacentMines[n]));
        // First touch
        if(flagMode) {
            if(isVisited[n]) {
                if(hasFlag[n] == false) {
                    tv.setText(R.string.flag);
                } else {
                    tv.setText("");
                }
                tv.setBackgroundColor(Color.GRAY);
            }
        } else {
            if (adjacentMines[n] == 0) {
                tv.setTextColor(Color.GRAY);
                tv.setBackgroundColor(Color.LTGRAY);
                depthFirstSearch(n);
            } else if(adjacentMines[n] == -1) {
                revealAllMines();
                finishGame();
            } else if (tv.getCurrentTextColor() == Color.GRAY) {
                tv.setTextColor(Color.GREEN);
                tv.setBackgroundColor(Color.parseColor("lime"));
            } else {
                tv.setTextColor(Color.GRAY);
                tv.setBackgroundColor(Color.LTGRAY);
            }
        }
    }

    public void onClickFlagButton(View view) {
        flagMode = !flagMode;
        Button flag = (Button) view;
        if(flagMode) {
            flag.setText(R.string.flag);
        } else {
            flag.setText(R.string.pick);
            flag.setTextColor(Color.GRAY);
        }
    }

    private void depthFirstSearch(int start) {
        dfsRecursive(start, isVisited);
    }

    private void dfsRecursive(int current, boolean[] isVisited) {
        isVisited[current] = true;
        Coordinate curr = indexToCoordinate(current);
        for (int dx = -1; dx <= 1; ++dx) {
            for (int dy = -1; dy <= 1; ++dy) {
                if (dx != 0 || dy != 0) {
                    int row = curr.getX() + dx;
                    int col = curr.getY() + dy;
                    if(row < ROW_COUNT && row >= 0 && col < COLUMN_COUNT && col >= 0) {
                        int newIndex = coordinateToIndex(row, col);
                        // Adjust the square
                        displaySquareNumber(newIndex);
                        if(adjacentMines[newIndex] == 0 && isVisited[newIndex] == false ) {
                            dfsRecursive(newIndex, isVisited);
                        }
                    }
                }
            }
        }
    }

    private void displaySquareNumber(int index) {
        TextView tv = cell_tvs.get(index);
        tv.setTextColor(Color.GRAY);
        tv.setBackgroundColor(Color.LTGRAY);
        tv.setText(String.valueOf(adjacentMines[index]));
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
            hasFlag[i] = false;
        }
        randomIndices = new HashSet<>();
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

    private void finishGame() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, ResultsPage.class );
                startActivity(intent);
            }
        }, 3000);
    }

    public void revealAllMines() {
        randomIndices.forEach(index -> {
            TextView tv = cell_tvs.get(index);
            tv.setText(R.string.mine);
        });
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