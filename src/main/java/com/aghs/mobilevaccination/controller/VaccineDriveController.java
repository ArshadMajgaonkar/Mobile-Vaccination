package com.aghs.mobilevaccination.controller;

import com.aghs.mobilevaccination.data.dto.CityDto;
import com.aghs.mobilevaccination.data.dto.DriveDto;
import com.aghs.mobilevaccination.data.model.AuthGroup;
import com.aghs.mobilevaccination.data.model.Staff;
import com.aghs.mobilevaccination.data.model.Vehicle;
import com.aghs.mobilevaccination.data.model.location.Centre;
import com.aghs.mobilevaccination.data.model.location.City;
import com.aghs.mobilevaccination.data.model.location.Spot;
import com.aghs.mobilevaccination.data.model.vaccine.VaccineDrive;
import com.aghs.mobilevaccination.data.model.vaccine.VaccineDriveStatus;
import com.aghs.mobilevaccination.data.repository.StaffRepository;
import com.aghs.mobilevaccination.data.repository.VehicleRepository;
import com.aghs.mobilevaccination.data.repository.location.SpotRepository;
import com.aghs.mobilevaccination.data.repository.vaccine.MemberVaccinationRepository;
import com.aghs.mobilevaccination.data.repository.vaccine.VaccineDriveRepository;
import com.aghs.mobilevaccination.data.repository.vaccine.VaccineRepository;
import com.aghs.mobilevaccination.service.AlgorithmDeployService;
import com.aghs.mobilevaccination.service.StaffUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;

import java.net.ConnectException;
import java.time.LocalDate;
import java.util.*;

@Controller
public class VaccineDriveController extends  DefaultController{
    @Autowired
    MemberVaccinationRepository memberVaccinationRepository;
    @Autowired
    SpotRepository spotRepository;
    @Autowired
    StaffRepository staffRepository;
    @Autowired
    VaccineRepository vaccineRepository;
    @Autowired
    VaccineDriveRepository vaccineDriveRepository;
    @Autowired
    VehicleRepository vehicleRepository;
    @Autowired
    StaffUserDetailService staffService;

    @Autowired
    AlgorithmDeployService algorithmDeployService;

    private static final String GET_REGISTRATIONS_URL = "/staff/vaccine-drive/get-registrations";
    private static final String DEPLOY_ALGORITHM = "/staff/vaccine-drive/deploy-algorithm";
    private static final String SAVE_VACCINE_DRIVE = "/staff/vaccine-drive/save";
    @Value("${drive.add.day.ahead}") private int ADD_DRIVE_DAYS_AHEAD;
    @Value("${drive.deploy.day.ahead}") private int DEPLOY_DRIVE_DAYS_AHEAD;
    @Value("${drive.complete.day.ahead}") private int COMPLETE_DRIVE_DAYS_AHEAD;


    // CONTROLLERS

    // Public

    @GetMapping("/vaccine-drive")
    public String getPublicDrive(Model model) {
        model.addAttribute("states", stateRepository.findAll());
        return "list-drive-public";
    }

    @PostMapping("/vaccine-drive")
    public String postPublicDrive(@ModelAttribute("cityDto") CityDto cityDto, Model model) {
        City city = getCity(cityDto, model);
        if(city!=null) {
            List<VaccineDriveStatus> status = Arrays.asList(VaccineDriveStatus.UPCOMING, VaccineDriveStatus.ON_GOING);
            List<VaccineDrive> drives =
                    vaccineDriveRepository.findByCityAndStatusIn(city, status);
            model.addAttribute("vaccineDrives", drives);
        }
        return "list-drive-public";
    }


    // Staff

    @GetMapping("/staff/vaccine-drive/add")
    public String getAddDrive(Model model) {
        model.addAttribute("vaccines", vaccineRepository.findAll());
        model.addAttribute("states", stateRepository.findAll());
        model.addAttribute("slotCount", 0);
        model.addAttribute("minDriveDate", LocalDate.now().plusDays(ADD_DRIVE_DAYS_AHEAD+1));
        return "add-drive";
    }

