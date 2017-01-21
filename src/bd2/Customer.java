package bd2;

import java.util.Iterator;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.cfg.Configuration;

@Entity
@Table(name="klienci")
public class Customer {
	private int id;
	private String firstName;
	private String secondName;
	private Adress adress;
	private String phoneNumer;
	private String email;
	private String bankAccount;
	private ClientType clientType;
	private float discount;

	@Id
	@Column(name="id_klienta")
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy="increment")
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Basic
	@Column(name="imie")
	@ColumnTransformer(
			  read="AES_DECRYPT(imie, 'pingantoniak')")
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	@Basic
	@Column(name="nazwisko")
	@ColumnTransformer(
			  read="AES_DECRYPT(nazwisko, 'pingantoniak')")
	public String getSecondName() {
		return secondName;
	}
	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}
	@ManyToOne
	@JoinColumn(name="adres_id_adresu")
	public Adress getAdress() {
		return adress;
	}
	public void setAdress(Adress adress) {
		this.adress = adress;
	}
	@Basic
	@Column(name="telefon")
	@ColumnTransformer(
			  read="AES_DECRYPT(telefon, 'pingantoniak')")
	public String getPhoneNumer() {
		return phoneNumer;
	}
	public void setPhoneNumer(String phoneNumer) {
		this.phoneNumer = phoneNumer;
	}
	@Basic
	@Column(name="email")
	@ColumnTransformer(
		  read="AES_DECRYPT(email, 'pingantoniak')") 
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@Basic
	@Column(name="numer_konta")
	@ColumnTransformer(
			  read="AES_DECRYPT(numer_konta, 'pingantoniak')")
	public String getBankAccount() {
		return bankAccount;
	}
	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}
	@ManyToOne
	@JoinColumn(name="typy_klientow_id_typu_klienta")
	public ClientType getClientType() {
		return clientType;
	}
	public void setClientType(ClientType clientType) {
		this.clientType = clientType;
	}
	@Column(name="rabat")
	public float getDiscount() {
		return discount;
	}
	public void setDiscount(float discount) {
		this.discount = discount;
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
			List adresses = session.createQuery("FROM Customer").list();
			for (Iterator iterator =adresses.iterator(); iterator.hasNext();){
				Customer adress = (Customer) iterator.next();
				System.out.print(adress.getId() + " ");
				System.out.print(adress.getFirstName() + " ");
				System.out.println(adress.getSecondName());
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
