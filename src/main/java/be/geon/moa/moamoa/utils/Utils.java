package be.geon.moa.moamoa.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import java.util.stream.StreamSupport;

@Slf4j
public class Utils {

    private static final ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.USE_LONG_FOR_INTS, true);

    private static final byte[] IV = {
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00
    };


//    @Value("#{moa.secret}")
//    private String SECRET_KEY;
    private static final String SECRET_KEY = "ABCDEFGH12345678";


    /**
     * Iterable 컬렉션에서 null이 아닌 요소들의 합을 계산합니다.
     *
     * @param iterable 합을 계산할 Iterable 컬렉션
     * @param selector 각 요소에서 Long 값을 추출하는 선택자 함수
     * @return null이 아닌 요소들의 합
     */
    public static <T> Long sumOfNotNull(Iterable<T> iterable, Function<T, Long> selector) {
        return StreamSupport.stream(iterable.spliterator(), false)
                .map(selector)
                .filter(Objects::nonNull)  // null 값 제외
                .reduce(0L, Long::sum);    // 합계 계산
    }

    /**
     * Iterable 컬렉션의 요소들이 주어진 선택자 함수에 의해 고유한지 확인합니다.
     *
     * @param iterable 검사할 Iterable 컬렉션
     * @param selector 각 요소에서 비교할 키를 추출하는 선택자 함수
     * @return 모든 요소가 고유하면 true, 중복이 있으면 false
     */
    public static <T, K> boolean isUniqueBy(Iterable<T> iterable, Function<T, K> selector) {
        Set<K> set = new HashSet<>();
        for (T item : iterable) {
            // 선택자 함수를 적용한 결과를 Set에 추가
            // 추가에 실패하면 (이미 존재하면) false 반환
            if (!set.add(selector.apply(item))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 주어진 JSON 문자열을 지정된 타입의 Java 객체로 변환합니다.
     *
     * @param json 변환할 JSON 문자열
     * @param <T>  변환할 타겟 객체의 타입
     * @return 변환된 Java 객체
     * @throws RuntimeException JSON 처리 중 오류가 발생한 경우
     */
    public static <T> T convert(String json) {
        try {
            return mapper.readValue(json, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 주어진 JSON 객체를 Map으로 변환합니다.
     *
     * @param object 변환할 JSON 객체
     * @param <T>    변환할 타겟 클래스의 타입
     * @return 변환된 Map 객체
     */
    public static <T> T convert(Object object) {
        return mapper.convertValue(object, new TypeReference<>() {
        });
    }

    /**
     * 주어진 JSON 객체를 지정된 클래스 타입의 객체로 변환합니다.
     *
     * @param object 변환할 JSON 객체
     * @param type   변환할 클래스 타입
     * @param <T>    변환할 타겟 클래스의 타입
     * @return 변환된 객체
     */
    public static <T> T convert(Object object, Class<T> type) {
        return mapper.convertValue(object, type);
    }

    /**
     * 주어진 JSON 배열을 지정된 타입의 리스트로 변환합니다.
     *
     * @param object 변환할 JSON 배열
     * @param type   리스트에 포함될 객체의 클래스 타입
     * @param <T>    리스트에 포함될 객체의 타입
     * @return 변환된 리스트 객체
     */
    public static <T> List<T> convertList(Object object, Class<T> type) {
        return mapper.convertValue(object, mapper.getTypeFactory().constructCollectionType(List.class, type));
    }

    /**
     * 주어진 객체를 JSON 문자열로 변환합니다.
     *
     * @param object 변환할 객체
     * @return JSON 문자열
     */
    public static String toString(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("convert object to string error", e);
            return "";
        }
    }

    /**
     * 주어진 JSON 배열을 지정된 타입의 리스트로 변환합니다.
     *
     * @param object 변환할 Object 객체
     * @return 변경된 문자열
     */
    public static String objectPrettyPrinter(Object object) {
        if (object == null) return "null";

        try {
            // ResponseEntity 처리를 위한 특별 로직
            if (object instanceof ResponseEntity) {
                ResponseEntity<?> responseEntity = (ResponseEntity<?>) object;
                Object body = responseEntity.getBody();

                // 응답 본문이 있는 경우 본문만 예쁘게 출력
                if (body != null) {
                    return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(body);
                }
            }

            // 일반 객체 예쁘게 출력
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (Exception e) {
            return String.format("Failed to convert object: %s", e.getMessage());
        }
    }


    public static String encrypt(String plainText) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(
                    SECRET_KEY.getBytes(StandardCharsets.UTF_8), "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(IV);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

            byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);

        } catch (Exception e) {
            throw new RuntimeException("암호화 오류", e);
        }
    }

    public static String decrypt(String encrypted) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(
                    SECRET_KEY.getBytes(StandardCharsets.UTF_8), "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(IV);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

            byte[] original = cipher.doFinal(Base64.getDecoder().decode(encrypted));
            return new String(original, StandardCharsets.UTF_8);

        } catch (Exception e) {
            throw new RuntimeException("복호화 오류", e);
        }
    }

}
