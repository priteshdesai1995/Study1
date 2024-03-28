package com.base.api.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.base.api.dto.filter.SuggestionFilter;
import com.base.api.entities.Suggestion;
import com.base.api.request.dto.SuggestionDTO;
import com.base.api.request.dto.SuggestionRequest;

public interface SuggestionService {

	Suggestion addSuggestions(SuggestionRequest suggestionFilter);

	List<SuggestionDTO> getAllSuggestions(SuggestionFilter suggestionFilter);

	ByteArrayInputStream writeExcel(List<Suggestion> suggestionDetails, String filename);

	InputStream load(List<Suggestion> entities);

	Suggestion findBySuggestionId(UUID suggestionId, Map<String, String> statusReq);
}
