package si.iitech.bear_bull_entities;

import java.util.Date;
import java.util.List;

import org.hibernate.annotations.JdbcType;
import org.hibernate.type.descriptor.jdbc.VarbinaryJdbcType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@JsonIgnoreProperties(value = { "report", "byteArrayValue", "calculator", "error", "lastUpdate" }, allowGetters = false)
@Table(name = "METADATA")
public class EtMetadata extends PanacheEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	private EtReport report;

	@ManyToOne(fetch = FetchType.LAZY)
	private EtMetadataCalculator calculator;

	private Double doubleValue;
	private Integer intValue;
	private String stringValue;
	private Boolean booleanValue;

	public EtMetadata() {

	}

	@Lob
	@JdbcType(VarbinaryJdbcType.class)
	@Column(length = 100000)
	private byte[] byteArrayValue;

	private String error;
	private Date lastUpdate;
	private String notation;

	public EtReport getReport() {
		return report;
	}

	public void setReport(EtReport report) {
		this.report = report;
	}

	public EtMetadataCalculator getCalculator() {
		return calculator;
	}

	public void setCalculator(EtMetadataCalculator calculator) {
		this.calculator = calculator;
	}

	public Double getDoubleValue() {
		return doubleValue;
	}

	public void setDoubleValue(Double doubleValue) {
		this.doubleValue = doubleValue;
	}

	public Integer getIntValue() {
		return intValue;
	}

	public void setIntValue(Integer intValue) {
		this.intValue = intValue;
	}

	public String getStringValue() {
		return stringValue;
	}

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

	public Boolean getBooleanValue() {
		return booleanValue;
	}

	public void setBooleanValue(Boolean booleanValue) {
		this.booleanValue = booleanValue;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getNotation() {
		return notation;
	}

	public void setNotation(String notation) {
		this.notation = notation;
	}

	public byte[] getByteArrayValue() {
		return byteArrayValue;
	}

	public void setByteArrayValue(byte[] byteArrayValue) {
		this.byteArrayValue = byteArrayValue;
	}

	public static List<EtMetadata> findByReportId(Long reportId) {
		return EtMetadata.find("report.id = ?1", reportId).list();
	}

	public static long deleteByReportId(Long reportId) {
		return EtMetadata.delete("report.id = ?1", reportId);
	}
}
