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

import bd2.SectorType;
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

public class SectorTypeTabController {
	private SessionFactory factory;
	@FXML
	private TableView<SectorType> sectorTypeTableView;
	@FXML
	private TableColumn<SectorType, Integer> sectorTypeIdTableColumn;
	@FXML
	private TableColumn<SectorType, String> sectorTypeNameTableColumn;
	@FXML
	private Tab sectorTypeTab;
	@FXML
	private Button addSectorTypeButton;
	@FXML
	private Button editSectorTypeButton;
	@FXML
	private Button deleteSectorTypeButton;
	@FXML private Label infoLabel;

	@FXML
	public void initialize() {
		loadData();
		addSectorTypeButton.setOnAction((ActionEvent event) -> {
			handleAdd();
		});
		deleteSectorTypeButton.setOnAction((ActionEvent event) -> {
			handleDelete();
		});
		editSectorTypeButton.setOnAction((ActionEvent event) -> {
			handleEdit();
		});
		infoLabel.setTextFill(Color.FIREBRICK);
	}

	private void handleAdd() {
		infoLabel.setText("");
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
					SectorType newSectorType = new SectorType();
					newSectorType.setTypeName(name);
					Integer newSectorTypeId = (Integer) session.save(newSectorType);
					tx.commit();
					sectorTypeTableView.getItems().add(newSectorType);
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
		stage.setTitle("Add Client Type");
		stage.setScene(new Scene(root));
		stage.show();

	}

	private void handleDelete() {
		infoLabel.setText("");
		int selectedIndex = sectorTypeTableView.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			SectorType sectorType = sectorTypeTableView.getItems().get(selectedIndex);
			Session session = factory.openSession();
			Transaction tx = null;
			try {
				tx = session.beginTransaction();
				session.delete(sectorType);
				tx.commit();
				sectorTypeTableView.getItems().remove(selectedIndex);
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
		int selectedIndex = sectorTypeTableView.getSelectionModel().getSelectedIndex();

		if (selectedIndex >= 0) {
		SectorType sectorType = sectorTypeTableView.getItems().get(selectedIndex);

			HBox root = new HBox();
			TextField nameTextField = new TextField("" + sectorType.getTypeName());
			Button editButton = new Button("Edit");
			root.getChildren().add(nameTextField);
			root.getChildren().add(editButton);

			editButton.setOnAction((ActionEvent event) -> {
				try {
					String name = nameTextField.getText();

					Session session = factory.openSession();
					Transaction tx = null;
					try {
						tx = session.beginTransaction();
						sectorType.setTypeName(name);
						session.update(sectorType);
						tx.commit();
						sectorTypeTableView.getItems().set(selectedIndex, sectorType);

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
			stage.setTitle("Edit SectorType");
			stage.setScene(new Scene(root));
			stage.show();
		}
	}

	private void loadData() {
		sectorTypeIdTableColumn.setCellValueFactory(new PropertyValueFactory<SectorType, Integer>("id"));
		sectorTypeNameTableColumn.setCellValueFactory(new PropertyValueFactory<SectorType, String>("typeName"));

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
			List list = session.createQuery("FROM SectorType").list();
			ObservableList<SectorType> data = FXCollections.observableList(list);
			sectorTypeTableView.setItems(data);
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
