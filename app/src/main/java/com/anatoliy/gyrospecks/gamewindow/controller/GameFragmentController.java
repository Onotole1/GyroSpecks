package com.anatoliy.gyrospecks.gamewindow.controller;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;

import com.anatoliy.gyrospecks.R;
import com.anatoliy.gyrospecks.base.controller.BaseFragmentController;
import com.anatoliy.gyrospecks.base.view.MainActivity;
import com.anatoliy.gyrospecks.gamewindow.view.GameFragment;
import com.anatoliy.gyrospecks.model.Cell;
import com.anatoliy.gyrospecks.model.Position;
import com.anatoliy.gyrospecks.utils.StopWatch;

import java.util.HashSet;

/**
 * Date: 29.05.2017
 * Time: 20:40
 *
 * @author Anatoliy
 */

@SuppressWarnings("deprecation")
public class GameFragmentController extends BaseFragmentController {
    private final static String GAME_FRAGMENT_CONTROLLER
            = "com.anatoliy.gyrospecks.gamewindow.controller.GameFragmentController";
    private final static int NUMBER_CELLS_BY_X = 4;
    private final static int NUMBER_CELLS_BY_Y = 4;
    private final static int NUMBER_CELLS = NUMBER_CELLS_BY_X * NUMBER_CELLS_BY_Y;
    private final static String BUTTON_TAG = "button%s_%s";
    private final static String STATE_IS_PAUSED = GAME_FRAGMENT_CONTROLLER + "stateIsPaused";
    private final static String STATE_SECONDS = GAME_FRAGMENT_CONTROLLER + "stateSeconds";

    private View rootView;
    private TextView textViewStopwatch;
    private int seconds;
    private boolean isPaused;

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

    private final GameWindowBroadcastReceiver gameWindowBroadcastReceiver
            = new GameWindowBroadcastReceiver();

    private HashSet<Cell> board = new HashSet<>(NUMBER_CELLS);
    private final HashSet<Cell> winBoard = new HashSet<>(NUMBER_CELLS);

    private Cell emptyCell = initEmptyCell();

    public GameFragmentController(final GameFragment gameFragment) {
        this.gameFragment = gameFragment;
    }

    @Override
    public void updateOnCreateView(final View view) {
        rootView = view;
        textViewStopwatch = (TextView) view.findViewById(R.id.activity_main_textView_stopwatch);
        newGame();
    }

    @Override
    public void updateOnSaveInstanceState(@NonNull final Bundle outState) {

    }

    @Override
    public void updateOnRestoreInstanceState(@NonNull final Bundle savedInstanceState) {

    }

    @Override
    public void updateOnResume() {
        subscribe();
        resumeGame();
        StateService.startRestoreState(gameFragment.getActivity());
    }

    @Override
    public void updateOnPause() {
        saveState();
        pauseGame();
        unsubscribe();
    }

    public boolean isPaused() {
        return isPaused;
    }

    private void initWinBoard() {
        if (winBoard.isEmpty()) {
            for (int i = 0, winBoardArrayLength = winBoardArray.length; i < winBoardArrayLength
                    ; i++) {
                final Cell cell = winBoardArray[i];
                winBoard.add(cell);
            }
        }
    }

    void updateOnRestoreState(@NonNull final HashSet<Cell> board) {
        this.board = board;

        for (final Cell cell:board) {
            if (cell.getNumber() == 0) {
                emptyCell = cell;
            }
        }

        drawBoard();

        final SharedPreferences sharedPreferences
                = gameFragment.getActivity()
                .getSharedPreferences(GAME_FRAGMENT_CONTROLLER, Context.MODE_PRIVATE);

        seconds = sharedPreferences.getInt(STATE_SECONDS, 0);
        textViewStopwatch.setText(StopWatch.formatTime(seconds));

        isPaused = sharedPreferences.getBoolean(STATE_IS_PAUSED, false);
        if (isPaused) {
            pauseGame();
        } else {
            resumeGame();
        }
    }

    private void saveState() {
        final Activity activity = gameFragment.getActivity();
        final SharedPreferences sharedPreferences
                = activity.getSharedPreferences(GAME_FRAGMENT_CONTROLLER, Context.MODE_PRIVATE);
        final SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putBoolean(STATE_IS_PAUSED, isPaused);
        edit.putInt(STATE_SECONDS, seconds);
        edit.apply();

        StateService.startSaveState(activity, board);
    }

    private Cell initEmptyCell() {
        return new Cell(0
                , new Position((int) (Math.random() * (NUMBER_CELLS_BY_X))
                , (int) (Math.random() * (NUMBER_CELLS_BY_Y))));
    }

    private void newGame() {
        initWinBoard();
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

    public void resumeGame() {
        gameWindowBroadcastReceiver.addObserver(this);
        GameWindowService.start(gameFragment.getActivity()
                , GameWindowService.getActionStart());

        isPaused = false;
    }

    private void subscribe() {
        gameWindowBroadcastReceiver.addObserver(this);
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(GameWindowBroadcastReceiver.getSensorValuesKey());
        intentFilter.addAction(GameWindowBroadcastReceiver.getRestoreStateKey());
        LocalBroadcastManager.getInstance(gameFragment.getActivity())
                .registerReceiver(gameWindowBroadcastReceiver, intentFilter);
    }

    public void pauseGame() {
        gameWindowBroadcastReceiver.removeObserver(this);
        GameWindowService.start(gameFragment.getActivity()
                , GameWindowService.getActionStop());

        isPaused = true;
    }

    private void unsubscribe() {
        gameWindowBroadcastReceiver.removeObserver(this);
        LocalBroadcastManager.getInstance(gameFragment.getActivity())
                .unregisterReceiver(gameWindowBroadcastReceiver);
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
            final Activity activity = gameFragment.getActivity();
            if (activity instanceof MainActivity) {
                ((MainActivity) activity).notifyOnWin(StopWatch.formatTime(seconds));
            }
        }
    }

    private void drawCell(final Cell cell) {
        final Position cellPosition = cell.getPosition();

        final TableLayout tableLayout = (TableLayout) rootView.findViewById(R.id.activity_main_table);

        final Button button = (Button) tableLayout.findViewWithTag(String.format(BUTTON_TAG
                , cellPosition.getX(), cellPosition.getY()));

        if (0 == cell.getNumber()) {
            button.setBackgroundColor(gameFragment.getResources().getColor(R.color.colorEmptyCell));
            button.setText("");
        } else {
            button.setText(String.valueOf(cell.getNumber()));
            button.setBackgroundDrawable(gameFragment.getResources().getDrawable(R.drawable.ic_cell));
        }
    }

    void secondPassed() {
        seconds++;
        textViewStopwatch.setText(StopWatch.formatTime(seconds));
    }

    public void restartGame() {
        board.clear();
        emptyCell = initEmptyCell();
        newGame();
        seconds = 0;
        textViewStopwatch.setText(StopWatch.formatTime(seconds));
    }
}
