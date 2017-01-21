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

public class EventTypeTabController {
	private SessionFactory factory;
	@FXML
	private TableView<EventType> eventTypeTableView;
	@FXML
	private TableColumn<EventType, Integer> eventTypeIdTableColumn;
	@FXML
	private TableColumn<EventType, String> eventTypeNameTableColumn;
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
		Button addButton = new Button("Add");
		root.getChildren().add(nameTextField);
		root.getChildren().add(addButton);
		
		addButton.setOnAction((ActionEvent event) -> {
			try {
				String name = nameTextField.getText();
				Session session = factory.openSession();
				Transaction tx = null;
				try {
					tx = session.beginTransaction();
					EventType newEventType = new EventType();
					newEventType.setTypeName(name);
					Integer newEventTypeId = (Integer) session.save(newEventType);
					tx.commit();
					eventTypeTableView.getItems().add(newEventType);
				} catch (HibernateException e) {
					if (tx != null)
						tx.rollback();
					//TODO print some info
					e.printStackTrace();
				} finally {
					session.close();
				}
			}catch (NumberFormatException nfx) {
				// TODO: handle exception
			}
		});
		
		Stage stage = new Stage();
		stage.setTitle("Add Event");
		stage.setScene(new Scene(root));
		stage.show();

	}

	private void handleDelete() {
		int selectedIndex = eventTypeTableView.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			EventType eventType = eventTypeTableView.getItems().get(selectedIndex);
			Session session = factory.openSession();
			Transaction tx = null;
			try {
				tx = session.beginTransaction();
				session.delete(eventType);
				tx.commit();
				eventTypeTableView.getItems().remove(selectedIndex);
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
		eventTypeIdTableColumn.setCellValueFactory(new PropertyValueFactory<EventType, Integer>("id"));
		eventTypeNameTableColumn.setCellValueFactory(new PropertyValueFactory<EventType, String>("typeName"));

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
			List list = session.createQuery("FROM EventType").list();
			ObservableList<EventType> data = FXCollections.observableList(list);
			eventTypeTableView.setItems(data);
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

	public void close() {
		factory.close();
		System.out.println("factory closed");		
	}

}
