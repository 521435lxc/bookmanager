<template>
  <div>
    <div class="search">
      <el-input placeholder="请输入教材名称" style="width: 200px"
                v-model="textbookName">
      </el-input>

      <el-button type="info" plain style="margin-left: 10px" @click="load(1)">查询</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>
    </div>

    <div class="operation">

      <el-button style="margin-left: 10px" v-if="user.roleName ==='ADMIN' " type="primary" plain @click="handleAdd">新增</el-button>
      <el-button v-if="user.roleName ==='ADMIN' " type="danger" plain @click="delBatch">批量删除</el-button>
      <el-button v-if="user.roleName ==='ADMIN' " type="success" plain @click="eport">导入书单</el-button>

      <el-button>
        <el-switch
            style="margin-left: 10px"
            v-model="available"
            active-text="未审批"
            @change="loadOrderStatus">
        </el-switch>
      </el-button>
    </div>

    <div class="table">
      <el-table :data="tableData" stripe  @selection-change="handleSelectionChange">
        <el-table-column type="selection"idth="55" align="center"></el-table-column>
        <el-table-column prop="orderFormId" label="序号" align="center" sortable></el-table-column>
        <el-table-column prop="isbn" label="ISBN号" show-overflow-tooltip align="center"></el-table-column>
        <el-table-column prop="textbookName" label="教材名称" show-overflow-tooltip align="center"></el-table-column>
        <el-table-column prop="applicationNumber" label="申请数量" show-overflow-tooltip align="center"></el-table-column>
        <el-table-column prop="departmentName" label="使用部门" align="center"></el-table-column>
        <el-table-column prop="applicationTime" label="申请时间" align="center"></el-table-column>
        <el-table-column prop="realName" label="申请人" align="center"></el-table-column>
        <el-table-column prop="applicationIllustration" label="申请说明" align="center"></el-table-column>
        <el-table-column width="180" label="操作" align="center">
          <template v-slot="scope">
<!--            未审批且为系主任-->
            <el-button v-if="scope.row.applicationStatus === '1' && user.roleName ==='MANAGER'"
                       type="primary" plain="true" size="small"
                       @click="approve(scope.row)" >审批</el-button>
<!--            系主任审批且为通过-->
            <el-button v-else-if="scope.row.applicationStatus === '2' && user.roleName ==='DEANSOFFICE' "
                       type="primary" plain="true" size="small"
                       @click="approve(scope.row)">审批</el-button>
            <span v-else-if="scope.row.applicationStatus === '2' && user.roleName ==='MANAGER'">已审批</span>
            <span v-else-if="scope.row.applicationStatus === '3' && user.roleName ==='DEANSOFFICE'">已通过</span>
            <span v-else-if="scope.row.applicationStatus === '4' && user.roleName ==='DEANSOFFICE'">未通过</span>
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
<!--      通过与否的弹窗-->
      <el-dialog title="审批" name="信息" :visible.sync="fromVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
        <el-form :model="orderForm" :rules="orderFormRules" ref="orderFormRef" label-width="100px" style="padding-right: 50px;">

          <el-form-item label="审批状态" prop="applicationStatus">
            <el-select v-model="orderForm.applicationStatus" style="width: 100%" placeholder="请选择">
              <el-option label="通过" value="3"></el-option>
              <el-option label="不通过" value="4"></el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="审批回复" prop="replyMessage">
           <el-input type="textarea" v-model="orderForm.replyMessage" placeholder="审批回复"></el-input>
          </el-form-item>
          <div style="text-align: center; margin-bottom: 20px; margin-left: 20px">
            <el-button type="primary" @click="saveOrderForm">保 存</el-button>
          </div>
        </el-form>
      </el-dialog>

    </div>



  </div>
</template>

<script>

export default {
  name: "Application",
  data() {
    return {
      available: false,
      fromVisible: false,
      tableData: [],  // 所有的数据
      pageNum: 1,   // 当前的页码
      pageSize: 10,  // 每页显示的个数
      departmentId: 0,
      total: 0,
      orderForm: {},
      orderFormRules: [{
        applicationStatus: [{
            required: true, message: '请选择', trigger: 'blur',
        }]
      }
      ],
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      orderFormDate: [], // 查询到的所有信息都存在
    }
  },
  created() {
    this.load(1)
    this.loadOrderForm()
  },
  methods: {
    loadOrderForm() {
      this.$request.get('/orderForm/selectAll').then(res =>{
        if(res.code === '200') {
          this.orderFormDate = res.data
        } else {
          this.$message.error(res,msg)
        }
      })
    },
    load(pageNum) {  // 分页查询
      if (pageNum) this.pageNum = pageNum

      this.$request.get('/orderForm/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          textbookName : this.textbookName,
          departmentId: this.user.departmentId,
          applicationStatus: this.applicationStatus,
          cancelStatus : '1',

        },
      }).then(res => {
        this.tableData = res.data?.list
        // 系主任和教务处要动态显示
        if (this.user.roleName === 'MANAGER'){
          // 如果是系主任  那就显示征订单状态
          this.tableData = this.tableData.map(item => {
            if(item.applicationStatus !== '1') {
              item.applicationStatus = '2'
            }
            return item
          })
        }else {
          // 再不济就是管理员
          this.tableData = this.tableData.filter(o =>
              o.applicationStatus === '2' || o.applicationStatus === '3' || o.applicationStatus === '4')
        }
        this.total = res.data?.total
      })
    },
    approve(row){
      // 把那行的东西给到弹框
      this.orderForm = JSON.parse(JSON.stringify(row))
      // 把审批状态和回复置空
      this.orderForm.applicationStatus = null
      this.orderForm.replyMessage = null
      this.fromVisible = true

    },
    saveOrderForm() {
      this.$request.post('/orderForm/approveOrder' , this.orderForm,{
        params: {
          roleId: this.user.roleId
        }
      },
     ).then(res => {
        if (res.code === '200'){
          this.$message.success('操作成功')
          this.orderForm = {}
          this.fromVisible = false
          this.load(1)
        }else {
          this.$message.error(res.msg)
        }
      })
    },
    loadOrderStatus() {
      // 审批与未审批
      if(this.available === true){
        this.applicationStatus = '1'
        this.load(1)
      }
      else {
        this.applicationStatus = null
        this.load(1)
      }
    },
    reset() {
      this.textbookName = null
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
