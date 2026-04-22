package edu.petroresilience.engine.tools;

import edu.petroresilience.engine.repository.FacilityRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Configuration
public class AiTools {

    private final FacilityRepository repository;

    public AiTools(FacilityRepository repository) {
        this.repository = repository;
    }

    public record FacilityRequest(String query) {}

    @Bean
    @Description("Get real-time information about petroleum facilities, refineries, and storage capacities from the database.")
    public Function<FacilityRequest, String> getFacilityDetails() {
        return request -> {
            System.out.println("🛠️ AI Tool Invoked: Database Readed!");

            return repository.findAll().stream()
                    .map(f -> f.getName() + " is a " + f.getType() + " located in " + f.getLocation() + " with a capacity of " + f.getCapacity() + " barrels.")
                    .collect(Collectors.joining("\n"));
        };
    }

    public record SanctionRequest(String countryName) {}

    @Bean
    @Description("Check if a specific country is under international trade sanctions or embargos.")
    public Function<SanctionRequest, String> checkSanctions() {
        return request -> {
            String country = request.countryName().toLowerCase();
            System.out.println("🛡️ Sanctions Guard Invoked for: " + country);

            // Our simple sanctions list check
            if (country.contains("iran") || country.contains("russia") || country.contains("north korea")) {
                return "WARNING: " + request.countryName() + " is currently under heavy trade sanctions. All transactions are BLOCKED.";
            } else {
                return "SUCCESS: " + request.countryName() + " is cleared for trade. No active sanctions found.";
            }
        };
    }
}