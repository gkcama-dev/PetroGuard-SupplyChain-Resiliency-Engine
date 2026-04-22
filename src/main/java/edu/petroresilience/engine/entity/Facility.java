package edu.petroresilience.engine.entity;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node("Facility")
@Data
public class Facility {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String type;
    private Double capacity;
    private String location;

}
