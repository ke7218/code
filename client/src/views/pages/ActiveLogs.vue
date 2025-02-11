<template>
	<div class="f-body-show">
		<el-card shadow="never">
			<div slot="header">
				信息查询
			</div>
			<div>
				<el-form :inline="true" :model="qryForm">
					<el-form-item>
						<el-input v-model="qryForm.name"
							placeholder="输入活动名称..."
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
					<el-table-column align="center" type="index" label="序号" width="80"></el-table-column>
					<el-table-column align="center" prop="createTime" label="报名时间" width="180"></el-table-column>
					<el-table-column align="center" prop="activeName" label="活动名称"></el-table-column>
					<el-table-column align="center" prop="teamName" label="发布社团"></el-table-column>
					<el-table-column align="center" prop="userName" label="报名用户"></el-table-column>
					<el-table-column align="center" label="操作" width="100">
						<template slot-scope="scope">
							<el-button icon="el-icon-delete" type="danger" size="mini"
								@click="delInfo(scope.row.id)"></el-button>
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
		getPageActiveLogs,
		delActiveLogs
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
				qryForm: {
					name: "",
				}
			}
		},
		methods: {
			getPageInfo(pageIndex, pageSize) {
				getPageActiveLogs(pageIndex, pageSize).then(resp => {
					this.pageInfos = resp.data.data;
					this.pageIndex = resp.data.pageIndex;
					this.pageSize = resp.data.pageSize;
					this.pageTotal = resp.data.pageTotal;
					this.totalInfo = resp.data.count;
					this.loading = false;
				});
			},
			getPageLikeInfo() {
				getPageActiveLogs(1, this.pageSize, this.qryForm.name).then(resp => {
					this.pageInfos = resp.data.data;
					this.pageIndex = resp.data.pageIndex;
					this.pageSize = resp.data.pageSize;
					this.totalInfo = resp.data.count;
					this.pageTotal = resp.data.pageTotal;
					this.loading = false;
				});
			},
			handleSizeChange(pageSize) {
				this.getPageInfo(this.pageIndex, pageSize, this.qryForm.name);
			},
			handleCurrentChange(pageIndex) {
				this.getPageInfo(pageIndex, this.pageSize, this.qryForm.name);
			},
			delInfo(id) {
				delActiveLogs(id).then(resp => {
					this.$message({
						message: resp.msg,
						type: 'success'
					});
					this.getPageInfo(1, this.pageSize);
				});
			}
		},
		created() {
			this.getPageInfo(1, this.pageSize);
		}
	}
</script>