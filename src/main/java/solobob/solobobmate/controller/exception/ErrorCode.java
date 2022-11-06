package solobob.solobobmate.controller.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // JWT
    TOKEN_EXPIRED(1000, "토큰이 만료되었습니다"),

    //member
    NOT_FOUND_MEMBER(2100, "존재하지 않는 회원입니다"),



    //party
    NOT_FOUND_PARTY(2200, "파티가 존재하지 않습니다"),

    PARTY_CREATE(2201, "파티에 소속되어 있습니다"),

    PARTY_JOIN_FULL(2202, "인원이 가득 찼습니다"),
    PARTY_JOIN_JOINED(2203, "이미 파티에 소속되어 있습니다"),
    PARTY_JOIN_MATCHED(2204, "이미 파티가 매칭되었습니다"),

    PARTY_SOR_READY(2205, "모두 준비상태이어야 합니다"),
    PARTY_SOR_NUM(2206, "파티원이 2명 미만입니다"),
    PARTY_SOR_MATCH(2207, "이미 파티가 매칭되었습니다"),

    PARTY_EXIT_OWNER(2208, "방장은 나갈 수 없습니다"),
    PARTY_EXIT_MATCH(2209, "이미 파티가 매칭되었습니다"),
    PARTY_EXIT_READY(2210, "준비상태에서 나갈 수 없습니다"),

    PARTY_DELETE_OWNER(2211, "방장권한입니다"),
    PARTY_MY_PARTY(2212, "파티에 속해있지 않습니다"),

    PARTY_UPDATE(2213, "최대 인원수보다 적습니다"),



    // restaurant
    NOT_FOUND_RESTAURANT(2300, "식당이 존재하지 않습니다"),

    NOT_FOUND_CHATROOM(2400, "채팅방이 존재하지 않습니다"),



    // report
    REPORT_MYSELF(2500, "본인은 신고할 수 없습니다"),
    REPORT_MEMBER(2501, "대상이 파티에 속해있지 않습니다"),
    REPORT_DUPLICATE(2502, "같은 사용자에 대해 중복 신고할 수 없습니다");


    private final int code;
    private final String messages;

    private ErrorCode(int code, String messages) {
        this.code = code;
        this.messages=messages;
    }
}
