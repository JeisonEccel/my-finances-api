package com.jeisoneccel.my_finances.classes.categories;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService service;

    @GetMapping({"", "/"})
    public List<Category> getAll() {
        return service.getAll();
    }

    @GetMapping({"/{id}", "/{id}/"})
    public ResponseEntity<Category> getById(@PathVariable String id) {
        return new ResponseEntity<>(service.getById(id), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Category> create(@RequestBody CategoryModel model) {
        return new ResponseEntity<>(service.create(model), HttpStatus.CREATED);
    }

    @PutMapping({"/{id}", "/{id}/"})
    public ResponseEntity<Category> update(@PathVariable String id, @RequestBody HashMap<String, Object> updates) {
        return new ResponseEntity<>(service.update(id, updates), HttpStatus.OK);
    }

    @DeleteMapping({"/{id}", "/{id}/"})
    public ResponseEntity<HttpStatus> delete(@PathVariable String id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
