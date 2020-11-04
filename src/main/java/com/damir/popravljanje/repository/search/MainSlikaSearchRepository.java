package com.damir.popravljanje.repository.search;

import com.damir.popravljanje.domain.MainSlika;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link MainSlika} entity.
 */
public interface MainSlikaSearchRepository extends ElasticsearchRepository<MainSlika, Long> {
}
