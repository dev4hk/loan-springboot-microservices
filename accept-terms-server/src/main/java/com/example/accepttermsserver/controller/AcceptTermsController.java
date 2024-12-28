package com.example.accepttermsserver.controller;

import com.example.accepttermsserver.dto.AcceptTermsRequestDto;
import com.example.accepttermsserver.dto.AcceptTermsResponseDto;
import com.example.accepttermsserver.dto.ResponseDTO;
import com.example.accepttermsserver.service.IAcceptTermsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(
        name = "CRUD REST APIs for Accept Terms",
        description = "CRUD REST APIs to CREATE accept terms details"
)
@Validated
@RequiredArgsConstructor
@RequestMapping("/accept-terms")
@RestController
public class AcceptTermsController {

    private final IAcceptTermsService acceptTermsService;

    @Operation(
            summary = "Create Accept Terms REST API",
            description = "REST API to create new accept terms data"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "HTTP Status Bad Request",
                    content = @Content(
                            schema = @Schema(implementation = ResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error"
            )
    }
    )
    @PostMapping
    public ResponseDTO<List<AcceptTermsResponseDto>> create(@Valid @RequestBody AcceptTermsRequestDto acceptTermsRequestDto) {
        return ResponseDTO.ok(acceptTermsService.create(acceptTermsRequestDto));
    }
}
