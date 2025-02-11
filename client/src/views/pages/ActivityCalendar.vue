<template>
  <div class="f-body-show">
    <el-card shadow="never">
      <div slot="header">
        <span>我的活动日历</span>
        <el-radio-group v-model="calendarType" style="float: right" size="small" @change="handleTypeChange">
          <el-radio-button label="month">月</el-radio-button>
          <el-radio-button label="week">周</el-radio-button>
          <el-radio-button label="day">日</el-radio-button>
        </el-radio-group>
      </div>
      <div v-if="calendarType === 'month'">
        <el-calendar v-model="date">
          <template slot="dateCell" slot-scope="{date, data}">
            <div class="calendar-date">
              <div class="date-text">{{ data.day.split('-').slice(2).join('') }}</div>
              <div v-for="activity in getActivitiesByDate(date)" :key="activity.id" class="activity-item">
                <el-tooltip :content="activity.name" placement="top">
                  <div class="activity-name" @click="showActivityDetail(activity)">
                    {{ activity.name }}
                  </div>
                </el-tooltip>
              </div>
            </div>
          </template>
        </el-calendar>
      </div>
      <div v-else-if="calendarType === 'week'" class="week-view">
        <div class="week-header">
          <div v-for="day in weekDays" :key="day" class="week-day-header">{{ day }}</div>
        </div>
        <div class="week-body">
          <div v-for="day in currentWeekDays" :key="day.toISOString()" class="week-day">
            <div class="date-text">{{ formatDate(day) }}</div>
            <div v-for="activity in getActivitiesByDate(day)" :key="activity.id" class="activity-item">
              <el-tooltip :content="activity.name" placement="top">
                <div class="activity-name" @click="showActivityDetail(activity)">
                  {{ activity.name }}
                </div>
              </el-tooltip>
            </div>
          </div>
        </div>
      </div>
      <div v-else class="day-view">
        <div class="day-header">
          <div class="date-text">{{ formatDate(date) }}</div>
        </div>
        <div class="day-body">
          <div v-for="activity in getActivitiesByDate(date)" :key="activity.id" class="day-activity-item">
            <div class="activity-time">{{ formatTime(activity.activeTime) }}</div>
            <div class="activity-content" @click="showActivityDetail(activity)">
              <div class="activity-title">{{ activity.name }}</div>
              <div class="activity-team">{{ activity.teamName }}</div>
            </div>
          </div>
          <div v-if="getActivitiesByDate(date).length === 0" class="no-activity">
            当天暂无活动
          </div>
        </div>
      </div>
    </el-card>

    <!-- 活动详情对话框 -->
    <el-dialog title="活动详情" :visible.sync="dialogVisible" width="700px">
      <el-descriptions :column="1" border>
        <el-descriptions-item label="活动名称">{{ currentActivity.name }}</el-descriptions-item>
        <el-descriptions-item label="发布社团">{{ currentActivity.teamName }}</el-descriptions-item>
        <el-descriptions-item label="活动时间">{{ currentActivity.activeTime }}</el-descriptions-item>
        <el-descriptions-item label="活动概述">{{ currentActivity.comm }}</el-descriptions-item>
        <el-descriptions-item label="活动要求">{{ currentActivity.ask }}</el-descriptions-item>
        <el-descriptions-item label="活动详情">{{ currentActivity.detail }}</el-descriptions-item>
      </el-descriptions>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">关闭</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { getPageActivities, getActiveLogs, getLoginUser } from "../../api";

