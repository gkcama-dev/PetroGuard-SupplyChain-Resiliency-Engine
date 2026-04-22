package edu.petroresilience.engine;

import edu.petroresilience.engine.entity.Facility;
import edu.petroresilience.engine.repository.FacilityRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AiSupplyEngineApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiSupplyEngineApplication.class, args);
    }

    @Bean
    CommandLineRunner initDatabase(FacilityRepository facilityRepository) {
        return args -> {

            facilityRepository.deleteAll();

            Facility sapugaskanda = new Facility();
            sapugaskanda.setName("Sapugaskanda Refinery");
            sapugaskanda.setType("Refinery");
            sapugaskanda.setCapacity(50000.0);
            sapugaskanda.setLocation("Western Province, Sri Lanka");

            // Neo4j Database -> Save
            facilityRepository.save(sapugaskanda);

            System.out.println("✅ Sapugaskanda Refinery Created!");
        };
    }

}
