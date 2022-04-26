package com.aghs.mobilevaccination.data.model.vaccine;

public enum VaccinationStatus {
    NOT_VACCINATED(0),
    REGISTERED(1),
    VACCINATED(2),
    DISCARDED(3),
    PARTIALLY_VACCINATED(4),
    FULLY_VACCINATED(5),
    IN_CENTRE(6);

    VaccinationStatus(int i) {}


    @Override
    public String toString() {
        switch (this) {
            case NOT_VACCINATED -> { return  "Not Vaccinated"; }
            case REGISTERED -> { return "Registered"; }
            case VACCINATED -> { return "Vaccinated"; }
            case DISCARDED -> { return  "Discarded"; }
            case PARTIALLY_VACCINATED -> { return "Partially Vaccinated"; }
            case FULLY_VACCINATED -> { return "Fully Vaccinated"; }
            case IN_CENTRE -> { return  "In Centre"; }
        }
        return null;
    }
}
