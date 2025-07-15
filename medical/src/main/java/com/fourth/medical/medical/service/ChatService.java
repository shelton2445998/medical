package com.fourth.medical.medical.service;

import com.fourth.medical.medical.dto.ChatMessageDto;
import com.fourth.medical.medical.vo.ChatMessageVo;

import java.util.List;

/**
 * 聊天 服务接口
 *
 * @author fourth
 * @since 2025-07-12
 */
public interface ChatService {

    /**
     * 处理用户消息
     *
     * @param dto
     * @param token
     * @return
     * @throws Exception
     */
    String processUserMessage(ChatMessageDto dto, String token);

    /**
     * 获取聊天历史
     *
     * @param token
     * @return
     * @throws Exception
     */
    List<ChatMessageVo> getChatHistory(String token);

    /**
     * 清空聊天记录
     *
     * @param token
     * @return
     * @throws Exception
     */
    boolean clearChatHistory(String token);

    /**
     * 获取AI医生列表
     *
     * @return
     * @throws Exception
     */
    List<ChatMessageVo> getAIDoctors();
} 