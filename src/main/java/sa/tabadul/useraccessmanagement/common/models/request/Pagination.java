package sa.tabadul.useraccessmanagement.common.models.request;

import lombok.Data;

@Data
public class Pagination<T> {
	
	private Integer page;
    private Integer length;
    private Integer start;
    private String sort;
    private String sortDir;
    private String search;
    private Integer draw;
    private T filter;
	private UserBranchKeys keys;
	
}
