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
@Table(name="resource")
@Data
public class SysResource implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", length = 10)
	private Long id;
	/**
	 * url
	 */
	@Column(name = "url", length = 100)
	private String url;
	/**
	 * 资源名称
	 */
	@Column(name = "name", length = 100)
	private String name;


}