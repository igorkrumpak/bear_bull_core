package si.iitech.coingecko.domain.Coins;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import si.iitech.coingecko.domain.Shared.Ticker;
import lombok.*;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CoinTickerById {
    @JsonProperty("name")
    private String name;
    @JsonProperty("tickers")
    private List<Ticker> tickers;

}
