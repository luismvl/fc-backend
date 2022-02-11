package com.example.firstcommit.specification;

import com.example.firstcommit.entities.Candidate;
import com.example.firstcommit.entities.Tag;
import com.example.firstcommit.entities.User;
import com.example.firstcommit.utils.Modality;
import com.example.firstcommit.utils.SearchCriteria;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.*;
import java.util.stream.Collectors;

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
                    root.<String>get(criteria.getKey()), criteria.getValue().toString());
        }
        if (criteria.getOperation().equalsIgnoreCase("<")) {
            return builder.lessThanOrEqualTo(
                    root.<String>get(criteria.getKey()), criteria.getValue().toString());
        }
        if (criteria.getOperation().equalsIgnoreCase(":")) {
//            Si el atributo es un enum
            if (root.get(criteria.getKey()).getJavaType() == Modality.class) {
                return builder.equal(root.<String>get(criteria.getKey()), Modality.valueOf((String) criteria.getValue()));
            }
//             Si el atributo es un String
            if (root.get(criteria.getKey()).getJavaType() == String.class) {
                return builder.like(builder.lower(root.<String>get(criteria.getKey())), "%" + criteria.getValue().toString().toLowerCase() + "%");
            }
//            Si el atributo es 'tags'
            if (criteria.getKey().equals("tags")) {
                query.distinct(true);
                // Paso el string de la query a un Set de ids
                Set<Long> ids = Arrays.stream(criteria.getValue().toString()
                                .replace("[", "").replace("]", "").split(","))
                        .map(Long::parseLong).collect(Collectors.toSet());

//                Si lo hago de esta manera cuando filtro por las tags 1,2,3 me trae candidatos que pueden no tener alguna de estas tags
//                Join<Candidate, Tag> join = root.join("tags");
//                return builder.and(join.get("id").in(ids));

                List<Predicate> predicates = new ArrayList<Predicate>();
                for (Long id : ids) {
                    Join<Candidate, Tag> join = root.join("tags");
                    Predicate p = builder.equal(join.get("id"), id);
                    predicates.add(p);
                }
                return builder.and(predicates.toArray(new Predicate[0]));
            }
//            Si el atributo es 'user'
            if (criteria.getKey().equals("user")) {
                Root<User> user = query.from(User.class);
                Expression<Set<Candidate>> userCandidates = user.get("candidates");
                return builder.and(builder.equal(user.get("username"), criteria.getValue()),
                        builder.isMember(root, userCandidates));
            }
            if(root.get(criteria.getKey()).getJavaType() == Boolean.class) {
                return builder.equal(root.get(criteria.getKey()), Boolean.valueOf((String) criteria.getValue()));

            }
            return builder.equal(root.get(criteria.getKey()), criteria.getValue());

        }
        return null;
    }
}
