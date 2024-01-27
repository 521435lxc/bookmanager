<template>
  <div>
<!--    头-->
    <div class="search">
      <el-input placeholder="请输入教材名称" style="width: 200px"
                v-model="queryCondition.textbookName">
      </el-input>

      <el-button type="info" plain style="margin-left: 10px" @click="load(1)">查询</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>
    </div>

    <div class="operation">

      <el-button style="margin-left: 10px" v-if="user.roleName ==='ADMIN' " type="primary" plain @click="facultyAdd">
        新增学院
      </el-button>
      <el-button style="margin-left: 10px" v-if="user.roleName ==='ADMIN' " type="success" plain @click="departmentAdd">
        新增部门
      </el-button>
    </div>
    <!--  表-->
    <div class="table">
      <el-row :gutter="10">
       <el-col :span="10">
         <el-table :data="facultyLoadPage" stripe  @selection-change="handleSelectionChange">
         <el-table-column type="selection"idth="45" align="center"></el-table-column>

         <el-table-column prop="facultyId" label="序号" show-overflow-tooltip align="center"></el-table-column>
         <el-table-column label="图片" align="center">
             <template v-slot="scope">
               <div style="display: flex; align-items: center">
                 <el-image style="width: 30px; height: 40px; border-radius: 50%" v-if="scope.row.facultyBadge"
                           :src="scope.row.facultyBadge" :preview-src-list="[scope.row.facultyBadge]"></el-image>
               </div>
             </template>
         </el-table-column>
         <el-table-column prop="facultyName" label="学院名称" show-overflow-tooltip align="center"></el-table-column>
         <el-table-column label="操作" width="180" align="center">
           <template v-slot="scope">
             <el-button plain type="success" @click="queryDepartment(scope.row)" size="mini">专业</el-button>
             <el-button plain type="primary" @click="facultyEdit(scope.row)" size="mini">编辑</el-button>
           </template>
         </el-table-column>
       </el-table>
         <div class="pagination">
           <el-pagination
               background
               @current-change="handleCurrentChange"
               :current-page="pageNum"
               :page-sizes="[1, 10, 20]"
               :page-size="pageSize"
               layout="total, prev, pager, next"
               :total="total">
           </el-pagination>
         </div>

       </el-col>
        <el-col :span="14">
          <el-table :data="departmentLoadPage" stripe  @selection-change="handleSelectionChange">
            <el-table-column type="selection"idth="55" align="center"></el-table-column>
            <el-table-column prop="departmentName" label="专业名称" show-overflow-tooltip align="center"></el-table-column>
            <el-table-column prop="departmentManagerName" label="系主任" show-overflow-tooltip align="center"></el-table-column>
            <el-table-column prop="facultyName" label="学院" show-overflow-tooltip align="center"></el-table-column>
            <el-table-column label="操作" width="180" align="center">
              <template v-slot="scope">
                <el-button v-if="user.roleName ==='ADMIN'" plain type="primary" @click="departmentEdit(scope.row)" size="mini">编辑</el-button>
                <el-button v-if="user.roleName ==='ADMIN'" plain type="danger" size="mini" @click=delDepartment(scope.row.departmentId)>删除</el-button>
                <el-button v-if="user.roleName ==='TEACHER'||user.roleName ==='MANAGER' " plain type="success" size="mini" @click="order(scope.row)">征订</el-button>
              </template>
            </el-table-column>
          </el-table>
          <div class="pagination">
            <el-pagination
                background
                @current-change="departmentCurrentChange"
                :current-page="departmentPageNum"
                :page-sizes="[1, 10, 20]"
                :page-size="departmentPageSize"
                layout="total, prev, pager, next"
                :total="departmentTotal">
            </el-pagination>
          </div>
        </el-col>
      </el-row>

    </div>

    <el-dialog title="" name="信息" :visible.sync="fromVisible1" width="40%" :close-on-click-modal="false" destroy-on-close>
      <el-form :model="facultyForm" :rules="facultyRules" ref="facultyFormRef" label-width="100px" style="padding-right: 50px;">
        <el-form-item label="图片">
          <div>
            <el-upload
                list-type="picture-card"
                :action="$baseUrl + '/files/upload'"
                :show-file-list="false"
                :on-success="handleAvatarSuccess"
            >
              <img v-if="facultyForm.facultyBadge" :src="facultyForm.facultyBadge" class="avatar" />
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
          </div>
        </el-form-item>
        <el-form-item label="学院名称" prop="facultyName">
          <el-input v-model="facultyForm.facultyName" placeholder="学院名称" ></el-input>
        </el-form-item>

        <div style="text-align: center; margin-bottom: 20px">
          <el-button type="primary" @click="saveFaculty">保 存</el-button>
        </div>
      </el-form>
    </el-dialog>

    <el-dialog title="" name="信息" :visible.sync="fromVisible2" width="40%" :close-on-click-modal="false" destroy-on-close>
      <el-form :model="departmentForm" :rules="departmentRules" ref="departmentFormRef" label-width="100px" style="padding-right: 50px;">

        <el-form-item label="部门名称" prop="departmentName">
          <el-input v-model="departmentForm.departmentName" placeholder="部门名称" ></el-input>
        </el-form-item>

        <el-form-item label="系主任" prop="departmentManagerName">
          <el-select clearable filterable v-model="departmentForm.departmentManagerName" placeholder="请选择角色" style="width: 100%">
            <!--   key唯一表示 label展示值 value 传递的实际值-->
            <el-option
                v-for="item in userData"
                :key="item.userId"
                :label="item.realName"
                :value="item.realName"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item  label="学院" prop="facultyId">
          <el-select filterable v-model="departmentForm.facultyId" placeholder="请选择学院" style="width: 100%">
            <!--            key唯一表示 label展示值 value 传递的实际值-->
            <el-option
                v-for="item in facultyData"
                :key="item.facultyName"
                :label="item.facultyName"
                :value="item.facultyId"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <div style="text-align: center; margin-bottom: 20px">
          <el-button type="primary" @click="saveDepartment">保 存</el-button>
        </div>
      </el-form>
    </el-dialog>

  </div>
