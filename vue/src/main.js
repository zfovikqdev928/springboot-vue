import Vue from 'vue'
import App from './App.vue'
import router from './router'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import './assets/gloable.css';
import request from "@/utils/request";

Vue.config.productionTip = false
// 导入 element 组件
Vue.use(ElementUI, {size: "mini"}); // 引入 ，控件大小

// 导入 request.js 的 axios
Vue.prototype.request = request


new Vue({
    router,
    render: h => h(App)
}).$mount('#app')
