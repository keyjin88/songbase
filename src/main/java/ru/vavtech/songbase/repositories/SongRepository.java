package ru.vavtech.songbase.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.vavtech.songbase.model.Song;

import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
//    List<Song> findByTitleLikeIgnoreCaseOrArtistLikeIgnoreCase(String filter);

    @Query("select s from Song s where lower(s.title) like lower(concat('%', :filter, '%')) or lower(s.artist) like lower(concat('%', :filter, '%'))")
    List<Song> findAllByFilter(@Param("filter") String filter);
}