    @PostMapping("/staff/vaccine-drive/add")
    public String postAddDrive(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate driveDate,
                               @ModelAttribute("cityDto") CityDto cityDto,
                               @ModelAttribute("vaccineName") String vaccineName,
                               @ModelAttribute("slotCount") Long slotCount,
                               Model model) {
        List<String> messages = new ArrayList<>();
        model.addAttribute("messages", messages);
        City selectedCity = getCity(cityDto, model);
        model.addAttribute("driveDate", driveDate);
        boolean isDuplicate = VaccineDrive.isDuplicate(driveDate,
                cityDto.getCityId(), vaccineName, cityRepository, vaccineRepository, vaccineDriveRepository);
        if(driveDate==null)
            messages.add("Please select a Drive Date.");
        else if(vaccineName==null || vaccineName.equals(""))
            messages.add("Please select a Vaccine.");
        else if(slotCount<1)
            messages.add("Please enter Slot Count.");
        else if(isDuplicate)
            messages.add("Vaccine Drive with same date, city and vaccine already exists. Update existing drive.");
        else if(selectedCity!=null) {
            VaccineDrive drive = new VaccineDrive();
            drive.setAddedAt(new Date());
            drive.setAddedBy(staffService.getCurrentStaff());
            drive.setCity(selectedCity);
            drive.setDriveDate(driveDate);
            drive.setSlotCount(slotCount);
            drive.setStatus(VaccineDriveStatus.BOOKING);
            drive.setVaccine(vaccineRepository.findById(vaccineName).orElse(null));
            vaccineDriveRepository.save(drive);
            messages.add("New Vaccine Drive added");
            // removing values from html form
            model.addAttribute("driveDate", null);
            model.addAttribute("vaccineName", null);
            model.addAttribute("slotCount", 0);
            model.addAttribute("cityDto", new CityDto());
        }
        else
            messages.add("Please select a City.");
        model.addAttribute("vaccines", vaccineRepository.findAll());
        model.addAttribute("minDriveDate", LocalDate.now().plusDays(ADD_DRIVE_DAYS_AHEAD+1));
        return "add-drive";
    }

    // TODO - No Need of this method
    @GetMapping(GET_REGISTRATIONS_URL)
    public String getRegistrations(Model model) {
        model.addAttribute("minDriveDate", LocalDate.now().plusDays(1));
        model.addAttribute("states", stateRepository.findAll());
        return "deploy-algorithm";
    }

    @PostMapping(GET_REGISTRATIONS_URL)
    public String postRegistrations(@ModelAttribute("driveId") Long driveId,
                                    Model model) {
        List<String> messages = new ArrayList<>();
        model.addAttribute("messages", messages);
        model.addAttribute("minDriveDate", LocalDate.now().plusDays(1));
        VaccineDrive drive = vaccineDriveRepository.findById(driveId).orElse(null);
        if(drive != null) {
            Map<String, Long> spotWiseRegistrations = new HashMap<>();
            long registrationCount = drive.getRegistration(spotWiseRegistrations, spotRepository, vaccinationRepository);
            long vehicleCount = drive.getCity().getVehicle(spotRepository, centreRepository, vehicleRepository).size();
            model.addAttribute("hasReport", true);
            model.addAttribute("drive",drive);
            model.addAttribute("spotWiseRegistrations", spotWiseRegistrations);
            model.addAttribute("registrationCount", registrationCount);
            model.addAttribute("slotCount", drive.getSlotCount());
            model.addAttribute("vehicleCount", vehicleCount);
            model.addAttribute("vaccines", vaccineRepository.findAll());
        } else
            messages.add("No such Vaccination Drive exists.");
        return "deploy-algorithm";
    }

    @PostMapping(DEPLOY_ALGORITHM)
    public String postDeployAlgorithm(@ModelAttribute("driveId") long driveId,
                                      @ModelAttribute("selectedVehicleCount") int selectedVehicleCount,
                                      @ModelAttribute("dosesPerVehicle") int dosesPerVehicle,
                                      Model model) {
        System.out.println(driveId);
        System.out.println(selectedVehicleCount);
        System.out.println(dosesPerVehicle);
        VaccineDrive drive = vaccineDriveRepository.findById(driveId).orElse(null);
        if(drive != null && selectedVehicleCount > 0 && dosesPerVehicle > 0) {
            List<String> messages = new ArrayList<>();
            model.addAttribute("messages", messages);
            HashMap<Long, Long> spotWiseRegistrations = new HashMap<>();
            drive.getRegistrationCountWithSpotId(spotWiseRegistrations, spotRepository, vaccinationRepository);
            try {
                List<List<?>> spotDistribution =
                        algorithmDeployService.getSlotDistribution(dosesPerVehicle, spotWiseRegistrations);
                drive.saveVaccineDrives(
                        spotDistribution,
                        dosesPerVehicle,
                        spotWiseRegistrations.keySet(),
                        vaccinationRepository,
                        spotRepository,
                        staffRepository,
                        vaccineDriveRepository
                );
                model.addAttribute("spotDistribution", spotDistribution);
                model.addAttribute("hasAddedDrives", true);
                model.addAttribute("states", stateRepository.findAll());
                System.out.println(spotDistribution);
            }catch (RestClientException e) {
                messages.add("Server Down! Please try again after some time.");
            }
            model.addAttribute("drive", drive);
        }
        return "deploy-algorithm";
    }

    @GetMapping({"/staff/vaccine-drive/", "/staff/vaccine-drive" })
    public String getListVaccineDrive(Model model) {
        LocalDate today = LocalDate.now();
        model.addAttribute("driveDate", today);
        model.addAttribute("states", stateRepository.findAll());
        model.addAttribute("driveStatuses", VaccineDriveStatus.values());
        return "list-vaccine-drive";
    }


