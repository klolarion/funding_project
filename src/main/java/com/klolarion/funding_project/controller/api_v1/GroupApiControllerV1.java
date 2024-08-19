package com.klolarion.funding_project.controller.api_v1;

import com.klolarion.funding_project.domain.entity.Group;
import com.klolarion.funding_project.domain.entity.GroupStatus;
import com.klolarion.funding_project.domain.entity.Member;
import com.klolarion.funding_project.dto.funding.FundingListDto;
import com.klolarion.funding_project.dto.group.CreateGroupDto;
import com.klolarion.funding_project.dto.group.GroupDetailDto;
import com.klolarion.funding_project.dto.group.GroupDto;
import com.klolarion.funding_project.dto.group.GroupInfoDto;
import com.klolarion.funding_project.service.FundingServiceImpl;
import com.klolarion.funding_project.service.GroupServiceImpl;
import com.klolarion.funding_project.service.MemberServiceImpl;
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
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/f2/v1/group")
@Slf4j
public class GroupApiControllerV1 {

    private final GroupServiceImpl groupServiceImpl;
    private final MemberServiceImpl memberServiceImpl;
    private final FundingServiceImpl fundingServiceImpl;

    @Operation(summary = "그룹 관리 페이지",
            tags = {"그룹 API - V1"},
            description = "그룹 관리 페이지, 내가 그룹장인 그룹 목록 조회",
            responses = {
                    @ApiResponse(responseCode = "200", description = "그룹 관리 페이지 호출 성공",
                            content = @Content(
                                    schema = @Schema(implementation = GroupDto.class),
                                    mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "그룹 관리 페이지 호출 실패",
                            content = @Content(
                                    mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류",
                            content = @Content(
                                    mediaType = "application/json")),
            })
    @GetMapping
    public ResponseEntity<?> group() {
        try {
            List<GroupDto> groupDtoList = groupServiceImpl.myLeaderGroups();
            if (groupDtoList != null) {
                log.debug("그룹 관리 페이지 호출 성공");
                return ResponseEntity.status(HttpStatus.OK).body(groupDtoList);
            } else {
                log.debug("그룹 관리 페이지 호출 실패");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("그룹 관리 페이지 호출 실패");
            }
        } catch (Exception e) {
            log.error("그룹 페이지 호출 실패(서버 오류)", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류");
        }
    }

    @Operation(summary = "그룹명 중복 확인",
            tags = {"그룹 API - V1"},
            description = "그룹 생성시 그룹명 중복 확인",
            parameters = {
                    @Parameter(name = "groupName", description = "중복여부를 확인할 그룹명", required = true, in = ParameterIn.QUERY),
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "그룹명 사용 가능",
                            content = @Content(
                                    mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "그룹명 중복, 사용 불가",
                            content = @Content(
                                    mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류",
                            content = @Content(
                                    mediaType = "application/json")),
            })
    @GetMapping("/name")
    public ResponseEntity<?> groupNameCheck(@RequestParam String groupName) {
        try {
            boolean result = groupServiceImpl.groupNameCheck(groupName);
            if (result) {
                log.debug("그룹명 사용 가능, Data - ", groupName);
                return ResponseEntity.status(HttpStatus.OK).body("그룹명 사용 가능");
            } else {
                log.debug("그룹명 중복, 사용 불가, Data - ", groupName);
                return ResponseEntity.status(HttpStatus.OK).body("그룹명 중복, 사용 불가");
            }
        } catch (Exception e) {
            log.error("서버 오류", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류");
        }
    }


    @Operation(summary = "그룹 생성",
            tags = {"그룹 API - V1"},
            description = "새 그룹 생성",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "그룹 생성 요청 Dto",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CreateGroupDto.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "그룹 생성 성공",
                            content = @Content(
                                    mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "그룹 생성 실패",
                            content = @Content(
                                    mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류",
                            content = @Content(
                                    mediaType = "application/json")),
            })
    @PostMapping
    public ResponseEntity<?> createGroup(@RequestBody CreateGroupDto createGroupDto) {
        try {
            Group group = groupServiceImpl.startGroup(createGroupDto.getGroupName(), createGroupDto.getGroupCategory());
            if (group != null) {
                log.debug("그룹 생성 성공, Data - ", createGroupDto);
                return ResponseEntity.status(HttpStatus.CREATED).body("그룹 생성 성공");
            } else {
                log.debug("그룹 생성 실패, Data - ", createGroupDto);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("그룹 생성 실패");
            }
        } catch (Exception e) {
            log.error("그룹 생성 실패(서버 오류)", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류");
        }
    }

    @Operation(summary = "내 그룹 관리",
            tags = {"그룹 API - V1"},
            description = "내 그룹 상세 정보, 그룹 멤버 목록, 가입 요청한 사용자 목록, 초대한 사용자 목록 조회",
            parameters = {
                    @Parameter(name = "groupId", description = "조회할 그룹의 ID", required = true, in = ParameterIn.PATH),
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "그룹 조회 성공",
                            content = @Content(
                                    schema = @Schema(implementation = GroupDetailDto.class),
                                    mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "그룹 조회 실패",
                            content = @Content(
                                    mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류",
                            content = @Content(
                                    mediaType = "application/json")),
            })
    @GetMapping("/detail/{groupId}")
    public ResponseEntity<?> detail(@PathVariable Long groupId) {
        try {
            GroupDetailDto groupDetailDto = new GroupDetailDto(
                    groupServiceImpl.groupDetail(groupId),
                    groupServiceImpl.groupMembers(groupId),
                    groupServiceImpl.requestedMembersToMyGroup(groupId),
                    groupServiceImpl.invitedMembersToMyGroup(groupId)
            );
            log.debug("그룹 조회 성공, Data - ", groupId);
            return ResponseEntity.status(HttpStatus.OK).body(groupDetailDto);
        } catch (Exception e) {
            log.error("그룹 조회 실패(서버 오류)", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류");
        }
    }


    @Operation(summary = "사용자 검색",
            tags = {"그룹 API - V1"},
            description = "그룹에 초대할 사용자를 이름으로 검색",
            parameters = {
                    @Parameter(name = "memberName", description = "조회할 사용자 이름", required = true, in = ParameterIn.QUERY),
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "사용자 조회 성공",
                            content = @Content(
                                    schema = @Schema(implementation = GroupDto.class),
                                    mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "사용자 조회 실패",
                            content = @Content(
                                    mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류",
                            content = @Content(
                                    mediaType = "application/json")),
            })
    @GetMapping("/detail/member")
    public ResponseEntity<?> findMember(@RequestParam String memberName) {
        try {
            List<Member> members = memberServiceImpl.searchMember(memberName);
            if (members != null) {
                log.debug("사용자 조회 성공, Data - ", memberName);
                return ResponseEntity.status(HttpStatus.OK).body("사용자 조회 성공");
            } else {
                log.debug("사용자 조회 실패, Data - ", memberName);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("사용자 조회 실패");
            }
        } catch (Exception e) {
            log.error("사용자 조회 실패(서버 오류)", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류");
        }
    }


    @Operation(summary = "그룹에 초대",
            tags = {"그룹 API - V1"},
            description = "사용자를 그룹에 초대",
            parameters = {
                    @Parameter(name = "memberId", description = "초대받을 사용자의 ID", required = true, in = ParameterIn.PATH),
                    @Parameter(name = "groupId", description = "초대할 그룹의 ID", required = true, in = ParameterIn.PATH),
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "초대 성공",
                            content = @Content(
                                    schema = @Schema(implementation = GroupDto.class),
                                    mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "초대 실패",
                            content = @Content(
                                    mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류",
                            content = @Content(
                                    mediaType = "application/json")),
            })
    @PostMapping("/detail/member/{memberId}/{groupId}")
    public ResponseEntity<?> inviteMember(@PathVariable Long memberId, @PathVariable Long groupId) {
        try {
            GroupStatus groupStatus = groupServiceImpl.inviteMember(groupId, memberId);
            if (groupStatus != null) {
                log.debug("사용자 초대 성공, Data - ", memberId, ", ", groupId);
                return ResponseEntity.status(HttpStatus.OK).body("사용자 초대 성공");
            } else {
                log.debug("사용자 초대 실패, Data - ", memberId, ", ", groupId);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("사용자 초대 실패");
            }
        } catch (Exception e) {
            log.error("그룹 초대 실패(서버 오류)", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류");
        }
    }

    @Operation(summary = "가입요청 수락",
            tags = {"그룹 API - V1"},
            description = "사용자의 그룹 가입요청을 수락",
            parameters = {
                    @Parameter(name = "memberId", description = "초대받을 사용자의 ID", required = true, in = ParameterIn.PATH),
                    @Parameter(name = "groupId", description = "초대할 그룹의 ID", required = true, in = ParameterIn.PATH),
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 수락 성공",
                            content = @Content(
                                    schema = @Schema(implementation = GroupDto.class),
                                    mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "요청 수락 실패",
                            content = @Content(
                                    mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류",
                            content = @Content(
                                    mediaType = "application/json")),
            })
    @PutMapping("/detail/member/{memberId}/{groupId}")
    public ResponseEntity<?> acceptRequest(@PathVariable Long memberId, @PathVariable Long groupId) {
        try {
            boolean result = groupServiceImpl.acceptMemberRequest(groupId, memberId);
            if (result) {
                log.debug("요청 수락 성공, Data - ", memberId, ", ", groupId);
                return ResponseEntity.status(HttpStatus.OK).body("요청 수락 성공");
            } else {
                log.debug("요청 수락 실패, Data - ", memberId, ", ", groupId);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("요청 수락 실패");
            }
        } catch (Exception e) {
            log.error("요청 수락 실패(서버 오류)", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류");
        }
    }


    //group Info

    @Operation(summary = "그룹 정보 조회",
            tags = {"그룹 API - V1"},
            description = "그룹 정보 조회",
            parameters = {
                    @Parameter(name = "groupId", description = "조회할 그룹의 ID", required = true, in = ParameterIn.PATH),
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "그룹 정보 조회 성공",
                            content = @Content(
                                    schema = @Schema(implementation = GroupInfoDto.class),
                                    mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "그룹 정보 조회 실패",
                            content = @Content(
                                    mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류",
                            content = @Content(
                                    mediaType = "application/json")),
            })
    @GetMapping("/info/{groupId}")
    public ResponseEntity<?> info(@PathVariable Long groupId) {
        try {
            Map<String, List<FundingListDto>> groupedMap = fundingServiceImpl.allFundingListByGroup(groupId);
            GroupInfoDto groupInfoDto = new GroupInfoDto(
                    groupServiceImpl.groupDetail(groupId),
                    groupServiceImpl.groupMembers(groupId),
                    groupedMap.values()
                            .stream()
                            .flatMap(List::stream)
                            .collect(Collectors.toList())
            );
            log.debug("그룹 조회 성공, Data - ", groupId);
            return ResponseEntity.status(HttpStatus.OK).body(groupInfoDto);
        } catch (Exception e) {
            log.error("그룹 조회 실패(서버 오류)", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류");
        }
    }


    @Operation(summary = "그룹 가입 신청",
            tags = {"그룹 API - V1"},
            description = "그룹에 가입을 요청",
            parameters = {
                    @Parameter(name = "groupId", description = "가입 요청할 그룹의 ID", required = true, in = ParameterIn.PATH),
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "가입 요청 성공",
                            content = @Content(
                                    schema = @Schema(implementation = GroupDto.class),
                                    mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "가입 요청 실패",
                            content = @Content(
                                    mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류",
                            content = @Content(
                                    mediaType = "application/json")),
            })
    @PostMapping("/info/request/{groupId}")
    public ResponseEntity<?> requestToGroup(@PathVariable Long groupId) {
        try {
            GroupStatus groupStatus = groupServiceImpl.requestToGroup(groupId);
            if(groupStatus != null){
                log.debug("가입 요청 성공, Data - ", groupId);
                return ResponseEntity.status(HttpStatus.OK).body("가입 요청 성공");
            }else{
                log.debug("가입 요청 실패, Data - ", groupId);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("가입 요청 실패");
            }
        } catch (Exception e) {
            log.error("가입 요청 실패(서버 오류)", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류");
        }
    }

}
