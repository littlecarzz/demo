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
@Table(name="role_resource")
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
	private Long roleId;
	/**
	 * 资源ID
	 */
	@Column(name = "resourceId", length = 50)
	private Long resourceId;

}