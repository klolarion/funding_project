package com.klolarion.funding_project.controller.api_v1;

import com.klolarion.funding_project.dto.friend.FriendRequestDto;
import com.klolarion.funding_project.dto.friend.SearchFriendDto;
import com.klolarion.funding_project.service.FriendServiceImpl;
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
@RequestMapping("/api/f2/v1/friend")
@Slf4j
public class FriendApiControllerV1 {
    private final FriendServiceImpl friendService;

    @Operation(summary = "친구 목록",
            tags = {"친구 API - V1"},
            description = "현재 등록된 친구 목록",
            responses = {
                    @ApiResponse(responseCode = "200", description = "정상 호출",
                            content = @Content(
                                    schema = @Schema(implementation = SearchFriendDto.class),
                                    mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류",
                            content = @Content(
                                    mediaType = "application/json")),
            })
    @GetMapping
    public ResponseEntity<?> getAllFriends() {
        List<SearchFriendDto> searchFriendDtos = friendService.myFriendList();
        return ResponseEntity.status(HttpStatus.OK).body(searchFriendDtos);
    }

    ;

    @Operation(summary = "친구 검색",
            tags = {"친구 API - V1"},
            description = "이름으로 친구 검색",
            parameters = {
                    @Parameter(name = "friendName", description = "검색할 친구의 이름", required = true, in = ParameterIn.PATH),
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "친구 검색 성공",
                            content = @Content(
                                    schema = @Schema(implementation = SearchFriendDto.class),
                                    mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "없는 이름",
                            content = @Content(
                                    mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류",
                            content = @Content(
                                    mediaType = "application/json")),
            })
    @GetMapping("/{friendName}")
    public ResponseEntity<?> searchFriend(@PathVariable String friendName) {
        List<SearchFriendDto> searchFriendDtos = friendService.searchFriend(friendName);
        if (searchFriendDtos != null) {
            return ResponseEntity.status(HttpStatus.OK).body(searchFriendDtos);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("친구를 찾을 수 없습니다.");


    }


    @Operation(summary = "친구 요청 목록",
            tags = {"친구 API - V1"},
            description = "내가 받은 친구요청 친구 목록",
            responses = {
                    @ApiResponse(responseCode = "200", description = "정상 호출",
                            content = @Content(
                                    schema = @Schema(implementation = FriendRequestDto.class),
                                    mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류",
                            content = @Content(
                                    mediaType = "application/json")),
            })
    @GetMapping("/request")
    public ResponseEntity<?> getFriendRequests() {
        List<FriendRequestDto> friendRequestDtos = friendService.requestList();
        return ResponseEntity.status(HttpStatus.OK).body(friendRequestDtos);
    }

    ;

    @Operation(summary = "친구 추가",
            tags = {"친구 API - V1"},
            description = "친구 추가 요청",
            parameters = {
                    @Parameter(name = "memberId", description = "추가할 친구의 ID", required = true, in = ParameterIn.PATH),
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "친구 요청 성공",
                            content = @Content(
                                    schema = @Schema(implementation = FriendRequestDto.class),
                                    mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류",
                            content = @Content(
                                    mediaType = "application/json")),
            })
    @PostMapping("/{memberId}")
    public ResponseEntity<?> addFriend(@PathVariable Long memberId) {
        friendService.addFriend(memberId);
        log.debug("친구 요청 성공, Data - ", memberId);
        return ResponseEntity.status(HttpStatus.OK).body("친구 요청 성공");
    }

    ;


    @Operation(summary = "친구 요청 수락",
            tags = {"친구 API - V1"},
            description = "친구 요청 수락",
            parameters = {
                    @Parameter(name = "friendStatusId", description = "수락할 친구신청의 ID", required = true, in = ParameterIn.PATH),
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "친구 요청 수락 성공",
                            content = @Content(
                                    mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "친구 요청 수락 실패",
                            content = @Content(
                                    mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류",
                            content = @Content(
                                    mediaType = "application/json")),
            })
    @PutMapping("/{friendStatusId}")
    public ResponseEntity<?> acceptFriend(@PathVariable Long friendStatusId) {
        boolean result = friendService.acceptFriendRequest(friendStatusId);
        if (result) {
            log.debug("친구 요청 수락 성공, Data - ", friendStatusId);
            return ResponseEntity.status(HttpStatus.OK).body("친구 요청 수락 성공");
        } else {
            log.debug("친구 요청 수락 실패, Data - ", friendStatusId);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("친구 요청 수락 실패");
        }
    }

    ;


    @Operation(summary = "친구 삭제",
            tags = {"친구 API - V1"},
            description = "친구 삭제",
            parameters = {
                    @Parameter(name = "friendId", description = "삭제할 친구의 ID", required = true, in = ParameterIn.PATH),
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "친구 삭제 성공",
                            content = @Content(
                                    mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "친구 삭제 실패",
                            content = @Content(
                                    mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류",
                            content = @Content(
                                    mediaType = "application/json")),
            })
    @DeleteMapping("/{friendId}")
    public ResponseEntity<?> removeFriend(@PathVariable Long friendId) {
        boolean result = friendService.removeFriend(friendId);
        if (result) {
            log.debug("친구 삭제 성공, Data - ", friendId);
            return ResponseEntity.status(HttpStatus.OK).body("친구 삭제 성공");
        } else {
            log.debug("친구 삭제 실패, Data - ", friendId);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("친구 삭제 실패");
        }
    }

    ;

    @Operation(summary = "친구 차단",
            tags = {"친구 API - V1"},
            description = "친구 차단",
            parameters = {
                    @Parameter(name = "friendId", description = "차단할 친구의 ID", required = true, in = ParameterIn.PATH),
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "친구 차단 성공",
                            content = @Content(
                                    mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "친구 차단 실패",
                            content = @Content(
                                    mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류",
                            content = @Content(
                                    mediaType = "application/json")),
            })
    @DeleteMapping("/ban/{friendId}")
    public ResponseEntity<?> banFriend(@PathVariable Long friendId) {
        boolean result = friendService.banMember(friendId);
        if (result) {
            log.debug("친구 차단 성공, Data - ", friendId);
            return ResponseEntity.status(HttpStatus.OK).body("친구 차단 성공");
        } else {
            log.debug("친구 차단 실패, Data - ", friendId);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("친구 차단 실패");
        }
    }

    ;
}
