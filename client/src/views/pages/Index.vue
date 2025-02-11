<template>
	<div class="f-body-show">
		<div class="main-container">
			<!-- 左侧内容 -->
			<div class="left-section">
				<el-row :gutter="15">
					<el-col :span="24">
						<el-card shadow="hover">
							<div>
								<el-descriptions title="个人资料" :column="1" size="small" border>
									<el-descriptions-item>
										<template slot="label">
											用户ID
										</template>
										{{ loginUser.id }}
									</el-descriptions-item>
									<el-descriptions-item>
										<template slot="label">
											用户姓名
										</template>
										{{ loginUser.name }}
									</el-descriptions-item>
									<el-descriptions-item>
										<template slot="label">
											用户性别
										</template>
										{{ loginUser.gender }}
									</el-descriptions-item>
									<el-descriptions-item>
										<template slot="label">
											用户年龄
										</template>
										{{ loginUser.age }}
									</el-descriptions-item>
									<el-descriptions-item>
										<template slot="label">
											联系电话
										</template>
										{{ loginUser.phone }}
									</el-descriptions-item>
									<el-descriptions-item>
										<template slot="label">
											联系地址
										</template>
										{{ loginUser.address }}
									</el-descriptions-item>
								</el-descriptions>
							</div>
						</el-card>
					</el-col>
				</el-row>
				<el-row :gutter="15">
					<el-col :span="24">
						<el-card shadow="hover" class="chart-card">
							<div slot="header" class="card-header">社团类型统计</div>
							<div ref="clubTypeChart" class="chart-container"></div>
						</el-card>
					</el-col>
				</el-row>
			</div>

			<!-- 右侧系统通知 -->
			<div class="right-section">
				<el-card shadow="hover" class="notice-card">
					<div slot="header" class="card-header">系统通知</div>
					<div>
						<el-timeline>
							<el-timeline-item color="#E6A23C" v-for="(item, index) in sysNotices" :key="index"
								:timestamp="item.createTime" placement="top">
								<el-card>
									<h4 class="notice-title">{{ item.title }}</h4>
									<p class="notice-content">{{ item.detail }}</p>
									<div class="notice-footer">
										<span class="notice-source">发布社团：{{ item.teamName }}</span>
									</div>
								</el-card>
							</el-timeline-item>
						</el-timeline>
					</div>
				</el-card>
			</div>
		</div>
	</div>
</template>

<style>
	/* 主容器布局 */
	.main-container {
		display: flex;
		gap: 15px;
		height: 100%;
	}

	/* 卡片标题样式 */
	.card-header {
		font-size: 16px;
		font-weight: bold;
		color: #333;
	}

	/* 左侧区域 */
	.left-section {
		flex: 1;
		display: flex;
		flex-direction: column;
		gap: 20px;
	}

	/* 右侧区域 */
	.right-section {
		width: 600px;
		min-width: 600px;
	}

	/* 系统通知卡片 */
	.notice-card {
		height: 100%;
	}


	/* 图表卡片样式 */
	.chart-card {
		margin-bottom: 15px;
	}

	.chart-container {
		height: 400px;
		width: 100%;
	}

	/* 通知标题样式 */
	.notice-title {
		font-size: 16px;
		line-height: 28px;
		margin: 0 0 15px 0;
		font-weight: bold;
	}

	/* 通知内容样式 */
	.notice-content {
		font-size: 14px;
		line-height: 28px;
		color: #606266;
		margin-bottom: 10px;
	}

	/* 通知底部样式 */
	.notice-footer {
		text-align: right;
	}

	/* 通知来源样式 */
	.notice-source {
		font-size: 12px;
		color: #909399;
	}
</style>

<script>
	import * as echarts from 'echarts'
	import {
		getLoginUser,
		getSysNoticeList,
		getClubTypeStats
	} from "../../api";

	export default {
		data() {
			return {
				loginUser: {},
				sysNotices: [],
				clubTypeChartInstance: null,
				clubTypeData: {
					types: [],
					counts: []
				}
			}
		},
		mounted() {
			getLoginUser(this.$store.state.token).then(resp => {
				this.loginUser = resp.data;
			});

			getSysNoticeList(this.$store.state.token).then(resp => {
				this.sysNotices = resp.data;
			});

			// 获取社团类型统计数据
			this.getClubTypeData();

			// 初始化图表
			this.$nextTick(() => {
				this.initChart();
			});

		},
		methods: {
			// 获取社团类型统计数据
			getClubTypeData() {
				getClubTypeStats(this.$store.state.token).then(resp => {
					if (resp.code === 0) {
						this.clubTypeData = resp.data;
						// 打印获取到的数据
						console.log('从后端获取的社团类型统计数据：', resp.data);
						// 更新图表
						this.updateClubTypeChart();
					}
				});
			},
			initChart() {
				this.clubTypeChartInstance = echarts.init(this.$refs.clubTypeChart);
				this.initClubTypeChart();
			},
			// 更新图表数据
			updateClubTypeChart() {
				if (this.clubTypeChartInstance) {
					const option = this.getChartOption();
					this.clubTypeChartInstance.setOption(option);
				}
			},
			// 获取图表配置
			getChartOption() {
				return {
					tooltip: {
						trigger: 'axis',
						axisPointer: {
							type: 'shadow'
						}
					},
					grid: {
						left: '3%',
						right: '4%',
						bottom: '3%',
						containLabel: true
					},
					xAxis: [{
						type: 'category',
						data: this.clubTypeData.types,
						axisTick: {
							alignWithLabel: true
						}
					}],
					yAxis: [{
						type: 'value',
						name: '社团数量'
					}],
					series: [{
						name: '社团数量',
						type: 'bar',
						barWidth: '60%',
						data: this.clubTypeData.counts,
						itemStyle: {
							color: function(params) {
								// 定义一组好看的颜色
								var colorList = [
									'#00B0FF', 
									'#00E676', 
									'#FFA726', 
									'#FF4081', 
									'#ebeb00', 
									'#dc133c' 
								];
								return colorList[params.dataIndex];
							}
						}
					}]
				};
			},
			initClubTypeChart() {
				const option = this.getChartOption();
				this.clubTypeChartInstance.setOption(option);
			}
		},
		beforeDestroy() {
			if (this.clubTypeChartInstance) {
				this.clubTypeChartInstance.dispose();
			}
		}
	}
</script>