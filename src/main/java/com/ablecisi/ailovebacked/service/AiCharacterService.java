package com.ablecisi.ailovebacked.service;

import com.ablecisi.ailovebacked.pojo.dto.AiCharacterCreateDTO;
import com.ablecisi.ailovebacked.pojo.dto.AiCharacterQueryDTO;
import com.ablecisi.ailovebacked.pojo.dto.AiCharacterUpdateDTO;
import com.ablecisi.ailovebacked.pojo.vo.AiCharacterVO;
import com.ablecisi.ailovebacked.result.PageResult;

import java.util.List;

/**
 * AILoveBacked
 * com.ablecisi.ailovebacked.service <br>
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/9/7
 * 星期日
 * 14:18
 **/
public interface AiCharacterService {
    Long create(AiCharacterCreateDTO dto);

    boolean update(AiCharacterUpdateDTO dto);

    boolean delete(Long id);

    AiCharacterVO detail(Long id);

    PageResult<AiCharacterVO> page(AiCharacterQueryDTO q);

    boolean setOnline(Long id, Integer online);

    boolean setStatus(Long id, Integer status);

    List<String> getCharacterTypes();
}
