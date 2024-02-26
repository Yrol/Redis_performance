package blog.yrol.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table // Explicitly mark Product as entity as table since we've 2 data sources Postgres and Redis -  since this will run into a conflict
@ToString // The @ToString annotation generates an implementation for the toString() method where the class name, along with each field in order — separated by commas — is printed
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    private Integer id;
    private String description;;
    private double price;
}
