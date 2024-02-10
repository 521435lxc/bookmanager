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
          v-model="noCancel"
          active-text="未取消"
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
        <el-table-column prop="applicationStatus" label="审核状态" align="center">
          <template  v-slot="{ row }">
            <span v-show="row.applicationStatus === '1' || row.applicationStatus === '2'">待处理</span>
<!--            <span v-else-if="row.applicationStatus === '2'" >待教务处审批</span>-->
<!--            <span v-else-if="row.applicationStatus === '3'" >已通过</span>-->
<!--            <span v-else>未通过</span>-->
          </template>

        </el-table-column>

        <el-table-column width="180" label="操作" align="center">
          <template v-slot="scope">
           <el-button v-if="scope.row.cancelStatus === '1'" type="primary" plain="true" size="small"
                      @click="cancelOrder(scope.row)" >取消申请</el-button>
           <span v-else>已取消</span>
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



  </div>
</template>

<script>

export default {
  name: "ApplicationPending",
  data() {
    return {
      noCancel: false,
      tableData: [],  // 所有的数据
      pageNum: 1,   // 当前的页码
      pageSize: 10,  // 每页显示的个数
      total: 0,
      orderForm: {},
      form: {},
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      orderFormDate: [], // 查询到的所有信息都存在
      textbookName: null,
      cancelStatus: null,
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
          cancelStatus : this.cancelStatus,
          applicantId: this.user.userId
        },
      }).then(res => {
        this.tableData = res.data?.list
        this.total = res.data?.total
        // 过滤一下tableData // 不是通过或者不通过的  统一弄成未处理
        this.tableData = this.tableData.filter(item => item.applicationStatus === '1'
            || item.applicationStatus === '2' )
      })
    },
    loadOrderStatus(){
      if (this.noCancel === true){
        this.cancelStatus = '1'
        this.load(1)
      }else {
        this.cancelStatus = null
        this.load(1)
      }
    },
    cancelOrder(row) {
      // 取消征订单时改为2 已取消
      this.form = JSON.parse(JSON.stringify(row))
      this.form.cancelStatus = '2'
      // 向后端发起请求修改 征订单是否取消的状态
      this.$confirm('您确定取消吗？', '确认取消', {type: "warning"}).then(response => {
        this.$request.put('/orderForm/cancelStatus', this.form).then(res => {
          if (res.code === '200') {
            this.$message.success('修改成功')
            this.load(1)
          } else {
            this.$message.error(res.msg)  // 弹出错误的信息
          }
        })
      }).catch(() => {
        this.load(1)
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
