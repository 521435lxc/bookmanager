package com.example.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.Constants;
import com.example.common.Result;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Textbook;
import com.example.exception.CustomException;
import com.example.mapper.TextbookMapper;
import com.example.service.TextbookService;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * (Textbook)表服务实现类
 *
 * @author makejava
 * @since 2024-01-19 21:40:44
 */
@Service("textbookService")
public class TextbookServiceImpl extends ServiceImpl<TextbookMapper, Textbook> implements TextbookService {


    @Resource
    private TextbookMapper textbookMapper;
    @Resource
    private TextbookService textbookService;

    @Override
    public List<Textbook> selectAllTextbook(Textbook textbook) {

        List<Textbook> textbooks = textbookMapper.selectAll(textbook);
        return textbooks;
    }

    /**
     * 分页查询
     */
    @Override
    public PageInfo<Textbook> selectPage(Textbook textbook, Integer pageNum, Integer pageSize) {

        PageHelper.startPage(pageNum, pageSize);
        List<Textbook> list = textbookMapper.selectAll(textbook);
        return PageInfo.of(list);
    }

    // 添加教材
    @Override
    public Result addTextbook(Textbook textbook) {
        // 插入校验（isbn 书名 不能重复）
        LambdaQueryWrapper<Textbook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Textbook::getTextbookName,textbook.getTextbookName());
        queryWrapper.eq(Textbook::getIsbn, textbook.getIsbn());
        Textbook dbTextbook = textbookMapper.selectOne(queryWrapper);
        if (ObjectUtil.isEmpty(dbTextbook)){
//            // 默认教材的状态为可用
//            textbook.setOrderStatus(Constants.AVAILABLE); 不用了
            int insert = textbookMapper.insert(textbook);
            if (insert > 0){
                return Result.success();
            }
            else{
                // 保存失败
                throw  new CustomException(ResultCodeEnum.TEXTBOOK_EXIST);
            }
        }
        else {
            // 该教材已经存在
            throw  new CustomException(ResultCodeEnum.TEXTBOOK_EXIST);
        }

    }

    // 根据id删除
    @Override
    public Result deleteTextbook(Integer textbookId) {
        int i = textbookMapper.deleteById(textbookId);
        if (i > 0){
            // 删除成功
            return Result.success();
        }
        throw new CustomException(ResultCodeEnum.SYSTEM_ERROR);
    }

    // 根据id批量删除
    @Override
    public Result deleteBatchTextbook(List<Integer> textbookIds) {
        int i = textbookMapper.deleteBatchIds(textbookIds);

           if (i > 0){
               return Result.success();
           }
        throw new CustomException(ResultCodeEnum.SYSTEM_ERROR);
    }

    @Override
    public Result updateTextbook(Textbook textbook) {

        LambdaQueryWrapper<Textbook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Textbook::getTextbookId, textbook.getTextbookId());
        int update = textbookMapper.update(textbook,queryWrapper);
        if (update > 0){
            return Result.success();
        }
        throw new CustomException(ResultCodeEnum.SYSTEM_ERROR);
    }
}
