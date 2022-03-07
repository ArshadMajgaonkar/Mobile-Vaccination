package com.aghs.mobilevaccination.data.converter;

import com.aghs.mobilevaccination.data.model.vaccine.VaccineDriveStatus;
import org.springframework.core.convert.converter.Converter;

public class StringToDriveStatusConvertor implements Converter<String, VaccineDriveStatus> {

    @Override
    public VaccineDriveStatus convert(String source) {
        if(source.equals(VaccineDriveStatus.INADEQUATE_DATA.toString()))
            return VaccineDriveStatus.INADEQUATE_DATA;
        else if(source.equals(VaccineDriveStatus.ON_GOING.toString()))
            return VaccineDriveStatus.ON_GOING;
        else if(source.equals(VaccineDriveStatus.UPCOMING.toString()))
            return VaccineDriveStatus.UPCOMING;
        else if(source.equals(VaccineDriveStatus.COMPLETED.toString()))
            return VaccineDriveStatus.COMPLETED;
        else if(source.equals(VaccineDriveStatus.CANCELLED.toString()))
            return VaccineDriveStatus.CANCELLED;
        else return null;
    }
}
