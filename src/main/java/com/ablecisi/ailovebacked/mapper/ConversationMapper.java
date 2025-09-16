package com.ablecisi.ailovebacked.mapper;

import com.ablecisi.ailovebacked.pojo.entity.Conversation;
import com.ablecisi.ailovebacked.pojo.vo.ConversationVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * ConversationMapper
 * AILoveBacked <br>
 * com.ablecisi.ailovebacked.mapper <br>
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/9/10
 * 星期三
 * 16:30
 **/
@Mapper
public interface ConversationMapper {
    int insert(Conversation po);

    int updateLast(@Param("id") Long id,
                   @Param("last") String last,
                   @Param("lastAt") LocalDateTime lastAt);

    Conversation selectById(@Param("id") Long id);

    List<ConversationVO> pageByUser(@Param("userId") Long userId,
                                    @Param("page") int page,
                                    @Param("size") int size,
                                    @Param("offset") int offset
    );

    long countByUser(@Param("userId") Long userId);
}
