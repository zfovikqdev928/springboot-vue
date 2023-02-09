<template>
  <div class="wrapper">
    <div style="margin:60px auto;background-color: #fff;width: 550px;height: 680px;padding: 20px; border-radius: 10px">
      <div style="margin: 20px 0;text-align: center;font-size: 24px"><b>用户注册</b></div>

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

        <!--确认密码-->
        <el-form-item prop="confirmPassword">
          <el-input size="medium" style="margin: 10px 0 "
                    prefix-icon="el-icon-lock" v-model="user.confirmPassword" show-password placeholder="确认密码">
          </el-input>
        </el-form-item>

        <el-form-item prop="nickname">
          <el-input size="medium" style="margin: 10px 0 "
                    prefix-icon="el-icon-lock" v-model="user.nickname" show-password placeholder="昵称">
          </el-input>
        </el-form-item>

        <el-form-item prop="email">
          <el-input size="medium" style="margin: 10px 0 "
                    prefix-icon="el-icon-lock" v-model="user.email" show-password placeholder="电子邮箱">
          </el-input>
        </el-form-item>

        <el-form-item prop="phone">
          <el-input size="medium" style="margin: 10px 0 "
                    prefix-icon="el-icon-lock" v-model="user.phone" show-password placeholder="手机号">
          </el-input>
        </el-form-item>

        <el-form-item prop="address">
          <el-input size="medium" style="margin: 10px 0 "
                    prefix-icon="el-icon-lock" v-model="user.address" show-password placeholder="地址">
          </el-input>
        </el-form-item>


        <el-form-item>
          <div style="margin: 10px 0;text-align: right">
            <el-button type="primary" size="small" autocomplete="off" @click="login">提交注册</el-button>
            <el-button type="warning" size="small" autocomplete="off" @click="$router.push('/login')">返回登录</el-button>
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
        confirmPassword:
            [
              {required: true, message: '请重复输入密码', trigger: 'blur'},
              {min: 1, max: 100, message: '长度在 1 - 100 个字符', trigger: 'blur'}
            ],
        nickname:
            [
              {required: true, message: '请输入昵称', trigger: 'blur'},
              {min: 1, max: 100, message: '长度在 1 - 100 个字符', trigger: 'blur'}
            ],
        email:
            [
              {required: true, message: '请输入电子邮箱', trigger: 'blur'},
              {min: 1, max: 100, message: '长度在 1 - 100 个字符', trigger: 'blur'}
            ],
        phone:
            [
              {required: true, message: '请输入手机号', trigger: 'blur'},
              {min: 11, max: 11, message: '长度在 11 个字符', trigger: 'blur'}
            ],
        address:
            [
              {required: true, message: '请输入地址', trigger: 'blur'},
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
          if (this.user.password !== this.user.confirmPassword){
            this.$message.error("两次输入的密码不一致")
            return false;
          }
          this.request.post("/user/register", this.user).then(res => {
            if (res.code == '200') {
              this.$message.success("注册成功!")
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