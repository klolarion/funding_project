package com.klolarion.funding_project;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.klolarion.funding_project.domain.entity.Member;
import com.klolarion.funding_project.dto.CacheDto;
import com.klolarion.funding_project.repository.MemberRepository;
import com.klolarion.funding_project.util.RedisService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * Entity Json 변형과 redis I/O 테스트
 */

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
public class RedisTest {

    @Autowired
    private RedisService redisService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private MemberRepository memberRepository;


    Member member = null;


    CacheDto cacheTestDto = new CacheDto();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        try {
            String pingResponse = redisTemplate.getConnectionFactory().getConnection().ping();
            member = memberRepository.findById(1L).orElseThrow(() -> new UsernameNotFoundException("User not found"));

            cacheTestDto.setMemberId(member.getMemberId());
            cacheTestDto.setAccount(member.getAccount());
            cacheTestDto.setEmail(member.getEmail());
            cacheTestDto.setTel(member.getTel());
            cacheTestDto.setLastUpdate(LocalDateTime.parse(member.getLastUpdateDate()));
            cacheTestDto.setEnabled(member.isEnabled());
            cacheTestDto.setBanned(member.isBanned());
            cacheTestDto.setRole(member.getRole().getRoleName());

            System.out.println("Cache Data : " + cacheTestDto);
            System.out.println("Redis 연결 성공: " + pingResponse);
        } catch (Exception e) {
            System.err.println("Redis 연결 실패: " + e.getMessage());
        }

        System.out.println("Redis test");
        System.out.println("<<----------------Test start");
        System.out.println();
    }

    @AfterEach
    void finish() {
        System.out.println();
        System.out.println("Test finished ------------>>");
    }

    @Test
    @Order(1)
    @DisplayName("Redis input test")
    void redisInputTest() {

        System.out.println();
        System.out.println("[ Redis input test ]");
        System.out.println();
        System.out.println("<<----------------Redis input start");

        //LocalDateTime을 직렬화하기위한 설정
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        try {
            String value = objectMapper.writeValueAsString(cacheTestDto);

            redisService.setData(cacheTestDto.getAccount() + "_cache", value, 1, TimeUnit.MINUTES);

            System.out.println(">> Data input success <<");

        } catch (JsonProcessingException e) {
            System.out.println(">> Data input failed <<");
            throw new RuntimeException(e);
        }

        System.out.println("Redis input finished ------------>>");
        System.out.println();
    }

    @Test
    @Order(2)
    @DisplayName("Redis output test")
    void redisOutputTest() {

        System.out.println();
        System.out.println("[ Redis output test ]");
        System.out.println();
        System.out.println("<<----------------Redis output start");


        //LocalDateTime을 직렬화하기위한 설정
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        try {
            String data = redisService.getData(cacheTestDto.getAccount() + "_cache");
            System.out.println("Data : " + data);
            CacheDto value = objectMapper.readValue(data, CacheDto.class);
            System.out.println();
            System.out.println(">> Saved cache data <<");
            System.out.println(value.toString());
            System.out.println();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


        System.out.println("Redis output finished ------------>>");
        System.out.println();
    }


}
