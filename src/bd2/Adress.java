package bd2;

import java.util.Iterator;
import java.util.List;

import javax.persistence.*;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.cfg.Configuration;


@Entity
@Table(name="adresy")
public class Adress {
	private int id;
	private String city;
	private String street;
	private short houseNumber;
	private String postalCode;
	private Integer flatNumber = new Integer(0);
	
	@Id
	@Column(name="id_adresu")
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@Column(name="miasto")
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	
	@Column(name="ulica")
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	
	@Column(name="nr_domu")
	public short getHouseNumber() {
		return houseNumber;
	}
	public void setHouseNumber(short houseNumber) {
		this.houseNumber = houseNumber;
	}
	
	@Column(name="kod_pocztowy")
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	
	@Column(name="nr_mieszkania")
	public Integer getFlatNumber() {
		return flatNumber;
	}
	public void setFlatNumber(Integer flatNumber) {
		this.flatNumber = flatNumber;
	}
	
	public static void main(String[] args) {
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
			List adresses = session.createQuery("FROM Adress").list();
			for (Iterator iterator =adresses.iterator(); iterator.hasNext();){
				Adress adress = (Adress) iterator.next();
				System.out.print(adress.getId() + " ");
				System.out.print(adress.getCity() + " ");
				System.out.println(adress.getPostalCode());
			}
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
