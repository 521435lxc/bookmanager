"use strict";(self["webpackChunkvue"]=self["webpackChunkvue"]||[]).push([[753],{4753:function(e,r,s){s.r(r),s.d(r,{default:function(){return u}});var t=function(){var e=this,r=e._self._c;return r("div",{staticClass:"container"},[r("div",{staticStyle:{width:"400px",padding:"30px","background-color":"white","border-radius":"5px"}},[r("div",{staticStyle:{"text-align":"center","font-size":"20px","margin-bottom":"20px",color:"#333"}},[e._v("欢迎使用")]),r("el-form",{ref:"formRef",attrs:{model:e.form,rules:e.rules}},[r("el-form-item",{attrs:{prop:"username"}},[r("el-input",{attrs:{"prefix-icon":"el-icon-user",placeholder:"请输入账号"},model:{value:e.form.username,callback:function(r){e.$set(e.form,"username",r)},expression:"form.username"}})],1),r("el-form-item",{attrs:{prop:"password"}},[r("el-input",{attrs:{"prefix-icon":"el-icon-lock",placeholder:"请输入密码","show-password":""},model:{value:e.form.password,callback:function(r){e.$set(e.form,"password",r)},expression:"form.password"}})],1),r("el-form-item",[r("el-button",{staticStyle:{width:"100%","background-color":"#333","border-color":"#333",color:"white"},on:{click:e.login}},[e._v("登 录")])],1)],1)],1)])},o=[],a=(s(7658),{name:"Login",data(){return{form:{role:"ADMIN"},rules:{username:[{required:!0,message:"请输入账号",trigger:"blur"}],password:[{required:!0,message:"请输入密码",trigger:"blur"}]}}},created(){},methods:{login(){this.$refs["formRef"].validate((e=>{e&&this.$request.post("/login",this.form).then((e=>{"200"===e.code?(localStorage.setItem("xm-user",JSON.stringify(e.data)),this.$router.push("/"),this.$message.success("登录成功")):this.$message.error(e.msg)}))}))}}}),i=a,l=s(3736),n=(0,l.Z)(i,t,o,!1,null,"573a5092",null),u=n.exports}}]);
//# sourceMappingURL=753.20fbbe19.js.map