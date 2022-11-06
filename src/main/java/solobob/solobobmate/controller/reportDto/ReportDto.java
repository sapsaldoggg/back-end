package solobob.solobobmate.controller.reportDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import solobob.solobobmate.domain.ReportType;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReportDto {

    private Long memberId;
    private ReportType reportType;
    private String description;

}
