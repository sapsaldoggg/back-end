package solobob.solobobmate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solobob.solobobmate.controller.exception.ErrorCode;
import solobob.solobobmate.controller.exception.SoloBobException;
import solobob.solobobmate.controller.reportDto.ReportDto;
import solobob.solobobmate.controller.reportDto.ReportInfoDto;
import solobob.solobobmate.domain.Member;
import solobob.solobobmate.domain.Party;
import solobob.solobobmate.domain.Report;
import solobob.solobobmate.domain.ReportType;
import solobob.solobobmate.repository.ReportRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;

    @Transactional
    public void reportMember(Member fromMember, Member toMember, Party party, ReportDto reportDto){
        if (!party.getMembers().contains(toMember)){
            throw new SoloBobException(ErrorCode.REPORT_MEMBER);
        }
        if (fromMember.getId().equals(toMember.getId())){
            throw new SoloBobException(ErrorCode.REPORT_MYSELF);
        }

        // 신고한 이력이 있으면 신고 x (단, 탈주는 가능)
        if (reportRepository.existsByFromMemberAndToMember(fromMember, toMember) && !reportDto.getReportType().equals(ReportType.ESCAPE)){
            throw new SoloBobException(ErrorCode.REPORT_DUPLICATE);
        }

        Report report = Report.create(fromMember, toMember, reportDto.getReportType(), reportDto.getDescription());

        reportRepository.save(report);
    }

    public ReportInfoDto reportInfoReturn(Member member, Party party){
        if (!party.getMembers().contains(member)){
            throw new SoloBobException(ErrorCode.REPORT_MEMBER);
        }
        List<Report> reports = reportRepository.findAllByToMemberId(member.getId());

        return new ReportInfoDto(member, reports);
    }
}
