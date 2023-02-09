<template>
  <el-container style="min-height: 100vh;">
    <!--  左侧边栏  -->
    <el-aside :width="sideWidth +'px'"
              style="background-color: rgb(238, 241, 246); box-shadow: 2px 0 6px rgb(0 21 41 / 35%);)">
      <Aside :isCollapse="isCollapse" :logoTextShow="logoTextShow"/>
    </el-aside>

    <!--  Header  -->
    <el-container>
      <el-header style="border-bottom: 1px solid #ccc;">
        <Header :collapseBtnClass="collapseBtnClass" :collapse="isCollapse"></Header>
      </el-header>


      <!--   主体   -->
      <el-main>
        <!--表示当前页面的子路由会在 router-view 里面展示-->
        <router-view />
      </el-main>
    </el-container>
  </el-container>

</template>

<script>
import Aside from "@/components/Aside";
import Header from "@/components/Header";

export default {
  name: 'Home',
  data() {
    return {
      collapseBtnClass: 'el-icon-s-fold',
      isCollapse: false,
      sideWidth: 200,
      logoTextShow: true,
    }
  },
  // 引入组件
  components: {
    Aside, Header
  },
  methods: {
    // 点击收缩按钮触发
    collapse() {
      this.isCollapse = !this.isCollapse
      if (this.isCollapse) {
        this.sideWidth = 64
        this.collapseBtnClass = 'el-icon-s-unfold'
        this.logoTextShow = false;
      } else {
        this.sideWidth = 200
        this.collapseBtnClass = 'el-icon-s-fold'
        this.logoTextShow = true
      }
    },

  }
}
</script>
