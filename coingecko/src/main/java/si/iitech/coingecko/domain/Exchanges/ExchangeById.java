package si.iitech.coingecko.domain.Exchanges;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import si.iitech.coingecko.domain.Shared.Ticker;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper = false)
public class ExchangeById extends Exchanges{
    @JsonProperty("tickers")
    private List<Ticker> tickers;
    @JsonProperty("status_updates")
    private List<Object> statusUpdates;

}
