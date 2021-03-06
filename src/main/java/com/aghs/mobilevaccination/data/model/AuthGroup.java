package com.aghs.mobilevaccination.data.model;

/**
 * Enum Authorization Group for Access Control of Accounts
 */
public enum AuthGroup {
    USER, VACCINATOR, STAFF, ADMIN;

    @Override
    public String toString() {
        switch (this) {
            case USER  -> { return "USER";  }
            case VACCINATOR -> { return "VACCINATOR"; }
            case STAFF -> { return "STAFF"; }
            case ADMIN -> { return "ADMIN"; }
        }
        return null;
    }
}
