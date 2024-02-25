package blog.yrol.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;

@Data
@ToString // The @ToString annotation generates an implementation for the toString() method where the class name, along with each field in order — separated by commas — is printed
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    private Integer id;
    private String description;;
    private double price;
}
