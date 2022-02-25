package com.kong.yygh.cmn.service.Impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kong.yygh.cmn.listener.DictListener;
import com.kong.yygh.cmn.mapper.DictMapper;
import com.kong.yygh.cmn.service.DictService;
import com.kong.yygh.model.cmn.Dict;
import com.kong.yygh.vo.cmn.DictEeVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {
    @Override
    public List<Dict> findChlidData(Long id) {
        QueryWrapper<Dict> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("parent_id",id);
        List<Dict> dicts = baseMapper.selectList(queryWrapper);
        for (Dict d:dicts) {
            d.setHasChildren(this.isChildren(d.getId()));
        }
        return dicts;
    }

    @Override
    public void exportDictData(HttpServletResponse response) {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");

// 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("数据字典", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename="+ fileName + ".xlsx");

            List<Dict> dictList = baseMapper.selectList(null);
            List<DictEeVo> dictVoList = new ArrayList<>(dictList.size());
            for(Dict dict : dictList) {
                DictEeVo dictVo = new DictEeVo();
                BeanUtils.copyProperties(dict, dictVo, DictEeVo.class);
                dictVoList.add(dictVo);
            }

            EasyExcel.write(response.getOutputStream(), DictEeVo.class).sheet("数据字典").doWrite(dictVoList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void importDataDict(MultipartFile multipartFile) {
        try {
            System.out.println(multipartFile);
            EasyExcel.read(multipartFile.getInputStream(),DictEeVo.class,new DictListener(baseMapper)).sheet().doRead();
        }catch (IOException e){

        }
    }

    @Override
    public String getDictName(String dictCode, String value) {
        Dict dict=null;
        dictCode= dictCode.replace("$","");
        value=value.replace("$","");
        if (StringUtils.isEmpty(dictCode)){
            QueryWrapper<Dict> wrapper=new QueryWrapper<>();
            wrapper.eq("value",value);
            dict = baseMapper.selectOne(wrapper);
        }else {
            Dict codeDict = this.getDictByDictCode(dictCode);
            Long parent_id = codeDict.getId();
            dict = baseMapper.selectOne(new QueryWrapper<Dict>().eq("parent_id", parent_id)
                    .eq("value", value));

        }
        return dict.getName();
    }

    @Override
    public List<Dict> findByDictCode(String dictCode) {
        Dict dictByDictCode = this.getDictByDictCode(dictCode);
        List<Dict> chlidData = this.findChlidData(dictByDictCode.getId());

        return chlidData;
    }

    private Dict getDictByDictCode(String dictCode){
        QueryWrapper<Dict> wrapper=new QueryWrapper<>();
        wrapper.eq("dict_code",dictCode);
        return baseMapper.selectOne(wrapper);
    }

    private boolean isChildren(Long id){
        QueryWrapper<Dict> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("parent_id",id);
        Integer integer = baseMapper.selectCount(queryWrapper);
        return integer>0;
    }
}
