package com.fourth.medical.medical.service.impl;

import com.fourth.medical.medical.dto.ChatMessageDto;
import com.fourth.medical.medical.service.ChatService;
import com.fourth.medical.medical.vo.ChatMessageVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 聊天 服务实现类
 *
 * @author fourth
 * @since 2025-07-12
 */
@Slf4j
@Service
public class ChatServiceImpl implements ChatService {

    @Value("${kimi.api.key:}")
    private String kimiApiKey;

    @Value("${kimi.api.url:https://api.moonshot.cn/v1/chat/completions}")
    private String kimiApiUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String processUserMessage(ChatMessageDto dto, String token) {
        log.info("处理用户消息: {}", dto.getMessage());
        
        // 首先尝试调用Kimi API
        try {
            log.info("开始调用Kimi API...");
            String aiResponse = callKimiAPI(dto.getMessage());
            log.info("Kimi AI回复成功: {}", aiResponse);
            return aiResponse;
        } catch (Exception e) {
            log.error("调用Kimi API失败，错误详情: {}", e.getMessage(), e);
            // 只有在API调用完全失败时才使用备用回复
            log.info("使用备用回复机制");
            return generateFallbackResponse(dto.getMessage());
        }
    }

    @Override
    public List<ChatMessageVo> getChatHistory(String token) {
        log.info("获取聊天历史");
        // 这里应该从数据库获取真实的聊天历史
        return new ArrayList<>();
    }

    @Override
    public boolean clearChatHistory(String token) {
        log.info("清空聊天记录");
        // 这里应该清空数据库中的聊天记录
        return true;
    }

    @Override
    public List<ChatMessageVo> getAIDoctors() {
        log.info("获取AI医生列表");
        
        List<ChatMessageVo> doctors = new ArrayList<>();
        
        ChatMessageVo doctor1 = new ChatMessageVo();
        doctor1.setId(1L);
        doctor1.setDoctorName("Kimi AI医生助手");
        doctor1.setDoctorAvatar("/static/images/avatar1.jpg");
        doctors.add(doctor1);
        
        ChatMessageVo doctor2 = new ChatMessageVo();
        doctor2.setId(2L);
        doctor2.setDoctorName("王医生");
        doctor2.setDoctorAvatar("/static/images/avatar2.jpg");
        doctors.add(doctor2);
        
        return doctors;
    }

