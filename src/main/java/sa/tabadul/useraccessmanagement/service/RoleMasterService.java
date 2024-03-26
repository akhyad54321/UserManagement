package sa.tabadul.useraccessmanagement.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import sa.tabadul.useraccessmanagement.common.configs.PropertiesConfig;
import sa.tabadul.useraccessmanagement.common.constants.CommonCodes;
import sa.tabadul.useraccessmanagement.common.enums.ExceptionMessage;
import sa.tabadul.useraccessmanagement.common.models.request.Pagination;
import sa.tabadul.useraccessmanagement.common.models.request.PolicyRole;
import sa.tabadul.useraccessmanagement.common.models.request.PortAdminPolicy;
import sa.tabadul.useraccessmanagement.common.models.request.RedHatKeycloakRole;
import sa.tabadul.useraccessmanagement.common.models.request.RoleMasterFilter;
import sa.tabadul.useraccessmanagement.common.models.request.UserBranchKeys;
import sa.tabadul.useraccessmanagement.common.models.response.PaginationResponse;
import sa.tabadul.useraccessmanagement.common.models.response.ApiResponse;
import sa.tabadul.useraccessmanagement.common.models.response.RoleTypeResponse;
import sa.tabadul.useraccessmanagement.common.service.UserManagementExternalService;
import sa.tabadul.useraccessmanagement.domain.AccessModule;
import sa.tabadul.useraccessmanagement.domain.Attributes;
import sa.tabadul.useraccessmanagement.domain.KeyClockRole;
import sa.tabadul.useraccessmanagement.domain.RoleAttribute;
import sa.tabadul.useraccessmanagement.domain.RoleMaster;
import sa.tabadul.useraccessmanagement.domain.RoleRedHat;
import sa.tabadul.useraccessmanagement.exception.BusinessException;
import sa.tabadul.useraccessmanagement.repository.AccessModuleRepository;
import sa.tabadul.useraccessmanagement.repository.KeyClockRoleRepository;
import sa.tabadul.useraccessmanagement.repository.RoleAttributeRepository;
import sa.tabadul.useraccessmanagement.repository.RoleMasterRepository;

@Service
public class RoleMasterService {

	public static final Logger log = LogManager.getLogger(RoleMasterService.class);

	@Autowired
	private RoleMasterRepository roleMasterRepository;

	@Autowired
	private AccessModuleRepository accessModuleRepository;

	@Autowired
	private CacheManager cacheManager;

	@Autowired
	private KeyClockRoleRepository keyClockRoleRepository;

	@Autowired
	private RoleAttributeRepository roleAttributeRepository;

	@Autowired
	PropertiesConfig propertiesConfig;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	private UserManagementExternalService userExternalService;

	ApiResponse<?> apiResponse = null;

	public RoleMaster add(RoleMaster r) {
		String code = r.getRoleCode();
		RoleMaster code2 = roleMasterRepository.findByRoleCode(code);
		String roleName = r.getRoleNameEnglish();
		RoleMaster roleName1 = roleMasterRepository.findByRoleNameEnglish(roleName);
		String roleArabic = r.getRoleNameArabic();
		RoleMaster roleArabic1 = roleMasterRepository.findByRoleNameArabic(roleArabic);
		String roleDesc = r.getRoleDescriptionEnglish();
		RoleMaster roleDesc1 = roleMasterRepository.findByRoleDescriptionEnglish(roleDesc);
		String roleDescArabic = r.getRoleDescriptionArabic();
		RoleMaster roleDescArabic1 = roleMasterRepository.findByRoleDescriptionArabic(roleDescArabic);
		if (code2 != null) {
			throw new BusinessException(HttpStatus.BAD_REQUEST.value(), ExceptionMessage.ROLE_EXISTS.getValue() + code);
		}
		if (roleName1 != null) {
			throw new BusinessException(HttpStatus.BAD_REQUEST.value(),
					ExceptionMessage.ROLE_EXISTS.getValue() + roleName);
		}
		if (roleArabic1 != null) {
			throw new BusinessException(HttpStatus.BAD_REQUEST.value(),
					ExceptionMessage.ROLE_EXISTS.getValue() + roleArabic);
		}
		if (roleDesc1 != null) {
			throw new BusinessException(HttpStatus.BAD_REQUEST.value(),
					ExceptionMessage.ROLE_EXISTS.getValue() + roleDesc);
		}
		if (roleDescArabic1 != null) {
			throw new BusinessException(HttpStatus.BAD_REQUEST.value(),
					ExceptionMessage.ROLE_EXISTS.getValue() + roleDescArabic);
		}
		RoleMaster roleMaster = roleMasterRepository.save(r);
		try {
			Cache cache = cacheManager.getCache(CommonCodes.ROLE_CACHE_VALUE);
			log.debug(CommonCodes.CACHE_REFRESH);
			List<RoleMaster> role = roleMasterRepository.findAllByOrderByCreatedDateDesc();
			cache.put(CommonCodes.ROLE_CACHE_KEY, role);
		} catch (Exception cacheException) {
			log.error(ExceptionMessage.ERROR_REFRESHING_CACHE.getValue(), cacheException);
		}
		return roleMaster;
	}

	public List<RoleMaster> get(Integer appId) {
		List<RoleMaster> cachedData = null;
		try {
			Cache cache = cacheManager.getCache(CommonCodes.ROLE_CACHE_VALUE);
			Cache.ValueWrapper cacheValueWrapper = cache.get(CommonCodes.ROLE_CACHE_KEY);
			if (cacheValueWrapper != null) {
				cachedData = (List<RoleMaster>) cacheValueWrapper.get();
			} else {
				List<RoleMaster> lstData = roleMasterRepository.findAllByOrderByCreatedDateDesc();
				cache.put(CommonCodes.ROLE_CACHE_KEY, lstData);
				cacheValueWrapper = cache.get(CommonCodes.ROLE_CACHE_KEY);
				cachedData = (List<RoleMaster>) cacheValueWrapper.get();
			}
			List<RoleMaster> roleMasters = cachedData.stream()
					.filter(rolemaster -> (rolemaster.getAppId() != null && rolemaster.getAppId().equals(appId)))
					.collect(Collectors.toList());
			if (!roleMasters.isEmpty()) {
				return roleMasters;
			} else {
				return Collections.emptyList();
			}
		} catch (Exception e) {
			log.error(ExceptionMessage.ERROR_ACCESSING_CACHE.getValue(), e);
		}
		cachedData = roleMasterRepository.findByAppId(appId);
		return cachedData;
	}

