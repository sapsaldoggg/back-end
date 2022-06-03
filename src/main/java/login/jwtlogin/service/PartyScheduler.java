package login.jwtlogin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PartyScheduler {

    private final PartyService partyService;

    @Scheduled(cron = "* * * * * *")
    public void deletePartyScheduler() {
        partyService.removePartyScheduler();
    }
}
