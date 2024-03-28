package com.humaine.admin.api.log;

import java.time.OffsetDateTime;

import com.humaine.portal.api.util.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ActivityLogObject {
	private String username;

	private String action;

	@Builder.Default
	private OffsetDateTime time = DateUtils.getCurrentTimestemp();

	private ActivityLogStatus status;

	private String ip;
}
