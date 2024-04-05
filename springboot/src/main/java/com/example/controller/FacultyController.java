package com.example.controller;

import com.example.common.Result;
import com.example.entity.Faculty;
import com.example.entity.Textbook;
import com.example.service.FacultyService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * (Faculty)表控制层
 *
 * @author makejava
 * @since 2024-01-21 13:29:11
 */
@RestController
@RequestMapping("/faculty")
public class FacultyController {
    /**
     * 服务对象
     */
    @Resource
    private FacultyService facultyService;

    // 查询学院信息
    @GetMapping("/selectAll")
    public Result selectAllFaculty() {
        return facultyService.selectAllFacutlty();
    }

    // 分页查询
    @GetMapping("/selectPage")
    public Result selectPage(Faculty faculty,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Faculty> page = facultyService.selectPage(faculty, pageNum, pageSize);
        return Result.success(page);
    }

    // 增加院的信息
    @PostMapping("/addFaculty")
    public Result addFaculty(@RequestBody Faculty faculty) {
        return facultyService.addFaculty(faculty);
    }


    // 编辑院的信息
    @PutMapping("/updateFaculty")
    public Result updateFaculty(@RequestBody Faculty faculty) {
        return facultyService.updateFaculty(faculty);
    }
}

