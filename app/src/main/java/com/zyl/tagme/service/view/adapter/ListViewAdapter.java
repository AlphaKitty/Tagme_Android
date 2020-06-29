package com.zyl.tagme.service.view.adapter;

import com.zyl.tagme.service.pojo.Blog;

import java.util.List;

/**
 * List布局适配器
 */
public class ListViewAdapter extends AbstractViewAdapter {

    /**
     * 用构造函数赋值
     *
     * @param blogs 元数据
     * @param view  view
     */
    public ListViewAdapter(List<Blog> blogs, int view) {
        super(blogs, view);
    }
}
