package com.klolarion.funding_project.controller.api_v1;

import com.klolarion.funding_project.domain.entity.Member;
import com.klolarion.funding_project.dto.member.MyPageDto;
import com.klolarion.funding_project.service.FundingServiceImpl;
import com.klolarion.funding_project.service.MemberServiceImpl;
import com.klolarion.funding_project.util.CurrentMember;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/f2/v1")
@Slf4j
public class MyPageApiControllerV1 {
    private final FundingServiceImpl fundingServiceImpl;
    private final MemberServiceImpl memberServiceImpl;
    private final CurrentMember currentMember;

    @Operation(summary = "마이 페이지",
            tags = {"마이 페이지 API - V1"},
            description = "내 펀딩, 내가 생성한 그룹, 주 결제수단, 참여중인 그룹, 결제수단 목록 조회",
            responses = {
                    @ApiResponse(responseCode = "200", description = "정상 호출",
                            content = @Content(
                                    schema = @Schema(implementation = MyPageDto.class),
                                    mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류",
                            content = @Content(
                                    mediaType = "application/json")),
            })
    @GetMapping("/mypage")
    public ResponseEntity<?> myPage() {
        try {
            //조회가 5개!!
            Member member = currentMember.getMember();
            // -> 컨트롤러에서 멤버 조회 후 서비스에 일괄전달방식으로 변경 필요 또는 마이페이지 서비스를 따로 만들어 거대한 한방쿼리 가능한지 검토 필요
            MyPageDto myPageDto = new MyPageDto(
                    fundingServiceImpl.myFundingList(),
                    memberServiceImpl.getMainPaymentMethod(),
                    memberServiceImpl.getMemberPageData(),
                    memberServiceImpl.getMyActivity()
            );
            return ResponseEntity.status(HttpStatus.OK).body(myPageDto);
        }catch (Exception e){
            log.error("마이 페이지 호출 실패(서버 오류)", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("호출 실패");
        }
    }
}
