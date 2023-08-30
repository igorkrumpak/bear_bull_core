package si.iitech.bear_bull_entities;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.TypedQuery;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
@Table(name = "PRICE")
public class EtPrice extends PanacheEntity implements IPrice {

	@ManyToOne
	private EtCoin coin;

	@ManyToOne
	private EtPrice superPrice;

	private Double price;
	private Date priceDate;
	private Double marketCapValue;
	private Double totalVolumeValue;
	private Date createdAt;

	@Enumerated(EnumType.STRING)
	private PriceType type;

	public EtPrice() {
	}

	public EtPrice(EtCoin coin, Double price, Date priceDate, Double marketCapValue, Double totalVolumeValue,
			PriceType type) {
		this.coin = coin;
		this.price = price;
		this.priceDate = priceDate;
		this.marketCapValue = marketCapValue;
		this.totalVolumeValue = totalVolumeValue;
		this.type = type;
	}

	@Override
	public Date getPriceDate() {
		return priceDate;
	}

	public EtCoin getCoin() {
		return coin;
	}

	@Override
	public Double getPrice() {
		return price;
	}

	@Override
	public Double getMarketCapValue() {
		return marketCapValue;
	}

	@Override
	public Double getTotalVolumeValue() {
		return totalVolumeValue;
	}

	public PriceType getType() {
		return type;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public static EtPrice getLatestPrice(Long coinId, Date untilDate) {
		return EtPrice
				.find("select price from EtPrice price where coin.id = ?1 and price.priceDate < ?2 order by priceDate DESC",
						coinId, untilDate)
				.firstResult();
	}

	public static EtPrice getLatestDailyPrice(Long coinId) {
		return EtPrice
				.find("select price from EtPrice price where coin.id = ?1 and type = 'DAILY' order by priceDate DESC",
						coinId)
				.firstResult();
	}

	public static List<EtPrice> getPrices(Long coinId, Date untilDate, PriceType priceType) {
		TypedQuery<EtPrice> query = getEntityManager().createQuery(
				"select price from EtPrice price where price.coin.id = ?1 and price.priceDate <= ?2 and price.type = ?3 order by price.priceDate DESC",
				EtPrice.class);
		query.setParameter(1, coinId);
		query.setParameter(2, untilDate);
		query.setParameter(3, priceType);
		return query.getResultList();
	}

	public static List<EtPrice> getPrices(Long coinId, Date untilDate) {
		TypedQuery<EtPrice> query = getEntityManager().createQuery(
				"select price from EtPrice price where price.coin.id = ?1 and price.priceDate <= ?2 order by price.priceDate DESC",
				EtPrice.class);
		query.setParameter(1, coinId);
		query.setParameter(2, untilDate);
		return query.getResultList();
	}

	public static List<EtPrice> getPrices(Long coinId, Date fromDate, Date untilDate) {
		TypedQuery<EtPrice> query = getEntityManager().createQuery(
				"select price from EtPrice price where price.coin.id = ?1 and price.priceDate >= ?2 and price.priceDate <= ?3 order by price.priceDate DESC",
				EtPrice.class);
		query.setParameter(1, coinId);
		query.setParameter(2, fromDate);
		query.setParameter(3, untilDate);
		return query.getResultList();
	}

	public static List<Date> getPriceDates(Long coinId, Date untilDate, PriceType priceType) {
		TypedQuery<Date> query = getEntityManager().createQuery(
				"select price.priceDate from EtPrice price where price.coin.id = ?1 and price.priceDate <= ?2 and price.type = ?3 order by price.priceDate DESC",
				Date.class);
		query.setParameter(1, coinId);
		query.setParameter(2, untilDate);
		query.setParameter(3, priceType);
		return query.getResultList();
	}

	public static EtPrice getFirstDailyPrice(Long coinId) {
		return EtPrice
				.find("select price from EtPrice price where coin.id = ?1 and type = 'DAILY' order by priceDate ASC",
						coinId)
				.firstResult();
	}

	public static EtPrice getDailyPrice(Long coinId, Date date) {
		return EtPrice.find("select price from EtPrice price where coin.id = ?1 and type = 'DAILY' and priceDate = ?2",
				coinId, date).firstResult();
	}

	public static List<EtPrice> getPricesFromDate(Long coinId, Date fromDate) {
		return EtPrice.find("select price from EtPrice price where coin.id = ?1 and priceDate >= ?2 order by priceDate ASC",
				coinId, fromDate).list();
	}

	public static EtPrice getPrice(Long coinId, Date date, PriceType priceType) {
		return EtPrice.find("select price from EtPrice price where coin.id = ?1 and type = ?3 and priceDate = ?2",
				coinId, date, priceType).firstResult();
	}

	public static List<EtPrice> getPrices(Long coinId, Date fromDate, Date untilDate, PriceType priceType) {
		return EtPrice.list(
				"select price from EtPrice price where coin.id = ?1 and price.priceDate between ?2 and ?3 and price.type = ?4 order by price.priceDate DESC",
				coinId, fromDate, untilDate, priceType);
	}

	@PrePersist
	protected void onCreate() {
		createdAt = new Date();
	}
}
