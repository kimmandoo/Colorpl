package com.colorpl.global.common.exception;

public class Messages {

    public static final String MEMBER_NOT_FOUND = "존재하지 않는 사용자입니다.";
    public static final String RESERVATION_NOT_FOUND = "예매 내역이 없습니다.";
    public static final String MEMBER_RESERVATION_MISMATCH = "예약이 없거나 멤버가 일치하지 않습니다.";
    public static final String SHOW_SCHEDULE_NOT_FOUND = "존재하지 않는 공연 일정 입니다.";
    public static final String RESERVATION_DETAIL_NOT_FOUND = "존재하지 않는 예매 상세 입니다.";
    public static final String REVIEW_NOT_FOUND = "존재하지 않는 리뷰 입니다.";
    public static final String EMAIL_NOT_FOUND = "존재하지 이메일 입니다.";
    public static final String EMAIL_ALREADY_EXISTS = "이미 사용 중인 이메일입니다.";
    public static final String MEMBER_REQUEST_NOT_MATCH = "이메일이나 패스워드가 일치하지 않습니다.";
    public static final String MEMBER_ALREADY_FOLLOW = " 이미 팔로우 중인 멤버입니다.";
    public static final String MEMBER_SELF_FOLLOW_ = " 자기 자신은 팔로우 불가합니다.";
    public static final String INVALID_GOOGLE_ID_TOKEN = "유효하지 않은 구글 id 토큰입니다.";
    public static final String CATEGORY_NOT_FOUND = "존재하지 않는 카테고리입니다. category=%s";
    public static final String SHOW_ALREADY_EXISTS = "이미 등록된 공연입니다. apiId=%s";
    public static final String EMAIL_NOT_EDIT = "이메일은 변경할 수 없습니다.";
    public static final String CATEGORY_LIMIT = "카테고리는 최대 2개까지만 허용됩니다.";
    public static final String COMMENT_NOT_FOUND = "존재하지 않는 댓글 입니다.";
    public static final String SHOW_DETAIL_NOT_FOUND = "존재하지 않는 공연상세입니다.";
    public static final String THEATER_ALREADY_EXISTS = "이미 등록된 공연시설입니다. apiId=%s";
    public static final String HALL_NOT_FOUND = "존재하지 않는 공연장입니다.";
    public static final String INVALID_RUNTIME = "유효하지 않은 런타임입니다. runtime=%s";
    public static final String RESERVATION_STATUS_ALREADY_ENABLED = "이미 예매되지 않은 좌석입니다. showScheduleId=%d, row=%d, col=%d";
    public static final String RESERVATION_STATUS_ALREADY_DISABLED = "이미 예매된 좌석입니다. showScheduleId=%d, row=%d, col=%d";
    public static final String INVALID_SEAT = "유효하지 않은 좌석입니다. row=%d, col=%d";
    public static final String SHOW_STATE_NOT_FOUND = "존재하지 않는 공연상태입니다. state=%s";
    public static final String INVALID_SEAT_CLASS = "유효하지 않은 좌석등급입니다. numberOfSeatClasses=%d";
    public static final String INVALID_DAY_OF_WEEK = "유효하지 않은 요일입니다. dayOfWeek=%s";
}
