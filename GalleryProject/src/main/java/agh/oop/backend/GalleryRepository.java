package agh.oop.backend;

import agh.oop.backend.model.ImageDescriptor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GalleryRepository extends JpaRepository<ImageDescriptor, Integer> {
}
