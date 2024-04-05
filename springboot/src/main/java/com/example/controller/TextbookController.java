package com.example.controller;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.example.common.Constants;
import com.example.common.Result;
import com.example.common.enums.ResultCodeEnum;
import com.example.entity.Textbook;
import com.example.entity.vo.TextbookVo;
import com.example.service.TextbookService;
import com.example.utils.BeanCopyUtils;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
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
                             @RequestParam Integer pageNum,
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

    // 导入书单
    @PostMapping("/importTextbook")
    public Result importTextbook(MultipartFile file) throws IOException {
        ExcelReader reader = ExcelUtil.getReader(file.getInputStream());
        List<TextbookVo> textbookVos = reader.readAll(TextbookVo.class);
        textbookVos.stream().forEach(
                t->t.setOrderStatus(Constants.AVAILABLE)
        );
        List<Textbook> textbookList = BeanCopyUtils.copyBeanList(textbookVos, Textbook.class);

        // 写入数据到数据库
        try {
            textbookService.saveBatch(textbookList);
        }catch (Exception e){
            e.printStackTrace();
            return Result.error(ResultCodeEnum.SYSTEM_ERROR);
        }
        return Result.success();
    }


}

