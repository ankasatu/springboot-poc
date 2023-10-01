package com.tofu.demo.controller;

import com.tofu.demo.service.dto.LabelRequest;
import com.tofu.demo.service.dto.LabelResponse;
import com.tofu.demo.service.label.LabelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.RouterOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/label")
@AllArgsConstructor
public class LabelController {
    private LabelService labelService;

    @GetMapping()
    @Operation(summary = "Get a list of labels")
    @ApiResponse(responseCode = "200", description = "List of labels retrieved successfully")
    public ResponseEntity<List<LabelResponse>> getList() {
        var result = labelService.getList();
        return ResponseEntity.ok(result);
    }

    @PostMapping()
    @Operation(summary = "Insert a new label")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Label inserted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<Object> insert(@RequestBody LabelRequest request) {
        var contextId = labelService.create(request);
        return ResponseEntity.ok(contextId);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Edit an existing label by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Label edited successfully"),
            @ApiResponse(responseCode = "404", description = "Label not found")
    })
    public ResponseEntity<Object> edit(@PathVariable("id") String id, @RequestBody LabelRequest request) {
        labelService.edit(id, request);
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a label by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Label deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Label not found")
    })
    public ResponseEntity<Object> delete(@PathVariable("id") String id) {
        labelService.delete(id);
        return ResponseEntity.ok(null);
    }
}
