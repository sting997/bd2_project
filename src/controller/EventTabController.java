package controller;

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
import bd2.Event;
import bd2.EventType;
import bd2.Stadium;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class EventTabController {
    @FXML private TableView<Event> eventTableView;
	@FXML private TableColumn eventIdTableColumn;
	@FXML private TableColumn eventNameTableColumn;
	@FXML private TableColumn eventDateTableColumn;
	@FXML private TableColumn<Event, Stadium> eventStadiumTableColumn;
	@FXML private TableColumn eventTypeTableColumn;
	@FXML private Tab eventTab;
	@FXML private TextField idTextField;
	@FXML private TextField nameTextField;
	@FXML private TextField dateTextField;
	@FXML private TextField stadiumTextField;
	@FXML private TextField typeTextField;
	@FXML private Button addButton;
	@FXML
	public void initialize() {
	    loadData();
		addButton.setOnAction((ActionEvent event)-> {
			handleAdd();
		});
    }
	private void handleAdd() {
		
	}
	private void loadData(){
		eventIdTableColumn.setCellValueFactory(new PropertyValueFactory<Event,String>("id"));
    	eventNameTableColumn.setCellValueFactory(new PropertyValueFactory<Event, String>("name"));
    	eventDateTableColumn.setCellValueFactory(new PropertyValueFactory<Event, Date>("date"));
    	eventStadiumTableColumn.setCellValueFactory(new PropertyValueFactory<Event, Stadium>("stadium"));
    	eventTypeTableColumn.setCellValueFactory(new PropertyValueFactory<Event, String>("eventType"));;
    	SessionFactory factory;
		try {
			factory = new Configuration().configure("/resources/hibernate.cfg.xml").buildSessionFactory();
		} catch (Throwable ex) {
			System.err.println("Failed to create sessionFactory object." + ex);
			throw new ExceptionInInitializerError(ex);
		}
		
		Session session = factory.openSession();
		Transaction tx = null;
		try{
			tx = session.beginTransaction();
			List list = session.createQuery("FROM Event").list();
			ObservableList<Event> data = FXCollections.observableList(list);
			eventTableView.setItems(data);
			tx.commit();
		}catch (HibernateException e) {
			if (tx!=null) tx.rollback();
			e.printStackTrace();
		}finally {
			session.close();
		}
		
		factory.close();
	}
	
	 
}
