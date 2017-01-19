package controller;

import java.util.Iterator;
import java.util.List;

import javax.persistence.Table;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import bd2.Adress;
import bd2.Event;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class Controller {
    @FXML private TableView<Event> eventTableView;
	@FXML private TableColumn eventIdTableColumn;
	@FXML private TableColumn eventNameTableColumn;
	@FXML private TableColumn eventDateTableColumn;
	@FXML private TableColumn eventStadiumTableColumn;
	@FXML private TableColumn eventTypeTableColumn;
	@FXML
	private void initialize() {
	    eventIdTableColumn.setCellValueFactory(new PropertyValueFactory<Event,Integer>("id"));
    	eventNameTableColumn.setCellValueFactory(new PropertyValueFactory<Event, String>("name"));
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
	  
    @FXML public void eventSelectionHandler(ActionEvent actionEvent) {
//    	
//    	eventIdTableColumn.setCellValueFactory(new PropertyValueFactory<Event,Integer>("id"));
//    	eventNameTableColumn.setCellValueFactory(new PropertyValueFactory<Event, String>("name"));
//    	SessionFactory factory;
//		try {
//			factory = new Configuration().configure("/resources/hibernate.cfg.xml").buildSessionFactory();
//		} catch (Throwable ex) {
//			System.err.println("Failed to create sessionFactory object." + ex);
//			throw new ExceptionInInitializerError(ex);
//		}
//		
//		Session session = factory.openSession();
//		Transaction tx = null;
//		try{
//			tx = session.beginTransaction();
//			List list = session.createQuery("FROM Event").list();
//			ObservableList<Event> data = FXCollections.observableList(list);
//			eventTableView.setItems(data);
//			tx.commit();
//		}catch (HibernateException e) {
//			if (tx!=null) tx.rollback();
//			e.printStackTrace();
//		}finally {
//			session.close();
//		}
//		
//		factory.close();
    }
}