export default {
  data() {
    return {
      date: new Date(),
      calendarType: 'month',
      activities: [],
      dialogVisible: false,
      currentActivity: {},
      isAdmin: false,
      userId: null,
      qryForm: {
        token: this.$store.state.token
      },
      weekDays: ['周日', '周一', '周二', '周三', '周四', '周五', '周六']
    }
  },
  computed: {
    currentWeekDays() {
      const days = [];
      const current = new Date(this.date);
      const first = current.getDate() - current.getDay();
      
      for (let i = 0; i < 7; i++) {
        const day = new Date(current.setDate(first + i));
        days.push(new Date(day));
      }
      return days;
    }
  },
  created() {
    this.fetchActivities();
    this.checkUserType();
  },
  methods: {
    async checkUserType() {
      const resp = await getLoginUser(this.$store.state.token);
      this.isAdmin = resp.data.type === 0;
      this.userId = resp.data.id;
    },
    async fetchActivities() {
      try {
        if (!this.userId) {
          const userResp = await getLoginUser(this.$store.state.token);
          this.userId = userResp.data.id;
          this.isAdmin = userResp.data.type === 0;
        }

        const activitiesResp = await getPageActivities(1, 1000, this.$store.state.token, "", "");
        
        if (activitiesResp.data && activitiesResp.data.data) {
          if (this.isAdmin) {
            this.activities = activitiesResp.data.data;
          } else {
            const myActivities = [];
            for (const activity of activitiesResp.data.data) {
              const logsResp = await getActiveLogs(activity.id);
              
              if (logsResp.data) {
                const userLogs = logsResp.data.filter(log => {
                  return String(log.userId || log.user_id) === String(this.userId);
                });
                if (userLogs.length > 0) {
                  myActivities.push(activity);
                }
              }
            }
            this.activities = myActivities;
          }
        }
      } catch (error) {
        console.error('获取活动列表失败:', error);
        this.$message.error('获取活动列表失败');
      }
    },
    getActivitiesByDate(date) {
      return this.activities.filter(activity => {
        const activityDate = new Date(activity.activeTime);
        return activityDate.toDateString() === date.toDateString();
      });
    },
    showActivityDetail(activity) {
      this.currentActivity = activity;
      this.dialogVisible = true;
    },
    handleTypeChange(type) {
      this.calendarType = type;
    },
    formatDate(date) {
      return new Date(date).getDate() + '日';
    },
    formatTime(dateStr) {
      return new Date(dateStr).toLocaleTimeString('zh-CN', { 
        hour: '2-digit', 
        minute: '2-digit' 
      });
    }
  }
}
</script>

<style scoped>
.calendar-date {
  height: 100%;
  padding: 4px;
}

.date-text {
  font-size: 14px;
  margin-bottom: 4px;
}

.activity-item {
  margin-bottom: 2px;
}

.activity-name {
  font-size: 12px;
  color: #409EFF;
  cursor: pointer;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  padding: 2px 4px;
  background-color: #ecf5ff;
  border-radius: 2px;
}

.activity-name:hover {
  color: #66b1ff;
  background-color: #d9ecff;
}

.el-calendar-day {
  height: 100px !important;
  padding: 4px !important;
}

.el-calendar-table td {
  border: 1px solid #ebeef5;
}

.week-view, .day-view {
  margin-top: 20px;
}

.week-header, .week-body {
  display: flex;
  width: 100%;
}

.week-day-header {
  flex: 1;
  text-align: center;
  padding: 10px;
  background-color: #f5f7fa;
  border: 1px solid #ebeef5;
  font-weight: bold;
}

.week-day {
  flex: 1;
  min-height: 150px;
  padding: 10px;
  border: 1px solid #ebeef5;
}

.day-view {
  border: 1px solid #ebeef5;
  border-radius: 4px;
}

.day-header {
  padding: 15px;
  background-color: #f5f7fa;
  border-bottom: 1px solid #ebeef5;
}

.day-body {
  padding: 20px;
  min-height: 400px;
}

.day-activity-item {
  display: flex;
  margin-bottom: 15px;
  padding: 10px;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  cursor: pointer;
}

.day-activity-item:hover {
  background-color: #f5f7fa;
}

.activity-time {
  width: 100px;
  color: #606266;
}

.activity-content {
  flex: 1;
}

.activity-title {
  font-weight: bold;
  color: #303133;
}

.activity-team {
  font-size: 12px;
  color: #909399;
  margin-top: 5px;
}

.no-activity {
  text-align: center;
  color: #909399;
  padding: 40px 0;
}
</style> 