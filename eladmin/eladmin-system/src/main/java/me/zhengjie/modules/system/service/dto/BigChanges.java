package me.zhengjie.modules.system.service.dto;

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
    Integer id;

    String eName;

    Integer eLevel;

    String riskName;

    Date time;

    Integer isAbroad;

    Integer isLawsuit;

    Integer rCategory;

    Integer money;

    String progress;

    String sDescription;

    Integer belongProgress;

    Integer rSubType;

}
