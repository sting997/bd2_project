package controller;

import bd2.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;

public class MainController {
//	@FXML private TableView<Event> eventTableView;
//	@FXML private TableColumn eventIdTableColumn;
//	@FXML private TableColumn eventNameTableColumn;
//	@FXML private TableColumn eventDateTableColumn;
//	@FXML private TableColumn eventStadiumTableColumn;
//	@FXML private TableColumn eventTypeTableColumn;
//	@FXML private TabPane tabPane ;
//	// Inject tab content.
//	@FXML private Tab eventTab;
	@FXML private TableView<Event> tableView;
	// Inject controller
	@FXML private EventTabController eventTabController;
	
	@FXML
	private void initialize() {
//		eventTabController.initialize();
	}
}
