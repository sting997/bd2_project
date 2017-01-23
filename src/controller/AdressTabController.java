package controller;

import java.io.IOException;
import java.text.ParseException;
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

public class AdressTabController {
	private SessionFactory factory;
	@FXML
	private TableView<Adress> adressTableView;
	@FXML
	private TableColumn<Adress, Integer> adressIdTableColumn;
	@FXML
	private TableColumn<Adress, String> adressCityTableColumn;
	@FXML
	private TableColumn<Adress, String> adressPostalCodeTableColumn;
	@FXML
	private TableColumn<Adress, String> adressStreetTableColumn;
	@FXML
	private TableColumn<Adress, Short> adressHouseNumberTableColumn;
	@FXML
	private TableColumn<Adress, Integer> adressFlatNumberTableColumn;
	@FXML
	private Tab adressTab;
	@FXML
	private Button addAdressButton;
	@FXML
	private Button editAdressButton;
	@FXML
	private Button deleteAdressButton;
	@FXML 
	private Label infoLabel;
	@FXML
	public void initialize() {
		loadData();
		addAdressButton.setOnAction((ActionEvent event) -> {
			handleAdd();
		});
		deleteAdressButton.setOnAction((ActionEvent event) -> {
			handleDelete();
		});
		editAdressButton.setOnAction((ActionEvent event) -> {
			handleEdit();
		});
		infoLabel.setTextFill(Color.FIREBRICK);

	}

	private void handleAdd() {
		infoLabel.setText("");
		HBox root = new HBox();
		TextField cityTextField = new TextField("City");
		TextField postalCodeTextField = new TextField("Postal Code");
		TextField streetTextField = new TextField("Street");
		TextField houseNumberTextField = new TextField("House Number");
		TextField flatNumberTextField = new TextField("Flat Number");
		
		Button addButton = new Button("Add");
		root.getChildren().add(cityTextField);
		root.getChildren().add(postalCodeTextField);
		root.getChildren().add(streetTextField);
		root.getChildren().add(houseNumberTextField);
		root.getChildren().add(flatNumberTextField);
		root.getChildren().add(addButton);

		addButton.setOnAction((ActionEvent event) -> {
			try {
				
				String city = cityTextField.getText();
				String postalCode = postalCodeTextField.getText();
				String street = streetTextField.getText();
				short houseNumber = Short.parseShort(houseNumberTextField.getText());
				int flatNumber = Integer.parseInt(flatNumberTextField.getText());

				
				Session session = factory.openSession();
				Transaction tx = null;
				try {
					tx = session.beginTransaction();
					Adress newAdress = new Adress();
					
					newAdress.setCity(city);
					newAdress.setPostalCode(postalCode);
					newAdress.setStreet(street);
					newAdress.setHouseNumber(houseNumber);
					newAdress.setFlatNumber(flatNumber);

					Integer newAdressId = (Integer) session.save(newAdress);
					tx.commit();
					adressTableView.getItems().add(newAdress);
				} catch (HibernateException e) {
					if (tx != null)
						tx.rollback();
					infoLabel.setText("Error");
					e.printStackTrace();
				} finally {
					session.close();
				}
			} catch (NumberFormatException e) {
				infoLabel.setText("Error");
			}
		});

		Stage stage = new Stage();
		stage.setTitle("Add Adress");
		stage.setScene(new Scene(root));
		stage.show();

	}

	private void handleDelete() {
		infoLabel.setText("");
		int selectedIndex = adressTableView.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			Adress adress = adressTableView.getItems().get(selectedIndex);
			Session session = factory.openSession();
			Transaction tx = null;
			try {
				tx = session.beginTransaction();
				session.delete(adress);
				tx.commit();
				adressTableView.getItems().remove(selectedIndex);
			} catch (HibernateException e) {
				if (tx != null)
					tx.rollback();
				infoLabel.setText("Error");
			} catch (Exception e) {
				infoLabel.setText("Error");
			}
			finally{
				session.close();
			}
		}
	}

	private void handleEdit() {
		infoLabel.setText("");
		int selectedIndex = adressTableView.getSelectionModel().getSelectedIndex();

		if (selectedIndex >= 0) {
			Adress adress = adressTableView.getItems().get(selectedIndex);

			HBox root = new HBox();
			TextField cityTextField = new TextField(adress.getCity());
			TextField postalCodeTextField = new TextField(adress.getPostalCode());
			TextField streetTextField = new TextField(adress.getStreet());
			TextField houseNumberTextField = new TextField("" + adress.getHouseNumber());
			TextField flatNumberTextField = new TextField("" + adress.getFlatNumber());
			
			Button editButton = new Button("Edit");
			root.getChildren().add(cityTextField);
			root.getChildren().add(postalCodeTextField);
			root.getChildren().add(streetTextField);
			root.getChildren().add(houseNumberTextField);
			root.getChildren().add(flatNumberTextField);
			root.getChildren().add(editButton);


			editButton.setOnAction((ActionEvent event) -> {
				try {
					String city = cityTextField.getText();
					String postalCode = postalCodeTextField.getText();
					String street = streetTextField.getText();
					short houseNumber = Short.parseShort(houseNumberTextField.getText());
					int flatNumber = Integer.parseInt(flatNumberTextField.getText());

					Session session = factory.openSession();
					Transaction tx = null;
					try {
						tx = session.beginTransaction();
						
						adress.setCity(city);
						adress.setPostalCode(postalCode);
						adress.setStreet(street);
						adress.setHouseNumber(houseNumber);
						adress.setFlatNumber(flatNumber);
						
						session.update(adress);
						
						tx.commit();
						adressTableView.getItems().set(selectedIndex, adress);

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
			stage.setTitle("Edit Adress");
			stage.setScene(new Scene(root));
			stage.show();
		}
	}

	private void loadData() {
		adressIdTableColumn.setCellValueFactory(new PropertyValueFactory<Adress, Integer>("id"));
		adressCityTableColumn.setCellValueFactory(new PropertyValueFactory<Adress, String>("city"));
		adressPostalCodeTableColumn.setCellValueFactory(new PropertyValueFactory<Adress, String>("postalCode"));
		adressStreetTableColumn.setCellValueFactory(new PropertyValueFactory<Adress, String>("street"));
		adressHouseNumberTableColumn.setCellValueFactory(new PropertyValueFactory<Adress, Short>("houseNumber"));
		adressFlatNumberTableColumn.setCellValueFactory(new PropertyValueFactory<Adress, Integer>("flatNumber"));


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
			List list = session.createQuery("FROM Adress").list();
			ObservableList<Adress> data = FXCollections.observableList(list);
			adressTableView.setItems(data);
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
