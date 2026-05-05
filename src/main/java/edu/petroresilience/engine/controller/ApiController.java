package edu.petroresilience.engine.controller;

import edu.petroresilience.engine.repository.FacilityRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "http://localhost:4200")
public class ApiController {

    private final FacilityRepository facilityRepository;

    public ApiController(FacilityRepository facilityRepository) {
        this.facilityRepository = facilityRepository;
    }

//    @GetMapping("/suppliers/safe")
//    public List<Map<String, String>> getSafeSuppliers(){
//        System.out.println("📡 Frontend requested safe suppliers data...");

    /// /        return facilityRepository.findSafeSuppliersViaOpenRoutes();
//        return List.of(
//                Map.of(
//                        "country", "Saudi Arabia",
//                        "route", "Strait of Hormuz",
//                        "cost", "$4.5M",
//                        "status", "Safe"
//                ),
//                Map.of(
//                        "country", "UAE",
//                        "route", "Strait of Hormuz",
//                        "cost", "$2.0M",
//                        "status", "Safe"
//                ),
//                Map.of(
//                        "country", "Oman",
//                        "route", "Arabian Sea",
//                        "cost", "$3.2M",
//                        "status", "Monitoring"
//                )
//        ).reversed();
//    }

    @GetMapping("/suppliers/safe")
    public List<Map<String, Object>> getSafeSuppliers() {
        System.out.println("📡 Frontend requested safe suppliers data from Neo4j...");
        
        return facilityRepository.getSuppliersForFrontend();
    }

    @GetMapping("/health")
    public Map<String, String> getSystemHealth() {
        return Map.of(
                "status", "ONLINE",
                "database", "Neo4j Connected",
                "message", "PetroGuard Enterprise API is running"
        );
    }
}
