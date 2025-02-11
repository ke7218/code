package self.cases.teams.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestHeader;

import self.cases.teams.entity.Users;
import self.cases.teams.handle.CacheHandle;
import self.cases.teams.service.UsersService;
import self.cases.teams.utils.DateUtils;
import self.cases.teams.utils.IDUtils;
import self.cases.teams.msg.R;
import self.cases.teams.msg.PageData;

import self.cases.teams.entity.Notices;
import self.cases.teams.service.NoticesService;
import self.cases.teams.utils.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * 系统请求响应控制器
 * 通知记录
 */
@Controller
@RequestMapping("/notices")
public class NoticesController extends BaseController {

    protected static final Logger Log = LoggerFactory.getLogger(NoticesController.class);

    @Autowired
    private CacheHandle cacheHandle;

    @Autowired
    private NoticesService noticesService;

    @Autowired
    private UsersService usersService;

    @RequestMapping("")
    public String index() {

        return "pages/Notices";
    }

    @GetMapping("/info")
    @ResponseBody
    public R getInfo(String id) {

        Log.info("查找指定通知记录，ID：{}", id);

        Notices notices = noticesService.getOne(id);

        return R.successData(notices);
    }

    @GetMapping("/page")
    @ResponseBody
    public R getPageInfos(Long pageIndex, Long pageSize,
                          String token, String title, String teamName) {
        // 验证 token
        String userId = cacheHandle.getUserInfoCache(token);
        if (userId == null) {
            return R.error("登录已过期，请重新登录");
        }

        Users user = usersService.getOne(userId);
        if (user == null) {
            return R.error("用户不存在");
        }

        if (user.getType() == 0) {
            Log.info("分页查找指通知记录，当前页码：{}，"
                            + "每页数据量：{}, 模糊查询，通知标题：{}，社团名称：{}", pageIndex,
                    pageSize, title, teamName);

            PageData page = noticesService.getPageAll(pageIndex, pageSize, title, teamName);

            return R.successData(page);
        } else {
            Log.info("分页查找指定用户相关通知记录，当前页码：{}，"
                            + "每页数据量：{}, 模糊查询，通知标题：{}，社团名称：{}", pageIndex,
                    pageSize, title, teamName);

            PageData page = noticesService.getPageById(pageIndex, pageSize, user.getId(), title, teamName);

            return R.successData(page);
        }
    }

    @PostMapping("/add")
    @ResponseBody
    public R addInfo(Notices notices) {

        notices.setId(IDUtils.makeIDByCurrent());
        notices.setCreateTime(DateUtils.getNowDate("yyyy-MM-dd"));

        if(StringUtils.isNullOrEmpty(notices.getTeamId())){

            notices.setTeamId(null);
        }

        Log.info("添加通知记录，传入参数：{}", notices);

        noticesService.add(notices);

        return R.success();
    }

    @PostMapping("/upd")
    @ResponseBody
    public R updInfo(Notices notices) {

        Log.info("修改通知记录，传入参数：{}", notices);

        noticesService.update(notices);

        return R.success();
    }

    @PostMapping("/del")
    @ResponseBody
    public R delInfo(String id) {

        Log.info("删除通知记录, ID:{}", id);

        Notices notices = noticesService.getOne(id);

        noticesService.delete(notices);

        return R.success();
    }

    @GetMapping("/sys/list")
    @ResponseBody
    public R getSysNoticeList(@RequestHeader String token) {
        Log.info("获取系统通知列表");
        
        // 获取当前登录用户ID
        String userId = cacheHandle.getUserInfoCache(token);
        if (userId == null) {
            return R.error("登录已过期，请重新登录");
        }
        
        // 获取用户信息
        Users user = usersService.getOne(userId);
        if (user == null) {
            return R.error("用户不存在");
        }
        
        List<Map<String, Object>> notices;
        if (user.getType() == 0) {
            // 系统管理员可以看到所有通知
            notices = noticesService.getAllNoticesWithTeam();
        } else {
            // 普通用户只能看到系统管理员和所在社团的通知
            notices = noticesService.getUserNoticesWithTeam(user.getId());
        }
        
        return R.successData(notices);
    }
}