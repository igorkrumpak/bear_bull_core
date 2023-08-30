package si.iitech.coingecko.domain.Coins.CoinData;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SparklineIn7d {
    @JsonProperty("price")
    private List<Double> price;

}
