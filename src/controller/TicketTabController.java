package controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import bd2.Carnet;
import bd2.ClientType;
import bd2.Customer;
import bd2.Event;
import bd2.EventType;
import bd2.Price;
import bd2.Seat;
import bd2.Stadium;
import bd2.Ticket;
import bd2.TicketStatus;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;

public class TicketTabController {
	private SessionFactory factory;
	@FXML
	private TableView<Ticket> ticketTableView;
	@FXML
	private TableColumn<Ticket, Integer> ticketIdTableColumn;
	@FXML
	private TableColumn<Ticket, Customer> ticketCustomerTableColumn;
	@FXML
	private TableColumn<Ticket, Seat> ticketSeatTableColumn;
	@FXML
	private TableColumn<Ticket, Event> ticketEventTableColumn;
	@FXML
	private TableColumn<Ticket, String> ticketDateTableColumn;
	@FXML
	private TableColumn<Ticket, TicketStatus> ticketStatusTableColumn;
	@FXML
	private TableColumn<Ticket, Carnet> ticketCarnetTableColumn;
	@FXML
	private TableColumn<Ticket, BigDecimal> ticketPriceTableColumn;
	@FXML
	private TableColumn<Ticket, BigDecimal> ticketDiscountTableColumn;
	@FXML
	private Tab ticketTab;
	@FXML
	private Button addTicketButton;
	@FXML
	private Button editTicketButton;
	@FXML
	private Button deleteTicketButton;
	@FXML
	private Label infoLabel;

	@FXML
	public void initialize() {
		loadData();
		addTicketButton.setOnAction((ActionEvent event) -> {
			handleAdd();
		});
		deleteTicketButton.setOnAction((ActionEvent event) -> {
			handleDelete();
		});
		editTicketButton.setOnAction((ActionEvent event) -> {
			handleEdit();
		});
		infoLabel.setTextFill(Color.FIREBRICK);

	}

	private void handleAdd() {
		infoLabel.setText("");
		HBox root = new HBox();
		TextField customerTextField = new TextField("Customer");
		TextField seatTextField = new TextField("Seat");
		TextField eventTextField = new TextField("Event");
		TextField dateTextField = new TextField("Date");
		TextField statusTextField = new TextField("Status");
		TextField carnetTextField = new TextField("Carnet");
		TextField priceTextField = new TextField("Price");
		TextField discountTextField = new TextField("Discount");

		Button addButton = new Button("Add");
		root.getChildren().add(customerTextField);
		root.getChildren().add(seatTextField);
		root.getChildren().add(eventTextField);
		root.getChildren().add(dateTextField);
		root.getChildren().add(statusTextField);
		root.getChildren().add(carnetTextField);
		root.getChildren().add(priceTextField);
		root.getChildren().add(discountTextField);
		root.getChildren().add(addButton);

		addButton.setOnAction((ActionEvent actionEvent) -> {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date;
			try {
				int customerId = Integer.parseInt(customerTextField.getText());
				int seatId = Integer.parseInt(seatTextField.getText());
				int eventId = Integer.parseInt(eventTextField.getText());
				date = dateFormat.parse(dateTextField.getText());
				int statusId = Integer.parseInt(statusTextField.getText());
				int carnetId = Integer.parseInt(carnetTextField.getText());
				BigDecimal discount = new BigDecimal(discountTextField.getText());
				BigDecimal price = new BigDecimal(priceTextField.getText());

				Session session = factory.openSession();
				Transaction tx = null;
				try {
					tx = session.beginTransaction();
					Customer customer = (Customer) session.get(Customer.class, customerId);
					Seat seat = (Seat) session.get(Seat.class, seatId);
					Event event = (Event) session.get(Event.class, eventId);
					TicketStatus ticketStatus = (TicketStatus) session.get(TicketStatus.class, statusId);
					Carnet carnet = (Carnet) session.get(Carnet.class, carnetId);
					Ticket newTicket = new Ticket();

					newTicket.setCustomer(customer);
					newTicket.setSeat(seat);
					newTicket.setEvent(event);
					newTicket.setSellDate(date);
					newTicket.setTicketStatus(ticketStatus);
					newTicket.setCarnet(carnet);
					newTicket.setDiscount(discount);
					newTicket.setPrice(price);
					
					Integer newTicketId = (Integer) session.save(newTicket);
					tx.commit();
					ticketTableView.getItems().add(newTicket);
				} catch (HibernateException ex) {
					if (tx != null)
						tx.rollback();
					infoLabel.setText("Error");
				} finally {
					session.close();
				}
			} catch (NumberFormatException | ParseException e) {
				infoLabel.setText("Error");
			}
		});

		Stage stage = new Stage();
		stage.setTitle("Add Ticket");
		stage.setScene(new Scene(root));
		stage.show();

	}

