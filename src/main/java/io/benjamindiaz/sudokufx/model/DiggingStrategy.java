package io.benjamindiaz.sudokufx.model;

import java.util.ArrayList;
import java.util.Collections;


public class DiggingStrategy {
	public int MAX_EMPTY_CELLS;
	public int LIMIT;
	public ArrayList<Position> cells = new ArrayList<Position>();

	public DiggingStrategy(int difficulty) {
		switch (difficulty) {
		case 1:
			generateRandomCells();
			MAX_EMPTY_CELLS = 4;
			LIMIT = 31;
			break;
		case 2:
			generateRandomCells();
			MAX_EMPTY_CELLS = 5;
			LIMIT = 45;
			break;
		case 3:
			generateJumpingOneCell();
			MAX_EMPTY_CELLS = 6;
			LIMIT = 49;
			break;
		case 4:
			generateWanderingAlongS();
			MAX_EMPTY_CELLS = 7;
			LIMIT = 53;
			break;
		case 5:
			generateOrderedCells();
			MAX_EMPTY_CELLS = 9;
			LIMIT = 59;
			break;
		default:
			System.err.println("Difficulty not set!");
			break;
		}
	}

	public boolean canDig(Board board, Position p) {
		int numEmptyCells = 0;
		for (int i = 0; i < Board.SIZE; i++) {
			if (board.getCell(p.row, i) == 0) {
				numEmptyCells += 1;
			}
		}
		
		if (numEmptyCells > MAX_EMPTY_CELLS) {
			return false;
		}
		numEmptyCells = 0;
		for (int i = 0; i < Board.SIZE; i++) {
			if (board.getCell(i, p.col) == 0) {
				numEmptyCells += 1;
			}
		}
		if (numEmptyCells > MAX_EMPTY_CELLS) {
			return false;
		}
		return true;
	}

	private void generateOrderedCells() {
		for (int i = 0; i < Board.SIZE; i++) {
			for (int j = 0; j < Board.SIZE; j++) {
				cells.add(new Position(i, j));
			}
		}
	}

	private void generateRandomCells() {
		generateOrderedCells();
		Collections.shuffle(cells);

	}

	private void generateWanderingAlongS() {
		Position p;
		for (int i = 0; i < Board.SIZE; i++) {
			for (int j = 0; j < Board.SIZE; j++) {
				if (i%2 == 0) {
					p = new Position(i, j);
				}
				else {
					p = new Position(i, 8-j);
				}
				cells.add(p);
			}
		}
	}

	private void generateJumpingOneCell() {
		generateWanderingAlongS();
		ArrayList<Position> temp = new ArrayList<Position>();
		for (int i = 0; i < cells.size(); i+=2) {
			temp.add(cells.get(i));
		}
		for (int i = 1; i < cells.size(); i+=2) {
			temp.add(cells.get(i));
		}
		cells = temp;

	}

}
