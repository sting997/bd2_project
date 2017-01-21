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

import bd2.Seat;
import bd2.Sector;

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

public class SeatTabController {
	private SessionFactory factory;
	@FXML
	private TableView<Seat> seatTableView;
	@FXML
	private TableColumn<Seat, Integer> seatIdTableColumn;
	@FXML
	private TableColumn<Seat, Sector> seatSectorTableColumn;
	@FXML
	private TableColumn<Seat, Integer> seatRowNumberTableColumn;
	@FXML
	private TableColumn<Seat, Integer> seatNumberTableColumn;
	@FXML
	private Tab seatTab;
	@FXML
	private Button addSeatButton;
	@FXML
	private Button editSeatButton;
	@FXML
	private Button deleteSeatButton;
	@FXML private Label infoLabel;
	@FXML
	public void initialize() {
		loadData();
		addSeatButton.setOnAction((ActionEvent event) -> {
			handleAdd();
		});
		deleteSeatButton.setOnAction((ActionEvent event) -> {
			handleDelete();
		});
		editSeatButton.setOnAction((ActionEvent event) -> {
			handleEdit();
		});
		infoLabel.setTextFill(Color.FIREBRICK);
	}

	private void handleAdd() {
		infoLabel.setText("");
		HBox root = new HBox();
		TextField sectorTextField = new TextField("Sector id");
		TextField rowNumberTextField = new TextField("Row Number");
		TextField seatNumberTextField = new TextField("Seat Number");
		Button addButton = new Button("Add");
		root.getChildren().add(sectorTextField);
		root.getChildren().add(rowNumberTextField);
		root.getChildren().add(seatNumberTextField);
		root.getChildren().add(addButton);

		addButton.setOnAction((ActionEvent event) -> {
			try {
				byte sectorId = Byte.parseByte(sectorTextField.getText());
				int rowNumber = Integer.parseInt(rowNumberTextField.getText());
				int seatNumber = Integer.parseInt(seatNumberTextField.getText());
				Session session = factory.openSession();
				Transaction tx = null;
				try {
					tx = session.beginTransaction();
					Sector sector = (Sector) session.get(Sector.class, sectorId);
					Seat newSeat = new Seat();
					newSeat.setSector(sector);
					newSeat.setRowNumber(rowNumber);
					newSeat.setSeatNumber(seatNumber);
					Integer newSeatId = (Integer) session.save(newSeat);
					tx.commit();
					seatTableView.getItems().add(newSeat);
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
		stage.setTitle("Add Event");
		stage.setScene(new Scene(root));
		stage.show();

	}

	private void handleDelete() {
		infoLabel.setText("");
		int selectedIndex = seatTableView.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			Seat seat = seatTableView.getItems().get(selectedIndex);
			Session session = factory.openSession();
			Transaction tx = null;
			try {
				tx = session.beginTransaction();
				session.delete(seat);
				tx.commit();
				seatTableView.getItems().remove(selectedIndex);
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
		int selectedIndex = seatTableView.getSelectionModel().getSelectedIndex();

		if (selectedIndex >= 0) {
			Seat seat = seatTableView.getItems().get(selectedIndex);

			HBox root = new HBox();
			TextField sectorTextField = new TextField("Sector id");
			TextField rowNumberTextField = new TextField("Row Number");
			TextField seatNumberTextField = new TextField("Seat Number");
			Button editButton = new Button("Edit");
			root.getChildren().add(sectorTextField);
			root.getChildren().add(rowNumberTextField);
			root.getChildren().add(seatNumberTextField);
			root.getChildren().add(editButton);

			editButton.setOnAction((ActionEvent event) -> {
				try {
					byte sectorId = Byte.parseByte(sectorTextField.getText());
					int rowNumber = Integer.parseInt(rowNumberTextField.getText());
					int seatNumber = Integer.parseInt(seatNumberTextField.getText());

					Session session = factory.openSession();
					Transaction tx = null;
					try {
						tx = session.beginTransaction();
						Sector sector = (Sector) session.get(Sector.class, sectorId);
						seat.setSector(sector);
						seat.setRowNumber(rowNumber);
						seat.setSeatNumber(seatNumber);
						session.update(seat);
						tx.commit();
						seatTableView.getItems().set(selectedIndex, seat);

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
			stage.setTitle("Edit Seat");
			stage.setScene(new Scene(root));
			stage.show();
		}
	}
	
	private void loadData() {
		seatIdTableColumn.setCellValueFactory(new PropertyValueFactory<Seat, Integer>("id"));
		seatSectorTableColumn.setCellValueFactory(new PropertyValueFactory<Seat, Sector>("sector"));
		seatRowNumberTableColumn.setCellValueFactory(new PropertyValueFactory<Seat, Integer>("rowNumber"));
		seatNumberTableColumn.setCellValueFactory(new PropertyValueFactory<Seat, Integer>("seatNumber"));

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
			List list = session.createQuery("FROM Seat").list();
			ObservableList<Seat> data = FXCollections.observableList(list);
			seatTableView.setItems(data);
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
