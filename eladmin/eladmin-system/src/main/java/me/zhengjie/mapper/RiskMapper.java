package me.zhengjie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.zhengjie.modules.system.service.dto.Risk;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RiskMapper extends BaseMapper<Risk> {
}
