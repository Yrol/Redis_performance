package blog.yrol.entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;

@Data
@ToString // The @ToString annotation generates an implementation for the toString() method where the class name, along with each field in order — separated by commas — is printed
public class Product {

    @Id
    private Integer id;
    private String description;;
    private double price;
}
