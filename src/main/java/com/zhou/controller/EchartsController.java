package com.zhou.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.Quarter;
import com.zhou.common.R;
import com.zhou.entity.SysUser;
import com.zhou.service.impl.SysUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 周益全 (https://gitee.com/zfovikqdev928)
 * @BugFeedBackEmail: zfovikqdev928@126.com
 * @Date: Create in 2023-02
 * @Description:
 * @Version:
 */
@RestController
@RequestMapping("/echarts")
public class EchartsController {
    @Autowired
    private SysUserServiceImpl sysUserService;

    /**
     * 演示 demo
     * @return
     */
    @GetMapping("/example")
    public R get() {
        Map<String, Object> map = new HashMap<>();
        map.put("x", CollUtil.newArrayList("星期一", "星期二", "Wed", "Thu", "Fri", "Sat", "Sun"));
        map.put("y", CollUtil.newArrayList(16, 230, 4, 218, 15, 147, 760));
        return R.success(map);
    }

    /**
     * 返回每个季度的流量人数
     *
     * @return
     */
    @GetMapping("/members")
    public R members() {
        List<SysUser> list = sysUserService.list();
        int q1 = 0;
        int q2 = 0;
        int q3 = 0;
        int q4 = 0;
        for (SysUser user : list) {
            Date createTime = user.getCreateTime();
            Quarter quarter = DateUtil.quarterEnum(createTime); // 获取季度
            switch (quarter) {
                case Q1:
                    q1 += 1;
                    break;
                case Q2:
                    q2 += 1;
                    break;
                case Q3:
                    q3 += 1;
                    break;
                case Q4:
                    q4 += 1;
                    break;
                default:
                    break;
            }
        }

        return R.success(CollUtil.newArrayList(q1, q2, q3, q4));
    }


}
