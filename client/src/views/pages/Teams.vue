<template>
	<div class="f-body-show">
		<el-card shadow="never">
			<div slot="header">
				信息查询
			</div>
			<div>
				<el-form :inline="true" :model="qryForm">
					<el-form-item >
						<el-input v-model="qryForm.name"
							placeholder="输入社团名称…"
							autocomplete="off"></el-input>
					</el-form-item>
					<el-form-item >
						<el-select v-model="qryForm.typeId" placeholder="请选择社团类型">
							<el-option label="查看全部" value=""></el-option>
							<el-option v-for="(item, index) in teamTypes" 
								:key="index" :label="item.name" :value="item.id"></el-option>
						</el-select>
					</el-form-item>
					<el-form-item>
						<el-button type="primary" 
							icon="el-icon-search" @click="getPageLikeInfo()"></el-button>
					</el-form-item>
				</el-form>
			</div>
		</el-card>

		<el-card shadow="never">
			<div v-if="userType == 0" slot="header">
				<el-button type="primary" size="mini" 
						icon="el-icon-plus" @click="showAddWin()"></el-button>
			</div>
			<div>
				<el-table v-loading="loading" element-loading-text="拼命加载中" element-loading-spinner="el-icon-loading"
					element-loading-background="rgba(124, 124, 124, 0.8)" :data="pageInfos" border>
					<el-table-column align="center" type="index"></el-table-column>
					<el-table-column align="center" prop="name" label="社团名称"></el-table-column>
					<el-table-column align="center" prop="typeName" label="社团类型"></el-table-column>
					<el-table-column align="center" prop="managerName" label="社团团长"></el-table-column>
					<el-table-column align="center" prop="createTime" label="建立时间"></el-table-column>
					<el-table-column align="center" prop="total" label="社团人数"></el-table-column>
					<el-table-column v-if="userType == 0" align="center" label="操作处理">
						<template slot-scope="scope">
							<el-button icon="el-icon-edit"
								type="primary" size="mini" @click="showUpdWin(scope.row)"></el-button>
							<el-button icon="el-icon-delete" type="danger" size="mini"
								@click="delInfo(scope.row.id)"></el-button>
						</template>
					</el-table-column>
					<el-table-column v-if="userType == 2" align="center" label="操作处理">
						<template slot-scope="scope">
							<el-button v-if="!isJoinedTeam(scope.row.id)" type="primary" size="mini"
								@click="applyTeam(scope.row.id)">申请加入</el-button>
							<el-tag v-else type="success">已加入</el-tag>
						</template>
					</el-table-column>
				</el-table>
				<el-pagination v-if="pageTotal > 1" style="margin-top: 15px;" @size-change="handleSizeChange"
					@current-change="handleCurrentChange" :current-page="pageIndex" :page-sizes="[5, 10, 20, 50]"
					:page-size="pageSize" layout="total, sizes, prev, pager, next, jumper" :total="totalInfo">
				</el-pagination>
			</div>
		</el-card>

		<el-dialog title="添加信息" width="600px" :visible.sync="showAddFlag">
			<el-form label-width="90px" :model="teamsForm">
				<el-form-item label="社团名称">
					<el-input v-model="teamsForm.name" 
						placeholder="请输入社团名称…" autocomplete="off"></el-input>
				</el-form-item>
				<el-form-item label="社团类型">
					<el-select style="width:100%;" v-model="teamsForm.typeId" placeholder="请选择社团类型">
						<el-option v-for="(item, index) in teamTypes" 
							:key="index" :label="item.name" :value="item.id"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="社团团长">
					<el-input v-model="teamsForm.manager" 
						placeholder="请输入社团管理员ID…" autocomplete="off"></el-input>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="showAddFlag = false">取 消</el-button>
				<el-button type="primary" @click="addInfo()">确 定</el-button>
			</div>
		</el-dialog>

		<el-dialog title="修改信息" width="600px" :visible.sync="showUpdFlag">
			<el-form label-width="90px" :model="teamsForm">
				<el-form-item label="社团名称">
					<el-input v-model="teamsForm.name" 
						placeholder="请输入社团名称…" autocomplete="off"></el-input>
				</el-form-item>
				<el-form-item label="社团类型">
					<el-select style="width:100%;" v-model="teamsForm.typeId" placeholder="请选择社团类型">
						<el-option v-for="(item, index) in teamTypes" 
							:key="index" :label="item.name" :value="item.id"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="社团团长">
					<el-input v-model="teamsForm.manager" 
						placeholder="请输入社团管理员ID…" autocomplete="off"></el-input>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="showUpdFlag = false">取 消</el-button>
				<el-button type="primary" @click="updInfo()">确 定</el-button>
			</div>
		</el-dialog>
	</div>
