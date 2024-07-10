package me.zhengjie.modules.system.service.dto;

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
    Integer id;

    String reportStatus;

    String reviewStatus;

    String relatedName;

    String thingName;

    Date time;

    String progress;

}
