package solobob.solobobmate.controller.reportDto;

import lombok.Data;
import solobob.solobobmate.domain.Report;
import solobob.solobobmate.domain.ReportType;

import java.time.LocalDateTime;

@Data
public class ReportListDto {

    private Long id;

    private ReportType reportType;

    private String description;

    private LocalDateTime reportedAt;

    public ReportListDto(Report report){
        this.id = report.getId();
        this.reportType = report.getReportType();
        this.description = report.getDescription();
        this.reportedAt = report.getCreateAt();
    }
}
