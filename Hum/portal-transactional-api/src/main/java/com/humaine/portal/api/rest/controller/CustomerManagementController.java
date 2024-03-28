package com.humaine.portal.api.rest.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.humaine.admin.api.constants.PermissionConstants;
import com.humaine.admin.api.dto.CustomerDTO;
import com.humaine.admin.api.dto.CustomerListing;
import com.humaine.admin.api.dto.DeleteAccountRequest;
import com.humaine.admin.api.dto.PaginatedRequest;
import com.humaine.admin.api.dto.PaginationSort;
import com.humaine.admin.api.enums.PaginationSortDirection;
import com.humaine.admin.api.log.config.ActivityLog;
import com.humaine.admin.api.projection.model.AccountEventsDetails;
import com.humaine.admin.api.service.MapCustomerIndustryService;
import com.humaine.portal.api.enums.UserEvents;
import com.humaine.portal.api.exception.APIException;
import com.humaine.portal.api.model.Account;
import com.humaine.portal.api.model.AccountAdminDetailsNotExist;
import com.humaine.portal.api.request.dto.RegistrationRequest;
import com.humaine.portal.api.rest.repository.AccountAdminRepository;
import com.humaine.portal.api.rest.repository.CustomerAdminRepository;
import com.humaine.portal.api.rest.repository.UserAdminEventRepository;
import com.humaine.portal.api.util.DateUtils;
import com.humaine.portal.api.util.ErrorMessageUtils;
import com.humaine.portal.api.util.ResponseBuilder;
import com.humaine.portal.api.util.TransactionInfo;

@RestController
@RequestMapping("/customers")
public class CustomerManagementController {

	@Autowired
	CustomerAdminRepository customerRepository;

	@Autowired
	UserAdminEventRepository userEventRepository;

	@Autowired
	MapCustomerIndustryService mapCustomerIndustryService;

	@Autowired
	private AccountAdminRepository accountRepository;

	@Autowired
	private ErrorMessageUtils errorMessageUtils;

