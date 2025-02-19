package self.cases.teams.service;

import self.cases.teams.msg.PageData;
import self.cases.teams.entity.ActiveLogs;
import java.util.List;
import java.util.Map;

/**
 * 业务层处理
 * 报名记录
 */
public interface ActiveLogsService extends BaseService<ActiveLogs, String> {

	/**
	 * 用户是否参与活动
	 * @param activeId 指定活动ID
	 * @param userId 指定用户ID
	 * @return
	 */
	public Boolean isActive(String activeId, String userId);

	/**
	 * 依据活动ID获取报名列表
	 * @param activeId 活动ID
	 * @return
	 */
	public List<Map<String, Object>> getListByActiveId(String activeId);

	/**
	 * 分页查询报名记录
	 * @param pageIndex 当前页码
	 * @param pageSize 每页数据量
	 * @param name 活动名称
	 * @return
	 */
	public PageData getPageInfo(Long pageIndex, Long pageSize, String name);
}