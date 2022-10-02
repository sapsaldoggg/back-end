package solobob.solobobmate.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Report extends BaseEntity{

    @Id @GeneratedValue
    @Column(name = "report_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private ReportType reportType;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_member_id")
    private Member fromMember;  // 신고 하는 사용자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_member_id")
    private Member toMember;    // 신고 당하는 사용자


    public static Report create(Member fromMember, Member toMember, ReportType reportType, String description){
        Report report = new Report();
        report.fromMember = fromMember;
        report.toMember = toMember;
        report.reportType = reportType;
        report.description = description;

        return report;
    }

}
