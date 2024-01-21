<template>
  <div class="container">
    <div style="width: 400px; padding: 30px; background-color: white; border-radius: 5px;">
      <div style="text-align: center; font-size: 20px; margin-bottom: 10px; color: #333">光华教材征订系统</div>

      <el-tabs v-model="activeName">
        <el-tab-pane label="账号密码登录" name="username">
          <el-form :model="usernameForm" :rules="usernameRules" ref="usernameformRef">
            <el-form-item prop="username">
              <el-input prefix-icon="el-icon-user" placeholder="请输入账号" v-model="usernameForm.username"></el-input>
            </el-form-item>
            <el-form-item prop="password">
              <el-input prefix-icon="el-icon-lock" placeholder="请输入密码" show-password  v-model="usernameForm.password"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button style="width: 100%; background-color: #333; border-color: #333; color: white" @click="login">登 录</el-button>
            </el-form-item>
            <div style="display: flex; align-items: center">
              <div style="flex: 1"></div>
              <div style="flex: 1; text-align: right">
                还没有账号？请 <a href="/register">注册</a>
              </div>
            </div>
          </el-form>
        </el-tab-pane>

        <el-tab-pane label="邮箱登录" name="email">
          <el-form :model="emailForm" :rules="emailRules" ref="emailformRef">
            <el-form-item prop="email">
              <el-input prefix-icon="el-icon-message" placeholder="请输入邮箱" v-model="emailForm.email"></el-input>
            </el-form-item>
            <el-form-item prop="code">
              <el-input style="width: 190px;" prefix-icon="el-icon-lock" placeholder="请输入验证码" show-password  v-model="emailForm.code"></el-input>
              <el-button style="margin-left: 20px;" type="primary" size="small"  @click="sendEmailCode()">获取验证码</el-button>
            </el-form-item>
            <el-form-item>
              <el-button style="width: 100%; background-color: #333; border-color: #333; color: white" @click="login">登 录</el-button>
            </el-form-item>
            <div style="display: flex; align-items: center">
              <div style="flex: 1"></div>
              <div style="flex: 1; text-align: right">
                还没有账号？请 <a href="/register">注册</a>
              </div>
            </div>
          </el-form>
        </el-tab-pane>
      </el-tabs>

    </div>
  </div>
</template>

<script>
export default {
  name: "Login",
  data() {
    return {
      activeName: 'username',
      usernameForm: {},
      emailForm: {},
      usernameRules: {
        username: [
          { required: true, message: '请输入账号', trigger: 'blur' },
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
        ]
      },
      emailRules: {
        email: [
          { required: true, message: '请输入邮箱', trigger: 'blur' },
          { pattern: /^\w+((.\w+)|(-\w+))@[A-Za-z0-9]+((.|-)[A-Za-z0-9]+).[A-Za-z0-9]+$/, message: '邮箱格式不正确', trigger: 'blur' }
        ],
        code: [
          { required: true, message: '请输入验证码', trigger: 'blur' },
        ]
      }
    }
  },
  created() {

  },
  methods: {
    login() { // 动态登录 邮箱 或者用户名
      let form = '';
      let userForm = {};
      let URL = '';
      if (this.activeName === 'username'){
        form = 'usernameformRef'
        userForm = this.usernameForm
        URL = '/login'
      }else if (this.activeName === 'email'){
        form = 'emailformRef'
        userForm = this.emailForm
        URL = '/loginEmail'
      }
      this.$refs[form].validate((valid) => {
        if (valid) {
          // 验证通过
          this.$request.post(URL,userForm ).then(res => {
            if (res.code === '200') {
              localStorage.setItem("xm-user", JSON.stringify(res.data))  // 存储用户数据
              this.$router.push('/')  // 跳转主页
              this.$message.success('登录成功')
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      })
    },
    // 用邮箱登录
    changeLoginMethod() {
      this.activenName = 'email'
    },
    sendEmailCode() {
      // 发送邮箱验证码
      this.$request.post("/email/" + this.emailForm.email).then(res => {
        if (res.code === '200') {
          this.$message.success("发送成功")
        } else {
          this.$message.error(res.msg)
        }
      })
    }
  }
}
</script>

<style scoped>
.container {
  height: 100vh;
  overflow: hidden;
  background-image: url("@/assets/imgs/bg.jpg");
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