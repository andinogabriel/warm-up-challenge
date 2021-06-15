package alkemy.warmupchallenge.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity(name = "categories")
@Getter @Setter @AllArgsConstructor
public class CategoryEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false, length = 100, unique = true)
    private String name;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "category")
    private List<PostEntity> posts;
}
