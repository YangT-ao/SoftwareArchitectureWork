package me.zhengjie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.zhengjie.modules.system.service.dto.ProgressReport;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProgressReportMapper extends BaseMapper<ProgressReport> {
}
