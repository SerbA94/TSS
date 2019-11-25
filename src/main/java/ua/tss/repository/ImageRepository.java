package ua.tss.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ua.tss.model.Image;

@Repository
public interface ImageRepository extends CrudRepository<Image, Long> {

}
