package com.humaine.portal.api.response.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.humaine.portal.api.model.AIUserGroup;
import com.humaine.portal.api.model.BigFive;
import com.humaine.portal.api.model.PersonaDetailsMaster;
import com.humaine.portal.api.model.UserGroup;
import com.humaine.portal.api.olap.model.OLAPUserGroup;
import com.humaine.portal.api.util.CommonUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PersonaDetails {

	List<String> personality;

	List<String> persuasiveStrategies;

	List<String> values;

	List<String> goals;

	List<String> motivationToBuy;

	List<String> frustrations;

	public PersonaDetails(UserGroup group) {
		if (group != null) {
			this.personality = new ArrayList<>();
			personality.addAll(group.getBigFive().getPersonalities());
			this.persuasiveStrategies = Arrays.asList(group.getPersuasive().getValue());
			this.values = Arrays.asList(group.getValues().getValue());
			this.goals = new ArrayList<>();
			this.goals.addAll(group.getBigFive().getGoals());
			this.frustrations = new ArrayList<>();
			this.frustrations.addAll(group.getBigFive().getFrustrations());
			this.motivationToBuy = Arrays.asList(group.getBuying().getValue());
		}
	}

	public PersonaDetails(PersonaDetailsMaster pm) {
		if (pm != null) {
			this.personality = new ArrayList<>();
			personality.addAll(pm.getPersonalities());
			this.persuasiveStrategies = Arrays.asList(pm.getStrategies().getValue());
			this.values = Arrays.asList(pm.getValues().getValue());
			this.goals = new ArrayList<>();
			this.goals.addAll(pm.getGoals());
			this.frustrations = new ArrayList<>();
			this.frustrations.addAll(pm.getFrustrations());
			this.motivationToBuy = Arrays.asList(pm.getBuy().getValue());
		}
	}

	public PersonaDetails(OLAPUserGroup group, BigFive bigFive) {
		if (group != null) {
			this.personality = new ArrayList<>();
			this.personality.addAll(bigFive.getPersonalities());
			this.persuasiveStrategies = Arrays.asList(group.getPersuasiveStratergies());
			this.values = Arrays.asList(group.getValues());
			this.goals = new ArrayList<>();
			this.goals = new ArrayList<>();
			this.goals.addAll(bigFive.getGoals());
			this.frustrations = new ArrayList<>();
			this.frustrations.addAll(bigFive.getFrustrations());
			this.motivationToBuy = Arrays.asList(group.getMotivationToBuy());
		}
	}

	public PersonaDetails(OLAPUserGroup group, BigFive bigFive, PersonaDetailsMaster pm) {
		this.personality = new ArrayList<>();
		this.goals = new ArrayList<>();
		this.frustrations = new ArrayList<>();
		if (group != null) {

			this.persuasiveStrategies = Arrays.asList(group.getPersuasiveStratergies());
			this.values = Arrays.asList(group.getValues());
			this.motivationToBuy = Arrays.asList(group.getMotivationToBuy());
			if (pm != null) {
				this.goals.addAll(pm.getGoals());
				this.frustrations.addAll(pm.getFrustrations());
				this.personality.addAll(pm.getPersonalities());
			}
		}
	}

	public PersonaDetails(List<OLAPUserGroup> groups, BigFive bigFive) {
		if (groups != null) {
			this.personality = new ArrayList<>();
			this.personality.addAll(bigFive.getPersonalities());
			this.persuasiveStrategies = new ArrayList<>(
					groups.stream().map(e -> e.getPersuasiveStratergies()).collect(Collectors.toSet()));
			this.values = new ArrayList<>(groups.stream().map(e -> e.getValues()).collect(Collectors.toSet()));
			this.goals = new ArrayList<>();
			this.goals.addAll(bigFive.getGoals());
			this.frustrations = new ArrayList<>();
			this.frustrations.addAll(bigFive.getFrustrations());
			this.motivationToBuy = new ArrayList<>(
					groups.stream().map(e -> e.getMotivationToBuy()).collect(Collectors.toList()));
		}
	}

	public PersonaDetails(List<OLAPUserGroup> groups, BigFive bigFive,
			Map<String, PersonaDetailsMaster> personaDetailsMap) {
		if (groups != null) {
			this.personality = new ArrayList<>();
			this.persuasiveStrategies = new ArrayList<>(
					groups.stream().map(e -> e.getPersuasiveStratergies()).collect(Collectors.toSet()));
			this.values = new ArrayList<>(groups.stream().map(e -> e.getValues()).collect(Collectors.toSet()));
			this.goals = new ArrayList<>();
			this.frustrations = new ArrayList<>();
			this.motivationToBuy = new ArrayList<>(
					groups.stream().map(e -> e.getMotivationToBuy()).collect(Collectors.toList()));
			List<String> keys = groups.stream().map(e -> {
				return CommonUtils.generateUnique(e.getBigFive()) + "_"
						+ CommonUtils.generateUnique(e.getMotivationToBuy()) + "_"
						+ CommonUtils.generateUnique(e.getPersuasiveStratergies()) + "_"
						+ CommonUtils.generateUnique(e.getValues());
			}).collect(Collectors.toList());

			if (personaDetailsMap != null) {
				keys.forEach(k -> {
					if (personaDetailsMap.containsKey(k)) {
						personaDetailsMap.get(k).getPersonalities().forEach(p -> {
							if (!this.personality.contains(p)) {
								this.personality.add(p);
							}
						});
						personaDetailsMap.get(k).getGoals().forEach(g -> {
							if (!this.goals.contains(g)) {
								this.goals.add(g);
							}
						});
						personaDetailsMap.get(k).getFrustrations().forEach(f -> {
							if (!this.frustrations.contains(f)) {
								this.frustrations.add(f);
							}
						});
					}
				});
			}
		}
	}

	public PersonaDetails(AIUserGroup group, BigFive bigFive) {
		if (group != null) {
			this.personality = new ArrayList<>();
			this.personality.addAll(bigFive.getPersonalities());
			this.persuasiveStrategies = Arrays.asList(group.getPersuasiveStratergies());
			this.values = Arrays.asList(group.getValues());
			this.goals = new ArrayList<>();
			this.goals.addAll(bigFive.getGoals());
			this.frustrations = new ArrayList<>();
			this.frustrations.addAll(bigFive.getFrustrations());
			this.motivationToBuy = Arrays.asList(group.getMotivationToBuy());
		}
	}

	public PersonaDetails(AIUserGroup group, BigFive bigFive, PersonaDetailsMaster pm) {
		if (group != null) {
			this.personality = new ArrayList<>();
			this.persuasiveStrategies = Arrays.asList(group.getPersuasiveStratergies());
			this.motivationToBuy = Arrays.asList(group.getMotivationToBuy());
			this.values = Arrays.asList(group.getValues());
			this.goals = new ArrayList<>();

			this.frustrations = new ArrayList<>();
			if (pm != null) {
				this.personality.addAll(pm.getPersonalities());
				this.goals.addAll(pm.getGoals());
				this.frustrations.addAll(pm.getFrustrations());
			}
		}
	}
}
