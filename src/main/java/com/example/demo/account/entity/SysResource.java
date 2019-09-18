package com.example.demo.account.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
	/**
	 * 父资源，若为根目录，则为null
	 */
//	@Column(name = "name", length = 100)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="parent")
	private SysResource parent;
	/**
	 * 子资源
	 */
	@OneToMany(mappedBy="parent",fetch = FetchType.LAZY)
	@OrderBy(value="num ASC")
	private List<SysResource> subResources = new ArrayList<SysResource>();
	/**
	 * 排序
	 */
	private Integer num;
	/**
	 * 图标
	 */
	private String icon;
	/**
	 * 是否能传播
	 */
	private Boolean spread;

}