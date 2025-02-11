package self.cases.teams.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import self.cases.teams.entity.ActivityComment;
import self.cases.teams.entity.Users;
import self.cases.teams.handle.CacheHandle;
import self.cases.teams.msg.R;
import self.cases.teams.msg.PageData;
import self.cases.teams.service.ActivityCommentService;
import self.cases.teams.service.UsersService;
import self.cases.teams.utils.DateUtils;
import self.cases.teams.utils.IDUtils;

import java.util.Map;

@Controller
@RequestMapping("/activities/comments")
public class ActivityCommentController extends BaseController {

    private static final Logger Log = LoggerFactory.getLogger(ActivityCommentController.class);

    @Autowired
    private ActivityCommentService activityCommentService;
    
    @Autowired
    private UsersService usersService;
    
    @Autowired
    private CacheHandle cacheHandle;

    @PostMapping("/add")
    @ResponseBody
    public R addComment(String token, ActivityComment comment) {
        try {
            Log.info("Adding comment for activity: {}, token: {}", comment.getActivityId(), token);
            Users user = usersService.getOne(cacheHandle.getUserInfoCache(token));
            if (user == null) {
                return R.error("用户未登录或登录已过期");
            }
            
            if (comment == null || comment.getActivityId() == null || comment.getContent() == null) {
                return R.error("评论内容不能为空");
            }
            
            comment.setId(IDUtils.makeIDByCurrent());
            comment.setUserId(user.getId());
            comment.setCreateTime(DateUtils.getNowDate());
            
            Log.info("Saving comment: {}", comment);
            activityCommentService.add(comment);
            return R.successMsg("评论成功");
        } catch (Exception e) {
            Log.error("Error adding comment: ", e);
            return R.error("发表评论失败：" + e.getMessage());
        }
    }

    @GetMapping("/list")
    @ResponseBody
    public R getComments(Long pageIndex, Long pageSize, String activityId) {
        Log.info("Getting comments for activity: {}, page: {}, size: {}", activityId, pageIndex, pageSize);
        try {
            if (activityId == null || pageIndex == null || pageSize == null) {
                return R.error("参数不能为空");
            }
            PageData pageData = activityCommentService.getCommentsByActivityId(pageIndex, pageSize, activityId);
            return R.successData(pageData);
        } catch (Exception e) {
            Log.error("Error getting comments: ", e);
            return R.error("获取评论失败");
        }
    }

    @GetMapping("/analysis/{activityId}")
    @ResponseBody
    public R getCommentAnalysis(@PathVariable String activityId) {
        try {
            Log.info("Analyzing comments for activity: {}", activityId);
            Map<String, Object> analysis = activityCommentService.analyzeComments(activityId);
            Log.info("Analysis result: {}", analysis);
            return R.successData(analysis);
        } catch (Exception e) {
            Log.error("Error analyzing comments: ", e);
            return R.error("评论分析失败");
        }
    }
} 