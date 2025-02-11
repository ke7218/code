<template>
	<div class="f-body-show">
		<el-card shadow="never">
			<div slot="header">
				信息查询
			</div>
			<div>
				<el-table v-loading="loading" element-loading-text="拼命加载中" element-loading-spinner="el-icon-loading"
					element-loading-background="rgba(124, 124, 124, 0.8)" :data="teamList" border>
					<el-table-column align="center" type="index"></el-table-column>
					<el-table-column align="center" prop="name" label="社团名称"></el-table-column>
					<el-table-column align="center" prop="typeName" label="社团类型"></el-table-column>
					<el-table-column align="center" prop="managerName" label="社团管理员"></el-table-column>
					<el-table-column align="center" prop="managerPhone" label="联系电话"></el-table-column>
					<el-table-column align="center" prop="total" label="成员数量"></el-table-column>
					<el-table-column align="center" prop="createTime" label="创建时间"></el-table-column>
				</el-table>
			</div>
		</el-card>
	</div>
</template>

<script>
	import { getMemTeamList } from "../../api";

	export default {
		data() {
			return {
				loading: true,
				teamList: []
			}
		},
		methods: {
			async fetchTeams() {
				try {
					const resp = await getMemTeamList({
						token: this.$store.state.token
					});
					if (resp.code === 0) {
						this.teamList = resp.data;
						this.loading = false;
					}
				} catch (error) {
					console.error('获取我的社团列表失败:', error);
					this.$message.error('获取我的社团列表失败');
				}
			}
		},
		mounted() {
			this.fetchTeams();
		}
	}
</script> 