	public RoleMaster getBy(int id, Integer appId) {
		List<RoleMaster> cachedData = null;
		try {
			Cache cache = cacheManager.getCache(CommonCodes.ROLE_CACHE_VALUE);
			Cache.ValueWrapper cacheValueWrapper = cache.get(CommonCodes.ROLE_CACHE_KEY);
			if (cacheValueWrapper != null) {
				cachedData = (List<RoleMaster>) cacheValueWrapper.get();
			} else {
				List<RoleMaster> lstData = roleMasterRepository.findAllByOrderByCreatedDateDesc();
				cache.put(CommonCodes.ROLE_CACHE_KEY, lstData);
				cacheValueWrapper = cache.get(CommonCodes.ROLE_CACHE_KEY);
				cachedData = (List<RoleMaster>) cacheValueWrapper.get();
			}
			Optional<RoleMaster> roleMaster = cachedData.stream()
					.filter(rolemaster -> ((rolemaster.getAppId() != null) && (rolemaster.getAppId().equals(appId)))
							&& (rolemaster.getId().equals(id)))
					.findFirst();
			if (roleMaster.isPresent()) {
				return roleMaster.get();
			} else {
				throw new BusinessException(HttpStatus.NOT_FOUND.value(),
						ExceptionMessage.DATA_NOT_FOUND.getValue() + id);
			}
		} catch (Exception e) {
			log.error(ExceptionMessage.ERROR_ACCESSING_CACHE.getValue(), e);
		}
		Optional<RoleMaster> roles = roleMasterRepository.findByIdAndAppId(id, appId);
		if (!roles.isPresent()) {
			throw new BusinessException(HttpStatus.NOT_FOUND.value(), ExceptionMessage.DATA_NOT_FOUND.getValue() + id);
		}
		return roles.get();
	}

	public RoleMaster updateBy(int id, RoleMaster r) {
		Optional<RoleMaster> role1 = roleMasterRepository.findById(id);
		List<RoleMaster> existingRoleCode = roleMasterRepository.findByRoleCodeAndIdNot(r.getRoleCode(), id);
		if (!existingRoleCode.isEmpty()) {
			throw new BusinessException(HttpStatus.BAD_REQUEST.value(),
					ExceptionMessage.DATA_ALREADY_EXISTS.getValue() + r.getRoleCode());
		}
		List<RoleMaster> existingRoleNameEnglish = roleMasterRepository
				.findByRoleNameEnglishAndIdNot(r.getRoleNameEnglish(), id);
		if (!existingRoleNameEnglish.isEmpty()) {
			throw new BusinessException(HttpStatus.BAD_REQUEST.value(),
					ExceptionMessage.DATA_ALREADY_EXISTS.getValue() + r.getRoleDescriptionEnglish());
		}
		List<RoleMaster> existingRoleNameArabic = roleMasterRepository
				.findByRoleNameArabicAndIdNot(r.getRoleNameArabic(), id);
		if (!existingRoleNameArabic.isEmpty()) {
			throw new BusinessException(HttpStatus.BAD_REQUEST.value(),
					ExceptionMessage.DATA_ALREADY_EXISTS.getValue() + r.getRoleNameArabic());
		}
		List<RoleMaster> existingRoleDescEnglish = roleMasterRepository
				.findByRoleDescriptionEnglishAndIdNot(r.getRoleDescriptionEnglish(), id);
		if (!existingRoleDescEnglish.isEmpty()) {
			throw new BusinessException(HttpStatus.BAD_REQUEST.value(),
					ExceptionMessage.DATA_ALREADY_EXISTS.getValue() + r.getRoleDescriptionEnglish());
		}
		List<RoleMaster> existingRoleDescArabic = roleMasterRepository
				.findByRoleDescriptionArabicAndIdNot(r.getRoleDescriptionArabic(), id);
		if (!existingRoleDescArabic.isEmpty()) {
			throw new BusinessException(HttpStatus.BAD_REQUEST.value(),
					ExceptionMessage.DATA_ALREADY_EXISTS.getValue() + r.getRoleDescriptionArabic());
		}
		if (!role1.isPresent()) {
			throw new BusinessException(HttpStatus.NOT_FOUND.value(), ExceptionMessage.DATA_NOT_FOUND.getValue() + id);
		}
		RoleMaster role = role1.get();
		role.setRoleCode(r.getRoleCode());
		role.setRoleNameEnglish(r.getRoleNameEnglish());
		role.setRoleNameArabic(r.getRoleNameArabic());
		role.setRoleDescriptionEnglish(r.getRoleDescriptionEnglish());
		role.setRoleDescriptionArabic(r.getRoleDescriptionArabic());
		role.setRollType(r.getRollType());
		role.setAppId(r.getAppId());
		role.setIsActive(r.getIsActive());
		role.setIsLicenceRequired(r.getIsLicenceRequired());
		role.setCanBeAssigned(r.getCanBeAssigned());
		role.setCreatedBy(r.getCreatedBy());
		role.setUpdatedBy(r.getUpdatedBy());
		role.setUpdatedDate(LocalDateTime.now());
		RoleMaster roleMaster = roleMasterRepository.save(role);
		try {
			Cache cache = cacheManager.getCache(CommonCodes.ROLE_CACHE_VALUE);
			log.debug(CommonCodes.CACHE_REFRESH);
			List<RoleMaster> roles = roleMasterRepository.findAllByOrderByCreatedDateDesc();
			cache.put(CommonCodes.ROLE_CACHE_KEY, roles);
		} catch (Exception cacheException) {
			log.error(ExceptionMessage.ERROR_REFRESHING_CACHE.getValue(), cacheException);
		}
		return roleMaster;
	}

