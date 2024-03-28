package com.humaine.portal.api.request.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.humaine.portal.api.annotation.FieldValueExists;
import com.humaine.portal.api.rest.repository.AgeGroupRepository;
import com.humaine.portal.api.rest.repository.BigFiveRepository;
import com.humaine.portal.api.rest.repository.BuyingRepository;
import com.humaine.portal.api.rest.repository.EducationRepository;
import com.humaine.portal.api.rest.repository.EthnicityRepository;
import com.humaine.portal.api.rest.repository.FamilySizeRepository;
import com.humaine.portal.api.rest.repository.GenderRepository;
import com.humaine.portal.api.rest.repository.PersuasiveRepository;
import com.humaine.portal.api.rest.repository.ValuesRepository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserGroupRequest {

	@NotBlank(message = "{api.error.user-group.name.null}{error.code.separator}{api.error.user-group.name.null.code}")
	String name;

	@FieldValueExists(repository = GenderRepository.class, requiredMsg = "api.error.gender.null", requiredMsgErorCode = "api.error.gender.null.code", message = "api.error.gender.not.found", messageCode = "api.error.gender.not.found.code", required = false)
	Long gender;

	@FieldValueExists(repository = AgeGroupRepository.class, requiredMsg = "api.error.age-group.null", requiredMsgErorCode = "api.error.age-group.null.code", message = "api.error.age-group.not.found", messageCode = "api.error.age-group.not.found.code")
	Long ageGroup;

	@NotBlank(message = "{api.error.state.null}{error.code.separator}{api.error.state.null.code}")
	String state;

	@FieldValueExists(repository = EthnicityRepository.class, requiredMsg = "api.error.ethnicity.null", requiredMsgErorCode = "api.error.ethnicity.null.code", message = "api.error.ethnicity.not.found", messageCode = "api.error.ethnicity.not.found.code", required = false)
	Long ethnicity;

	@FieldValueExists(repository = FamilySizeRepository.class, requiredMsg = "api.error.family-size.null", requiredMsgErorCode = "api.error.family-size.null.code", message = "api.error.family-size.not.found", messageCode = "api.error.family-size.not.found.code")
	Long familySize;

	@NotNull(message = "{api.error.is-external.null}{error.code.separator}{api.error.is-external.null.code}")
	Boolean isExternalFactor;

	@FieldValueExists(repository = BigFiveRepository.class, requiredMsg = "api.error.big-five.null", requiredMsgErorCode = "api.error.big-five.null.code", message = "api.error.big-five.not.found", messageCode = "api.error.big-five.not.found.code")
	Long bigFive;

	@FieldValueExists(repository = ValuesRepository.class, requiredMsg = "api.error.values.null", requiredMsgErorCode = "api.error.values.null.code", message = "api.error.values.not.found", messageCode = "api.error.values.not.found.code")
	Long values;

	@FieldValueExists(repository = PersuasiveRepository.class, requiredMsg = "api.error.persuasive-stratergies.null", requiredMsgErorCode = "api.error.persuasive-stratergies.null.code", message = "api.error.persuasive-stratergies.not.found", messageCode = "api.error.persuasive-stratergies.not.found.code")
	Long persuasiveStratergies;

	@FieldValueExists(repository = BuyingRepository.class, requiredMsg = "api.error.buying.null", requiredMsgErorCode = "api.error.buying.null.code", message = "api.error.buying.not.found", messageCode = "api.error.buying.not.found.code")
	Long motivationToBuy;

	@FieldValueExists(repository = EducationRepository.class, requiredMsg = "api.error.educatioin.null", requiredMsgErorCode = "api.error.educatioin.null.code", message = "api.error.educatioin.not.found", messageCode = "api.error.educatioin.not.found.code")
	Long education;
}
