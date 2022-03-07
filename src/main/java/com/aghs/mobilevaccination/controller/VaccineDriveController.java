package com.aghs.mobilevaccination.controller;

import com.aghs.mobilevaccination.data.dto.CityDto;
import com.aghs.mobilevaccination.data.model.Vehicle;
import com.aghs.mobilevaccination.data.model.location.Centre;
import com.aghs.mobilevaccination.data.model.location.City;
import com.aghs.mobilevaccination.data.model.location.Spot;
import com.aghs.mobilevaccination.data.model.vaccine.MemberVaccination;
import com.aghs.mobilevaccination.data.model.vaccine.VaccineDrive;
import com.aghs.mobilevaccination.data.model.vaccine.VaccineDriveStatus;
import com.aghs.mobilevaccination.data.repository.StaffRepository;
import com.aghs.mobilevaccination.data.repository.VehicleRepository;
import com.aghs.mobilevaccination.data.repository.location.SpotRepository;
import com.aghs.mobilevaccination.data.repository.vaccine.MemberVaccinationRepository;
import com.aghs.mobilevaccination.data.repository.vaccine.VaccineDriveRepository;
import com.aghs.mobilevaccination.data.repository.vaccine.VaccineRepository;
import com.aghs.mobilevaccination.service.AlgorithmDeployService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.ConnectException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
public class VaccineDriveController extends  DefaultController{
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
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
    AlgorithmDeployService algorithmDeployService;

    private static final String GET_REGISTRATIONS_URL = "/get-registrations";
    private static final String DEPLOY_ALGORITHM = "/deploy-algorithm";
    private static final String SAVE_VACCINE_DRIVE = "/save-vaccine-drive";


    // Private Methods

    private long getUnlinkedRegistration(LocalDate driveDate, City city, Map<Long, Long> spotWiseRegistrations) {
        List<Spot> spots = spotRepository.findByCity(city);
        long totalRegistration = 0;
        for(Spot spot: spots) {
            List<MemberVaccination> memberVaccinations =
                    memberVaccinationRepository.findByVaccinationSpotAndSelectedDateAndVaccineDrive(
                            spot, driveDate, null);
            spotWiseRegistrations.put(spot.getId(), (long) memberVaccinations.size());
            totalRegistration += memberVaccinations.size();
        }
        return totalRegistration;
    }

    private List<Spot> getSpots(List<?> spotIds) {
        List<Spot> spots = new ArrayList<>();
        for(Object spotId: spotIds) {
            Long spotIdLong;
            if(spotId instanceof Integer)
                spotIdLong = Long.valueOf((Integer) spotId);
            else if(spotId instanceof Long)
                spotIdLong = (Long) spotId;
            else
                throw new IllegalArgumentException("spotId is not a Integer");
            spotRepository.findById(spotIdLong).ifPresent(spots::add);
        }
        return spots;
    }

    private List<Vehicle> getVehicleFromCity(City city) {
        List<Vehicle> vehiclesInCity = new ArrayList<>();
        List<Spot> spotsInCity = spotRepository.findByCity(city);
        for(Spot spot: spotsInCity) {
            List<Centre> centresInCity = centreRepository.findBySpot(spot);
            for (Centre centre : centresInCity) {
                vehiclesInCity.addAll(vehicleRepository.findByCentre(centre));
            }
        }
        return vehiclesInCity;
    }

    /**
     * Saves VaccineDrive as well as updates respective MemberVaccination
     * @param spotDistribution List of Spot distributed in list based on different drives
     * @param driveDate VaccineDrive conduction date
     * @param city The city in which VaccineDrive will be conducted.
     * @param dosesPerVehicle Vaccine Doses Count for vehicle.
     */
    private void saveVaccineDrives(List<List<?>> spotDistribution,
                                   LocalDate driveDate,
                                   City city,
                                   int dosesPerVehicle) {
        for(List<?> spotIdList: spotDistribution) {
            List<Spot> spots = getSpots(spotIdList);
            VaccineDrive vaccineDrive = new VaccineDrive();
            vaccineDrive.setDriveDate(driveDate);
            vaccineDrive.setStatus(VaccineDriveStatus.INADEQUATE_DATA);
            vaccineDrive.setCity(city);
            vaccineDrive.setVaccinationSpots(new HashSet<>(spots));
            vaccineDrive.setSlotCount((long)dosesPerVehicle);
            vaccineDrive.setAddedAt(new Date());
            vaccineDrive.setAddedBy(staffRepository.findByUsername("admin"));
            vaccineDrive.setVaccinator(staffRepository.findByUsername("admin"));
            //vaccineDrive.setAddedBy()
            //vaccineDrive.setVaccinator();
            vaccineDriveRepository.save(vaccineDrive);
            vaccineDrive.updateMemberVaccinations(memberVaccinationRepository);
        }
    }


    // Controllers

    @GetMapping(GET_REGISTRATIONS_URL)
    public String getRegistrations(Model model) {
        model.addAttribute("minDriveDate", LocalDate.now().plusDays(1));
        model.addAttribute("states", stateRepository.findAll());
        return "deploy-algorithm";
    }

