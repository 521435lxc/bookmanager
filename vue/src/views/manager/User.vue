<template>
  <div>
    <div class="search">
      <el-input placeholder="请输入用户名" style="width: 200px"
                v-model="queryCondition.username">
      </el-input>

      <el-button type="info" plain style="margin-left: 10px" @click="load(1)">查询</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>
    </div>

    <div class="operation">

      <el-button style="margin-left: 10px" v-if="user.roleName ==='ADMIN' " type="primary" plain
                 size="default" @click="handleAdd">新增用户</el-button>
      <el-button style="height: 39.5px;" size="mini">
        <el-switch style="margin-left: 10px; font-size: 100px; rgb(17, 169, 131))"
                   v-model="available"
                   active-text="可用"
                   @change="loadOrderStatus">
       </el-switch>
      </el-button>
    </div>

    <div class="table">
      <el-table :data="userData" stripe  @selection-change="handleSelectionChange">
        <el-table-column type="selection"idth="55" align="center"></el-table-column>
        <el-table-column prop="userId" label="序号" align="center" sortable></el-table-column>
        <el-table-column label="头像">
          <template v-slot="scope">
            <div style="display: flex; align-items: center">
              <el-image style="width: 30px; height: 40px; border-radius: 0" v-if="scope.row.avatar"
                        :src="scope.row.avatar" :preview-src-list="[scope.row.avatar]"></el-image>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="username" label="用户名" show-overflow-tooltip align="center"></el-table-column>
        <el-table-column prop="realName" label="姓名" show-overflow-tooltip align="center"></el-table-column>
        <el-table-column prop="email" label="电子邮件" show-overflow-tooltip align="center"></el-table-column>
        <el-table-column prop="phoneNumber" label="电话" align="center"></el-table-column>
        <el-table-column prop="roleName" label="角色" align="center"></el-table-column>
        <el-table-column prop="registrationDate" label="注册时间" align="center"></el-table-column>
        <el-table-column prop="enableStatus" label="启用状态" align="center">
          <template v-slot="{ row }">
            <span v-if="row.enableStatus === '1' ">已启用</span>
            <span v-else-if="row.enableStatus === '0' ">未启用</span>
            <span v-else>未启用</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100" align="center">
          <template v-slot="scope">
            <el-button v-if="scope.row.enableStatus ==='0'" plain type="primary" @click="handleEdit(scope.row)" size="mini">启用</el-button>
            <el-button v-if="scope.row.enableStatus ==='1'" plain type="danger"  @click="handleEdit(scope.row)" size="mini">停用</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!--       分页-->
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
    </div>

    <el-dialog title="" name="信息" :visible.sync="fromVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px" style="padding-right: 50px;">
        <el-form-item label="头像">
          <div >
            <el-upload
                list-type="picture-card"
                :action="$baseUrl + '/files/upload'"
                :show-file-list="false"
                :on-success="handleBookCoverSuccess"
            >
              <img style="width: 148px; height: 148px" v-if="form.avatar" :src="form.avatar" class="avatar" />
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
          </div>
        </el-form-item>
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="用户名" ></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password"  placeholder="初始密码"></el-input>
        </el-form-item>
        <el-form-item  label="角色" prop="roleId">
          <el-select v-model="form.roleId" filterable="true" placeholder="请选择角色" style="width: 100%">
            <!-- key唯一表示 label展示值 value 传递的实际值-->
            <el-option label="管理员" value="1"></el-option>
            <el-option label="教师" value="2"></el-option>
            <el-option label="系主任" value="3"></el-option>
            <el-option label="教务处" value="4"></el-option>
          </el-select>
        </el-form-item>

        <div style="text-align: center; margin-bottom: 20px">
          <el-button type="primary" @click="save">保 存</el-button>
        </div>
      </el-form>
    </el-dialog>


  </div>
</template>

