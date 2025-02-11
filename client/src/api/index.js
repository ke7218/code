import http from "../utils/http.js";

/** 用户认证相关接口 */
export function login(param) {
	return http.post('/login', param);
}

export function exit(token) {
	return http.get('/exit', {params: {token: token}});
}

export function getLoginUser(token) {
	return http.get('/info', {params: {token: token}});
}

export function checkUserPwd(token, oldPwd) {
	return http.get('/checkPwd', {params: {token: token, oldPwd: oldPwd}});
}

export function updLoginUserInfo(params) {
	return http.post('/info', params);
}

export function updLoginUserPwd(token, newPwd) {
	return http.post('/pwd', {token: token, password: newPwd});
}

/** 系统用户管理接口 */
export function getPageUsers(pageIndex, pageSize, userName, name, phone) {
	return http.get('/users/page', 
		{params: {pageIndex: pageIndex, pageSize: pageSize, userName: userName, name: name, phone: phone}});
}

export function addUsers(params) {
	return http.post('/users/add', params);
}

export function updUsers(params) {
	return http.post('/users/upd', params);
}

export function delUsers(id) {
	return http.post('/users/del', {id: id});
}

/** 通知管理接口 */
export function getSysNoticeList(token) {
	return http.get('/notices/sys/list', {
		headers: {
			token: token
		}
	});
}

export function getPageNotices(pageIndex, pageSize, token, title, teamName) {
	return http.get('/notices/page', 
		{params: {pageIndex: pageIndex, pageSize: pageSize, token: token, title: title, teamName: teamName}});
}

export function addNotices(params) {
	return http.post('/notices/add', params);
}

export function updNotices(params) {
	return http.post('/notices/upd', params);
}

export function delNotices(id) {
	return http.post('/notices/del', {id: id});
}

/** 社团类型管理接口 */
export function getAllTypes() {
	return http.get('/teamTypes/all');
}

export function getPageTeamTypes(pageIndex, pageSize, name) {
	return http.get('/teamTypes/page', 
		{params: {pageIndex: pageIndex, pageSize: pageSize, name: name}});
}

export function addTeamTypes(params) {
	return http.post('/teamTypes/add', params);
}

export function updTeamTypes(params) {
	return http.post('/teamTypes/upd', params);
}

export function delTeamTypes(id) {
	return http.post('/teamTypes/del', {id: id});
}

export function getClubTypeStats(token) {
	return http.get('/teamTypes/typeStats', {
		headers: {
			token: token
		}
	});
}

/** 社团信息管理接口 */
export function getAllTeamList() {
	return http.get('/teams/all');
}

export function getManTeamList(manId) {
	return http.get('/teams/man', {params: {manId: manId}});
}

export function getMemTeamList(params) {
	return http.get('/teams/member', {params: params});
}

export function getPageTeams(pageIndex, pageSize, token, name, typeId) {
	return http.get('/teams/page', 
		{params: {pageIndex: pageIndex, pageSize: pageSize, token: token, name: name, typeId: typeId}});
}

export function addTeams(params) {
	return http.post('/teams/add', params);
}

export function updTeams(params) {
	return http.post('/teams/upd', params);
}

export function delTeams(id) {
	return http.post('/teams/del', {id: id});
}

/** 社团活动管理接口 */
export function getPageActivities(pageIndex, pageSize, token, teamName, activeName) {
	return http.get('/activities/page', 
		{params: {pageIndex: pageIndex, pageSize: pageSize, token: token, teamName: teamName, activeName: activeName}});
}

export function addActivities(params) {
	return http.post('/activities/add', params);
}

export function updActivities(params) {
	return http.post('/activities/upd', params);
}

export function delActivities(id) {
	return http.post('/activities/del', {id: id});
}

/** 活动报名管理接口 */
export function getActiveLogs(activeId) {
	return http.get('/activeLogs/list', {params: {activeId: activeId}});
}

export function getPageActiveLogs(pageIndex, pageSize, name) {
	return http.get('/activeLogs/page', 
		{params: {pageIndex: pageIndex, pageSize: pageSize, name: name}});
}

export function addActiveLogs(params) {
	return http.post('/activeLogs/add', params);
}

export function updActiveLogs(params) {
	return http.post('/activeLogs/upd', params);
}

export function delActiveLogs(id) {
	return http.post('/activeLogs/del', {id: id});
}

/** 入团申请管理接口 */
export function getPageApplyLogs(pageIndex, pageSize, token, teamName, userName) {
	return http.get('/applyLogs/page', 
		{params: {pageIndex: pageIndex, pageSize: pageSize, token: token, teamName: teamName, userName: userName}});
}

export function addApplyLogs(params) {
	return http.post('/applyLogs/add', params);
}

export function updApplyLogs(params) {
	return http.post('/applyLogs/upd', params);
}

export function delApplyLogs(id) {
	return http.post('/applyLogs/del', {id: id});
}

/** 社团成员管理接口 */
export function getPageMembers(pageIndex, pageSize, token, teamName, userName) {
	return http.get('/members/page', 
		{params: {pageIndex: pageIndex, pageSize: pageSize, token: token, teamName: teamName, userName: userName}});
}

export function addMembers(params) {
	return http.post('/members/add', params);
}

export function updMembers(params) {
	return http.post('/members/upd', params);
}

export function delMembers(id) {
	return http.post('/members/del', {id: id});
}

/** 费用记录管理接口 */
export function getPagePayLogs(pageIndex, pageSize, token, teamName, userName) {
	return http.get('/payLogs/page', 
		{params: {pageIndex: pageIndex, pageSize: pageSize, token: token, teamName: teamName, userName: userName}});
}

export function addPayLogs(params) {
	return http.post('/payLogs/add', params);
}

export function updPayLogs(params) {
	return http.post('/payLogs/upd', params);
}

export function delPayLogs(id) {
	return http.post('/payLogs/del', {id: id});
}

/** 活动评论相关接口 */
export function getActivityComments(pageIndex, pageSize, activityId) {
	return http.get('/activities/comments/list', 
		{params: {pageIndex, pageSize, activityId}});
}

export function addActivityComment(token, comment) {
	return http.post('/activities/comments/add', 
		{...comment, token});
}

export function getActivityCommentAnalysis(activityId) {
	return http.get(`/activities/comments/analysis/${activityId}`);
}

