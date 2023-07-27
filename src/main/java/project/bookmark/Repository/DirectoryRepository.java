package project.bookmark.Repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.bookmark.Domain.Directory;

import java.util.List;
import java.util.Optional;

@Repository
public interface DirectoryRepository extends JpaRepository<Directory, Long>, DirectoryQueryRepository {
    List<Directory> findByUserId(Long userId);
    @EntityGraph(attributePaths = {"user"})
    @Query("select d from Directory d")
    Optional<Directory> findByIdEntityGraph(Long id);
}
