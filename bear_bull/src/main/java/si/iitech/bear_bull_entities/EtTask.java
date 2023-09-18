package si.iitech.bear_bull_entities;

import java.util.Date;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

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
