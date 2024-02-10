<template>
  <div class="manager-container">
    <!--  头部  -->
    <div class="manager-header">
      <div class="manager-header-left">
        <img src="@/assets/imgs/logo.png" />
        <div class="title">光华教材征订系统</div>
      </div>

      <div class="manager-header-center">
        <el-breadcrumb separator-class="el-icon-arrow-right">
          <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
          <el-breadcrumb-item :to="{ path: $route.path }">{{ $route.meta.name }}</el-breadcrumb-item>
        </el-breadcrumb>
      </div>

      <div class="manager-header-right">
        <el-dropdown placement="bottom">
          <div class="avatar">
            <img :src="user.avatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'" />
            <div>{{ user.username ||  '管理员' }}</div>
          </div>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item @click.native="goToPerson">个人信息</el-dropdown-item>
            <el-dropdown-item @click.native="$router.push('/password')">修改密码</el-dropdown-item>
            <el-dropdown-item @click.native="logout">退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </div>

    <!--  主体  -->
    <div class="manager-main">
      <!--  侧边栏  -->
      <div class="manager-main-left">
        <el-menu  router style="border: none" :default-active="$route.path">
          <el-menu-item index="/home">
            <i class="el-icon-s-home"></i>
            <span slot="title">首页</span>
          </el-menu-item>

          <el-submenu index="info" >
            <template slot="title">
              <i class="el-icon-document-copy"></i>
              <span>教材信息</span>
            </template>
            <el-menu-item index="/textbookList">
              教材列表
            </el-menu-item>
          </el-submenu>

          <el-submenu v-if="user.roleName === 'ADMIN'" index="user">
            <template slot="title">
              <i class="el-icon-user"></i>
              <span>用户管理</span>
            </template>
            <el-menu-item index="/userList">
              用户列表
            </el-menu-item>
          </el-submenu>
<!--          管理员不允许看审批信息-->
          <el-submenu v-if=" user.roleName === 'TEACHER'|| user.roleName ==='DEANSOFFICE' || user.roleName ==='MANAGER'  "  index="approval">
            <template slot="title">
              <i class="el-icon-s-check"></i>
              <span>审批信息</span>
            </template>
            <el-menu-item v-if=" user.roleName ==='MANAGER' || user.roleName ==='DEANSOFFICE' " index="/applicationList">
              申请列表
            </el-menu-item>
            <el-menu-item v-if="user.roleName ==='MANAGER'|| user.roleName ==='TEACHER' " index="/applicationPending">
              申请待办
            </el-menu-item>
            <el-menu-item index="/applicationProcessed">
              申请已办
            </el-menu-item>
          </el-submenu>

          <el-submenu index="faculty">
            <template slot="title">
              <i class="el-icon-menu"></i>
              <span>院系信息</span>
            </template>
            <el-menu-item index="/department">
              学院信息
            </el-menu-item>
          </el-submenu>

          <el-submenu v-if="user.roleName === 'ADMIN'" index="notice">
            <template slot="title">
              <i class="el-icon-bell"></i>
              <span>公告信息</span>
            </template>
            <el-menu-item index="/noticeList">
              公告列表
            </el-menu-item>
          </el-submenu>

          <el-submenu v-if="user.roleName === 'MANAGER'|| user.roleName ==='DEANSOFFICE' " index="order">
            <template slot="title">
              <i class="el-icon-s-data"></i>
              <span>征订信息</span>
            </template>
            <el-menu-item index="/orderInfo">
              征订统计
            </el-menu-item>
          </el-submenu>
        </el-menu>
      </div>

      <!--  数据表格  -->
      <div class="manager-main-right">
        <router-view @update:user="updateUser" />
      </div>
    </div>

  </div>
</template>

<script>
export default {
  name: "Manager",
  data() {
    return {
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
    }
  },
  created() {
    // if (!this.user.id) {
    //   this.$router.push('/login')
    // }
  },
  methods: {
    updateUser() {
      this.user = JSON.parse(localStorage.getItem('xm-user') || '{}')   // 重新获取下用户的最新信息
    },
    goToPerson() {
        this.$router.push('/adminPerson')
    },
    logout() {
      localStorage.removeItem('xm-user')
      this.$router.push('/login')
    }
  }
}
</script>

<style scoped>
@import "@/assets/css/manager.css";
</style>