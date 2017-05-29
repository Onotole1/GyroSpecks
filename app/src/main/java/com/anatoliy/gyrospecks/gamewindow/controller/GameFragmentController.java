package com.anatoliy.gyrospecks.gamewindow.controller;

import android.app.Activity;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;

import com.anatoliy.gyrospecks.R;
import com.anatoliy.gyrospecks.base.controller.BaseFragmentController;
import com.anatoliy.gyrospecks.gamewindow.view.GameFragment;
import com.anatoliy.gyrospecks.model.Cell;
import com.anatoliy.gyrospecks.model.Position;

import java.util.HashSet;

/**
 * Date: 29.05.2017
 * Time: 20:40
 *
 * @author Anatoliy
 */

public class GameFragmentController extends BaseFragmentController {
    private final static int NUMBER_CELLS_BY_X = 4;
    private final static int NUMBER_CELLS_BY_Y = 4;
    private final static int NUMBER_CELLS = NUMBER_CELLS_BY_X * NUMBER_CELLS_BY_Y;
    private final static String BUTTON_TAG = "button%s_%s";

    private final static Cell[] winBoardArray = {
            new Cell(0, new Position(3, 3)),
            new Cell(1, new Position(0, 0)),
            new Cell(2, new Position(0, 1)),
            new Cell(3, new Position(0, 2)),
            new Cell(4, new Position(0, 3)),
            new Cell(5, new Position(1, 0)),
            new Cell(6, new Position(1, 1)),
            new Cell(7, new Position(1, 2)),
            new Cell(8, new Position(1, 3)),
            new Cell(9, new Position(2, 0)),
            new Cell(10, new Position(2, 1)),
            new Cell(11, new Position(2, 2)),
            new Cell(12, new Position(2, 3)),
            new Cell(13, new Position(3, 0)),
            new Cell(14, new Position(3, 1)),
            new Cell(15, new Position(3, 2))
    };

    private final GameFragment gameFragment;

    private final SensorValuesBroadcastReceiver sensorValuesBroadcastReceiver
            = new SensorValuesBroadcastReceiver();

    private final HashSet<Cell> board = new HashSet<>(NUMBER_CELLS);
    private final HashSet<Cell> winBoard = new HashSet<>(NUMBER_CELLS);

    private final Cell emptyCell
            = new Cell(0
            , new Position((int) (Math.random() * (NUMBER_CELLS_BY_X))
            , (int) (Math.random() * (NUMBER_CELLS_BY_Y))));

    public GameFragmentController(final GameFragment gameFragment) {
        this.gameFragment = gameFragment;
    }

    @Override
    public void updateOnCreateView(final View view) {

    }

    @Override
    public void updateOnSaveInstanceState(@NonNull final Bundle outState) {

    }

    @Override
    public void updateOnRestoreInstanceState(@NonNull final Bundle savedInstanceState) {

    }

