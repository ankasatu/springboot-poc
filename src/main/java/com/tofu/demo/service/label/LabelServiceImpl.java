package com.tofu.demo.service.label;

import com.tofu.demo.exception.ValidationException;
import com.tofu.demo.model.Label;
import com.tofu.demo.repository.LabelRepository;
import com.tofu.demo.service.dto.LabelRequest;
import com.tofu.demo.service.dto.LabelResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class LabelServiceImpl implements LabelService {

    @Autowired
    private LabelRepository labelRepository;

    @Override
    public String create(LabelRequest request) {
        var label = Label.builder()
                .id(UUID.randomUUID().toString())
                .name(request.getName())
                .description(request.getDescription())
                .build();

        labelRepository.save(label);

        return label.getId();
    }

    @Override
    public List<LabelResponse> getList() {
        return labelRepository.findAll().stream().map(p->LabelResponse.builder()
                    .id(p.getId())
                    .name(p.getName())
                    .description(p.getDescription())
                .build()).toList();
    }

    @Override
    public void edit(String id, LabelRequest request) {
        var label = labelRepository.findById(id)
                .orElseThrow(() -> new ValidationException("label id tidak ditemukan"));

        if (StringUtils.hasText(request.getName())) {
            label.setName(request.getName());
        }

        label.setDescription(request.getDescription());

        labelRepository.save(label);
    }

    @Override
    public void delete(String id) {
        var label = labelRepository.findById(id)
                .orElseThrow(() -> new ValidationException("label id tidak ditemukan"));

        labelRepository.delete(label);
    }

    @Override
    public List<Label> getLabelByIds(List<String> labelIds) {
        List<Label> labels = labelRepository.findAllById(labelIds);

        if (labels.isEmpty()) {
            throw new ValidationException("label kosong");
        }

        boolean diffLabelSize = labels.size() != labelIds.size();
        if (diffLabelSize) {

            Set<String> setA = labels.stream().map(p->p.getId()).collect(Collectors.toSet());
            List<String> ids = labelIds.stream().filter(setA::contains).toList();

            throw new ValidationException("diff size ?? " + "".join(",", ids));
        }

        return labels;
    }
}
