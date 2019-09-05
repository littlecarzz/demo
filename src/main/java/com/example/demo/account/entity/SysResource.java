package com.example.demo.account.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 描述：资源表
 * @author littlecar
 * @date 2019/9/5 10:10
 */
@Entity
@Table(name="sys_resource")
@Data
public class SysResource implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", length = 10)
	private Long id;
	/**
	 * url
	 */
	@Column(name = "resourceString", length = 1000)
	private String resourceString;
	/**
	 * 资源ID
	 */
	@Column(name = "resourceId", length = 50)
	private String resourceId;
	/**
	 * 备注
	 */
	@Column(name = "remark", length = 200)
	private String remark;
	/**
	 * 资源名称
	 */
	@Column(name = "resourceName", length = 400)
	private String resourceName;
	/**
	 * 资源所对应的方法名
	 */
	@Column(name = "methodName", length = 400)
	private String methodName;
	/**
	 * 资源所对应的包路径
	 */
	@Column(name = "methodPath", length = 1000)
	private String methodPath;

}