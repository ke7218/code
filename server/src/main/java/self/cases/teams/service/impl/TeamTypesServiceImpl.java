package self.cases.teams.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import self.cases.teams.dao.TeamsDao;
import self.cases.teams.entity.Teams;
import self.cases.teams.msg.PageData;
import self.cases.teams.entity.TeamTypes;
import self.cases.teams.dao.TeamTypesDao;
import self.cases.teams.service.TeamTypesService;
import self.cases.teams.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("teamTypesService")
public class TeamTypesServiceImpl implements TeamTypesService {

    @Autowired
    private TeamTypesDao teamTypesDao;

    @Autowired
    private TeamsDao teamsDao;

    @Override
    @Transactional
    public void add(TeamTypes teamTypes) {

        teamTypesDao.insert(teamTypes);
    }

    @Override
    @Transactional
    public void update(TeamTypes teamTypes) {

        teamTypesDao.updateById(teamTypes);
    }

    @Override
    @Transactional
    public void delete(TeamTypes teamTypes) {

        teamTypesDao.deleteById(teamTypes);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public Boolean isRemove(String typeId){

        QueryWrapper<Teams> qw = new QueryWrapper<Teams>();
        qw.eq("type_id", typeId);

        return teamsDao.selectCount(qw) <= 0;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public TeamTypes getOne(String id) {

        QueryWrapper<TeamTypes> qw = new QueryWrapper<TeamTypes>();
        qw.orderByDesc("create_time");

        TeamTypes teamTypes = teamTypesDao.selectById(id);

        return teamTypes;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<TeamTypes> getAll() {

        List<TeamTypes> list = teamTypesDao.selectList(null);

        return list;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public PageData getPageInfo(Long pageIndex, Long pageSize, TeamTypes teamTypes) {

        QueryWrapper<TeamTypes> qw = new QueryWrapper<TeamTypes>();

        if (StringUtils.isNotNullOrEmpty(teamTypes.getName())) {

            qw.like("name", teamTypes.getName());
        }

        qw.orderByDesc("create_time");

        Page<TeamTypes> page =
                teamTypesDao.selectPage(new Page<TeamTypes>(pageIndex, pageSize), qw);

        return parsePage(page);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public Map<String, Object> getTypeStats() {
        // 获取所有社团类型
        List<TeamTypes> types = teamTypesDao.selectList(null);
        
        // 准备数据
        List<String> typeNames = new ArrayList<>();
        List<Integer> typeCounts = new ArrayList<>();
        
        // 统计每种类型的社团数量
        for (TeamTypes type : types) {
            typeNames.add(type.getName());
            
            // 查询该类型的社团数量
            QueryWrapper<Teams> qw = new QueryWrapper<>();
            qw.eq("type_id", type.getId());
            int count = teamsDao.selectCount(qw);
            
            typeCounts.add(count);
        }
        
        // 封装结果
        Map<String, Object> result = new HashMap<>();
        result.put("types", typeNames);
        result.put("counts", typeCounts);
        
        return result;
    }

    /**
     * 转化分页查询的结果
     */
    public PageData parsePage(Page<TeamTypes> p) {

        List<Map<String, Object>> resl = new ArrayList<Map<String, Object>>();

        for (TeamTypes teamTypes : p.getRecords()) {

            Map<String, Object> temp = new HashMap<String, Object>();
            temp.put("id", teamTypes.getId());
            temp.put("name", teamTypes.getName());
            temp.put("createTime", teamTypes.getCreateTime());
            resl.add(temp);
        }

        PageData pageData = new PageData(p.getCurrent(), p.getSize(), p.getTotal(), resl);

        return pageData;
    }
}