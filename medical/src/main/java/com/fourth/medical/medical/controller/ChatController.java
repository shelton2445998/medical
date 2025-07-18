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
 * 功能说明：
 * 本控制器为移动端用户提供AI聊天服务，支持用户与AI医生进行对话、获取聊天历史、
 * 清空聊天记录以及获取AI医生列表。主要用于App端智能问诊、健康咨询等场景。
 * 
 * 主要功能：
 * 1. 发送消息给AI - 用户向AI医生发送消息并获取回复
 * 2. 获取聊天历史 - 查询用户与AI的对话记录
 * 3. 清空聊天记录 - 清除用户的聊天历史
 * 4. 获取AI医生列表 - 获取可用的AI医生信息
 * 
 * 业务流程：
 * 1. 用户在App端发起聊天请求
 * 2. 控制器接收请求参数，调用服务层进行业务处理
 * 3. 服务层返回AI回复，控制器封装为统一响应格式
 * 4. 返回结果给前端展示
 * 
 * 权限控制：
 * - 仅限已登录用户访问（通过Token校验）
 * - 查询操作无需特殊权限
 * 
 * 数据安全：
 * - 仅返回当前用户的聊天记录，防止越权访问
 * - 日志记录所有聊天操作，便于审计
 * 
 * 扩展点：
 * - 可扩展AI医生类型、专业领域、回复模板等功能
 * - 支持多轮对话、上下文理解
 * 
 * @author fourth
 * @since 2025-07-12
 * @version 1.0
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
     * 功能说明：
     * 用户向AI医生发送消息，AI根据消息内容进行智能回复。
     * 主要用于App端智能问诊、健康咨询等场景。
     * 
     * @param dto 聊天消息数据传输对象
     * @param request HTTP请求对象，用于获取用户Token
     * @return ApiResult<String> AI回复内容
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
     * 功能说明：
     * 查询用户与AI的对话记录，便于用户查看历史对话内容。
     * 
     * @param request HTTP请求对象，用于获取用户Token
     * @return ApiResult<List<ChatMessageVo>> 聊天历史记录
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
     * 功能说明：
     * 清除用户的聊天历史记录，保护用户隐私。
     * 
     * @param request HTTP请求对象，用于获取用户Token
     * @return ApiResult<Boolean> 清空结果
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
     * 功能说明：
     * 获取可用的AI医生信息，便于用户选择不同的AI医生进行咨询。
     * 
     * @return ApiResult<List<ChatMessageVo>> AI医生列表
     */
    @Operation(summary = "获取AI医生列表")
    @GetMapping("/doctors")
    public ApiResult<List<ChatMessageVo>> getAIDoctors() {
        log.info("获取AI医生列表");
        List<ChatMessageVo> doctors = chatService.getAIDoctors();
        return ApiResult.success(doctors);
    }
} 