package com.humaine.collection.api.rest.service.impl;

import java.time.OffsetDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.humaine.collection.api.enums.UserEvents;
import com.humaine.collection.api.es.repository.impl.ESPageLoadDataRespository;
import com.humaine.collection.api.es.repository.impl.ESUserEventRepository;
import com.humaine.collection.api.exception.APIException;
import com.humaine.collection.api.model.PageLoadData;
import com.humaine.collection.api.model.Product;
import com.humaine.collection.api.model.User;
import com.humaine.collection.api.model.UserEvent;
import com.humaine.collection.api.request.dto.PageLoadEventRequest;
import com.humaine.collection.api.request.dto.UserEventRequest;
import com.humaine.collection.api.rest.repository.InventoryRepository;
import com.humaine.collection.api.rest.repository.PageLoadDataRepository;
import com.humaine.collection.api.rest.repository.ProductRepository;
import com.humaine.collection.api.rest.repository.UserEventRepository;
import com.humaine.collection.api.rest.repository.UserRepository;
import com.humaine.collection.api.rest.service.SaleService;
import com.humaine.collection.api.rest.service.UserEventService;
import com.humaine.collection.api.rest.service.UserService;
import com.humaine.collection.api.rest.service.UserSessionService;
import com.humaine.collection.api.util.DateUtils;
import com.humaine.collection.api.util.ErrorMessageUtils;

@Service
@Transactional
public class UserEventServiceImpl implements UserEventService {

	private static final Logger log = LogManager.getLogger(UserEventServiceImpl.class);

	@Autowired
	private UserEventRepository userEventRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private UserSessionService userSessionService;

	@Autowired
	private SaleService saleService;

	@Autowired
	private ErrorMessageUtils errorMessageUtils;

	@Autowired
	private InventoryRepository inventoryRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PageLoadDataRepository pageLoadDataRepository;

	@Autowired
	private ESUserEventRepository esUserEventRepository;

	@Autowired
	private ESPageLoadDataRespository esPageLoadDataRespository;

	@Autowired
	private ProductRepository productRepository;

	@Value("${product.default.title}")
	private String defaultProductTitle;

	@Override
	public UserEvent addOrEditUserEvent(UserEventRequest userEventRequest) throws APIException {
		// genrate unique uuid for UserEvent

		// get current Timestemp
		OffsetDateTime currentTimestemp = DateUtils.getCurrentTimestemp();

		UserEvent userEvent = null;
		switch (userEventRequest.getEventID()) {
		case START:
			userEventRequest.setProductID(null);
			log.info("Start Event Request with Session ID: {}", userEventRequest.getSessionID());
			this.userService.addOrEditUser(userEventRequest);
			userEvent = this.saveUserEvent(userEventRequest, currentTimestemp);
			this.userSessionService.startUsersession(userEventRequest, currentTimestemp);
			break;

		case NAV:
		case PRODVIEW:
		case ADDCART:
		case REMCART:
		case ADDLIST:
		case REMLIST:
		case REVIEW:
		case RATE:
			log.info("{} Event Request with Session ID: {}", userEventRequest.getEventID().value(),
					userEventRequest.getSessionID());
			userEvent = this.saveUserEvent(userEventRequest, currentTimestemp);
			break;

		case DISCOVER:
		case NEWSLETTER_SUBSCRIBE:
		case SEARCH:
		case MENU:
		case BACK_NAV:
		case SAVE_FOR_LATER:
		case PROD_RETURN:
		case VISIT_BLOG_POST:
		case VISIT_SOCIAL_MEDIA:
		case DELETE:
			log.info("{} Event Request with Session ID: {}", userEventRequest.getEventID().value(),
					userEventRequest.getSessionID());
			userEvent = this.saveUserEvent(userEventRequest, currentTimestemp);
			break;

		case BUY:
			log.info("Buy Event Request with Session ID: {}", userEventRequest.getSessionID());
			userEvent = this.saveUserEvent(userEventRequest, currentTimestemp);
			this.saleService.addSale(userEventRequest, currentTimestemp, userEvent);
			break;

		case END:
			log.info("Session End Event Request with Session ID: {}", userEventRequest.getSessionID());
			userEvent = this.saveUserEvent(userEventRequest, currentTimestemp);
			this.userSessionService.endUsersession(userEventRequest, currentTimestemp);

		case TEXT_HIGHLIGHT:
			userEvent = this.saveUserEvent(userEventRequest, currentTimestemp);
			break;

		case DOUBLE_CLICK:
			userEvent = this.saveUserEvent(userEventRequest, currentTimestemp);
			break;

		case IMG_VIEW:
			userEvent = this.saveUserEvent(userEventRequest, currentTimestemp);
			break;
		case NEW_TAB:
			userEvent = this.saveUserEvent(userEventRequest, currentTimestemp);
			break;
		default:
			log.error("Invalid Event Request with Session ID: {}", userEventRequest.getSessionID());
			break;
		}
		esUserEventRepository.indexUserEvent(userEvent.getId());
		return userEvent;
	}

