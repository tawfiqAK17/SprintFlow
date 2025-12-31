package com.ensa.SprintFlow.security.resourceHierarchyValidation;

import com.ensa.SprintFlow.security.resourceHierarchyValidation.resourceHierarchyValidationStrategy.ResourceHierarchyValidationStrategy;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ResourceHierarchyValidator {
  List<ResourceHierarchyValidationStrategy> validationStrategies;

  public void validate(String url) {
    for (var strategy : validationStrategies) {
      if (strategy.supports(url)) {
        strategy.validate(url);
        return;
      }
    }
  }
}
