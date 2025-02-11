<template>
	<div class="f-body-show">
		<el-card shadow="never">
			<div slot="header">
				信息查询
			</div>
			<div>
				<el-form :inline="true" :model="qryForm">
					<el-form-item>
						<el-input v-model="qryForm.activeName"
							placeholder="输入活动名称..."
							autocomplete="off"></el-input>
					</el-form-item>
					<el-form-item>
						<el-input v-model="qryForm.teamName"
							placeholder="输入发布社团..."
							autocomplete="off"></el-input>
					</el-form-item>
					<el-form-item>
						<el-button type="primary" 
							icon="el-icon-search" @click="getPageLikeInfo()"></el-button>
					</el-form-item>
				</el-form>
			</div>
		</el-card>

		<el-card shadow="never">
			<div v-if="userType == 1" slot="header">
				<el-button type="primary" size="mini" 
						icon="el-icon-plus" @click="showAddWin()"></el-button>
			</div>
			<div>
				<el-table v-loading="loading" element-loading-text="拼命加载中" element-loading-spinner="el-icon-loading"
					element-loading-background="rgba(124, 124, 124, 0.8)" :data="pageInfos" border>
					<el-table-column align="center" type="index"></el-table-column>
					<el-table-column align="center" prop="name" label="活动名称"></el-table-column>
					<el-table-column align="center" prop="teamName" label="发布社团"></el-table-column>
					<el-table-column align="center" prop="activeTime" label="活动时间"></el-table-column>
					<el-table-column align="center" prop="total" label="报名人数"></el-table-column>
					<el-table-column align="center" label="参与人员">
						<template slot-scope="scope">
							<el-popover title="活动参与人员" trigger="click" 
								@show="getActivePeople(scope.row.id)" :width="550" placement="left">
								<el-table :data="activeLogs" border>
									<el-table-column align="center" type="index"></el-table-column>
									<el-table-column align="center" prop="userName" label="参与人员"></el-table-column>
									<el-table-column align="center" prop="userPhone" label="联系电话"></el-table-column>
									<el-table-column align="center" prop="createTime" label="报名时间"></el-table-column>
								</el-table>
								<el-button type="text" size="mini" slot="reference">参与人员</el-button>
							</el-popover>
						</template>
					</el-table-column>
					<el-table-column v-if="userType == 0" align="center" label="操作处理">
						<template slot-scope="scope">
							<el-button icon="el-icon-delete" type="danger" size="mini"
								@click="delInfo(scope.row.id)"></el-button>
						</template>
					</el-table-column>
					<el-table-column v-else align="center" label="了解更多">
						<template slot-scope="scope">
							<el-button type="text" size="mini" @click="showDetailWin(scope.row)">查看详情</el-button>
						</template>
					</el-table-column>
				</el-table>
				<el-pagination v-if="pageTotal > 1" style="margin-top: 15px;" @size-change="handleSizeChange"
					@current-change="handleCurrentChange" :current-page="pageIndex" :page-sizes="[5, 10, 20, 50]"
					:page-size="pageSize" layout="total, sizes, prev, pager, next, jumper" :total="totalInfo">
				</el-pagination>
			</div>
		</el-card>

		<el-dialog title="添加信息" :fullscreen="true" :visible.sync="showAddFlag">
			<el-form label-width="90px" :model="activitiesForm">
				<el-form-item label="活动名称">
					<el-input v-model="activitiesForm.name" 
						placeholder="请输入活动名称…" autocomplete="off"></el-input>
				</el-form-item>
				<el-form-item label="活动时间">
					<el-date-picker style="width:100%;" value-format="yyyy-MM-dd HH:mm:ss" 
							v-model="activitiesForm.activeTime" 
							type="datetime" placeholder="选择活动时间…"></el-date-picker>
				</el-form-item>
				<el-form-item label="发布社团">
					<el-select style="width:100%;" v-model="activitiesForm.teamId" placeholder="请选择发布社团…">
						<el-option v-for="(item, index) in teams" 
							:key="index" :label="item.name" :value="item.id"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="活动概述">
					<el-input type="textarea" rows="6" v-model="activitiesForm.comm" 
						placeholder="请输入活动概述…" autocomplete="off"></el-input>
				</el-form-item>
				<el-form-item label="活动要求">
					<el-input type="textarea" rows="6" v-model="activitiesForm.ask" 
						placeholder="请输入活动要求…" autocomplete="off"></el-input>
				</el-form-item>
				<el-form-item label="活动详情">
					<el-input type="textarea" rows="10" v-model="activitiesForm.detail" 
						placeholder="请输入活动详情…" autocomplete="off"></el-input>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="showAddFlag = false">取 消</el-button>
				<el-button type="primary" @click="addInfo()">确 定</el-button>
			</div>
		</el-dialog>

		<el-dialog title="活动详情" :visible.sync="showDetailFlag" width="1200px" :fullscreen="false">
			<div class="detail-container">
				<!-- 左侧活动信息 -->
				<div class="activity-info">
					<el-descriptions :column="1" size="small" border>
						<el-descriptions-item>
							<template slot="label">活动名称</template>
							{{ currentActivity.name }}
						</el-descriptions-item>
						<el-descriptions-item>
							<template slot="label">发布社团</template>
							{{ currentActivity.teamName }}
						</el-descriptions-item>
						<el-descriptions-item>
							<template slot="label">活动时间</template>
							{{ currentActivity.activeTime }}
						</el-descriptions-item>
						<el-descriptions-item>
							<template slot="label">活动概述</template>
							{{ currentActivity.comm }}
						</el-descriptions-item>
						<el-descriptions-item>
							<template slot="label">活动要求</template>
							{{ currentActivity.ask }}
						</el-descriptions-item>
						<el-descriptions-item>
							<template slot="label">活动详情</template>
							{{ currentActivity.detail }}
						</el-descriptions-item>
					</el-descriptions>
					
					<div style="text-align: center; margin: 20px 0;">
						<el-button type="primary" @click="active(currentActivity.id)">
							我要报名
						</el-button>
					</div>
					
					<!-- 评论分析卡片只对社长(userType==1)显示 -->
					<el-card v-if="userType === 1" class="comment-analysis-card" shadow="never">
						<div class="analysis-stats">
							<div class="stat-item positive">
								<div class="stat-value">{{ commentAnalysis.positive }}</div>
								<div class="stat-label">积极评论</div>
							</div>
							<div class="stat-item neutral">
								<div class="stat-value">{{ commentAnalysis.neutral }}</div>
								<div class="stat-label">中性评论</div>
							</div>
							<div class="stat-item negative">
								<div class="stat-value">{{ commentAnalysis.negative }}</div>
								<div class="stat-label">消极评论</div>
							</div>
						</div>
					</el-card>
				</div>

				<!-- 右侧评论区域 -->
				<div class="comment-section" style="flex: 1; margin-left: 30px; border-left: 1px solid #eee; padding-left: 30px; display: flex; flex-direction: column;">
					<div class="comment-header">
						<h3>活动评论</h3>
					</div>
					
					<!-- 评论列表区域 -->
					<div class="comment-list-container" style="flex: 1; overflow-y: auto;">
						<div class="comment-list">
							<div v-for="item in comments" :key="item.id" class="comment-item">
								<div class="comment-header">
									<span class="username">{{item.userName}}</span>
									<span class="time">{{item.createTime}}</span>
								</div>
								<div class="comment-content">{{item.content}}</div>
							</div>
						</div>
						
						<!-- 分页 -->
						<el-pagination
							v-if="commentTotal > 0"
							@size-change="handleCommentSizeChange"
							@current-change="handleCommentCurrentChange"
							:current-page="commentPage"
							:page-sizes="[5, 10, 20]"
							:page-size="commentSize"
							layout="total, sizes, prev, pager, next"
							:total="commentTotal">
						</el-pagination>
					</div>

					<!-- 评论输入框 -->
					<div class="comment-form">
						<el-input
							type="textarea"
							:rows="3"
							placeholder="请输入您的评论..."
							v-model="newComment">
						</el-input>
						<el-button 
							type="primary"
							size="small"
							style="margin-top: 10px"
							@click="submitComment">
							发表评论
						</el-button>
					</div>
				</div>
			</div>
		</el-dialog>
	</div>
