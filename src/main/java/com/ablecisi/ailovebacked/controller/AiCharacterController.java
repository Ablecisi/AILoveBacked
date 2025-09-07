package com.ablecisi.ailovebacked.controller;

import com.ablecisi.ailovebacked.pojo.dto.AiCharacterCreateDTO;
import com.ablecisi.ailovebacked.pojo.dto.AiCharacterQueryDTO;
import com.ablecisi.ailovebacked.pojo.dto.AiCharacterUpdateDTO;
import com.ablecisi.ailovebacked.pojo.vo.AiCharacterVO;
import com.ablecisi.ailovebacked.result.PageResult;
import com.ablecisi.ailovebacked.result.Result;
import com.ablecisi.ailovebacked.service.AiCharacterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * AiCharacterController
 * AILoveBacked <br>
 * com.ablecisi.ailovebacked.controller <br>
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/9/7
 * 星期日
 * 14:23
 **/
@RestController
@RequestMapping("/api/character")
@RequiredArgsConstructor
@Slf4j
public class AiCharacterController {

    private final AiCharacterService service;

    /**
     * 创建角色
     *
     * @param dto 角色信息
     * @return 角色ID
     */
    @PostMapping("/create")
    public Result<Long> create(@RequestBody @Valid AiCharacterCreateDTO dto) {
        log.info("创建角色: {}", dto);
        Long id = service.create(dto);
        return Result.success("创建成功", id);
    }

    /**
     * 更新角色
     *
     * @param id  角色ID
     * @param dto 角色信息
     * @return 是否成功
     */
    @PutMapping("/update/{id}")
    public Result<Boolean> update(@PathVariable Long id, @RequestBody AiCharacterUpdateDTO dto) {
        log.info("更新角色: id={}, dto={}", id, dto);
        dto.setId(id);
        boolean ok = service.update(dto);
        return ok ? Result.success(true) : Result.error("更新失败", false);
    }

    /**
     * 删除角色
     *
     * @param id 角色ID
     * @return 是否成功
     */
    @DeleteMapping("/delete/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        log.info("删除角色: id={}", id);
        boolean ok = service.delete(id);
        return ok ? Result.success(true) : Result.error("删除失败", false);
    }

    /**
     * 角色详情
     *
     * @param id 角色ID
     * @return 角色信息
     */
    @GetMapping("/{id}")
    public Result<AiCharacterVO> detail(@PathVariable Long id) {
        log.info("查询角色详情: id={}", id);
        AiCharacterVO vo = service.detail(id);
        return vo != null ? Result.success(vo) : Result.error("未找到该角色", null);
    }

    /**
     * 角色列表
     *
     * @param userId  用户ID
     * @param typeId  角色类型ID
     * @param status  角色状态
     * @param keyword 关键词
     * @param page    页码
     * @param size    每页数量
     * @param orderBy 排序字段
     * @param order   排序方式
     * @return 角色列表
     */
    @GetMapping("/list")
    public Result<PageResult<AiCharacterVO>> list(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long typeId,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "update_time") String orderBy,
            @RequestParam(defaultValue = "DESC") String order
    ) {
        log.info("查询角色列表: userId={}, typeId={}, status={}, keyword={}, page={}, size={}, orderBy={}, order={}",
                userId, typeId, status, keyword, page, size, orderBy, order);
        AiCharacterQueryDTO q = new AiCharacterQueryDTO();
        q.setUserId(userId);
        q.setTypeId(typeId);
        q.setStatus(status);
        q.setKeyword(keyword);
        q.setPage(page);
        q.setSize(size);
        q.setOrderBy(orderBy);
        q.setOrder(order);
        q.setOffset(0);
        PageResult<AiCharacterVO> pr = service.page(q);
        return Result.success(pr);
    }

    /**
     * 设置角色在线状态
     *
     * @param id     角色ID
     * @param online 在线状态，1=在线，0=离线
     * @return 是否成功
     */
    @PostMapping("/{id}/online")
    public Result<Boolean> setOnline(@PathVariable Long id, @RequestParam Integer online) {
        log.info("设置角色在线状态: id={}, online={}", id, online);
        boolean ok = service.setOnline(id, online);
        return ok ? Result.success(true) : Result.error("设置在线状态失败", false);
    }

    /**
     * 设置角色启用状态
     *
     * @param id     角色ID
     * @param status 启用状态，1=启用，0=禁用
     * @return 是否成功
     */
    @PostMapping("/{id}/status")
    public Result<Boolean> setStatus(@PathVariable Long id, @RequestParam Integer status) {
        log.info("设置角色启用状态: id={}, status={}", id, status);
        boolean ok = service.setStatus(id, status);
        return ok ? Result.success(true) : Result.error("设置启用状态失败", false);
    }

    /**
     * 获取角色类型列表
     *
     * @return 角色类型列表
     */
    @GetMapping("/types")
    public Result<List<String>> getCharacterTypes() {
        log.info("获取角色类型列表");
        List<String> types = service.getCharacterTypes();
        return Result.success(types);
    }
}
