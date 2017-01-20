package controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import bd2.Event;
import bd2.EventType;
import bd2.Stadium;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class EventTabController {
	private SessionFactory factory;
	@FXML
	private TableView<Event> eventTableView;
	@FXML
	private TableColumn<Event, Integer> eventIdTableColumn;
	@FXML
	private TableColumn<Event, String> eventNameTableColumn;
	@FXML
	private TableColumn<Event, Date> eventDateTableColumn;
	@FXML
	private TableColumn<Event, Stadium> eventStadiumTableColumn;
	@FXML
	private TableColumn<Event, EventType> eventTypeTableColumn;
	@FXML
	private Tab eventTab;
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
		editEventButton.setOnAction((ActionEvent event) -> {
			// TODO handler
		});
	}

	private void handleAdd() {
		HBox root = new HBox();
		TextField nameTextField = new TextField("Name");
		TextField dateTextField = new TextField("Date");
		TextField stadiumTextField = new TextField("Stadium id");
		TextField typeTextField = new TextField("Type id");
		Button addButton = new Button("Add");
		root.getChildren().add(nameTextField);
		root.getChildren().add(dateTextField);
		root.getChildren().add(stadiumTextField);
		root.getChildren().add(typeTextField);
		root.getChildren().add(addButton);
		
		addButton.setOnAction((ActionEvent event) -> {
			byte stadiumId = Byte.parseByte(stadiumTextField.getText());
			byte typeId = Byte.parseByte(typeTextField.getText());
			String name = nameTextField.getText();
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date;
			try {
				date = dateFormat.parse(dateTextField.getText());
				Session session = factory.openSession();
				Transaction tx = null;
				try {
					tx = session.beginTransaction();
					Stadium stadium =(Stadium)session.get(Stadium.class, stadiumId);
					EventType eventType = (EventType) session.get(EventType.class, typeId);
					Event newEvent = new Event();
					newEvent.setName(name);
					newEvent.setDate(date);
					newEvent.setStadium(stadium);
					newEvent.setEventType(eventType);
					Integer newEventId = (Integer) session.save(newEvent);
					tx.commit();
					eventTableView.getItems().add(newEvent);
				} catch (HibernateException e) {
					if (tx != null)
						tx.rollback();
					//TODO print some info
					e.printStackTrace();
				} finally {
					session.close();
				}
			} catch (ParseException e) {
				//TODO print some info
				e.printStackTrace();
			}
		});
		
		Stage stage = new Stage();
		stage.setTitle("Add Event");
		stage.setScene(new Scene(root));
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
				// TODO jakos na ekranie pieknie pokazac info ze sie nie da
				e.printStackTrace();
			} finally {
				session.close();
			}
		}
	}

	private void loadData() {
		eventIdTableColumn.setCellValueFactory(new PropertyValueFactory<Event, Integer>("id"));
		eventNameTableColumn.setCellValueFactory(new PropertyValueFactory<Event, String>("name"));
		eventDateTableColumn.setCellValueFactory(new PropertyValueFactory<Event, Date>("date"));
		eventStadiumTableColumn.setCellValueFactory(new PropertyValueFactory<Event, Stadium>("stadium"));
		eventTypeTableColumn.setCellValueFactory(new PropertyValueFactory<Event, EventType>("eventType"));

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
		//TODO factory needs to be closed at the end of programme
		//otherwise the programme does not stop and needs to be terminated manually
		// factory.close();
	}

}
