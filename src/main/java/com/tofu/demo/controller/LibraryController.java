package com.tofu.demo.controller;

import com.tofu.demo.service.dto.BookRequest;
import com.tofu.demo.service.dto.BookResponse;
import com.tofu.demo.service.library.LibraryService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/library")
@AllArgsConstructor
public class LibraryController {

    private LibraryService libraryService;

    @GetMapping("/books")
    public ResponseEntity<List<BookResponse>> getBooks() {
        var result = libraryService.getBooks();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/book/{isbn}")
    public ResponseEntity<Object> getBookByIsbn(@PathVariable("isbn") String isbn) {
        var result = libraryService.getBookDetail(isbn);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/book")
    public ResponseEntity<Object> insertBook(@RequestBody BookRequest request) {
        var bookId = libraryService.bookCreate(request);
        return ResponseEntity.ok(bookId);
    }

    @PutMapping("/book/{id}")
    public ResponseEntity<Object> updateBook(@PathVariable("id") String id, @RequestBody BookRequest request) {
        libraryService.bookUpdate(id, request);
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/book/{id}")
    public ResponseEntity<Object> updateBook(@PathVariable("id") String id) {
        libraryService.bookDelete(id);
        return ResponseEntity.ok(null);
    }

    @PostMapping("/book/{id}/label")
    public ResponseEntity<Object> insertBook(@PathVariable("id") String id, @RequestBody List<String> request) {
        libraryService.labelAssign(id, request);
        return ResponseEntity.ok(null);
    }

}