	public void deleteBy(int id) {
		Optional<RoleMaster> role1 = roleMasterRepository.findById(id);
		if (role1.isPresent()) {
			RoleMaster role2 = role1.get();
			role2.setIsActive(!role2.getIsActive());
			roleMasterRepository.save(role2);
			try {
				Cache cache = cacheManager.getCache(CommonCodes.ROLE_CACHE_VALUE);
				log.debug(CommonCodes.CACHE_REFRESH);
				List<RoleMaster> role = roleMasterRepository.findAllByOrderByCreatedDateDesc();
				cache.put(CommonCodes.ROLE_CACHE_KEY, role);
			} catch (Exception cacheException) {
				log.error(ExceptionMessage.ERROR_REFRESHING_CACHE.getValue(), cacheException);
			}
		} else {
			throw new BusinessException(HttpStatus.NOT_FOUND.value(), ExceptionMessage.DATA_NOT_FOUND.getValue() + id);
		}
	}

	public List<RoleMaster> getAllBy(Pagination p, Integer appId) {
		int page = p.getPage();
		int length = p.getLength();
		String sort = p.getSort();
		String sortDir = p.getSortDir();
		String search = p.getSearch();
		try {
			Sort sorting = Sort.by(Sort.Direction.valueOf(sortDir), sort);

			PageRequest pageable = PageRequest.of(page, length, sorting);

			List<RoleMaster> cachedData = null;
			Cache cache = cacheManager.getCache(CommonCodes.ROLE_CACHE_VALUE);

			if (search != null && !search.isEmpty()) {

				if (cache == null || cache.get(CommonCodes.ROLE_CACHE_KEY) == null) {
					List<RoleMaster> role = roleMasterRepository.findAllByOrderByCreatedDateDesc();
					cache.put(CommonCodes.ROLE_CACHE_KEY, role);
					Cache.ValueWrapper cacheValueWrapper = cache.get(CommonCodes.ROLE_CACHE_KEY);
					cachedData = (List<RoleMaster>) cacheValueWrapper.get();
				} else {
					Cache.ValueWrapper cacheValueWrapper = cache.get(CommonCodes.ROLE_CACHE_KEY);
					if (cacheValueWrapper != null) {
						cachedData = (List<RoleMaster>) cacheValueWrapper.get();
					}
				}

				Predicate<RoleMaster> likeFilter = obj -> ((obj.getAppId() != null && obj.getAppId().equals(appId))
						|| (appId == null))
						&& ((obj.getRoleCode() != null
								&& obj.getRoleCode().toLowerCase().contains(search.toLowerCase()))
								|| (obj.getRoleNameArabic() != null
										&& obj.getRoleNameArabic().toLowerCase().contains(search.toLowerCase()))
								|| (obj.getRoleNameEnglish() != null
										&& obj.getRoleNameEnglish().toLowerCase().contains(search.toLowerCase()))
								|| (obj.getRoleDescriptionArabic() != null
										&& obj.getRoleDescriptionArabic().toLowerCase().contains(search.toLowerCase()))
								|| (obj.getRoleDescriptionEnglish() != null && obj.getRoleDescriptionEnglish()
										.toLowerCase().contains(search.toLowerCase())));

				List<RoleMaster> filteredData = cachedData.stream().filter(likeFilter).collect(Collectors.toList());

				int startIndex = page * length;
				int endIndex = startIndex + length;

				startIndex = Math.max(startIndex, 0);

				endIndex = Math.min(endIndex, filteredData.size());

				List<RoleMaster> pageData = new ArrayList<>();
				if (startIndex < endIndex) {
					pageData = filteredData.subList(startIndex, endIndex);
				}
				return pageData;
			} else {
				Cache.ValueWrapper cacheValueWrapper = cache.get(CommonCodes.ROLE_CACHE_KEY);
				if (cacheValueWrapper == null || cacheValueWrapper.get() == null) {
					List<RoleMaster> role = roleMasterRepository.findAllByOrderByCreatedDateDesc();
					cache.put(CommonCodes.ROLE_CACHE_KEY, role);
					cachedData = role;
				} else {
					cachedData = (List<RoleMaster>) cacheValueWrapper.get();
				}

				List<RoleMaster> roleMasters = cachedData.stream().filter(rolemaster -> {
					Integer roleAppId = rolemaster.getAppId();
					return roleAppId != null && roleAppId.equals(appId);
				}).collect(Collectors.toList());

				int startIndex = (int) pageable.getOffset();
				int endIndex = Math.min((startIndex + pageable.getPageSize()), roleMasters.size());
				List<RoleMaster> pageData = roleMasters.subList(startIndex, endIndex);

				if (!pageData.isEmpty()) {
					return pageData;
				}
			}
		} catch (Exception e) {
			log.error(ExceptionMessage.ERROR_ACCESSING_CACHE.getValue(), e);
		}
		PageRequest pageable = PageRequest.of(page, length, Sort.by(Sort.Direction.valueOf(sortDir), sort));
		if (search != null && !search.isEmpty()) {
			return roleMasterRepository.findByPagination(search, pageable, appId);
		} else {
			return roleMasterRepository.findByPaginationWithOutSearch(pageable, appId);
		}
	}

	public long getTotalRecord(Integer appId) {
		List<RoleMaster> cachedData = null;
		try {
			Cache cache = cacheManager.getCache(CommonCodes.ROLE_CACHE_VALUE);
			Cache.ValueWrapper cacheValueWrapper = cache.get(CommonCodes.ROLE_CACHE_KEY);
			if (cacheValueWrapper != null) {
				cachedData = (List<RoleMaster>) cacheValueWrapper.get();
			} else {
				List<RoleMaster> lstData = roleMasterRepository.findAllByOrderByCreatedDateDesc();
				cache.put(CommonCodes.ROLE_CACHE_KEY, lstData);
				cacheValueWrapper = cache.get(CommonCodes.ROLE_CACHE_KEY);
				cachedData = (List<RoleMaster>) cacheValueWrapper.get();
			}
			List<RoleMaster> roleMasters = cachedData.stream().filter(rolemaster -> {
				Integer roleAppId = rolemaster.getAppId();
				return roleAppId != null && roleAppId.equals(appId);
			}).collect(Collectors.toList());

			return roleMasters.size();
		} catch (Exception e) {
			log.error(ExceptionMessage.ERROR_ACCESSING_CACHE.getValue(), e);
		}
		cachedData = roleMasterRepository.findByAppId(appId);
		return cachedData.size();
	}

