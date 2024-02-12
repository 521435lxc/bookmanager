<template>
  <div>
    <div class="search">
        <el-input placeholder="请输入教材名称" style="width: 200px"
                  v-model="queryCondition.textbookName">
        </el-input>

      <el-button type="info" plain style="margin-left: 10px" @click="load(1)">查询</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>
    </div>

    <div class="operation">

      <el-button style="margin-left: 10px" v-if="user.roleName ==='ADMIN' " type="primary" plain @click="handleAdd">新增</el-button>
      <el-button v-if="user.roleName ==='ADMIN' " type="danger" plain @click="delBatch">批量删除</el-button>
      <el-upload  :headers="{token: user.token}"
                  :action= "$baseUrl+ '/textbook/importTextbook'"
                  :show-file-list="false"
                  :on-success="handleImport"
                  style="display: inline-block; margin-left: 10px;"
      >
        <el-button v-if="user.roleName ==='ADMIN' " type="success" plain>导入书单</el-button>
      </el-upload>



      <el-button style="margin-left: 10px" size="mini">
        <el-switch style="margin-left: 10px"
                   v-model="available"
                   active-text="可征订"
                   @change="loadOrderStatus">
      </el-switch>
      </el-button>
    </div>

    <div class="table">
      <el-table :data="tableData" stripe  @selection-change="handleSelectionChange">
        <el-table-column type="selection"idth="55" align="center"></el-table-column>
        <el-table-column prop="textbookId" label="序号" align="center" sortable></el-table-column>
        <el-table-column label="封面">
          <template v-slot="scope">
            <div style="display: flex; align-items: center">
              <el-image style="width: 30px; height: 40px; border-radius: 0" v-if="scope.row.bookCover"
                        :src="scope.row.bookCover" :preview-src-list="[scope.row.bookCover]"></el-image>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="isbn" label="ISBN号" show-overflow-tooltip align="center"></el-table-column>
        <el-table-column prop="textbookName" label="教材名称" show-overflow-tooltip align="center"></el-table-column>
        <el-table-column prop="categoryName" label="教材分类" show-overflow-tooltip align="center"></el-table-column>
        <el-table-column prop="press" label="出版社" align="center"></el-table-column>
        <el-table-column prop="author" label="作者" align="center"></el-table-column>
        <el-table-column prop="version" label="版本" align="center"></el-table-column>
        <el-table-column prop="orderStatus" label="状态" align="center">
          <template v-slot="{ row }">
            <span v-if="row.orderStatus === '1' ">可征订</span>
            <span v-else-if="row.orderStatus === '0' ">不可用</span>
            <span v-else>不可用</span>
          </template>
        </el-table-column>
        <el-table-column v-if="user.roleName ==='ADMIN'" label="操作" width="180" align="center">
          <template v-slot="scope">
            <el-button v-if="user.roleName ==='ADMIN'" plain type="primary" @click="handleEdit(scope.row)" size="mini">编辑</el-button>
            <el-button v-if="user.roleName ==='ADMIN'" plain type="danger" size="mini" @click=del(scope.row.textbookId)>删除</el-button>
            <el-button v-if="user.roleName ==='TEACHER'|| user.roleName ==='MANAGER' " plain type="success" size="mini" @click="order(scope.row)">征订</el-button>
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
         <el-form-item label="封面">
           <div>
             <el-upload
                 list-type="picture-card"
                 :action="$baseUrl + '/files/upload'"
                 :show-file-list="false"
                 :on-success="handleBookCoverSuccess"
             >
               <img style="width: 148px; height: 148px" v-if="form.bookCover" :src="form.bookCover" class="avatar" />
               <i v-else class="el-icon-plus avatar-uploader-icon"></i>
             </el-upload>
           </div>
         </el-form-item>
        <el-form-item label="书名" prop="textbookName">
          <el-input v-model="form.textbookName" placeholder="书名" ></el-input>
        </el-form-item>
        <el-form-item label="ISBN" prop="isbn">
          <el-input v-model="form.isbn" placeholder="ISBN"></el-input>
        </el-form-item>
        <el-form-item  label="类别" prop="categoryId">
          <el-select v-model="form.categoryId" placeholder="请选择种类" style="width: 100%">
            <!--            key唯一表示 label展示值 value 传递的实际值-->
            <el-option
                v-for="item in categoryData"
                :key="item.categoryName"
                :label="item.categoryName"
                :value="item.categoryId"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="出版社" prop="press">
          <el-input v-model="form.press" placeholder="出版社"></el-input>
        </el-form-item>
        <el-form-item label="作者" prop="author">
          <el-input v-model="form.author" placeholder="作者"></el-input>
        </el-form-item>
        <el-form-item label="版本" prop="version">
          <el-input v-model="form.version" placeholder="版本"></el-input>
        </el-form-item>

        <el-form-item label="征订" prop="orderStatus">

           <el-select v-model="form.orderStatus" style="width: 100%">
             <el-option label="可征订" value="1"></el-option>
             <el-option label="不可用" value="0"></el-option>
           </el-select>
        </el-form-item>

        <div style="text-align: center; margin-bottom: 20px">
          <el-button type="primary" @click="save">保 存</el-button>
        </div>
      </el-form>

    </el-dialog>

    <el-dialog title="征订单" name="征订" :visible.sync="orderVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
      <el-form :model="orderForm" :rules="orderRules" ref="orderFormRef" label-width="100px" style="padding-right: 50px;">
        <el-image style="margin-left: 100px; margin-bottom: 20px; width: 100px;height: 100px;"
                  :src="orderForm.bookCover" preview-src-list></el-image>

        <el-form-item label="书名" prop="textbookName">
            <el-input readonly v-model="orderForm.textbookName" placeholder="书名" ></el-input>
        </el-form-item>
        <el-form-item label="ISBN" prop="isbn">
          <el-input  readonly v-model="orderForm.isbn" placeholder="ISBN"></el-input>
        </el-form-item>
        <el-form-item label="申请数量" prop="applicationNumber">
          <el-input v-model="orderForm.applicationNumber" placeholder="申请数量"></el-input>
        </el-form-item>
        <el-form-item label="使用系别" prop="departmentId">

          <el-select  filterable v-model="orderForm.departmentId" placeholder="使用系别" style="width: 100%">
            <el-option
                v-for="item in departmentData"
                :key="item.departmentId"
                :label="item.departmentName"
                :value="item.departmentId"
            >
            </el-option>
          </el-select>
        </el-form-item>
        
        <el-form-item label="申请说明" prop="applicationIllustration">
          <el-input type="textarea" :row="2" v-model="orderForm.applicationIllustration" placeholder="申请说明"></el-input>
        </el-form-item>

        <div style="text-align: center; margin-bottom: 20px">
          <el-button type="primary" @click="saveOrder">保 存</el-button>
        </div>
      </el-form>
    </el-dialog>

  </div>
