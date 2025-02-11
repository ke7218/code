package self.cases.teams.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import self.cases.teams.entity.ActivityComment;
import java.util.List;
import java.util.Map;

@Repository("activityCommentDao")
public interface ActivityCommentDao extends BaseMapper<ActivityComment> {
    
    @Select("SELECT ac.*, u.name as userName FROM activity_comments ac " +
           "LEFT JOIN users u ON ac.user_id = u.id " +
           "WHERE ac.activity_id = #{activityId} " +
           "ORDER BY ac.create_time DESC")
    Page<Map<String, Object>> getCommentsByActivityId(Page<Map<String, Object>> page, 
                                                     @Param("activityId") String activityId);

    @Select("SELECT id, activity_id, user_id, content, create_time, sentiment " +
           "FROM activity_comments " +
           "WHERE activity_id = #{activityId}")
    List<ActivityComment> getCommentsByActivityIdForAnalysis(@Param("activityId") String activityId);
} 