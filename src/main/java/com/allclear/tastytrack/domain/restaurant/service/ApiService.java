package com.allclear.tastytrack.domain.restaurant.service;

import com.allclear.tastytrack.domain.restaurant.dto.LocalDataResponse;
import com.allclear.tastytrack.domain.restaurant.dto.RawRestaurantResponse;
import com.allclear.tastytrack.domain.restaurant.entity.RawRestaurant;
import com.allclear.tastytrack.domain.restaurant.repository.RawRestaurantRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiService {

    private final RawRestaurantRepository rawRestaurantRepository;
    private final ObjectMapper objectMapper;

    @Value("${api.key}")
    private String apiKey; // API 인증키

    /**
     * 서울 맛집 데이터를 수집하여 DB 맛집 원본 테이블에 저장합니다.
     * 작성자 : 유리빛나
     *
     * @param startIndex 요청 시작 위치
     * @param endIndex 요청 종료 위치
     */
    public void getRawRestaurants(String startIndex, String endIndex) {

        // 요청 URL 구성
        String apiUrl = "http://openapi.seoul.go.kr:8088"; // URL
        String responseType = "json";                      // 요청파일 타입
        String serviceName = "LOCALDATA_072404";           // 서비스명

        URI uri = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .pathSegment(apiKey, responseType, serviceName, startIndex, endIndex)
                .build()
                .encode()
                .toUri();

        // RestTemplate 사용하여 GET 요청 보내기
        RestTemplate template = new RestTemplate();

        String jsonResponse = template.getForObject(uri, String.class);
        log.info("JSON 응답:" + jsonResponse);

        // JSON 데이터를 파싱하여 엔티티로 변환
        // ObjectMapper를 사용하여 역직렬화 (JSON 문자열을 Java 객체로 변환)
        try {
            LocalDataResponse localDataResponse = objectMapper.readValue(jsonResponse, LocalDataResponse.class);

            List<RawRestaurantResponse> rows = localDataResponse.getLocalData().getRawRestaurantResponses();

            for (RawRestaurantResponse raw : rows) {
                RawRestaurant restaurant = getRawRestaurantBuild(raw);

                rawRestaurantRepository.save(restaurant);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 공공데이터의 맛집 응답 데이터를 원본 맛집 DB Entity에 저장하는 Builder 메서드
     * 작성자 : 유리빛나
     * 
     * @param raw 공공데이터의 맛집 응답 데이터
     * @return 맛집 응답 데이터가 저장된 원본 맛집 DB Entity
     */
    private static RawRestaurant getRawRestaurantBuild(RawRestaurantResponse raw) {

        return RawRestaurant.builder()
                .mgtno(raw.getMgtno())
                .dtlstategbn(raw.getDtlstategbn())
                .bplcnm(raw.getBplcnm())
                .uptaenm(raw.getUptaenm())
                .dcbymd(raw.getDcbymd())
                .sitepostno(raw.getSitepostno())
                .sitewhladdr(raw.getSitewhladdr())
                .rdnwhladdr(raw.getRdnwhladdr())
                .rdnpostno(raw.getRdnpostno())
                .lastmodts(raw.getLastmodts())
                .lon(raw.getLon())
                .lat(raw.getLat())
                .build();
    }

}
