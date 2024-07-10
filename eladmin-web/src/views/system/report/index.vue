<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <div v-if="searchOn">
        <!-- 搜索 -->
        <el-select v-model="query.status" size="mini" placeholder="状态" style="width: 120px;">
          <el-option label="区域一" value="shanghai"></el-option>
          <el-option label="区域二" value="beijing"></el-option>
        </el-select>
        <el-input v-model="query.eventName" size="mini" clearable placeholder="事件名称" style="width: 200px;" @keyup.enter.native="toQuery" />
        <el-input v-model="query.companyName" size="mini" clearable placeholder="输入职能部门或机构名称" style="width: 200px;" @keyup.enter.native="toQuery" />
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
    <el-dialog :visible.sync="mainFormMeta.visible" :title="mainFormMeta.title" width="800px">
      <el-form ref="form" :inline="true" :model="form" :rules="rules" size="small" label-position="right" label-width="150px">
        <div>
          <el-form-item label="涉及企业名称" prop="companyName">
            <el-input v-model="form.companyName" style="width: 200px" />
          </el-form-item>
          <el-form-item label="事件名称" prop="eventName">
            <el-input v-model="form.eventName" style="width: 200px" />
          </el-form-item>
        </div>
        <div>
          <el-form-item label="审核状态" prop="checkStatus">
            <el-input v-model="form.checkStatus" style="width: 200px" />
          </el-form-item>
          <el-form-item label="上报状态" prop="reportStatus">
            <el-select v-model="form.reportStatus" size="small" placeholder="涉及企业等级" style="width: 200px">
              <el-option label="第一级" value="1"></el-option>
              <el-option label="第二级" value="2"></el-option>
            </el-select>
          </el-form-item>
        </div>
        <div>
          <el-form-item label="事件发生时间" prop="happenTime">
            <el-date-picker
              v-model="form.happenTime"
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
        <el-button type="primary" @click="toConfirm">确认</el-button>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="formMeta.visible" :title="formMeta.title" width="1100px">
      <el-form ref="form" :inline="true" :model="subForm" :rules="subFormRules" size="small" label-position="right" label-width="150px">
        <div>
          <el-form-item label="涉及企业名称" prop="companyName">
            <el-input v-model="subForm.companyName" style="width: 300px" />
          </el-form-item>
          <el-form-item label="涉及企业层级" prop="companyLevel">
            <el-select v-model="subForm.companyLevel" size="small" placeholder="涉及企业等级" style="width: 300px">
              <el-option label="第一级" value="1"></el-option>
              <el-option label="第二级" value="2"></el-option>
            </el-select>
          </el-form-item>
        </div>
        <div>
          <el-form-item label="风险事件名称" prop="eventName">
            <el-select v-model="subForm.eventName" size="small" placeholder="风险事件名称" style="width: 300px">
              <el-option label="某某事件1" value="某某事件1"></el-option>
              <el-option label="某某事件2" value="某某事件1"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="事件发生时间" prop="happenTime">
            <el-date-picker
              v-model="subForm.happenTime"
              style="width: 300px"
              type="datetime"
              placeholder="选择日期时间">
            </el-date-picker>
          </el-form-item>
        </div>
        <div>
          <el-form-item label="是否境外" prop="eventPlace" style="width: 450px">
            <el-radio-group v-model="subForm.eventPlace">
              <el-radio label="境内">境内</el-radio>
              <el-radio label="境外">境外</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="是否涉诉" prop="appeal">
            <el-radio-group v-model="subForm.appeal">
              <el-radio label="涉诉">涉诉</el-radio>
              <el-radio label="不涉诉">不涉诉</el-radio>
            </el-radio-group>
          </el-form-item>
        </div>
        <div>
          <el-form-item label="风险类别" prop="riskType1">
            <el-select v-model="subForm.riskType1" size="small" placeholder="风险类别1" style="width: 200px; margin-right: 10px">
              <el-option label="战略风险" value="某某事件1"></el-option>
              <el-option label="某某事件2" value="某某事件1"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="" prop="riskType2">
            <el-select v-model="subForm.riskType2" size="small" placeholder="风险类别2" style="width: 200px;">
              <el-option label="宏观经济风险" value="宏观经济风险"></el-option>
              <el-option label="某某事件2" value="某某事件2"></el-option>
            </el-select>
          </el-form-item>
        </div>
        <div>
          <el-form-item label="损失（风险）金额（万元）" prop="lossMoney">
            <el-input v-model="subForm.lossMoney" style="width: 415px" />
          </el-form-item>
        </div>
        <div>
          <el-form-item label="处置进展情况" prop="progress">
            <el-input v-model="subForm.progress" style="width: 415px" type="textarea" :autosize="{ minRows: 3}"></el-input>
          </el-form-item>
        </div>
        <div>
          <el-form-item label="当前情况描述" prop="currentSituation">
            <el-input v-model="subForm.currentSituation" type="textarea" style="width: 415px" :autosize="{ minRows: 3}"></el-input>
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
        style="margin-top: 15px"
        :highlight-current-row="true"
      >
        <el-table-column prop="id" label="序号" />
        <el-table-column prop="companyName" label="涉及企业名称(信用代码)" />
        <el-table-column prop="companyLevel" label="涉及企业层级" />
        <el-table-column prop="eventName" label="风险事件名称" />
        <el-table-column prop="riskType2" label="风险类别" />
        <el-table-column prop="happenTime" label="事件发生时间" />
        <el-table-column prop="currentSituation" label="当前情况描述" />
        <el-table-column prop="lossMoney" label="损失（风险）金额（万元）" />
        <el-table-column prop="resubmitTime" label="续报时间" />
        <el-table-column prop="progress" label="处置进展情况" align="center">
          <template slot-scope="scope">
            <el-tag type="info">正在处理</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="eventPlace" label="是否境外" />
        <el-table-column prop="appeal" label="是否涉诉" />
        <el-table-column label="操作" width="130px" align="center" fixed="right">
          <template slot-scope="scope">
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
      <el-table-column prop="id" label="序号" width="125px" />
      <el-table-column prop="checkStatus" label="审核状态" align="center">
        <template v-slot:default="scope">
          <el-tag type="success">编制</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="reportStatus" label="上报状态" align="center">
        <template slot-scope="scope">
          <el-tag type="success">待上报</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="companyName" label="涉及企业名称(信用代码)" />
      <el-table-column prop="eventName" label="事件名称" />
      <el-table-column prop="happenTime" label="事件发生时间" />
      <el-table-column prop="progress" label="处置进展情况" />
      <el-table-column v-if="checkPer(['admin','menu:edit','menu:del'])" label="操作" width="130px" align="center" fixed="right">
        <template slot-scope="scope">
          <el-button size="small" icon="el-icon-tickets" type="primary" @click="handleDetailClick(scope.$index, scope.row)">详情</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      :page-size.sync="page.pageSize"
      :total="page.total"
      :current-page.sync="page.pageNum"
      style="margin-top: 8px;"
      layout="total, prev, pager, next, sizes"
      @size-change="sizeChangeHandler"
      @current-change="pageChangeHandler"
    />
  </div>
