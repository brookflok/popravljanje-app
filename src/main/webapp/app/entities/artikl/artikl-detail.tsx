import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './artikl.reducer';
import { IArtikl } from 'app/shared/model/artikl.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IArtiklDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ArtiklDetail = (props: IArtiklDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { artiklEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="popravljanjeApp.artikl.detail.title">Artikl</Translate> [<b>{artiklEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="ime">
              <Translate contentKey="popravljanjeApp.artikl.ime">Ime</Translate>
            </span>
          </dt>
          <dd>{artiklEntity.ime}</dd>
          <dt>
            <span id="kratkiOpis">
              <Translate contentKey="popravljanjeApp.artikl.kratkiOpis">Kratki Opis</Translate>
            </span>
          </dt>
          <dd>{artiklEntity.kratkiOpis}</dd>
          <dt>
            <span id="detaljniOpis">
              <Translate contentKey="popravljanjeApp.artikl.detaljniOpis">Detaljni Opis</Translate>
            </span>
          </dt>
          <dd>{artiklEntity.detaljniOpis}</dd>
          <dt>
            <span id="majstor">
              <Translate contentKey="popravljanjeApp.artikl.majstor">Majstor</Translate>
            </span>
          </dt>
          <dd>{artiklEntity.majstor ? 'true' : 'false'}</dd>
          <dt>
            <span id="postoji">
              <Translate contentKey="popravljanjeApp.artikl.postoji">Postoji</Translate>
            </span>
          </dt>
          <dd>{artiklEntity.postoji ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="popravljanjeApp.artikl.lokacija">Lokacija</Translate>
          </dt>
          <dd>{artiklEntity.lokacija ? artiklEntity.lokacija.id : ''}</dd>
          <dt>
            <Translate contentKey="popravljanjeApp.artikl.potreba">Potreba</Translate>
          </dt>
          <dd>{artiklEntity.potreba ? artiklEntity.potreba.id : ''}</dd>
          <dt>
            <Translate contentKey="popravljanjeApp.artikl.usluga">Usluga</Translate>
          </dt>
          <dd>{artiklEntity.usluga ? artiklEntity.usluga.id : ''}</dd>
          <dt>
            <Translate contentKey="popravljanjeApp.artikl.dodatniinfouser">Dodatniinfouser</Translate>
          </dt>
          <dd>{artiklEntity.dodatniinfouser ? artiklEntity.dodatniinfouser.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/artikl" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/artikl/${artiklEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ artikl }: IRootState) => ({
  artiklEntity: artikl.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ArtiklDetail);
