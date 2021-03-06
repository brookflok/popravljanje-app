import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './galerija.reducer';
import { IGalerija } from 'app/shared/model/galerija.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IGalerijaDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const GalerijaDetail = (props: IGalerijaDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { galerijaEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="popravljanjeApp.galerija.detail.title">Galerija</Translate> [<b>{galerijaEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="ime">
              <Translate contentKey="popravljanjeApp.galerija.ime">Ime</Translate>
            </span>
          </dt>
          <dd>{galerijaEntity.ime}</dd>
          <dt>
            <span id="datum">
              <Translate contentKey="popravljanjeApp.galerija.datum">Datum</Translate>
            </span>
          </dt>
          <dd>{galerijaEntity.datum ? <TextFormat value={galerijaEntity.datum} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <Translate contentKey="popravljanjeApp.galerija.artikl">Artikl</Translate>
          </dt>
          <dd>{galerijaEntity.artikl ? galerijaEntity.artikl.ime : ''}</dd>
        </dl>
        <Button tag={Link} to="/galerija" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/galerija/${galerijaEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ galerija }: IRootState) => ({
  galerijaEntity: galerija.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(GalerijaDetail);
