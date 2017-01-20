package controller;

import java.awt.Button;
import java.awt.TextField;

import bd2.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;

public class MainController {
	
	// Inject tab content.
	@FXML private BorderPane borderPane;
	@FXML private TableView<Event> eventTableView;
	@FXML private TableColumn eventIdTableColumn;
	@FXML private TableColumn eventNameTableColumn;
	@FXML private TableColumn eventDateTableColumn;
	@FXML private TableColumn eventStadiumTableColumn;
	@FXML private TableColumn eventTypeTableColumn;
	@FXML private TextField idTextField;
	@FXML private TextField nameTextField;
	@FXML private TextField dateTextField;
	@FXML private TextField stadiumTextField;
	@FXML private TextField typeTextField;
	@FXML private Button addButton;
	// Inject controller
	@FXML private EventTabController eventTabController;
	
	@FXML
	private void initialize() {
//		eventTabController.initialize();
	}
}
