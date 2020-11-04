import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './kanton.reducer';
import { IKanton } from 'app/shared/model/kanton.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IKantonDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const KantonDetail = (props: IKantonDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { kantonEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="popravljanjeApp.kanton.detail.title">Kanton</Translate> [<b>{kantonEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="imeKantona">
              <Translate contentKey="popravljanjeApp.kanton.imeKantona">Ime Kantona</Translate>
            </span>
          </dt>
          <dd>{kantonEntity.imeKantona}</dd>
          <dt>
            <Translate contentKey="popravljanjeApp.kanton.entitet">Entitet</Translate>
          </dt>
          <dd>{kantonEntity.entitet ? kantonEntity.entitet.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/kanton" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/kanton/${kantonEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ kanton }: IRootState) => ({
  kantonEntity: kanton.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(KantonDetail);
