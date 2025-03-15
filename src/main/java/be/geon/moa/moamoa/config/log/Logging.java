package be.geon.moa.moamoa.config.log;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Logging {
    /**
     * 로깅할 액션명을 지정합니다.
     */
    String action();

    /**
     * 응답 결과를 로그에 포함할지 여부를 지정합니다.
     * 기본값은 false입니다.
     */
    boolean includeResponse() default false;

    /**
     * 파라미터를 로그에 포함할지 여부를 지정합니다.
     * 기본값은 true입니다.
     */
    boolean includeParameters() default true;
}