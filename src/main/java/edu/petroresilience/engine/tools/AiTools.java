package edu.petroresilience.engine.tools;

import edu.petroresilience.engine.repository.FacilityRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Configuration
public class AiTools {

    private final FacilityRepository repository;
    private final SimpMessagingTemplate messagingTemplate;

    public AiTools(FacilityRepository repository, SimpMessagingTemplate messagingTemplate) {
        this.repository = repository;
        this.messagingTemplate = messagingTemplate;
    }

    public record FacilityRequest(String query) {
    }

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

    public record SanctionRequest(String countryName) {
    }

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

    @Bean
    @Description("Find alternative oil suppliers that have an OPEN maritime route and are NOT under sanctions.")
    public Supplier<String> findAlternativeSuppliers() {
        return () -> {
            System.out.println("🔍 Checking Neo4j for safe suppliers and open sea routes...");
            List<String> safeCountries = repository.findSafeSuppliersViaOpenRoutes();

            System.out.println("📦 Data from Neo4j: " + safeCountries);

            if (safeCountries.isEmpty()) {

                // Emergency incident! Broadcast this live in real time!
                messagingTemplate.convertAndSend("/topic/alerts", "🔴 CRITICAL ALERT: MARITIME BLOCKADE DETECTED!");
                return "CRITICAL WARNING: No safe alternative suppliers found. Either all countries are sanctioned, or the maritime routes are CLOSED.";
            }

            // Status is normal! Routes are open and operational.
            messagingTemplate.convertAndSend("/topic/alerts", "🟢 SYSTEM NORMAL: Safe suppliers available.");
            return "Safe suppliers with open routes found: " + String.join(", ", safeCountries);
        };
    }
}