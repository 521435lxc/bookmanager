package com.example.controller;

import com.example.common.Result;
import com.example.entity.Textbook;
import com.example.service.TextbookService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * (Textbook)表控制层
 *
 * @author makejava
 * @since 2024-01-19 21:40:43
 */
@RestController
@RequestMapping("/textbook")
public class TextbookController {
    /**
     * 服务对象
     */
    @Resource
    private TextbookService textbookService;


     //  查询全部
    @GetMapping("/selectAll")
    public Result selectAll(Textbook textbook){
        List<Textbook> textbookList = textbookService.selectAllTextbook(textbook);
        return Result.success(textbookList);
    }

    // 分页查询
    @GetMapping  ("/selectPage")
    public Result selectPage(Textbook textbook,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Textbook> page = textbookService.selectPage(textbook, pageNum, pageSize);
        return Result.success(page);
    }


    // 新增教材
    @PostMapping("/addTextbook")
    public Result addTextbook(@RequestBody Textbook textbook){
        return textbookService.addTextbook(textbook);
    }

    // 更新教材信息
    @PutMapping("/updateTextbook")
    public Result updateTextbook(@RequestBody Textbook textbook){
        return textbookService.updateTextbook(textbook);

    }

    // 根据id删除教材
    @DeleteMapping("/deleteById/{textbookId}")
    public Result deleteTextbook(@PathVariable Integer textbookId){
        return textbookService.deleteTextbook(textbookId);
    }

    // 批量删除教材根据id
    @DeleteMapping("/deleteBatch")
    public Result deleteBatchTextbook(@RequestBody List<Integer> textbookIds){
        return textbookService.deleteBatchTextbook(textbookIds);
    }
}

