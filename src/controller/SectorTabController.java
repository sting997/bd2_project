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

import bd2.Sector;
import bd2.SectorType;
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

public class SectorTabController {
	private SessionFactory factory;
	@FXML
	private TableView<Sector> sectorTableView;
	@FXML
	private TableColumn<Sector, Integer> sectorIdTableColumn;
	@FXML
	private TableColumn<Sector, String> sectorNameTableColumn;
	@FXML
	private TableColumn<Sector, String> sectorDateTableColumn;
	@FXML
	private TableColumn<Sector, Stadium> sectorStadiumTableColumn;
	@FXML
	private TableColumn<Sector, SectorType> sectorTypeTableColumn;
	@FXML
	private Tab sectorTab;
	@FXML
	private Button addSectorButton;
	@FXML
	private Button editSectorButton;
	@FXML
	private Button deleteSectorButton;
	@FXML
	Label infoLabel;

	@FXML
	public void initialize() {
		loadData();
		addSectorButton.setOnAction((ActionEvent event) -> {
			handleAdd();
		});
		deleteSectorButton.setOnAction((ActionEvent event) -> {
			handleDelete();
		});
		editSectorButton.setOnAction((ActionEvent event) -> {
			handleEdit();
		});
	}

	private void handleEdit() {
		infoLabel.setText("");
		int selectedIndex = sectorTableView.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			Sector newSector = sectorTableView.getItems().get(selectedIndex);
			HBox root = new HBox();
			TextField stadiumTextField = new TextField("" + newSector.getStadium().getId());
			TextField typeTextField = new TextField("" + newSector.getSectorType().getId());
			Button editButton = new Button("Edit");
			root.getChildren().add(stadiumTextField);
			root.getChildren().add(typeTextField);
			root.getChildren().add(editButton);

			editButton.setOnAction((ActionEvent event) -> {
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				Date date;
				try {
					int stadiumId = Integer.parseInt(stadiumTextField.getText());
					int typeId = Integer.parseInt(typeTextField.getText());
					Session session = factory.openSession();
					Transaction tx = null;
					try {
						tx = session.beginTransaction();
						Stadium stadium = (Stadium) session.get(Stadium.class, stadiumId);
						SectorType sectorType = (SectorType) session.get(SectorType.class, typeId);
						newSector.setStadium(stadium);
						newSector.setSectorType(sectorType);
						session.update(newSector);
						tx.commit();
						sectorTableView.getItems().set(selectedIndex, newSector);
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
			stage.setTitle("Edit Sector");
			stage.setScene(new Scene(root));
			stage.show();
		}
	}

	private void handleAdd() {
		infoLabel.setText("");
		HBox root = new HBox();
		TextField stadiumTextField = new TextField("Stadium id");
		TextField typeTextField = new TextField("Type id");
		Button addButton = new Button("Add");
		root.getChildren().add(stadiumTextField);
		root.getChildren().add(typeTextField);
		root.getChildren().add(addButton);

		addButton.setOnAction((ActionEvent event) -> {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date;
			try {
				int stadiumId = Integer.parseInt(stadiumTextField.getText());
				int typeId = Integer.parseInt(typeTextField.getText());
				Session session = factory.openSession();
				Transaction tx = null;
				try {
					tx = session.beginTransaction();
					Stadium stadium = (Stadium) session.get(Stadium.class, stadiumId);
					SectorType sectorType = (SectorType) session.get(SectorType.class, typeId);
					Sector newSector = new Sector();
					newSector.setStadium(stadium);
					newSector.setSectorType(sectorType);
					Integer newSectorId = (Integer) session.save(newSector);
					tx.commit();
					sectorTableView.getItems().add(newSector);
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
		stage.setTitle("Add Sector");
		stage.setScene(new Scene(root));
		stage.show();

	}

	private void handleDelete() {
		infoLabel.setText("");
		int selectedIndex = sectorTableView.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			Sector sector = sectorTableView.getItems().get(selectedIndex);
			Session session = factory.openSession();
			Transaction tx = null;
			try {
				tx = session.beginTransaction();
				session.delete(sector);
				tx.commit();
				sectorTableView.getItems().remove(selectedIndex);
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
		sectorIdTableColumn.setCellValueFactory(new PropertyValueFactory<Sector, Integer>("id"));
		sectorStadiumTableColumn.setCellValueFactory(new PropertyValueFactory<Sector, Stadium>("stadium"));
		sectorTypeTableColumn.setCellValueFactory(new PropertyValueFactory<Sector, SectorType>("sectorType"));

		
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
			List list = session.createQuery("FROM Sector").list();
			ObservableList<Sector> data = FXCollections.observableList(list);
			sectorTableView.setItems(data);
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
