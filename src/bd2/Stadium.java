package bd2;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.jar.Attributes.Name;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.cfg.Configuration;

@Entity
@Table(name="stadiony")
public class Stadium {
	private byte id;
	private String name;
	private Adress adress;
	private Date openFrom;
	private Date openTo;
	
	@Id
	@Column(name="id_stadionu")
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy="increment")
	public byte getId() {
		return id;
	}
	public void setId(byte id) {
		this.id = id;
	}
	
	@Column(name="nazwa_stadionu")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@OneToOne
	@JoinColumn(name="adresy_id_adresu")
	public Adress getAdress() {
		return adress;
	}
	public void setAdress(Adress adress) {
		this.adress = adress;
	}
	
	@Temporal(TemporalType.TIME)
	@Column(name="czynny_od")
	public Date getOpenFrom() {
		return openFrom;
	}
	public void setOpenFrom(Date openFrom) {
		this.openFrom = openFrom;
	}
	
	@Temporal(TemporalType.TIME)
	@Column(name="czynny_do")
	public Date getOpenTo() {
		return openTo;
	}
	public void setOpenTo(Date openTo) {
		this.openTo = openTo;
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
			List result = session.createQuery("FROM Stadium").list();
			for (Iterator iterator =result.iterator(); iterator.hasNext();){
				Stadium stadium = (Stadium) iterator.next();
				System.out.print(stadium.getId() + " ");
				System.out.print(stadium.getAdress().getId() + " ");
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
	
	@Override
	public String toString() {
//		String string = new String();
//		return string + id;
		return name;
	}
	
}
