package io.benjamindiaz.sudokufx.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Sudoku implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7274050187283218257L;
	private Board fullBoard;
	private Board diggedBoard;
	private ArrayList<Position> editablePositions;

	public Sudoku(Board fullBoard, Board diggedBoard) {
		super();
		this.fullBoard = fullBoard;
		this.diggedBoard = diggedBoard;
		initEditablePositions();
	}

	private void initEditablePositions() {
		editablePositions = new ArrayList<>();
		ArrayList<Position> fullBoardPositions = fullBoard.getPositions();
		for (int i = 0; i < Board.SIZE * Board.SIZE; i++) {
			Position p = fullBoardPositions.get(i);
			if (diggedBoard.getCell(p) == 0) {
				editablePositions.add(p);
			}
		}
	}

	public ArrayList<Position> getEditablePositions() {
		return editablePositions;
	}

	public void setCell(Position p, int value) {
		diggedBoard.setCell(p, value);
	}

	public int getCell(Position p) {
		return diggedBoard.getCell(p);
	}

	public int getCell(int row, int col) {
		return diggedBoard.getCell(row, col);
	}

	public boolean isEditable(Position p) {
		return editablePositions.contains(p);
	}

	public boolean isEditable(int row, int col) {
		return editablePositions.contains(new Position(row, col));
	}

	/**
	 * 
	 * @return number of errors
	 */
	public int getErrors() {
		int errors = 0;
		int diggedCell;
		int fullCell;
		for (int i = 0; i < Board.SIZE; i++) {
			for (int j = 0; j < Board.SIZE; j++) {
				diggedCell = diggedBoard.getCell(i, j);
				fullCell = fullBoard.getCell(i, j);
				if (diggedCell != 0 && diggedCell != fullCell) {
					errors++;
				}
			}
		}
		return errors;
	}

	public int getBlanks() {
		int blanks = 0;
		int cell;
		for (int i = 0; i < Board.SIZE; i++) {
			for (int j = 0; j < Board.SIZE; j++) {
				cell = diggedBoard.getCell(i, j);
				if (cell == 0) {
					blanks++;
				}
			}
		}
		return blanks;
	}

}