<script>
export default {
  name: "User",
  data() {
    return {
      available: false,
      tableData: [],  // 所有用户的数据
      categoryData: [], // 所用的分类信息
      pageNum: 1,   // 当前的页码
      pageSize: 10,  // 每页显示的个数
      total: 0,
      fromVisible: false,
      currentUser: {}, // 点击停用的用户
      form: {},
      rules: {
        username: [{
          required: true, message: '请输入用户名', trigger: 'blur',
        }],
        password: [{
          required: true, message: '请输入密码', trigger: 'blur',
        }],
        roleId: [{
          required: true, message: '请选择角色', trigger: 'blur',
        }]

      },
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),

      queryCondition: {
        username:null ,
        enableStatus: null
      },

      textbookData: [], // 查询到的所有信息都存在
      textbookIds: [],
    }
  },
  created() {
    this.load(1)
    this.loadUser()
    this.selectCategory()
  },
  methods: {
    loadUser() {
      this.$request.get('/user/selectAll').then(res =>{
        if(res.code === '200') {
          this.tableData = res.data
        } else {
          this.$message.error(res,msg)
        }
      })
    },
    load(pageNum) {  // 分页查询
      if (pageNum) this.pageNum = pageNum

      this.$request.get('/user/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          username: this.queryCondition.username,
          enableStatus: this.queryCondition.enableStatus
        },
      }).then(res => {
        this.userData = res.data?.list
        this.total = res.data?.total
      })
    },

    // 用户按钮的可用与不可用
    loadOrderStatus() {
      if(this.available === true){
        this.queryCondition.enableStatus = '1'
        this.load(1)
      }
      else {
        this.queryCondition.enableStatus = null
        this.load(1)
      }
    },
    handleAdd() {   // 新增教材数据数据
      this.form = {}  // 新增数据的时候清空数据
      this.fromVisible = true   // 打开弹窗

    },
    handleEdit(row) {   // 停用账号
      this.$confirm('你确定当前的操作吗', '确认操作', {type: "warning"}).then(response =>{
        this.currentUser = JSON.parse(JSON.stringify(row))  // 给这个用户停用或者启用

      if (this.currentUser.enableStatus === '1'){
        this.currentUser.enableStatus = '0'
      }else {
        this.currentUser.enableStatus = '1'
      }
     this.$request.put("/user/setStatus",this.currentUser).then(res => {
       if (res.code === '200'){
         this.$message.success('操作成功')
         this.load(1)
         location.reload()
       }
     })}).catch(()=>{

      })
    },
    order(row) {
      // 发起征订
      if (row.orderStatus !== '1'){
        this.$message.error("暂不可征订")
      }else {
        this.$message.success("可以征订")
      }


    },
    save() {   // 保存按钮触发的逻辑  它会触发新增或者更新
      this.$refs['formRef'].validate((valid) => {
        if (valid) {
          this.$request.post("/user/addUser",this.form).then(res => {
            if (res.code === '200') {  // 表示成功保存
              this.$message.success('保存成功')
              this.load(1)
              this.fromVisible = false
            } else {
              this.$message.error(res.msg)  // 弹出错误的信息
            }
          })
        }
      })
    },
    selectCategory(){ // 查询分类信息的方法 在点击编辑和新增的时候调用
      this.$request.get('/category/selectAll').then(res => {
        this.categoryData = res.data
      })
    },
    del(textbookId) {   // 单个删除
      this.$confirm('您确定删除吗？', '确认删除', {type: "warning"}).then(response => {
        this.$request.delete('/textbook/deleteById/' + textbookId).then(res => {
          if (res.code === '200') {   // 表示操作成功
            this.$message.success('删除成功')
            this.load(1)
          } else {
            this.$message.error(res.msg)  // 弹出错误的信息
          }
        })
      }).catch(() => {
        this.load(1)
      })
    },
    handleSelectionChange(rows) {   // 当前选中的所有的行数据
      this.textbookIds = rows.map(v => v.textbookId)   //  [1,2]
    },
    reset() {
      this.queryCondition.username = null
      this.load(1)
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
    // 上传文件的方法
    handleBookCoverSuccess(response, file, fileList) {
      // 把头像属性换成上传的图片的链接
      this.form.bookCover = response.data
    },

  }
}
</script>

<style scoped>

</style>
