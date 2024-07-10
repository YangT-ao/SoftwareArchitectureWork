package me.zhengjie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import me.zhengjie.mapper.ProgressReportMapper;
import me.zhengjie.mapper.RiskMapper;
import me.zhengjie.mapper.SubRiskMapper;
import me.zhengjie.modules.system.service.dto.BigChanges;
import me.zhengjie.mapper.BigChangesMapper;
import me.zhengjie.modules.system.service.dto.ProgressReport;
import me.zhengjie.modules.system.service.dto.Risk;
import me.zhengjie.modules.system.service.dto.SubRisk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bigchanges")
public class BigChangesController {

    @Autowired
    BigChangesMapper bigChangesMapper;

    @Autowired
    ProgressReportMapper progressReportMapper;

    @Autowired
    RiskMapper riskMapper;

    @Autowired
    SubRiskMapper subRiskMapper;

    @GetMapping("/getAllData")
    public Map<String, Object> getAllBigChanges(){
        Map<String, Object> map = new HashMap<>();
        map.put("code", 200);
        LambdaQueryWrapper<ProgressReport> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        List<ProgressReport> list = progressReportMapper.selectList(lambdaQueryWrapper);

        map.put("data", list);
        return map;
    }

    @GetMapping("/getAllSubData")
    public Map<String, Object> getAllSubData(@RequestParam Integer id){
        Map<String, Object> map = new HashMap<>();
        map.put("code", 200);
        LambdaQueryWrapper<BigChanges> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BigChanges::getBelongProgress, id);
        List<BigChanges> list = bigChangesMapper.selectList(lambdaQueryWrapper);
        map.put("data", list);
        return map;
    }

    @GetMapping("/updateData")
    public Map<String, Object> updateData(@RequestBody BigChanges bigChanges){
        Map<String, Object> map = new HashMap<>();
        LambdaQueryWrapper<BigChanges> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BigChanges::getId, bigChanges.getId());
        int result = bigChangesMapper.update(bigChanges, lambdaQueryWrapper);
        if(result == 1){
            map.put("code", 200);
            map.put("msg", "修改成功");
        }else{
            map.put("code", -1);
            map.put("msg", "修改失败");
        }
        return map;
    }

    @GetMapping("/submit")
    public Map<String, Object> submit(@RequestParam Integer id){
        Map<String, Object> map = new HashMap<>();
        LambdaUpdateWrapper<ProgressReport> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.set(ProgressReport::getReportStatus, "已上报").eq(ProgressReport::getId, id);
        int result = progressReportMapper.update(new ProgressReport(), lambdaUpdateWrapper);
        if(result == 1){
            map.put("code", 200);
            map.put("msg", "修改成功");
        }else{
            map.put("code", -1);
            map.put("msg", "修改失败");
        }
        return map;
    }

    @GetMapping("/withdraw")
    public Map<String, Object> withdraw(@RequestParam Integer id){
        Map<String, Object> map = new HashMap<>();
        LambdaUpdateWrapper<ProgressReport> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.set(ProgressReport::getReportStatus, "待上报").eq(ProgressReport::getId, id);
        int result = progressReportMapper.update(new ProgressReport(), lambdaUpdateWrapper);
        if(result == 1){
            map.put("code", 200);
            map.put("msg", "修改成功");
        }else{
            map.put("code", -1);
            map.put("msg", "修改失败");
        }
        return map;
    }

    @GetMapping("/deleteProgressReport")
    public Map<String, Object> deleteProgressReport(@RequestParam Integer id){
        Map<String, Object> map = new HashMap<>();
        LambdaQueryWrapper<ProgressReport> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ProgressReport::getId, id);
        int result1 = progressReportMapper.delete(lambdaQueryWrapper);
        LambdaQueryWrapper<BigChanges> lambdaQueryWrapper2 = new LambdaQueryWrapper<>();
        lambdaQueryWrapper2.eq(BigChanges::getBelongProgress, id);
        int result2 = bigChangesMapper.delete(lambdaQueryWrapper2);
        if(result1 == 1 && result2 > 0){
            map.put("code", 200);
            map.put("msg", "修改成功");
        }else{
            map.put("code", -1);
            map.put("msg", "修改失败");
        }
        return map;
    }

    @GetMapping("/deleteBigChanges")
    public Map<String, Object> deleteBigChanges(@RequestParam Integer id){
        Map<String, Object> map = new HashMap<>();
        LambdaQueryWrapper<BigChanges> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BigChanges::getId, id);
        int result = bigChangesMapper.delete(lambdaQueryWrapper);
        if(result == 1){
            map.put("code", 200);
            map.put("msg", "修改成功");
        }else{
            map.put("code", -1);
            map.put("msg", "修改失败");
        }
        return map;
    }

    @GetMapping("/getAllRiskType")
    public Map<String, Object> getAllRiskType(){
        Map<String, Object> map = new HashMap<>();
        List<Risk> list = riskMapper.selectList(null);
        for (Risk risk : list) {
            LambdaQueryWrapper<SubRisk> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(SubRisk::getBelongType, risk.getId());
            List<SubRisk> list2 = subRiskMapper.selectList(lambdaQueryWrapper);
            risk.setSubRisks(list2);
        }
        map.put("code", 200);
        map.put("data",list);
        return map;
    }

    @GetMapping("/getSubRiskType")
    public Map<String, Object> getSubAllRiskType(@RequestParam Integer id){
        Map<String, Object> map = new HashMap<>();
        LambdaQueryWrapper<SubRisk> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SubRisk::getBelongType, id);
        List<SubRisk> list = subRiskMapper.selectList(lambdaQueryWrapper);
        map.put("code", 200);
        map.put("data",list);
        return map;
    }

}
