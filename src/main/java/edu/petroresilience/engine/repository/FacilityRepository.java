package edu.petroresilience.engine.repository;

import edu.petroresilience.engine.entity.Facility;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query; // මේක අනිවාර්යයි
import java.util.List;

public interface FacilityRepository extends Neo4jRepository<Facility,Long> {
    // Get supplier countries without sanctions that have open maritime routes
    @Query("MATCH (c:Country)-[r:USES_ROUTE]->(route:MaritimeChokepoint)-[:CONNECTS_TO]->(f:Facility) " +
            "WHERE c.sanctioned = false AND route.status = 'Open' " +
            "RETURN c.name + ' (Route: ' + route.name + ', Shipping: $' + toString(r.shipping_cost_per_barrel) + ')'")
    List<String> findSafeSuppliersViaOpenRoutes();
}
