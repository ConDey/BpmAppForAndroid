package com.eazytec.bpm.lib.common.webservice;

/**
 * 标准的分页数据接口
 *
 * @author ConDey
 * @version Id: PageDataTObject, v 0.1 2017/6/11 下午7:39 ConDey Exp $$
 */
public class PageDataTObject extends WebDataTObject {

    private int pageNo;

    private int pageSize;

    private int nextPage;

    private int totalPages;

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    /**
     * 是否有下一页数据
     *
     * @return
     */
    public boolean hasNextPage() {
        return !(pageNo >= totalPages);
    }
}
