package com.humaine.portal.api.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AvatarImageUtils {

	@Value("${no.of.avatar.images}")
	private Integer noOfAvtarImages;

	@Value("${avatar.images.extension}")
	private String avatarImageExtension = ".png";

	public String getRandomAvatar() {
		int randomNumber = getRandomInteger(noOfAvtarImages, 1);
		return randomNumber + avatarImageExtension;
	}

	private int getRandomInteger(int maximum, int minimum) {
		return ((int) (Math.random() * (maximum - minimum))) + minimum;
	}
}
