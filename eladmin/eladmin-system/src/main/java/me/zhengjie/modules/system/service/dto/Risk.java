package me.zhengjie.modules.system.service.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Table;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "risk")
public class Risk {
    Integer id;

    String rType;

    @TableField(exist = false)
    List<SubRisk> subRisks;
}
