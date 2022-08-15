package solobob.solobobmate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PartyScheduler {

    private final PartyService partyService;

    //1시간 마다 검사
    @Scheduled(cron = "0 0 0/1 * * *")
    public void deletePartyScheduler() {
        partyService.removePartyScheduler();
    }
}
