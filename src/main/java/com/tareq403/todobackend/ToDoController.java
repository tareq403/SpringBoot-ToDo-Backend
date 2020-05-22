package com.tareq403.todobackend;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ToDoController {
    private final ToDoRepository repository;

    public ToDoController(ToDoRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/todo")
    List<ToDo> findAll() {
        return this.repository.findAll();
    }

    @GetMapping("/todo/{id}")
    ToDo findById(@PathVariable String id) {
        return this.repository.findById(id).orElseThrow(() -> new RuntimeException("Not found."));
    }

    @PostMapping("/todo")
    ToDo save(@RequestBody ToDo toDo) {
        return this.repository.save(toDo);
    }

    @PutMapping("/todo/{id}")
    ToDo replaceById(@RequestBody ToDo newToDo, @PathVariable String id) {
        return this.repository.findById(id)
                .map(toDo -> {
                    toDo.setName(newToDo.getName());
                    toDo.setDone(newToDo.isDone());
                    return repository.save(toDo);
                })
                .orElseGet(() -> {
                    newToDo.setId(id);
                    return this.repository.save(newToDo);
                });
    }

    @DeleteMapping("/todo/{id}")
    void deleteById(@PathVariable String id) {
        this.repository.deleteById(id);
    }
}
