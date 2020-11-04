import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './javno-pitanje.reducer';
import { IJavnoPitanje } from 'app/shared/model/javno-pitanje.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IJavnoPitanjeDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const JavnoPitanjeDetail = (props: IJavnoPitanjeDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { javnoPitanjeEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="popravljanjeApp.javnoPitanje.detail.title">JavnoPitanje</Translate> [<b>{javnoPitanjeEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="pitanje">
              <Translate contentKey="popravljanjeApp.javnoPitanje.pitanje">Pitanje</Translate>
            </span>
          </dt>
          <dd>{javnoPitanjeEntity.pitanje}</dd>
          <dt>
            <span id="datum">
              <Translate contentKey="popravljanjeApp.javnoPitanje.datum">Datum</Translate>
            </span>
          </dt>
          <dd>{javnoPitanjeEntity.datum ? <TextFormat value={javnoPitanjeEntity.datum} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="prikaz">
              <Translate contentKey="popravljanjeApp.javnoPitanje.prikaz">Prikaz</Translate>
            </span>
          </dt>
          <dd>{javnoPitanjeEntity.prikaz ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="popravljanjeApp.javnoPitanje.dodatniinfoUser">Dodatniinfo User</Translate>
          </dt>
          <dd>{javnoPitanjeEntity.dodatniinfoUser ? javnoPitanjeEntity.dodatniinfoUser.id : ''}</dd>
          <dt>
            <Translate contentKey="popravljanjeApp.javnoPitanje.artikl">Artikl</Translate>
          </dt>
          <dd>{javnoPitanjeEntity.artikl ? javnoPitanjeEntity.artikl.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/javno-pitanje" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/javno-pitanje/${javnoPitanjeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ javnoPitanje }: IRootState) => ({
  javnoPitanjeEntity: javnoPitanje.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(JavnoPitanjeDetail);
