package edu.petroresilience.engine.repository;

import edu.petroresilience.engine.entity.Facility;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import java.util.List;
import java.util.Map;

public interface FacilityRepository extends Neo4jRepository<Facility,Long> {
    // AI Tools
    @Query("MATCH (c:Country)-[r:USES_ROUTE]->(route:MaritimeChokepoint)-[:CONNECTS_TO]->(f:Facility) " +
            "WHERE c.sanctioned = false AND route.status = 'Open' " +
            "RETURN c.name + ' (Route: ' + route.name + ', Shipping: $' + toString(r.shipping_cost_per_barrel) + ')'")
    List<String> findSafeSuppliersViaOpenRoutes();

    // Frontend UI Table
    @Query("MATCH (c:Country)-[r:USES_ROUTE]->(route:MaritimeChokepoint) " +
            "WHERE c.sanctioned = false AND route.status = 'Open' " +
            "RETURN c.name AS country, route.name AS route, '$' + toString(r.shipping_cost_per_barrel) + 'M' AS cost, 'Safe' AS status")
    List<Map<String, Object>> getSuppliersForFrontend();
}