	public long getFilteredRecord(String search, Integer appId) {
		List<RoleMaster> cachedData = null;
		try {
			Cache cache = cacheManager.getCache(CommonCodes.ROLE_CACHE_VALUE);
			Cache.ValueWrapper cacheValueWrapper = cache.get(CommonCodes.ROLE_CACHE_KEY);
			if (search != null && !search.isEmpty()) {
				if (cacheValueWrapper != null) {
					cachedData = (List<RoleMaster>) cacheValueWrapper.get();
				} else {
					List<RoleMaster> lstData = roleMasterRepository.findAllByOrderByCreatedDateDesc();
					cache.put(CommonCodes.ROLE_CACHE_KEY, lstData);
					cacheValueWrapper = cache.get(CommonCodes.ROLE_CACHE_KEY);
					cachedData = (List<RoleMaster>) cacheValueWrapper.get();
				}
				List<RoleMaster> lstRoleMasters = cachedData.stream().filter(obj -> {
					Integer roleAppId = obj.getAppId();
					return (roleAppId != null && roleAppId.equals(appId)) && ((obj.getRoleCode() != null
							&& obj.getRoleCode().toLowerCase().contains(search.toLowerCase()))
							|| (obj.getRoleNameArabic() != null
									&& obj.getRoleNameArabic().toLowerCase().contains(search.toLowerCase()))
							|| (obj.getRoleNameEnglish() != null
									&& obj.getRoleNameEnglish().toLowerCase().contains(search.toLowerCase()))
							|| (obj.getRoleDescriptionArabic() != null
									&& obj.getRoleDescriptionArabic().toLowerCase().contains(search.toLowerCase()))
							|| (obj.getRoleDescriptionEnglish() != null
									&& obj.getRoleDescriptionEnglish().toLowerCase().contains(search.toLowerCase())));
				}).collect(Collectors.toList());

				return lstRoleMasters.size();
			} else {
				if (cacheValueWrapper != null) {
					cachedData = (List<RoleMaster>) cacheValueWrapper.get();
				} else {
					List<RoleMaster> lstData = roleMasterRepository.findAllByOrderByCreatedDateDesc();
					cache.put(CommonCodes.ROLE_CACHE_KEY, lstData);
					cacheValueWrapper = cache.get(CommonCodes.ROLE_CACHE_KEY);
					cachedData = (List<RoleMaster>) cacheValueWrapper.get();
				}
				List<RoleMaster> roleMasters = cachedData.stream().filter(rolemaster -> {
					Integer roleAppId = rolemaster.getAppId();
					return roleAppId != null && roleAppId.equals(appId);
				}).collect(Collectors.toList());

				return roleMasters.size();
			}
		} catch (Exception e) {
			log.error(ExceptionMessage.ERROR_ACCESSING_CACHE.getValue(), e);
		}
		if (search != null && !search.isEmpty()) {
			List<RoleMaster> lstData = roleMasterRepository.findByPagination(search, null, appId);
			return lstData.size();
		} else {
			List<RoleMaster> lstData = roleMasterRepository.findByPaginationWithOutSearch(null, appId);
			return lstData.size();
		}
	}

	public List<RoleMaster> getAllData(RoleMasterFilter filter, Integer appId) {
		if ((filter.getRoleCode() == null || filter.getRoleCode().isEmpty())
				&& (filter.getRoleNameEnglish() == null || filter.getRoleNameEnglish().isEmpty())
				&& (filter.getRoleDescriptionEnglish() == null || filter.getRoleDescriptionEnglish().isEmpty())
				&& (filter.getRoleDescriptionArabic() == null || filter.getRoleDescriptionArabic().isEmpty())) {
			throw new BusinessException(HttpStatus.BAD_REQUEST.value(), ExceptionMessage.FEILDS_EMPTY.getValue());
		}
		try {
			List<RoleMaster> cachedData = null;
			Cache cache = cacheManager.getCache(CommonCodes.ROLE_CACHE_VALUE);
			Cache.ValueWrapper cacheValueWrapper = cache.get(CommonCodes.ROLE_CACHE_KEY);
			if (cacheValueWrapper != null) {
				cachedData = (List<RoleMaster>) cacheValueWrapper.get();
			} else {
				List<RoleMaster> lstData = roleMasterRepository.findAllByOrderByCreatedDateDesc();
				cache.put(CommonCodes.ROLE_CACHE_KEY, lstData);
				cacheValueWrapper = cache.get(CommonCodes.ROLE_CACHE_KEY);
				cachedData = (List<RoleMaster>) cacheValueWrapper.get();
			}
			List<RoleMaster> filteredData = cachedData.stream()
					.filter(rolemaster -> (rolemaster.getIsActive())
							&& ((rolemaster.getAppId() != null) && (rolemaster.getAppId().equals(appId)))
							&& (filter.getRoleCode() == null
									|| filter.getRoleCode().isEmpty()
									|| (rolemaster.getRoleCode() != null && rolemaster.getRoleCode().equalsIgnoreCase(
											filter.getRoleCode())))
							&& (filter.getRoleNameEnglish() == null
									|| filter.getRoleNameEnglish().isEmpty()
									|| (rolemaster.getRoleNameEnglish() != null
											&& rolemaster
													.getRoleNameEnglish()
													.equalsIgnoreCase(filter.getRoleNameEnglish())))
							&& (filter.getRoleNameArabic() == null || filter.getRoleNameArabic().isEmpty()
									|| (rolemaster.getRoleNameArabic() != null && rolemaster.getRoleNameArabic()
											.equalsIgnoreCase(filter.getRoleNameArabic())))
							&& ((filter.getRoleDescriptionEnglish() == null
									|| filter.getRoleDescriptionEnglish().isEmpty())
									|| (rolemaster.getRoleDescriptionEnglish() != null && rolemaster
											.getRoleDescriptionEnglish().equals(filter.getRoleDescriptionEnglish())))
							&& (filter.getRoleDescriptionArabic() == null || filter.getRoleDescriptionArabic().isEmpty()
									|| (rolemaster.getRoleDescriptionArabic() != null
											&& rolemaster.getRoleDescriptionArabic()
													.equalsIgnoreCase(filter.getRoleDescriptionArabic()))))
					.collect(Collectors.toList());
			if (!filteredData.isEmpty()) {
				return filteredData;
			}

			throw new BusinessException(ExceptionMessage.DATA_NOT_FOUND.getValue());
		} catch (Exception e) {
			log.error(ExceptionMessage.ERROR_ACCESSING_CACHE.getValue(), e);
		}
		List<RoleMaster> filteredData = roleMasterRepository.findAllByFilter(filter.getRoleCode(),
				filter.getRoleNameEnglish(), filter.getRoleNameArabic(), filter.getRoleDescriptionEnglish(),
				filter.getRoleDescriptionArabic(), appId);
		if (!filteredData.isEmpty()) {
			return filteredData;
		}

		throw new BusinessException(ExceptionMessage.DATA_NOT_FOUND.getValue());

	}

