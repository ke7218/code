<template>
	<el-header class="f-header">
		<div class="f-header-container">
			<img src="../assets/logo.png" class="header-logo" />
			<span class="header-title">校园社团管理系统</span>
		</div>
		<el-dropdown @command="handleUser" class="f-header-user" trigger="click">
			<span class="el-dropdown-link">
				<i class="fa fa-user"></i>
				个人中心
			</span>
			<el-dropdown-menu slot="dropdown">
				<el-dropdown-item command="info">个人信息</el-dropdown-item>
				<el-dropdown-item command="pwd">修改密码</el-dropdown-item>
				<el-dropdown-item command="exit">退出系统</el-dropdown-item>
			</el-dropdown-menu>
		</el-dropdown>

		<el-dialog title="修改信息" width="700px" :append-to-body="true" :visible.sync="showUpdInfoFlag">
			<el-form label-width="80px" :model="userForm" :rules="rules" ref="userForm">
				<el-row :gutter="15">
					<el-col :span="12">
						<el-form-item label="用户账号">
							<el-input v-model="userForm.userName" disabled></el-input>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="用户密码">
							<el-input v-model="userForm.passWord" disabled></el-input>
						</el-form-item>
					</el-col>
				</el-row>
				<el-row :gutter="15">
					<el-col :span="12">
						<el-form-item label="用户姓名" prop="name">
							<el-input v-model="userForm.name"
								placeholder="请输入用户姓名…" autocomplete="off"></el-input>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="用户年龄" prop="age">
							<el-input v-model="userForm.age"
								placeholder="请输入用户年龄…" autocomplete="off"></el-input>
						</el-form-item>
					</el-col>
				</el-row>
				<el-row :gutter="15">
					<el-col :span="12">
						<el-form-item label="用户性别" prop="gender">
							<el-radio-group v-model="userForm.gender">
								<el-radio label="男"></el-radio>
								<el-radio label="女"></el-radio>
							</el-radio-group>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="联系电话" prop="phone">
							<el-input v-model="userForm.phone"
								placeholder="请输入联系电话…" autocomplete="off"></el-input>
						</el-form-item>
					</el-col>
				</el-row>
				<el-form-item label="联系地址">
					<el-input rows="4" type="textarea" v-model="userForm.address"
						placeholder="请输入联系地址（选填）…" autocomplete="off"></el-input>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="showUpdInfoFlag = false">取 消</el-button>
				<el-button type="primary" @click="updInfo('userForm')">保 存</el-button>
			</div>
		</el-dialog>

		<el-dialog title="修改密码" width="600px" :append-to-body="true" :visible.sync="showUpdPwdFlag">
			<el-form label-width="80px" ref="userPwd" :model="userPwd" :rules="userPwdFormRules">
				<el-form-item label="原始密码" prop="oldPwd">
					<el-input type="password" v-model="userPwd.oldPwd" placeholder="请输入原始密码……" autocomplete="off">
					</el-input>
				</el-form-item>
				<el-form-item label="修改密码" prop="newPwd">
					<el-input type="password" v-model="userPwd.newPwd" placeholder="请输入修改密码……" autocomplete="off">
					</el-input>
				</el-form-item>
				<el-form-item label="确认密码" prop="rePwd">
					<el-input type="password" v-model="userPwd.rePwd" placeholder="请再次确认密码……" autocomplete="off">
					</el-input>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="showUpdPwdFlag = false">取 消</el-button>
				<el-button type="primary" @click="updPwd('userPwd')">确 定</el-button>
			</div>
		</el-dialog>
	</el-header>
</template>

<style>
</style>

