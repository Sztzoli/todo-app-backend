package com.example.todoapp.repositories;

import com.example.todoapp.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {


    @Query("select c from Category c left join fetch c.todos where c.id = :id")
    Optional<Category> findCategoryId(Long id);

}
