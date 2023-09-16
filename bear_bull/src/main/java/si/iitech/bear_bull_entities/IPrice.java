package si.iitech.bear_bull_entities;

import java.util.Date;

public interface IPrice {
	
	public Date getPriceDate();
	
	public Double getPrice();

	public Double getMarketCapValue();

	public Double getTotalVolumeValue();
}
