package bd2;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="typy_klientow")
public class ClientType {
	private int id;
	String name;
	
	@Id
	@Column(name="id_typu_klienta")
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy="increment")
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@Column(name = "typ")
	public String getTypeName() {
		return name;
	}
	public void setTypeName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString(){
		return this.getTypeName();
	}
	
	
}
