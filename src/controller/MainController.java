package controller;




import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;

public class MainController {
	

	@FXML private EventTabController eventTabController;
	@FXML private CustomerTabController customerTabController;
	@FXML private ClientTypeTabController clientTypeTabController;
	@FXML private SeatTabController seatTabController;
	@FXML private CarnetTabController carnetTabController;
	@FXML private EventTypeTabController eventTypeTabController;
	@FXML private TicketTabController ticketTabController;
	@FXML private TicketStatusTabController ticketStatusTabController;
	@FXML private SectorTabController sectorTabController;
	@FXML private SectorTypeTabController sectorTypeTabController;
	@FXML private PriceTabController priceTabController;
	@FXML private MenuItem closeMenuItem;
	
	@FXML
	private void initialize() {
		closeMenuItem.setOnAction((ActionEvent event) -> {
			handleClose();
		});
	}
	
	private void handleClose() {
		eventTabController.close();
		seatTabController.close();
		customerTabController.close();
		clientTypeTabController.close();
		carnetTabController.close();
		eventTypeTabController.close();
		ticketTabController.close();
		ticketStatusTabController.close();
		sectorTypeTabController.close();
		sectorTabController.close();
		priceTabController.close();
		
		Platform.exit();
	}
}
