package solobob.solobobmate.controller.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {


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



    // restaurant
    NOT_FOUND_RESTAURANT(2300, "식당이 존재하지 않습니다");



    private final int code;
    private final String messages;

    private ErrorCode(int code, String messages) {
        this.code = code;
        this.messages=messages;
    }
}
