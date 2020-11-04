import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './usluga.reducer';
import { IUsluga } from 'app/shared/model/usluga.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IUslugaDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const UslugaDetail = (props: IUslugaDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { uslugaEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="popravljanjeApp.usluga.detail.title">Usluga</Translate> [<b>{uslugaEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="cijena">
              <Translate contentKey="popravljanjeApp.usluga.cijena">Cijena</Translate>
            </span>
          </dt>
          <dd>{uslugaEntity.cijena}</dd>
        </dl>
        <Button tag={Link} to="/usluga" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/usluga/${uslugaEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ usluga }: IRootState) => ({
  uslugaEntity: usluga.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(UslugaDetail);
