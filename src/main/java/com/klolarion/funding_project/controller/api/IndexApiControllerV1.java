package com.klolarion.funding_project.controller.api;

import com.klolarion.funding_project.domain.entity.Member;
import com.klolarion.funding_project.dto.IndexDto;
import com.klolarion.funding_project.dto.funding.FundingListDto;
import com.klolarion.funding_project.dto.group.GroupDto;
import com.klolarion.funding_project.service.FundingServiceImpl;
import com.klolarion.funding_project.service.GroupServiceImpl;
import com.klolarion.funding_project.service.MemberServiceImpl;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/f1/v1")
@Slf4j
public class IndexApiControllerV1 {
    private final FundingServiceImpl fundingServiceImpl;
    private final MemberServiceImpl memberService;
    private final GroupServiceImpl groupServiceImpl;

    @GetMapping
    public ResponseEntity<?> getIndex(){
        IndexDto indexDto = new IndexDto(
                fundingServiceImpl.allFundingList(),
                groupServiceImpl.allGroupExceptMy()
        );
        return ResponseEntity.status(HttpStatus.OK).body(indexDto);
    }
}
