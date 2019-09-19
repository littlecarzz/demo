package com.example.demo.account.entity;

import lombok.Data;

import java.util.List;

/**
 * 描述：
 *
 * @author littlecar
 * @date 2019/9/19 10:59
 */
@Data
public class ResourceInfo {

    private String title;
    private String icon;
    private String href;
    private Boolean spread;
    private List<ResourceInfo> children;
}
