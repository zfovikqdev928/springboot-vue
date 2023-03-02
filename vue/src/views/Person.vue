<template>
  <el-card style="width: 500px;padding: 10px">
    <el-form label-width="80px" size="small" :model="form">

      <!--用户头像-->
      <div>
        <el-avatar src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"></el-avatar>
      </div>


      <!--用户基本信息-->
      <el-form-item label="用户名">
        <el-input v-model="form.username" disabled placeholder="请输入用户名"></el-input>
      </el-form-item>
      <el-form-item label="昵称">
        <el-input v-model="form.nickname" autocomplete="off" placeholder="请输入昵称"></el-input>
      </el-form-item>
      <el-form-item label="密码">
        <el-input type="text" v-model="form.password" autocomplete="off" placeholder="请输入密码"></el-input>
      </el-form-item>
      <el-form-item label="邮箱">
        <el-input v-model="form.email" autocomplete="off" placeholder="请输入邮箱"></el-input>
      </el-form-item>
      <el-form-item label="手机号">
        <el-input v-model="form.phone" autocomplete="off" placeholder="请输入手机号"></el-input>
      </el-form-item>
      <el-form-item label="地址">
        <el-input v-model="form.address" autocomplete="off" placeholder="请输入地址"></el-input>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="save">确 定</el-button>
      </el-form-item>
    </el-form>
  </el-card>

</template>

<script>
export default {
  name: "Person",
  data() {
    return {
      form: {},
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
    }
  },
  created() {
    this.request.get("/user/username/" + this.user.username).then(res => {
      if (res.code == '200') {
        this.form = res.data
      }

    })
  },
  methods: {
    save() {
      this.request.post("/user/save", this.form).then(res => {
        if (res.data) {
          this.$message.success("保存成功")

        } else {
          this.$message.error("保存失败")
        }
      })
    },
  }
}
</script>

<style scoped>
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}

.avatar {
  width: 178px;
  height: 178px;
  display: block;
}

</style>