	private UserEvent saveUserEvent(UserEventRequest userEventRequest, OffsetDateTime currentTimestemp) {
		log.debug("Inside save User Event");
		List<UserEvents> allowedProductMetaData = List.of(UserEvents.BUY);

		if (userEventRequest.getProductMetaData() != null
				&& !allowedProductMetaData.contains(userEventRequest.getEventID())) {
			userEventRequest.setProductMetaData(null);
		}

		if (StringUtils.isBlank(userEventRequest.getUserID())) {
			log.error("User Id is Empty");
			throw new APIException(errorMessageUtils.getMessageWithCode("api.error.usereventrequest.userID.null", null,
					"api.error.usereventrequest.userID.null.code"));
		}

		if (UserEvents.RATE.equals(userEventRequest.getEventID())) {
			if (userEventRequest.getRatingValue() == null) {
				log.error("rating value is empty");
				throw new APIException(errorMessageUtils.getMessageWithCode("api.error.rating-value.null", null,
						"api.error.rating-value.null.code"));
			}
		}

		if (userEventRequest.getEventID().equals(UserEvents.START)) {
			if (StringUtils.isBlank(userEventRequest.getDeviceId())) {
				log.error("Device Id is Empty");
				throw new APIException(errorMessageUtils.getMessageWithCode("api.error.deviceId.null", null,
						"api.error.deviceId.null.code"));
			}
			if (StringUtils.isBlank(userEventRequest.getDeviceType())) {
				log.error("DeviceType is Empty");
				throw new APIException(errorMessageUtils.getMessageWithCode("api.error.deviceType.null", null,
						"api.error.deviceType.null.code"));
			}
		}

		if (!userEventRequest.getEventID().equals(UserEvents.START)) {
			User user = this.userRepository.findByUserAndAccountId(userEventRequest.getUserID(),
					userEventRequest.getAccountID());
			if (user == null) {
				throw new APIException(errorMessageUtils.getMessageWithCode(
						"api.error.usereventrequest.userID.not.exist", new Object[] { userEventRequest.getUserID() },
						"api.error.usereventrequest.userID.not.exist.code"));
			}
			this.userSessionService.validateSession(userEventRequest.getSessionID());
		}

		List<UserEvents> nonProductRelatedEvents = List.of(UserEvents.NAV, UserEvents.END, UserEvents.START,
				UserEvents.DISCOVER, UserEvents.NEWSLETTER_SUBSCRIBE, UserEvents.MENU, UserEvents.SEARCH,
				UserEvents.BACK_NAV, UserEvents.VISIT_BLOG_POST, UserEvents.VISIT_SOCIAL_MEDIA,
				UserEvents.TEXT_HIGHLIGHT, UserEvents.DOUBLE_CLICK, UserEvents.NEW_TAB);

		List<UserEvents> allowProductsDataOptionals = List.of(UserEvents.SEARCH);

		if ((!nonProductRelatedEvents.contains(userEventRequest.getEventID())
				|| allowProductsDataOptionals.contains(userEventRequest.getEventID())
						&& !StringUtils.isEmpty(userEventRequest.getProductID()))) {
			if (StringUtils.isBlank(userEventRequest.getProductID())) {
				log.error("Empty Product ID");
				throw new APIException(errorMessageUtils.getMessageWithCode("api.error.usereventrequest.productId.null",
						null, "api.error.usereventrequest.productId.null.code"));
			}

			Product inventory = productRepository.getProductsByAccountAndId(userEventRequest.getAccountID(),
					userEventRequest.getProductID());
			if (inventory == null) {
				log.error("Product not found with Product ID: {}", userEventRequest.getProductID());
				throw new APIException(errorMessageUtils.getMessageWithCode("api.error.product.not.exist",
						new Object[] { userEventRequest.getProductID() }, "api.error.product.not.exist.code"));
			}

			String productId = inventory.getProductId();
			if (this.defaultProductTitle.equalsIgnoreCase(inventory.getProductName())
					&& inventory.getParentProduct() != null) {
				productId = inventory.getParentProduct().getProductId();
			}
			userEventRequest.setProductID(productId);
		} else {
			userEventRequest.setProductID(null);
		}

		List<UserEvents> pageURLRequiredEvents = List.of(UserEvents.NAV, UserEvents.DISCOVER, UserEvents.BACK_NAV);

		if (pageURLRequiredEvents.contains(userEventRequest.getEventID())) {
			if (StringUtils.isBlank(userEventRequest.getPageURL())) {
				log.error("Empty Page URL");
				throw new APIException(errorMessageUtils.getMessageWithCode("api.error.pageURL.null", new Object[] {},
						"api.error.pageURL.null.code"));
			}
		}

		if (UserEvents.VISIT_BLOG_POST.equals(userEventRequest.getEventID())) {
			if (StringUtils.isEmpty(userEventRequest.getPostId())) {
				log.error("PostId is Empty");
				throw new APIException(errorMessageUtils.getMessageWithCode("api.error.postID.null", new Object[] {},
						"api.error.postID.null.code"));
			}

			if (StringUtils.isEmpty(userEventRequest.getPostTitle())) {
				log.error("PostTitle is Empty");
				throw new APIException(errorMessageUtils.getMessageWithCode("api.error.post-title.null",
						new Object[] {}, "api.error.post-title.null.code"));
			}
		} else {
			userEventRequest.setPostId(null);
			userEventRequest.setPostTitle(null);
		}

		if (UserEvents.MENU.equals(userEventRequest.getEventID())) {
			userEventRequest.setPageURL(null);
			if (StringUtils.isEmpty(userEventRequest.getMenuName())) {
				log.error("MenuName is Empty");
				throw new APIException(errorMessageUtils.getMessageWithCode("api.error.menuName.null", new Object[] {},
						"api.error.menuName.null.code"));
			}

//			if (StringUtils.isEmpty(userEventRequest.getMenuId())) {
//				log.error("MenuId is Empty");
//				throw new APIException(errorMessageUtils.getMessageWithCode("api.error.menuID.null", new Object[] {},
//						"api.error.menuID.null.code"));
//			}

			if (StringUtils.isEmpty(userEventRequest.getMenuURL())) {
				log.error("Menu Url is Empty");
				throw new APIException(errorMessageUtils.getMessageWithCode("api.error.menuURL.null", new Object[] {},
						"api.error.menuURL.null.code"));
			}
		}

		if (UserEvents.VISIT_SOCIAL_MEDIA.equals(userEventRequest.getEventID())) {
			if (StringUtils.isEmpty(userEventRequest.getSocialMediaPlateForm())) {
				log.error("SocialMediaPlateForm is Empty");
				throw new APIException(errorMessageUtils.getMessageWithCode("api.error.social-media-platform.null",
						new Object[] {}, "api.error.social-media-platform.null.code"));
			}

			if (StringUtils.isEmpty(userEventRequest.getSocialMediaURL())) {
				log.error("SocialMediaURL is Empty");
				throw new APIException(errorMessageUtils.getMessageWithCode("api.error.social-media-url.null",
						new Object[] {}, "api.error.social-media-url.null.code"));
			}
		} else {
			userEventRequest.setSocialMediaPlateForm(null);
			userEventRequest.setSocialMediaURL(null);
		}

		if (UserEvents.TEXT_HIGHLIGHT.equals(userEventRequest.getEventID())) {
			if (StringUtils.isBlank(userEventRequest.getHighlightedText())) {
				log.error("HighLight text is Empty");
				throw new APIException(errorMessageUtils.getMessageWithCode("api.error.highlighted-text.null",
						new Object[] {}, "api.error.highlighted-text.null.code"));
			}
		}

		if (UserEvents.DOUBLE_CLICK.equals(userEventRequest.getEventID())) {
			if (StringUtils.isBlank(userEventRequest.getSelectedElement())) {
				log.error("Selected element is Empty");
				throw new APIException(errorMessageUtils.getMessageWithCode("api.error.selectedt-element-text.null",
						new Object[] {}, "api.error.selectedt-element-text.null.code"));
			}
		}

		if (UserEvents.IMG_VIEW.equals(userEventRequest.getEventID())) {
			if (StringUtils.isBlank(userEventRequest.getProductImageURL())) {
				log.error("product image url is null.");
				throw new APIException(errorMessageUtils.getMessageWithCode("api.error.product-image-url.null",
						new Object[] {}, "api.error.product-image-url.null.code"));
			}
		}

		UserEvent userEvent = new UserEvent(userEventRequest, currentTimestemp);
		this.userEventRepository.save(userEvent);
		log.info("User Session Info Saved: {}", userEventRequest.toString());
		return userEvent;
	}

	@Override
	public PageLoadData savePageLoadData(PageLoadEventRequest request) {
		this.userSessionService.validateSession(request.getSessionID());
		PageLoadData data = new PageLoadData(request, DateUtils.getCurrentTimestemp());
		data = this.pageLoadDataRepository.save(data);
		esPageLoadDataRespository.indexPageLoad(data.getId());
		return data;
	}
}