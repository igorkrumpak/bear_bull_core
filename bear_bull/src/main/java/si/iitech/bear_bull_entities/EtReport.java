package si.iitech.bear_bull_entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.TypedQuery;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
@Table(name = "REPORT")
@JsonIgnoreProperties({ "reportType", "metadatasCount", "metadatasErrorCount" })
public class EtReport extends PanacheEntity {

	@ManyToOne
	private EtCoin coin;
	private Date reportDate;
	private Long metadatasCount = 0L;
	private Long inputMetadatasCount = 0L;
	private Long metadatasErrorCount = 0L;
	private boolean dashboardReport;

	@OneToMany(mappedBy = "report", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<EtMetadata> metadatas = new ArrayList<>();

	@Enumerated(EnumType.STRING)
	private ReportType reportType;

	public EtReport() {
	}

	public EtReport(EtCoin coin, Date reportDate, ReportType reportType) {
		this.coin = coin;
		this.reportDate = reportDate;
		this.reportType = reportType;
	}
	
	public boolean isDashboardReport() {
		return dashboardReport;
	}
	
	public void setDashboardReport(boolean dashboardReport) {
		this.dashboardReport = dashboardReport;
	}

	public EtCoin getCoin() {
		return coin;
	}

	public Date getReportDate() {
		return reportDate;
	}

	public ReportType getReportType() {
		return reportType;
	}

	public List<EtMetadata> getMetadatas() {
		return metadatas;
	}

	public void addMetadata(EtMetadata metadata) {
		this.metadatas.add(metadata);
	}

	public void setMetadatasCount(Long metadatasCount) {
		this.metadatasCount = metadatasCount;
	}

	public Long getMetadatasCount() {
		return metadatasCount;
	}

	public void setMetadatas(List<EtMetadata> metadatas) {
		this.metadatas = metadatas;
	}

	public Long getMetadatasErrorCount() {
		return metadatasErrorCount;
	}

	public void setMetadatasErrorCount(Long metadatasErrorCount) {
		this.metadatasErrorCount = metadatasErrorCount;
	}

	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}

	public void setInputMetadatasCount(Long inputMetadatasCount) {
		this.inputMetadatasCount = inputMetadatasCount;
	}

	public Long getInputMetadatasCount() {
		return inputMetadatasCount;
	}

	public static EtReport findDashboardReport(Long coinId) {
		return EtReport.find("select r from EtReport r where r.coin.id = ?1 and r.reportType = 'DASHBOARD'", coinId)
				.firstResult();
	}

	public static Date findLatestReportDate(Long coinId) {
		TypedQuery<Date> query = getEntityManager().createQuery(
				"select r.reportDate from EtReport r where r.coin.id = ?1 order by reportDate desc", Date.class);
		query.setParameter(1, coinId);
		List<Date> dates = query.getResultList();
		return dates.size() > 0 ? dates.get(0) : null;
	}

	public static List<EtReport> getReportsAscUntil(Long coinId, Date until) {
		return EtReport.find(
				"select DISTINCT r from EtReport r join fetch r.metadatas m where r.coin.id = ?1 and r.reportDate >= ?2 order by r.reportDate asc",
				coinId, until).list();
	}

	public static EtReport getReportWithMetadata(Long reportId) {
		return EtReport.find(
				"select DISTINCT r from EtReport r join fetch r.metadatas m where r.id = ?1",
				reportId).singleResult();
	}


	public static List<EtReport> getReportsDescUntil(Long coinId, Date until, ReportType reportType) {
		return EtReport.find(
				"select DISTINCT r from EtReport r join fetch r.metadatas m where r.coin.id = ?1 and r.reportDate <= ?2 AND r.reportType = ?3 order by r.reportDate desc",
				coinId, until, reportType).list();
	}

	public static List<EtReport> getReportsDesc(Long coinId, Date from, Date until) {
		return EtReport.find(
				"select DISTINCT r from EtReport r join fetch r.metadatas m where r.coin.id = ?1 and r.reportDate >= ?2 and r.reportDate <= ?3 AND r.reportType = 'DAILY' order by r.reportDate desc",
				coinId, from, until).list();
	}

	public static List<EtReport> getReportsAsc(Long coinId) {
		return EtReport.find("coin.id = ?1 and reportType = 'DAILY' order by reportDate asc", coinId).list();
	}

	public static List<EtReport> getReportsAsc(Long coinId, Date from) {
		return EtReport
				.find("coin.id = ?1 and reportType = 'DAILY' and reportDate > ?2 order by reportDate asc", coinId, from)
				.list();
	}

	public static List<Date> getReportDates(Long coinId, Date until, ReportType reportType) {
		TypedQuery<Date> query = getEntityManager().createQuery(
				"select r.reportDate from EtReport r where r.coin.id = ?1 and reportType = ?3 and r.reportDate <= ?2 order by r.reportDate desc",
				Date.class);
		query.setParameter(1, coinId);
		query.setParameter(2, until);
		query.setParameter(3, reportType);
		return query.getResultList();
	}

	public static List<EtReport> getReportsWithEmptyMetadatas(Long coinId) {
		return EtReport
				.find("select r from EtReport r where r.metadatasCount = 0 and r.coin.id = ?1 and reportType = 'DAILY'",
						coinId)
				.list();
	}

	
	public static List<EtReport> getDailyReportsWithMissingMetadatas(Long coinId, long metadatasSize) {
		return EtReport.find(
				"select DISTINCT r from EtReport r join fetch r.metadatas m where r.coin.id = ?1 and r.metadatasCount < ?2 and reportType = 'DAILY' order by r.reportDate asc",
				coinId, metadatasSize).list();
	}
	 
	public static List<EtReport> getReportsWithMissingMetadatas(Long coinId, long metadatasSize, ReportType reportType) {
		return EtReport.find(
				"select DISTINCT r from EtReport r join fetch r.metadatas m where r.coin.id = ?1 and r.metadatasCount < ?2 and r.reportType = ?3 and r.dashboardReport is false order by r.reportDate asc",
				coinId, metadatasSize, reportType).list();
	}


	public static List<EtReport> getReportsWithErrorMetadatas(Long coinId, ReportType reportType) {
		return EtReport.find(
				"select DISTINCT r from EtReport r join fetch r.metadatas m where r.coin.id = ?1 and r.metadatasErrorCount > 0 and r.reportType = ?2 and r.dashboardReport is false order by r.reportDate asc",
				coinId, reportType).list();
	}

	@Transient
	public Map<String, Object> getCalculatedValues() {
		Map<String, Object> calculatedValues = new HashMap<>();
	
		for (EtMetadata each : getMetadatas()) {
			if (each.getBooleanValue() != null) {
				calculatedValues.put(each.getNotation(), each.getBooleanValue());
				continue;
			}
			if (each.getDoubleValue() != null) {
				calculatedValues.put(each.getNotation(), each.getDoubleValue());
				continue;
			}
			if (each.getIntValue()!= null) {
				calculatedValues.put(each.getNotation(), each.getIntValue());
				continue;
			}
			if (each.getStringValue() != null) {
				calculatedValues.put(each.getNotation(), each.getStringValue());
				continue;
			}
			calculatedValues.put(each.getNotation(), null);
		}
		return calculatedValues;
	}

}
