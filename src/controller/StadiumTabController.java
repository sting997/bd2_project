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

public class StadiumTabController {
	private SessionFactory factory;
	@FXML
	private TableView<Stadium> stadiumTableView;
	@FXML
	private TableColumn<Stadium, Integer> stadiumIdTableColumn;
	@FXML
	private TableColumn<Stadium, String> stadiumNameTableColumn;
	@FXML
	private TableColumn<Stadium, Adress> stadiumAdressTableColumn;
	@FXML
	private TableColumn<Stadium, String> stadiumOpenDateTableColumn;
	@FXML
	private TableColumn<Stadium, String> stadiumCloseDateTableColumn;
	@FXML
	private Tab stadiumTab;
	@FXML
	private Button addStadiumButton;
	@FXML
	private Button editStadiumButton;
	@FXML
	private Button deleteStadiumButton;
	@FXML
	Label infoLabel;

	@FXML
	public void initialize() {
		loadData();
		addStadiumButton.setOnAction((ActionEvent event) -> {
			handleAdd();
		});
		deleteStadiumButton.setOnAction((ActionEvent event) -> {
			handleDelete();
		});
		editStadiumButton.setOnAction((ActionEvent event) -> {
			handleEdit();
		});
	}

	private void handleEdit() {
		infoLabel.setText("");
		int selectedIndex = stadiumTableView.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			Stadium newStadium = stadiumTableView.getItems().get(selectedIndex);
			HBox root = new HBox();
			TextField nameTextField = new TextField(newStadium.getName());
			TextField addressTextField = new TextField("" + newStadium.getAdress().getId());
			TextField openDateTextField = new TextField("" + newStadium.getOpenFrom());
			TextField closeDateTextField = new TextField("" + newStadium.getOpenTo());
			Button editButton = new Button("Edit");
			root.getChildren().add(nameTextField);
			root.getChildren().add(addressTextField);
			root.getChildren().add(openDateTextField);
			root.getChildren().add(closeDateTextField);
			root.getChildren().add(editButton);

			editButton.setOnAction((ActionEvent event) -> {
				DateFormat dateFormat = new SimpleDateFormat("HH-mm");
				Date openDate, closeDate;
				try {
					int addressId = Integer.parseInt(addressTextField.getText());

					String name = nameTextField.getText();
					openDate = dateFormat.parse(openDateTextField.getText());
					closeDate = dateFormat.parse(closeDateTextField.getText());
					Session session = factory.openSession();
					Transaction tx = null;
					try {
						tx = session.beginTransaction();
						Adress address = (Adress) session.get(Adress.class, addressId);
						newStadium.setName(name);
						newStadium.setOpenFrom(openDate);
						newStadium.setOpenTo(closeDate);
						newStadium.setAdress(address);
						session.update(newStadium);
						tx.commit();
						stadiumTableView.getItems().set(selectedIndex, newStadium);
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
			stage.setTitle("Edit Stadium");
			stage.setScene(new Scene(root));
			stage.show();
		}
	}

	private void handleAdd() {
		infoLabel.setText("");
		HBox root = new HBox();
		TextField nameTextField = new TextField("Name");
		TextField addressTextField = new TextField("Address id");
		TextField openDateTextField = new TextField("Open From");
		TextField closeDateTextField = new TextField("Open To");
		Button addButton = new Button("Add");
		root.getChildren().add(nameTextField);
		root.getChildren().add(addressTextField);
		root.getChildren().add(openDateTextField);
		root.getChildren().add(closeDateTextField);
		root.getChildren().add(addButton);
		
		addButton.setOnAction((ActionEvent event) -> {
			DateFormat dateFormat = new SimpleDateFormat("HH:mm");
			Date openDate, closeDate;
			try {
				int addressId = Integer.parseInt(addressTextField.getText());

				String name = nameTextField.getText();
				openDate = dateFormat.parse(openDateTextField.getText());
				closeDate = dateFormat.parse(closeDateTextField.getText());
				Session session = factory.openSession();
				Transaction tx = null;
				try {
					tx = session.beginTransaction();
					Adress address = (Adress) session.get(Adress.class, addressId);
					Stadium newStadium = new Stadium();
					newStadium.setName(name);
					newStadium.setOpenFrom(openDate);
					newStadium.setOpenTo(closeDate);
					newStadium.setAdress(address);
					Integer newStadiumId = (Integer) session.save(newStadium);
					tx.commit();
					stadiumTableView.getItems().add(newStadium);					
					
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
		stage.setTitle("Add Stadium");
		stage.setScene(new Scene(root));
		stage.show();

	}

	private void handleDelete() {
		infoLabel.setText("");
		int selectedIndex = stadiumTableView.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			Stadium stadium = stadiumTableView.getItems().get(selectedIndex);
			Session session = factory.openSession();
			Transaction tx = null;
			try {
				tx = session.beginTransaction();
				session.delete(stadium);
				tx.commit();
				stadiumTableView.getItems().remove(selectedIndex);
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
		stadiumIdTableColumn.setCellValueFactory(new PropertyValueFactory<Stadium, Integer>("id"));
		stadiumNameTableColumn.setCellValueFactory(new PropertyValueFactory<Stadium, String>("name"));
		stadiumOpenDateTableColumn.setCellValueFactory(stadium -> {
			SimpleStringProperty property = new SimpleStringProperty();
			DateFormat df = new SimpleDateFormat("HH:mm");
			property.setValue(df.format(stadium.getValue().getOpenFrom()));
			return property;
		});
		stadiumCloseDateTableColumn.setCellValueFactory(stadium -> {
			SimpleStringProperty property = new SimpleStringProperty();
			DateFormat df = new SimpleDateFormat("HH:mm");
			property.setValue(df.format(stadium.getValue().getOpenTo()));
			return property;
		});
		stadiumAdressTableColumn.setCellValueFactory(new PropertyValueFactory<Stadium, Adress>("adress"));

		
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
			List list = session.createQuery("FROM Stadium").list();
			ObservableList<Stadium> data = FXCollections.observableList(list);
			stadiumTableView.setItems(data);
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
