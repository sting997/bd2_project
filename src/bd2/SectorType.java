package bd2;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="typy_sektora")
public class SectorType {
	private int id;
	private String name;
	
	@Id
	@Column(name="id_typu")
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy="increment")
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@Column(name="nazwa_typu")
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
