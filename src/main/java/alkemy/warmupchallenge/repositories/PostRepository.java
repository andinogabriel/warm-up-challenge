package alkemy.warmupchallenge.repositories;

import alkemy.warmupchallenge.dtos.PostToShowDto;
import alkemy.warmupchallenge.entities.CategoryEntity;
import alkemy.warmupchallenge.entities.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {

    Optional<PostEntity> findById(long id);

    Page<PostToShowDto> findAllByOrderByCreationDate(Pageable pageable);

    Page<PostToShowDto> findByTitleContaining(Pageable pageable, String title);

    Page<PostToShowDto> findByCategory(Pageable pageable, CategoryEntity categoryEntity);

    Page<PostToShowDto> findByTitleContainingAndCategory(Pageable pageable, String title, CategoryEntity categoryEntity);

}
