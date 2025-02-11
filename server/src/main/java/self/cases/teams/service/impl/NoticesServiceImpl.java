package self.cases.teams.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import self.cases.teams.dao.*;
import self.cases.teams.entity.*;
import self.cases.teams.msg.PageData;
import self.cases.teams.service.NoticesService;
import self.cases.teams.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("noticesService")
public class NoticesServiceImpl implements NoticesService {

    @Autowired
    private NoticesDao noticesDao;

    @Autowired
    private TeamsDao teamsDao;

    @Autowired
    private MembersDao membersDao;

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public PageData getPageInfo(Long pageIndex, Long pageSize, Notices notices) {
        QueryWrapper<Notices> qw = new QueryWrapper<>();
        
        if (StringUtils.isNotNullOrEmpty(notices.getTitle())) {
            qw.like("title", notices.getTitle());
        }
        
        if (StringUtils.isNotNullOrEmpty(notices.getDetail())) {
            qw.like("detail", notices.getDetail());
        }
        
        qw.orderByDesc("create_time");
        
        Page<Notices> page = noticesDao.selectPage(new Page<>(pageIndex, pageSize), qw);
        return parsePage(page);
    }

    @Override
    @Transactional
    public void add(Notices notices) {

        noticesDao.insert(notices);
    }

    @Override
    @Transactional
    public void update(Notices notices) {

        noticesDao.updateById(notices);
    }

    @Override
    @Transactional
    public void delete(Notices notices) {

        noticesDao.deleteById(notices);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public Notices getOne(String id) {

        Notices notices = noticesDao.selectById(id);

        return notices;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<Notices> getSysNotices(){

        List<Notices> list = noticesDao.qrySysNotices();

        return list;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<Notices> getManNotices(String manId){

        List<Notices> list = noticesDao.qryManNotices(manId);

        return list;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<Notices> getMemNotices(String memId){

        List<Notices> list = noticesDao.qryMemNotices(memId);

        return list;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public PageData getPageAll(Long pageIndex, Long pageSize, String title, String teamName){

        Page<Map<String, Object>>  page =
                noticesDao.qryPageAll(new Page<Map<String, Object>>(pageIndex, pageSize), title, teamName);

        return parsePage(page);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public PageData getPageById(Long pageIndex, Long pageSize, String userId, String title, String teamName){

        Page<Map<String, Object>>  page =
                noticesDao.qryPageById(new Page<Map<String, Object>>(pageIndex, pageSize), userId, title, teamName);

        return parsePage(page);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<Map<String, Object>> getAllNoticesWithTeam() {
        // 获取所有通知
        QueryWrapper<Notices> qw = new QueryWrapper<>();
        qw.orderByDesc("create_time");
        List<Notices> notices = noticesDao.selectList(qw);
        
        return convertNoticesWithTeam(notices);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<Map<String, Object>> getUserNoticesWithTeam(String userId) {
        // 获取用户所在的所有社团ID
        QueryWrapper<Members> memberQw = new QueryWrapper<>();
        memberQw.eq("user_id", userId);
        List<Members> members = membersDao.selectList(memberQw);
        List<String> teamIds = members.stream()
                .map(Members::getTeamId)
                .collect(Collectors.toList());
        
        // 获取系统管理员发布的通知和用户所在社团的通知
        QueryWrapper<Notices> noticeQw = new QueryWrapper<>();
        noticeQw.and(wrapper -> wrapper
                .isNull("team_id")  // 系统管理员发布的通知（team_id 为 null）
                .or()
                .in(!teamIds.isEmpty(), "team_id", teamIds));  // 用户所在社团发布的通知
        noticeQw.orderByDesc("create_time");
        
        List<Notices> notices = noticesDao.selectList(noticeQw);
        return convertNoticesWithTeam(notices);
    }

    /**
     * 转换通知列表，添加发布社团信息
     */
    private List<Map<String, Object>> convertNoticesWithTeam(List<Notices> notices) {
        List<Map<String, Object>> result = new ArrayList<>();
        
        for (Notices notice : notices) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", notice.getId());
            map.put("title", notice.getTitle());
            map.put("detail", notice.getDetail());
            map.put("createTime", notice.getCreateTime());
            
            // 根据 team_id 是否为空来判断是否为系统管理员发布的通知
            if (notice.getTeamId() == null) {
                map.put("teamName", "系统管理员");
            } else {
                Teams team = teamsDao.selectById(notice.getTeamId());
                map.put("teamName", team != null ? team.getName() : "未知社团");
            }
            
            result.add(map);
        }
        
        return result;
    }

    /**
     * 转化分页查询的结果
     */
    public PageData parsePage(Page<?> p) {
        List<Map<String, Object>> resultList;
        if (p.getRecords().isEmpty()) {
            resultList = new ArrayList<>();
        } else if (p.getRecords().get(0) instanceof Map) {
            resultList = (List<Map<String, Object>>) p.getRecords();
        } else if (p.getRecords().get(0) instanceof Notices) {
            resultList = new ArrayList<>();
            for (Object obj : p.getRecords()) {
                Notices notice = (Notices) obj;
                Map<String, Object> map = new HashMap<>();
                map.put("id", notice.getId());
                map.put("title", notice.getTitle());
                map.put("detail", notice.getDetail());
                map.put("createTime", notice.getCreateTime());
                
                if (notice.getTeamId() == null) {
                    map.put("teamName", "系统管理员");
                } else {
                    Teams team = teamsDao.selectById(notice.getTeamId());
                    map.put("teamName", team != null ? team.getName() : "未知社团");
                }
                
                resultList.add(map);
            }
        } else {
            throw new IllegalArgumentException("Unsupported record type: " + p.getRecords().get(0).getClass());
        }
        
        return new PageData(p.getCurrent(), p.getSize(), p.getTotal(), resultList);
    }
}