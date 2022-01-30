package com.example.firstcommit.specification;

import com.example.firstcommit.entities.Candidate;
import com.example.firstcommit.entities.Tag;
import com.example.firstcommit.entities.User;
import com.example.firstcommit.utils.SearchCriteria;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.Set;

public class CandidateSpecification implements Specification<Candidate> {

    private final SearchCriteria criteria;

    public CandidateSpecification(final SearchCriteria criteria) {
        super();
        this.criteria = criteria;
    }


    @Override
    public Predicate toPredicate(Root<Candidate> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        if (criteria.getOperation().equalsIgnoreCase(">")) {
            return builder.greaterThanOrEqualTo(
                    root.<String> get(criteria.getKey()), criteria.getValue().toString());
        }
        if (criteria.getOperation().equalsIgnoreCase("<")) {
            return builder.lessThanOrEqualTo(
                    root.<String> get(criteria.getKey()), criteria.getValue().toString());
        }
        if (criteria.getOperation().equalsIgnoreCase(":")) {
            if (root.get(criteria.getKey()).getJavaType() == String.class) {
                return builder.like(
                        root.<String>get(criteria.getKey()), "%" + criteria.getValue() + "%");
            } else if (criteria.getKey().equals("tags")) {
                query.distinct(true);
                Root<Tag> tag = query.from(Tag.class);
                Expression<Set<Candidate>> tagCandidates = tag.get("candidates");
                return builder.and(builder.equal(tag.get("name"), criteria.getValue().toString().toUpperCase()),
                        builder.isMember(root, tagCandidates));
            } else if(criteria.getKey().equals("user")) {
                Root<User> user = query.from(User.class);
                Expression<Set<Candidate>> userCandidates = user.get("candidates");
                return  builder.and(builder.equal(user.get("username"), criteria.getValue()),
                        builder.isMember(root, userCandidates));
            } else {
                return builder.equal(root.get(criteria.getKey()), criteria.getValue());
            }
        }
        return null;
    }
}
