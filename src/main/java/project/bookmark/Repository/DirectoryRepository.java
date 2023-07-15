package project.bookmark.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.bookmark.Domain.Directory;

import java.util.List;

@Repository
public interface DirectoryRepository extends JpaRepository<Directory, Long>, DirectoryQueryRepository {
    List<Directory> findByUserId(Long userId);
}
