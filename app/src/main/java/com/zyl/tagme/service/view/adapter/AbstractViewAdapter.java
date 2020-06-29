package com.zyl.tagme.service.view.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zyl.tagme.R;
import com.zyl.tagme.service.pojo.Blog;

import java.util.List;

public abstract class AbstractViewAdapter extends RecyclerView.Adapter<AbstractViewAdapter.InnerHolder> {

    // 大图样式
    private static final int LARGE_IMAGE_VIEW = 0;
    // 小图样式
    private static final int SMALL_IMAGE_VIEW = 1;
    // 多图样式
    private static final int MULTI_IMAGE_VIEW = 2;
    // 只有文字的样式
    private static final int TEXT_ONLY_VIEW = 3;
    // 视频样式
    private static final int VIDEO_VIEW = 4;
    // 测试样式
    private static final int TEST_VIEW = 5;

    // 元数据 提供数据
    private final List<Blog> blogs;
    // 卡片布局id 展示数据
    private int view;
    // card点击事件监听器
    private OnCardClickListener onCardClickListener;

    /**
     * 用构造函数赋值
     *
     * @param blogs 元数据
     * @param view  卡片布局id
     */
    public AbstractViewAdapter(List<Blog> blogs, int view) {
        this.blogs = blogs;
        this.view = view;
    }

    /**
     * 根据卡片布局id获取布局句柄 并根据句柄细分其中的元素 并一一赋值
     *
     * @param parent   ViewGroup
     * @param viewType 卡片类型
     * @return 细分后尚未赋值的卡片容器
     */
    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // View代表的就是card界面
        View cardView;
        switch (viewType) {
            case TEST_VIEW:
                cardView = View.inflate(parent.getContext(), R.layout.test_view, null);
                return new TestViewInnerHolder(cardView);
            case TEXT_ONLY_VIEW:
                cardView = View.inflate(parent.getContext(), view, null);
                return new TextOnlyViewHolder(cardView);
            default:
                throw new IllegalStateException("Unexpected value: " + viewType);
        }
    }

    /**
     * 根据卡片位置对卡片容器进行赋值
     *
     * @param holder   卡片容器
     * @param position 位置
     */
    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        holder.setCardData(blogs.get(position), position);
    }

    /**
     * 获取卡片个数
     *
     * @return 卡片个数
     */
    public int getItemCount() {
        if (blogs != null) {
            return blogs.size();
        }
        return 0;
    }

    /**
     * 根据位置定位blog
     *
     * @param position 位置
     * @return Blog
     */
    public Blog getBlogByPosition(int position) {
        if (null != blogs) {
            return blogs.get(position);
        }
        return null;
    }

    /**
     * 从adapter角度设置卡片点击事件监听器
     *
     * @param listener OnCardClickListener
     */
    public void setOnCardClickListener(OnCardClickListener listener) {
        this.onCardClickListener = listener;
    }

    /**
     * 获取cardView类型
     *
     * @param position 位置
     * @return 卡片类型
     */
    @Override
    public int getItemViewType(int position) {
        // TODO: 2020/6/24 zylTodo 怎么确定卡牌类型
        if (blogs.get(position).getTitle().startsWith("test")) {
            return TEST_VIEW;
        } else {
            return TEXT_ONLY_VIEW;
        }
    }

    /**
     * card点击事件接口
     */
    public interface OnCardClickListener {
        void onCardClick(int position);
    }

    public interface OnUpLoadMoreListener {
        void onUpLoadMore(InnerHolder holder);
    }

    /**
     * 卡片容器内部类 里面细分卡片组件
     */
    public static class InnerHolder extends RecyclerView.ViewHolder {

        public InnerHolder(@NonNull View cardView) {
            super(cardView);
        }

        public void setCardData(Blog blog, int position) {
        }
    }

    /**
     * 测试Holder实现
     */
    class TestViewInnerHolder extends InnerHolder {

        private final TextView title;
        private int position;

        public TestViewInnerHolder(@NonNull View cardView) {
            super(cardView);

            // cardView就是一个卡片
            title = cardView.findViewById(R.id.test_card_title);

            // 实际设置卡片点击事件的地方
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null != onCardClickListener) {
                        onCardClickListener.onCardClick(position);
                    }
                }
            });
        }

        public void setCardData(Blog blog, int position) {

            this.position = position;

            title.setText(blog.getTitle());
        }
    }

    /**
     * 只有文字的Holder实现
     */
    class TextOnlyViewHolder extends InnerHolder {

        private final TextView title;
        private final TextView author;
        private final TextView summary;
        private final TextView agree_count;
        private final TextView comment_count;
        private int position;

        public TextOnlyViewHolder(@NonNull View cardView) {
            super(cardView);

            // cardView就是一个卡片
            title = cardView.findViewById(R.id.card_title);
            author = cardView.findViewById(R.id.card_author);
            summary = cardView.findViewById(R.id.card_summary);
            agree_count = cardView.findViewById(R.id.card_agree_count);
            comment_count = cardView.findViewById(R.id.card_comment_count);

            // 实际设置卡片点击事件的地方
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null != onCardClickListener) {
                        onCardClickListener.onCardClick(position);
                    }
                }
            });
        }

        public void setCardData(Blog blog, int position) {

            this.position = position;

            title.setText(blog.getTitle());
            author.setText(blog.getAuthor());
            summary.setText(blog.getSummary());
            agree_count.setText(String.valueOf(blog.getAgreeCount() + " 赞同"));
            comment_count.setText(String.valueOf(blog.getCommentCount() + " 评论"));
        }
    }

}
