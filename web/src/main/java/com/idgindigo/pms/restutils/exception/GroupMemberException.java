package com.idgindigo.pms.restutils.exception;

/**
 * @author valentyn_vakatsiienko
 * @since 1/31/14 4:57 PM
 */
public class GroupMemberException extends RestFriendlyException {
    public static final String GROUP_MEMBER = "groupMember.";
    public static final String INVALID_ROOM_USE = GROUP_MEMBER + "invalidRoomUse";
    public static final String INVALID_ROOM = GROUP_MEMBER + "invalidRoom";

    public GroupMemberException(String code, String source) {
        super(code, source);
    }
}
