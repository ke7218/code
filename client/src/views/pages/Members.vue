<template>
	<div class="f-body-show">
		<el-card shadow="never">
			<div slot="header">
				信息查询
			</div>
			<div>
				<el-form :inline="true" :model="qryForm">
					<el-form-item>
						<el-select v-model="qryForm.teamName" placeholder="请选择社团" clearable>
							<el-option label="查看全部" value=""></el-option>
							<el-option v-for="(item, index) in teams" 
								:key="index" :label="item.name" :value="item.name">
							</el-option>
						</el-select>
					</el-form-item>
					<el-form-item>
						<el-input v-model="qryForm.userName"
							placeholder="输入用户姓名…"
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
			<div>
				<el-table v-loading="loading" element-loading-text="拼命加载中" element-loading-spinner="el-icon-loading"
					element-loading-background="rgba(124, 124, 124, 0.8)" :data="pageInfos" border>
					<el-table-column align="center" type="index"></el-table-column>
					<el-table-column align="center" prop="userId" label="成员ID"></el-table-column>
					<el-table-column align="center" prop="userName" label="成员姓名"></el-table-column>
					<el-table-column align="center" prop="userGender" label="成员性别"></el-table-column>
					<el-table-column align="center" prop="userAge" label="成员年龄"></el-table-column>
					<el-table-column align="center" prop="userPhone" label="成员电话"></el-table-column>
					<el-table-column align="center" prop="teamName" label="社团名称"></el-table-column>
					<el-table-column align="center" prop="createTime" label="入团时间"></el-table-column>
					<el-table-column align="center" label="操作处理">
						<template slot-scope="scope">
							<el-button type="danger" size="mini"
								@click="delInfo(scope.row.id)">移除</el-button>
						</template>
					</el-table-column>
				</el-table>
				<el-pagination v-if="pageTotal > 1" style="margin-top: 15px;" @size-change="handleSizeChange"
					@current-change="handleCurrentChange" :current-page="pageIndex" :page-sizes="[5, 10, 20, 50]"
					:page-size="pageSize" layout="total, sizes, prev, pager, next, jumper" :total="totalInfo">
				</el-pagination>
			</div>
		</el-card>
	</div>
</template>

<style>

</style>

<script>
	import {
		getPageMembers,
		delMembers,
		getAllTeamList,
		getManTeamList,
		getLoginUser
	} from "../../api";

	export default {

		data() {

			return {
				pageInfos: [],
				pageIndex: 1,
				pageSize: 10,
				pageTotal: 0,
				totalInfo: 0,
				loading: true,
				teams: [],
				isAdmin: false,
				qryForm: {
					token: this.$store.state.token,
					teamName: "",
					userName: "",
				}
			}
		},
		methods: {
			async checkUserType() {
				try {
					const resp = await getLoginUser(this.$store.state.token);
					this.isAdmin = resp.data.type === 0;
					return resp.data;
				} catch (error) {
					console.error('获取用户信息失败:', error);
					return null;
				}
			},
			async fetchTeamList() {
				try {
					const userInfo = await this.checkUserType();
					if (!userInfo) return;

					let resp;
					if (this.isAdmin) {
						// 超级管理员可以看到所有社团
						resp = await getAllTeamList();
					} else {
						// 社团管理员只能看到自己管理的社团
						resp = await getManTeamList(userInfo.id);
					}

					if (resp.code === 0) {
						this.teams = resp.data;
					}
				} catch (error) {
					console.error('获取社团列表失败:', error);
					this.$message.error('获取社团列表失败');
				}
			},
			getPageInfo(pageIndex, pageSize) {

				getPageMembers(pageIndex, pageSize, this.qryForm.token).then(resp => {

					this.pageInfos = resp.data.data;
					this.pageIndex = resp.data.pageIndex;
					this.pageSize = resp.data.pageSize;
					this.pageTotal = resp.data.pageTotal;
					this.totalInfo = resp.data.count;

					this.loading = false;
				});
			},
			getPageLikeInfo() {

				getPageMembers(1, this.pageSize, this.qryForm.token, this.qryForm.teamName, this.qryForm.userName).then(resp => {

					this.pageInfos = resp.data.data;
					this.pageIndex = resp.data.pageIndex;
					this.pageSize = resp.data.pageSize;
					this.totalInfo = resp.data.count;
					this.pageTotal = resp.data.pageTotal;
					this.loading = false;
				});
			},
			handleSizeChange(pageSize) {

				this.getPageInfo(this.pageIndex, pageSize, this.qryForm.token, this.qryForm.teamName, this.qryForm.userName);
			},
			handleCurrentChange(pageIndex) {

				this.getPageInfo(pageIndex, this.pageSize, this.qryForm.token, this.qryForm.teamName, this.qryForm.userName);
			},
			delInfo(id){
				this.$confirm('移除成员将移除相关记录，确认继续吗?', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {

					delMembers(id).then(resp => {

						if(resp.code == 0){
							
							this.$message({
								message: resp.msg,
								type: 'success'
							});

							this.getPageInfo(1, this.pageSize, this.qryForm.token);
						}else{

							this.$message({
								message: resp.msg,
								type: 'warning'
							});
						}
					});
				});
			}
		},
		async mounted() {
			await this.fetchTeamList();
			this.getPageInfo(1, this.pageSize, this.qryForm.token);
		}
	}
</script>