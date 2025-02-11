package self.cases.teams.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("activity_comments")
public class ActivityComment {
    private String id;
    
    @TableField("activity_id")
    private String activityId;
    
    @TableField("user_id")
    private String userId;
    
    private String content;
    
    @TableField("create_time")
    private String createTime;
    
    private String sentiment; // 情感分析结果: POSITIVE/NEGATIVE/NEUTRAL
    
    // 非数据库字段
    @TableField(exist = false)
    private String userName;
} 