<template>
  <div class="container">
    <div style="width: 400px; padding: 30px; background-color: white; border-radius: 5px;">
      <div style="text-align: center; font-size: 20px; margin-bottom: 20px; color: #333">欢迎注册</div>
      <el-form :model="form" :rules="rules" ref="formRef">
        <el-form-item prop="username">
          <el-input prefix-icon="el-icon-user" placeholder="请输入账号" v-model="form.username"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input prefix-icon="el-icon-lock" placeholder="请输入密码" show-password  v-model="form.password"></el-input>
        </el-form-item>
        <el-form-item prop="confirmPass">
          <el-input prefix-icon="el-icon-lock" placeholder="请确认密码" show-password  v-model="form.confirmPass"></el-input>
        </el-form-item>
        <el-form-item prop="roleName">
          <el-select v-model="form.roleName" placeholder="请选择角色" style="width: 100%">
            <el-option label="教师" value="TEACHER"></el-option>
            <el-option label="系主任" value="MANAGER"></el-option>
            <el-option label="教务处" value="DEANSOFFICE"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item prop="departmentName"
                      v-if="form.roleName ==='MANAGER' ">
<!--          这点需要用到native因为 这个元素应该没有点击事件-->
          <el-select  @click.native="selectDepartment" v-model="form.departmentName" placeholder="请选择系别" style="width: 100%">
<!--            key唯一表示 label展示值 value 传递的实际值-->
            <el-option
                v-for="item in departmentList"
                :key="item.departmentName"
                :label="item.departmentName"
                :value="item.departmentName"
            >
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button style="width: 100%; background-color: #333; border-color: #333; color: white" @click="register">注 册</el-button>
        </el-form-item>
        <div style="display: flex; align-items: center">
          <div style="flex: 1"></div>
          <div style="flex: 1; text-align: right">
            已有账号？请 <a href="/login">登录</a>
          </div>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script>
export default {
  name: "Register",
  data() {
    // 验证码校验
    const validatePassword = (rule, confirmPass, callback) => {
      if (confirmPass === '') {
        callback(new Error('请确认密码'))
      } else if (confirmPass !== this.form.password) {
        callback(new Error('两次输入的密码不一致'))
      } else {
        callback()
      }
    }
    return {
      departmentList: [],
      form: {},
      rules: {
        username: [
          { required: true, message: '请输入账号', trigger: 'blur' },
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
        ],
        confirmPass: [
          { validator: validatePassword, trigger: 'blur' }
        ],
        roleName: [
          { required: true, message: '请选择角色', trigger: 'blur' },
        ],
        departmentName: [
          { required: true, message: '请选择系别', trigger: 'blur'}
        ],
        departmentId:[]
      }
    }
  },
  created() {

  },
  methods: {
    register() {
      this.$refs['formRef'].validate((valid) => {
        if (valid) {
          // 验证通过
          this.$request.post('/register', this.form).then(res => {
            if (res.code === '200') {
              this.$router.push('/')  // 跳转登录页面
              this.$message.success('注册成功')
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      })
    },
    selectDepartment() {
        this.$request.get('/department/selectAll').then(res => {
          this.departmentList =  res.data
        })
    }
  }

}
</script>

<style scoped>
.container {
  height: 100vh;
  overflow: hidden;
  background-image: url("@/assets/imgs/bg1.jpg");
  background-size: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #666;
}
a {
  color: #2a60c9;
}
</style>