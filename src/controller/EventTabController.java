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
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
	private TableColumn<Event, String> eventDateTableColumn;
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
	Label infoLabel;

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
			handleEdit();
		});
	}

	private void handleEdit() {
		infoLabel.setText("");
		int selectedIndex = eventTableView.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			Event newEvent = eventTableView.getItems().get(selectedIndex);
			HBox root = new HBox();
			TextField nameTextField = new TextField(newEvent.getName());
			TextField dateTextField = new TextField("" + newEvent.getDate());
			TextField stadiumTextField = new TextField("" + newEvent.getStadium().getId());
			TextField typeTextField = new TextField("" + newEvent.getEventType().getId());
			Button editButton = new Button("Edit");
			root.getChildren().add(nameTextField);
			root.getChildren().add(dateTextField);
			root.getChildren().add(stadiumTextField);
			root.getChildren().add(typeTextField);
			root.getChildren().add(editButton);

			editButton.setOnAction((ActionEvent event) -> {
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				Date date;
				try {
					int stadiumId = Integer.parseInt(stadiumTextField.getText());
					int typeId = Integer.parseInt(typeTextField.getText());
					String name = nameTextField.getText();
					date = dateFormat.parse(dateTextField.getText());
					Session session = factory.openSession();
					Transaction tx = null;
					try {
						tx = session.beginTransaction();
						Stadium stadium = (Stadium) session.get(Stadium.class, stadiumId);
						EventType eventType = (EventType) session.get(EventType.class, typeId);
						newEvent.setName(name);
						newEvent.setDate(date);
						newEvent.setStadium(stadium);
						newEvent.setEventType(eventType);
						session.update(newEvent);
						tx.commit();
						eventTableView.getItems().set(selectedIndex, newEvent);
					} catch (HibernateException e) {
						if (tx != null)
							tx.rollback();
						infoLabel.setText("Error");
					} finally {
						session.close();
					}
				} catch (NumberFormatException nfx) {
					infoLabel.setText("Error");
				} catch (ParseException e) {
					infoLabel.setText("Error");
				}

			});

			Stage stage = new Stage();
			stage.setTitle("Edit Event");
			stage.setScene(new Scene(root));
			stage.show();
		}
	}

	private void handleAdd() {
		infoLabel.setText("");
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
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date;
			try {
				int stadiumId = Integer.parseInt(stadiumTextField.getText());
				int typeId = Integer.parseInt(typeTextField.getText());
				String name = nameTextField.getText();
				date = dateFormat.parse(dateTextField.getText());
				Session session = factory.openSession();
				Transaction tx = null;
				try {
					tx = session.beginTransaction();
					Stadium stadium = (Stadium) session.get(Stadium.class, stadiumId);
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
					infoLabel.setText("Error");
				} finally {
					session.close();
				}
			} catch (NumberFormatException nfx) {
				infoLabel.setText("Error");
			} catch (ParseException e) {
				infoLabel.setText("Error");
			}
		});

		Stage stage = new Stage();
		stage.setTitle("Add Event");
		stage.setScene(new Scene(root));
		stage.show();

	}

	private void handleDelete() {
		infoLabel.setText("");
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
				infoLabel.setText("Error");
			} finally {
				session.close();
			}
		}
	}

	private void loadData() {
		eventIdTableColumn.setCellValueFactory(new PropertyValueFactory<Event, Integer>("id"));
		eventNameTableColumn.setCellValueFactory(new PropertyValueFactory<Event, String>("name"));
		eventDateTableColumn.setCellValueFactory(event -> {
			SimpleStringProperty property = new SimpleStringProperty();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			property.setValue(df.format(event.getValue().getDate()));
			return property;
		});
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
	}

	public void close() {
		factory.close();
		System.out.println("factory closed");
	}
}
