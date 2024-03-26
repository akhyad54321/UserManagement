package sa.tabadul.useraccessmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import sa.tabadul.useraccessmanagement.common.models.response.Access;
import sa.tabadul.useraccessmanagement.domain.AccessModules;

public interface AccessModulesRepository extends JpaRepository<AccessModules, Integer> {
	
	
	@Query("SELECT new sa.tabadul.useraccessmanagement.common.models.response.Access(h.id,h.header,h.headerAr,h.headerDescription,h.headerIconPath, h.headSequenceNo, "
			+ "m.id,m.module,m.moduleDescription,m.moduleIconPath,m.moduleRoute,"
			+ "p.id,p.page,p.pageCode,p.pageArabic,p.pageDescriptionArabic,p.pageDescription,p.pageIconPath,p.pageRoute,p.mawaniMenuSetting,p.sequence,p.appId,"
			+ "st.id,st.stakeholderCategoryRID,st.stakeholdertypeID,st.accessView,st.accessCreate,st.accessEdit,st.accessDelete,st.accessPrint,st.accessApproveReject,st.isActive) "
			+ "FROM AccessHeader h "
			+ "JOIN AccessModules m ON m.accessHeaderID=h.id "
			+ "JOIN AccessPages p ON p.accessModuleID=m.id "
			+ "JOIN AccessPageStakeHolders st ON st.accesspageID=p.id "
			+ "WHERE st.stakeholderCategoryRID IN (:stakeHolderIds) AND p.appId=:appId AND st.isActive=true AND st.stakeholdertypeID IN (:subRoleIds) AND st.accessView=true ORDER BY p.sequence")
	List<Access> userAccess(@Param("stakeHolderIds") List<Integer> stakeHolderIds,@Param("subRoleIds") List<Integer> subRoleIds,@Param("appId")Integer appId);

}
