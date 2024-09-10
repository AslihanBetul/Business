package emiresen.analyticsservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    private String nickname;
    private String message;
    private Date timeStamp;
}
