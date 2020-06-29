package com.zyl.tagme.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.navigation.NavigationView;
import com.zyl.tagme.CreateBlogActivity;
import com.zyl.tagme.R;
import com.zyl.tagme.common.async.BlogAsyncTask;
import com.zyl.tagme.common.util.MyToast;
import com.zyl.tagme.service.pojo.Blog;
import com.zyl.tagme.service.view.adapter.AbstractViewAdapter;
import com.zyl.tagme.service.view.adapter.ListViewAdapter;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class HomeFragment extends Fragment {

    // 一页大小
    public static final int PAGE_SIZE = 10;

    private RecyclerView recyclerView;
    private List<Blog> blogs;
    private AbstractViewAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView.LayoutManager layoutManager;
    private int lastVisibleItem;
    private int currentPage = 1;
    private NavigationView navigationView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        blogs = getBlogs(currentPage, PAGE_SIZE);
        refreshCardGroup();

        // 下拉刷新布局
        swipeRefreshLayout = getView().findViewById(R.id.swipe_refresh);
        navigationView = getView().findViewById(R.id.nav_view);
        // 初始化下拉刷新事件
        initDownPullRefreshListener();
        // 初始化上划加载更多事件
        initUpPullLoadMoreListener();
    }

    /**
     * 请求后端数据
     *
     * @return List<Blog>
     */
    private List<Blog> getBlogs(int pageIndex, int pageSize) {
        try {
            return new BlogAsyncTask().getBlogs(pageIndex, pageSize).execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 刷新卡片组
     */
    private void refreshCardGroup() {
        // 创建适配器 传入后台来的数据
        adapter = new ListViewAdapter(blogs, R.layout.card_view);
        // 创建布局管理器
        layoutManager = new LinearLayoutManager(getActivity());
        // 刷新卡片
        refreshCard(adapter, layoutManager);
    }

    /**
     * 刷新cardView
     *
     * @param viewAdapter   视图适配器
     * @param layoutManager 布局管理器
     */
    private void refreshCard(AbstractViewAdapter viewAdapter, RecyclerView.LayoutManager layoutManager) {
        // 定位到 recyclerView
        recyclerView = getView().findViewById(R.id.recyclerview);
        // 设置适配器 适配内容
        recyclerView.setAdapter(viewAdapter);
        // 设置布局管理器 选择布局
        recyclerView.setLayoutManager(layoutManager);

        // 创建卡片点击事件
        initCardClickListener();
    }

    /**
     * 创建卡片点击事件
     */
    private void initCardClickListener() {
        adapter.setOnCardClickListener(new AbstractViewAdapter.OnCardClickListener() {
            @Override
            public void onCardClick(int position) {
                // MyToast.showToast(MainActivity.this, adapter.getBlogByPosition(position).getTitle());
                Intent blogIntent = new Intent(getActivity(), CreateBlogActivity.class);
                blogIntent.putExtra("blogId", blogs.get(position).getId());
                blogIntent.putExtra("blogTitle", blogs.get(position).getTitle());
                blogIntent.putExtra("blogAuthor", blogs.get(position).getAuthor());
                blogIntent.putExtra("blogContent", blogs.get(position).getContent());
                startActivity(blogIntent);
            }
        });
    }

    /**
     * 设置下拉刷新监听器
     */
    private void initDownPullRefreshListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        blogs = getBlogs(1, PAGE_SIZE);
                        refreshCardGroup();
                        adapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });
    }

    /**
     * 设置上划加载更多事件
     */
    private void initUpPullLoadMoreListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem == adapter.getItemCount() - 1) {
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            MyToast.showToast(getActivity(), "加载中...");
                            blogs.addAll(Objects.requireNonNull(getBlogs(++currentPage, PAGE_SIZE)));
                            // refreshCardGroup();
                            adapter.notifyDataSetChanged();
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    });
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = ((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();
            }
        });
    }

}