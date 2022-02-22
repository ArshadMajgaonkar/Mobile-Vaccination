package com.aghs.mobilevaccination.data.model.vaccine;

public enum VaccineDriveStatus {
    UPCOMING, ON_GOING, COMPLETED, CANCELLED;


    @Override
    public String toString() {
        switch (this) {
            case UPCOMING -> { return "Upcoming"; }
            case ON_GOING -> { return "On Going"; }
            case COMPLETED -> { return "Completed"; }
            case CANCELLED -> { return "Cancelled"; }
        }
        return null;
    }
}
