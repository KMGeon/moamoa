package be.geon.moa.moamoa.config.log;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

import static be.geon.moa.moamoa.utils.Utils.objectPrettyPrinter;


@org.aspectj.lang.annotation.Aspect
@Configuration
public class LoggerConfig {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @Around("@annotation(be.geon.moa.moamoa.config.log.Logging) && @annotation(logging)")
    public Object aroundLogger(ProceedingJoinPoint joinPoint, Logging logging) throws Throwable {
        RequestLog requestLog = new RequestLog();

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        requestLog.setUri(request.getRequestURI());

        String httpMethod = request.getMethod();

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = methodSignature.getName();

        logger.info(" ====== [{}]{} [{}.{}()] start ======", httpMethod, requestLog.getUri(), className, methodName);

        logMethodParameters(joinPoint, httpMethod, requestLog.getUri(), className, methodName);

        Object result = null;
        try {
            result = joinPoint.proceed();
            requestLog.setResult("success");

            if (logging.includeResponse() && result != null) {
                try {
                    String prettyJson = objectPrettyPrinter(result);

                    if (prettyJson.length() > 1500) {
                        int cutIndex = findAppropriateBreakPoint(prettyJson, 1500);
                        prettyJson = prettyJson.substring(0, cutIndex) + "\n... (truncated)";
                    }
                    logger.info(" ====== [{}]{} [{}.{}.Response] {} ======", httpMethod, requestLog.getUri(), className, methodName, prettyJson);
                } catch (Exception e) {
                    logger.warn(" Failed to format response for logging: {}", e.getMessage());
                }
            }




        } catch (Throwable t) {
            requestLog.setResult("fail");
            logger.error(" Exception occurred: {}", t.getMessage(), t);
            throw t;
        } finally {
            requestLog.setMethod(methodName);
            requestLog.setAction(logging.action());

            logger.info(" ====== [{}]{} [{}.{}()] end ======", httpMethod, requestLog.getUri(), className, methodName);
        }
        return result;
    }

    /**
     * 메서드 파라미터를 로깅합니다.
     *
     * @param joinPoint  AOP 조인 포인트
     * @param httpMethod
     * @param uri
     * @param className
     * @param methodName
     */
    private void logMethodParameters(ProceedingJoinPoint joinPoint, String httpMethod, String uri, String className, String methodName) {
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Parameter[] parameters = signature.getMethod().getParameters();
            Annotation[][] parameterAnnotations = signature.getMethod().getParameterAnnotations();
            Object[] args = joinPoint.getArgs();

            Map<String, Object> params = new HashMap<>();

            for (int i = 0; i < parameters.length; i++) {
                // 파일 업로드나 민감한 정보는 제외
                if (args[i] != null && !isMultipartFile(args[i].getClass().getName())) {
                    if (args[i] instanceof HttpServletRequest) {
                        continue;
                    }

                    if (args[i].getClass().getSimpleName().equals("Model") ||
                            args[i].getClass().getName().contains("org.springframework.ui.Model")) {
                        continue;
                    }

                    String paramName = parameters[i].getName();

                    // @RequestParam 어노테이션이 있으면 value 속성값을 사용
                    for (Annotation annotation : parameterAnnotations[i]) {
                        if (annotation.annotationType().getSimpleName().equals("RequestParam")) {
                            try {
                                String value = (String) annotation.annotationType().getMethod("value").invoke(annotation);
                                if (value != null && !value.isEmpty()) {
                                    paramName = value;
                                }
                            } catch (Exception ignored) {
                                // 예외 무시
                            }
                        }
                    }

                    params.put(paramName, args[i]);
                }
            }

            try {
                for (Object arg : args) {
                    if (arg instanceof HttpServletRequest) {
                        HttpServletRequest request = (HttpServletRequest) arg;
                        Map<String, String[]> requestParams = request.getParameterMap();

                        for (Map.Entry<String, String[]> entry : requestParams.entrySet()) {
                            String key = entry.getKey();
                            String[] values = entry.getValue();

                            if (values.length == 1) {
                                params.put(key, values[0]);
                            } else {
                                params.put(key, values);
                            }
                        }
                        break;
                    }
                }
            } catch (Exception e) {
                logger.warn(" Failed to extract parameters from HttpServletRequest: {}", e.getMessage());
                e.printStackTrace();
            }

            if (!params.isEmpty()) {
                try {
                    String prettyJson = objectPrettyPrinter(params);
                    logger.info("[{}]{} [{}.{}.Request Parameter] {}", httpMethod, uri, className, methodName, prettyJson);
                } catch (Exception e) {
                    logger.info(" Parameters: {}", params);
                }
            } else {
                logger.info(" No parameters or all parameters excluded from logging");
            }
        } catch (Exception e) {
            logger.warn(" Failed to log method parameters: {}", e.getMessage());
        }
    }

    private boolean isMultipartFile(String className) {
        return className.contains("MultipartFile");
    }

    private int findAppropriateBreakPoint(String text, int maxLength) {
        if (text.length() <= maxLength) {
            return text.length();
        }

        int breakPoint = maxLength;

        int newlineIndex = text.lastIndexOf('\n', maxLength);
        if (newlineIndex > 0) {
            breakPoint = newlineIndex + 1; // 줄바꿈 포함
        } else {
            int commaIndex = text.lastIndexOf(',', maxLength);
            if (commaIndex > 0) {
                breakPoint = commaIndex + 1; // 쉼표 포함
            } else {
                int bracketIndex = text.lastIndexOf('}', maxLength);
                if (bracketIndex > 0) {
                    breakPoint = bracketIndex + 1; // 중괄호 포함
                }
            }
        }

        return breakPoint;
    }
}