	public RoleMaster getByCode(String roleCode) {

		return roleMasterRepository.findByRoleCode(roleCode);
	}

	public List<Map<String, ?>> getallModules() {

		List<AccessModule> lstAccessModules = this.accessModuleRepository.findByIsActive(true);
		List<Map<String, ?>> lstModules = new ArrayList<>();
		lstAccessModules.stream().forEach(objmodules -> {
			Map<String, Object> objModuleMap = new HashMap<>();
			objModuleMap.put("id", objmodules.getId());
			objModuleMap.put("module", objmodules.getModule());
			objModuleMap.put("module_description", objmodules.getModuleDescription());
			lstModules.add(objModuleMap);
		});
		return lstModules;
	}

	public List<RoleTypeResponse> getStkateholderById(Integer app_id, String roll_type) {
		List<RoleMaster> cachedData = null;
		try {
			Cache cache = cacheManager.getCache(CommonCodes.ROLE_CACHE_VALUE);
			Cache.ValueWrapper cacheValueWrapper = cache.get(CommonCodes.ROLE_CACHE_KEY);

			if (cacheValueWrapper != null) {
				cachedData = (List<RoleMaster>) cacheValueWrapper.get();
			} else {
				List<RoleMaster> lstData = roleMasterRepository.findAllByOrderByCreatedDateDesc();
				cache.put(CommonCodes.ROLE_CACHE_KEY, lstData);
				cacheValueWrapper = cache.get(CommonCodes.ROLE_CACHE_KEY);
				cachedData = (List<RoleMaster>) cacheValueWrapper.get();
			}
			if (StringUtils.isEmpty(roll_type)) {
				List<RoleMaster> filteredList = cachedData.stream()
						.filter(rolemaster -> rolemaster.getIsActive()
								&& (rolemaster.getCanBeAssigned() != null && rolemaster.getCanBeAssigned())
								&& rolemaster.getAppId() != null && rolemaster.getAppId().equals(app_id)
								&& (StringUtils.isEmpty(roll_type) || (rolemaster.getRollType() != null
										&& rolemaster.getRollType().equalsIgnoreCase(roll_type)))
								&& !StringUtils.equalsAnyIgnoreCase(rolemaster.getRollType(), "Subrole"))
						.collect(Collectors.toList());
				return filteredList.stream().map(user -> modelMapper.map(user, RoleTypeResponse.class))
						.collect(Collectors.toList());
			} else {
				List<RoleMaster> filteredList = cachedData.stream()
						.filter(rolemaster -> (rolemaster.getIsActive())
								&& (rolemaster.getCanBeAssigned() != null && rolemaster.getCanBeAssigned())
								&& rolemaster.getAppId() != null && rolemaster.getAppId().equals(app_id)
								&& rolemaster.getRollType() != null
								&& rolemaster.getRollType().equalsIgnoreCase(roll_type))
						.collect(Collectors.toList());

				return filteredList.stream().map(user -> modelMapper.map(user, RoleTypeResponse.class))
						.collect(Collectors.toList());
			}
		} catch (Exception e) {
			log.error(ExceptionMessage.ERROR_ACCESSING_CACHE.getValue(), e);
		}
		if (roll_type == null || roll_type == "") {
			List<RoleMaster> lstData = this.roleMasterRepository.getAllStakeholderByAppId(app_id);

			return lstData.stream().map(user -> modelMapper.map(user, RoleTypeResponse.class))
					.collect(Collectors.toList());
		} else {
			List<RoleMaster> lstData = this.roleMasterRepository.getAllStakeholderByAppIdAndRollType(app_id, roll_type);
			return lstData.stream().map(user -> modelMapper.map(user, RoleTypeResponse.class))
					.collect(Collectors.toList());
		}
	}

//-------------------------------------------------------V2 service ----------------------------------------------
	
	public List<RoleMaster> getRolesByAttribute(String client, String role, String appId) {
		List<RoleAttribute> attributes;
		List<RoleMaster> roleMasterList = new ArrayList<>();
		attributes = roleAttributeRepository.findByNameAndValue(client, role, appId);
						
		for (RoleAttribute attribute : attributes) {
			List<RoleAttribute> lstRoleAtt = roleAttributeRepository.findByKeyClockRole(attribute.getKeyClockRole());
			RoleMaster roleMaster = new RoleMaster();
			roleMaster.setRoleId(attribute.getKeyClockRole().getId());
			roleMaster.setRoleCode(attribute.getKeyClockRole().getName());
			for (RoleAttribute objAttribute : lstRoleAtt) {
				mapRoleAttributeToRoleMaster(objAttribute, roleMaster);
			}

			roleMasterList.add(roleMaster);
		}

		return roleMasterList;
	}

