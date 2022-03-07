package com.aghs.mobilevaccination.data.model.vaccine;

public enum VaccinationStatus {
    BOOKED, VACCINATED, DISCARDED;

    @Override
    public String toString() {
        switch (this) {
            case BOOKED -> { return "Booked"; }
            case VACCINATED -> { return "Vaccinated"; }
            case DISCARDED -> { return  "Discarded"; }
        }
        return null;
    }
}
