package com.example.entry_server.controller;

import com.example.entry_server.dto.EntryRequestDto;
import com.example.entry_server.dto.EntryResponseDto;
import com.example.entry_server.dto.EntryUpdateResponseDto;
import com.example.entry_server.dto.ResponseDTO;
import com.example.entry_server.service.IEntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/entries")
@RequiredArgsConstructor
public class EntryController {

    private final IEntryService entryService;

    @PostMapping("/{applicationId}")
    public ResponseDTO<EntryResponseDto> createEntry(@PathVariable Long applicationId, @RequestBody EntryRequestDto request) {
        EntryResponseDto response = entryService.create(applicationId, request);
        return ResponseDTO.ok(response);
    }

    @GetMapping("/{applicationId}")
    public ResponseDTO<EntryResponseDto> getEntry(@PathVariable Long applicationId) {
        EntryResponseDto response = entryService.get(applicationId);
        return ResponseDTO.ok(response);
    }

    @PutMapping("/{entryId}")
    public ResponseDTO<EntryUpdateResponseDto> updateEntry(@PathVariable Long entryId, @RequestBody EntryRequestDto request) {
        EntryUpdateResponseDto response = entryService.update(entryId, request);
        return ResponseDTO.ok(response);
    }

    @DeleteMapping("/{entryId}")
    public ResponseDTO<Void> deleteEntry(@PathVariable Long entryId) {
        entryService.delete(entryId);
        return ResponseDTO.ok();
    }
}