    @Override
    public void updateOnResume() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SensorValuesBroadcastReceiver.getSensorValuesKey());
        LocalBroadcastManager.getInstance(gameFragment.getActivity())
                .registerReceiver(sensorValuesBroadcastReceiver, intentFilter);

        sensorValuesBroadcastReceiver.addObserver(this);

        SensorAlarmListenerService.start(gameFragment.getActivity()
                , SensorAlarmListenerService.getActionStart());
    }

    @Override
    public void updateOnPause() {
        LocalBroadcastManager.getInstance(gameFragment.getActivity())
                .unregisterReceiver(sensorValuesBroadcastReceiver);
        sensorValuesBroadcastReceiver.removeObserver(this);
    }

    private void initWinBoard() {
        for (int i = 0, winBoardArrayLength = winBoardArray.length; i < winBoardArrayLength; i++) {
            final Cell cell = winBoardArray[i];
            winBoard.add(cell);
        }

    }

    private void newGame() {
        fillBoard();
    }

    private void drawBoard() {
        for (final Cell cell : board) {
            drawCell(cell);
        }
    }

    private void fillBoard() {
        board.add(emptyCell);

        for (int i = 1; i < NUMBER_CELLS; i++) {
            Cell newCell = null;

            do {
                boolean hasSamePosition = false;

                final int randomPositionX = (int) (Math.random() * (NUMBER_CELLS_BY_X));
                final int randomPositionY = (int) (Math.random() * (NUMBER_CELLS_BY_Y));

                final Position position = new Position(randomPositionX, randomPositionY);

                for (final Cell next : board) {
                    if (next.getPosition().equals(position)) {
                        hasSamePosition = true;
                        break;
                    }
                }

                if (!hasSamePosition) {
                    newCell = new Cell(i, position);
                    board.add(newCell);
                }
            } while (null == newCell);
        }

        drawBoard();
    }

    void step(final int step) {
        if (Steps.LEFT == step) {
            leftStep();
        } else if (Steps.RIGHT == step) {
            rightStep();
        } else if (Steps.UP == step) {
            topStep();
        } else if (Steps.BOTTOM == step) {
            bottomStep();
        }

        drawBoard();
        checkWin();
    }

    private void bottomStep() {
        final Position emptyCellPosition = emptyCell.getPosition();

        final int emptyCellPositionX = emptyCellPosition.getX();
        final int emptyCellPositionY = emptyCellPosition.getY();

        final int leftCellPositionX = emptyCellPositionX - 1;

        if (leftCellPositionX >= 0) {
            final Position leftCellPosition = new Position(leftCellPositionX, emptyCellPositionY);
            final Cell leftCell = getCellByPosition(leftCellPosition);
            if (null != leftCell) {
                leftCell.setPosition(emptyCellPosition);
                emptyCell.setPosition(leftCellPosition);
            }
        }
    }

    private void topStep() {
        final Position emptyCellPosition = emptyCell.getPosition();

        final int emptyCellPositionX = emptyCellPosition.getX();
        final int emptyCellPositionY = emptyCellPosition.getY();

        final int rightCellPositionX = emptyCellPositionX + 1;

        if (rightCellPositionX < NUMBER_CELLS_BY_X) {
            final Position rightCellPosition = new Position(rightCellPositionX, emptyCellPositionY);
            final Cell leftCell = getCellByPosition(rightCellPosition);
            if (null != leftCell) {
                leftCell.setPosition(emptyCellPosition);
                emptyCell.setPosition(rightCellPosition);
            }
        }
    }

    private void leftStep() {
        final Position emptyCellPosition = emptyCell.getPosition();

        final int emptyCellPositionX = emptyCellPosition.getX();
        final int emptyCellPositionY = emptyCellPosition.getY();

        final int bottomCellPositionY = emptyCellPositionY + 1;

        if (bottomCellPositionY < NUMBER_CELLS_BY_Y) {
            final Position bottomCellPosition = new Position(emptyCellPositionX, bottomCellPositionY);
            final Cell bottomCell = getCellByPosition(bottomCellPosition);
            if (null != bottomCell) {
                bottomCell.setPosition(emptyCellPosition);
                emptyCell.setPosition(bottomCellPosition);
            }
        }
    }

    private void rightStep() {
        final Position emptyCellPosition = emptyCell.getPosition();

        final int emptyCellPositionX = emptyCellPosition.getX();
        final int emptyCellPositionY = emptyCellPosition.getY();

        final int topCellPositionY = emptyCellPositionY - 1;

        if (topCellPositionY >= 0) {
            final Position topCellPosition = new Position(emptyCellPositionX, topCellPositionY);
            final Cell topCell = getCellByPosition(topCellPosition);
            if (null != topCell) {
                topCell.setPosition(emptyCellPosition);
                emptyCell.setPosition(topCellPosition);
            }
        }
    }

    private Cell getCellByPosition(@NonNull final Position position) {
        for (final Cell next : board) {
            if (next.getPosition().equals(position)) {
                return next;
            }
        }

        return null;
    }

    private void checkWin() {

        if (board.equals(winBoard)) {

        }
    }

    private void drawCell(final Cell cell) {
        final Position cellPosition = cell.getPosition();

        final Activity activity = gameFragment.getActivity();

        final TableLayout tableLayout
                = (TableLayout) activity.findViewById(R.id.activity_main_table);
        final Button button = (Button) tableLayout.findViewWithTag(String.format(BUTTON_TAG
                , cellPosition.getX(), cellPosition.getY()));

        if (0 == cell.getNumber()) {
            button.setEnabled(false);
            button.setBackgroundColor(activity.getResources().getColor(R.color.colorEmptyCell));
            button.setText("");
        } else {
            button.setText(String.valueOf(cell.getNumber()));
            button.setEnabled(true);
            button.setBackgroundColor(activity.getResources().getColor(R.color.colorFillCell));
        }
    }
}