    @PostMapping({"/staff/vaccine-drive", "/staff/vaccine-drive/"})
    public String postListVaccineDrive(
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate driveDate,
            @ModelAttribute("cityDto") CityDto cityDto,
            @RequestParam(value = "selectedDriveStatus", required = false) VaccineDriveStatus status,
            Model model
    ) {
        List<String> messages = new ArrayList<>();
        City driveCity = getCity(cityDto, model);
        if(driveDate == null)
            messages.add("Please select a date.");
        else if(driveCity == null)
            messages.add("Please select a city.");
        else {
            List<VaccineDrive> vaccineDrives;
            if(status == null)
                vaccineDrives = vaccineDriveRepository.findByDriveDateAndCity(driveDate, driveCity);
            else
                vaccineDrives = vaccineDriveRepository.findByDriveDateAndCityAndStatus(
                        driveDate,
                        driveCity,
                        status
                );
            model.addAttribute("vaccineDrives", vaccineDrives);
        }
        model.addAttribute("formUrl", "/list-vaccine-drive");
        model.addAttribute("today", LocalDate.now());
        model.addAttribute("driveDate", driveDate);
        model.addAttribute("driveStatuses", VaccineDriveStatus.values());
        model.addAttribute("selectedDriveStatus", status);
        model.addAttribute("minDeployDate", LocalDate.now());
        model.addAttribute("maxDeployDate", LocalDate.now().plusDays(DEPLOY_DRIVE_DAYS_AHEAD+2));
        System.out.println(LocalDate.now().plusDays(DEPLOY_DRIVE_DAYS_AHEAD+2));
        return "list-vaccine-drive";
    }


    @PostMapping("/staff/vaccine-drive/list-inadequate")
    public String listInadequateVaccineDrive(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate driveDate,
                                             @ModelAttribute("cityId") long cityId,
                                             Model model) {
        City city = cityRepository.findById(cityId).orElse(null);
        if(city!=null) {
            CityDto dto = city.toDto();
            return postListVaccineDrive(driveDate, dto, VaccineDriveStatus.INADEQUATE_DATA, model);
        }
        return "error";
    }

    @PostMapping("/staff/vaccine-drive/set-on-going")
    public String setToOnGoing(@ModelAttribute("driveId") Long driveId, Model model) {
        VaccineDrive drive = vaccineDriveRepository.findById(driveId).orElse(null);
        if(drive!=null) {
            drive.setStatus(VaccineDriveStatus.ON_GOING);
            vaccineDriveRepository.save(drive);
            CityDto cityDto = drive.getCity().toDto();
            model.addAttribute("cityDto", cityDto);
            return postListVaccineDrive(drive.getDriveDate(), cityDto, null, model);
        }
        return "error";
    }

    @PostMapping("/staff/vaccine-drive/set-completed")
    public String setToCompleted(@ModelAttribute("driveId") Long driveId, Model model) {
        VaccineDrive drive = vaccineDriveRepository.findById(driveId).orElse(null);
        if(drive!=null) {
            drive.setStatus(VaccineDriveStatus.COMPLETED);
            vaccineDriveRepository.save(drive);
            CityDto cityDto = drive.getCity().toDto();
            model.addAttribute("cityDto", cityDto);
            return postListVaccineDrive(drive.getDriveDate(), cityDto, null, model);
        }
        return "error";
    }

    @PostMapping("/staff/vaccine-drive/update")
    public String updateVaccineDrive(@ModelAttribute("driveDto")DriveDto driveDto, Model model) {
        List<String> messages = new ArrayList<>();
        model.addAttribute("messages", messages);
        System.out.println(driveDto);
        VaccineDrive vaccineDrive = vaccineDriveRepository.findById(driveDto.getDriveId()).orElse(null);
        if(vaccineDrive != null) {
            if(driveDto.isValid()){
                vaccineDrive.updateFromDto(driveDto, staffRepository, vehicleRepository);
                if (vaccineDrive.getStatus() == VaccineDriveStatus.INADEQUATE_DATA)
                    vaccineDrive.setStatus(VaccineDriveStatus.UPCOMING);
                vaccineDriveRepository.save(vaccineDrive);
                messages.add("Vaccine Drive Updated Successfully");
            }
            else messages.add("Select valid Vehicle and Vaccinator.");
            List<Staff> vaccinators = vaccineDrive.getCity().getVaccinators(centreRepository, spotRepository, staffRepository);
            List<Vehicle> vehicles = vaccineDrive.getCity().getVehicle(spotRepository, centreRepository, vehicleRepository);
            vaccineDrive.removeAllocatedVehicleAndStaff(vaccinators, vehicles, vaccineDriveRepository);
            model.addAttribute("vaccineDrive", vaccineDrive);
            model.addAttribute("vehicles", vehicles);
            model.addAttribute("vaccinators", vaccinators);
        }
        else messages.add("No such Vaccine Drive exist.");
        return "update-vaccine-drive";
    }
}
