import request from '@/utils/request'

// 获取所有的进展报告
export function getAllProgressReportData(params) {
  return request({
    url: 'api/bigchanges/getAllData',
    method: 'get',
    params: params
  })
}

// 增加进展报告
export function addProgressReport(data) {
  return request({
    url: 'api/bigchanges/addData',
    method: 'post',
    data: data
  })
}

// 更新进展报告 updateData
export function updateProgressReport(data) {
  return request({
    url: 'api/bigchanges/updateData',
    method: 'post',
    data: data
  })
}

// 获取所有的续报数据
export function getAllSubProgress(params) {
  return request({
    url: 'api/bigchanges/getAllSubData',
    method: 'get',
    params: params
  })
}

// 增加续报
export function addSubProgress(data) {
  return request({
    url: 'api/bigchanges/addSubData',
    method: 'post',
    data: data
  })
}

// 更新续报
export function updateSubProgress(data) {
  return request({
    url: 'api/bigchanges/updateSubData',
    method: 'post',
    data: data
  })
}

// 提交进展报告
export function submitProgressReport(params) {
  return request({
    url: 'api/bigchanges/submit',
    method: 'get',
    params: params
  })
}

// 撤销进展报告
export function withdrawProgressReport(params) {
  return request({
    url: 'api/bigchanges/withdraw',
    method: 'get',
    params: params
  })
}

// 删除进展报告
export function deleteProgressReport(params) {
  return request({
    url: 'api/bigchanges/deleteProgressReport',
    method: 'get',
    params: params
  })
}

// 删除续报
export function deleteSubProgressReport(params) {
  return request({
    url: 'api/bigchanges/deleteBigChanges',
    method: 'get',
    params: params
  })
}

// 获取所有的主风险类别
export function getAllRiskType() {
  return request({
    url: 'api/bigchanges/getAllRiskType',
    method: 'get'
  })
}

// 获取指定住风险类别下的所有子风险类别
export function getSubRiskType(params) {
  return request({
    url: 'api/bigchanges/getSubRiskType',
    method: 'get',
    params: params
  })
}