	private static final Logger log = LogManager.getLogger(CustomerManagementController.class);

//	@PreAuthorize("hasAnyAuthority('" + PermissionConstants.CUSTOMER_LIST + "')")
	@PostMapping(value = "", headers = { "Version=V1" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TransactionInfo> getAllCustomers(@RequestBody PaginatedRequest request) {
		log.info("Get All Customers call starts... ");
		if (request.getPage() == null)
			request.setPage(0);
		if (request.getSize() == null)
			request.setSize(10);
		if (request.getSort() == null) {
			request.setSort(new PaginationSort("registeredOn", PaginationSortDirection.DESC));
		}
		Sort sort = Sort.by(request.getSort().getField());
		if (PaginationSortDirection.DESC.equals(request.getSort().getDirection())) {
			sort = sort.descending();
		}
		PageRequest pageRequest = PageRequest.of(request.getPage(), request.getSize(), sort);
		org.springframework.data.domain.Page<CustomerListing> customers = customerRepository
				.findAllCustomers(pageRequest);

		org.springframework.data.domain.Page<CustomerDTO> result = new PageImpl<>(customers.stream().map(e -> {
			return new CustomerDTO(e);
		}).collect(Collectors.toList()), customers.getPageable(), customers.getTotalElements());
		log.info("Get All Customers call ends... ");
		return ResponseBuilder.buildResponse(result);
	}

//	@PreAuthorize("hasAnyAuthority('" + PermissionConstants.CUSTOMER_VIEW + "')")
	@ActivityLog("View Customer Info: [" + "AccountId:{{accountId}}]")
	@GetMapping(value = "/{accountId}", headers = { "Version=V1" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TransactionInfo> getAccountInfo(@PathVariable("accountId") Long accountId) throws Exception {
		Optional<Account> account = accountRepository.findById(accountId);
		if ((account.isEmpty()) || (account.isPresent() && account.get().getDeleted() == true)) {
			throw new APIException(this.errorMessageUtils.getMessageWithCode("api.error.account.details.not.found",
					new Object[] {}, "api.error.account.details.not.found.code"));
		}

//		List<Page> pages = new ArrayList<Page>();
//		if (account.get().getPages() != null) {
//			pages = account.get().getPages().stream()
//					.filter(data -> data.getPageName().equals(Pages.HOME_LANDINGPAGE.getPageName()))
//					.collect(Collectors.toList());
//			account.get().getPages().clear();
//			if (pages != null) {
//				account.get().getPages().addAll(pages);
//			}
//		}
		if (account.get().getBusinessInformation() == null) {
			Account acc = new Account();
			acc.setId(account.get().getId());
			AccountAdminDetailsNotExist result = new AccountAdminDetailsNotExist(account.get());
			result.setStatus(false);
			return ResponseBuilder.buildResponse(result);
		}
		AccountAdminDetailsNotExist result = new AccountAdminDetailsNotExist(account.get());
		result.setStatus(account.get().getBusinessInformation() != null);
		return ResponseBuilder.buildResponse(result);
	}

//	@PreAuthorize("hasAnyAuthority('" + PermissionConstants.CUSTOMER_EDIT + "')")
	@ActivityLog("Edit Customer Info: [" + "AccountId:{{accountId}}]")
	@PutMapping(value = "", headers = { "Version=V1" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TransactionInfo> updateAccountInfo(@Valid @RequestBody RegistrationRequest request)
			throws Exception {
		Optional<Account> account = accountRepository.findById(request.getAccountID());
		if ((account.isEmpty()) || (account.isPresent() && account.get().getDeleted() == true)) {
			throw new APIException(this.errorMessageUtils.getMessageWithCode("api.error.account.details.not.found",
					new Object[] {}, "api.error.account.details.not.found.code"));
		}
		Account acc = account.get();
		acc.setRegistrationInfo(request);
		accountRepository.save(acc);
		return ResponseBuilder.buildMessageCodeResponse(errorMessageUtils
				.getMessageWithCode("api.customer-info.save.success", null, "api.customer-info.save.success.code"));
	}

//	@PreAuthorize("hasAnyAuthority('" + PermissionConstants.CUSTOMER_DELETE + "')")
	@ActivityLog("Deletes Customer Info: [" + "AccountId:{{accountId}}]")
	@DeleteMapping(value = "delete", headers = { "Version=V1" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TransactionInfo> deleteAccountInfo(@Valid @RequestBody DeleteAccountRequest request)
			throws Exception {
		List<Long> ids = accountRepository.getAccountByIds(request.getIds());
		List<Long> notFoundIds = request.getIds().stream().filter(e -> {
			return !ids.contains(e);
		}).collect(Collectors.toList());

		if (!notFoundIds.isEmpty()) {
			throw new APIException(this.errorMessageUtils.getMessageWithCode("api.error.ids.not.found",
					new Object[] { notFoundIds.toString() }, "api.error.ids.not.found.code"));
		}

		accountRepository.deleteAccountByIds(request.getIds());

		return ResponseBuilder.buildMessageCodeResponse(errorMessageUtils.getMessageWithCode(
				"api.customer-info.deleted.success", null, "api.customer-info.deleted.success.code"));
	}

//	@PreAuthorize("hasAnyAuthority('" + PermissionConstants.CUSTOMER_VIEW + "')")
	@ActivityLog("View Customer eventsData Info: [" + "AccountId:{{accountId}}]")
	@GetMapping(value = "/eventsdata/{accountId}", headers = {
			"Version=V1" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TransactionInfo> getAccountEventsInfo(@PathVariable("accountId") Long accountId)
			throws Exception {
		Optional<Account> account = accountRepository.findById(accountId);
		if ((account.isEmpty()) || (account.isPresent() && account.get().getDeleted() == true)) {
			throw new APIException(this.errorMessageUtils.getMessageWithCode("api.error.account.details.not.found",
					new Object[] {}, "api.error.account.details.not.found.code"));
		}

		AccountEventsDetails eventDetails = userEventRepository.AccoutWiseEventsDetails(accountId,
				DateUtils.getFirstDayOfCurrentMonth(), DateUtils.getLastDayOfCurrentMonth(), UserEvents.BUY.value(),
				UserEvents.PRODVIEW.value());
		return ResponseBuilder.buildResponse(eventDetails);
	}
};