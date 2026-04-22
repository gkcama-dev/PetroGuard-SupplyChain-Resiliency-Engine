package edu.petroresilience.engine;

import edu.petroresilience.engine.entity.Facility;
import edu.petroresilience.engine.repository.FacilityRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.neo4j.core.Neo4jClient;

@SpringBootApplication
public class AiSupplyEngineApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiSupplyEngineApplication.class, args);
    }

    @Bean
    CommandLineRunner initDatabase(FacilityRepository facilityRepository, Neo4jClient neo4jClient) {
        return args -> {

            // 1. පරණ දත්ත සියල්ලම මුලින්ම මකා දැමීම (Clean Slate)
            neo4jClient.query("MATCH (n) DETACH DELETE n").run();

            // 2. පිරිපහදුව නිර්මාණය කිරීම
            Facility sapugaskanda = new Facility();
            sapugaskanda.setName("Sapugaskanda Refinery");
            sapugaskanda.setType("Refinery");
            sapugaskanda.setCapacity(50000.0);
            sapugaskanda.setLocation("Western Province, Sri Lanka");
            facilityRepository.save(sapugaskanda);

            // 3. රටවල්, මුහුදු මාර්ග සහ සම්බන්ධතා එකවර නිර්මාණය කිරීම (The Enterprise Way)
            String cypherSetup = """
                MATCH (f:Facility {name: 'Sapugaskanda Refinery'})
                
                // රටවල් සෑදීම (සම්බාධක තත්ත්වයත් සමඟ)
                MERGE (sa:Country {name: 'Saudi Arabia', sanctioned: false})
                MERGE (uae:Country {name: 'UAE', sanctioned: false})
                MERGE (russia:Country {name: 'Russia', sanctioned: true})
                
                // මුහුදු මාර්ගය සෑදීම
                MERGE (hormuz:MaritimeChokepoint {name: 'Strait of Hormuz', status: 'Open'})
                
                // සම්බන්ධතා ඇති කිරීම
                MERGE (sa)-[r1:USES_ROUTE]->(hormuz)
                MERGE (uae)-[r2:USES_ROUTE]->(hormuz)
                MERGE (hormuz)-[:CONNECTS_TO]->(f)
                
                // නාවික ගාස්තු (Shipping Costs) සකස් කිරීම
                SET r1.shipping_cost_per_barrel = 4.50
                SET r2.shipping_cost_per_barrel = 2.00
                """;

            neo4jClient.query(cypherSetup).run();

            System.out.println("✅ Enterprise Graph Database Initialized Successfully!");
        };
    }

}
