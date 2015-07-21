package io.benjamindiaz.sudokufx.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * The Sudoku board
 * 
 * @author Benjamin Diaz
 *
 */
public class Board implements Serializable {

	private static final long serialVersionUID = -2329080360064804899L;

	public int[][] board;
	private HashMap<Position, ArrayList<Integer>> possibilities;
	private ArrayList<Position> positions;
	public static final int SIZE = 9;

	/**
	 * Initialize new board. Initialize possible values for each cell.
	 * 
	 * @param size
	 *            Size of the board
	 */
	public Board() {
		board = new int[SIZE][SIZE];
		possibilities = new HashMap<Position, ArrayList<Integer>>();
		ArrayList<Integer> values = new ArrayList<Integer>();
		for (int i = 1; i <= SIZE; i++) {
			values.add(i);
		}
		positions = new ArrayList<Position>();
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				positions.add(new Position(i, j));
			}
		}
		for (int i = 0; i < SIZE * SIZE; i++) {
			possibilities.put(positions.get(i), values);
		}
	}

	@SuppressWarnings("unchecked")
	public Board clone() {
		Board newBoard = new Board();
		/* */
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				newBoard.board[i][j] = board[i][j];
			}
		}
		newBoard.positions = (ArrayList<Position>) this.positions.clone();
		newBoard.possibilities = (HashMap<Position, ArrayList<Integer>>) this.possibilities
				.clone();
		return newBoard;
	}

	public void recomputePossibilities() {
		Integer n;
		ArrayList<Integer> values;
		int posLen = getPositions().size();
		for (int z = 0; z < posLen; z++) {
			Position p = positions.get(z);
			if (getCell(p) != 0)
				continue;
			values = new ArrayList<Integer>();
			for (int i = 1; i <= SIZE; i++) {
				values.add(i);
			}
			/* Recompute possibilities for row and column */
			for (int i = 0; i < SIZE; i++) {
				if (i != p.col) {
					n = getCell(p.row, i);
					if (n != 0) {
						values.remove(n);
					}
				}
				if (i != p.row) {
					n = getCell(i, p.col);
					if (n != 0) {
						values.remove(n);
					}
				}
			}
			/* Recompute possibilities for box */
			int startRow = 3 * (p.row / 3);
			int startCol = 3 * (p.col / 3);

			for (int j = startRow; j < startRow + 3; j++) {
				for (int k = startCol; k < startCol + 3; k++) {
					if (j != p.row || k != p.col) {
						n = getCell(j, k);
						if (n != 0) {
							values.remove(n);
						}
					}
				}
			}
			/* Update possibilities */
			possibilities.put(p, values);
		}
	}

	public int getCell(Position p) {
		return board[p.row][p.col];
	}

	public int getCell(int row, int col) {
		return board[row][col];
	}

	public void setCell(Position p, int value) {
		board[p.row][p.col] = value;
		recomputePossibilities();
	}

	public void setCell(int row, int col, int value) {
		board[row][col] = value;
		recomputePossibilities();
	}

	public void clear(Position p) {
		board[p.row][p.col] = 0;
		recomputePossibilities();
	}

	public ArrayList<Position> getPositions() {
		return positions;
	}

	public ArrayList<Integer> getPossibilities(Position p) {
		if (board[p.row][p.col] != 0) {
			System.err.println("Cell " + p + " is not empty!");
			return null;
		}
		// ArrayList<Integer> pos = possibilities.get(p.toString());
		ArrayList<Integer> pos = possibilities.get(p);
		if (pos.size() == 0) {
			return null;
		} else {
			return pos;
		}
	}

	@Override
	public String toString() {
		String str = "";
		for (int[] row : board) {
			str += Arrays.toString(row) + "\n";
		}
		return str;
	}

}