	private void mapRoleAttributeToRoleMaster(RoleAttribute attribute, RoleMaster roleMaster) {
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(CommonCodes.DATE_FORMAT);
	    
	    switch (attribute.getName()) {
		case "id":
			roleMaster.setId(Integer.parseInt(attribute.getValue()));
			break;
		case "roleNameEnglish":
			roleMaster.setRoleNameEnglish(attribute.getValue());
			break;
		case "roleNameArabic":
			roleMaster.setRoleNameArabic(attribute.getValue());
			break;
		case "roleDescriptionEnglish":
			roleMaster.setRoleDescriptionEnglish(attribute.getValue());
			break;
		case "roleDescriptionArabic":
			roleMaster.setRoleDescriptionArabic(attribute.getValue());
			break;
		case "rollType":
			roleMaster.setRollType(attribute.getValue());
			break;
		case "isLicenceRequired":
			roleMaster.setIsLicenceRequired("true".equalsIgnoreCase(attribute.getValue()));
			break;
		case "canBeAssigned":
			roleMaster.setCanBeAssigned("true".equalsIgnoreCase(attribute.getValue()));
			break;
		case "appId":
			roleMaster.setAppId(attribute.getValue() == null ? null : Integer.parseInt(attribute.getValue()));
			break;
		case "createdBy":
			roleMaster.setCreatedBy(attribute.getValue());
			break;
		case "createdDate":
			roleMaster.setCreatedDate(attribute.getValue() == null ? null : attribute.getValue().isEmpty() ? null
					: attribute.getValue().contains("T") ? LocalDateTime.parse(attribute.getValue(), formatter)
							: LocalDate.parse(attribute.getValue()).atStartOfDay());
			break;
		case "updatedBy":
			roleMaster.setUpdatedBy(attribute.getValue());
			break;
		case "updatedDate":
			roleMaster.setUpdatedDate(attribute.getValue() == null ? null : attribute.getValue().isEmpty() ? null
					: attribute.getValue().contains("T") ? LocalDateTime.parse(attribute.getValue(), formatter)
							: LocalDate.parse(attribute.getValue()).atStartOfDay());
			break;
		case "isActive":
			roleMaster.setIsActive("true".equalsIgnoreCase(attribute.getValue()));
			break;
		case "portRid":
			roleMaster.setPortRid(attribute.getValue());
			break;
		}

	}
	
	public List<RoleMaster> allRoles() {
		try {
			Cache cache = cacheManager.getCache(CommonCodes.ROLE_CACHE_VALUE);
			List<RoleMaster> roleMasters = roleMasterRepository.findAllByOrderByCreatedDateDesc();
			cache.put(CommonCodes.ROLE_CACHE_KEY, roleMasters);
			log.debug(CommonCodes.DATA_STORED_IN_CACHE);
			return roleMasters;
		} catch (Exception e) {
			log.error(ExceptionMessage.ERROR_ACCESSING_CACHE.getValue(), e);
		}
		return roleMasterRepository.findAllByOrderByCreatedDateDesc();
	}

	public List<RoleMaster> rolesByIds(List<Integer> arrIds) {
		return this.roleMasterRepository.findAllById(arrIds);
	}

