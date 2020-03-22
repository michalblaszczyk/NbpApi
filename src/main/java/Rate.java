import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown =true)
public class Rate {
    private String effectiveDate;
    private Double mid;

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public Double getMid() {
        return mid;
    }

    public void setMid(Double mid) {
        this.mid = mid;
    }
}
