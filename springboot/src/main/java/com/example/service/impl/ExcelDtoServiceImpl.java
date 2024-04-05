package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.ExcelDto;
import com.example.mapper.ExcelDtoMapper;
import com.example.service.ExcelDtoService;
import org.springframework.stereotype.Service;

/**
 * (ExcelDto)表服务实现类
 *
 * @author makejava
 * @since 2024-03-20 20:50:31
 */
@Service("excelDtoService")
public class ExcelDtoServiceImpl extends ServiceImpl<ExcelDtoMapper, ExcelDto> implements ExcelDtoService {

}