</template>

<script>
export default {
  name: "Textbook",
  data() {
    return {
      available: false,
      tableData: [],  // 所有的数据

      departmentData: [],
      categoryData: [], // 所用的分类信息
      pageNum: 1,   // 当前的页码
      pageSize: 10,  // 每页显示的个数
      total: 0,
      fromVisible: false,
      orderVisible: false,
      orderForm: {},
      form: {},
      rules: {
        isbn: [{
          required: true, message: '请输入ISBN号', trigger: 'blur',
        }],
        textbookName: [{
          required: true, message: '请输入教材名', trigger: 'blur',
        }],
        categoryId: [{
          required: true, message: '请选择种类', trigger: 'blur',
        }],
        press: [{
          required: true, message: '请输入出版社', trigger: 'blur'
        }],
        author: [{
          required: true, message: '请输入作者', trigger: 'blur'
        }],
        version: [{
          required: true, message: '请输入版本', trigger: 'blur'
        }],
        orderStatus: [{
          required: true, message: '请选择状态', trigger: 'blur'
        }]

      },
      orderRules:{
        applicationNumber: [
         {
          required: true,
           message: '请输入数量',
           trigger: 'blur'
        },{
          pattern: /^[1-9][0-9]*$/,
          message: '数量格式不对，请输入数字',
          trigger: 'blur'
        }
        ],
        departmentId: [{
          required: true,
          message: '请选择系别',
          trigger: 'blur'
        }]
      },
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),

      // 根据条件分页查询
      queryCondition: {
        textbookName:null ,
        orderStatus: null
      },

      textbookData: [], // 查询到的所有信息都存在
      textbookIds: [],
    }
  },
  created() {
    this.load(this.pageNum)
    this.loadTextbook()
    this.selectCategory()
    this.loadDepartment()
  },
  methods: {
    handleImport(res, file, fileList) {
      if (res.code === '200'){
        this.$message.success('导入成功')
        this.load(1)
      }else {
        this.$message.error(res.msg)
      }
    },
    loadTextbook() {
      this.$request.get('/textbook/selectAll').then(res =>{
        if(res.code === '200') {
          this.textbookData = res.data
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    loadDepartment() {
      this.$request.get('/department/selectAll').then(res =>{
        if(res.code === '200') {
          this.departmentData = res.data
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    load(pageNum) {  // 分页查询
      // if (!pageNum) this.pageNum = pageNum

      this.$request.get('/textbook/selectPage', {
        params: {
          pageNum: pageNum,
          pageSize: this.pageSize,
          textbookName: this.queryCondition.textbookName,
          orderStatus: this.queryCondition.orderStatus
        },
      }).then(res => {
        this.tableData = res.data?.list
        this.total = res.data?.total
      })
    },

    // 征订按钮的可用与不可用
    loadOrderStatus() {
      if(this.available === true){
        this.queryCondition.orderStatus = '1'
        this.load(1)
      }
      else {
        this.queryCondition.orderStatus = null
        this.load(1)
      }
    },
    handleAdd() {   // 新增教材数据数据
      this.form = {}  // 新增数据的时候清空数据
      this.fromVisible = true   // 打开弹窗
    },
    handleEdit(row) {   // 编辑教材数据
      this.form = JSON.parse(JSON.stringify(row))  // 给form对象赋值  注意要深拷贝数据
      this.fromVisible = true   // 打开弹窗
    },
    order(row) {
      // 发起征订
      if (row.orderStatus !== '1'){
        this.$message.error("暂不可征订")
      }else {
        // 打开一个弹窗
        this.orderForm = {}
        this.orderVisible = true
        this.orderForm.textbookName = row.textbookName
        this.orderForm.isbn = row.isbn
        this.orderForm.bookCover = row.bookCover
      }
    },
    save() {   // 保存按钮触发的逻辑  它会触发新增或者更新
      this.$refs['formRef'].validate((valid) => {
        if (valid) {
          this.$request({ // 是否存在id判断新增还是修改的关键
            url: this.form.textbookId ? '/textbook/updateTextbook' : '/textbook/addTextbook',
            method: this.form.textbookId ? 'PUT' : 'POST',
            data: this.form
          }).then(res => {
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
    saveOrder() { // 生成征订单
      this.$refs['orderFormRef'].validate((valid) => {
        if (valid) {
          this.$request.post("/orderForm/addOrderForm",this.orderForm).then(res =>{
            if (res.code === '200'){
              this.$message.success('征订成功')
              this.orderVisible = false
            }else {
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
            this.load(this.pageNum)
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
    delBatch() {   // 批量删除
      if (!this.textbookIds.length) {
        this.$message.warning('请选择数据')
        return
      }
      this.$confirm('您确定批量删除这些数据吗？', '确认删除', {type: "warning"}).then(response => {
        this.$request.delete('/textbook/deleteBatch', {data: this.textbookIds}).then(res => {
          if (res.code === '200') {   // 表示操作成功
            this.$message.success('删除成功')
            this.load(this.pageNum)
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
      this.pageNum = pageNum
      this.load(this.pageNum)

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
