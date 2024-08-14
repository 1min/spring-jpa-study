package org.example.init.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {
    Page<Item> findPageBy(Pageable page);
    Slice<Item> findSliceBy(Pageable page);
    List<Item> findAllByTitleContains(String searchText);
    @Query(value = "select * from shop.item where id = ?1", nativeQuery = true)
    Item rawQuery1(Integer id);
    @Query(value = "SELECT * FROM shop.item WHERE MATCH(title) AGAINST(?1)",  nativeQuery = true)
    List<Item> fullTextSearch(String text);
}
