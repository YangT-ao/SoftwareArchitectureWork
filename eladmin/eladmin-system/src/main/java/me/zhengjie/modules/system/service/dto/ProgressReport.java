package me.zhengjie.modules.system.service.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Table;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "progress_report")
public class ProgressReport {
    @TableId(value = "id",type = IdType.AUTO)
    Integer id;

    String reportStatus;

    String reviewStatus;

    String relatedName;

    String thingName;

    Date time;

    String progress;

}