</template>

<style>
.comment-section {
	margin-top: 20px;
	padding-top: 20px;
	border-top: 1px solid #eee;
}

.comment-header h3 {
	margin: 0 0 15px 0;
	color: #303133;
}

.comment-list {
	margin-bottom: 20px;
}

.comment-item {
	padding: 10px 0;
	border-bottom: 1px solid #eee;
}

.comment-header {
	margin-bottom: 5px;
	padding-top: 0;
}

.comment-header .username {
	font-weight: bold;
	margin-right: 10px;
}

.comment-header .time {
	color: #999;
	font-size: 12px;
}

.comment-content {
	line-height: 1.5;
}

.comment-form {
	flex: 0 0 auto;
	padding-top: 20px;
	background: #fff;
	border-top: 1px solid #eee;
	margin-top: 10px;
}

.detail-container {
	position: relative;
	height: 70vh;
	display: flex;
	flex-direction: row;
}

.activity-info {
	flex: 0 0 500px;
	display: flex;
	flex-direction: column;
	justify-content: center;
}

.comment-section {
	flex: 1;
	margin-left: 30px;
	border-left: 1px solid #eee;
	padding-left: 30px;
	display: flex;
	flex-direction: column;
}

.comment-list-container {
	flex: 1;
	overflow-y: auto;
	padding-right: 10px;
}