    /**
     * 调用Kimi API
     */
    private String callKimiAPI(String userMessage) {
        if (kimiApiKey == null || kimiApiKey.isEmpty()) {
            throw new RuntimeException("Kimi API Key未配置");
        }

        try {
            // 构建请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + kimiApiKey);

            // 构建请求体
            String requestBody = buildKimiRequestBody(userMessage);
            log.info("Kimi API请求体: {}", requestBody);

            // 创建请求实体
            HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

            // 发送请求
            ResponseEntity<String> response = restTemplate.exchange(
                kimiApiUrl,
                HttpMethod.POST,
                requestEntity,
                String.class
            );

            log.info("Kimi API响应状态: {}", response.getStatusCode());
            log.info("Kimi API响应体: {}", response.getBody());

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                // 解析Kimi API响应
                return parseKimiResponse(response.getBody());
            } else {
                throw new RuntimeException("Kimi API请求失败: " + response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("调用Kimi API异常: {}", e.getMessage(), e);
            throw new RuntimeException("调用Kimi API失败: " + e.getMessage());
        }
    }

    /**
     * 构建Kimi API请求体
     */
    private String buildKimiRequestBody(String userMessage) {
        try {
            // 构建医疗咨询的上下文
            String systemPrompt = "你是一位专业的AI医疗助手，专门为用户提供健康咨询和医疗建议。请根据用户的问题提供专业、准确、易懂的回答。注意：\n" +
                    "1. 提供一般性的健康建议和症状分析\n" +
                    "2. 对于严重症状，建议及时就医\n" +
                    "3. 回答要简洁明了，易于理解\n" +
                    "4. 不要提供具体的药物剂量建议\n" +
                    "5. 强调预防保健的重要性";

            // 使用ObjectMapper构建JSON，避免手动拼接导致的转义问题
            ObjectNode requestNode = objectMapper.createObjectNode();
            ArrayNode messagesNode = requestNode.putArray("messages");
            
            // 添加system消息
            ObjectNode systemMessage = messagesNode.addObject();
            systemMessage.put("role", "system");
            systemMessage.put("content", systemPrompt);
            
            // 添加user消息
            ObjectNode userMessageNode = messagesNode.addObject();
            userMessageNode.put("role", "user");
            userMessageNode.put("content", userMessage);
            
            // 添加其他参数
            requestNode.put("model", "moonshot-v1-8k");
            requestNode.put("stream", false);
            
            return objectMapper.writeValueAsString(requestNode);
        } catch (Exception e) {
            log.error("构建Kimi请求体失败: {}", e.getMessage(), e);
            throw new RuntimeException("构建请求体失败: " + e.getMessage());
        }
    }

    /**
     * 解析Kimi API响应
     */
    private String parseKimiResponse(String responseBody) {
        try {
            log.info("开始解析Kimi响应: {}", responseBody);
            
            // 使用Jackson解析JSON
            JsonNode rootNode = objectMapper.readTree(responseBody);
            
            // 检查是否有错误
            if (rootNode.has("error")) {
                String errorMessage = rootNode.get("error").get("message").asText();
                log.error("Kimi API返回错误: {}", errorMessage);
                return "抱歉，AI服务暂时不可用，请稍后再试。";
            }
            
            // 获取choices数组
            JsonNode choicesNode = rootNode.get("choices");
            if (choicesNode != null && choicesNode.isArray() && choicesNode.size() > 0) {
                JsonNode firstChoice = choicesNode.get(0);
                JsonNode messageNode = firstChoice.get("message");
                if (messageNode != null && messageNode.has("content")) {
                    String content = messageNode.get("content").asText();
                    log.info("成功解析Kimi响应内容: {}", content);
                    return content;
                }
            }
            
            // 如果无法解析，尝试简单的字符串匹配
            if (responseBody.contains("\"content\":")) {
                int startIndex = responseBody.indexOf("\"content\":") + 11;
                int endIndex = responseBody.indexOf("\"", startIndex);
                if (endIndex == -1) {
                    endIndex = responseBody.length() - 1;
                }
                String content = responseBody.substring(startIndex, endIndex);
                return content.replace("\\n", "\n").replace("\\\"", "\"");
            }
            
            log.warn("无法解析Kimi响应格式: {}", responseBody);
            return "抱歉，我暂时无法理解您的问题，请重新描述一下。";
        } catch (Exception e) {
            log.error("解析Kimi响应失败: {}", e.getMessage(), e);
            return "抱歉，处理您的请求时出现了问题，请稍后再试。";
        }
    }

    /**
     * 生成备用回复（当Kimi API不可用时）
     */
    private String generateFallbackResponse(String userMessage) {
        String message = userMessage.toLowerCase();
        
        if (message.contains("头痛") || message.contains("头疼")) {
            return "头痛可能由多种原因引起，如紧张性头痛、偏头痛、感冒等。建议您：\n" +
                   "1. 保持充足的休息和睡眠\n" +
                   "2. 避免长时间用眼\n" +
                   "3. 适当进行放松运动\n" +
                   "4. 如果疼痛持续或加重，建议及时就医检查";
        } else if (message.contains("感冒") || message.contains("发烧")) {
            return "感冒是常见的上呼吸道感染，建议您：\n" +
                   "1. 多休息，保持充足睡眠\n" +
                   "2. 多喝温水，保持水分\n" +
                   "3. 适当服用退烧药（如布洛芬）\n" +
                   "4. 如果症状严重或持续不退，请及时就医";
        } else if (message.contains("失眠") || message.contains("睡不着")) {
            return "失眠是常见的睡眠问题，建议您：\n" +
                   "1. 保持规律的作息时间\n" +
                   "2. 睡前避免使用电子设备\n" +
                   "3. 创造舒适的睡眠环境\n" +
                   "4. 可以尝试冥想或深呼吸放松\n" +
                   "5. 如果长期失眠，建议咨询专业医生";
        } else if (message.contains("减肥") || message.contains("瘦身")) {
            return "健康减肥需要科学的方法：\n" +
                   "1. 控制饮食，减少高热量食物\n" +
                   "2. 增加运动量，每周至少150分钟中等强度运动\n" +
                   "3. 保持充足睡眠\n" +
                   "4. 避免过度节食，保证营养均衡\n" +
                   "5. 建议在专业指导下进行";
        } else if (message.contains("血压") || message.contains("高血压")) {
            return "血压管理很重要：\n" +
                   "1. 定期监测血压\n" +
                   "2. 控制盐分摄入\n" +
                   "3. 保持适量运动\n" +
                   "4. 戒烟限酒\n" +
                   "5. 按医嘱服用降压药\n" +
                   "6. 定期复查";
        } else if (message.contains("糖尿病") || message.contains("血糖")) {
            return "糖尿病管理要点：\n" +
                   "1. 定期监测血糖\n" +
                   "2. 控制饮食，避免高糖食物\n" +
                   "3. 适量运动\n" +
                   "4. 按医嘱用药\n" +
                   "5. 定期检查并发症\n" +
                   "6. 保持良好的生活习惯";
        } else if (message.contains("谢谢") || message.contains("感谢")) {
            return "不客气！很高兴能帮助到您。如果您还有其他健康问题，随时可以咨询我。祝您身体健康！";
        } else if (message.contains("你好") || message.contains("您好")) {
            return "您好！我是您的AI医疗助手，很高兴为您服务。我可以为您提供健康咨询、症状分析、用药建议等服务。请问有什么可以帮助您的吗？";
        } else {
            return "感谢您的咨询。我理解您的问题，但为了确保给您最准确的建议，建议您：\n" +
                   "1. 详细描述症状\n" +
                   "2. 提供相关病史\n" +
                   "3. 如有必要，建议咨询专业医生\n" +
                   "4. 我可以为您提供一般性的健康建议，但不能替代专业医疗诊断";
        }
    }
} 