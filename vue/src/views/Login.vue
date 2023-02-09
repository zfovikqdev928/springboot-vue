<template>
  <div class="wrapper">
    <div style="margin:200px auto;background-color: #fff;width: 350px;height: 300px;padding: 20px; border-radius: 10px">

      <div style="margin: 20px 0;text-align: center;font-size: 24px"><b>登录</b></div>

      <el-form :rules="rules" :model="user" ref="userForm">

        <el-form-item prop="username">
          <el-input size="medium" style="margin: 10px 0 "
                    prefix-icon="el-icon-user" v-model="user.username" placeholder="用户名">
          </el-input>
        </el-form-item>

        <el-form-item prop="password">
          <el-input size="medium" style="margin: 10px 0 "
                    prefix-icon="el-icon-lock" v-model="user.password" show-password placeholder="密码">
          </el-input>
        </el-form-item>
        <el-form-item>

          <div style="margin: 10px 0;text-align: right">
            <el-button type="primary" size="small" autocomplete="off" @click="login">登录</el-button>
            <el-button type="warning" size="small" autocomplete="off">注册</el-button>
            <el-button type="success" size="small" autocomplete="off" @click="reset">重置</el-button>
          </div>
        </el-form-item>
      </el-form>


    </div>

  </div>
</template>

<script>
export default {
  name: "Login",
  data() {
    return {
      user: {},

      rules: {
        username:
            [
              {required: true, message: '请输入用户名', trigger: 'blur'},
              {min: 1, max: 50, message: '长度在 1 - 50 个字符', trigger: 'blur'}
            ],
        password:
            [
              {required: true, message: '请输入密码', trigger: 'blur'},
              {min: 1, max: 100, message: '长度在 1 - 100 个字符', trigger: 'blur'}
            ],
      }
    }
  },

  methods: {

    // 校验登录表单的填写情况
    login() {
      this.$refs["userForm"].validate((valid) => {
        if (valid) {
          this.request.post("/user/login", this.user).then(res => {
            if (res.code == '200') {
              localStorage.setItem("user", JSON.stringify(res.data)) // 存储用户信息到浏览器
              this.$message.success("登录成功!")
              this.$router.push("/")
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      });
    },
    // 重置按钮方法
    reset() {
      this.user.username = ""
      this.user.password = ""
    },
  },
}
</script>

<style>

.wrapper {
  height: 100vh;
  background-image: linear-gradient(to bottom right, #FC466B, #3F5EFB);
  overflow: hidden;
}
</style>