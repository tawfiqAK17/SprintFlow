package com.ensa.SprintFlow.mapper;

import com.ensa.SprintFlow.dto.acceptanceCriteria.AcceptanceCriteriaDto;
import com.ensa.SprintFlow.model.AcceptanceCriteria;
import com.ensa.SprintFlow.model.And;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface AcceptanceCriteriaMapper {

    @Mapping(target = "givenWhat", source = "given")
    @Mapping(target = "whenWhat", source = "when")
    @Mapping(target = "ands", source = "ands", qualifiedByName = "stringsToAnds")
    @Mapping(target = "thenWhat", source = "then")
    AcceptanceCriteria mapToAcceptanceCriteria(AcceptanceCriteriaDto dto);

    @Mapping(target = "given", source = "givenWhat")
    @Mapping(target = "when", source = "whenWhat")
    @Mapping(target = "ands", source = "ands", qualifiedByName = "andsToStrings")
    @Mapping(target = "then", source = "thenWhat")
    AcceptanceCriteriaDto mapToAcceptanceCriteriaDto(AcceptanceCriteria acceptanceCriteria);

    List<AcceptanceCriteriaDto> mapToAcceptanceCriteriaDto(List<AcceptanceCriteria> criteriaList);

    @Named("stringsToAnds")
    default Set<And> stringsToAnds(Set<String> strings) {
        if (strings == null) return null;
        return strings.stream()
            .map(s -> And.builder().andWhat(s).build())
            .collect(Collectors.toSet());
    }

    @Named("andsToStrings")
    default Set<String> andsToStrings(Set<And> ands) {
        if (ands == null) return null;
        return ands.stream()
            .map(And::getAndWhat)
            .collect(Collectors.toSet());
    }
}
