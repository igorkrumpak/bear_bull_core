package si.iitech.bear_bull_entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
@Table(name = "TASK")
public class EtTask extends PanacheEntity {

	public String name;
	public Date createdAt;

	public EtTask() {
	}

	public EtTask(String name) {
		this.name = name;
	}

	@PrePersist
	protected void onCreate() {
		createdAt = new Date();
	}

	public String getName() {
		return name;
	}

	public Date getCreatedAt() {
		return createdAt;
	}
}
