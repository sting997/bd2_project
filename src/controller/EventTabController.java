package controller;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.Table;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import bd2.Adress;
import bd2.Event;
import bd2.EventType;
import bd2.Stadium;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Callback;

public class EventTabController {
	private SessionFactory factory;
	@FXML
	private TableView<Event> eventTableView;
	@FXML
	private TableColumn eventIdTableColumn;
	@FXML
	private TableColumn eventNameTableColumn;
	@FXML
	private TableColumn eventDateTableColumn;
	@FXML
	private TableColumn<Event, Stadium> eventStadiumTableColumn;
	@FXML
	private TableColumn eventTypeTableColumn;
	@FXML
	private Tab eventTab;
	// @FXML
	// private TextField idTextField;
	// @FXML
	// private TextField nameTextField;
	// @FXML
	// private TextField dateTextField;
	// @FXML
	// private TextField stadiumTextField;
	// @FXML
	// private TextField typeTextField;
	@FXML
	private Button addEventButton;
	@FXML
	private Button editEventButton;
	@FXML
	private Button deleteEventButton;

	@FXML
	public void initialize() {
		loadData();
		addEventButton.setOnAction((ActionEvent event) -> {
			handleAdd();
		});
		deleteEventButton.setOnAction((ActionEvent event) -> {
			handleDelete();
		});
	}

	private void handleAdd() {
		// TODO
		Parent root;
		// root =
		// FXMLLoader.load(getClass().getClassLoader().getResource("path/to/other/view.fxml"),
		// resources);
		GridPane grid = new GridPane();
		Stage stage = new Stage();
		stage.setTitle("My New Stage Title");
		stage.setScene(new Scene(grid, 450, 450));
		stage.show();

	}

	private void handleDelete() {
		int selectedIndex = eventTableView.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			Event event = eventTableView.getItems().get(selectedIndex);
			Session session = factory.openSession();
			Transaction tx = null;
			try {
				tx = session.beginTransaction();
				session.delete(event);
				tx.commit();
				eventTableView.getItems().remove(selectedIndex);
			} catch (HibernateException e) {
				if (tx != null)
					tx.rollback();
				//TODO jakos na ekranie pieknie pokazac info ze sie nie da 
				e.printStackTrace();
			} finally {
				session.close();
			}
		}
	}

	private void loadData() {
		eventIdTableColumn.setCellValueFactory(new PropertyValueFactory<Event, String>("id"));
		eventNameTableColumn.setCellValueFactory(new PropertyValueFactory<Event, String>("name"));
		eventDateTableColumn.setCellValueFactory(new PropertyValueFactory<Event, Date>("date"));
		eventStadiumTableColumn.setCellValueFactory(new PropertyValueFactory<Event, Stadium>("stadium"));
		eventTypeTableColumn.setCellValueFactory(new PropertyValueFactory<Event, String>("eventType"));
		;

		try {
			factory = new Configuration().configure("/resources/hibernate.cfg.xml").buildSessionFactory();
		} catch (Throwable ex) {
			System.err.println("Failed to create sessionFactory object." + ex);
			throw new ExceptionInInitializerError(ex);
		}

		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			List list = session.createQuery("FROM Event").list();
			ObservableList<Event> data = FXCollections.observableList(list);
			eventTableView.setItems(data);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}

		// factory.close();
	}

}
