package si.iitech.coingecko.domain.Exchanges;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import si.iitech.coingecko.domain.Shared.Ticker;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExchangesTickersById {
    @JsonProperty("name")
    private String name;
    @JsonProperty("tickers")
    private List<Ticker> tickers;

}
