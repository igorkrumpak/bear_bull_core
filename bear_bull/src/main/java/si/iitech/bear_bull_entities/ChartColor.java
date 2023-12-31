package si.iitech.bear_bull_entities;

public enum ChartColor {
    BLUE("blue"),
    BLACK("black"),
    GRAY("gray"),
    RED("red"),
    ORANGE("orange"), 
    SLATE_GREY("SlateGrey"),
    LIGHT_GREY("LightGrey")
    ;

    private String value;

    private ChartColor(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
  
}