</template>

<script>
export default {
  name: "Department",
  data() {
    return {
      available: false,
      facultyData: [],
      facultyLoadPage: [],
      departmentData: [],
      departmentLoadPage: [],

      // 学院的分页
      pageNum: 1,   // 当前的页码
      pageSize: 10,  // 每页显示的个数
      total: 0,
      // 系的分页
      departmentPageNum: 1,
      departmentPageSize: 10,
      departmentTotal: 0,
      // 选中的学院的id
      facultyId: 1,

      fromVisible1: false,
      fromVisible2: false,

      facultyForm: {},
      departmentForm: {},

      facultyRules: {

      },
      departmentRules: {

      },
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      userData: [],
      queryCondition: {
        textbookName:null ,
      },

    }
  },
  created() {
    this.loadFaculty()
    this.loadFacultyPage(1)
    this.loadDepartment()
    this.loadDepartmentPage(1)
    this.queryTeacher()
  },
  methods: {
    loadFaculty() {
      this.$request.get('/faculty/selectAll').then(res =>{
        if(res.code === '200') {
          this.facultyData = res.data
        } else {
          this.$message.error(res,msg)
        }
      })
    },
    loadDepartment() {
      this.$request.get('/department/selectAll').then(res =>{
        if(res.code === '200') {
          this.departmentData = res.data
        } else {
          this.$message.error(res,msg)
        }
      })
    },
    loadFacultyPage(pageNum) {  // 分页查询
      if (pageNum)
      this.pageNum = pageNum
      this.$request.get('/faculty/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
        },
      }).then(res => {
        this.facultyLoadPage = res.data?.list
        this.total = res.data?.total
      })
    },
    loadDepartmentPage(pageNum) {  // 分页查询
      if (pageNum)
        this.departmentPageNum = pageNum
      this.$request.get('/department/selectPage', {
        params: {
          pageNum: this.departmentPageNum,
          pageSize: this.departmentPageSize,
          facultyId: this.facultyId
        },
      }).then(res => {
        this.departmentLoadPage = res.data?.list
        this.departmentTotal = res.data?.total
      })
    },
    queryDepartment(row) {   // 查看该院的系
      this.facultyId = row.facultyId
      this.loadDepartmentPage(1)
    },
    // 查询没有省份的教师信息
    queryTeacher() {
      this.$request.get('/user/queryTeacher').then(res => {
        if (res.code === '200') {
          this.userData = res.data
        }else {
          this.$message.error(res,msg)
        }
      })
    },
    facultyAdd() {
      this.facultyForm = {}  // 新增数据的时候清空数据
      this.fromVisible1 = true   // 打开弹窗
    },
    departmentAdd() {
      this.departmentForm = {}  // 新增数据的时候清空数据
      this.fromVisible2 = true   // 打开弹窗
      this.queryTeacher() // 重新加载教师的数据
      // this.loadDepartmentPage(1)
    },
    facultyEdit(row) {
      this.facultyForm = JSON.parse(JSON.stringify(row))
      this.fromVisible1 = true;
    },
    departmentEdit(row) {
      this.departmentForm = JSON.parse(JSON.stringify(row))
      this.fromVisible2 = true;
      this.queryTeacher()
    },

    saveFaculty() {   // 保存按钮触发的逻辑  它会触发新增或者更新
      this.$refs['facultyFormRef'].validate((valid) => {
        if (valid) {
          this.$request({ // 是否存在id判断新增还是修改的关键
            url: this.facultyForm.facultyId ? '/faculty/updateFaculty' : '/faculty/addFaculty',
            method: this.facultyForm.facultyId ? 'PUT' : 'POST',
            data: this.facultyForm
          }).then(res => {
            if (res.code === '200') {  // 表示成功保存
              this.$message.success('保存成功')
              this.loadFacultyPage(1)
              this.fromVisible1 = false
            } else {
              this.$message.error(res.msg)  // 弹出错误的信息
            }
          })
        }
      })
    },
    saveDepartment() {   // 保存按钮触发的逻辑  它会触发新增或者更新
      this.$refs['departmentFormRef'].validate((valid) => {
        if (valid) {
          this.$request({ // 是否存在id判断新增还是修改的关键
            url: this.departmentForm.departmentId ? '/department/updateDepartment' : '/department/addDepartment',
            method: this.departmentForm.departmentId ? 'PUT' : 'POST',
            data: this.departmentForm
          }).then(res => {
            if (res.code === '200') {  // 表示成功保存
              this.$message.success('保存成功')
              this.loadDepartmentPage(1)
              this.fromVisible2 = false
            } else {
              this.$message.error(res.msg)  // 弹出错误的信息
            }
          })
        }
      })
    },
    delDepartment(departmentId) {   // 单个删除
      this.$confirm('您确定删除吗？', '确认删除', {type: "warning"}).then(response => {
        this.$request.delete('/department/deleteById/' + departmentId).then(res => {
          if (res.code === '200') {   // 表示操作成功
            this.$message.success('删除成功')
            this.loadDepartmentPage(1)
          } else {
            this.$message.error(res.msg)  // 弹出错误的信息
          }
        })
      }).catch(() => {

      })
    },
    handleSelectionChange(rows) {   // 当前选中的所有的行数据
      this.textbookIds = rows.map(v => v.textbookId)   //  [1,2]
    },
    delBatch() {   // 批量删除
      if (!this.textbookIds.length) {
        this.$message.warning('请选择数据')
        return
      }
      this.$confirm('您确定批量删除这些数据吗？', '确认删除', {type: "warning"}).then(response => {
        this.$request.delete('/textbook/deleteBatch', {data: this.textbookIds}).then(res => {
          if (res.code === '200') {   // 表示操作成功
            this.$message.success('删除成功')
            this.load(1)
          } else {
            this.$message.error(res.msg)  // 弹出错误的信息
          }
        })
      }).catch(() => {
      })
    },
    reset() {
      this.queryCondition.textbookName = null
      this.load(1)
    },
    handleCurrentChange(pageNum) {
      this.loadFacultyPage(pageNum)
    },
    departmentCurrentChange(pageNum){
      this.loadDepartmentPage(pageNum)
    }
  }
}
</script>

<style scoped>

</style>
