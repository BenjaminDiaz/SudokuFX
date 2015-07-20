package io.benjamindiaz.sudokufx.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Generates a ready-to-play Sudoku board
 * 
 * @author Benjamin Diaz
 *
 */
public class SudokuGenerator {

	private Sudoku sudoku;
	public SudokuSolver solver = new SudokuSolver();

	public Sudoku generate(int difficulty) {
		Board fullBoard = generateFullBoard();
		Board diggedBoard = dig(difficulty, fullBoard);
		sudoku = new Sudoku(fullBoard, diggedBoard);
		return sudoku;
	}

	private Board generateFullBoard() {
		Board fullBoard = lasVegas(11);
		while (fullBoard == null) {
			fullBoard = lasVegas(11);
		}
		return fullBoard;
	}

	/**
	 * Generates a full Sudoku board implementing a Las Vegas algorithm. It
	 * chooses a determined number of positions to fill randomly. Then it
	 * recursively checks if they are valid, then it uses the solving function
	 * to fill the board.
	 * 
	 * @param givensCount
	 *            The number of givens that will be randomly placed
	 * @return Full Sudoku board
	 */
	private Board lasVegas(int givensCount) {
		ArrayList<Position> positions = new ArrayList<Position>();
		for (int i = 0; i < Board.SIZE; i++) {
			for (int j = 0; j < Board.SIZE; j++) {
				positions.add(new Position(i, j));
			}
		}
		ArrayList<Position> givens = randomSample(positions, givensCount);
		Board partialBoard = new Board();
		partialBoard = lasVegas(partialBoard, givens, 0);
		Board fullBoard = solver.solve(partialBoard);
		return fullBoard;
	}

	/**
	 * 
	 * @param board
	 *            The board
	 * @param givens
	 *            The position list of the givens
	 * @param i
	 *            The current index of the givens positions
	 * @return Full Sudoku board
	 */
	private Board lasVegas(Board board, ArrayList<Position> givens, int i) {
		/* All givens have been filled */
		if (i >= givens.size()) {
			return board;
		}

		Position currentCell = givens.get(i);
		ArrayList<Integer> cellPossibilities = board.getPossibilities(currentCell);
		Collections.shuffle(cellPossibilities);
		Board solution;
		for (Integer value : cellPossibilities) {
			board.setCell(currentCell, value);
			solution = lasVegas(board, givens, i + 1);
			if (solution != null) {
				return solution;
			}
		}
		return null;
	}

	/**
	 * A simple function to obtain a random sample of givens from all positions
	 * 
	 * @param positions
	 *            List containing all board positions
	 * @param givensCount
	 *            The number of elements of the sample
	 * @return A random sample of givensCount amount from positions list
	 */
	private ArrayList<Position> randomSample(ArrayList<Position> positions, int givensCount) {
		ArrayList<Position> result = new ArrayList<Position>();
		@SuppressWarnings("unchecked")
		ArrayList<Position> temp = (ArrayList<Position>) positions.clone();
		int n = temp.size();
		Random r = new Random();
		int randPosition;
		while (result.size() < givensCount) {
			randPosition = r.nextInt(n);
			result.add(temp.get(randPosition));
			temp.remove(randPosition);
			n--;
		}

		return result;
	}

	private Board dig(int difficulty, Board board) {
		DiggingStrategy dg = new DiggingStrategy(difficulty);
		return dig(board, dg);
	}

	private Board dig(Board fullBoard, DiggingStrategy dg) {
		Board board = fullBoard.clone();
		int digCount = 0;
		int previousValue;
		boolean hasAnotherSolution = false;
		ArrayList<Integer> possibilities;
		for (Position cell : dg.cells) {
			if (!(dg.canDig(board, cell))) {
				continue;
			}
			previousValue = board.getCell(cell);
			board.clear(cell);
			possibilities = board.getPossibilities(cell);
			if(possibilities == null) continue;
			possibilities.remove((Object) previousValue);
			hasAnotherSolution = false;
			for (Integer newValue : possibilities) {
				board.setCell(cell, newValue);
				if (solver.solve(board) != null) {
					hasAnotherSolution = true;
				}
			}
			/* If it has another solution, revert changes */
			if (hasAnotherSolution) {
				board.setCell(cell, previousValue);
			}
			/* Else, dig it */
			else {
				board.clear(cell);
				digCount += 1;
			}
			if (digCount > dg.LIMIT) {
				return board;
			}

		}
		return board;
	}
}
