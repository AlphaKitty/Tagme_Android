package com.zyl.tagme.service.view.adapter;

import com.zyl.tagme.service.pojo.Blog;

import java.util.List;

public class StaggeredViewAdapter extends AbstractViewAdapter {
    /**
     * 用构造函数赋值
     *
     * @param blogs 元数据
     * @param view  view
     */
    public StaggeredViewAdapter(List<Blog> blogs, int view) {
        super(blogs, view);
    }
}
