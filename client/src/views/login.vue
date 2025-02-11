<template>
	<div class="login">
		<div class="login-container">
			<el-form :model="loginForm" :rules="rules" ref="loginForm">
				<h2 style="text-align: center; margin-bottom: 50px;">"社团星"校园社团管理系统</h2>
				<el-form-item prop="userName">
					<el-input v-model="loginForm.userName" prefix-icon="el-icon-user" placeholder="请输入账号..."></el-input>
				</el-form-item>
				<el-form-item prop="passWord">
					<el-input v-model="loginForm.passWord" prefix-icon="el-icon-lock" type="password" show-password
						placeholder="请输入密码..."></el-input>
				</el-form-item>
				<el-form-item>
					<el-button type="primary" class="login-btn" @click="submitForm('loginForm')">登录</el-button>
					<el-button class="register-btn" @click="showAddWin">注册</el-button>
				</el-form-item>
			</el-form>
		</div>

		<!-- 注册对话框 -->
		<el-dialog title="用户注册" width="500px" :modal="false" :visible.sync="showAddFlag">
			<el-form label-width="90px" :model="usersForm" :rules="registerRules" ref="usersForm">
				<el-form-item label="用户账号" prop="userName">
					<el-input v-model="usersForm.userName"
						placeholder="请输入用户账号…" autocomplete="off"></el-input>
				</el-form-item>
				<el-form-item label="用户密码" prop="passWord">
					<el-input v-model="usersForm.passWord" type="password" show-password
						placeholder="请输入用户密码…" autocomplete="off"></el-input>
				</el-form-item>
				<el-form-item label="确认密码" prop="confirmPwd">
					<el-input v-model="usersForm.confirmPwd" type="password" show-password
						placeholder="请再次输入密码…" autocomplete="off"></el-input>
				</el-form-item>
				<el-form-item label="用户姓名" prop="name">
					<el-input v-model="usersForm.name"
						placeholder="请输入用户姓名…" autocomplete="off"></el-input>
				</el-form-item>
				<el-form-item label="用户年龄" prop="age">
					<el-input v-model="usersForm.age"
						placeholder="请输入用户年龄…" autocomplete="off"></el-input>
				</el-form-item>
				<el-form-item label="用户性别" prop="gender">
					<el-radio-group v-model="usersForm.gender">
						<el-radio label="男"></el-radio>
						<el-radio label="女"></el-radio>
					</el-radio-group>
				</el-form-item>
				<el-form-item label="联系电话" prop="phone">
					<el-input v-model="usersForm.phone"
						placeholder="请输入联系电话…" autocomplete="off"></el-input>
				</el-form-item>
				<el-form-item label="联系地址">
					<el-input rows="3" type="textarea" v-model="usersForm.address"
						placeholder="请输入联系地址（选填）…" autocomplete="off"></el-input>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="showAddFlag = false">取 消</el-button>
				<el-button type="primary" @click="addInfo('usersForm')">确 定</el-button>
			</div>
		</el-dialog>
	</div>
</template>

