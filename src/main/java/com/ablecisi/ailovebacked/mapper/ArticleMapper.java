package com.ablecisi.ailovebacked.mapper;

import com.ablecisi.ailovebacked.pojo.entity.Article;
import com.ablecisi.ailovebacked.pojo.vo.admin.AdminArticleListVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * AILoveBacked <br>
 * com.ablecisi.ailovebacked.mapper <br>
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/8/22
 * 星期五
 * 22:59
 **/
@Mapper
public interface ArticleMapper {

    /**
     * 根据文章ID获取文章详情
     *
     * @param articleId 文章ID
     * @return 文章详情
     */
    Article selectById(Long articleId);

    /**
     * 根据文章ID获取相关文章列表
     *
     * @param articleId 文章ID
     * @param tags      文章标签
     * @return 相关文章列表
     */
    List<Article> selectRelatedArticlesByTags(Long articleId, List<String> tags);

    /**
     * 获取热门文章列表
     *
     * @return 热门文章列表
     */
    List<Article> selectHotArticles();

    List<Article> selectCollectedByUserId(@Param("userId") Long userId,
                                          @Param("offset") int offset,
                                          @Param("size") int size);

    long countPageForAdmin(@Param("keyword") String keyword);

    List<AdminArticleListVO> pageForAdmin(@Param("keyword") String keyword,
                                          @Param("offset") int offset,
                                          @Param("size") int size);

    int insert(Article article);

    int updateRow(Article article);

    int deleteById(@Param("id") Long id);

    int adjustLikeCount(@Param("id") Long id, @Param("delta") int delta);

    int incrementViewCount(@Param("id") Long id);

    /**
     * 新增一条评论时 article.comment_count +1
     */
    int incrementCommentCount(@Param("id") Long id);

    /**
     * 软删除一条评论时 article.comment_count -1（不低于 0）
     */
    int decrementCommentCount(@Param("id") Long id);

    /**
     * 按增量调整评论数（如批量软删子树 delta 为负数）
     */
    int adjustCommentCount(@Param("id") Long id, @Param("delta") int delta);
}
