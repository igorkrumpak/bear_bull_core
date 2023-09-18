package si.iitech.bear_bull_entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "COIN")
public class EtCoin extends PanacheEntity implements ICoin {

	private String name;
	private String symbol;
	private Date createdAt;
	private Date lastUpdatedAt;
	private String smallImage;
	private String largeImage;
	private String thumbImage;

	@Column(unique = true)
	private String coinId;

	@OneToMany(mappedBy = "coin", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<EtPrice> prices = new ArrayList<EtPrice>();

	@OneToMany(mappedBy = "coin", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<EtReport> reports = new ArrayList<EtReport>();

	public EtCoin() {
	}

	public EtCoin(String coinId, String symbol, String name) {
		this.coinId = coinId;
		this.symbol = symbol;
		this.name = name;
	}

	@Override
	public String getCoinId() {
		return coinId;
	}

	@Override
	public String getName() {
		return name;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	@Override
	public String getSymbol() {
		return symbol;
	}

	public Date getLastUpdatedAt() {
		return lastUpdatedAt;
	}

	public String getSmallImage() {
		return smallImage;
	}

	public void setSmallImage(String smallImage) {
		this.smallImage = smallImage;
	}

	public String getLargeImage() {
		return largeImage;
	}

	public void setLargeImage(String largeImage) {
		this.largeImage = largeImage;
	}

	public void setThumbImage(String thumbImage) {
		this.thumbImage = thumbImage;
	}

	public String getThumbImage() {
		return thumbImage;
	}

	public static EtCoin findByCoinId(String coinId) {
		return EtCoin.find("coinId = ?1", coinId).firstResult();
	}

	public static List<EtCoin> listCoinsWithoutPrices() {
		return EtCoin.list("select coin from EtCoin coin where coin.prices is empty");
	}

	public static List<EtCoin> listCoinsWithPrices() {
		return EtCoin.list("select coin from EtCoin coin where coin.prices is not empty");
	}

	public static List<EtCoin> listCoinsWithReports() {
		return EtCoin.list("select coin from EtCoin coin where coin.reports is not empty");
	}

	public static List<EtCoin> listAllCoins() {
		return EtCoin.list("select coin from EtCoin coin order by coin.lastUpdatedAt asc");
	}

	public static List<EtCoin> listAllCoinsDesc() {
		return EtCoin.list("select coin from EtCoin coin order by coin.lastUpdatedAt desc");
	}

	@PrePersist
	protected void onCreate() {
		createdAt = new Date();
	}

	@PreUpdate
	protected void onUpdate() {
		lastUpdatedAt = new Date();
	}
}
