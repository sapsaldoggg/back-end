package solobob.solobobmate.controller.reportDto;

import lombok.Data;
import solobob.solobobmate.domain.Member;
import solobob.solobobmate.domain.Report;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class ReportInfoDto {

    private Long memberId;

    private List<ReportListDto> reportList;

    public ReportInfoDto(Member toMember, List<Report> reports){
        this.memberId = toMember.getId();
        this.reportList = reports.stream().map(m -> new ReportListDto(m)).collect(Collectors.toList());
    }
}
