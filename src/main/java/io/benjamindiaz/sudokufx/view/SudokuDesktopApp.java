package io.benjamindiaz.sudokufx.view;

import io.benjamindiaz.sudokufx.model.Position;
import io.benjamindiaz.sudokufx.model.Sudoku;
import io.benjamindiaz.sudokufx.model.SudokuGenerator;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class SudokuDesktopApp extends Application {

	Stage window;
	Label[] cells;
	Sudoku sudoku;
	Label selectedCell;
	Label lastSelectedCell;
	Font font = new Font("Arial", 20);

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		window = primaryStage;
		Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
		Scene scene = new Scene(new VBox(), bounds.getWidth(),
				bounds.getHeight());
		String cssPath = getClass().getResource("/stylesheet.css")
				.toExternalForm();
		scene.getStylesheets().add(cssPath);

		/* Menu */
		MenuBar menuBar = new MenuBar();
		Menu menuFile = new Menu("Sudoku");
		MenuItem easy = new MenuItem("Easy");
		easy.setOnAction(e -> {
			drawSudoku(1);
		});
		MenuItem medium = new MenuItem("Medium");
		medium.setOnAction(e -> {
			drawSudoku(2);
		});
		MenuItem hard = new MenuItem("Hard");
		hard.setOnAction(e -> {
			drawSudoku(3);
		});
		MenuItem veryHard = new MenuItem("Very Hard");
		veryHard.setOnAction(e -> {
			drawSudoku(4);
		});
		MenuItem evil = new MenuItem("Evil");
		evil.setOnAction(e -> {
			drawSudoku(5);
		});
		MenuItem exit = new Menu("Exit");
		exit.setOnAction(e -> {
			window.close();
			System.exit(0);
		});
		menuFile.getItems().addAll(easy, medium, hard, veryHard, evil,
				new SeparatorMenuItem(), exit);
		menuBar.getMenus().addAll(menuFile);
		/* Board */
		GridPane grid = new GridPane();
		cells = new Label[81];
		Label cell;
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				cell = new Label();
				cell.setPrefSize(bounds.getWidth() / 9, bounds.getWidth() / 9);				
				cell.setFont(font);
				cell.setAlignment(Pos.CENTER);
				cell.setStyle("-fx-background-insets: 1; -fx-background-color:#F5F5F5;");
				cell.setUserData(new Position(i, j));
				cells[i * 9 + j] = cell;
			}
		}
		GridPane[] grids = new GridPane[9];
		int z = 0;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				GridPane g = new GridPane();
				g.setGridLinesVisible(true);
				int index = i * 27 + j * 3;
				g.addRow(0, cells[index], cells[index + 1], cells[index + 2]);
				g.addRow(1, cells[index + 9], cells[index + 10],
						cells[index + 11]);
				g.addRow(2, cells[index + 18], cells[index + 19],
						cells[index + 20]);
				grids[z] = g;
				z++;
				g.setPadding(new Insets(1));
			}
		}
		for (int i = 0; i < 3; i++) {
			grid.addRow(i, grids[i * 3], grids[i * 3 + 1], grids[i * 3 + 2]);
		}
		grid.setAlignment(Pos.CENTER);
		grid.setGridLinesVisible(true);
		grid.setPadding(new Insets(1, 1, 0, 1));
		/* Number buttons */
		HBox numberButtons = new HBox(2);
		HBox numberButtons2 = new HBox(2);
		for (int i = 0; i < 9; i++) {
			Button b = new Button(String.valueOf(i + 1));
			b.setPrefSize(bounds.getWidth() / 4.5, bounds.getWidth() / 9.5);
			b.setFont(font);
			b.setAlignment(Pos.CENTER);
			b.setUserData(String.valueOf(i + 1));
			b.setPadding(new Insets(1));
			b.setOnAction(e -> {
				if (selectedCell == null)
					return;
				else
					selectedCell.setText((String) b.getUserData());
				if (sudoku != null)
					sudoku.setCell((Position) selectedCell.getUserData(),
							Integer.parseInt((String) b.getUserData()));
			});
			if (i < 5)
				numberButtons.getChildren().add(b);
			else
				numberButtons2.getChildren().add(b);
		}
		numberButtons.setAlignment(Pos.CENTER);
		numberButtons.setPadding(new Insets(1));
		numberButtons2.setAlignment(Pos.CENTER);
		numberButtons2.setPadding(new Insets(1));
		/* Option Buttons */
		HBox optionButtons = new HBox(2);
		Button clear = new Button("C");
		clear.setAlignment(Pos.CENTER);
		clear.setPadding(new Insets(1));
		clear.setFont(font);
		clear.setPrefSize(bounds.getWidth() / 4.5, bounds.getWidth() / 9.5);
		clear.setOnAction(e -> {
			if (selectedCell == null)
				return;
			else
				selectedCell.setText("");
			if (sudoku != null)
				sudoku.setCell((Position) selectedCell.getUserData(), 0);
		});
		numberButtons2.getChildren().add(clear);
		Button check = new Button("Check!");
		check.setAlignment(Pos.CENTER);
		check.setPrefSize(bounds.getWidth(), bounds.getWidth() / 9.5);
		check.setOnAction(e -> {
			if(sudoku == null) return;
			int errors = sudoku.getErrors();
			int blanks = sudoku.getBlanks();
			System.out.println(errors+" "+blanks);
			Alert alert;
			if (errors != 0) {
				if (errors > 1) {
					alert = new Alert(AlertType.INFORMATION, "You have "
							+ errors + " errors! Don't give up!");
				} else {
					alert = new Alert(AlertType.INFORMATION, "You have "
							+ errors + " error! Don't give up!");
				}
				alert.setHeaderText("Ups! There is a problem, Houston!");
			} else if (blanks != 0) {
				alert = new Alert(AlertType.INFORMATION,
						"You are doing well! Don't give up!");
				alert.setHeaderText("Keep the good work!");
			} else {
				alert = new Alert(AlertType.INFORMATION,
						"Congratulations! Great job!");
				alert.setHeaderText("Awesome work! You rock!");
			}
			alert.showAndWait();
		});
		optionButtons.getChildren().addAll(check);
		optionButtons.setAlignment(Pos.CENTER);
		optionButtons.setPadding(new Insets(1));
		/* Add all and show */
		VBox body = new VBox(3);
		body.getChildren().addAll(grid, numberButtons, numberButtons2,
				optionButtons);
		((VBox) scene.getRoot()).getChildren().addAll(menuBar, body);
		window.setTitle("SudokuFX");
		window.setScene(scene);
		window.show();

	}

	private void drawBoard() {
		selectedCell = lastSelectedCell = null;
		Label cell;
		boolean editable;
		int row;
		int col;

		for (int i = 0; i < 81; i++) {
			cell = cells[i];
			row = i / 9;
			col = i % 9;
			editable = sudoku.isEditable(row, col);
			if (editable) {
				cell.setOnMouseClicked(e -> {
					if (selectedCell == (Label) e.getSource())
						return;
					selectedCell = (Label) e.getSource();
					selectedCell
							.setStyle("-fx-background-insets: 1; -fx-background-color: #b8b8b8;");

					if (lastSelectedCell != null) {
						lastSelectedCell
								.setStyle("-fx-background-insets: 1; -fx-background-color:#EEEEEE;");
					}
					lastSelectedCell = selectedCell;
				});
			} else {
				cell.setStyle("-fx-background-insets: 1; -fx-background-color:#F5F5F5;");
				cell.setOnMouseClicked(null);
			}
			int cellValue = sudoku.getCell(row, col);
			if (cellValue != 0) {
				cell.setText(String.valueOf(cellValue));
			} else {
				cell.setText("");
				cell.setStyle("-fx-background-insets: 1; -fx-background-color: #EEEEEE;");
			}
		}
		selectedCell = lastSelectedCell = null;
	}

	private void setSudoku(Sudoku s) {
		this.sudoku = s;
	}

	private void drawSudoku(int difficulty) {
		SudokuGenerator sg = new SudokuGenerator();
		Sudoku s = sg.generate(difficulty);
		setSudoku(s);
		drawBoard();
	}


}
