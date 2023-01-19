package agh.oop.backend.persistence;

import agh.oop.backend.model.ImageDescriptor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GalleryRepository extends PagingAndSortingRepository<ImageDescriptor, Integer> {
}
