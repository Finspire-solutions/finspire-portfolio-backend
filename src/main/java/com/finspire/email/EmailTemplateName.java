package com.finspire.email;

import lombok.Getter;

@Getter
public enum EmailTemplateName {
    ACTIVATE_ACCOUNT("activate_account"),
    BOOKING_CONFIRM("booking_confirm_email"),
    RESERVATION_CANCELLED_EMAIL("reservation_cancelled_email"),
    RESERVATION_MISSED_EMAIL("reservation_missed_email"),
    REJECT_EMAIL("reject_email"),
    ACCEPTED_EMAIL("accepted_email");

    private final String name;
    EmailTemplateName(String name) {
        this.name = name;
    }
}
