package alkemy.warmupchallenge.dtos;

import java.util.Date;

public interface PostToShowDto {

    long getId();
    String getTitle();
    String getImageLink();
    CategoryDto getCategory();
    Date getCreationDate();

    interface CategoryDto {
        long getId();
        String getName();
    }



}
