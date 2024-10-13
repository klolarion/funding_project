package com.klolarion.funding_project.controller.api_v1;

import com.klolarion.funding_project.converter.DTOConverter;
import com.klolarion.funding_project.domain.entity.Member;
import com.klolarion.funding_project.dto.PaymentMethodListDto;
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
@RequestMapping("/api/f2/v1/my-page")
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
    @GetMapping
    public ResponseEntity<?> myPage() {

        //조회가 5개!!
        //각 서비스결과를 단기간 캐싱해두고 수정시 캐시도 수정하는 방식으로 변경 필요
        Member member = currentMember.getMember();

        MyPageDto myPageDto = new MyPageDto(
                fundingServiceImpl.myFundingList(member.getMemberId()),
                DTOConverter.toDto(memberServiceImpl.getMainPaymentMethod(member.getMemberId()), PaymentMethodListDto::fromDomainToPaymentMethodListDto),
                memberServiceImpl.getMemberPageData(member.getMemberId()),
                memberServiceImpl.getMyActivity(member.getMemberId())
        );
        return ResponseEntity.status(HttpStatus.OK).body(myPageDto);

    }
}
