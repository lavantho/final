package com.insmart.app.service;

import org.springframework.http.ResponseEntity;

import java.io.Serializable;
import java.util.List;

public interface Service <T extends Serializable>  {
    ResponseEntity<List<?>> getAll();
    ResponseEntity<?> getOne(Long id);
    ResponseEntity<?> create(T t);
    ResponseEntity<?> update(Long id, T t);
    ResponseEntity<?> delete(Long id);

}
