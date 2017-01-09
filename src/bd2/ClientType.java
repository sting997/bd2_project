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
	private byte id;
	String type;
	
	@Id
	@Column(name="id_typu_klienta")
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy="increment")
	public byte getId() {
		return id;
	}
	public void setId(byte id) {
		this.id = id;
	}
	
	@Column(name = "typ")
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
}
