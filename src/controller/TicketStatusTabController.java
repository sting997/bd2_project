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

import bd2.TicketStatus;
import bd2.Event;
import bd2.EventType;
import bd2.Seat;
import bd2.Sector;
import bd2.Stadium;
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
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class TicketStatusTabController {
	private SessionFactory factory;
	@FXML
	private TableView<TicketStatus> ticketStatusTableView;
	@FXML
	private TableColumn<TicketStatus, Integer> ticketStatusIdTableColumn;
	@FXML
	private TableColumn<TicketStatus, String> ticketStatusNameTableColumn;
	@FXML
	private Tab ticketStatusTab;
	@FXML
	private Button addTicketStatusButton;
	@FXML
	private Button editTicketStatusButton;
	@FXML
	private Button deleteTicketStatusButton;
	@FXML private Label infoLabel;

	@FXML
	public void initialize() {
		loadData();
		addTicketStatusButton.setOnAction((ActionEvent event) -> {
			handleAdd();
		});
		deleteTicketStatusButton.setOnAction((ActionEvent event) -> {
			handleDelete();
		});
		editTicketStatusButton.setOnAction((ActionEvent event) -> {
			handleEdit();
		});
		infoLabel.setTextFill(Color.FIREBRICK);
	}

	private void handleAdd() {
		infoLabel.setText("");
		HBox root = new HBox();
		TextField statusTextField = new TextField("Status");
		Button addButton = new Button("Add");
		root.getChildren().add(statusTextField);
		root.getChildren().add(addButton);
		
		addButton.setOnAction((ActionEvent event) -> {
			try {
				String status = statusTextField.getText();
				Session session = factory.openSession();
				Transaction tx = null;
				try {
					tx = session.beginTransaction();
					TicketStatus newTicketStatus = new TicketStatus();
					newTicketStatus.setStatus(status);
					Integer newTicketStatusId = (Integer) session.save(newTicketStatus);
					tx.commit();
					ticketStatusTableView.getItems().add(newTicketStatus);
				} catch (HibernateException e) {
					if (tx != null)
						tx.rollback();
						infoLabel.setText("Error");
					e.printStackTrace();
				} finally {
					session.close();
				}
			}catch (NumberFormatException nfx) {
				infoLabel.setText("Error");
			}
		});
		
		Stage stage = new Stage();
		stage.setTitle("Add Ticket Status");
		stage.setScene(new Scene(root));
		stage.show();

	}

	private void handleDelete() {
		infoLabel.setText("");
		int selectedIndex = ticketStatusTableView.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			TicketStatus ticketStatus = ticketStatusTableView.getItems().get(selectedIndex);
			Session session = factory.openSession();
			Transaction tx = null;
			try {
				tx = session.beginTransaction();
				session.delete(ticketStatus);
				tx.commit();
				ticketStatusTableView.getItems().remove(selectedIndex);
			} catch (HibernateException e) {
				if (tx != null)
					tx.rollback();
					infoLabel.setText("Error");
				e.printStackTrace();
			} catch (Exception e) {
				infoLabel.setText("Error");
			} 
			finally {
				session.close();
			}
		}
	}
	
	private void handleEdit() {
		infoLabel.setText("");
		int selectedIndex = ticketStatusTableView.getSelectionModel().getSelectedIndex();

		if (selectedIndex >= 0) {
		TicketStatus ticketStatus = ticketStatusTableView.getItems().get(selectedIndex);

			HBox root = new HBox();
			TextField statusTextField = new TextField("" + ticketStatus.getStatus());
			Button editButton = new Button("Edit");
			root.getChildren().add(statusTextField);
			root.getChildren().add(editButton);

			editButton.setOnAction((ActionEvent event) -> {
				try {
					String status = statusTextField.getText();

					Session session = factory.openSession();
					Transaction tx = null;
					try {
						tx = session.beginTransaction();
						ticketStatus.setStatus(status);
						session.update(ticketStatus);
						tx.commit();
						ticketStatusTableView.getItems().set(selectedIndex, ticketStatus);

					} catch (HibernateException e) {
						if (tx != null)
							tx.rollback();
						infoLabel.setText("Error");
					} finally {
						session.close();
					}
				} catch (NumberFormatException e) {
					infoLabel.setText("Error");
				}
			});

			Stage stage = new Stage();
			stage.setTitle("Edit TicketStatus");
			stage.setScene(new Scene(root));
			stage.show();
		}
	}

	private void loadData() {
		ticketStatusIdTableColumn.setCellValueFactory(new PropertyValueFactory<TicketStatus, Integer>("id"));
		ticketStatusNameTableColumn.setCellValueFactory(new PropertyValueFactory<TicketStatus, String>("status"));

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
			List list = session.createQuery("FROM TicketStatus").list();
			ObservableList<TicketStatus> data = FXCollections.observableList(list);
			ticketStatusTableView.setItems(data);
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

	public void close() {
		factory.close();
		System.out.println("factory closed");		
	}

}
