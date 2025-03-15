package be.geon.moa.moamoa.service;

import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;


/**
 * todo : 파이썬으로 크롤링 돌려서 Batch로 빼기
 */
@Service
@RequiredArgsConstructor
public class SummeryService {
    private final OpenAiChatModel openAiChatModel;

    public String webSummarizer(String url) {
        String webContent;
        try {
            Document doc = Jsoup.connect(url).get();
            webContent = doc.select("body").text();
            webContent = webContent.replaceAll("\\s+", " ").trim();

            if (webContent.isEmpty()) {
                return "웹 페이지에서 요약할 내용을 찾을 수 없습니다.";
            }
        } catch (IOException e) {
            return "URL에 접근하는 중 오류가 발생했습니다: " + e.getMessage();
        }

        int charCount = webContent.length();
        int wordCount = webContent.split("\\s+").length;
        double readingTimeMinutes = wordCount / 200.0;


        PromptTemplate promptTemplate = new PromptTemplate(
                "당신은 전문적인 웹 콘텐츠 요약가입니다. " +
                        "제공된 웹 페이지의 내용을 분석하여 다음과 같은 형식으로 요약해주세요:\n\n" +
                        "1. 간단 요약: 전체 내용을 3-5문장으로 요약\n" +
                        "2. 핵심 포인트: 글의 가장 중요한 요점 3-5개를 추출\n" +
                        "3. 주요 키워드: 내용에서 발견된 주요 키워드 나열\n" +
                        "4. 예제 코드: 웹 페이지 내용의 주제나 목적과 관련된 간단하고 명확한 Java 예제 코드\n" +
                        "5. 글자 수 및 읽기 시간: 전체 글자 수와 예상 읽기 시간(분 단위)\n\n" +
                        "요약은 객관적이고 정확해야 하며, 원문의 의도를 유지해야 합니다. " +
                        "예제 코드는 내용과 연관성이 있어야 하며, 주석으로 설명을 추가해주세요.\n\n" +
                        "웹 페이지 URL: {url}\n" +
                        "웹 페이지 내용: {content}\n" +
                        "글자 수: {charCount}\n" +
                        "예상 읽기 시간: {readingTime}분\n"
        );

        String prompt = String.valueOf(promptTemplate.create(Map.of(
                "url", url,
                "content", webContent,
                "charCount", charCount,
                "readingTime", String.format("%.2f", readingTimeMinutes)
        )));

        return openAiChatModel.call(prompt);
    }
}