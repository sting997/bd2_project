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
import bd2.Sector;
import bd2.Stadium;
import bd2.Price;
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

public class PriceTabController {
	private SessionFactory factory;
	@FXML
	private TableView<Price> priceTableView;
	@FXML
	private TableColumn<Price, Integer> priceIdTableColumn;
	@FXML
	private TableColumn<Price, Event> priceEventTableColumn;
	@FXML
	private TableColumn<Price, Sector> priceSectorTableColumn;
	@FXML
	private TableColumn<Price, BigDecimal> pricePriceTableColumn;
	@FXML
	private Tab priceTab;
	@FXML
	private Button addPriceButton;
	@FXML
	private Button editPriceButton;
	@FXML
	private Button deletePriceButton;
	@FXML
	private Label infoLabel;

	@FXML
	public void initialize() {
		loadData();
		addPriceButton.setOnAction((ActionEvent event) -> {
			handleAdd();
		});
		deletePriceButton.setOnAction((ActionEvent event) -> {
			handleDelete();
		});
		editPriceButton.setOnAction((ActionEvent event) -> {
			handleEdit();
		});
		infoLabel.setTextFill(Color.FIREBRICK);

	}

	private void handleAdd() {
		infoLabel.setText("");
		HBox root = new HBox();
		TextField eventTextField = new TextField("Event");
		TextField sectorTextField = new TextField("Sector");
		TextField priceTextField = new TextField("Price");

		Button addButton = new Button("Add");
		root.getChildren().add(eventTextField);
		root.getChildren().add(sectorTextField);
		root.getChildren().add(priceTextField);
		root.getChildren().add(addButton);

		addButton.setOnAction((ActionEvent actionEvent) -> {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date;
			try {
				int eventId = Integer.parseInt(eventTextField.getText());
				int sectorId = Integer.parseInt(sectorTextField.getText());
				BigDecimal price = new BigDecimal(priceTextField.getText());

				Session session = factory.openSession();
				Transaction tx = null;
				try {
					tx = session.beginTransaction();					
					Event event = (Event) session.get(Event.class, eventId);
					Sector sector = (Sector) session.get(Sector.class, sectorId);
					Price newPrice = new Price();

					newPrice.setEvent(event);
					newPrice.setSector(sector);
					newPrice.setPrice(price);
					
					Integer newPriceId = (Integer) session.save(newPrice);
					tx.commit();
					priceTableView.getItems().add(newPrice);
				} catch (HibernateException ex) {
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
		stage.setTitle("Add Price");
		stage.setScene(new Scene(root));
		stage.show();

	}

	private void handleDelete() {
		infoLabel.setText("");
		int selectedIndex = priceTableView.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			Price price = priceTableView.getItems().get(selectedIndex);
			Session session = factory.openSession();
			Transaction tx = null;
			try {
				tx = session.beginTransaction();
				session.delete(price);
				tx.commit();
				priceTableView.getItems().remove(selectedIndex);
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
		int selectedIndex = priceTableView.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			Price newPrice = priceTableView.getItems().get(selectedIndex);
			HBox root = new HBox();
			
			TextField eventTextField = new TextField("" + newPrice.getEvent().getId());
			TextField sectorTextField = new TextField("" + newPrice.getSector().getId());
			TextField priceTextField = new TextField("" + newPrice.getPrice());

			Button editButton = new Button("Edit");

			root.getChildren().add(eventTextField);
			root.getChildren().add(sectorTextField);
			root.getChildren().add(priceTextField);
			root.getChildren().add(editButton);
			
			editButton.setOnAction((ActionEvent actionEvent) -> {
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				Date date;
				try {
					int eventId = Integer.parseInt(eventTextField.getText());
					int sectorId = Integer.parseInt(sectorTextField.getText());
					BigDecimal price = new BigDecimal(priceTextField.getText());

					Session session = factory.openSession();
					Transaction tx = null;
					try {
						tx = session.beginTransaction();					
						Event event = (Event) session.get(Event.class, eventId);
						Sector sector = (Sector) session.get(Sector.class, sectorId);
						
						newPrice.setEvent(event);
						newPrice.setSector(sector);
						newPrice.setPrice(price);
						
						session.update(newPrice);
						tx.commit();
						priceTableView.getItems().set(selectedIndex, newPrice);
					} catch (HibernateException e) {
						if (tx != null)
							tx.rollback();
						infoLabel.setText("Error");
					} finally {
						session.close();
					}
				} catch (NumberFormatException nfx) {
					infoLabel.setText("Error");
				}
			});
			Stage stage = new Stage();
			stage.setTitle("Edit Price");
			stage.setScene(new Scene(root));
			stage.show();
		}
	}
	

	private void loadData() {
		priceIdTableColumn.setCellValueFactory(new PropertyValueFactory<Price, Integer>("id"));
		priceEventTableColumn.setCellValueFactory(new PropertyValueFactory<Price, Event>("event"));
		priceSectorTableColumn.setCellValueFactory(new PropertyValueFactory<Price, Sector>("sector"));
		pricePriceTableColumn.setCellValueFactory(new PropertyValueFactory<Price, BigDecimal>("price"));

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
			List list = session.createQuery("FROM Price").list();
			ObservableList<Price> data = FXCollections.observableList(list);
			priceTableView.setItems(data);
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
