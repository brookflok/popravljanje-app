import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './dodatni-info-user.reducer';
import { IDodatniInfoUser } from 'app/shared/model/dodatni-info-user.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IDodatniInfoUserDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const DodatniInfoUserDetail = (props: IDodatniInfoUserDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { dodatniInfoUserEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="popravljanjeApp.dodatniInfoUser.detail.title">DodatniInfoUser</Translate> [
          <b>{dodatniInfoUserEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="korisnickoime">
              <Translate contentKey="popravljanjeApp.dodatniInfoUser.korisnickoime">Korisnickoime</Translate>
            </span>
          </dt>
          <dd>{dodatniInfoUserEntity.korisnickoime}</dd>
          <dt>
            <span id="brojTelefona">
              <Translate contentKey="popravljanjeApp.dodatniInfoUser.brojTelefona">Broj Telefona</Translate>
            </span>
          </dt>
          <dd>{dodatniInfoUserEntity.brojTelefona}</dd>
          <dt>
            <span id="majstor">
              <Translate contentKey="popravljanjeApp.dodatniInfoUser.majstor">Majstor</Translate>
            </span>
          </dt>
          <dd>{dodatniInfoUserEntity.majstor ? 'true' : 'false'}</dd>
          <dt>
            <span id="postoji">
              <Translate contentKey="popravljanjeApp.dodatniInfoUser.postoji">Postoji</Translate>
            </span>
          </dt>
          <dd>{dodatniInfoUserEntity.postoji ? 'true' : 'false'}</dd>
          <dt>
            <span id="detaljniOpis">
              <Translate contentKey="popravljanjeApp.dodatniInfoUser.detaljniOpis">Detaljni Opis</Translate>
            </span>
          </dt>
          <dd>{dodatniInfoUserEntity.detaljniOpis}</dd>
          <dt>
            <Translate contentKey="popravljanjeApp.dodatniInfoUser.lokacija">Lokacija</Translate>
          </dt>
          <dd>{dodatniInfoUserEntity.lokacija ? dodatniInfoUserEntity.lokacija.id : ''}</dd>
          <dt>
            <Translate contentKey="popravljanjeApp.dodatniInfoUser.profilnaSlika">Profilna Slika</Translate>
          </dt>
          <dd>{dodatniInfoUserEntity.profilnaSlika ? dodatniInfoUserEntity.profilnaSlika.id : ''}</dd>
          <dt>
            <Translate contentKey="popravljanjeApp.dodatniInfoUser.user">User</Translate>
          </dt>
          <dd>{dodatniInfoUserEntity.user ? dodatniInfoUserEntity.user.id : ''}</dd>
          <dt>
            <Translate contentKey="popravljanjeApp.dodatniInfoUser.ucesnici">Ucesnici</Translate>
          </dt>
          <dd>{dodatniInfoUserEntity.ucesnici ? dodatniInfoUserEntity.ucesnici.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/dodatni-info-user" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/dodatni-info-user/${dodatniInfoUserEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ dodatniInfoUser }: IRootState) => ({
  dodatniInfoUserEntity: dodatniInfoUser.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(DodatniInfoUserDetail);
