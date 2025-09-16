package com.ablecisi.ailovebacked.mapper;

import com.ablecisi.ailovebacked.pojo.entity.Message;
import com.ablecisi.ailovebacked.pojo.vo.MessageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * MessageMapper
 * AILoveBacked <br>
 * com.ablecisi.ailovebacked.mapper <br>
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/9/10
 * 星期三
 * 16:31
 **/
@Mapper
public interface MessageMapper {
    int insert(Message po);

    int updateEmotion(@Param("id") Long id,
                      @Param("emotion") String emotion,
                      @Param("confidence") Double confidence);

    List<MessageVO> pageByConversation(@Param("cid") Long conversationId,
                                       @Param("page") int page,
                                       @Param("size") int size,
                                       @Param("offset") int offset
    );

    long countByConversation(@Param("cid") Long conversationId);

    /**
     * 最近 N 条（按 id 倒序取 N 条，再在 Service 里正序）
     */
    List<MessageVO> latestN(@Param("cid") Long conversationId, @Param("limit") int limit);
}

