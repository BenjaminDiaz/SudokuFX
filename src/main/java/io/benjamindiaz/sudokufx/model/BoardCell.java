package io.benjamindiaz.sudokufx.model;

public class BoardCell {

	/**
	 * Value is the real value of the cell, established when the board is
	 * generated, while userValue is the value shown to user, modified during
	 * the digging and solving. Editable determines if the cell can be edited by
	 * the user or not. All of this is to optimize the saving feature, and
	 * separate the model from the view.
	 */

	private boolean editable = false;
	private int value, userValue;

	public BoardCell() {
		value = 0;
		userValue = 0;
	}
	
	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
		this.userValue = value;
	}

	public int getUserValue() {
		return userValue;
	}

	public void setUserValue(int userValue) {
		this.userValue = userValue;
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}
	
	

}
