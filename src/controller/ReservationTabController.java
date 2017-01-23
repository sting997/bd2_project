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

import bd2.Adress;
import bd2.Customer;
import bd2.Reservation;
import bd2.Ticket;
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

public class ReservationTabController {
	private SessionFactory factory;
	@FXML
	private TableView<Reservation> reservationTableView;
	@FXML
	private TableColumn<Reservation, Integer> reservationIdTableColumn;
	@FXML
	private TableColumn<Reservation, Customer> reservationCustomerTableColumn;
	@FXML
	private TableColumn<Reservation, Ticket> reservationTicketTableColumn;
	@FXML
	private TableColumn<Reservation, String> reservationSubmitDateTableColumn;
	@FXML
	private TableColumn<Reservation, String> reservationExpiryDateTableColumn;
	@FXML
	private TableColumn<Reservation, Boolean> reservationExecutedTableColumn;
	@FXML
	private Tab reservationTab;
	@FXML
	private Button addReservationButton;
	@FXML
	private Button editReservationButton;
	@FXML
	private Button deleteReservationButton;
	@FXML
	Label infoLabel;

	@FXML
	public void initialize() {
		loadData();
		addReservationButton.setOnAction((ActionEvent event) -> {
			handleAdd();
		});
		deleteReservationButton.setOnAction((ActionEvent event) -> {
			handleDelete();
		});
		editReservationButton.setOnAction((ActionEvent event) -> {
			handleEdit();
		});
	}

	private void handleEdit() {
		infoLabel.setText("");
		int selectedIndex = reservationTableView.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			Reservation newReservation = reservationTableView.getItems().get(selectedIndex);
			HBox root = new HBox();
			TextField customerTextField = new TextField("" + newReservation.getCustomer().getId());
			TextField ticketTextField = new TextField("" + newReservation.getTicket().getId());
			TextField submitDateTextField = new TextField("" + newReservation.getSubmitDate());
			TextField expiryDateTextField = new TextField("" + newReservation.getExpiryDate());
			TextField executedTextField = new TextField("" + newReservation.getExecuted());
			Button editButton = new Button("Edit");
			root.getChildren().add(customerTextField);
			root.getChildren().add(ticketTextField);
			root.getChildren().add(submitDateTextField);
			root.getChildren().add(expiryDateTextField);
			root.getChildren().add(executedTextField);
			root.getChildren().add(editButton);

			editButton.setOnAction((ActionEvent event) -> {
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				Date submitDate, expiryDate;
				try {
					int customerId = Integer.parseInt(customerTextField.getText());
					int ticketId = Integer.parseInt(ticketTextField.getText());

					submitDate = dateFormat.parse(submitDateTextField.getText());
					expiryDate = dateFormat.parse(expiryDateTextField.getText());
					boolean executed = Boolean.parseBoolean(executedTextField.getText());
					Session session = factory.openSession();
					Transaction tx = null;
					try {
						tx = session.beginTransaction();
						Customer customer = (Customer) session.get(Customer.class, customerId);
						Ticket ticket = (Ticket) session.get(Ticket.class, ticketId);
						
						newReservation.setCustomer(customer);
						newReservation.setTicket(ticket);
						newReservation.setSubmitDate(submitDate);
						newReservation.setExpiryDate(expiryDate);
						newReservation.setExecuted(executed);
						session.update(newReservation);
						tx.commit();
						reservationTableView.getItems().set(selectedIndex, newReservation);
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
			stage.setTitle("Edit Reservation");
			stage.setScene(new Scene(root));
			stage.show();
		}
	}

	private void handleAdd() {
		infoLabel.setText("");
		HBox root = new HBox();
		TextField customerTextField = new TextField("Customer id");
		TextField ticketTextField = new TextField("Ticket id");
		TextField submitDateTextField = new TextField("Submit Date");
		TextField expiryDateTextField = new TextField("Expiry Date");
		TextField executedTextField = new TextField("Executed");
		Button addButton = new Button("Add");
		root.getChildren().add(customerTextField);
		root.getChildren().add(ticketTextField);
		root.getChildren().add(submitDateTextField);
		root.getChildren().add(expiryDateTextField);
		root.getChildren().add(executedTextField);
		root.getChildren().add(addButton);
		
		addButton.setOnAction((ActionEvent event) -> {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date submitDate, expiryDate;
			try {
				int customerId = Integer.parseInt(customerTextField.getText());
				int ticketId = Integer.parseInt(ticketTextField.getText());

				submitDate = dateFormat.parse(submitDateTextField.getText());
				expiryDate = dateFormat.parse(expiryDateTextField.getText());
				boolean executed = Boolean.parseBoolean(executedTextField.getText());
				Session session = factory.openSession();
				Transaction tx = null;
				try {
					tx = session.beginTransaction();
					Customer customer = (Customer) session.get(Customer.class, customerId);
					Ticket ticket = (Ticket) session.get(Ticket.class, ticketId);
					Reservation newReservation = new Reservation();
					
					newReservation.setCustomer(customer);
					newReservation.setTicket(ticket);
					newReservation.setSubmitDate(submitDate);
					newReservation.setExpiryDate(expiryDate);
					newReservation.setExecuted(executed);
					Integer newReservationId = (Integer) session.save(newReservation);
					tx.commit();
					reservationTableView.getItems().add(newReservation);
					
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
		stage.setTitle("Add Reservation");
		stage.setScene(new Scene(root));
		stage.show();

	}

	private void handleDelete() {
		infoLabel.setText("");
		int selectedIndex = reservationTableView.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			Reservation reservation = reservationTableView.getItems().get(selectedIndex);
			Session session = factory.openSession();
			Transaction tx = null;
			try {
				tx = session.beginTransaction();
				session.delete(reservation);
				tx.commit();
				reservationTableView.getItems().remove(selectedIndex);
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
		reservationIdTableColumn.setCellValueFactory(new PropertyValueFactory<Reservation, Integer>("id"));
		reservationCustomerTableColumn.setCellValueFactory(new PropertyValueFactory<Reservation, Customer>("customer"));
		reservationTicketTableColumn.setCellValueFactory(new PropertyValueFactory<Reservation, Ticket>("ticket"));
		reservationSubmitDateTableColumn.setCellValueFactory(reservation -> {
			SimpleStringProperty property = new SimpleStringProperty();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			property.setValue(df.format(reservation.getValue().getSubmitDate()));
			return property;
		});
		reservationExpiryDateTableColumn.setCellValueFactory(reservation -> {
			SimpleStringProperty property = new SimpleStringProperty();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			property.setValue(df.format(reservation.getValue().getExpiryDate()));
			return property;
		});
		reservationExecutedTableColumn.setCellValueFactory(new PropertyValueFactory<Reservation, Boolean>("executed"));
		
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
			List list = session.createQuery("FROM Reservation").list();
			ObservableList<Reservation> data = FXCollections.observableList(list);
			reservationTableView.setItems(data);
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
