<template>
  <div style="line-height: 60px;display: flex">
    <div style="flex: 1;">
      <span :class="collapseBtnClass" style="cursor: pointer;font-size: 18px" @click="collapse"></span>
      <!--      页签 - 也叫面包屑-->
      <el-breadcrumb separator="/" style="display: inline-block;margin-left: 10px ">
        <el-breadcrumb-item :to=" '/' ">首页</el-breadcrumb-item>
      </el-breadcrumb>


    </div>
    <el-dropdown style="width: 100px;cursor: pointer">
      <div style="display: inline-block">
        <img src="../assets/Tx.jpg"
             alt=""
             style="width: 30px;border-radius: 50%;position:relative; top: 10px;right: 5px"
        >

        <span>{{ user.nickname }}</span>
        <i class="el-icon-arrow-down" style="margin-left: 5px;"></i>
      </div>
      <el-dropdown-menu slot="dropdown" style="text-align: center;width: 100px">
        <el-dropdown-item style="font-size: 14px; padding: 5px 0">个人信息</el-dropdown-item>
        <el-dropdown-item style="font-size: 14px; padding: 5px 0">
          <router-link to="/login" style="text-decoration: none">退出</router-link>
        </el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
  </div>
</template>

<script>
export default {
  name: "Header",
  props: {
    collapseBtnClass: String,
    collapse: Boolean,
  },
  data() {
    return {
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
    }
  }
}
</script>