package agh.oop.backend;

import agh.oop.backend.model.ImageDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GalleryRepository extends JpaRepository<ImageDAO, Integer> {
}
