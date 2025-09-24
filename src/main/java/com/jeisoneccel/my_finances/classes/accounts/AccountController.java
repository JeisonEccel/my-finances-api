package com.jeisoneccel.my_finances.classes.accounts;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService service;

    @GetMapping({"", "/"})
    public List<Account> getAll() {
        return service.getAll();
    }

    @GetMapping({"/{id}", "/{id}/"})
    public ResponseEntity<Account> getById(@PathVariable String id) {
        return new ResponseEntity<>(service.getById(id), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Account> create(@RequestBody AccountModel model) {
        return new ResponseEntity<>(service.create(model), HttpStatus.CREATED);
    }

    @PutMapping({"/{id}", "/{id}/"})
    public ResponseEntity<Account> update(@PathVariable String id, @RequestBody HashMap<String, Object> updates) {
        return new ResponseEntity<>(service.update(id, updates), HttpStatus.OK);
    }

    @DeleteMapping({"/{id}", "/{id}/"})
    public ResponseEntity<HttpStatus> delete(@PathVariable String id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
