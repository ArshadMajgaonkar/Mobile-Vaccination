package com.aghs.mobilevaccination.data.model.vaccine;

public enum VaccinationStatus {
    REGISTERED, VACCINATED, DISCARDED;

    @Override
    public String toString() {
        switch (this) {
            case REGISTERED -> { return "Registered"; }
            case VACCINATED -> { return "Vaccinated"; }
            case DISCARDED -> { return  "Discarded"; }
        }
        return null;
    }
}
