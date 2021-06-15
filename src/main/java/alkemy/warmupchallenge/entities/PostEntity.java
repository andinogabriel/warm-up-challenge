package alkemy.warmupchallenge.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity(name = "posts")
@EntityListeners(AuditingEntityListener.class)
@Getter @Setter @AllArgsConstructor
public class PostEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String body;

    private String image;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    @CreatedDate
    private Date creationDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

}
