package com.aghs.mobilevaccination.data.model.vaccine;

public enum VaccinationStatus {
    UNVACCINATED(0), REGISTERED(1), VACCINATED(2), DISCARDED(3);

    VaccinationStatus(int i) {}


    @Override
    public String toString() {
        switch (this) {
            case UNVACCINATED -> { return  "Unvaccinated"; }
            case REGISTERED -> { return "Registered"; }
            case VACCINATED -> { return "Vaccinated"; }
            case DISCARDED -> { return  "Discarded"; }
        }
        return null;
    }
}
