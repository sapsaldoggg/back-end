package solobob.solobobmate.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum ReportType {
    AD("광고"),CHEAT("사기"), ESCAPE("탈주"), INSULT("욕설/부적절한 언어"), PORN("음란물"), SPAM("도배"), THREAT("비방/위협"), ETC("기타");

    private final String value;

    ReportType(String value){
        this.value = value;
    }

    @JsonCreator
    public static ReportType from(String value){
        for (ReportType type : ReportType.values()) {
            if (type.getValue().equals(value))
                return type;
        }
        return null;
    }

    @JsonValue
    public String getValue(){
        return value;
    }

}
