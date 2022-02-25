package com.kong.yygh.cmn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kong.yygh.model.cmn.Dict;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface DictService extends IService<Dict> {
    List<Dict> findChlidData(Long id);

    void exportDictData(HttpServletResponse httpServletResponse);

    void importDataDict(MultipartFile multipartFile);

    String getDictName(String dictCode, String value);

    List<Dict> findByDictCode(String dictCode);
}
