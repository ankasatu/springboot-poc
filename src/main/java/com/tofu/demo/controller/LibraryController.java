package com.tofu.demo.controller;

import com.tofu.demo.service.dto.BookLabelResponse;
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

    @PostMapping("/assign/book/{id}/labels")
    public ResponseEntity<Object> setLabel(@PathVariable("id") String id, @RequestBody List<String> labelIds) {
        libraryService.setBookLabel(id, labelIds);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/label/{labelId}/book")
    public ResponseEntity<BookLabelResponse> getBooksByLabelId(@PathVariable("labelId") String labelId) {
        var result = libraryService.getBooksByLabel(labelId);
        return ResponseEntity.ok(result);
    }

}
