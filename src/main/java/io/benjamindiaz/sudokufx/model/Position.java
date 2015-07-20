package io.benjamindiaz.sudokufx.model;

import java.io.Serializable;


/**
 * 
 * @author Benjamin Diaz
 *
 */
public class Position implements Serializable{
	
	private static final long serialVersionUID = 6119607630239143971L;
	
	public int row;
	public int col;

	public Position() {
	}
	
	public Position(int row, int col) {
		this.row = row;
		this.col = col;
	}

	/**
	 * Returns position in terms of row and column. First digit is row, second
	 * is column. E.g: '01', '24', '09', '00', etc.
	 */
	@Override
	public String toString() {
		return row + "" + col;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + col;
		result = prime * result + row;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Position) {
			Position p = (Position) obj;
			if(p.row == this.row && p.col == this.col) {
				return true;
			}
		}
		return false;
	}
	

}
