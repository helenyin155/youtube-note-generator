package com.aminoacids.youtubenotes.services;


import com.aminoacids.youtubenotes.dao.BotRequest;
import com.aminoacids.youtubenotes.dao.BotSummaryRequest;
import com.aminoacids.youtubenotes.dao.BotSummaryResponse;
import com.aminoacids.youtubenotes.model.ChatGptConfig;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OpenAiService {
    private static RestTemplate restTemplate = new RestTemplate();
    //    Build headers
    public HttpEntity<BotSummaryRequest> buildHttpEntity(BotSummaryRequest chatRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(ChatGptConfig.MEDIA_TYPE));
        headers.add(ChatGptConfig.AUTHORIZATION, ChatGptConfig.BEARER + ChatGptConfig.API_KEY);
        return new HttpEntity<>(chatRequest, headers);
    }

    //    Generate response
    public BotSummaryResponse getResponse(HttpEntity<BotSummaryRequest> chatRequestHttpEntity) {
        ResponseEntity<BotSummaryResponse> responseEntity = restTemplate.postForEntity(
                ChatGptConfig.URL,
                chatRequestHttpEntity,
                BotSummaryResponse.class);

        return responseEntity.getBody();
    }

    public BotSummaryResponse createSummary(BotRequest botRequest) {
        BotSummaryResponse completion =  this.getResponse(
                this.buildHttpEntity(
                        new BotSummaryRequest(
                                ChatGptConfig.MODEL,
                                botRequest.getMessage(),
                                ChatGptConfig.TEMPERATURE,
                                ChatGptConfig.MAX_TOKEN,
                                ChatGptConfig.TOP_P)));

        return completion;
    }
}

