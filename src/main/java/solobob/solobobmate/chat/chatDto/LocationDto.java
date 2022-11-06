package solobob.solobobmate.chat.chatDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LocationDto {
    private Long roomId;
    private String nickname;
    private String latitude;
    private String longitude;
}
