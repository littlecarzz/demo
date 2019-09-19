package com.example.demo.account.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 描述：资源表
 * @author littlecar
 * @date 2019/9/5 10:10
 */
@Entity
@Table(name="resource")
@Data
public class SysResource implements Serializable, Comparator<SysResource> {

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
	@Column(name = "title", length = 100)
	private String title;
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

	@Override
	public int compare(SysResource o1, SysResource o2) {
		return o1.getNum()-o2.getNum();
	}

	@Override
	public String toString() {
		return "SysResource{" +
				"id=" + id +
				", url='" + url + '\'' +
				", title='" + title + '\'' +
				", parent=" + parent +
				", subResources.size=" + subResources.size() +
				", num=" + num +
				", icon='" + icon + '\'' +
				'}';
	}
}