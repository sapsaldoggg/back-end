package solobob.solobobmate.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PartyTest {

    /**
     * 준비 or 시작 or 기타 => 통합 테스트
     */

    public Party basicCreate() {
        Member member = Member.builder().nickname("owner").build();
        Restaurant restaurant = Restaurant.builder().name("한신식당").build();
        Party party = Party.create(member, restaurant, "111", 4);
        return party;
    }

    @DisplayName("파티 수정")
    @Test
    public void 파티수정() {
        //given
        Party party = basicCreate();
        //when
        Party updateParty = party.update("222", 3);
        //then
        assertThat(party).usingRecursiveComparison().isEqualTo(updateParty);
    }

    @DisplayName("파티 멤버 추가")
    @Test
    public void 멤버추가() {
        //given
        Party party = basicCreate();
        Member member1 = Member.builder().nickname("adduser1").build();
        Member member2 = Member.builder().nickname("adduser2").build();
        Member member3 = Member.builder().nickname("adduser3").build();
        //when
        party.addMember(member1);
        party.addMember(member2);
        party.addMember(member3);
        //then
        assertThat(party.getMembers()).contains(member1,member2,member3);
        assertThat(member1.getParty()).isSameAs(party);
        assertThat(member1.getIsJoined()).isEqualTo(true);
        assertThat(party.getCurrentNumber()).isEqualTo(4);
        assertThat(party.getFullStatus()).isEqualTo(FullStatus.FULL);
    }



    /**
     * 방장 여부 => 통합 test
     */
    @DisplayName("멤버 삭제")
    @Test
    public void 멤버삭제() {
        //given
        Party party = basicCreate();
        Member member = Member.builder().nickname("adduser1").build();
        //when
        party.addMember(member);
        party.deleteMember(member);
        //then
        assertThat(party.getMembers()).doesNotContain(member);
        assertThat(member.getIsJoined()).isEqualTo(false);
        assertThat(member.getParty()).isEqualTo(null);
    }


}
