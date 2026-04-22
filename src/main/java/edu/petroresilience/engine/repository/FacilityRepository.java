package edu.petroresilience.engine.repository;

import edu.petroresilience.engine.entity.Facility;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface FacilityRepository extends Neo4jRepository<Facility,Long> {
}
