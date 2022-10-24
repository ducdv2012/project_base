package com.project.base.repository;

import com.project.base.api.response.ListResponse;
import com.project.base.model.Users;
import com.project.base.response.UserListResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CustomUserRepository {
    private final EntityManager entityManager;
    private final ModelMapper modelMapper;

    @SuppressWarnings("unchecked")
    public ListResponse findAllWithSearch(String search, Long pageNo, Long pageSize) {
        if (pageNo > 0) pageNo = pageNo - 1;
        StringBuilder sql = new StringBuilder();
        StringBuilder sqlCount = new StringBuilder();
        sqlCount.append("SELECT COUNT(*) FROM users u ");
        sql.append("SELECT * FROM users u ");
        sqlCount.append("WHERE 1 =1 ");
        sql.append("WHERE 1 =1 ");

        if (!StringUtils.isEmpty(search)) {
            sqlCount.append(" AND u.username LIKE :username ");
            sql.append(" AND u.username LIKE :username ");
        }

        Query queryCount = entityManager.createNativeQuery(sqlCount.toString());
        Query query = entityManager.createNativeQuery(sql.toString(), Users.class);
        if (!StringUtils.isEmpty(search)) {
            queryCount.setParameter("username", "%" + search + "%");
            query.setParameter("username", "%" + search + "%");
        }

        query.setFirstResult(Math.toIntExact(pageNo * pageSize));
        query.setMaxResults(Math.toIntExact(pageSize));
        long count = queryCount.getSingleResult() == null ? 0 : Long.parseLong(queryCount.getSingleResult().toString());
        List<Users> resultList = query.getResultList();
        List<UserListResponse> list = resultList.stream().map(item -> modelMapper.map(item, UserListResponse.class)).collect(Collectors.toList());
        return ListResponse.builder()
                .totalElements(count)
                .data(list)
                .build();
    }
}