	public ResponseEntity<ApiResponse<?>> addV2(RoleMaster roleMaster, String token) {
		RedHatKeycloakRole roles = new RedHatKeycloakRole();
		roles.setName(roleMaster.getRoleNameEnglish());
		roles.setDescription(roleMaster.getRoleDescriptionEnglish());
		String result = userExternalService.roleCreateRedHat(roles, token);
		if (result != null) {
			JSONObject jsonObject = userExternalService.roleGetRedHat(roleMaster.getRoleNameEnglish(), token);

			if (jsonObject != null) {

				String id = jsonObject.getString("id");
				RoleRedHat roleRedHat = new RoleRedHat();
				roleRedHat.setId(id);
				roleRedHat.setName(jsonObject.getString("name"));
				roleRedHat.setDescription(jsonObject.getString("description"));
				roleRedHat.setContainerId(jsonObject.getString("containerId"));
				roleRedHat.setClientRole(jsonObject.getBoolean("clientRole"));
				roleRedHat.setComposite(jsonObject.getBoolean("composite"));

				Attributes attributes = new Attributes();
				
				RoleAttribute roleAttribute = roleAttributeRepository.findTopByOrderByIdDesc();
				String idValue = roleAttribute.getValue();
				Integer idIntValue = Integer.parseInt(idValue);
				Integer incrementedId = idIntValue + 1;
				List<String> lstId = new ArrayList<>();
				lstId.add(String.valueOf(incrementedId));
				attributes.setId(lstId);
				

				List<String> roleEnglish = new ArrayList<>();
				roleEnglish.add(roleMaster.getRoleDescriptionEnglish());
				attributes.setRoleNameEnglish(roleEnglish);

				List<Boolean> canBeAssigned = new ArrayList<>();
				canBeAssigned.add(roleMaster.getCanBeAssigned());
				attributes.setCanBeAssigned(canBeAssigned);

				List<String> createdByList = new ArrayList<>();
				createdByList.add(roleMaster.getCreatedBy());
				attributes.setCreatedBy(createdByList);
				
				List<String> updatedByList = new ArrayList<>();
				updatedByList.add(roleMaster.getUpdatedBy());
				attributes.setUpdatedBy(updatedByList);

				LocalDateTime currentDateTime = LocalDateTime.now();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
				String createdDateString = currentDateTime.format(formatter);
				
				
				List<String> createdDateList = new ArrayList<>();
				createdDateList.add(createdDateString);
				attributes.setCreatedDate(createdDateList);

				List<String> updatedDateList = new ArrayList<>();
				updatedDateList.add("");
				attributes.setUpdatedDate(updatedDateList);

				List<Boolean> isActiveList = new ArrayList<>();
				isActiveList.add(roleMaster.getIsActive());
				attributes.setIsActive(isActiveList);

				List<Boolean> isLicenceRequiredList = new ArrayList<>();
				isLicenceRequiredList.add(roleMaster.getIsLicenceRequired());
				attributes.setIsLicenceRequired(isLicenceRequiredList);

				List<String> roleDescriptionArabicList = new ArrayList<>();
				roleDescriptionArabicList.add(roleMaster.getRoleDescriptionArabic());
				attributes.setRoleDescriptionArabic(roleDescriptionArabicList);

				List<String> roleDescriptionEnglishList = new ArrayList<>();
				roleDescriptionEnglishList.add(roleMaster.getRoleDescriptionEnglish());
				attributes.setRoleDescriptionEnglish(roleDescriptionEnglishList);

				List<String> roleNameArabicList = new ArrayList<>();
				roleNameArabicList.add(roleMaster.getRoleNameArabic());
				attributes.setRoleNameArabic(roleNameArabicList);

				List<String> roleNameEnglishList = new ArrayList<>();
				roleNameEnglishList.add(roleMaster.getRoleDescriptionEnglish());
				attributes.setRoleNameEnglish(roleNameEnglishList);

				List<String> roleCodeList = new ArrayList<>();
				roleCodeList.add(roleMaster.getRoleCode());
				attributes.setRoleCode(roleCodeList);
				
				List<String> portRidList = new ArrayList<>();
				portRidList.add(roleMaster.getPortRid());
				attributes.setPortRid(portRidList);

				List<String> roleAppId = new ArrayList<>();
				roleAppId.add(String.valueOf(roleMaster.getAppId()));
				attributes.setAppId(roleAppId);

				List<String> rollTypeList = new ArrayList<>();
				rollTypeList.add(roleMaster.getRoleDescriptionEnglish());
				attributes.setRollType(rollTypeList);
				roleRedHat.setAttributes(attributes);
				userExternalService.attributeCreateRedhat(roleRedHat, id, token);

				PortAdminPolicy portAdminPolicy = new PortAdminPolicy();
				portAdminPolicy.setType("role");
				portAdminPolicy.setLogic("POSITIVE");
				portAdminPolicy.setDecisionStrategy("UNANIMOUS");
				portAdminPolicy.setName(jsonObject.getString("name") + "_Policy");
				List<PolicyRole> rolePolicy = new ArrayList<>();
				PolicyRole policyRole = new PolicyRole();
				policyRole.setId(id);
				policyRole.setRequired(true);
				rolePolicy.add(policyRole);
				userExternalService.policyCreate(portAdminPolicy, token);
				apiResponse = new ApiResponse<>(HttpStatus.OK.value(), "Role added successfully", roleMaster);
				return ResponseEntity.status(HttpStatus.OK.value()).body(apiResponse);

			} else {
				apiResponse = new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "id not present", null);
				return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(apiResponse);
			}

		} else {
			apiResponse = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Role name already exist", null);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(apiResponse);
		}

	}

	public ResponseEntity<PaginationResponse> rolePagination(Pagination pagination) {
		String sort = pagination.getSort();
		String sortDir = pagination.getSortDir();
		String search = pagination.getSearch().trim();
		UserBranchKeys keys = pagination.getKeys();
		String client = keys.getAppId();
		String portRid = keys.getPortRid();
		long totalRecored;
		long filteredRecored;
		List<KeyClockRole> list = new ArrayList<>();
		List<RoleMaster> rlist = null;

		if (client == null || client.isEmpty()) {
			log.error("Client id is null");
			throw new BusinessException(HttpStatus.BAD_REQUEST.value(), "Client id is null");
		}
		if (portRid == null || portRid.isEmpty()) {
			log.error("Port id is null");
			throw new BusinessException(HttpStatus.BAD_REQUEST.value(), "Port id is null");
		}

		list = keyClockRoleRepository.getAllRoleData(client.toString(),portRid);
		List<RoleMaster> updatedRList = convertToRoleMaster(list);
		rlist = updatedRList.stream()
				.filter(obj -> obj.getIsActive() == true).collect(Collectors.toList());
		totalRecored = rlist.size();
		filteredRecored = rlist.size();
		if (search != null && !search.isEmpty()) {

			rlist = rlist.stream()
					.filter(obj -> ((obj.getRoleCode() != null
							&& obj.getRoleCode().toLowerCase().contains(search.toLowerCase()))
							|| (obj.getRoleNameArabic() != null
									&& obj.getRoleNameArabic().toLowerCase().contains(search.toLowerCase()))
							|| (obj.getRoleNameEnglish() != null
									&& obj.getRoleNameEnglish().toLowerCase().contains(search.toLowerCase()))
							|| (obj.getRoleDescriptionArabic() != null
									&& obj.getRoleDescriptionArabic().toLowerCase().contains(search.toLowerCase()))
							|| (obj.getRoleDescriptionEnglish() != null
									&& obj.getRoleDescriptionEnglish().toLowerCase().contains(search.toLowerCase()))
							|| (obj.getRollType() != null
									&& obj.getRollType().toString().toLowerCase().contains(search.toLowerCase()))
							|| (obj.getCreatedDate() != null
									&& obj.getCreatedDate().toString().toLowerCase().contains(search.toLowerCase()))
							|| (obj.getIsActive() != null
									&& obj.getIsActive().equals("active".contains(search.toLowerCase())))))
					.collect(Collectors.toList());
			filteredRecored = rlist.size();
		}

		if (sortDir.equalsIgnoreCase(CommonCodes.ASCENDING)) {
			if (sort.equalsIgnoreCase("roleCode")) {
				rlist = rlist.stream().sorted(Comparator.comparing(RoleMaster::getRoleCode))
						.collect(Collectors.toList());
			} else if (sort.equalsIgnoreCase("rollType")) {
				rlist = rlist.stream().sorted(Comparator.comparing(RoleMaster::getRollType))
						.collect(Collectors.toList());
			} else if (sort.equalsIgnoreCase("roleNameEnglish")) {
				rlist = rlist.stream().sorted(Comparator.comparing(RoleMaster::getRoleNameEnglish))
						.collect(Collectors.toList());
			} else if (sort.equalsIgnoreCase("roleDescriptionEnglish")) {
				rlist = rlist.stream().sorted(Comparator.comparing(RoleMaster::getRoleDescriptionEnglish))
						.collect(Collectors.toList());
			} else if (sort.equalsIgnoreCase("createdDate")) {
				rlist = rlist.stream().sorted(Comparator.comparing(RoleMaster::getCreatedDate))
						.collect(Collectors.toList());
			} else if (sort.equalsIgnoreCase("isActive")) {
				rlist = rlist.stream().sorted(Comparator.comparing(RoleMaster::getIsActive))
						.collect(Collectors.toList());
			}
		} else {
			if (sort.equalsIgnoreCase("roleCode")) {
				rlist = rlist.stream().sorted(Comparator.comparing(RoleMaster::getRoleCode).reversed())
						.collect(Collectors.toList());
			} else if (sort.equalsIgnoreCase("rollType")) {
				rlist = rlist.stream().sorted(Comparator.comparing(RoleMaster::getRollType).reversed())
						.collect(Collectors.toList());
			} else if (sort.equalsIgnoreCase("roleNameEnglish")) {
				rlist = rlist.stream().sorted(Comparator.comparing(RoleMaster::getRoleNameEnglish).reversed())
						.collect(Collectors.toList());
			} else if (sort.equalsIgnoreCase("roleDescriptionEnglish")) {
				rlist = rlist.stream().sorted(Comparator.comparing(RoleMaster::getRoleDescriptionEnglish).reversed())
						.collect(Collectors.toList());
			} else if (sort.equalsIgnoreCase("createdDate")) {
				rlist = rlist.stream().sorted(Comparator.comparing(RoleMaster::getCreatedDate).reversed())
						.collect(Collectors.toList());
			} else if (sort.equalsIgnoreCase("isActive")) {
				rlist = rlist.stream().sorted(Comparator.comparing(RoleMaster::getIsActive).reversed())
						.collect(Collectors.toList());
			}
		}

		PageRequest pageable = PageRequest.of(pagination.getPage(), pagination.getLength());
		rlist = rlist.stream().skip(pageable.getOffset()).limit(pageable.getPageSize()).collect(Collectors.toList());

		PaginationResponse response = new PaginationResponse(totalRecored, filteredRecored, rlist);

		return ResponseEntity.ok(response);
	}

	private List<RoleMaster> convertToRoleMaster(List<KeyClockRole> keyClockRole) {
		List<RoleMaster> roleMasterlst = new ArrayList<>();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(CommonCodes.DATE_FORMAT);

		for (KeyClockRole kc : keyClockRole) {
			RoleMaster roleMaster = new RoleMaster();
			roleMaster.setRoleCode(kc.getName());
			roleMaster.setRoleId(kc.getId());
			for (RoleAttribute attribute : kc.getRoleAttributes()) {
				switch (attribute.getName()) {
				case "id":
					roleMaster.setId(Integer.parseInt(attribute.getValue()));
					break;
				case "roleNameEnglish":
					roleMaster.setRoleNameEnglish(attribute.getValue());
					break;
				case "roleNameArabic":
					roleMaster.setRoleNameArabic(attribute.getValue());
					break;
				case "roleDescriptionEnglish":
					roleMaster.setRoleDescriptionEnglish(attribute.getValue());
					break;
				case "roleDescriptionArabic":
					roleMaster.setRoleDescriptionArabic(attribute.getValue());
					break;
				case "rollType":
					roleMaster.setRollType(attribute.getValue());
					break;
				case "isLicenceRequired":
					roleMaster.setIsLicenceRequired("true".equalsIgnoreCase(attribute.getValue()));
					break;
				case "canBeAssigned":
					roleMaster.setCanBeAssigned("true".equalsIgnoreCase(attribute.getValue()));
					break;
				case "appId":
					roleMaster.setAppId(attribute.getValue() == null ? null : Integer.parseInt(attribute.getValue()));
					break;
				case "createdBy":
					roleMaster.setCreatedBy(attribute.getValue());
					break;
				case "createdDate":
					roleMaster.setCreatedDate(attribute.getValue() == null ? null : attribute.getValue().isEmpty() ? null
							: attribute.getValue().contains("T") ? LocalDateTime.parse(attribute.getValue(), formatter)
									: LocalDate.parse(attribute.getValue()).atStartOfDay());
					break;
				case "updatedBy":
					roleMaster.setUpdatedBy(attribute.getValue());
					break;
				case "updatedDate":
					roleMaster.setUpdatedDate(attribute.getValue() == null ? null : attribute.getValue().isEmpty() ? null
							: attribute.getValue().contains("T") ? LocalDateTime.parse(attribute.getValue(), formatter)
									: LocalDate.parse(attribute.getValue()).atStartOfDay());
					break;
				case "isActive":
					roleMaster.setIsActive("true".equalsIgnoreCase(attribute.getValue()));
					break;
				case "portRid":
					roleMaster.setPortRid(attribute.getValue());
					break;
				}

			}
			roleMasterlst.add(roleMaster);
		}
		return roleMasterlst;
	}

	public RoleMaster getByRoleId(String id, String client) {

		List<KeyClockRole> lstkr = new ArrayList<>();
		KeyClockRole kr = keyClockRoleRepository.finById(id, client);
		if (kr == null) {
			log.error("Id not found");
			throw new BusinessException(HttpStatus.NOT_FOUND.value(), "Id not found");
		}
		lstkr.add(kr);
		List<RoleMaster> lstrm = convertToRoleMaster(lstkr);
		return lstrm.get(0);
	}
	
	public RoleMaster update(RoleMaster roleMaster, String id, String token) {
		String getAttributes = userExternalService.getAttributes(id, token);
		if (getAttributes != null) {
			ObjectMapper objectMapper = new ObjectMapper();
			RoleRedHat roleRedHat = new RoleRedHat();
			try {
				roleRedHat = objectMapper.readValue(getAttributes, RoleRedHat.class);
			} catch (Exception e) {
				e.getMessage();
			}
			Attributes attributes = roleRedHat.getAttributes();

			List<Boolean> isActiveList = new ArrayList<>();
			isActiveList.add(roleMaster.getIsActive());
			attributes.setIsActive(isActiveList);

			LocalDateTime currentDateTime = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
			String updatedDateString = currentDateTime.format(formatter);
			List<String> updatedDateList = new ArrayList<>();
			updatedDateList.add(updatedDateString);
			attributes.setUpdatedDate(updatedDateList);

			List<String> updatedByList = new ArrayList<>();
			updatedByList.add(roleMaster.getUpdatedBy());
			attributes.setUpdatedBy(updatedByList);

			roleRedHat.setAttributes(attributes);
			userExternalService.attributeCreateRedhat(roleRedHat, id, token);
			return roleMaster;

		} else {
			throw new BusinessException(HttpStatus.NOT_FOUND.value(), ExceptionMessage.DATA_NOT_FOUND.getValue() + id);
		}

	}

	public void delete(String id,String token) {
		String getAttributes = userExternalService.getAttributes(id, token);
		if (getAttributes != null) {
			ObjectMapper objectMapper = new ObjectMapper();

			try {
				RoleRedHat roleRedHat = objectMapper.readValue(getAttributes, RoleRedHat.class);
				Attributes attributes = roleRedHat.getAttributes();	
				List<Boolean> isActiveList = new ArrayList<>();
				isActiveList.add(false);
				attributes.setIsActive(isActiveList);
				roleRedHat.setAttributes(attributes);
				userExternalService.attributeCreateRedhat(roleRedHat, id, token);

			} catch (Exception e) {
				e.getMessage();
			}

		} else {
			throw new BusinessException(HttpStatus.NOT_FOUND.value(), ExceptionMessage.DATA_NOT_FOUND.getValue() + id);
		}
	}

}
