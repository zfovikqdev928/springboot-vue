<template>
  <div>
    <el-row>
      <el-col :span="12">
        <div id="main" style="width: 500px;height: 400px"></div>
      </el-col>
      <el-col :span="12">
        <div id="pie" style="width: 500px;height: 400px"></div>
      </el-col>
    </el-row>
  </div>

</template>

<script>
import * as echarts from 'echarts'

export default {
  name: "Home",
  data() {
    return {}
  },

  // mounted 等页面元素渲染后再触发
  mounted() {
    var chartDom = document.getElementById('main');
    var myChart = echarts.init(chartDom);
    var option = {
      title: {
        text: '2023 年各季度会员数量统计',
        subtext: '趋势图',
        left: 'center'
      },
      xAxis: {
        type: 'category',
        data: ["第一季度", "第二季度", "第三季度", "第四季度"]
      },
      yAxis: {
        type: 'value'
      },
      series: [
        {
          data: [],
          type: 'line'
        },
        {
          data: [],
          type: 'bar'
        }
      ]
    };

    this.request.get("/echarts/members").then(res => {
      // option.xAxis.data = res.data;
      option.series[0].data = res.data;
      option.series[1].data = res.data;
      // 数据准备完成后再 setOption
      option && myChart.setOption(option);

    })


    // 饼图
    var charPie = document.getElementById('pie');
    var PieInit = echarts.init(charPie);
    var optionPie = {
      title: {
        text: '2023 年各季度会员数量统计',
        subtext: '趋势图',
        left: 'center'
      },
      tooltip: {
        trigger: 'item'
      },
      legend: {
        orient: 'vertical',
        left: 'left'
      },
      series: [
        {
          type: 'pie',
          radius: '60%',
          data: [],
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }
      ]
    };
    this.request.get("/echarts/members").then(res => {
      option.series[0].data = res.data;
      option.series[1].data = res.data;
      // 数据准备完成后再 setOption
      optionPie.series[0].data = [
        {name: "第一季度", value: res.data[0]},
        {name: "第二季度", value: res.data[1]},
        {name: "第三季度", value: res.data[2]},
        {name: "第四季度", value: res.data[3]}
      ]
      PieInit.setOption(optionPie)
    })

  }
}
</script>