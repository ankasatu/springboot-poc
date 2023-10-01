package com.tofu.demo.service.label;

import com.tofu.demo.model.Label;
import com.tofu.demo.service.dto.LabelRequest;
import com.tofu.demo.service.dto.LabelResponse;

import java.util.List;

public interface LabelService {
    String create(LabelRequest request);
    List<LabelResponse> getList();
    void edit(String id, LabelRequest request);
    void delete(String id);

    List<Label> getLabelByIds(List<String> labelIds);
}
