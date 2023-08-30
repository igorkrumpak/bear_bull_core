package si.iitech.coingecko.domain.Search;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Trending {
    @JsonProperty("coins")
    private List<TrendingCoin> coins = null;
    @JsonProperty("exchanges")
    private List<Object> exchanges = null;

}
