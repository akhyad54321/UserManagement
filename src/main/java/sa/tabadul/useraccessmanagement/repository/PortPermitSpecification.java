package sa.tabadul.useraccessmanagement.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import lombok.AllArgsConstructor;
import sa.tabadul.useraccessmanagement.common.constants.CommonCodes;
import sa.tabadul.useraccessmanagement.common.models.request.Pagination;
import sa.tabadul.useraccessmanagement.common.models.request.PortPermitFilter;
import sa.tabadul.useraccessmanagement.common.models.request.UserBranchKeys;
import sa.tabadul.useraccessmanagement.domain.PortPermitLicense;

@AllArgsConstructor
public class PortPermitSpecification implements Specification<PortPermitLicense> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8520414781783425345L;

	private transient Pagination<PortPermitFilter> objPagination;
	private List<Integer> lstPortsRid;
	private List<Integer> lstRequestTypeRid;
	private List<Integer> lstApprovalStatusRid;

	@Override
	public Predicate toPredicate(Root<PortPermitLicense> root, CriteriaQuery<?> query,
			CriteriaBuilder criteriaBuilder) {

		UserBranchKeys objBranchKeys = objPagination.getKeys();
		Boolean isUser = false;

		List<Predicate> lstPredicates = new ArrayList<>();

		if (objBranchKeys.getStakeHolderType().equals(CommonCodes.PORTOFF)) {
			lstPredicates.add(criteriaBuilder.equal(root.get("isActive"), true));
			lstPredicates.add(criteriaBuilder.equal(root.get("portRid"), objBranchKeys.getPortRid()));
		} else if (objBranchKeys.getStakeHolderType().equals(CommonCodes.PORTMAN)) {
			
			
			objBranchKeys.getRequestTypeRid().add(CommonCodes.CANCEL_LICENSE);
			objBranchKeys.getRequestTypeRid().add(CommonCodes.CANCEL_LICENSE_LO);
			objBranchKeys.getRequestTypeRid().add(CommonCodes.SUSPEND_LICENSE);
			objBranchKeys.getRequestTypeRid().add(CommonCodes.REACTIVATE_LICENSE);

			objBranchKeys.getStatusTypeRid().add(CommonCodes.PENDING_FOR_APPROVAL_LO);
			objBranchKeys.getStatusTypeRid().add(CommonCodes.PENDING_FOR_APPROVAL_LM);
			objBranchKeys.getStatusTypeRid().add(CommonCodes.APPROVED_LO);
			objBranchKeys.getStatusTypeRid().add(CommonCodes.APPROVED_LM);
			objBranchKeys.getStatusTypeRid().add(CommonCodes.REJECTED_LM);
			lstPredicates.add(criteriaBuilder.equal(root.get("isActive"), true));
			lstPredicates.add(root.get("requestTypeRid").in(objBranchKeys.getRequestTypeRid()));
			lstPredicates.add(root.get("approvalStatusRid").in(objBranchKeys.getStatusTypeRid()));
			lstPredicates.add(criteriaBuilder.equal(root.get("portRid"), objBranchKeys.getPortRid()));
		} else {
			isUser = true;
			lstPredicates.add(criteriaBuilder.equal(root.get("userOrgId"), objBranchKeys.getOrgId()));
		}


		query.distinct(true);

		return applySearchOrFilter(root, criteriaBuilder, objPagination.getSearch(),
				criteriaBuilder.and(lstPredicates.toArray(new Predicate[lstPredicates.size()])), isUser);
	}

	private Predicate searchLicense(Root<PortPermitLicense> root, CriteriaBuilder criteriaBuilder, Boolean isUser) {

		String search = objPagination.getSearch();
		Expression<String> crnAsString = root.get("crn").as(String.class);
		Expression<String> createdDateAsString = root.get("createdDate").as(String.class);
		Join<Object, Object> userOrgJoin = root.join("userOrg");

		// Join the userLicense entity from userOrg
		Join<Object, Object> userLicenseJoin = userOrgJoin.join("userLicense");
		List<Predicate> lstPredicates = new ArrayList<>();

		if (Boolean.TRUE.equals(isUser)) {
			if (!CollectionUtils.isEmpty(lstPortsRid)) {
				lstPredicates.add(root.get("portRid").in(lstPortsRid));
			}

			lstPredicates.add(criteriaBuilder.like(criteriaBuilder.lower(userLicenseJoin.get("licenseNumber")),
					"%" + search.toLowerCase() + "%"));
			Expression<String> licExpiryDate = userLicenseJoin.get("licenseExpiryDate").as(String.class);

			lstPredicates
					.add(criteriaBuilder.like(criteriaBuilder.lower(licExpiryDate), "%" + search.toLowerCase() + "%"));
			Expression<String> licCreatedDate = userLicenseJoin.get("createdDate").as(String.class);

			lstPredicates
					.add(criteriaBuilder.like(criteriaBuilder.lower(licCreatedDate), "%" + search.toLowerCase() + "%"));
		} else {
			lstPredicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("userOrg").get("orgName")),
					"%" + search.toLowerCase() + "%"));

			lstPredicates
					.add(criteriaBuilder.like(criteriaBuilder.lower(crnAsString), "%" + search.toLowerCase() + "%"));

			lstPredicates.add(
					criteriaBuilder.like(criteriaBuilder.lower(createdDateAsString), "%" + search.toLowerCase() + "%"));

		}

		if (!CollectionUtils.isEmpty(lstRequestTypeRid)) {
			lstPredicates.add(root.get("requestTypeRid").in(lstRequestTypeRid));
		}
		if (!CollectionUtils.isEmpty(lstApprovalStatusRid)) {
			lstPredicates.add(root.get("approvalStatusRid").in(lstApprovalStatusRid));
		}

		return criteriaBuilder.or(lstPredicates.toArray(new Predicate[lstPredicates.size()]));

	}

	private Predicate filterLicense(Root<PortPermitLicense> root, CriteriaBuilder criteriaBuilder) {

		PortPermitFilter filter = objPagination.getFilter();
		List<Predicate> lstPredicates = new ArrayList<>();

		if (filter.getLicenseType() != null) {
			lstPredicates.add(criteriaBuilder.equal(root.get("requestTypeRid"), filter.getLicenseType()));
		}
		if (filter.getStatus() != null) {
			lstPredicates.add(criteriaBuilder.equal(root.get("approvalStatusRid"), filter.getStatus()));
		}
		if (filter.getFromDate() != null) {
			lstPredicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdDate"), filter.getFromDate()));
		}
		if (filter.getToDate() != null) {
			lstPredicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdDate"), filter.getToDate()));
		}

		return criteriaBuilder.and(lstPredicates.toArray(new Predicate[lstPredicates.size()]));

	}

	private Predicate applySearchOrFilter(Root<PortPermitLicense> root, CriteriaBuilder criteriaBuilder, String search,
			Predicate predicate, Boolean isUser) {
		if (StringUtils.isNotEmpty(search)) {
			return criteriaBuilder.and(predicate, searchLicense(root, criteriaBuilder, isUser));
		} else if (objPagination.getFilter() != null) {
			return criteriaBuilder.and(predicate, filterLicense(root, criteriaBuilder));
		} else {
			return predicate;
		}
	}
}
