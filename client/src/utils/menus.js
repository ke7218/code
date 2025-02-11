import {
	getLoginUser
} from "../api";
import Home from "../views/home.vue";
import Index from "../views/pages/Index.vue";
import Users from "../views/pages/Users.vue";
import TeamTypes from "../views/pages/TeamTypes.vue";
import Teams from "../views/pages/Teams.vue";
import Members from "../views/pages/Members.vue";
import Activities from "../views/pages/Activities.vue";
import ActivityCalendar from "../views/pages/ActivityCalendar.vue";
import ActiveLogs from "../views/pages/ActiveLogs.vue";
import Notices from "../views/pages/Notices.vue";
import ApplyLogs from "../views/pages/ApplyLogs.vue";
import PayLogs from "../views/pages/PayLogs.vue";
import MyTeams from "../views/pages/MyTeams.vue";

// 系统管理员
export const adminMenus = {
	path: '/home',
	name: 'home',
	component: Home,
	children: [{
			path: '/index',
			name: '首页',
			icon: "fa fa-dashboard",
			component: Index
		},
		{
			path: '/users',
			name: '用户管理',
			icon: "fa fa-users",
			component: Users
		},
		{
			path: '/teamTypes',
			name: '社团类型',
			icon: "fa fa-tags",
			component: TeamTypes
		},
		{
			path: '/teams',
			name: '社团信息',
			icon: "fa fa-university",
			component: Teams
		},
		{
			path: '/members',
			name: '社团成员',
			icon: "fa fa-user-circle",
			component: Members
		},
		{
			path: '/activities',
			name: '社团活动',
			icon: "fa fa-calendar",
			component: Activities
		},
		{
			path: '/activityCalendar',
			name: '活动日历',
			icon: "fa fa-calendar-check-o",
			component: ActivityCalendar
		},
		{
			path: '/activeLogs',
			name: '活动记录',
			icon: "fa fa-list-alt",
			component: ActiveLogs
		},
		{
			path: '/notices',
			name: '通知信息',
			icon: "fa fa-bell",
			component: Notices
		},
		{
			path: '/applyLogs',
			name: '入社申请',
			icon: "fa fa-file-text",
			component: ApplyLogs
		},
		{
			path: '/payLogs',
			name: '缴费信息',
			icon: "fa fa-money",
			component: PayLogs
		}
	]
};

// 社团管理员
export const manMenus = {
	path: '/home',
	name: 'home',
	component: Home,
	children: [{
			path: '/index',
			name: '首页',
			icon: "fa fa-dashboard",
			component: Index
		},
		{
			path: '/teams',
			name: '社团信息',
			icon: "fa fa-university",
			component: Teams
		},
		{
			path: '/members',
			name: '社团成员',
			icon: "fa fa-user-circle",
			component: Members
		},
		{
			path: '/applyLogs',
			name: '入社申请',
			icon: "fa fa-file-text",
			component: ApplyLogs
		},
		{
			path: '/activities',
			name: '社团活动',
			icon: "fa fa-calendar",
			component: Activities
		},
		{
			path: '/activityCalendar',
			name: '活动日历',
			icon: "fa fa-calendar-check-o",
			component: ActivityCalendar
		},
		{
			path: '/activeLogs',
			name: '活动记录',
			icon: "fa fa-list-alt",
			component: ActiveLogs
		},
		{
			path: '/notices',
			name: '通知信息',
			icon: "fa fa-bell",
			component: Notices
		},
		{
			path: '/payLogs',
			name: '缴费信息',
			icon: "fa fa-money",
			component: PayLogs
		}
	]
};

// 社团成员
export const memMenus = {
	path: '/home',
	name: 'home',
	component: Home,
	children: [{
			path: '/index',
			name: '首页',
			icon: "fa fa-dashboard",
			component: Index
		},
		{
			path: '/teams',
			name: '社团信息',
			icon: "fa fa-university",
			component: Teams
		},
		{
			path: '/myTeams',
			name: '我的社团',
			icon: "fa fa-star",
			component: MyTeams
		},
		{
			path: '/activities',
			name: '社团活动',
			icon: "fa fa-calendar",
			component: Activities
		},
		{
			path: '/activityCalendar',
			name: '活动日历',
			icon: "fa fa-calendar-check-o",
			component: ActivityCalendar
		},
		{
			path: '/applyLogs',
			name: '入社申请',
			icon: "fa fa-file-text",
			component: ApplyLogs
		},
		{
			path: '/payLogs',
			name: '缴费信息',
			icon: "fa fa-money",
			component: PayLogs
		}
	]
};

export default function initMenu(router, store) {
	let token = null;
	if (store.state.token) {
		token = store.state.token;
	} else {
		token = sessionStorage.getItem("token");
		store.state.token = sessionStorage.getItem("token");
	}

	getLoginUser(token).then(resp => {
		if (resp.data.type == 0) {
			router.addRoute(adminMenus);
			store.commit("setMenus", adminMenus);
		} else if (resp.data.type == 1) {
			router.addRoute(manMenus);
			store.commit("setMenus", manMenus);
		} else if (resp.data.type == 2) {
			router.addRoute(memMenus);
			store.commit("setMenus", memMenus);
		}
	});
}