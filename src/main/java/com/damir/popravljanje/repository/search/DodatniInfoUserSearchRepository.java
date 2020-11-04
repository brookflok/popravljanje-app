package com.damir.popravljanje.repository.search;

import com.damir.popravljanje.domain.DodatniInfoUser;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link DodatniInfoUser} entity.
 */
public interface DodatniInfoUserSearchRepository extends ElasticsearchRepository<DodatniInfoUser, Long> {
}
