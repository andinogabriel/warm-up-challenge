package alkemy.warmupchallenge.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity(name = "users")
@Getter @Setter
public class UserEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false, length = 100, unique = true)
    private String email;

    @Column(nullable = false, length = 100)
    private String encryptedPassword;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<PostEntity> posts;

}
