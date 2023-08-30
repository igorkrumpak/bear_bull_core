package si.iitech.bear_bull_entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
@Table(name = "ERROR")
public class EtError extends PanacheEntity {

	private String message;
	private String errorMessage;
	private Date createdAt;

	public EtError() {
	}

	public EtError(String message, String errorMessage) {
		this.message = message;
		this.errorMessage = errorMessage;
	}

	@PrePersist
	protected void onCreate() {
		createdAt = new Date();
	}

	public static void create(String message, String errorMessage) {
		EtError error = new EtError(message, errorMessage);
		error.persist();
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public String getMessage() {
		return message;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
}
