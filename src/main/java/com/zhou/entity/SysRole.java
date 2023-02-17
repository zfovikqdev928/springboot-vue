package com.zhou.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 *
 * @author 周益全
 * @since 2023-02-16
 */
@Data
@TableName("sys_role")
@ApiModel(value = "Role对象",description = "")
public class SysRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    private String name;

    private String description;


}
