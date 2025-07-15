package com.fourth.medical.medical.controller;

import com.fourth.medical.framework.response.ApiResult;
import com.fourth.medical.medical.dto.ChatMessageDto;
import com.fourth.medical.medical.service.ChatService;
import com.fourth.medical.medical.vo.ChatMessageVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * AI聊天 控制器
 *
 * @author fourth
 * @since 2025-07-12
 */
@Slf4j
@RestController
@Tag(name = "AI聊天")
@RequestMapping("/app/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    /**
     * 发送消息给AI
     *
     * @param dto
     * @param request
     * @return
     * @throws Exception
     */
    @Operation(summary = "发送消息给AI")
    @PostMapping("/send")
    public ApiResult<String> sendMessage(@Valid @RequestBody ChatMessageDto dto, HttpServletRequest request) {
        log.info("收到用户消息: {}", dto.getMessage());
        
        // 从请求中获取token
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        
        String aiResponse = chatService.processUserMessage(dto, token);
        log.info("AI回复: {}", aiResponse);
        
        return ApiResult.success(aiResponse);
    }

    /**
     * 获取聊天历史
     *
     * @param request
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取聊天历史")
    @GetMapping("/history")
    public ApiResult<List<ChatMessageVo>> getChatHistory(HttpServletRequest request) {
        log.info("获取聊天历史");
        
        // 从请求中获取token
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        
        List<ChatMessageVo> history = chatService.getChatHistory(token);
        return ApiResult.success(history);
    }

    /**
     * 清空聊天记录
     *
     * @param request
     * @return
     * @throws Exception
     */
    @Operation(summary = "清空聊天记录")
    @PostMapping("/clear")
    public ApiResult<Boolean> clearChatHistory(HttpServletRequest request) {
        log.info("清空聊天记录");
        
        // 从请求中获取token
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        
        boolean result = chatService.clearChatHistory(token);
        return ApiResult.success(result);
    }

    /**
     * 获取AI医生列表
     *
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取AI医生列表")
    @GetMapping("/doctors")
    public ApiResult<List<ChatMessageVo>> getAIDoctors() {
        log.info("获取AI医生列表");
        List<ChatMessageVo> doctors = chatService.getAIDoctors();
        return ApiResult.success(doctors);
    }
} 