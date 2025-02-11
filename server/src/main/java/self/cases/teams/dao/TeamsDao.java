package self.cases.teams.dao;

import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import self.cases.teams.entity.Teams;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 数据层处理接口
 * 社团信息
 */
@Repository("teamsDao")
public interface TeamsDao extends BaseMapper<Teams> {
	
	/**
	 * 获取指定用户加入的社团列表
	 */
	@Select("SELECT t.id, t.name, t.total, t.create_time as createTime, " +
			"tt.name as typeName, u.name as managerName, u.phone as managerPhone " +
			"FROM teams t " +
			"INNER JOIN members m ON t.id = m.team_id " +
			"INNER JOIN team_types tt ON t.type_id = tt.id " +
			"INNER JOIN users u ON t.manager = u.id " +
			"WHERE m.user_id = #{userId} " +
			"ORDER BY t.create_time DESC")
	List<Map<String, Object>> getListByMemberId(@Param("userId") String userId);

}