package bd2;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="sektory")
public class Sector {
	private byte id;
	private Stadium stadium;
	private SectorType sectorType;
	
	@Id
	@Column(name="id_sektora")
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy="increment")
	public byte getId() {
		return id;
	}
	public void setId(byte id) {
		this.id = id;
	}
	@ManyToOne
	@JoinColumn(name="stadiony_id_stadionu")
	public Stadium getStadium() {
		return stadium;
	}
	public void setStadium(Stadium stadium) {
		this.stadium = stadium;
	}
	
	@ManyToOne
	@JoinColumn(name="sektory_id_sektora")
	public SectorType getSectorType() {
		return sectorType;
	}
	public void setSectorType(SectorType sectorType) {
		this.sectorType = sectorType;
	}
	
	
}
