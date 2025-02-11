package self.cases.teams.service;

import self.cases.teams.entity.ActivityComment;
import self.cases.teams.msg.PageData;
import java.util.Map;

public interface ActivityCommentService {
    
    void add(ActivityComment comment);
    
    void delete(String id);
    
    PageData getCommentsByActivityId(Long pageIndex, Long pageSize, String activityId);
    
    Map<String, Object> analyzeComments(String activityId);
} 