	private void handleDelete() {
		infoLabel.setText("");
		int selectedIndex = ticketTableView.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			Ticket ticket = ticketTableView.getItems().get(selectedIndex);
			Session session = factory.openSession();
			Transaction tx = null;
			try {
				tx = session.beginTransaction();
				session.delete(ticket);
				tx.commit();
				ticketTableView.getItems().remove(selectedIndex);
			} catch (HibernateException e) {
				if (tx != null)
					tx.rollback();
				infoLabel.setText("Error");
			} catch (Exception e) {
				infoLabel.setText("Error");
			} finally {
				session.close();
			}
		}
	}

	private void handleEdit() {
		infoLabel.setText("");
		int selectedIndex = ticketTableView.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			Ticket newTicket = ticketTableView.getItems().get(selectedIndex);
			HBox root = new HBox();
			TextField customerTextField = new TextField("" + newTicket.getCustomer());
			TextField seatTextField = new TextField("" + newTicket.getSeat().getId());
			TextField eventTextField = new TextField("" + newTicket.getEvent().getId());
			TextField dateTextField = new TextField("" + newTicket.getSellDate());
			TextField statusTextField = new TextField("" + newTicket.getTicketStatus().getId());
			TextField carnetTextField = new TextField("" + newTicket.getCarnet());
			TextField priceTextField = new TextField("" + newTicket.getPrice());
			TextField discountTextField = new TextField("" + newTicket.getDiscount());
			Button editButton = new Button("Edit");
			root.getChildren().add(customerTextField);
			root.getChildren().add(seatTextField);
			root.getChildren().add(eventTextField);
			root.getChildren().add(dateTextField);
			root.getChildren().add(statusTextField);
			root.getChildren().add(carnetTextField);
			root.getChildren().add(priceTextField);
			root.getChildren().add(discountTextField);
			root.getChildren().add(editButton);
			
			editButton.setOnAction((ActionEvent actionEvent) -> {
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				Date date;
				try {
					int customerId = Integer.parseInt(customerTextField.getText());
					int seatId = Integer.parseInt(seatTextField.getText());
					int eventId = Integer.parseInt(eventTextField.getText());
					date = dateFormat.parse(dateTextField.getText());
					int statusId = Integer.parseInt(statusTextField.getText());
					int carnetId = Integer.parseInt(carnetTextField.getText());
					BigDecimal discount = new BigDecimal(discountTextField.getText());
					BigDecimal price = new BigDecimal(priceTextField.getText());

					Session session = factory.openSession();
					Transaction tx = null;
					try {
						tx = session.beginTransaction();
						Customer customer = (Customer) session.get(Customer.class, customerId);
						Seat seat = (Seat) session.get(Seat.class, seatId);
						Event event = (Event) session.get(Event.class, eventId);
						TicketStatus ticketStatus = (TicketStatus) session.get(TicketStatus.class, statusId);
						Carnet carnet = (Carnet) session.get(Carnet.class, carnetId);
						
						newTicket.setCustomer(customer);
						newTicket.setSeat(seat);
						newTicket.setEvent(event);
						newTicket.setSellDate(date);
						newTicket.setTicketStatus(ticketStatus);
						newTicket.setCarnet(carnet);
						newTicket.setDiscount(discount);
						newTicket.setPrice(price);
						
						session.update(newTicket);
						tx.commit();
						ticketTableView.getItems().set(selectedIndex, newTicket);
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
			stage.setTitle("Edit Ticket");
			stage.setScene(new Scene(root));
			stage.show();
		}
	}
	

	private void loadData() {
		ticketIdTableColumn.setCellValueFactory(new PropertyValueFactory<Ticket, Integer>("id"));
		ticketCustomerTableColumn.setCellValueFactory(new PropertyValueFactory<Ticket, Customer>("customer"));
		ticketSeatTableColumn.setCellValueFactory(new PropertyValueFactory<Ticket, Seat>("seat"));
		ticketEventTableColumn.setCellValueFactory(new PropertyValueFactory<Ticket, Event>("event"));
		ticketDateTableColumn.setCellValueFactory((ticket -> {
			SimpleStringProperty property = new SimpleStringProperty();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			property.setValue(df.format(ticket.getValue().getSellDate()));
			return property;
		}));
		ticketStatusTableColumn.setCellValueFactory(new PropertyValueFactory<Ticket, TicketStatus>("ticketStatus"));
		ticketCarnetTableColumn.setCellValueFactory(new PropertyValueFactory<Ticket, Carnet>("carnet"));
		ticketPriceTableColumn.setCellValueFactory(new PropertyValueFactory<Ticket, BigDecimal>("price"));
		ticketDiscountTableColumn.setCellValueFactory(new PropertyValueFactory<Ticket, BigDecimal>("discount"));

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
			List list = session.createQuery("FROM Ticket").list();
			ObservableList<Ticket> data = FXCollections.observableList(list);
			ticketTableView.setItems(data);
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
