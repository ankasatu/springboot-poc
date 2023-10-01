package com.tofu.demo.service.label;

import com.tofu.demo.Utils;
import com.tofu.demo.exception.ValidationException;
import com.tofu.demo.model.Label;
import com.tofu.demo.repository.LabelRepository;
import com.tofu.demo.service.dto.LabelRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class LabelServiceImplTest {
    @Mock
    private LabelRepository labelRepository;

    LabelServiceImpl labelService;
    Label label;
    @BeforeEach
    private void beforeEach() {
        MockitoAnnotations.openMocks(this);

        label = Label.builder()
                .id(UUID.randomUUID().toString())
                .name("Hello World")
                .build();

        labelService = new LabelServiceImpl(labelRepository);
    }

    @Test
    void shouldCreateProperly() {

        LabelRequest request = LabelRequest.builder()
                .name("test")
                .build();

        var result = labelService.create(request);

        assertTrue(Utils.matchUuidFormat.matches(result));
        verify(labelRepository, atLeastOnce()).save(any());
    }

    @Nested
    class Edit {
        @Test
        void shouldEditProperly() {
            when(labelRepository.findById(any())).thenReturn(Optional.of(label));

            labelService.edit(label.getId(), LabelRequest.builder().build());

            verify(labelRepository, atLeastOnce()).findById(any());
            verify(labelRepository, atLeastOnce()).save(any());
        }

        @Test
        void shouldThrow() {
            when(labelRepository.findById(any())).thenReturn(Optional.empty());

            assertThrows(ValidationException.class, () -> labelService.edit(label.getId(), LabelRequest.builder().build()));

            verify(labelRepository, atLeastOnce()).findById(any());
            verify(labelRepository, never()).save(any());
        }
    }

    @Nested
    class Delete {
        @Test
        void shouldDeleteProperly() {
            when(labelRepository.findById(any())).thenReturn(Optional.of(label));

            labelService.delete(label.getId());

            verify(labelRepository, atLeastOnce()).findById(any());
            verify(labelRepository, atLeastOnce()).delete(any());
        }

        @Test
        void shouldThrow() {
            when(labelRepository.findById(any())).thenReturn(Optional.empty());

            assertThrows(ValidationException.class, () -> labelService.delete(label.getId()));

            verify(labelRepository, atLeastOnce()).findById(any());
            verify(labelRepository, never()).delete(any());
        }
    }

    @Nested
    class GetLabelByIds {
        List<String> ids = Arrays.asList("a", "b");

        @Test
        void shouldRunProperly() {
            when(labelRepository.findAllById(any())).thenReturn(ids.stream().map(p->Label.builder().id(p).build()).toList());
            var result = labelService.getLabelByIds(ids);
            assertEquals(ids.size(), result.size());
        }

        @Test
        void shouldThrowIfEmptyArgs() {

            when(labelRepository.findAllById(any())).thenReturn(Arrays.asList());

            assertThrows(ValidationException.class, () -> labelService.getLabelByIds(ids));
            verify(labelRepository, atLeastOnce()).findAllById(any());
        }

        @Test
        void shouldThrowIfDiffArrSize() {
            when(labelRepository.findAllById(any())).thenReturn(Arrays.asList(Label.builder().build()));

            assertThrows(ValidationException.class, () -> labelService.getLabelByIds(ids));
            verify(labelRepository, atLeastOnce()).findAllById(any());
        }
    }
}
