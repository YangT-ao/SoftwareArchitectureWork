package me.zhengjie.modules.system.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "sub_risk")
public class SubRisk {
    Integer id;

    String rSubType;

    Integer belongType;
}
