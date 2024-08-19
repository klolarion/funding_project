package com.klolarion.funding_project.controller.api_v1;

import com.klolarion.funding_project.domain.entity.*;
import com.klolarion.funding_project.dto.PaymentMethodDto;
import com.klolarion.funding_project.dto.admin.AdminDto;
import com.klolarion.funding_project.service.AdminServiceImpl;
import com.klolarion.funding_project.service.ProductServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@Slf4j
public class AdminApiController {
    private final AdminServiceImpl adminServiceImpl;
    private final ProductServiceImpl productServiceImpl;

    @GetMapping
    public ResponseEntity<?> admin() {
        try {
            AdminDto adminDto = new AdminDto(
                    //한방쿼리로 수정 가능한지 검토
                    productServiceImpl.allProducts(),
                    adminServiceImpl.getCodes(),
                    adminServiceImpl.paymentMethodList()
            );
            if(adminDto != null) {
                log.debug("정보 조회 성공 : 관리자 메인");
                return ResponseEntity.status(HttpStatus.OK).body(adminDto);
            }else {
                log.debug("정보 조회 실패 : 관리자 메인");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("정보 조회 실패");
            }
        } catch (Exception e) {
            log.error("서버 오류, ", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류");

        }
    }

    @PostMapping("/product")
    public ResponseEntity<?> addProduct(@RequestParam String productName, @RequestParam Long price, @RequestParam int stock) {
        try {
            Product product = adminServiceImpl.addProduct(productName, price, stock);
            if (product != null) {
                log.debug("상품 추가 성공, Data - ", product);
                return ResponseEntity.status(HttpStatus.CREATED).body(product);
            } else {
                log.debug("상품 추가 실패, Data - ", productName);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("상품 추가 실패");
            }
        } catch (Exception e) {
            log.error("서버 오류, ", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류");
        }
    }

    @PutMapping("/stock/up")
    public ResponseEntity<?> addStock(@RequestParam Long productId, @RequestParam int stock) {
        try {
            boolean result = adminServiceImpl.addStock(productId, stock);
            if (result) {
                log.debug("재고 추가 성공, Data - ", productId, ", ", stock);
                return ResponseEntity.status(HttpStatus.OK).body("재고 추가 성공");
            } else {
                log.debug("재고 추가 실패, Data - ", productId, ", ", stock);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("재고 추가 실패");
            }
        } catch (Exception e) {
            log.error("서버 오류, ", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류");
        }
    }

    @PutMapping("/stock/down")
    public ResponseEntity<?> subStock(@RequestParam Long productId, @RequestParam int stock) {
        try {
            boolean result = adminServiceImpl.addStock(productId, stock);
            if (result) {
                log.debug("재고 삭제 성공, Data - ", productId, ", ", stock);
                return ResponseEntity.status(HttpStatus.OK).body("재고 삭제 성공");
            } else {
                log.debug("재고 삭제 실패, Data - ", productId, ", ", stock);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("재고 삭제 실패");
            }
        } catch (Exception e) {
            log.error("서버 오류, ", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류");
        }
    }

    @PutMapping("/restock")
    public ResponseEntity<?> restock(@RequestParam Long productId) {
        try {
            boolean result = adminServiceImpl.setRestock(productId);
            if (result) {
                log.debug("재입고설정 완료, Data - ", productId);
                return ResponseEntity.status(HttpStatus.OK).body("재입고설정 성공");
            } else {
                log.debug("재입고설정 실패, Data - ", productId);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("재입고설정 실패");
            }
        } catch (Exception e) {
            log.error("서버 오류, ", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류");
        }
    }

    @PutMapping("/saleFinished")
    public ResponseEntity<?> saleFinished(@RequestParam Long productId) {
        try {
            boolean result = adminServiceImpl.setSellFinished(productId);
            if (result) {
                log.debug("판매종료설정 성공, Data - ", productId);
                return ResponseEntity.status(HttpStatus.OK).body("판매종료설정 성공");
            } else {
                log.debug("판매종료설정 실패, Data - ", productId);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("판매종료설정 실패");
            }
        } catch (Exception e) {
            log.error("서버 오류, ", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류");
        }
    }

    //검색
    @GetMapping("/product/{productId}")
    public ResponseEntity<?> productSearch(@PathVariable Long productId) {
        try {
            Product product = productServiceImpl.getProduct(productId);
            if (product != null) {
                log.debug("상품 조회 성공, Data - ", productId);
                return ResponseEntity.status(HttpStatus.OK).body(product);
            } else {
                log.debug("상품 조회 실패, Data - ", productId);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("상품 조회 실패");
            }
        } catch (Exception e) {
            log.error("서버 오류, ", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류");
        }
    }

    @GetMapping("/funding/{fundingId}")
    public ResponseEntity<?> fundingSearch(@PathVariable Long fundingId) {
        try {
            Funding funding = adminServiceImpl.searchFunding(fundingId);
            if (funding != null) {
                return ResponseEntity.status(HttpStatus.OK).body(funding);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("펀딩 조회 실패");
            }
        } catch (Exception e) {
            log.error("서버 오류, ", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류");
        }
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<?> memberSearch(@PathVariable Long memberId) {
        try {
            Member member = adminServiceImpl.searchMember(memberId);
            if (member != null) {
                log.debug("사용자 조회 성공, Data - ", memberId);
                return ResponseEntity.status(HttpStatus.OK).body(member);
            } else {
                log.debug("사용자 조회 실패, Data - ", memberId);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("사용자 조회 실패");
            }
        } catch (Exception e) {
            log.error("서버 오류, ", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류");
        }
    }

    //펀딩
    @PutMapping("/funding")
    public ResponseEntity<?> closeFunding(@RequestBody Long fundingId) {
        try {
            boolean result = adminServiceImpl.closeFunding(fundingId);
            if (result) {
                log.debug("펀딩 중단 성공, Data - ", fundingId);
                return ResponseEntity.status(HttpStatus.OK).body("펀딩 중단 성공");
            } else {
                log.debug("펀딩 중단 실패, Data - ", fundingId);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("펀딩 중단 실패");
            }
        } catch (Exception e) {
            log.error("서버 오류, ", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류");
        }
    }

    @DeleteMapping("/funding")
    public ResponseEntity<?> deleteFunding(@RequestParam Long fundingId) {
        try {
            boolean result = adminServiceImpl.deleteFunding(fundingId);
            if (result) {
                log.debug("펀딩 삭제 성공, Data - ", fundingId);
                return ResponseEntity.status(HttpStatus.OK).body("펀딩 삭제 성공");
            } else {
                log.debug("펀딩 삭제 실패, Data - ", fundingId);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("펀딩 삭제 실패");
            }
        } catch (Exception e) {
            log.error("서버 오류, ", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류");
        }
    }

    //코드
    @PostMapping("/code")
    public ResponseEntity<?> addCode(@RequestParam int code, @RequestParam String description, @RequestParam String reference) {
        try {
            CodeMaster codeMaster = adminServiceImpl.addCode(code, description, reference);
            if (codeMaster != null) {
                log.debug("코드 생성 성공, Data - ", code, ", ", description, ", ", reference);
                return ResponseEntity.status(HttpStatus.CREATED).body(codeMaster);
            } else {
                log.debug("코드 생성 실패, Data - ", code, ", ", description, ", ", reference);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("코드 생성 실패");
            }
        } catch (Exception e) {
            log.error("서버 오류, ", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류");
        }
    }

    @DeleteMapping("/code")
    public ResponseEntity<?> deleteCode(@RequestParam Long codeId) {
        try {
            boolean result = adminServiceImpl.deleteCode(codeId);
            if (result) {
                log.debug("코드 삭제 성공, Data - ", codeId);
                return ResponseEntity.status(HttpStatus.OK).body("코드 삭제 성공");
            } else {
                log.debug("코드 삭제 실패, Data - ", codeId);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("코드 삭제 실패");
            }
        } catch (Exception e) {
            log.error("서버 오류, ", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류");
        }
    }

    //결제 수단
    @PostMapping("/paymentMethod")
    public ResponseEntity<?> addPaymentMethod(@RequestParam Long codeId, @RequestParam String accountNumber, @RequestParam Long availableAmount) {
        try {
//        adminServiceImpl.getCode(codeId);
            PaymentMethodDto paymentMethodDto = new PaymentMethodDto(adminServiceImpl.getCode(codeId), accountNumber, availableAmount);
            PaymentMethod paymentMethod = adminServiceImpl.addPaymentMethod(paymentMethodDto);
            if (paymentMethodDto != null && paymentMethod != null) {
                log.debug("결제수단 추가 성공, Data - ", codeId);
                return ResponseEntity.status(HttpStatus.CREATED).body("결제수단 추가 성공");
            } else {
                log.debug("결제수단 추가 실패, Data - ", codeId);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("결제수단 추가 실패");
            }
        } catch (Exception e) {
            log.error("서버 오류, ", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류");
        }
    }

//    @GetMapping("/paymentMethod")
//    public String deletePaymentMethod(@RequestParam Long paymentMethodId) {
//        adminServiceImpl.deletePaymentMethod(paymentMethodId);
//        return "redirect:/f1/admin";
//    }
}
