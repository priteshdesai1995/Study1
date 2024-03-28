package com.base.api.request.dto;

import java.time.OffsetDateTime;

import com.base.api.activity.log.ActivityLogStatus;
import com.base.api.utils.Util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString
public class ActivityLogObject {
	private String username;

	private String action;

	@Builder.Default
	private OffsetDateTime time = Util.getCurrentTimestamp();

	private ActivityLogStatus status;

	private String ip;
}
