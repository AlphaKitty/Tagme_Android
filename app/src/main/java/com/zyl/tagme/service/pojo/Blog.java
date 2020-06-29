package com.zyl.tagme.service.pojo;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 富文本素材
 * </p>
 *
 * @author zyl
 * @since 2020-06-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Blog implements Serializable {

    private static final long serialVersionUID = 6208818256216121232L;

    /**
     * blogID
     */
    private String id;

    /**
     * 标题
     */
    private String title;

    /**
     * 作者
     */
    private String author;

    /**
     * 素材标签
     */
    private String tag;

    /**
     * 摘要
     */
    private String summary;

    /**
     * 内容
     */
    private String content;

    /**
     * 播放量
     */
    private Integer viewCounts;

    /**
     * 赞同数
     */
    private Integer agreeCount;

    /**
     * 评论数
     */
    private Integer commentCount;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;


}
