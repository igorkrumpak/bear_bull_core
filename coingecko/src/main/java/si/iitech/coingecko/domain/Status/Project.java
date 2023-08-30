package si.iitech.coingecko.domain.Status;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import si.iitech.coingecko.domain.Shared.Image;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Project {
    @JsonProperty("type")
    private String type;
    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("symbol")
    private String symbol;
    @JsonProperty("image")
    private Image image;

}
