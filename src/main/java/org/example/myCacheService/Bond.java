package org.example.myCacheService;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;

@Validated
public class Bond {


    @JsonProperty
    @NonNull
    private String bondId;
    @JsonProperty("exchange")
    private String exchange;
    @JsonProperty("name")
    private String name;
    @JsonProperty("securityType")
    private String securityType;
    @JsonProperty("description")
    private String description;
    @JsonProperty("currency")
    private String currency;
    @JsonProperty("country")
    private String country;

    public Bond(String bondId,String exchange, String name, String securityType, String description, String currency,String country){
        this.bondId= bondId;
        this.exchange = exchange;
        this.name = name;
        this.securityType = securityType;
        this.description = description;
        this.currency = currency;
        this.country = country;
    }

    public String getBondId() {
        return bondId;
    }
}
