import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './poruka.reducer';
import { IPoruka } from 'app/shared/model/poruka.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPorukaDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const PorukaDetail = (props: IPorukaDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { porukaEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="popravljanjeApp.poruka.detail.title">Poruka</Translate> [<b>{porukaEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="text">
              <Translate contentKey="popravljanjeApp.poruka.text">Text</Translate>
            </span>
          </dt>
          <dd>{porukaEntity.text}</dd>
          <dt>
            <span id="datum">
              <Translate contentKey="popravljanjeApp.poruka.datum">Datum</Translate>
            </span>
          </dt>
          <dd>{porukaEntity.datum ? <TextFormat value={porukaEntity.datum} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="postoji">
              <Translate contentKey="popravljanjeApp.poruka.postoji">Postoji</Translate>
            </span>
          </dt>
          <dd>{porukaEntity.postoji ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="popravljanjeApp.poruka.dodatniInfoUser">Dodatni Info User</Translate>
          </dt>
          <dd>{porukaEntity.dodatniInfoUser ? porukaEntity.dodatniInfoUser.id : ''}</dd>
          <dt>
            <Translate contentKey="popravljanjeApp.poruka.chat">Chat</Translate>
          </dt>
          <dd>{porukaEntity.chat ? porukaEntity.chat.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/poruka" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/poruka/${porukaEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ poruka }: IRootState) => ({
  porukaEntity: poruka.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(PorukaDetail);