<script>
	import {
		exit,
		getLoginUser,
		checkUserPwd,
		updLoginUserInfo,
		updLoginUserPwd,
	} from "../api";

	export default {
		data() {
			var checkOldPwd = async (rule, value, callback) => {
				if (value) {
					await checkUserPwd(this.$store.state.token, value).then((resp) => {
						if (resp.code != 0) {
							callback(new Error("原始密码输入错误"));
						}
					});
				} else {
					callback(new Error("原始密码必须输入"));
				}
				callback();
			};
			var checkNewPwd = (rule, value, callback) => {
				if (!value) {
					callback(new Error("修改密码必须输入"));
				}
				else if (value === this.userPwd.oldPwd) {
					callback(new Error("新密码不能与原密码相同"));
				}

				callback();
			};
			var checkRePwd = (rule, value, callback) => {
				if (!value) {
					callback(new Error("确认密码必须输入"));
				}

				if (value != this.userPwd.newPwd) {
					callback(new Error("两次输入密码不一致"));
				}

				callback();
			};
			// 年龄验证规则
			const validateAge = (rule, value, callback) => {
				if (!value) {
					callback(new Error('请输入年龄'));
				} else if (!Number.isInteger(Number(value))) {
					callback(new Error('请输入数字'));
				} else if (value < 0 || value > 120) {
					callback(new Error('年龄必须在0-120之间'));
				} else {
					callback();
				}
			};
			// 手机号验证规则
			const validatePhone = (rule, value, callback) => {
				const reg = /^1[3-9]\d{9}$/;
				if (!value) {
					callback(new Error('请输入手机号'));
				} else if (!reg.test(value)) {
					callback(new Error('请输入正确的手机号'));
				} else {
					callback();
				}
			};
			return {
				showUpdInfoFlag: false,
				showUpdPwdFlag: false,
				userPwd: {
					oldPwd: "",
					newPwd: "",
					rePwd: "",
				},
				userPwdFormRules: {
					oldPwd: [{
						validator: checkOldPwd,
						trigger: "blur",
					}, ],
					newPwd: [{
						validator: checkNewPwd,
						trigger: "blur",
					}, ],
					rePwd: [{
						validator: checkRePwd,
						trigger: "blur",
					}, ],
				},
				userForm: {
					id: "",
					userName: "",
					passWord: "",
					name: "",
					gender: "",
					age: "",
					phone: "",
					address: "",
				},
				rules: {
					name: [
						{ required: true, message: '请输入姓名', trigger: 'blur' },
						{ min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
					],
					age: [
						{ required: true, message: '请输入年龄', trigger: 'blur' },
						{ validator: validateAge, trigger: 'blur' }
					],
					gender: [
						{ required: true, message: '请选择性别', trigger: 'change' }
					],
					phone: [
						{ required: true, message: '请输入手机号', trigger: 'blur' },
						{ validator: validatePhone, trigger: 'blur' }
					]
				}
			};
		},
		methods: {
			handleUser(comm) {
				if (comm == "info") {
					this.showUpdInfoWin();
				}

				if (comm == "pwd") {
					this.showUpdPwdWin();
				}

				if (comm == "exit") {
					this.$confirm("确认要退出吗？", "系统提示", {
						confirmButtonText: "确认",
						cancelButtonText: "取消",
						type: "warning",
					}).then(() => {
						exit(this.$store.state.token).then(() => {
							this.$store.commit("clearToken");
							this.$store.commit("clearMenus");
							sessionStorage.clear();
							this.$router.push("/");
						});
					});
				}
			},
			initUserPwd() {
				this.userPwd = {
					oldPwd: "",
					newPwd: "",
					rePwd: "",
				};
			},
			initUserForm() {
				this.userForm = {
					id: "",
					userName: "",
				};
			},
			showUpdInfoWin() {
				getLoginUser(this.$store.state.token).then((resp) => {
					this.initUserForm();
					this.userForm = resp.data;
					this.showUpdInfoFlag = true;
				});
			},
			showUpdPwdWin() {
				this.initUserPwd();
				this.showUpdPwdFlag = true;
			},
			updInfo(formName) {
				this.$refs[formName].validate((valid) => {
					if (valid) {
						this.userForm.token = this.$store.state.token;
						updLoginUserInfo(this.userForm).then((resp) => {
							this.$message({
								message: resp.msg,
								type: "success",
							});

							this.showUpdInfoFlag = false;
							this.initUserForm();
						});
					} else {
						return false;
					}
				});
			},
			updPwd(formName) {
				this.$refs[formName].validate((valid) => {
					if (valid) {
						updLoginUserPwd(this.$store.state.token, this.userPwd.newPwd).then((resp) => {
							this.$message({
								message: resp.msg,
								type: "success",
							});

							this.showUpdPwdFlag = false;
							this.initUserPwd();
						});
					} else {
						return false;
					}
				});
			},
		},
	};
</script>
