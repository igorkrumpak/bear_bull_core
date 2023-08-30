package si.iitech.coingecko.domain.Coins;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import si.iitech.coingecko.domain.Coins.CoinData.CommunityData;
import si.iitech.coingecko.domain.Coins.CoinData.DeveloperData;
import si.iitech.coingecko.domain.Coins.CoinData.PublicInterestStats;
import si.iitech.coingecko.domain.Shared.Image;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CoinHistoryById {
    @JsonProperty("id")
    private String id;
    @JsonProperty("symbol")
    private String symbol;
    @JsonProperty("name")
    private String name;
    @JsonProperty("localization")
    private Map<String, String> localization;
    @JsonProperty("image")
    private Image image;
    @JsonProperty("market_data")
    private MarketData marketData;
    @JsonProperty("community_data")
    private CommunityData communityData;
    @JsonProperty("developer_data")
    private DeveloperData developerData;
    @JsonProperty("public_interest_stats")
    private PublicInterestStats publicInterestStats;

}
