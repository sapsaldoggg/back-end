package solobob.solobobmate.controller.reportDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import solobob.solobobmate.domain.ReportType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportDto {

    private Long memberId;
    private ReportType reportType;
    private String description;

}
