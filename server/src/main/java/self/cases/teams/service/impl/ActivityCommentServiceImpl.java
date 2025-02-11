package self.cases.teams.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import self.cases.teams.dao.ActivityCommentDao;
import self.cases.teams.entity.ActivityComment;
import self.cases.teams.msg.PageData;
import self.cases.teams.service.ActivityCommentService;
import self.cases.teams.utils.SentimentAnalyzer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("activityCommentService")
public class ActivityCommentServiceImpl implements ActivityCommentService {

    private static final Logger Log = LoggerFactory.getLogger(ActivityCommentServiceImpl.class);

    @Autowired
    private ActivityCommentDao activityCommentDao;
    
    @Override
    public void add(ActivityComment comment) {
        try {
            Log.info("Adding comment to database: {}", comment);
            // 在保存评论前进行情感分析
            String sentiment = SentimentAnalyzer.analyzeSentiment(comment.getContent());
            comment.setSentiment(sentiment);
            activityCommentDao.insert(comment);
        } catch (Exception e) {
            Log.error("Error in add comment: ", e);
            throw new RuntimeException("添加评论失败", e);
        }
    }
    
    @Override
    public void delete(String id) {
        activityCommentDao.deleteById(id);
    }
    
    @Override
    public PageData getCommentsByActivityId(Long pageIndex, Long pageSize, String activityId) {
        try {
            Page<Map<String, Object>> page = new Page<>(pageIndex, pageSize);
            page = activityCommentDao.getCommentsByActivityId(page, activityId);
            return new PageData(page);
        } catch (Exception e) {
            Log.error("Error in getCommentsByActivityId: ", e);
            throw new RuntimeException("获取评论列表失败", e);
        }
    }

    @Override
    public Map<String, Object> analyzeComments(String activityId) {
        List<ActivityComment> comments = activityCommentDao.getCommentsByActivityIdForAnalysis(activityId);

        int positive = 0, negative = 0, neutral = 0;
        
        for (ActivityComment comment : comments) {
            switch (comment.getSentiment()) {
                case "POSITIVE":
                    positive++;
                    break;
                case "NEGATIVE":
                    negative++;
                    break;
                case "NEUTRAL":
                default:
                    neutral++;
                    break;
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("positive", positive);
        result.put("negative", negative);
        result.put("neutral", neutral);
        result.put("total", comments.size());
        
        return result;
    }
} 