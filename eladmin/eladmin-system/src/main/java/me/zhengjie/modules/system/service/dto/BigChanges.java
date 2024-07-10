package me.zhengjie.modules.system.service.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Table;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "big_changes")
public class BigChanges {
    @TableId(value = "id",type = IdType.AUTO)
    Integer id;

    String eName;

    Integer eLevel;

    String riskName;

    Date time;

    Date subTime;

    Integer isAbroad;

    Integer isLawsuit;

    Integer rCategory;

    Integer money;

    String progress;

    String sDescription;

    @TableField(value = "belong_progress")
    Integer belongProgress;

    Integer rSubType;

}
