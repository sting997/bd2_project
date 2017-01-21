package controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.Iterator;
import java.util.List;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.math.BigDecimal;

import javax.persistence.GeneratedValue;
import javax.persistence.Table;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import bd2.Carnet;
import bd2.Customer;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

public class CarnetTabController {
	private SessionFactory factory;
	@FXML
	private TableView<Carnet> carnetTableView;
	@FXML
	private TableColumn<Carnet, Integer> carnetIdTableColumn;
	@FXML
	private TableColumn<Carnet, BigDecimal> carnetPriceTableColumn;
	@FXML
	private TableColumn<Carnet, String> carnetDateTableColumn;
	@FXML
	private TableColumn<Carnet, Customer> carnetCustomerTableColumn;
	@FXML
	private Tab carnetTab;
	@FXML
	private Button addCarnetButton;
	@FXML
	private Button editCarnetButton;
	@FXML
	private Button deleteCarnetButton;
	@FXML 
	private Label infoLabel;
	@FXML
	public void initialize() {
		loadData();
		addCarnetButton.setOnAction((ActionEvent event) -> {
			handleAdd();
		});
		deleteCarnetButton.setOnAction((ActionEvent event) -> {
			handleDelete();
		});
		editCarnetButton.setOnAction((ActionEvent event) -> {
			handleEdit();
		});
		infoLabel.setTextFill(Color.FIREBRICK);
	}

	private void handleAdd() {
		infoLabel.setText("");
		HBox root = new HBox();
		TextField priceTextField = new TextField("Price");
		TextField dateTextField = new TextField("Date");
		TextField customerTextField = new TextField("Customer");
		Button addButton = new Button("Add");
		root.getChildren().add(priceTextField);
		root.getChildren().add(dateTextField);
		root.getChildren().add(customerTextField);
		root.getChildren().add(addButton);

		addButton.setOnAction((ActionEvent event) -> {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date;
			try {
				BigDecimal price = new BigDecimal(priceTextField.getText());
				date = dateFormat.parse(dateTextField.getText());
				int customerId = Integer.parseInt(customerTextField.getText());
				Session session = factory.openSession();
				Transaction tx = null;
				try {
					tx = session.beginTransaction();
					Customer customer = (Customer) session.get(Customer.class, customerId);
					Carnet newCarnet = new Carnet();
					newCarnet.setPrice(price);
					newCarnet.setExpireDate(date);
					newCarnet.setCustomer(customer);
					Integer newCarnetId = (Integer) session.save(newCarnet);
					tx.commit();
					carnetTableView.getItems().add(newCarnet);
				} catch (HibernateException e) {
					if (tx != null)
						tx.rollback();
						infoLabel.setText("Error");
				} finally {
					session.close();
				}
			} catch (NumberFormatException e) {
				infoLabel.setText("Error");
			} catch (ParseException e) { 
				infoLabel.setText("Error");
			}
		});

		Stage stage = new Stage();
		stage.setTitle("Add Carnet");
		stage.setScene(new Scene(root));
		stage.show();

	}

	private void handleDelete() {
		infoLabel.setText("");
		int selectedIndex = carnetTableView.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			Carnet carnet = carnetTableView.getItems().get(selectedIndex);
			Session session = factory.openSession();
			Transaction tx = null;
			try {
				tx = session.beginTransaction();
				session.delete(carnet);
				tx.commit();
				carnetTableView.getItems().remove(selectedIndex);
			} catch (HibernateException e) {
				if (tx != null)
					tx.rollback();
				infoLabel.setText("Error");
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
		int selectedIndex = carnetTableView.getSelectionModel().getSelectedIndex();

		if (selectedIndex >= 0) {
			Carnet carnet = carnetTableView.getItems().get(selectedIndex);

			HBox root = new HBox();
			TextField priceTextField = new TextField("" + carnet.getPrice());
			TextField dateTextField = new TextField("" + carnet.getExpireDate());
			TextField customerTextField = new TextField("" + carnet.getCustomer().getId());
			Button editButton = new Button("Edit");
			root.getChildren().add(priceTextField);
			root.getChildren().add(dateTextField);
			root.getChildren().add(customerTextField);
			root.getChildren().add(editButton);

			editButton.setOnAction((ActionEvent event) -> {
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				Date date;
				try {
					BigDecimal price = new BigDecimal(priceTextField.getText());
					date = dateFormat.parse(dateTextField.getText());
					int customerId = Integer.parseInt(customerTextField.getText());

					Session session = factory.openSession();
					Transaction tx = null;
					try {
						tx = session.beginTransaction();
						Customer customer = (Customer) session.get(Customer.class, customerId);
						carnet.setPrice(price);
						carnet.setExpireDate(date);
						carnet.setCustomer(customer);
						session.update(carnet);
						tx.commit();
						carnetTableView.getItems().set(selectedIndex, carnet);

					} catch (HibernateException e) {
						if (tx != null)
							tx.rollback();
						infoLabel.setText("Error");
					} finally {
						session.close();
					}
				} catch (NumberFormatException e) {
					infoLabel.setText("Error");
				} catch (ParseException e) { 
					infoLabel.setText("Error");
				}
			});

			Stage stage = new Stage();
			stage.setTitle("Edit Carnet");
			stage.setScene(new Scene(root));
			stage.show();
		}
	}
	
	private void loadData() {
		carnetIdTableColumn.setCellValueFactory(new PropertyValueFactory<Carnet, Integer>("id"));
		carnetPriceTableColumn.setCellValueFactory(new PropertyValueFactory<Carnet, BigDecimal>("price"));
		carnetDateTableColumn.setCellValueFactory(carnet -> {
			SimpleStringProperty property = new SimpleStringProperty();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			property.setValue(df.format(carnet.getValue().getExpireDate()));
			return property;
		});		
		carnetCustomerTableColumn.setCellValueFactory(new PropertyValueFactory<Carnet, Customer>("customer"));

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
			List list = session.createQuery("FROM Carnet").list();
			ObservableList<Carnet> data = FXCollections.observableList(list);
			carnetTableView.setItems(data);
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
