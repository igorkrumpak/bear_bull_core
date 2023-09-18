package si.iitech.bear_bull_entities;

import java.util.List;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity
@Table(name = "METADATA_CALCULATOR")
public class EtMetadataCalculator extends PanacheEntity implements IMetadataCalculator {

	private String name;

	@Column(length = 1000)
	private String description;

	private String notation;

	@Column(length = 1000)
	private String code;
	
	private Boolean deleted = false;

	@Enumerated(EnumType.STRING)
	private ResultType resultType;

	@Enumerated(EnumType.STRING)
	private ChartCategory chartCategory;

	@Enumerated(EnumType.STRING)
	private ChartColor chartColor;

	private Integer index;

	private Boolean isUsedInDashboard = false;

	private Boolean isUsedInReport = false;

	private Boolean isChartEnabled = false;

	private Boolean isInput = false;

	@Override
	public String getNotation() {
		return notation;
	}

	public void setNotation(String notation) {
		this.notation = notation;
	}

	@Override
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public ResultType getResultType() {
		return resultType;
	}

	public void setResultType(ResultType resultType) {
		this.resultType = resultType;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public static List<EtMetadataCalculator> findAllForChart() {
		return EtMetadataCalculator.find(
				"select calculator from EtMetadataCalculator calculator where deleted = FALSE and chartCategory is not null order by calculator.index asc")
				.list();
	}

	public static List<EtMetadataCalculator> listAllForReportOrderByIndexAsc() {
		return EtMetadataCalculator.find(
				"select calculator from EtMetadataCalculator calculator where deleted = FALSE and isUsedInReport = TRUE order by calculator.index asc")
				.list();
	}

	public static List<EtMetadataCalculator> listAllForDashboardOrderByIndexAsc() {
		return EtMetadataCalculator.find(
				"select calculator from EtMetadataCalculator calculator where deleted = FALSE and isUsedInDashboard = TRUE order by calculator.index asc")
				.list();
	}

	public static List<EtMetadataCalculator> listAllForInput() {
		return EtMetadataCalculator.find(
				"select calculator from EtMetadataCalculator calculator where deleted = FALSE and isInput = TRUE")
				.list();
	}

	public static EtMetadataCalculator findByNotation(String notation) {
		return EtMetadataCalculator.find("notation = ?1", notation).firstResult();
	}

    public Boolean getIsUsedInDashboard() {
		return isUsedInDashboard;
	}

	public Boolean getIsUsedInReport() {
		return isUsedInReport;
	}

	public void setIsUsedInDashboard(Boolean isUsedInDashboard) {
		this.isUsedInDashboard = isUsedInDashboard;
	}

	public void setIsUsedInReport(Boolean isUsedInReport) {
		this.isUsedInReport = isUsedInReport;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ChartCategory getChartCategory() {
		return chartCategory;
	}

	public void setChartCategory(ChartCategory chartCategory) {
		this.chartCategory = chartCategory;
	}

    public void setChartColor(ChartColor chartColor) {
		this.chartColor = chartColor;
	}

	public ChartColor getChartColor() {
		return chartColor;
	}

	public Boolean getIsChartEnabled() {
		return isChartEnabled;
	}

	public void setIsChartEnabled(Boolean isChartEnabled) {
		this.isChartEnabled = isChartEnabled;
	}

	public Boolean getIsInput() {
		return isInput;
	}

	public void setIsInput(Boolean isInput) {
		this.isInput = isInput;
	}
}
