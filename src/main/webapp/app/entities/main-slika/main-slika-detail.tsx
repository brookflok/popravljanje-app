import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './main-slika.reducer';
import { IMainSlika } from 'app/shared/model/main-slika.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IMainSlikaDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const MainSlikaDetail = (props: IMainSlikaDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { mainSlikaEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="popravljanjeApp.mainSlika.detail.title">MainSlika</Translate> [<b>{mainSlikaEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="ime">
              <Translate contentKey="popravljanjeApp.mainSlika.ime">Ime</Translate>
            </span>
          </dt>
          <dd>{mainSlikaEntity.ime}</dd>
          <dt>
            <span id="datum">
              <Translate contentKey="popravljanjeApp.mainSlika.datum">Datum</Translate>
            </span>
          </dt>
          <dd>{mainSlikaEntity.datum ? <TextFormat value={mainSlikaEntity.datum} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <Translate contentKey="popravljanjeApp.mainSlika.artikl">Artikl</Translate>
          </dt>
          <dd>{mainSlikaEntity.artikl ? mainSlikaEntity.artikl.ime : ''}</dd>
        </dl>
        <Button tag={Link} to="/main-slika" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/main-slika/${mainSlikaEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ mainSlika }: IRootState) => ({
  mainSlikaEntity: mainSlika.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(MainSlikaDetail);
