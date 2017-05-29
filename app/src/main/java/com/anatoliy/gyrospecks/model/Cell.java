package com.anatoliy.gyrospecks.model;

/**
 * Date: 25.05.2017
 * Time: 21:50
 *
 * @author Anatoliy
 */

public class Cell {
    private int number;
    private Position position;

    public Cell(final int number, final Position position) {
        this.number = number;
        this.position = position;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(final int number) {
        this.number = number;
    }

    public void setPosition(final Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cell cell = (Cell) o;

        if (number != cell.number) return false;
        return position.equals(cell.position);

    }

    @Override
    public int hashCode() {
        int result = number;
        result = 31 * result + position.hashCode();
        return result;
    }
}