</template>

<script>
import '@riophae/vue-treeselect/dist/vue-treeselect.css'

// crud交由presenter持有
// const defaultForm = { id: null, title: null, menuSort: 999, path: null, component: null, componentName: null, iFrame: false, roles: [], pid: 0, icon: null, cache: false, hidden: false, type: 0, permission: null }
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
        checkStatus: '',
        eventName: '',
        companyName: ''
      },
      page: {
        pageSize: 10,
        total: 11,
        pageNum: 1
      },
      subPage: {
        pageSize: 10,
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
      data: [
        {
          id: 1,
          checkStatus: '编制',
          reportStatus: '待上报',
          companyName: '长沙市人民政府国有资产监督管理委员会',
          eventName: '风险事件名称',
          happenTime: '2021-01-01',
          progress: '处置进展情况'
        }
      ],
      subData: [
        {
          id: 1,
          companyName: '长沙市人民政府国有资产监督管理委员会',
          companyLevel: '第一级',
          eventName: '2024年度财务报告数据不确定风险事件',
          riskType1: '战略风险',
          riskType2: '宏观经济风险',
          happenTime: '2024-04-10 10:00:00',
          eventPlace: '境内',
          appeal: '涉诉',
          lossMoney: 0,
          handleProgress: '正在处理',
          resubmitTime: '2024-05-21 15:54:55',
          currentSituation: ''
        }
      ],
      rules: {
        title: [
          { required: true, message: '请输入标题', trigger: 'blur' }
        ],
        path: [
          { required: true, message: '请输入地址', trigger: 'blur' }
        ]
      },
      subFormRules: {
        companyName: [
          { required: true, message: '请输入公司名称', trigger: 'blur' }
        ]
      },
      searchOn: true,
      ids: [],
      single: false,
      multiple: false,
      allColumnsSelected: false,
      formMeta: {
        visible: false,
        title: '新增重大变化/进展编制'
      },
      mainFormMeta: {
        visible: false,
        title: '新增重大变化/进展续报'
      },
      form: {
        id: 1,
        checkStatus: '编制',
        reportStatus: '待上报',
        companyName: '长沙市人民政府国有资产监督管理委员会',
        eventName: '风险事件名称',
        happenTime: '2021-01-01',
        progress: '处置进展情况'
      },
      subForm: {
        companyName: '长沙市人民政府国有资产监督管理委员会',
        companyLevel: '第一级',
        eventName: '2024年度财务报告数据不确定风险事件',
        riskType1: '战略风险',
        riskType2: '宏观经济风险',
        happenTime: '2024-04-10 10:00:00',
        eventPlace: '境内',
        appeal: '涉诉',
        lossMoney: 0,
        handleProgress: '正在处理',
        resubmitTime: '2024-05-21 15:54:55',
        currentSituation: ''
      }
    }
  },
  methods: {
    pageChangeHandler() {

    },
    sizeChangeHandler() {

    },
    subPageChangeHandler() {

    },
    subSizeChangeHandler() {

    },
    load_data() {

    },
    resetQuery() {
      Object.keys(this.query).forEach(key => {
        this.query[key] = null
      })
    },
    toQuery() {

    },
    toEdit() {

    },
    toDelete() {

    },
    toAdd() {
      this.mainFormMeta.visible = true
    },
    toSubmit() {

    },
    toRollback() {

    },
    toResubmit() {
      if (this.subEditOn) {
        Object.keys(this.subForm).forEach(key => {
          this.subForm[key] = null
        })
      } else {
        console.log('增加续报')
      }
      this.subEditOn = false
    },
    toConfirm() {
      this.mainFormMeta.visible = false
    },
    toCancel() {
      this.mainFormMeta.visible = false
    },
    toSubEdit(index, row) {
      this.subEditOn = true
      this.subForm.eventName = '公司'
    },
    toSubCancel() {
      this.formMeta.visible = false
    },
    toSubConfirm() {
      this.formMeta.visible = false
      if (this.subEditOn) {
        console.log('修改续报成功')
      }
    },
    toggleSearch() {
      this.searchOn = !this.searchOn
    },
    refresh() {
      this.load_data()
    },
    handleDetailClick(index, row) {
      this.formMeta.visible = true
      /* if (row !== undefined && row !== 'undefined') {
        this.formMeta.title = '编辑'
        this.form.id = row.id
      } else {
        this.formMeta.title = '添加'
        this.form.id = null
      } */
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
