package bd2;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="klienci")
public class Customer {
	private int id;
	private byte[] firstName;
	private byte[] secondName;
	private Adress adress;
	private byte[] phoneNumer;
	private byte[] email;
	private byte[] bankAccount;
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
	public byte[] getFirstName() {
		return firstName;
	}
	public void setFirstName(byte[] firstName) {
		this.firstName = firstName;
	}
	
	@Basic
	@Column(name="nazwisko")
	public byte[] getSecondName() {
		return secondName;
	}
	public void setSecondName(byte[] secondName) {
		this.secondName = secondName;
	}
	@ManyToOne
	@JoinColumn(name="adresy_id_adresu")
	public Adress getAdress() {
		return adress;
	}
	public void setAdress(Adress adress) {
		this.adress = adress;
	}
	@Basic
	@Column(name="telefon")
	public byte[] getPhoneNumer() {
		return phoneNumer;
	}
	public void setPhoneNumer(byte[] phoneNumer) {
		this.phoneNumer = phoneNumer;
	}
	@Basic
	@Column(name="email")
	public byte[] getEmail() {
		return email;
	}
	public void setEmail(byte[] email) {
		this.email = email;
	}
	@Basic
	@Column(name="numer_konta")
	public byte[] getBankAccount() {
		return bankAccount;
	}
	public void setBankAccount(byte[] bankAccount) {
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
	
	
}