</template>

<style>

</style>

<script>
	import {
		getAllTypes,
		getPageTeams,
		getLoginUser,
		addTeams,
		updTeams,
		delTeams,
		addApplyLogs,
		getMemTeamList
	} from "../../api";

	export default {

		data() {

			return {
				teamTypes: [],
				userType: 0,
				pageInfos: [],
				pageIndex: 1,
				pageSize: 10,
				pageTotal: 0,
				totalInfo: 0,
				loading: true,
				showAddFlag: false,
				showUpdFlag: false,
				qryForm: {
					name: "",
					typeId: "",
					token: this.$store.state.token,
				},
				teamsForm: {
					id: "",
					name: "",
					total: 1,
					manager: "",
					typeId: "",
				},
				joinedTeams: []
			}
		},
		methods: {

			getPageInfo(pageIndex, pageSize) {

				getPageTeams(pageIndex, pageSize, this.qryForm.token).then(resp => {

					this.pageInfos = resp.data.data;
					this.pageIndex = resp.data.pageIndex;
					this.pageSize = resp.data.pageSize;
					this.pageTotal = resp.data.pageTotal;
					this.totalInfo = resp.data.count;

					this.loading = false;
				});
			},
			getPageLikeInfo() {

				getPageTeams(1, this.pageSize, this.qryForm.token, this.qryForm.name, this.qryForm.typeId).then(resp => {

					this.pageInfos = resp.data.data;
					this.pageIndex = resp.data.pageIndex;
					this.pageSize = resp.data.pageSize;
					this.totalInfo = resp.data.count;
					this.pageTotal = resp.data.pageTotal;
					this.loading = false;
				});
			},
			handleSizeChange(pageSize) {

				this.getPageInfo(this.pageIndex, pageSize, this.qryForm.token, this.qryForm.name, this.qryForm.typeId);
			},
			handleCurrentChange(pageIndex) {

				this.getPageInfo(pageIndex, this.pageSize, this.qryForm.token, this.qryForm.name, this.qryForm.typeId);
			},
			initForm() {

				this.teamsForm = {
					id: "",
					name: "",
					total: 1,
					manager: "",
					typeId: "",
				};
			},
			showAddWin() {
				
				this.initForm();
				this.showAddFlag = true;
			},
			showUpdWin(row) {

				this.teamsForm = row;
				this.showUpdFlag = true;
			},
			addInfo() {

				addTeams(this.teamsForm).then(resp => {
					
					if(resp.code == 0){

						this.$message({
							message: resp.msg,
							type: 'success'
						});

						this.getPageInfo(1, this.pageSize, this.qryForm.token);

						this.showAddFlag = false;
					}else{

						this.$message({
							message: resp.msg,
							type: 'warning'
						});
					}
				});
			},
			updInfo() {

				updTeams(this.teamsForm).then(resp => {

					this.$message({
						message: resp.msg,
						type: 'success'
					});

					this.getPageInfo(1, this.pageSize, this.qryForm.token);

					this.showUpdFlag = false;
				});
			},
			delInfo(id){
				this.$confirm('即将删除相关信息, 是否继续?', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {

					delTeams(id).then(resp => {
												
						this.$message({
							message: resp.msg,
							type: 'success'
						});

						this.getPageInfo(1, this.pageSize, this.qryForm.token);
					});
				});
			},
			applyTeam(teamId) {
				this.$confirm('确认申请加入该社团?', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					addApplyLogs({
						token: this.qryForm.token,
						teamId: teamId
					}).then(res => {
						if (res.code === 0) {
							this.$message({
								type: 'success',
								message: '申请已提交!'
							});
							this.getPageInfo(this.pageIndex, this.pageSize);
						} else {
							this.$message({
								type: 'warning',
								message: res.msg
							});
						}
					});
				}).catch(() => {
					this.$message({
						type: 'info',
						message: '已取消申请'
					});
				});
			},
			isJoinedTeam(teamId) {
				return this.joinedTeams.includes(teamId);
			},
			async fetchUserTeams() {
				try {
					const resp = await getMemTeamList({
						token: this.$store.state.token
					});
					if (resp.code === 0) {
						this.joinedTeams = resp.data.map(team => team.id);
					}
				} catch (error) {
					console.error('获取用户社团列表失败:', error);
					this.$message.error('获取用户社团列表失败');
				}
			}
		},
		async mounted() {

			const userResp = await getLoginUser(this.$store.state.token);
			this.userType = userResp.data.type;
			
			if (this.userType === 2) {
				await this.fetchUserTeams();
			}
			
			this.getPageInfo(1, this.pageSize, this.qryForm.token);

			getAllTypes().then(resp =>{

				this.teamTypes = resp.data;
			});
		}
	}
</script>