    @PostMapping(GET_REGISTRATIONS_URL)
    public String postRegistrations(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate driveDate,
                                    @ModelAttribute("cityDto") CityDto cityDto,
                                    Model model) {
        List<String> messages = new ArrayList<>();
        model.addAttribute("messages", messages);
        model.addAttribute("driveDate", driveDate);
        model.addAttribute("minDriveDate", LocalDate.now().plusDays(1));
        City city = getCity(cityDto, model);
        if(city != null) {
            if(driveDate != null) {
                if(vaccineDriveRepository.findByDriveDateAndCity(driveDate, city).size() == 0) {
                    Map<Long, Long> spotWiseRegistrations = new HashMap<>();
                    long totalRegistration = getUnlinkedRegistration(driveDate, city, spotWiseRegistrations);
                    model.addAttribute("hasReport", true);
                    model.addAttribute("spotWiseRegistrations", spotWiseRegistrations);
                    model.addAttribute("totalRegistration", totalRegistration);
                    model.addAttribute("cityCapacity", city.getAllotedSlotsPerDay());
                    model.addAttribute("vehicleCount", getVehicleFromCity(city).size());
                    model.addAttribute("vaccines", vaccineRepository.findAll());
                }
                else
                    model.addAttribute("driveAddedForCityDate", true);
            }
            else messages.add("Please select Vaccination Drive Date");
        }
        return "deploy-algorithm";
    }

    @PostMapping(DEPLOY_ALGORITHM)
    public String postDeployAlgorithm(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate driveDate,
                                      @ModelAttribute("cityId") long cityId,
                                      @ModelAttribute("selectedVehicleCount") int selectedVehicleCount,
                                      @ModelAttribute("dosesPerVehicle") int dosesPerVehicle,
                                      Model model) throws ConnectException {
        model.addAttribute("vaccines", vaccineRepository);
        model.addAttribute("driveDate", driveDate);
        System.out.println(driveDate);
        System.out.println(cityId);
        System.out.println(selectedVehicleCount);
        System.out.println(dosesPerVehicle);
        if(driveDate != null && cityId > -1 && selectedVehicleCount > 0 && dosesPerVehicle > 0) {
            List<String> messages = new ArrayList<>();
            model.addAttribute("messages", messages);
            City city = cityRepository.findById(cityId).orElse(null);
            if( city != null) {
                HashMap<Long, Long> spotWiseRegistrations = new HashMap<>();
                // TODO: vaccination count which are yet to be added to drive
                getUnlinkedRegistration(driveDate, city, spotWiseRegistrations);
                try {
                    List<List<?>> spotDistribution =
                            algorithmDeployService.getSlotDistribution(dosesPerVehicle, spotWiseRegistrations);
                    City driveCity = cityRepository.findById(cityId).orElse(null);
                    saveVaccineDrives(spotDistribution, driveDate, driveCity, dosesPerVehicle);
                    model.addAttribute("spotDistribution", spotDistribution);
                    model.addAttribute("hasAddedDrives", true);
                    model.addAttribute("states", stateRepository.findAll());
                    System.out.println(spotDistribution);
                }catch (ConnectException ce) {
                    messages.add("Server Down! Please try again after some time.");
                }
            }
            else
                messages.add("Select correct city.");
        }
        return "deploy-algorithm";
    }

    @GetMapping("/list-vaccine-drive")
    public String getListVaccineDrive(Model model) {
        LocalDate today = LocalDate.now();
        model.addAttribute("maxDriveDate", LocalDate.now().plusDays(2));
        model.addAttribute("states", stateRepository.findAll());
        model.addAttribute("driveStatuses", VaccineDriveStatus.values());
        return "list-vaccine-drive";
    }


    @PostMapping("/list-vaccine-drive")
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
                vaccineDrives = vaccineDriveRepository.findByDriveDateAndStatusAndCity(
                        driveDate,
                        status,
                        driveCity
                );
            model.addAttribute("vaccineDrives", vaccineDrives);
        }
        model.addAttribute("formUrl", "/list-vaccine-drive");
        model.addAttribute("driveDate", driveDate);
        model.addAttribute("driveStatuses", VaccineDriveStatus.values());
        model.addAttribute("selectedDriveStatus", status);
        model.addAttribute("maxDriveDate", LocalDate.now().plusDays(2));
        return "list-vaccine-drive";
    }


    @PostMapping("/list-inadequate-vaccine-drive")
    public String listInadequateVaccineDrive(@ModelAttribute("driveDate") String driveDateString,
                                             @ModelAttribute("cityId") long cityId,
                                             Model model) {
        City driveCity = cityRepository.findById(cityId).orElse(null);
        List<VaccineDrive> vaccineDrives =
                vaccineDriveRepository.findByStatusAndCity(VaccineDriveStatus.INADEQUATE_DATA, driveCity);
        CityDto cityDto = driveCity.toDto();
        model.addAttribute("vaccineDrives", vaccineDrives);
        model.addAttribute("driveStatuses", VaccineDriveStatus.values());
        model.addAttribute("selectedDriveStatus", VaccineDriveStatus.INADEQUATE_DATA);
        model.addAttribute("cityDto", cityDto);
        model.addAttribute("states", stateRepository.findAll());
        model.addAttribute("districts", districtRepository.findByState(driveCity.getDistrict().getState()));
        model.addAttribute("cities", cityRepository.findByDistrict(driveCity.getDistrict()));
        model.addAttribute("driveDate", driveDateString);
        model.addAttribute("maxDriveDate", LocalDate.now().plusDays(2));
        return "list-vaccine-drive";
    }

    @PostMapping("/update-vaccine-drive")
    public String updateVaccineDrive(@ModelAttribute("vaccineDrive")VaccineDrive vaccineDrive) {
        if(vaccineDrive.isUpdateValid()){
            if(vaccineDrive.getStatus() == VaccineDriveStatus.INADEQUATE_DATA)
                vaccineDrive.setStatus(VaccineDriveStatus.UPCOMING);
            vaccineDriveRepository.save(vaccineDrive);
        }
        return "update-vaccine-drive";
    }

    @ModelAttribute("vaccineDrive")
    public VaccineDrive getVaccineDrive() {
        return new VaccineDrive();
    }
}