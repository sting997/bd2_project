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
import bd2.ClientType;
import bd2.Customer;


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

public class CustomerTabController {
	private SessionFactory factory;
	@FXML
	private TableView<Customer> customerTableView;
	@FXML
	private TableColumn<Customer, Integer> customerIdTableColumn;
	@FXML
	private TableColumn<Customer, String> customerFirstNameTableColumn;
	@FXML
	private TableColumn<Customer, String> customerSecondNameTableColumn;
	@FXML
	private TableColumn<Customer, ClientType> customerClientTypeTableColumn;
	@FXML
	private TableColumn<Customer, String> customerPhoneNumerTableColumn;
	@FXML
	private TableColumn<Customer, String> customerEmailTableColumn;
	@FXML
	private TableColumn<Customer, Adress> customerAdressTableColumn;
	@FXML
	private TableColumn<Customer, Float> customerDiscountTableColumn;
	@FXML
	private TableColumn<Customer, String> customerBankAccountTableColumn;
	@FXML
	private Tab customerTab;
	@FXML
	private Button addCustomerButton;
	@FXML
	private Button editCustomerButton;
	@FXML
	private Button deleteCustomerButton;
	@FXML 
	private Label infoLabel;
	@FXML
	public void initialize() {
		loadData();
		if(addCustomerButton == null)
		     System.out.println("Hello, World");
		addCustomerButton.setOnAction((ActionEvent event) -> {
			handleAdd();
		});
		deleteCustomerButton.setOnAction((ActionEvent event) -> {
			handleDelete();
		});
		editCustomerButton.setOnAction((ActionEvent event) -> {
			handleEdit();
		});
		infoLabel.setTextFill(Color.FIREBRICK);

	}

