package com.klolarion.funding_project.controller.api_v1;

import com.klolarion.funding_project.dto.funding.FundingListDto;
import com.klolarion.funding_project.service.FundingServiceImpl;
import com.klolarion.funding_project.service.GroupServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/f1/v1/index")
@Slf4j
public class IndexApiControllerV1 {
    private final FundingServiceImpl fundingServiceImpl;
    private final GroupServiceImpl groupServiceImpl;


    @Operation(summary = "Index 페이지",
            tags = {"Index API - V1"},
            description = "Index 페이지에 필요한 정보 조회",
            responses = {
                    @ApiResponse(responseCode = "200", description = "정상 호출",
                            content = @Content(
                                    schema = @Schema(implementation = FundingListDto.class),
                                    mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "정보 조회 실패",
                            content = @Content(
                                    mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류",
                            content = @Content(
                                    mediaType = "application/json")),
            })
    @GetMapping
    public ResponseEntity<?> getIndex() {

        List<FundingListDto> result = fundingServiceImpl.allFundingList();
        return ResponseEntity.status(HttpStatus.OK).body(result);

    }
}
