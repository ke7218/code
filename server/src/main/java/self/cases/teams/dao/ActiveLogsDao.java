package self.cases.teams.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import self.cases.teams.entity.ActiveLogs;
import java.util.Map;

/**
 * 数据层处理接口
 * 报名记录
 */
@Repository("activeLogsDao")
public interface ActiveLogsDao extends BaseMapper<ActiveLogs> {
	
    @Select("<script>" +
            "SELECT " +
            "al.id, al.create_time createTime, al.active_id activeId, al.user_id userId, " +
            "a.name activeName, u.name userName, t.name teamName " +
            "FROM active_logs al " +
            "LEFT JOIN activities a ON al.active_id = a.id " +
            "LEFT JOIN users u ON al.user_id = u.id " +
            "LEFT JOIN teams t ON a.team_id = t.id " +
            "<where> " +
            "<if test='name != null and name.trim() != &quot;&quot; '>" +
            "AND a.name LIKE CONCAT('%', #{name}, '%') " +
            "</if>" +
            "</where>" +
            "ORDER BY al.create_time DESC" +
            "</script>")
    Page<Map<String, Object>> qryPageInfo(Page<Map<String, Object>> page, @Param("name") String name);
}