.comment-list {
	margin-bottom: 20px;
}

.comment-header h3 {
	margin: 0 0 15px 0;
	color: #303133;
}

/* 自定义滚动条样式 */
.comment-list-container::-webkit-scrollbar {
	width: 6px;
}

.comment-list-container::-webkit-scrollbar-thumb {
	background-color: #ddd;
	border-radius: 3px;
}

.comment-list-container::-webkit-scrollbar-track {
	background-color: #f5f5f5;
}

.comment-analysis-card {
	margin: 15px 0;
	background-color: #f8f9fa;
}

.analysis-stats {
	display: flex;
	justify-content: space-around;
	padding: 10px 0;
}

.stat-item {
	text-align: center;
	padding: 10px 20px;
	border-radius: 4px;
	min-width: 120px;
}

.stat-value {
	font-size: 24px;
	font-weight: bold;
	margin-bottom: 5px;
}

.stat-label {
	font-size: 14px;
	color: #606266;
}

.positive {
	background-color: #f0f9eb;
	color: #67c23a;
}

.neutral {
	background-color: #f4f4f5;
	color: #909399;
}

.negative {
	background-color: #fef0f0;
	color: #f56c6c;
}
</style>

<script>
	import {
		addActiveLogs,
		getManTeamList,
		getLoginUser,
		getActiveLogs,
		getPageActivities,
		addActivities,
		delActivities,
		getActivityComments,
		addActivityComment,
		getActivityCommentAnalysis
	} from "../../api";

	export default {

		data() {

			return {
				teams: [],
				userType: "",
				activeLogs: [],
				pageInfos: [],
				pageIndex: 1,
				pageSize: 10,
				pageTotal: 0,
				totalInfo: 0,
				loading: true,
				showAddFlag: false,
				showDetailFlag: false,
				qryForm: {
					token: this.$store.state.token,
					teamName: "",
					activeName: "",
				},
				activitiesForm: {
					id: "",
					name: "",
					comm: "",
					detail: "",
					ask: "",
					total: 1,
					activeTime: "",
					teamId: "",
				},
				activeTab: 'info',
				comments: [],
				commentPage: 1,
				commentSize: 10,
				commentTotal: 0,
				newComment: '',
				currentActivityId: '',
				currentActivity: {
					id: '',
					name: '',
					teamName: '',
					activeTime: '',
					comm: '',
					ask: '',
					detail: ''
				},
				commentAnalysis: {
					positive: 0,
					neutral: 0,
					negative: 0,
					total: 0
				}
			}
		},
		methods: {

			getActivePeople(activeId){

				getActiveLogs(activeId).then(resp =>{
					
					this.activeLogs = [];
					this.activeLogs = resp.data;
				});
			},

			getPageInfo(pageIndex, pageSize) {
				getPageActivities(pageIndex, pageSize, this.qryForm.token, this.qryForm.teamName, this.qryForm.activeName).then(resp => {
					this.pageInfos = resp.data.data;
					this.pageIndex = resp.data.pageIndex;
					this.pageSize = resp.data.pageSize;
					this.pageTotal = resp.data.pageTotal;
					this.totalInfo = resp.data.count;
					this.loading = false;
				});
			},
			getPageLikeInfo() {
				getPageActivities(1, this.pageSize, this.qryForm.token, this.qryForm.teamName, this.qryForm.activeName).then(resp => {
					this.pageInfos = resp.data.data;
					this.pageIndex = resp.data.pageIndex;
					this.pageSize = resp.data.pageSize;
					this.totalInfo = resp.data.count;
					this.pageTotal = resp.data.pageTotal;
					this.loading = false;
				});
			},
			handleSizeChange(pageSize) {
				this.getPageInfo(this.pageIndex, pageSize);
			},
			handleCurrentChange(pageIndex) {
				this.getPageInfo(pageIndex, this.pageSize);
			},
			initForm() {

				this.activitiesForm = {
					id: "",
					name: "",
					comm: "",
					detail: "",
					ask: "",
					total: 1,
					activeTime: "",
					teamId: "",
				};
			},
			showAddWin() {

				this.initForm();
				this.showAddFlag = true;
			},
			active(id){

				addActiveLogs({token: this.$store.state.token, activeId: id}).then(resp =>{
					
					if(resp.code == 0){

						this.$message({
							message: resp.msg,
							type: 'success'
						});
					}else{

						this.$message({
							message: resp.msg,
							type: 'warning'
						});
					}
				});
			},
			addInfo() {

				addActivities(this.activitiesForm).then(resp => {

					this.$message({
						message: resp.msg,
						type: 'success'
					});

					this.getPageInfo(1, this.pageSize, this.qryForm.token);

					this.showAddFlag = false;

				});
			},
			delInfo(id){
				this.$confirm('删除活动将同时删除报名记录, 是否继续?', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {

					delActivities(id).then(resp => {

						this.$message({
							message: resp.msg,
							type: 'success'
						});

						this.getPageInfo(1, this.pageSize, this.qryForm.token);
					});
				});
			},
			async showDetailWin(row) {
				console.log('Opening detail for activity:', row.id);
				this.currentActivity = {...row};
				this.currentActivityId = row.id;
				this.showDetailFlag = true;
				await this.loadComments();
				await this.loadCommentAnalysis();
			},
			async loadComments() {
				getActivityComments(this.commentPage, this.commentSize, this.currentActivityId).then(resp => {
					console.log('Comments response:', resp);
					if(resp.code === 0) {
						this.comments = resp.data.data;
						this.commentTotal = resp.data.count;
					} else {
						this.$message.error(resp.msg || '加载评论失败');
					}
				}).catch(error => {
					console.error('Failed to load comments:', error);
					this.$message.error('加载评论失败');
				});
			},
			handleCommentSizeChange(val) {
				this.commentSize = val;
				this.loadComments();
			},
			handleCommentCurrentChange(val) {
				this.commentPage = val;
				this.loadComments();
			},
			submitComment() {
				if (!this.newComment.trim()) {
					this.$message.warning('请输入评论内容');
					return;
				}
				
				const commentData = {
					activityId: this.currentActivityId,
					content: this.newComment.trim()
				};
				
				addActivityComment(this.$store.state.token, commentData)
				.then(resp => {
					if (resp.code === 0) {
						this.$message.success('评论成功');
						this.newComment = '';
						this.loadComments();
					} else {
						this.$message.error(resp.msg || '发表评论失败');
					}
				}).catch(error => {
					console.error('Failed to submit comment:', error);
					this.$message.error('发表评论失败');
				});
			},
			async loadCommentAnalysis() {
				try {
					console.log('Loading analysis for activity:', this.currentActivity.id);
					const response = await getActivityCommentAnalysis(this.currentActivity.id);
					console.log('Analysis response:', response);
					if (response.code === 0) {
						this.commentAnalysis = response.data;
					}
				} catch (error) {
					console.error('Failed to load comment analysis:', error);
					this.$message.error('获取评论分析失败');
				}
			}
		},
		mounted() {

			this.getPageInfo(1, this.pageSize, this.qryForm.token);

			getLoginUser(this.$store.state.token).then(resp =>{

				this.userType = resp.data.type;

				getManTeamList(resp.data.id).then(resp =>{

					this.teams = resp.data;
				});
			});
		}
	}
</script>