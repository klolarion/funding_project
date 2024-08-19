package com.klolarion.funding_project.controller.api_v1;

import com.klolarion.funding_project.domain.entity.Member;
import com.klolarion.funding_project.domain.entity.PaymentMethod;
import com.klolarion.funding_project.domain.entity.PaymentMethodList;
import com.klolarion.funding_project.dto.member.MyPageDto;
import com.klolarion.funding_project.dto.member.PaymentMethodDto;
import com.klolarion.funding_project.service.AdminServiceImpl;
import com.klolarion.funding_project.service.MemberServiceImpl;
import com.klolarion.funding_project.util.CurrentMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/f2/v1/payment/method")
@Slf4j
public class PaymentMethodApiControllerV1 {
    private final MemberServiceImpl memberService;
    private final AdminServiceImpl adminService;
    private final CurrentMember currentMember;

    @Operation(summary = "결제수단 페이지",
            tags = {"결제수단 API - V1"},
            description = "내 주 결제수단, 내 결제수단 목록, 모든 결제수단 목록 조회",
            responses = {
                    @ApiResponse(responseCode = "200", description = "정상 호출",
                            content = @Content(
                                    schema = @Schema(implementation = PaymentMethodDto.class),
                                    mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "결제수단 페이지 호출 실패",
                            content = @Content(
                                    mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류",
                            content = @Content(
                                    mediaType = "application/json")),
            })
    @GetMapping
    public ResponseEntity<?> paymentMethod() {
        try {
            Member member = currentMember.getMember();
            //한방쿼리로 수정 가능한지 검토
            PaymentMethodDto paymentMethodDto = new PaymentMethodDto(
                    memberService.myPaymentLists(member.getMemberId()),
                    adminService.paymentMethodList(),
                    memberService.getMainPaymentMethod()
            );

            if(paymentMethodDto != null) {
                log.debug("결제수단 페이지 호출 성공");
                return ResponseEntity.status(HttpStatus.OK).body(paymentMethodDto);
            }else{
                log.debug("결제수단 페이지 호출 실패");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("결제수단 페이지 호출 실패");
            }
        } catch (Exception e) {
            log.error("서버 오류", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류");
        }
    }


    @Operation(summary = "결제수단 추가",
            tags = {"결제수단 API - V1"},
            description = "해당 결제수단을 내 결제수단 목록에 추가",
            parameters = {
                    @Parameter(name = "paymentMethodId", description = "내 결제수단 목록에 추가할 결제수단의 ID", required = true, in = ParameterIn.PATH),
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "결제수단 추가 성공",
                            content = @Content(
                                    mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "결제수단 추가 실패",
                            content = @Content(
                                    mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류",
                            content = @Content(
                                    mediaType = "application/json")),
            })
    @PostMapping("/{paymentMethodId}")
    public ResponseEntity<?> addMyPaymentMethod(@PathVariable Long paymentMethodId) {
        try {
            PaymentMethodList paymentMethodList = memberService.addPaymentMethod(paymentMethodId);
            if(paymentMethodList != null){
                log.debug("결제수단 추가 성공");
                return ResponseEntity.status(HttpStatus.OK).body("결제수단 추가 성공");
            }else{
                log.debug("결제수단 추가 실패");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("결제수단 추가 실패");
            }
        } catch (Exception e) {
            log.error("서버 오류", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류");
        }
    }


    @Operation(summary = "주 결제수단으로 설정",
            tags = {"결제수단 API - V1"},
            description = "등록된 결제수단을 주 결제수단 목록으로 지정",
            parameters = {
                    @Parameter(name = "myPaymentMethodId", description = "주 결제수단으로 지정될 결제수단의 ID", required = true, in = ParameterIn.PATH),
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "주 결제수단 지정 성공",
                            content = @Content(
                                    schema = @Schema(implementation = PaymentMethodDto.class),
                                    mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "주 결제수단 지정 실패",
                            content = @Content(
                                    mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류",
                            content = @Content(
                                    mediaType = "application/json")),
            })
    @PutMapping("/{myPaymentMethodId}")
    public ResponseEntity<?> addMainPaymentMethod(@PathVariable Long myPaymentMethodId) {
        try {
            boolean result = memberService.makeMainPayment(myPaymentMethodId);
            if(result){
                log.debug("주 결제수단 지정 성공");
                return ResponseEntity.status(HttpStatus.OK).body("주 결제수단 지정 성공");
            }else{
                log.debug("주 결제수단 지정 실패");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("주 결제수단 지정 실패");
            }
        } catch (Exception e) {
            log.error("서버 오류", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류");
        }
    }


    @Operation(summary = "결제수단 등록 해제",
            tags = {"결제수단 API - V1"},
            description = "등록된 결제수단 등록 해제, 주 결제수단이 아닐 것",
            parameters = {
                    @Parameter(name = "myPaymentMethodId", description = "등록 해제할 결제수단의 ID", required = true, in = ParameterIn.PATH),
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "결제수단 등록 해제 성공",
                            content = @Content(
                                    mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "결제수단 등록 해제 실패",
                            content = @Content(
                                    mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류",
                            content = @Content(
                                    mediaType = "application/json")),
            })
    @DeleteMapping("/{myPaymentMethodId}")
    public ResponseEntity<?> deletePaymentMethod(@PathVariable Long myPaymentMethodId) {
        try {
            boolean result = memberService.removePayment(myPaymentMethodId);
            if(result){
                log.debug("결제수단 등록 해제 성공");
                return ResponseEntity.status(HttpStatus.OK).body("결제수단 등록 해제 성공");
            }else{
                log.debug("결제수단 등록 해제 실패");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("결제수단 등록 해제 실패");
            }
        } catch (Exception e) {
            log.error("서버 오류", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류");
        }
    }
}
