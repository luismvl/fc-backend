package com.example.firstcommit.utils;

import com.example.firstcommit.entities.Candidate;
import com.example.firstcommit.specification.CandidateSpecification;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CandidateSpecificationsBuilder {

    private final List<SearchCriteria> params;

    public CandidateSpecificationsBuilder() {
        params = new ArrayList<SearchCriteria>();
    }

    public CandidateSpecificationsBuilder with(String key, String operation, Object value) {
        params.add(new SearchCriteria(key, operation, value));
        return this;
    }

    public Specification<Candidate> build() {
        if (params.size() == 0) {
            return null;
        }

        List<Specification<Candidate>> specs = params.stream()
                .map(CandidateSpecification::new)
                .collect(Collectors.toList());

        Specification<Candidate> result = specs.get(0);

        for (int i = 1; i < params.size(); i++) {
            result = params.get(i)
                    .isOrPredicate()
                    ? Specification.where(result)
                    .or(specs.get(i))
                    : Specification.where(result)
                    .and(specs.get(i));
        }
        return result;
    }
}
