package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.ValidationMapper;
import com.example.service.ValidationService;
import org.springframework.stereotype.Service;
import com.example.entity.Validation;
/**
 * (Validation)表服务实现类
 *
 * @author makejava
 * @since 2024-01-18 19:09:33
 */
@Service("validationService")
public class ValidationServiceImpl extends ServiceImpl<ValidationMapper, Validation> implements ValidationService {

}
