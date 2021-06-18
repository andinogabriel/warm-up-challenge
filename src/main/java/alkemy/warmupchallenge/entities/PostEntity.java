package alkemy.warmupchallenge.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Date;

@Entity(name = "posts")
@SQLDelete(sql = "UPDATE posts SET deleted = true WHERE id=?")
@FilterDef(name = "deletedPostFilter", parameters = @ParamDef(name = "isDeleted", type = "boolean")) //Define los requerimientos, los cuales, serán usados por @Filter
@Filter(name = "deletedPostFilter", condition = "deleted = :isDeleted") //Condición para aplicar el filtro en función del parámetro
@EntityListeners(AuditingEntityListener.class)
@Getter @Setter
@NoArgsConstructor
public class PostEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String body;

    private String imageLink;

    @ManyToOne
    @JsonIgnoreProperties(value = {"posts", "handler","hibernateLazyInitializer"}, allowSetters = true)
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    @CreatedDate
    private Date creationDate;

    private boolean deleted = Boolean.FALSE;

    @ManyToOne
    @JsonIgnoreProperties(value = {"posts", "handler","hibernateLazyInitializer"}, allowSetters = true)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Builder
    public PostEntity(String title, String body, CategoryEntity category, UserEntity user) {
        this.title = title;
        this.body = body;
        this.category = category;
        this.user = user;
    }
}
