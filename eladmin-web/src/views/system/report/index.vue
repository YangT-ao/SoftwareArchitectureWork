<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <div v-if="searchOn">
        <!-- 搜索 -->
        <el-select v-model="query.reviewStatus" size="mini" clearable placeholder="审核状态" style="width: 120px;">
          <el-option label="编制" value="编制"></el-option>
          <el-option label="退回" value="退回"></el-option>
          <el-option label="撤回" value="撤回"></el-option>
        </el-select>
        <el-input v-model="query.thingName" size="mini" clearable placeholder="事件名称" style="width: 200px;" @keyup.enter.native="toQuery" />
        <el-input v-model="query.relatedName" size="mini" clearable placeholder="输入职能部门或机构名称" style="width: 200px;" @keyup.enter.native="toQuery" />
        <el-button size="mini" type="success" icon="el-icon-search" @click="toQuery">搜索</el-button>
        <el-button size="mini" type="warning" icon="el-icon-refresh-left" @click="resetQuery()">重置</el-button>
      </div>
      <div class="crud-opts">
        <span class="crud-opts-left">
          <!--左侧插槽-->
          <el-button
            v-permission="permission.add"
            class="filter-item"
            size="mini"
            type="primary"
            icon="el-icon-plus"
            @click="toAdd"
          >
            新增
          </el-button>
          <el-button
            v-permission="permission.edit"
            class="filter-item"
            size="mini"
            type="success"
            icon="el-icon-edit"
            :disabled="ids.length !== 1"
            @click="toEdit"
          >
            修改
          </el-button>
          <el-button
            slot="reference"
            v-permission="permission.del"
            class="filter-item"
            type="danger"
            icon="el-icon-delete"
            size="mini"
            :disabled="ids.length === 0"
            :loading="deleteLoading"
            @click="toDelete"
          >
            删除
          </el-button>
          <el-button
            :disabled="ids.length === 0"
            :loading="submitLoading"
            class="filter-item"
            size="mini"
            type="success"
            icon="el-icon-upload"
            @click="toSubmit"
          >提交</el-button>
          <el-button
            slot="reference"
            v-permission="permission.del"
            class="filter-item"
            type="danger"
            icon="el-icon-back"
            size="mini"
            :loading="rollbackLoading"
            :disabled="ids.length === 0"
            @click="toRollback"
          >
            撤回
          </el-button>
          <!--右侧-->
        </span>
        <el-button-group class="crud-opts-right">
          <el-button
            size="mini"
            plain
            type="info"
            icon="el-icon-search"
            @click="toggleSearch"
          />
          <el-button
            size="mini"
            icon="el-icon-refresh"
            @click="refresh"
          />
          <el-popover
            placement="bottom-end"
            width="150"
            trigger="click"
          >
            <el-button
              slot="reference"
              size="mini"
              icon="el-icon-s-grid"
            >
              <i
                class="fa fa-caret-down"
                aria-hidden="true"
              />
            </el-button>
            <el-checkbox
              v-model="allColumnsSelected"
            >
              全选
            </el-checkbox>
            <el-checkbox
              v-for="item in tableColumns"
              :key="item.property"
              v-model="item.visible"
            >
              {{ item.label }}
            </el-checkbox>
          </el-popover>
        </el-button-group>
      </div>
    </div>
    <!--表单渲染-->
    <el-dialog append-to-body :close-on-click-modal="false" :visible.sync="mainFormMeta.visible" :title="mainFormMeta.title" width="800px">
      <el-form ref="editForm" :inline="true" :model="form" :rules="rules" size="small" label-position="right" label-width="150px">
        <div>
          <el-form-item label="涉及企业名称" prop="relatedName">
            <el-input v-model="form.relatedName" style="width: 200px" />
          </el-form-item>
          <el-form-item label="事件名称" prop="thingName">
            <el-input v-model="form.thingName" style="width: 200px" />
          </el-form-item>
        </div>
        <div>
          <el-form-item label="审核状态" prop="reviewStatus">
            <el-select v-model="form.reviewStatus" size="small" placeholder="请选择审核状态" style="width: 200px">
              <el-option label="编制" value="编制"></el-option>
              <el-option label="撤回" value="撤回"></el-option>
              <el-option label="退回" value="退回"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="上报状态" prop="reportStatus">
            <el-select v-model="form.reportStatus" size="small" placeholder="请选择上报状态" style="width: 200px">
              <el-option label="待上报" value="待上报"></el-option>
              <el-option label="已上报" value="已上报"></el-option>
            </el-select>
          </el-form-item>
        </div>
        <div>
          <el-form-item label="事件发生时间" prop="time">
            <el-date-picker
              v-model="form.time"
              style="width: 200px"
              type="datetime"
              placeholder="选择日期时间">
            </el-date-picker>
          </el-form-item>
        </div>
        <div>
          <el-form-item label="处置进展情况" prop="progress">
            <el-input v-model="form.progress" style="width: 415px" type="textarea" :autosize="{ minRows: 3}"></el-input>
          </el-form-item>
        </div>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="text" @click="toCancel">取消</el-button>
        <el-button type="primary" @click="toConfirm('editForm')">确认</el-button>
      </div>
    </el-dialog>
    <el-dialog append-to-body :close-on-click-modal="false" :visible.sync="subFormMeta.visible" :title="subFormMeta.title" width="1100px">
      <el-form ref="subEditForm" :inline="true" :model="subForm" :rules="subFormRules" size="small" label-position="right" label-width="150px">
        <div>
          <el-form-item label="涉及企业名称" prop="eName">
            <el-input v-model="subForm.eName" style="width: 300px" />
          </el-form-item>
          <el-form-item label="涉及企业层级" prop="eLevel">
            <el-select v-model="subForm.eLevel" size="small" placeholder="涉及企业等级" style="width: 300px">
              <el-option label="第一级" :value="1" />
              <el-option label="第二级" :value="2" />
              <el-option label="第三级" :value="3" />
              <el-option label="第四级" :value="4" />
              <el-option label="第五级" :value="5" />
            </el-select>
          </el-form-item>
        </div>
        <div>
          <el-form-item label="风险事件名称" prop="riskName">
            <el-input v-model="subForm.riskName" type="text" size="small" placeholder="风险事件名称" style="width: 300px"/>
          </el-form-item>
          <el-form-item label="事件发生时间" prop="time">
            <el-date-picker
              v-model="subForm.time"
              style="width: 300px"
              type="datetime"
              placeholder="选择日期时间">
            </el-date-picker>
          </el-form-item>
        </div>
        <div>
          <el-form-item label="是否境外" prop="isAbroad" style="width: 450px">
            <el-radio-group v-model="subForm.isAbroad">
              <el-radio :label="0">境内</el-radio>
              <el-radio :label="1">境外</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="是否涉诉" prop="isLawsuit">
            <el-radio-group v-model="subForm.isLawsuit">
              <el-radio :label="1">涉诉</el-radio>
              <el-radio :label="0">不涉诉</el-radio>
            </el-radio-group>
          </el-form-item>
        </div>
        <div>
          <el-form-item label="风险类别" prop="rCategory">
            <el-select v-model="subForm.rCategory" size="small" placeholder="主风险类别" @change="riskTypeSelectChange" style="width: 200px; margin-right: 10px">
              <el-option v-for="item in riskOptions" :key="item.id" :label="item.rType" :value="item.id"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="" prop="rSubType">
            <el-select v-model="subForm.rSubType" size="small" placeholder="子风险类别" style="width: 200px;">
              <el-option v-for="item in subRiskOptions" :key="item.id" :label="item.rSubType" :value="item.id"></el-option>
            </el-select>
          </el-form-item>
        </div>
        <div>
          <el-form-item label="损失（风险）金额（万元）" prop="money">
            <el-input v-model="subForm.money" type="Number" style="width: 415px" />
          </el-form-item>
        </div>
        <div>
          <el-form-item label="处置进展情况" prop="progress">
            <el-input v-model="subForm.progress" style="width: 415px" type="textarea" :autosize="{ minRows: 3}"></el-input>
          </el-form-item>
        </div>
        <div>
          <el-form-item label="当前情况描述" prop="sDescription">
            <el-input v-model="subForm.sDescription" type="textarea" style="width: 415px" :autosize="{ minRows: 3}"></el-input>
          </el-form-item>
        </div>
      </el-form>
      <h3 style="margin-top: 40px">重大变化/重大进展续报</h3>
      <el-button
        :loading="resubmitLoading"
        class="filter-item"
        size="medium"
        type="success"
        icon="el-icon-upload"
        @click="toResubmit"
      >增加续报</el-button>
      <el-table
        ref="table"
        v-loading="subTableLoading"
        lazy
        :border="true"
        :data="subData"
        row-key="id"
        style="margin-top: 15px; text-align: center"
        :highlight-current-row="true"
      >
        <el-table-column prop="id" label="序号" />
        <el-table-column prop="eName" label="涉及企业名称(信用代码)" />
        <el-table-column prop="eLevel" label="涉及企业层级">
          <template v-slot="scope">
            <span v-if="scope.row.eLevel === 1">
              第一级
            </span>
            <span v-else-if="scope.row.eLevel === 2">
              第二级
            </span>
            <span v-else-if="scope.row.eLevel === 3">
              第三级
            </span>
            <span v-else-if="scope.row.eLevel === 4">
              第四级
            </span>
            <span v-else-if="scope.row.eLevel === 5">
              第五级
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="riskName" label="风险事件名称" />
        <el-table-column prop="rCategory" label="主风险类别">
          <template v-slot="scope">
            {{ riskTypeId2Name[scope.row.rCategory] }}
          </template>
        </el-table-column>
        <el-table-column prop="rSubType" label="子风险类别">
          <template v-slot="scope">
            {{ subRiskTypeId2Name[scope.row.rSubType] }}
          </template>
        </el-table-column>
        <el-table-column prop="time" label="事件发生时间" />
        <el-table-column prop="sDescription" label="当前情况描述" />
        <el-table-column prop="money" label="损失（风险）金额（万元）" />
        <el-table-column prop="subTime" label="续报时间" />
        <el-table-column prop="progress" label="处置进展情况" align="center">
          <template v-slot="scope">
            <el-tag v-if="scope.row.progress" type="info">{{ scope.row.progress }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="isAbroad" label="是否境外">
          <template v-slot="scope">
            <span v-if="scope.row.isAbroad === 1">
              境外
            </span>
            <span v-else-if="scope.row.isAbroad === 0">
              境内
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="isLawsuit" label="是否涉诉">
          <template v-slot="scope">
            <span v-if="scope.row.isLawsuit === 1">
              涉诉
            </span>
            <span v-else-if="scope.row.isLawsuit === 0">
              不涉诉
            </span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="130px" align="center" fixed="right">
          <template v-slot="scope">
            <el-button size="small" icon="el-icon-edit-outline" type="primary" @click="toSubEdit(scope.$index, scope.row)">修改</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        :page-size.sync="subPage.pageSize"
        :total="subPage.total"
        :current-page.sync="subPage.pageNum"
        style="margin-top: 8px;"
        layout="total, prev, pager, next, sizes"
        :page-sizes="[5, 10, 20, 30, 40, 50, 100]"
        @size-change="subSizeChangeHandler"
        @current-change="subPageChangeHandler"
      />
      <div slot="footer" class="dialog-footer">
        <el-button type="text" @click="toSubCancel">取消</el-button>
        <el-button type="primary" @click="toSubConfirm">确认</el-button>
      </div>
    </el-dialog>
    <!--表格渲染-->
    <el-table
      ref="table"
      v-loading="loading"
      lazy
      :data="data"
      row-key="id"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column prop="id" label="序号" width="50px" />
      <el-table-column prop="reviewStatus" label="审核状态" align="center">
        <template v-slot="scope">
          <el-tag v-if="scope.row.reviewStatus === '编制'" type="success">编制</el-tag>
          <el-tag v-else-if="scope.row.reviewStatus === '退回'" type="error">退回</el-tag>
          <el-tag v-else-if="scope.row.reviewStatus === '撤回'" type="warning">撤回</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="reportStatus" label="上报状态" align="center">
        <template v-slot="scope">
          <el-tag v-if="scope.row.reportStatus === '待上报'" type="info">待上报</el-tag>
          <el-tag v-else-if="scope.row.reportStatus === '已上报'" type="success">已上报</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="relatedName" label="涉及企业名称(信用代码)" />
      <el-table-column prop="thingName" label="事件名称" />
      <el-table-column prop="time" label="事件发生时间" />
      <el-table-column prop="progress" label="处置进展情况" />
      <el-table-column v-if="checkPer(['admin','menu:edit','menu:del'])" label="操作" width="130px" align="center" fixed="right">
        <template v-slot="scope">
          <el-button size="small" icon="el-icon-tickets" type="primary" @click="handleDetailClick(scope.$index, scope.row)">详情</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      :page-size.sync="page.pageSize"
      :total="page.total"
      :current-page.sync="page.pageNum"
      style="margin-top: 8px;"
      :page-sizes="[5, 10, 20, 30, 40, 50, 100]"
      layout="total, prev, pager, next, sizes"
      @size-change="sizeChangeHandler"
      @current-change="pageChangeHandler"
    />
  </div>
</template>

<script>
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import {
  addProgressReport, addSubProgress,
  deleteProgressReport,
  getAllProgressReportData,
  getAllRiskType,
  getAllSubProgress,
  getProgressReportDataById, submitProgressReport, updateProgressReport, updateSubProgress, withdrawProgressReport
} from '@/api/system/report'

// crud交由presenter持有
const defaultForm = {
  id: null,
  reviewStatus: '编制',
  reportStatus: '待上报',
  relatedName: '',
  thingName: '',
  time: '',
  progress: ''
}
const defaultSubForm = {
  eName: '',
  eLevel: 1,
  riskName: '',
  rCategory: '',
  rSubType: '',
  time: '',
  isAbroad: 0,
  isLawsuit: 1,
  money: 0,
  progress: '',
  subTime: '',
  sDescription: ''
}
export default {
  name: 'Report',
  data() {
    return {
      menus: [],
      permission: {
        add: ['admin', 'menu:add'],
        edit: ['admin', 'menu:edit'],
        del: ['admin', 'menu:del']
      },
      query: {
        reviewStatus: '',
        thingName: '',
        relatedName: ''
      },
      page: {
        pageSize: 5,
        total: 11,
        pageNum: 1
      },
      subPage: {
        pageSize: 5,
        total: 11,
        pageNum: 1
      },
      selections: [],
      loading: false,
      subTableLoading: false,
      deleteLoading: false,
      submitLoading: false,
      rollbackLoading: false,
      resubmitLoading: false,
      tableColumns: [],
      subEditOn: false,
      riskOptions: [
        {
          'id': 1,
          'rType': '战略风险',
          'subRisks': [
            {
              'belongType': 1,
              'id': 1,
              'rSubType': '宏观经济风险'
            },
            {
              'belongType': 1,
              'id': 2,
              'rSubType': '政策性风险'
            },
            {
              'belongType': 1,
              'id': 3,
              'rSubType': '领导力风险'
            }
          ]
        },
        {
          'id': 2,
          'rType': '运营风险',
          'subRisks': [
            {
              'belongType': 2,
              'id': 4,
              'rSubType': '生产风险'
            },
            {
              'belongType': 2,
              'id': 5,
              'rSubType': '财务风险'
            },
            {
              'belongType': 2,
              'id': 6,
              'rSubType': '安全风险'
            }
          ]
        }
      ],
      subRiskOptions: [

      ],
      riskMap: {
        1: []
      },
      riskTypeId2Name: {
        1: ''
      },
      subRiskTypeId2Name: {
        1: ''
      },
      data: [
        {
          id: 1,
          reviewStatus: '编制',
          reportStatus: '待上报',
          relatedName: '长沙市人民政府国有资产监督管理委员会',
          thingName: '风险事件名称',
          time: '2021-01-01',
          progress: '处置进展情况'
        }
      ],
      subData: [
        {
          id: 1,
          belongProgress: null,
          eName: '长沙市人民政府国有资产监督管理委员会',
          eLevel: 2,
          riskName: '2024年度财务报告数据不确定风险事件',
          rCategory: '战略风险',
          rSubType: '宏观经济风险',
          time: '2024-04-10 10:00:00',
          isAbroad: 0,
          isLawsuit: 1,
          money: 0,
          progress: '正在处理',
          subTime: '2024-05-21 15:54:55',
          sDescription: ''
        }
      ],
      rules: {
        relatedName: [
          { required: true, message: '请输入涉及企业名称', trigger: 'blur' }
        ],
        thingName: [
          { required: true, message: '请输入事件名称', trigger: 'blur' }
        ],
        reviewStatus: [
          { required: true, message: '请选择审核状态', trigger: 'blur' }
        ],
        reportStatus: [
          { required: true, message: '请选择上报状态', trigger: 'blur' }
        ],
        time: [
          { required: true, message: '请输入事件发生时间', trigger: 'blur' }
        ]
      },
      subFormRules: {
        eName: [
          { required: true, message: '请输入涉及企业名称', trigger: 'blur' }
        ],
        eLevel: [
          { required: true, message: '请选择涉及企业层级', trigger: 'blur' }
        ],
        riskName: [
          { required: true, message: '请输入风险事件名称', trigger: 'blur' }
        ],
        rCategory: [
          { required: true, message: '请输入主风险类别', trigger: 'blur' }
        ],
        rSubType: [
          { required: true, message: '请输入子风险类别', trigger: 'blur' }
        ],
        time: [
          { required: true, message: '请输入事件发生时间', trigger: 'blur' }
        ]

      },
      searchOn: true,
      ids: [],
      single: false,
      multiple: false,
      allColumnsSelected: false,
      subFormMeta: {
        visible: false,
        title: '新增重大变化/进展编制'
      },
      mainFormMeta: {
        visible: false,
        isEdit: false,
        title: '新增重大变化/进展续报'
      },
      form: {
        id: 1,
        reviewStatus: '编制',
        reportStatus: '待上报',
        relatedName: '长沙市人民政府国有资产监督管理委员会',
        thingName: '风险事件名称',
        time: '2021-01-01',
        progress: '处置进展情况'
      },
      subForm: {
        eName: '长沙市人民政府国有资产监督管理委员会',
        eLevel: 3,
        belongProgress: 1,
        riskName: '2024年度财务报告数据不确定风险事件',
        rCategory: '',
        rSubType: '',
        time: '2024-04-10 10:00:00',
        isAbroad: 1,
        isLawsuit: 1,
        money: 0,
        progress: '正在处理',
        subTime: '2024-05-21 15:54:55',
        sDescription: ''
      }
    }
  },
  created() {
    this.getRiskTypeData()
    this.getMainData()
  },
  methods: {
    riskTypeSelectChange(value) {
      this.subRiskOptions = this.riskMap[value]
      // console.log(this.subRiskOptions)
      // console.log(this.subForm)
      this.subForm.rSubType = null
    },
    getMainData() {
      this.loading = true
      const query = {}
      Object.keys(this.query).forEach(key => {
        query[key] = this.query[key]
      })
      query['pageSize'] = this.page.pageSize
      query['pageNum'] = this.page.pageNum
      // console.log(query)
      getAllProgressReportData(query).then(res => {
        if (res.code === 200) {
          this.data = res.data.records
          this.page.total = res.data.total
          this.loading = false
        }
      })
    },
    getSubData(id) {
      this.subTableLoading = true
      const query = {}
      query.id = id
      query.pageNum = this.subPage.pageNum
      query.pageSize = this.subPage.pageSize
      getAllSubProgress(query).then(res => {
        if (res.code === 200) {
          this.subData = res.data.records
          this.subPage.total = res.data.total
          this.subTableLoading = false
        }
      })
    },
    getRiskTypeData() {
      getAllRiskType().then(res => {
        if (res.code === 200) {
          this.riskOptions = res.data
          this.riskOptions.forEach(item => {
            this.riskMap[item.id] = item.subRisks
            this.riskTypeId2Name[item.id] = item.rType
            item.subRisks.forEach(item => {
              this.subRiskTypeId2Name[item.id] = item.rSubType
            })
          })
        }
      })
    },
    pageChangeHandler(e) {
      this.page.pageNum = e
      this.getMainData()
    },
    sizeChangeHandler(e) {
      this.page.pageSize = e
      this.page.pageNum = 1
      this.getMainData()
    },
    subPageChangeHandler(e) {
      this.subPage.pageNum = e
      this.getSubData(this.subForm.belongProgress)
    },
    subSizeChangeHandler(e) {
      this.subPage.pageSize = e
      this.subPage.pageNum = 1
      this.getSubData(this.subForm.belongProgress)
    },
    resetQuery() {
      Object.keys(this.query).forEach(key => {
        this.query[key] = null
      })
    },
    toQuery() {
      this.getMainData()
    },
    toEdit() {
      this.mainFormMeta.title = '修改重大变化/进展编制'
      this.mainFormMeta.visible = true
      this.mainFormMeta.isEdit = true
      if (this.ids.length !== 1) return
      getProgressReportDataById(this.ids[0]).then(res => {
        if (res.code === 200) {
          this.form = res.data
        }
      })
    },
    toDelete() {
      this.$confirm('是否确认删除编号为"' + this.ids + '"的数据项？', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteProgressReport(this.ids).then(res => {
          if (res.code === 200) {
            this.$message({
              type: 'success',
              message: '删除成功！'
            })
            this.page.pageNum = 1
            this.getMainData()
          } else {
            this.$message({
              type: 'error',
              message: '删除失败！'
            })
          }
        })
      })
    },
    toAdd() {
      this.mainFormMeta.title = '新增重大变化/进展编制'
      this.mainFormMeta.visible = true
      this.mainFormMeta.isEdit = false
      Object.keys(this.form).forEach(key => {
        this.form[key] = defaultForm[key]
      })
    },
    toSubmit() {
      this.$confirm('是否确认提交编号为"' + this.ids + '"的报告？', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        submitProgressReport(this.ids).then(res => {
          if (res.code === 200) {
            this.$message({
              type: 'success',
              message: '提交成功！'
            })
            this.getMainData()
          } else {
            this.$message({
              type: 'error',
              message: '提交失败！'
            })
          }
        })
      })
    },
    toRollback() {
      this.$confirm('是否确认撤销编号为"' + this.ids + '"的报告？', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        withdrawProgressReport(this.ids).then(res => {
          if (res.code === 200) {
            this.$message({
              type: 'success',
              message: '撤销成功！'
            })
            this.getMainData()
          } else {
            this.$message({
              type: 'error',
              message: '撤销失败！'
            })
          }
        })
      })
    },
    // 增加续报
    toResubmit() {
      if (this.subEditOn) {
        this.resetSubFormData()
        this.$message.success('请重新填写表单')
      } else {
        this.$refs['subEditForm'].validate(valid => {
          if (valid) {
            addSubProgress(this.subForm).then(res => {
              if (res.code === 200) {
                this.getSubData(this.subForm.belongProgress)
                this.$message.success('新增续报成功')
              } else {
                this.$message.error('新增续报失败')
              }
            })
          }
        })
      }
      this.subEditOn = false
    },
    toConfirm(editData) {
      this.$refs[editData].validate(valid => {
        if (valid) {
          if (this.mainFormMeta.isEdit) {
            updateProgressReport(this.form).then(res => {
              if (res.code === 200) {
                this.getMainData()
                this.$message({
                  type: 'success',
                  message: '修改报告成功！'
                })
              } else {
                this.$message({
                  type: 'error',
                  message: '修改报告失败！'
                })
              }
            })
          } else {
            addProgressReport(this.form).then(res => {
              if (res.code === 200) {
                this.getMainData()
                this.$message({
                  type: 'success',
                  message: '新增报告成功！'
                })
              } else {
                this.$message({
                  type: 'error',
                  message: '新增报告失败！'
                })
              }
            })
          }
          this.mainFormMeta.visible = false
        }
      })
    },
    toCancel() {
      this.mainFormMeta.visible = false
    },
    toSubEdit(index, row) {
      this.subEditOn = true
      // 更新子风险类别选项
      this.riskTypeSelectChange(row.rCategory)
      Object.keys(row).forEach(key => {
        this.subForm[key] = row[key]
      })
    },
    // 重置主表单数据  进展报告
    resetFormData() {
      Object.keys(defaultForm).forEach(key => {
        this.form[key] = defaultForm[key]
      })
    },
    // 重置子表单数据 进展续报
    resetSubFormData() {
      Object.keys(defaultSubForm).forEach(key => {
        this.subForm[key] = defaultSubForm[key]
      })
    },
    toSubCancel() {
      this.subFormMeta.visible = false
    },
    toSubConfirm() {
      this.subFormMeta.visible = false
      if (this.subEditOn) {
        this.$refs['subEditForm'].validate(valid => {
          if (valid) {
            updateSubProgress(this.subForm).then(res => {
              if (res.code === 200) {
                this.getSubData(this.subForm.belongProgress)
                this.$message.success('修改续报成功')
              } else {
                this.$message.error('修改续报失败')
              }
            })
          }
        })
      }
    },
    toggleSearch() {
      this.searchOn = !this.searchOn
    },
    refresh() {
      this.getMainData()
    },
    handleDetailClick(index, row) {
      this.subFormMeta.visible = true
      this.resetSubFormData()
      this.subForm.belongProgress = row.id
      this.getSubData(row.id)
    },

    // 处理选中事件
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id) // 将被选中的行对应的记录的id加入ids数组中
      this.single = selection.length !== 1
      this.multiple = !selection.length
    }
  }
}
</script>

<style lang="css" scoped>
  .crud-opts {
    padding: 4px 0;
    display: -webkit-flex;
    display: flex;
    align-items: center;
  }
  .crud-opts .crud-opts-right {
    margin-left: auto;
  }
  .crud-opts .crud-opts-right span {
    float: left;
  }
  /deep/ .el-table .el-table__header-wrapper th, .el-table .el-table__fixed-header-wrapper th {
    font-size: 13px;
    font-weight: bold;
  }
</style>

<style rel="stylesheet/scss" lang="scss" scoped>
 ::v-deep .el-input-number .el-input__inner {
    text-align: left;
  }
 ::v-deep .vue-treeselect__control,::v-deep .vue-treeselect__placeholder,::v-deep .vue-treeselect__single-value {
    height: 30px;
    line-height: 30px;
  }
</style>
