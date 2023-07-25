package project.bookmark.Repository;

import project.bookmark.Domain.Directory;

import java.util.List;

public interface DirectoryQueryRepository {
    List<Directory> findBookmarksAndDirectories(Long user_id);
}
