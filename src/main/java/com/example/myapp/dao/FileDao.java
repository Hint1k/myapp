package com.example.myapp.dao;

import com.example.myapp.entity.FileDb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileDao extends JpaRepository<FileDb, String> {
}