<style>
	/* 登录页面背景容器 */
	.login {
		width: 100%;
		height: 100vh;
		background: url("../assets/bg.jpg") no-repeat center center;
		background-size: cover;
		position: relative;
		display: flex;
		align-items: center;
		justify-content: center;
	}

	/* 登录框样式 */
	.login-container {
		width: 400px;
		padding: 40px;
		padding-bottom: 50px;
		border-radius: 15px;
		background: rgba(255, 255, 255, 0.7);
		box-shadow: 0 0 20px rgba(0, 0, 0, 0.15);
		position: absolute;
		top: 50%;
		left: 50%;
		transform: translate(-50%, -50%);
	}

	/* 登录标题样式 */
	.login-container h2 {
		color: #333;
		margin-bottom: 30px;
		font-size: 24px;
	}

	/* 按钮容器样式 */
	.login-container .el-form-item:last-child .el-form-item__content {
		display: flex;
		flex-direction: column;
		gap: 15px;
	}

	/* 注册按钮样式 */
	.register-btn {
		width: 100% !important;
		height: 40px;
		font-size: 16px;
		background-color: #7dcf53 !important;
		border-color: #7dcf53 !important;
		color: #FFF !important;
		margin-left: 0px !important;
	}

	/* 注册按钮悬停效果 */
	.register-btn:hover {
		background-color: #47c807 !important;
		border-color:#47c807 !important;
	}

	/* 注册对话框容器 */
	.el-dialog__wrapper {
		display: flex;
		align-items: center;
		justify-content: center;
		margin-top: -100px;
	}

	/* 注册对话框基础样式 */
	.el-dialog {
		margin: 0 !important;
		margin-bottom: 0;
	}

	/* 对话框头部样式 */
	.el-dialog__header {
		padding: 20px !important;
		background-color: #f5f7fa;
		border-bottom: 1px solid #e4e7ed;
		text-align: center;
	}

	/* 对话框标题样式 */
	.el-dialog__title {
		font-size: 18px;
		color: #333;
		font-weight: bold;
	}

	/* 表单项样式 */
	.el-form-item {
		margin-bottom: 22px !important;
	}

	/* 表单标签样式 */
	.el-form-item__label {
		font-weight: 500;
		color: #333;
	}

	/* 对话框底部样式 */
	.el-dialog__footer {
		padding: 15px 20px 20px !important;
		border-top: 1px solid #e4e7ed;
	}

	/* 单选框组样式 */
	.el-radio-group {
		display: flex;
		gap: 30px;
	}

	/* 文本域样式 */
	.el-textarea__inner {
		font-family: inherit;
		height: 40px !important;
		min-height: 40px !important;
		resize: vertical;
	}
</style>

<script>
	import initMenu from "../utils/menus.js";
	import {
		login,
		addUsers
	} from '../api/index.js'

	export default {
		data() {
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
			// 确认密码验证规则
			const validateConfirmPwd = (rule, value, callback) => {
				if (!value) {
					callback(new Error('请再次输入密码'));
				} else if (value !== this.usersForm.passWord) {
					callback(new Error('两次输入的密码不一致'));
				} else {
					callback();
				}
			};
			return {
				showAddFlag: false,
				usersForm: {
					id: "",
					userName: "",
					passWord: "",
					confirmPwd: "",
					name: "",
					gender: "",
					age: "",
					phone: "",
					address: "",
					type: 2,
					status: 1
				},
				loginForm: {
					userName: '',
					passWord: ''
				},
				rules: {
					userName: [{
						required: true,
						message: '用户账号必须输入',
						trigger: 'blur'
					}],
					passWord: [{
						required: true,
						message: '用户密码必须输入',
						trigger: 'blur'
					}],
				},
				registerRules: {
					userName: [
						{ required: true, message: '请输入用户账号', trigger: 'blur' },
						{ min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
					],
					passWord: [
						{ required: true, message: '请输入密码', trigger: 'blur' },
						{ min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
					],
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
					],
					confirmPwd: [
						{ required: true, message: '请再次输入密码', trigger: 'blur' },
						{ validator: validateConfirmPwd, trigger: 'blur' }
					],
				}
			}
		},
		methods: {
			showAddWin() {
				this.showAddFlag = true;
			},
			submitForm(formName) {
				this.$refs[formName].validate((valid) => {
					if (valid) {
						login(this.loginForm).then(res => {
							this.$store.commit('setToken', res.data);
							sessionStorage.setItem("token", res.data);
							initMenu(this.$router, this.$store);
							this.$router.push('/index');
						});
					} else {
						return false;
					}
				});
			},
			addInfo(formName) {
				this.$refs[formName].validate((valid) => {
					if (valid) {
						addUsers(this.usersForm).then(resp => {
							if (resp.code == 0) {
								this.$confirm('注册成功, 立即登陆?', '提示', {
									confirmButtonText: '确定',
									cancelButtonText: '取消',
									type: 'warning'
								}).then(() => {
									login({
										userName: this.usersForm.userName,
										passWord: this.usersForm.passWord
									}).then(res => {
										this.$store.commit('setToken', res.data);
										sessionStorage.setItem("token", res.data);
										initMenu(this.$router, this.$store);
										this.$router.push('/index');
									});
								});
							} else {
								this.$message({
									message: resp.msg,
									type: 'warning'
								});
							}
						});
					} else {
						return false;
					}
				});
			}
		}
	}
</script>