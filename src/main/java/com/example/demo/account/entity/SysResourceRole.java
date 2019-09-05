package com.example.demo.account.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 描述：角色资源表
 * @author littlecar
 * @date 2019/9/5 10:10
 */
@Entity
@Table(name="sys_resource_role")
@Data
public class SysResourceRole implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", length = 10)
	private Long id;
	/**
	 * 角色ID
	 */
	@Column(name = "roleId", length = 50)
	private String roleId;
	/**
	 * 资源ID
	 */
	@Column(name = "resourceId", length = 50)
	private String resourceId;
	/**
	 * 更新时间
	 */
	@Column(name = "updateTime")
	private Date updateTime;

}