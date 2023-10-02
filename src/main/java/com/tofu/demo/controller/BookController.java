package com.tofu.demo.controller;

import com.tofu.demo.service.book.BookService;
import com.tofu.demo.service.dto.BookDetailResponse;
import com.tofu.demo.service.dto.BookRequest;
import com.tofu.demo.service.dto.BookResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/book")
@AllArgsConstructor
public class BookController {
    private BookService bookService;

    @GetMapping()
    @Operation(summary = "Get a list of book")
    @ApiResponse(responseCode = "200", description = "List of books retrieved successfully")
    public ResponseEntity<List<BookResponse>> getList() {
        var result = bookService.getList();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/isbn/{isbn}")
    @Operation(summary = "Get a detail of book by isbn")
    @ApiResponse(responseCode = "200", description = "List of books retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Book not found")
    public ResponseEntity<BookDetailResponse> getDetailByIsbn(@PathVariable("isbn") String isbn) {
        var result = bookService.getBookByIsbn(isbn);
        return ResponseEntity.ok(result);
    }

    @PostMapping()
    @Operation(summary = "Insert a new book")
    @ApiResponse(responseCode = "200", description = "Book inserted successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    public ResponseEntity<Object> insert(@RequestBody BookRequest request) {
        var contextId = bookService.create(request);
        return ResponseEntity.ok(contextId);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Edit an existing book by ID")
    @ApiResponse(responseCode = "200", description = "Book edited successfully")
    @ApiResponse(responseCode = "404", description = "Book not found")
    public ResponseEntity<Object> edit(@PathVariable("id") String id, @RequestBody BookRequest request) {
        bookService.edit(id, request);
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a book by ID")
    @ApiResponse(responseCode = "200", description = "Book deleted successfully")
    @ApiResponse(responseCode = "400", description = "Book not found")
    public ResponseEntity<Object> delete(@PathVariable("id") String id) {
        bookService.delete(id);
        return ResponseEntity.ok(null);
    }
}
