package self.cases.teams.msg;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PageData {

    // 当前页码
    private Long pageIndex;

    // 每页数据量
    private Long pageSize;

    // 查询的总页数
    private Long pageTotal;

    // 符合条件的总记录数
    private Long count;

    // 分页查询包含的结果集
    private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();

    public Long getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Long pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Long getPageSize() {
        return pageSize;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }

    public Long getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(Long pageTotal) {
        this.pageTotal = pageTotal;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public List<Map<String, Object>> getData() {
        return data;
    }

    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }

    public PageData() {
        super();
    }

    public PageData(Long pageIndex, Long pageSize, Long count, List<Map<String, Object>> data) {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.count = count;
        this.data = (data != null && data.size() > 0) ? data : this.data;

        if(count > 0){
            this.pageTotal = (count % pageSize) == 0 ?  (count / pageSize) : (count / pageSize + 1);
        }else {
            this.pageTotal = 0L;
        }
    }

    // 添加新的构造函数，用于处理MyBatis Plus的Page对象
    public PageData(Page<Map<String, Object>> page) {
        this.data = page.getRecords();
        this.count = page.getTotal();
        this.pageIndex = page.getCurrent();
        this.pageSize = page.getSize();
        this.pageTotal = page.getPages();
    }

    @Override
    public String toString() {
        return "Page [pageIndex=" + pageIndex + ", pageSize=" + pageSize + ", pageTotal=" + pageTotal + ", count="
                + count + ", data=" + data + "]";
    }
}