	private void handleAdd() {
		infoLabel.setText("");
		HBox root = new HBox();
		TextField firstNameTextField = new TextField("First Name");
		TextField secondNameTextField = new TextField("Second Name");
		TextField clientTypeTextField = new TextField("Client Type");
		TextField phoneNumberTextField = new TextField("Phone Number");
		TextField emailTextField = new TextField("Email");
		TextField adressTextField = new TextField("Address");
		TextField discountTextField = new TextField("Discount");
		TextField bankAccountTextField = new TextField("Bank Account");
		
		

		Button addButton = new Button("Add");
		root.getChildren().add(firstNameTextField);
		root.getChildren().add(secondNameTextField);
		root.getChildren().add(clientTypeTextField);
		root.getChildren().add(phoneNumberTextField);
		root.getChildren().add(emailTextField);
		root.getChildren().add(adressTextField);
		root.getChildren().add(discountTextField);
		root.getChildren().add(bankAccountTextField);
		root.getChildren().add(addButton);

		addButton.setOnAction((ActionEvent event) -> {
			try {
				
				String firstName = firstNameTextField.getText();
				String secondName = secondNameTextField.getText();
				String email = emailTextField.getText();
				String phoneNumber = phoneNumberTextField.getText();
				String bankAccount = bankAccountTextField.getText();
				int clientTypeId = Integer.parseInt(clientTypeTextField.getText());
				float discount = Float.parseFloat(discountTextField.getText());
				int adressId = Integer.parseInt(adressTextField.getText());
				
				Session session = factory.openSession();
				Transaction tx = null;
				try {
					tx = session.beginTransaction();
					Adress adress = (Adress) session.get(Adress.class, adressId);
					ClientType clientType = (ClientType) session.get(ClientType.class, clientTypeId);
					Customer newCustomer = new Customer();
					
					newCustomer.setFirstName(firstName);
					newCustomer.setSecondName(secondName);
					newCustomer.setEmail(email);
					newCustomer.setPhoneNumer(phoneNumber);
					newCustomer.setBankAccount(bankAccount);
					newCustomer.setDiscount(discount);
					newCustomer.setAdress(adress);
					newCustomer.setClientType(clientType);
					
					Integer newCustomerId = (Integer) session.save(newCustomer);
					tx.commit();
					customerTableView.getItems().add(newCustomer);
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
		stage.setTitle("Add Customer");
		stage.setScene(new Scene(root));
		stage.show();

	}

	private void handleDelete() {
		infoLabel.setText("");
		int selectedIndex = customerTableView.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			Customer customer = customerTableView.getItems().get(selectedIndex);
			Session session = factory.openSession();
			Transaction tx = null;
			try {
				tx = session.beginTransaction();
				session.delete(customer);
				tx.commit();
				customerTableView.getItems().remove(selectedIndex);
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
		int selectedIndex = customerTableView.getSelectionModel().getSelectedIndex();

		if (selectedIndex >= 0) {
			Customer customer = customerTableView.getItems().get(selectedIndex);

			HBox root = new HBox();
			TextField firstNameTextField = new TextField(customer.getFirstName());
			TextField secondNameTextField = new TextField(customer.getSecondName());
			TextField clientTypeTextField = new TextField("" + customer.getClientType().getId());
			TextField phoneNumberTextField = new TextField(customer.getPhoneNumer());
			TextField emailTextField = new TextField(customer.getEmail());
			TextField adressTextField = new TextField("" + customer.getAdress().getId());
			TextField discountTextField = new TextField("" + customer.getDiscount());
			TextField bankAccountTextField = new TextField(customer.getBankAccount());
			
			Button editButton = new Button("Edit");
			root.getChildren().add(firstNameTextField);
			root.getChildren().add(secondNameTextField);
			root.getChildren().add(clientTypeTextField);
			root.getChildren().add(phoneNumberTextField);
			root.getChildren().add(emailTextField);
			root.getChildren().add(adressTextField);
			root.getChildren().add(discountTextField);
			root.getChildren().add(bankAccountTextField);
			root.getChildren().add(editButton);

			editButton.setOnAction((ActionEvent event) -> {
				try {
					String firstName = firstNameTextField.getText();
					String secondName = secondNameTextField.getText();
					String email = emailTextField.getText();
					String phoneNumber = phoneNumberTextField.getText();
					String bankAccount = bankAccountTextField.getText();
					int clientTypeId = Integer.parseInt(clientTypeTextField.getText());
					float discount = Float.parseFloat(discountTextField.getText());
					int adressId = Integer.parseInt(adressTextField.getText());

					Session session = factory.openSession();
					Transaction tx = null;
					try {
						tx = session.beginTransaction();
						Adress adress = (Adress) session.get(Adress.class, adressId);
						ClientType clientType = (ClientType) session.get(ClientType.class, clientTypeId);
						
						customer.setFirstName(firstName);
						customer.setSecondName(secondName);
						customer.setEmail(email);
						customer.setPhoneNumer(phoneNumber);
						customer.setBankAccount(bankAccount);
						customer.setDiscount(discount);
						customer.setAdress(adress);
						customer.setClientType(clientType);
						
						session.update(customer);
						
						tx.commit();
						customerTableView.getItems().set(selectedIndex, customer);

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
			stage.setTitle("Edit Customer");
			stage.setScene(new Scene(root));
			stage.show();
		}
	}

	private void loadData() {
		customerIdTableColumn.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("id"));
		customerFirstNameTableColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("firstName"));
		customerSecondNameTableColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("secondName"));
		customerClientTypeTableColumn.setCellValueFactory(new PropertyValueFactory<Customer, ClientType>("clientType"));
		customerPhoneNumerTableColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("phoneNumer"));
		customerEmailTableColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("email"));
		customerAdressTableColumn.setCellValueFactory(new PropertyValueFactory<Customer, Adress>("adress"));
		customerDiscountTableColumn.setCellValueFactory(new PropertyValueFactory<Customer, Float>("discount"));
		customerBankAccountTableColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("bankAccount"));


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
			List list = session.createQuery("FROM Customer").list();
			ObservableList<Customer> data = FXCollections.observableList(list);
			customerTableView.setItems(data);
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
